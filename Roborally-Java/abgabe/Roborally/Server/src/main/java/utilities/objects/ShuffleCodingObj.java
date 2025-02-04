package utilities.objects;

import server.ClientHandler;
import server.Server;

public class ShuffleCodingObj {

    private int clientID;

    /**
     * creates ShuffleCoding Object
     * if there are not enough cards on the deck, the discard pile has to be shuffled
     * @param clientID
     */
    public ShuffleCodingObj(int clientID){
        this.clientID = clientID;
    }

    public int getClientID(){
        return this.clientID;
    }

}
