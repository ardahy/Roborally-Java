package model.field;

import com.google.gson.annotations.Expose;

public class Pit extends Field{
    @Expose
    private String type;
    @Expose
    private String isOnBoard;

    public Pit(String isOnBoard){
        super();
        this.type = "Pit";
        this.isOnBoard = isOnBoard;
    }

    public String getType() {
        return type;
    }

    public String getIsOnBoard() {
        return isOnBoard;
    }


}
