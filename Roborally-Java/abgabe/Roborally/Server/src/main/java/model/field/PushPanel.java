package model.field;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class PushPanel extends Field{
    @Expose
    private String type;
    @Expose
    private String isOnBoard;
    @Expose
    private ArrayList<String> orientations = new ArrayList();
    @Expose
    private ArrayList<Integer> registers;

    public PushPanel(Integer count, String isOnBoard,ArrayList<String> orientation) {
        super();
        this.isOnBoard = isOnBoard;
        this.orientations = orientation;
        this.registers = registers;
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

    public ArrayList<Integer> getRegister() {
        return registers;
    }

}
