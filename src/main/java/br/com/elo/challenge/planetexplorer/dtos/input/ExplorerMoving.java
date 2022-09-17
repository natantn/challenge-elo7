package br.com.elo.challenge.planetexplorer.dtos.input;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ExplorerMoving {

    @NotEmpty
    @NotNull
    String movement;


    public ExplorerMoving() {
    }

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }
}
