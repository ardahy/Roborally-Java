package model.field;

import com.google.gson.annotations.Expose;

/**
 * class restart point
 * @author Aigerim
 */
public class RestartPoint extends Field {
    @Expose
    public String type;
    @Expose
    private String isOnBoard;

    public RestartPoint(String isOnBoard) {
        super();
        this.type = "RestartPoint";
        this.isOnBoard = isOnBoard;
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

}
