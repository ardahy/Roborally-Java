package model.field;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * antenna object class
 * @author Aigerim Abdykadyrova
 */
public class Antenna extends Field {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("isOnBoard")
    @Expose
    private String isOnBoard;
    @SerializedName("orientations")
    @Expose
    private ArrayList<String> orientations = new ArrayList<>();

    public Antenna(String isOnBoard, ArrayList<String> orientations2) {
        super();
        this.type = "Antenna";
        this.isOnBoard = isOnBoard;
        this.orientations = orientations2;
    }

    /**
     * get name type
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
     * get orientation
     * @return
     */
    public ArrayList<String> getOrientation() {
        return orientations;
    }

}
