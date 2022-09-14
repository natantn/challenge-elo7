package br.com.elo.challenge.planetexplorer.controllers;

import br.com.elo.challenge.planetexplorer.dtos.output.ExplorerInDetails;
import br.com.elo.challenge.planetexplorer.dtos.input.ExplorerLaunching;
import br.com.elo.challenge.planetexplorer.dtos.input.ExplorerMoving;
import br.com.elo.challenge.planetexplorer.dtos.input.ExplorerToCreate;
import br.com.elo.challenge.planetexplorer.dtos.output.RegisterWithMessage;
import br.com.elo.challenge.planetexplorer.enums.Direction;
import br.com.elo.challenge.planetexplorer.enums.ExplorerStatus;
import br.com.elo.challenge.planetexplorer.enums.RegisterType;
import br.com.elo.challenge.planetexplorer.enums.Side;
import br.com.elo.challenge.planetexplorer.models.Explorer;
import br.com.elo.challenge.planetexplorer.models.Planet;
import br.com.elo.challenge.planetexplorer.repository.ExplorerRespository;
import br.com.elo.challenge.planetexplorer.repository.PlanetRepository;
import br.com.elo.challenge.planetexplorer.services.ExplorerService;
import br.com.elo.challenge.planetexplorer.services.PlanetService;
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
    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private ExplorerService explorerService;
    @Autowired
    private PlanetService planetService;

    @PostMapping
    @Transactional
    public ResponseEntity<?> createExplorer(@RequestBody ExplorerToCreate body, UriComponentsBuilder uriBuilder) {
        Explorer probe = body.createExplorer();

        Explorer probeExists = explorerRespository.findExplorerBySlug(probe.getSlug());
        if(probeExists != null && !probeExists.getId().isEmpty()) {
            return ResponseEntity.badRequest().body(new RegisterWithMessage(
                    "There is one explorer already created with this name. Please, try another one",
                    RegisterType.ERROR,
                    probeExists.showExplorerInDetails())
            );
        }

        explorerService.saveExplorer(probe);

        URI uri = uriBuilder.path("/explorer/{slug}").buildAndExpand(probe.getSlug()).toUri();
        return ResponseEntity.created(uri).body(probe.showExplorerInDetails());
    }

    @GetMapping
    public List<ExplorerInDetails> listAllExplorers(String planet){
        List<Explorer> explorers = explorerService.getAllExplorers(planet);
        return ExplorerInDetails.parseExplorersList(explorers);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getAnExplorerBySlug(@PathVariable String slug){
        Explorer probe = explorerService.getExplorerBySlug(slug);
        if(probe == null) {
            return ResponseEntity.badRequest().body(new RegisterWithMessage(
                    "There is no explorer identified by informed slug. Please, verify it",
                    RegisterType.ERROR,
                    null)
            );
        }

        return ResponseEntity.ok(probe.showExplorerInDetails());
    }

    @PutMapping("/{slug}/launch")
    @Transactional
    public ResponseEntity<?> launchExplorer(@PathVariable String slug, @RequestBody ExplorerLaunching body) {
        Explorer probe = explorerService.getExplorerBySlug(slug);
        if (probe.getStatus() == ExplorerStatus.ON_PLANET) {
            return ResponseEntity.badRequest().body(new RegisterWithMessage(
                    "Explorer already on a planet",
                    RegisterType.EXPLORER,
                    probe.showExplorerInDetails())
            );
        }

        if (!planetService.checkIfPositionsAreInPlanetCoordenates(body.getPlanetSlug(), body.getPosX(), body.getPosY())) {
            return ResponseEntity.badRequest().body(new RegisterWithMessage(
                    "Explorer landing coordenates are beyond planet's limit \n" +
                            "Please, verify it",
                    RegisterType.EXPLORER,
                    probe.showExplorerInDetails())
            );
        }

        if (!explorerService.tryToLand(probe, body.getPlanetSlug(), body.getPosX(), body.getPosY(), body.getOrientation())) {
            return ResponseEntity.badRequest().body(new RegisterWithMessage(
                    "It's not possible to land in target position neither any position around \n" +
                            "Please, try again",
                    RegisterType.EXPLORER,
                    probe.showExplorerInDetails())
            );
        }

        return  ResponseEntity.ok(probe.showExplorerInDetails());
    }

    @PutMapping("/{slug}/move")
    @Transactional
    public ResponseEntity<?> moveExplorer(@PathVariable String slug, @RequestBody ExplorerMoving body) {
        Explorer probe = explorerService.getExplorerBySlug(slug);
        if (probe.getStatus() == ExplorerStatus.AT_BASE) {
            return ResponseEntity.badRequest().body(new RegisterWithMessage(
                    "Explorer at base. It's not possible to move it around",
                    RegisterType.EXPLORER,
                    probe.showExplorerInDetails())
            );
        }

        explorerService.moveExplorer(probe, body.getMovement());
        explorerService.saveExplorer(probe);

        return  ResponseEntity.ok(probe.showExplorerInDetails());
    }
}
