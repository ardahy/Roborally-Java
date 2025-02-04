package utilities.objects;

import viewModel.Client;

public class WelcomeObj implements ClientObjectHandler<WelcomeObj>{

    private int clientID;

    /**
     * creates Welcome Object
     * @param clientID : server sends client his ID
     */
    public WelcomeObj(int clientID) {
        this.clientID = clientID;
    }

    public int getClientID() {
        return clientID;
    }

    @Override
    public void action(Client client, WelcomeObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleWelcome(client, bodyObject);
    }

}
