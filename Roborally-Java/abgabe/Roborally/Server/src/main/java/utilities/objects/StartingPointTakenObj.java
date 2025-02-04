package utilities.objects;

import server.ClientHandler;
import server.Server;

public class StartingPointTakenObj {

    private int x;
    private int y;
    private int clientID;

    /**
     * creates StartingPointTaken Object
     * server sends valid starting point to all clients
     * @param x : x-coordinate of starting point
     * @param y : y-coordinate of starting point
     * @param clientID : ID of client that has chosen starting point
     */
    public StartingPointTakenObj(int x, int y, int clientID){
        this.x = x;
        this.y = y;
        this.clientID = clientID;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getClientID(){
        return this.clientID;
    }

}
