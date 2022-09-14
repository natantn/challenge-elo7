package br.com.elo.challenge.planetexplorer.services;

import br.com.elo.challenge.planetexplorer.dtos.output.RegisterWithMessage;
import br.com.elo.challenge.planetexplorer.enums.Direction;
import br.com.elo.challenge.planetexplorer.enums.Side;
import br.com.elo.challenge.planetexplorer.models.Explorer;
import br.com.elo.challenge.planetexplorer.models.Planet;
import br.com.elo.challenge.planetexplorer.repository.ExplorerRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExplorerService {

    @Autowired
    private ExplorerRespository explorerRespository;
    @Autowired
    private PlanetService planetService;

    public Explorer getExplorerBySlug(String slug) {
        return explorerRespository.findExplorerBySlug(slug);
    }

    public List<Explorer> getAllExplorers(String planetSlug) {
        List<Explorer> explorers = new ArrayList<>();
        if (planetSlug == null) {
            explorers = explorerRespository.findAll();
        } else {


        }
        return explorers;
    }

    public boolean tryToLand(Explorer probe, String planetSlug, int landingPosX, int landingPosY, Direction orientation) {
        int landTries = 0;
        int landAtPosX = landingPosX;
        int landAtPosY = landingPosY;
        Direction landAtDirection = Direction.North;
        Planet targetPlanet = planetService.getPlanetBySlug(planetSlug);
        do {
            boolean isLandPossible = !targetPlanet.hasAnExplorerAtXY(landAtPosX, landAtPosY);
            if (isLandPossible) {
                probe.LandAt(targetPlanet.getPlanetAsItemRegister(), landAtPosX, landAtPosY, orientation);
                targetPlanet.landExplorer(probe.getExplorerAsRegisterItem());

                this.saveExplorer(probe);
                planetService.savePlanet(targetPlanet);

                return true;
            }
            else {
                landAtPosX = landingPosX;
                landAtPosY = landingPosY;
                switch (landAtDirection) {
                    case North: //Plus 1 in Y
                        landAtPosY++;
                        break;
                    case South: //Less 1 in Y
                        landAtPosY--;
                        break;
                    case East: //Plus 1 in X
                        landAtPosX++;
                        break;
                    case West: //Less 1 in X
                        landAtPosX--;
                        break;
                }
                landAtDirection = Direction.nextDirection(landAtDirection);
                landTries++;
            }

        } while (landTries <= 4);

        return false;
    }

    public void saveExplorer(Explorer probe) {
        explorerRespository.save(probe);
    }

    public void moveExplorer(Explorer probe, String movement) {
        String[] movementSequence = movement.split("");
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
    }
}
