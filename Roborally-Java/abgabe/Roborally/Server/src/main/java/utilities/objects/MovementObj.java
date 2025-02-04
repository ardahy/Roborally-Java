package utilities.objects;

import server.ClientHandler;
import server.Server;

public class MovementObj {

    private int clientID;
    private int x;
    private int y;

    /**
     * creates Movement Object
     * @param clientID
     * @param x : number of moves in x-direction
     * @param y : number of moves in y-direction
     */
    public MovementObj(int clientID, int x, int y){
        this.clientID = clientID;
        this.x = x;
        this.y = y;
    }

    public int getClientID(){
        return this.clientID;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

}
