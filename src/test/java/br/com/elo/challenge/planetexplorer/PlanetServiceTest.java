package br.com.elo.challenge.planetexplorer;

import br.com.elo.challenge.planetexplorer.dtos.input.PlanetToRegister;
import br.com.elo.challenge.planetexplorer.dtos.output.ExplorerItem;
import br.com.elo.challenge.planetexplorer.dtos.output.PlanetInDetails;
import br.com.elo.challenge.planetexplorer.enums.Direction;
import br.com.elo.challenge.planetexplorer.models.Explorer;
import br.com.elo.challenge.planetexplorer.models.Planet;
import br.com.elo.challenge.planetexplorer.repository.PlanetRepository;
import br.com.elo.challenge.planetexplorer.services.PlanetService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

    @Mock
    private PlanetRepository planetRepository;

    @Autowired
    @InjectMocks
    private PlanetService planetService;

    private PlanetToRegister newPlanet;
    private Planet existingPlanet;
    private List<Planet> planetList = new ArrayList<>();
    private List<PlanetInDetails> planetInDetailsList;
    private Explorer probe;

    @BeforeEach
    public void setUp() {
        newPlanet = new PlanetToRegister("earth", 5,5);

        existingPlanet = new Planet("earth", 5,5);
        planetList.add(existingPlanet);

        probe = new Explorer("voyager");
        probe.LandAt(existingPlanet.getPlanetAsItemRegister(), 3, 3 , Direction.North);
        existingPlanet.landExplorer(probe.getExplorerAsRegisterItem());

        planetInDetailsList = PlanetInDetails.parsePlanetsList(planetList);

    }

    @Test
    public void registerNewPlanetAndReturnIt() {
        when(planetRepository.save(any())).thenReturn(existingPlanet);

        planetService.savePlanet(newPlanet.getAsPlanet());

        verify(planetRepository, times(1)).save(any());
    }

    @Test
    public void getPlanetBySlugAndReturnIt() {
        when(planetRepository.findPlanetBySlug(any())).thenReturn(existingPlanet);

        Planet p = planetService.getPlanetBySlug(newPlanet.getAsPlanet().getSlug());

        verify(planetRepository, times(1)).findPlanetBySlug(any());
        Assertions.assertNotNull(p);
    }

    @Test
    public void getAllPlanets() {
        planetService.savePlanet(newPlanet.getAsPlanet());
        when(planetRepository.findAll()).thenReturn(planetList);

        List<PlanetInDetails> list = planetService.getPlanets();

        Assertions.assertEquals(list.get(0).getSlug(), planetInDetailsList.get(0).getSlug());
        verify(planetRepository, times(1)).findAll();

    }

    @Test
    public void checkCoodenatesThatAreInPlanet(){
        when(planetRepository.findPlanetBySlug(any())).thenReturn(existingPlanet);
        Assertions.assertTrue(planetService.checkIfPositionsAreInPlanetCoordenates(existingPlanet.getSlug(), 3,2));
    }

    @Test
    public void checkCoordenatesThatAreNotInPlanet(){
        when(planetRepository.findPlanetBySlug(any())).thenReturn(existingPlanet);
        Assertions.assertFalse(planetService.checkIfPositionsAreInPlanetCoordenates(existingPlanet.getSlug(), 6,5));
        Assertions.assertFalse(planetService.checkIfPositionsAreInPlanetCoordenates(existingPlanet.getSlug(), 0,-1));
    }

    @Test
    public void checkPositionsInPlanet(){
        Assertions.assertTrue(planetService.isPositionInPlanet(existingPlanet, "X", 3));
        Assertions.assertTrue(planetService.isPositionInPlanet(existingPlanet, "Y", 4));
    }

    @Test
    public void checkPositionsNotInPlanet(){
        Assertions.assertFalse(planetService.isPositionInPlanet(existingPlanet, "X", 0));
        Assertions.assertFalse(planetService.isPositionInPlanet(existingPlanet, "Y", -1));
        Assertions.assertFalse(planetService.isPositionInPlanet(existingPlanet, "X", 6));
    }

    @Test
    public void checkIfPositionIsFree(){
        Assertions.assertTrue(planetService.isPositionFree(existingPlanet, "5x5"));
        Assertions.assertFalse(planetService.isPositionFree(existingPlanet, probe.getExplorerMapCoordenates()));
    }

    @Test
    public void updatingListOfProbes(){
        when(planetRepository.save(any())).thenReturn(existingPlanet);
        probe.Move(1, "X");

        planetService.saveProbe(existingPlanet, probe.getExplorerAsRegisterItem());

        Assertions.assertTrue(existingPlanet.getProbesAtPlanet().size() == 1);
        verify(planetRepository, times(1)).save(any());
    }
}
