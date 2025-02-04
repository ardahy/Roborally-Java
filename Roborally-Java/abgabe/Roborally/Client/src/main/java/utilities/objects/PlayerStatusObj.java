package utilities.objects;

import viewModel.Client;

public class PlayerStatusObj implements ClientObjectHandler<PlayerStatusObj>{

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
    public void action(Client client, PlayerStatusObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handlePlayerStatus(client, bodyObject);

    }
}
