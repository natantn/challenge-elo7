package br.com.elo.challenge.planetexplorer.controllers;

import br.com.elo.challenge.planetexplorer.dtos.output.ExplorerInDetails;
import br.com.elo.challenge.planetexplorer.dtos.input.ExplorerLaunching;
import br.com.elo.challenge.planetexplorer.dtos.input.ExplorerMoving;
import br.com.elo.challenge.planetexplorer.dtos.input.ExplorerToCreate;
import br.com.elo.challenge.planetexplorer.dtos.output.ExplorerWithMessage;
import br.com.elo.challenge.planetexplorer.enums.ExplorerStatus;
import br.com.elo.challenge.planetexplorer.enums.Side;
import br.com.elo.challenge.planetexplorer.models.Explorer;
import br.com.elo.challenge.planetexplorer.repository.ExplorerRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/explorer")
public class ExplorerController {

    @Autowired
    private ExplorerRespository explorerRespository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> createExplorer(@RequestBody ExplorerToCreate body, UriComponentsBuilder uriBuilder) {
        Explorer probe = body.createExplorer();

        Explorer probeExists = explorerRespository.findExplorerBySlug(probe.getSlug());
        if(probeExists != null && !probeExists.getId().isEmpty()) {
            return ResponseEntity.badRequest().body(new ExplorerWithMessage(
                    "There is one explorer already created with this name. Please, try another one",
                    probeExists.showExplorerInDetails())
            );
        }

        explorerRespository.save(probe);

        URI uri = uriBuilder.path("/explorer/{slug}").buildAndExpand(probe.getSlug()).toUri();
        return ResponseEntity.created(uri).body(probe.showExplorerInDetails());
    }

    @GetMapping
    public List<ExplorerInDetails> listAllExplorers(String planet){
        List<Explorer> explorers = new ArrayList<>();
        if (planet == null) {
            explorers = explorerRespository.findAll();
        } else {

        }
        return ExplorerInDetails.parseExplorersList(explorers);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ExplorerInDetails> getAnExplorerBySlug(@PathVariable String slug){
        Explorer probe = explorerRespository.findExplorerBySlug(slug);
        return ResponseEntity.ok(probe.showExplorerInDetails());

    }

    @PutMapping("/{slug}/launch")
    @Transactional
    public ResponseEntity<?> launchExplorer(@PathVariable String slug, @RequestBody ExplorerLaunching body) {
        Explorer probe = explorerRespository.findExplorerBySlug(slug);
        if (probe.getStatus() == ExplorerStatus.ON_PLANET) {
            return ResponseEntity.badRequest().body(new ExplorerWithMessage(
                    "Explorer already on a planet",
                    probe.showExplorerInDetails())
            );
        }

        probe.LandAt(body.getPosX(), body.getPosY(), body.getOrientation());
        explorerRespository.save(probe);

        return  ResponseEntity.ok(probe.showExplorerInDetails());
    }

    @PutMapping("/{slug}/move")
    @Transactional
    public ResponseEntity<?> moveExplorer(@PathVariable String slug, @RequestBody ExplorerMoving body) {
        Explorer probe = explorerRespository.findExplorerBySlug(slug);
        if (probe.getStatus() == ExplorerStatus.AT_BASE) {
            return ResponseEntity.badRequest().body(new ExplorerWithMessage(
                    "Explorer at base. It's not possible to move it around",
                    probe.showExplorerInDetails())
            );
        }

        String[] movementSequence = body.getMovement().split("");
        for (String command: movementSequence) {
            switch (command){
                case "M":
                    probe.Move();
                    break;
                case "L":
                    probe.Turn(Side.Left);
                    break;
                case "R":
                    probe.Turn(Side.Right);
                    break;
                default:
                    break;
            }
        }
        explorerRespository.save(probe);

        return  ResponseEntity.ok(probe.showExplorerInDetails());
    }
}
