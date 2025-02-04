package utilities.objects;

import viewModel.Client;

public class ConnectionUpdateObj implements ClientObjectHandler<ConnectionUpdateObj>{

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
    public void action(Client client, ConnectionUpdateObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleConnectionUpdate(client, bodyObject);

    }
}
