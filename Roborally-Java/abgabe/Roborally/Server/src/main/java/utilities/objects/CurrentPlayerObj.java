package utilities.objects;

import server.ClientHandler;
import server.Server;

public class CurrentPlayerObj {

        private int clientID;

    /**
     * creates CurrentPlayer Object
     * server determines active player, first player that is connected starts game
     * @param clientID
     */
    public CurrentPlayerObj(int clientID) {

        this.clientID = clientID;
    }

    public int getClientID() {
        return clientID;
    }
}


