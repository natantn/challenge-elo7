package br.com.elo.challenge.planetexplorer;

import br.com.elo.challenge.planetexplorer.dtos.input.ExplorerToCreate;
import br.com.elo.challenge.planetexplorer.dtos.output.ExplorerInDetails;
import br.com.elo.challenge.planetexplorer.dtos.output.ExplorerItem;
import br.com.elo.challenge.planetexplorer.dtos.output.PlanetInDetails;
import br.com.elo.challenge.planetexplorer.enums.Direction;
import br.com.elo.challenge.planetexplorer.enums.ExplorerStatus;
import br.com.elo.challenge.planetexplorer.models.Explorer;
import br.com.elo.challenge.planetexplorer.models.Planet;
import br.com.elo.challenge.planetexplorer.repository.ExplorerRespository;
import br.com.elo.challenge.planetexplorer.services.ExplorerService;
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
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExplorerServiceTest {

    @Mock
    private ExplorerRespository explorerRespository;
    @Mock
    private PlanetService planetService;

    @Autowired
    @InjectMocks
    private ExplorerService explorerService;

    private ExplorerToCreate newExplorer;
    private Explorer existingProbe;
    private Planet planet;
    private List<Explorer> explorers = new ArrayList<>();
    private List<ExplorerInDetails> explorerInDetailsList;

    @BeforeEach
    public void setUp() {
        newExplorer = new ExplorerToCreate("voyager");
        existingProbe = new Explorer("voyager");

        planet = new Planet("earth", 5,5);

        explorers.add(existingProbe);
        explorerInDetailsList = ExplorerInDetails.parseExplorersList(explorers);
    }

    @AfterEach
    public void tearDown(){
        newExplorer = null;
        existingProbe = null;
        planet = null;
        explorers = null;
    }

    @Test
    public void createNewExplorerAndReturnIt(){
        when(explorerRespository.save(any())).thenReturn(existingProbe);

        explorerService.saveExplorer(newExplorer.createExplorer());

        verify(explorerRespository, times(1)).save(any());
    }

    @Test
    public void getExplorerBySlugAndReturnIt(){
        when(explorerRespository.findExplorerBySlug(any())).thenReturn(existingProbe);

        Explorer e = explorerService.getExplorerBySlug(newExplorer.createExplorer().getSlug());

        verify(explorerRespository, times(1)).findExplorerBySlug(any());
        Assertions.assertNotNull(e);
    }

    @Test
    public void getAllExplorerInAList(){
        explorerService.saveExplorer(newExplorer.createExplorer());
        when(explorerRespository.findAll()).thenReturn(explorers);

        List<Explorer> list = explorerService.getAllExplorers(null);

        Assertions.assertEquals(list.get(0).getSlug(), explorers.get(0).getSlug());
        verify(explorerRespository, times(1)).findAll();
    }

    @Test
    public void tryToLandingWithSucess(){
        when(planetService.getPlanetBySlug(any())).thenReturn(planet);

        Assertions.assertTrue(explorerService.tryToLand(existingProbe, planet.getSlug(), 2, 2, Direction.North));
        Assertions.assertEquals(existingProbe.getStatus(), ExplorerStatus.ON_PLANET);
    }

    @Test
    public void tryToLandingAroundPosition(){
        when(planetService.getPlanetBySlug(any())).thenReturn(planet);

        Explorer inPlanet = new Explorer("galileu");
        explorerService.tryToLand(inPlanet, planet.getSlug(), 2, 2, Direction.North);

        Assertions.assertTrue(explorerService.tryToLand(existingProbe, planet.getSlug(), 2, 2, Direction.North));
        Assertions.assertEquals(existingProbe.getStatus(), ExplorerStatus.ON_PLANET);
        Assertions.assertNotEquals(existingProbe.getExplorerMapCoordenates(), "2x2");
    }

    @Test
    public void tryToLandAndFail(){
        when(planetService.getPlanetBySlug(any())).thenReturn(planet);

        int index = 1;
        Explorer inPlanet;
        do {
            inPlanet = new Explorer("galileu" + index);
            explorerService.tryToLand(inPlanet, planet.getSlug(), 2, 2, Direction.North);
            index++;
        }
        while (index <= 5);

        Assertions.assertFalse(explorerService.tryToLand(existingProbe, planet.getSlug(), 2, 2, Direction.North));
        Assertions.assertEquals(existingProbe.getStatus(), ExplorerStatus.AT_BASE);
    }

    @Test
    public void moveProbeFoward(){
        when(planetService.getPlanetBySlug(any())).thenReturn(planet);
        explorerService.tryToLand(existingProbe, planet.getSlug(), 2, 2, Direction.North);

        explorerService.moveExplorer(existingProbe, "M");

        Assertions.assertEquals("2x3", existingProbe.getExplorerMapCoordenates());
        Assertions.assertEquals(existingProbe.getOrientation(), Direction.North);
    }

    @Test
    public void turnProbeAround360(){
        when(planetService.getPlanetBySlug(any())).thenReturn(planet);
        explorerService.tryToLand(existingProbe, planet.getSlug(), 2, 2, Direction.North);

        explorerService.moveExplorer(existingProbe, "R");
        Assertions.assertEquals(existingProbe.getExplorerMapCoordenates(), "2x2");
        Assertions.assertEquals(existingProbe.getOrientation(), Direction.East);

        explorerService.moveExplorer(existingProbe, "RRLRLR");
        Assertions.assertEquals(existingProbe.getOrientation(), Direction.West);

        explorerService.moveExplorer(existingProbe, "R");
        Assertions.assertEquals(existingProbe.getOrientation(), Direction.North);

        explorerService.moveExplorer(existingProbe, "LLLL");
        Assertions.assertEquals(existingProbe.getOrientation(), Direction.North);
    }

    @Test
    public void moveProbeInCicle(){
        when(planetService.getPlanetBySlug(any())).thenReturn(planet);
        explorerService.tryToLand(existingProbe, planet.getSlug(), 2, 2, Direction.North);

        explorerService.moveExplorer(existingProbe, "MRMRMRMR");

        Assertions.assertEquals(existingProbe.getExplorerMapCoordenates(), "2x2");
        Assertions.assertEquals(Direction.North, existingProbe.getOrientation());
    }

//    @Test
//    public void tryToMoveABlockedProbe() {
//        when(planetService.getPlanetBySlug(any())).thenReturn(planet);
//        explorerService.tryToLand(existingProbe, planet.getSlug(), 2, 2, Direction.North);
//        int index = 1;
//        Explorer inPlanet;
//        do {
//            inPlanet = new Explorer("galileu" + index);
//            explorerService.tryToLand(inPlanet, planet.getSlug(), 2, 2, Direction.North);
//            index++;
//        }
//        while (index <= 4);
//
//        explorerService.moveExplorer(existingProbe, "M");
//
//        Assertions.assertEquals(existingProbe.getExplorerMapCoordenates(), "2x2");
//        Assertions.assertEquals(Direction.North, existingProbe.getOrientation());
//    }
}
