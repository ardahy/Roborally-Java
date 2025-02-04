package model.field;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;

/**
 * class conveyorBelt
 * @author Aigerim
 */
public class ConveyorBelt extends Field {
    @Expose
    private String type;
    @Expose
    private String isOnBoard;
    @Expose
    private int speed;
    @Expose
    private ArrayList<String> orientations = new ArrayList<>();

    public ConveyorBelt(String isOnBoard, int speed, ArrayList<String> orientation) {
        this.type = "ConveyorBelt";
        this.speed = speed;
        this.isOnBoard = isOnBoard;
        this.orientations = orientation;
    }

    /**
     * get field type
     * @return
     */
    public String getType(){
        return this.type;
    }

    /**
     * get board position name
     * @return
     */
    public String getIsOnBoard() {
        return isOnBoard;
    }

    /**
     * get speed
     * @return
     */
    public Integer getSpeed() {
        return speed;
    }

    /**
     * get orientation list
     * @return
     */
    public ArrayList<String> getOrientation() {
        return orientations;
    }
}
