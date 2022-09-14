package br.com.elo.challenge.planetexplorer.dtos.output;

import br.com.elo.challenge.planetexplorer.enums.Direction;
import br.com.elo.challenge.planetexplorer.models.Explorer;

import java.util.List;
import java.util.stream.Collectors;

public class ExplorerItem {

    private String name;
    private String slug;
    private String coordenates;
    private Direction orientation;

    public ExplorerItem() {
    }

    public ExplorerItem(Explorer explorer) {
        this.name = explorer.getName();
        this.slug = explorer.getSlug();
        this.coordenates = explorer.getExplorerMapCoordenates();
        this.orientation = explorer.getOrientation();
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

    public Direction getOrientation() {
        return orientation;
    }

    public void setOrientation(Direction orientation) {
        this.orientation = orientation;
    }

    public static List<ExplorerItem> parseExplorersList(List<Explorer> explorers) {
        return explorers.stream().map(ExplorerItem::new).collect(Collectors.toList());
    }
}
