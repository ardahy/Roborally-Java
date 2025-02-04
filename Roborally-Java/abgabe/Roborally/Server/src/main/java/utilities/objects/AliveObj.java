package utilities.objects;

import server.ClientHandler;
import server.Server;

public class AliveObj implements ServerObjectHandler<AliveObj>{

    /**
     * creates Alive Object
     * server sends Object every 5 seconds to client and client acknowledges
     */
    public AliveObj() {}

    @Override
    public void action(Server server, ClientHandler task , AliveObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleAliveObj();
    }
}
