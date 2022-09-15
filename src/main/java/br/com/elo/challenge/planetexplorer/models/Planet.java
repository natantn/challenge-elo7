package br.com.elo.challenge.planetexplorer.models;

import br.com.elo.challenge.planetexplorer.dtos.output.ExplorerInDetails;
import br.com.elo.challenge.planetexplorer.dtos.output.ExplorerItem;
import br.com.elo.challenge.planetexplorer.dtos.output.PlanetInDetails;
import br.com.elo.challenge.planetexplorer.dtos.output.PlanetItem;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Document(collection = "planets")
public class Planet extends SpaceRegister {

    private String id;
    private String name;
    private String slug;
    private int limitX;
    private int limitY;
    private List<ExplorerItem> probesExploringPlanet = new ArrayList<>();

    public Planet() {
    }

    public Planet(String name, int x, int y){
        this.name = name;
        this.limitX = x;
        this.limitY = y;
        this.slug = generateSlug(name);
        this.probesExploringPlanet = new ArrayList<>();
    }

    public static String generateSlug(String name) {
        return name.toLowerCase(Locale.ROOT).replaceAll(" ", "-")
                + LocalDate.now().toString().replaceAll("-", "");
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public int getLimitX() {
        return limitX;
    }

    public int getLimitY() {
        return limitY;
    }

    public void landExplorer(ExplorerItem explorer) {
        this.probesExploringPlanet.add(explorer);
    }

    public void saveExplorer(ExplorerItem explorerItem) {
        for (ExplorerItem e : probesExploringPlanet) {
            if (e.getSlug().equals(explorerItem.getSlug())) {
                this.probesExploringPlanet.remove(e);
                this.probesExploringPlanet.add(explorerItem);
            }
        }
    }

    public boolean hasAnExplorerAtXY(int x, int y){
        for (ExplorerItem e: probesExploringPlanet) {
            String[] coordenates = e.getCoordenates().split("x");
            if (Integer.parseInt(coordenates[0]) == x && Integer.parseInt(coordenates[1]) == y) {
                return true;
            }
        }
        return false;
    }

    public String getPlanetMapCoordenates() {
        return limitX + "x" + limitY;
    }

    public List<ExplorerItem> getProbesAtPlanet(){
        return this.probesExploringPlanet;
    }

    public PlanetInDetails showPlanetInDetails(){
        return new PlanetInDetails(this);
    }

    public PlanetItem getPlanetAsItemRegister(){
        return new PlanetItem(this);
    }
}
