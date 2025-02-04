package model.field;

import com.google.gson.annotations.Expose;

/**
 * class empty
 * @author Aigerim
 */
public class Empty extends Field {
    @Expose
    private String type;
    @Expose
    private String isOnBoard;

    public Empty(String isOnBoard) {
        super();
        this.type = "Empty";
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
