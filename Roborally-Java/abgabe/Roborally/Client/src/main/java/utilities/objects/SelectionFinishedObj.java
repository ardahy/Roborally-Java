package utilities.objects;

import viewModel.Client;

public class SelectionFinishedObj implements ClientObjectHandler<SelectionFinishedObj>{

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

    @Override
    public void action(Client client, SelectionFinishedObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleSelectionFinished(client, bodyObject);

    }
}
