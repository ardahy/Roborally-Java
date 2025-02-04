package utilities.objects;

import server.ClientHandler;
import server.Server;

public class WelcomeObj implements ServerObjectHandler<WelcomeObj> {

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
    public void action(Server server, ClientHandler task, WelcomeObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleWelcome(bodyObject);

    }

}

