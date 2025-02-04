package model.field;

import com.google.gson.annotations.Expose;

/**
 * checkpoint object class
 * @author Aigerim
 */
public class CheckPoint extends Field {
    @Expose
    private String type;
    @Expose
    private Integer count;
    @Expose
    private String isOnBoard;

    public CheckPoint(String isOnBoard, Integer count){
        super();
        this.type = "CheckPoint";
        this.count = count;
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

    /**
     * get count
     * @return
     */
    public Integer getCount() {
        return count;
    }
}
