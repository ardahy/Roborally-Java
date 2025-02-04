package model.field;

import com.google.gson.annotations.Expose;

/**
 * class start point
 * @author Aigerim
 */
public class StartPoint extends Field {
    @Expose
    private String type;
    @Expose
    private String isOnBoard;

    public StartPoint(String isOnBoard){
        super();
        this.type = "StartPoint";
        this.isOnBoard = isOnBoard;
    }

    /**
     * get field name
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

}
