package utilities.objects;

import server.ClientHandler;
import server.Server;

public class SelectionFinishedObj {

    private int clientID;

    /**
     * creates SelectionFinished Object
     * server broadcasts when a client has finished filling his registers
     * @param clientID
     */
    public SelectionFinishedObj(int clientID){
        this.clientID = clientID;
    }

    public int getClientID(){
        return this.clientID;
    }

}
