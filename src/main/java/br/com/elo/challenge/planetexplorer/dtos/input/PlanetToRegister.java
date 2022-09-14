package br.com.elo.challenge.planetexplorer.dtos.input;

import br.com.elo.challenge.planetexplorer.models.Planet;

public class PlanetToRegister {

    private String name;
    private int limitX;
    private int limitY;

    public PlanetToRegister() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLimitX() {
        return limitX;
    }

    public void setLimitX(int limitX) {
        this.limitX = limitX;
    }

    public int getLimitY() {
        return limitY;
    }

    public void setLimitY(int limitY) {
        this.limitY = limitY;
    }

    public Planet getAsPlanet(){
        return new Planet(this.name, this.limitX, this.limitY);
    }
}
