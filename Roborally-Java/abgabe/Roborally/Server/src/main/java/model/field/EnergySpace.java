package model.field;

import com.google.gson.annotations.Expose;

/**
 * class energy space
 */
public class EnergySpace extends Field {
    @Expose
    private String type;
    @Expose
    private String isOnBoard;
    @Expose
    private Integer count;

    public EnergySpace(String isOnBoard) {
        super();
        this.type = "EnergySpace";
        this.isOnBoard = isOnBoard;
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

    /**
     * get count
     * @return
     */
    public Integer getCount() {
        return count;
    }

}
