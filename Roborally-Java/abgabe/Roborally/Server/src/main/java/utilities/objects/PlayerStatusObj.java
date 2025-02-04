package utilities.objects;

import server.ClientHandler;
import server.Server;

public class PlayerStatusObj implements ServerObjectHandler<PlayerStatusObj>{

    private int clientID;
    private boolean ready;

    /**
     * creates PlayerStatus Object
     * server sends player's status to all clients
     * @param clientID
     * @param ready
     */
    public PlayerStatusObj(int clientID, boolean ready){
        this.clientID = clientID;
        this.ready = ready;
    }

    public int getClientID(){
        return this.clientID;
    }

    public boolean getReady(){
        return this.ready;
    }

    @Override
    public void action(Server server, ClientHandler task, PlayerStatusObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handlePlayerStatus(bodyObject);

    }

}
