package br.com.elo.challenge.planetexplorer.dtos.output;

import br.com.elo.challenge.planetexplorer.models.Planet;

public class PlanetItem {

    private String name;
    private String slug;
    private String coordenates;

    public PlanetItem() {
    }

    public PlanetItem(Planet planet) {
        this.name = planet.getName();
        this.slug = planet.getSlug();
        this.coordenates = planet.getPlanetMapCoordenates();
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
}
