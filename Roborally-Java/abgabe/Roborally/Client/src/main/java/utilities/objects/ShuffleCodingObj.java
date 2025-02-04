package utilities.objects;

import viewModel.Client;

public class ShuffleCodingObj implements ClientObjectHandler<ShuffleCodingObj>{

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

    @Override
    public void action(Client client, ShuffleCodingObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleShuffleCoding(client, bodyObject);

    }
}
