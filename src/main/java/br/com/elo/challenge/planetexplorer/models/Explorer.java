package br.com.elo.challenge.planetexplorer.models;

import br.com.elo.challenge.planetexplorer.dtos.output.ExplorerInDetails;
import br.com.elo.challenge.planetexplorer.dtos.output.ExplorerItem;
import br.com.elo.challenge.planetexplorer.dtos.output.PlanetItem;
import br.com.elo.challenge.planetexplorer.enums.Direction;
import br.com.elo.challenge.planetexplorer.enums.ExplorerStatus;
import br.com.elo.challenge.planetexplorer.enums.Side;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Locale;

@Document(collection = "explorers")
public class Explorer extends SpaceRegister{

    @Id
    private String id;

    private String name;
    private String slug;
    private int posX = -1;
    private int posY = -1;
    private PlanetItem planet;
    private Direction orientation;
    private ExplorerStatus status;

    public Explorer() {
    }

    public Explorer(String name) {
        this.name = name;
        this.slug = this.generateSlug(this.name);
        this.status = ExplorerStatus.AT_BASE;
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

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public PlanetItem getPlanet() {
        return planet;
    }

    public Direction getOrientation() {
        return orientation;
    }

    public ExplorerStatus getStatus() {
        return status;
    }

    private String generateSlug(String name) {
        return name.toLowerCase(Locale.ROOT).replaceAll(" ", "-")
                + LocalDate.now().toString().replaceAll("-", "");
    }

    public void LandAt(PlanetItem planet, int X, int Y, Direction orientation){
        this.posX = X;
        this.posY = Y;
        this.orientation = orientation;
        this.planet = planet;
        this.status = ExplorerStatus.ON_PLANET;
    }

    /***
     *
     * @return
     */
    public void Move(int unit, String dir){
        if (dir.equals("X")) this.posX = unit;
        else if (dir.equals("Y")) this.posY = unit;

//        switch (this.orientation) {
//            case North: //Plus 1 in Y
//                this.posY++;
//                break;
//            case South: //Less 1 in Y
//                this.posY--;
//                break;
//            case East: //Plus 1 in X
//                this.posX++;
//                break;
//            case West: //Less 1 in X
//                this.posX--;
//                break;
//            default:
//                return false;
//        }
//        return true;
    }

    /***
     *
     * @param side
     */
    public void Turn(Side side){
        switch (this.orientation) {
            case North:
                this.orientation = side == Side.Left ? Direction.West : Direction.East;
                break;
            case East:
                this.orientation = side == Side.Left ? Direction.North : Direction.South;
                break;
            case South:
                this.orientation = side == Side.Left ? Direction.East : Direction.West;
                break;
            case West:
                this.orientation = side == Side.Left ? Direction.South : Direction.North;
                break;
        }
    }

    public String getExplorerMapCoordenates() {
        return this.posX + "x" + this.posY;
    }

    public ExplorerInDetails showExplorerInDetails() {
        return new ExplorerInDetails(this);
    }

    public ExplorerItem getExplorerAsRegisterItem() {
        return new ExplorerItem(this);
    }


}