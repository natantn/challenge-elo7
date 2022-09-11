package br.com.elo.challenge.planetexplorer.dtos.input;

import br.com.elo.challenge.planetexplorer.models.Explorer;

public class ExplorerToCreate {

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
