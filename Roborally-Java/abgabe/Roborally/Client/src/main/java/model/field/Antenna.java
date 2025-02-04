package model.field;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;

/**
 * class antenna
 * @author Aigerim
 */
public class Antenna extends Field {
    @Expose
    private String type;
    @Expose
    private String isOnBoard;
    @Expose
    private ArrayList<String> orientations = new ArrayList<>();

    public Antenna(String isOnBoard, ArrayList<String> orientations2) {
        super();
        this.type = "Antenna";
        this.isOnBoard = isOnBoard;
        this.orientations = orientations2;
    }

    /**
     * get field type
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
     *get orientation list
     * @return
     */
    public ArrayList<String> getOrientation() {
        return orientations;
    }
}
