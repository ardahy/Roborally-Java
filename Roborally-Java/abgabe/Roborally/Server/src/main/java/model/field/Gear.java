package model.field;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Gear extends Field {
    @Expose
    private String type;
    @Expose
    private String isOnBoard;
    @Expose
    private ArrayList<String> orientations = new ArrayList();


    public Gear(String isOnBoard,ArrayList<String> orientation) {
        super();
        this.type = "Gear" ;
        this.isOnBoard = isOnBoard;
        this.orientations = orientation;
    }

    public String getType() {
        return type;
    }

    public String getIsOnBoard() {
        return isOnBoard;
    }

    public ArrayList<String> getOrientation() {
        return orientations;
    }


}
