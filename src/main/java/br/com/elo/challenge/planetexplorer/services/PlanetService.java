package br.com.elo.challenge.planetexplorer.services;

import br.com.elo.challenge.planetexplorer.dtos.output.ExplorerItem;
import br.com.elo.challenge.planetexplorer.dtos.output.PlanetInDetails;
import br.com.elo.challenge.planetexplorer.dtos.output.RegisterWithMessage;
import br.com.elo.challenge.planetexplorer.models.Planet;
import br.com.elo.challenge.planetexplorer.repository.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanetService {

    @Autowired
    private PlanetRepository planetRepository;

    public List<PlanetInDetails> getPlanets() {
        List<Planet> planets;
        planets = planetRepository.findAll();

        return PlanetInDetails.parsePlanetsList(planets);
    }


    public void savePlanet(Planet planet) {
        planetRepository.save(planet);
    }

    public Planet getPlanetBySlug(String slug) {
        return planetRepository.findPlanetBySlug(slug);
    }

    public boolean checkIfPositionsAreInPlanetCoordenates(String planetSlug, int landingPosX, int landingPosY){
        Planet planet = getPlanetBySlug(planetSlug);
        if (landingPosX > planet.getLimitX() || landingPosY > planet.getLimitY()) {
            return false;
        }

        return true;
    }

    public boolean isPositionInPlanet(Planet planet, String axis, int pos) {
        if (pos <= 0) return false;

        if (
                (axis.equals("X") && pos <= planet.getLimitX()) ||
                (axis.equals("Y") && pos <= planet.getLimitY())
            ) return true;

        return false;
    }

    public boolean isPositionFree(Planet planet, String position) {
        for (ExplorerItem probe : planet.getProbesAtPlanet()) {
            if (probe.getCoordenates().equals(position)) return false;
        }
        return true;
    }


    public void saveProbe(Planet planet, ExplorerItem probe) {
        planet.saveExplorer(probe);
        savePlanet(planet);
    }
}
