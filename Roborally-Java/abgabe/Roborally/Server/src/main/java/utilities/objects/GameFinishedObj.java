package utilities.objects;

import server.ClientHandler;
import server.Server;

public class GameFinishedObj {

    private int clientID;

    /**
     * creates GameFinished Object
     * if a player has reached the last checkpoint, the game is finished
     * @param clientID : ID of player that has reached the last checkpoint
     */
    public GameFinishedObj(int clientID){
        this.clientID = clientID;
    }

    public int getClientID(){
        return this.clientID;
    }

}
