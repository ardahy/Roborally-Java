package model.field;

import java.util.ArrayList;

/**
 * abstract class for fields elements
 * @author Aigerim Abdykadyrova
 */
public abstract class Field {
    public String type;
    private Integer speed;
    private Integer count;
    private String isOnBoard;
    private ArrayList<String> orientations = new ArrayList();
    private ArrayList<Integer> registers = new ArrayList<>();

    public Field(String type, Integer speed, Integer count, String isOnBoard, ArrayList<String> orientation, ArrayList<Integer> registers) {
        this.type = type;
        this.speed = speed;
        this.count = count;
        this.isOnBoard = isOnBoard;
        this.orientations = orientation;
        this.registers = registers;
    }

    public Field() {

    }

    /**
     * get field type name
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * get Board name
     * @return
     */
    public String getIsOnBoard() {
        return isOnBoard;
    }

    /**
     * get orientations
     * @return
     */
    public ArrayList<String> getOrientation() {
        return orientations;
    }

    /**
     * get speed
     * @return
     */
    public Integer getSpeed() {
        return speed;
    }

    /**
     * get count
     * @return
     */
    public Integer getCount() {
        return count;
    }

    /**
     * get register
     * @return
     */
    public ArrayList<Integer> getRegister() {
        return registers;
    }

}
