package br.com.elo.challenge.planetexplorer.dtos.input;

import br.com.elo.challenge.planetexplorer.models.Explorer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ExplorerToCreate {

    @NotEmpty
    @NotNull
    private String name;

    public ExplorerToCreate() {
    }

    public ExplorerToCreate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Explorer createExplorer(){
        return new Explorer(name);
    }
}
