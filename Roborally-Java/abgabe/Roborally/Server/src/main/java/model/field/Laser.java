package model.field;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * class laser
 */
public class Laser extends Field {
    @Expose
    private String type;
    @Expose
    private String isOnBoard;
    @Expose
    private ArrayList<String> orientations = new ArrayList();
    @Expose
    private Integer count;

    public Laser(Integer count, String isOnBoard,ArrayList<String> orientation) {
        super();
        this.isOnBoard = isOnBoard;
        this.orientations = orientation;
        this.count = count;
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

    public ArrayList<String> getOrientation() {
        return orientations;
    }

    public Integer getCount() {
        return count;
    }

}
