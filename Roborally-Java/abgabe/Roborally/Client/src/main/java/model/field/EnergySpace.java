package model.field;

import com.google.gson.annotations.Expose;

/**
 * class energy space
 * @author Aigerim
 */
public class EnergySpace extends Field {
    @Expose
    private String type;
    @Expose
    private String isOnBoard;

    public EnergySpace(String isOnBoard) {
        super();
        this.type = "EnergySpace";
        this.isOnBoard = isOnBoard;
    }

    /**
     * get field type
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
