package br.com.elo.challenge.planetexplorer.dtos.input;

import br.com.elo.challenge.planetexplorer.enums.Direction;

public class ExplorerLaunching {

    private int posX;
    private int posY;
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
}
