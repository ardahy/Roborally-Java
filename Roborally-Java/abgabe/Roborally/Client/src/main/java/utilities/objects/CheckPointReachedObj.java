package utilities.objects;

import viewModel.Client;

public class CheckPointReachedObj implements ClientObjectHandler<CheckPointReachedObj> {

    private int clientID;
    private int number;

    /**
     * creates CheckPointReached Object
     * @param clientID
     * @param number : number of checkpoints reached
     */
    public CheckPointReachedObj(int clientID, int number) {
        this.clientID = clientID;
        this.number = number;
    }

    public int getClientID() {
        return this.clientID;
    }

    public int getNumber() {
        return this.number;
    }

    @Override
    public void action(Client client, CheckPointReachedObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleCheckPointReached(client, bodyObject);
    }

}
