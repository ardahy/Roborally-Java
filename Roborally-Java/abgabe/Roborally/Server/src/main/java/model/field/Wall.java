package model.field;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * class wall
 * @author Aigerim
 */
public class Wall extends Field {
    @Expose
    private String type;
    @Expose
    private String isOnBoard;
    @Expose
    private ArrayList<String> orientations = new ArrayList();

    public Wall(String isOnBoard, ArrayList<String> orientation, ArrayList<Integer> registers) {
        super();
        this.type = "Wall";
        this.isOnBoard = isOnBoard;
        this.orientations = orientation;
    }

    /**
     * get field name
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * get board position name
     * @return
     */
    public String getIsOnBoard() {
        return isOnBoard;
    }

    /**
     * get list of orientations
     * @return
     */
    public ArrayList<String> getOrientation() {
        return orientations;
    }
}
