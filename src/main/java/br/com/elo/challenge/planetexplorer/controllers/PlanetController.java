package br.com.elo.challenge.planetexplorer.controllers;

import br.com.elo.challenge.planetexplorer.dtos.input.PlanetToRegister;
import br.com.elo.challenge.planetexplorer.dtos.output.ExplorerInDetails;
import br.com.elo.challenge.planetexplorer.dtos.output.PlanetInDetails;
import br.com.elo.challenge.planetexplorer.dtos.output.RegisterWithMessage;
import br.com.elo.challenge.planetexplorer.enums.RegisterType;
import br.com.elo.challenge.planetexplorer.models.Explorer;
import br.com.elo.challenge.planetexplorer.models.Planet;
import br.com.elo.challenge.planetexplorer.repository.PlanetRepository;
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
@RequestMapping(value = "/planet")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @PostMapping
    @Transactional
    public ResponseEntity<?> RegisterNewPlanet(@RequestBody PlanetToRegister body, UriComponentsBuilder uriBuilder) {
        Planet newPlanet = body.getAsPlanet();
        if(planetService.getPlanetBySlug(newPlanet.getSlug()) != null) {
            return ResponseEntity.badRequest().body(new RegisterWithMessage(
                    "There is one explorer already created with this name. Please, try another one",
                    RegisterType.PLANET,
                    newPlanet.showPlanetInDetails())
            );
        }

        planetService.savePlanet(newPlanet);
        URI uri = uriBuilder.path("/planet/{slug}").buildAndExpand(newPlanet.getSlug()).toUri();
        return ResponseEntity.created(uri).body(newPlanet.showPlanetInDetails());
    }

    @GetMapping
    public List<PlanetInDetails> listAllPlanets(){
        return planetService.getPlanets();
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getPlanetBySlug(@PathVariable String slug){
        Planet planet = planetService.getPlanetBySlug(slug);
        if(planet == null) {
            return ResponseEntity.badRequest().body(new RegisterWithMessage(
                    "There is no planet identified by informed slug. Please, verify it",
                    RegisterType.ERROR,
                    null)
            );
        }

        return ResponseEntity.ok(planet.showPlanetInDetails());
    }


}
