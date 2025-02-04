package model.clientgame;

/**
 * position class for robot's figure
 */
public class Position {

    public int x;
    public int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * get x position of roboter
     * @return
     */
    public int getX(){
        return this.x;
    }

    /**
     * get y position of roboter
     * @return
     */
    public int getY(){
        return this.y;
    }


}
