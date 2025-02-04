package utilities.objects;

import server.ClientHandler;
import server.Server;

public class PlayerTurningObj {

    private int clientID;
    private String rotation;

    /**
     * creates PlayerTurning Object
     * @param clientID
     * @param rotation : clockwise/counterclockwise
     */
    public PlayerTurningObj(int clientID, String rotation){
        this.clientID = clientID;
        this.rotation = rotation;
    }

    public int getClientID(){
        return this.clientID;
    }

    public String getRotation(){
        return this.rotation;
    }

}
