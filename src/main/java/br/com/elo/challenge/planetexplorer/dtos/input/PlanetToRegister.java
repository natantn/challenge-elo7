package br.com.elo.challenge.planetexplorer.dtos.input;

import br.com.elo.challenge.planetexplorer.models.Planet;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PlanetToRegister {

    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    @DecimalMax(value = "10")
    @DecimalMin(value = "3")
    private int limitX;
    @NotEmpty
    @NotNull
    @DecimalMax(value = "10")
    @DecimalMin(value = "3")
    private int limitY;

    public PlanetToRegister() {
    }

    public PlanetToRegister(String name, int limitX, int limitY) {
        this.name = name;
        this.limitX = limitX;
        this.limitY = limitY;
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
