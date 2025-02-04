package utilities.objects;


import server.ClientHandler;
import server.Server;

public class ConnectionUpdateObj implements ServerObjectHandler<ConnectionUpdateObj>{

    private int clientID;
    private boolean isConnected;
    private String action;

    /**
     * creates ConnectionUpdate Object
     * server informs everyone if a client has lost the connection
     * client hast lost connection if he does not acknowledge AliveObj message
     * @param clientID
     * @param isConnected
     * @param action : Remove/AIControl/Ignore/Reconnect
     */
    public ConnectionUpdateObj(int clientID, boolean isConnected, String action){
        this.clientID = clientID;
        this.isConnected = isConnected;
        this.action = action;
    }

    public int getClientID(){
        return this.clientID;
    }
    public boolean getIsConnected(){
        return this.isConnected;
    }
    public String getAction(){
        return this.action;
    }

    @Override
    public void action(Server server, ClientHandler task, ConnectionUpdateObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleConnectionUpdate(server, bodyObject);

    }
}


