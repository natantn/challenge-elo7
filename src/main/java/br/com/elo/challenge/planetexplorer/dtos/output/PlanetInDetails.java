package br.com.elo.challenge.planetexplorer.dtos.output;

import br.com.elo.challenge.planetexplorer.models.Explorer;
import br.com.elo.challenge.planetexplorer.models.Planet;
import br.com.elo.challenge.planetexplorer.models.SpaceRegister;

import java.util.List;
import java.util.stream.Collectors;

public class PlanetInDetails extends SpaceRegister {

    private String name;
    private String slug;
    private String coordenates;
    private List<ExplorerItem> explorersAtPlanet;

    public PlanetInDetails() {
    }

    public PlanetInDetails(Planet planet) {
        this.name = planet.getName();
        this.slug = planet.getSlug();
        this.coordenates = planet.getPlanetMapCoordenates();
        this.explorersAtPlanet = planet.getProbesAtPlanet();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCoordenates() {
        return coordenates;
    }

    public void setCoordenates(String coordenates) {
        this.coordenates = coordenates;
    }

    public List<ExplorerItem> getExplorersAtPlanet() {
        return explorersAtPlanet;
    }

    public void setExplorersAtPlanet(List<ExplorerItem> explorersAtPlanet) {
        this.explorersAtPlanet = explorersAtPlanet;
    }

    public static List<PlanetInDetails> parsePlanetsList(List<Planet> planets) {
        return planets.stream().map(PlanetInDetails::new).collect(Collectors.toList());
    }
}
