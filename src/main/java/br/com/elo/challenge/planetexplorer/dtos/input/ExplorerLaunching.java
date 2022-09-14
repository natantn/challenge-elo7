package br.com.elo.challenge.planetexplorer.dtos.input;

import br.com.elo.challenge.planetexplorer.enums.Direction;
import br.com.elo.challenge.planetexplorer.models.Planet;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


public class ExplorerLaunching {

    @NotNull
    @NotEmpty
    private String planetSlug;
    @NotNull
    @NotEmpty
    @Positive
    private int posX;
    @Positive
    @NotNull
    @NotEmpty
    private int posY;
    @NotNull
    @NotEmpty
    private Direction orientation;

    public ExplorerLaunching() {
    }

    public ExplorerLaunching(int posX, int posY, Direction orientation) {
        this.posX = posX;
        this.posY = posY;
        this.orientation = orientation;
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

    public String getPlanetSlug() {
        return planetSlug;
    }

    public void setPlanetSlug(String planetSlug) {
        this.planetSlug = planetSlug;
    }
}
