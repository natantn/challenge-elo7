package br.com.elo.challenge.planetexplorer.dtos.output;

import br.com.elo.challenge.planetexplorer.enums.Direction;
import br.com.elo.challenge.planetexplorer.enums.ExplorerStatus;
import br.com.elo.challenge.planetexplorer.models.Explorer;

import java.util.List;
import java.util.stream.Collectors;

public class ExplorerInDetails {

    private String name;
    private String slug;
    private int posX;
    private int posY;
    private Direction orientation;
    private ExplorerStatus status;

    public ExplorerInDetails() {
    }

    public ExplorerInDetails(Explorer explorer) {
        this.name = explorer.getName();
        this.slug = explorer.getSlug();
        this.posX = explorer.getPosX();
        this.posY = explorer.getPosY();
        this.orientation = explorer.getOrientation();
        this.status = explorer.getStatus();

    }

    public static List<ExplorerInDetails> parseExplorersList(List<Explorer> explorers) {
            return explorers.stream().map(ExplorerInDetails::new).collect(Collectors.toList());
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

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Direction getOrientation() {
        return orientation;
    }

    public void setOrientation(Direction orientation) {
        this.orientation = orientation;
    }

    public ExplorerStatus getStatus() {
        return status;
    }

    public void setStatus(ExplorerStatus status) {
        this.status = status;
    }
}

