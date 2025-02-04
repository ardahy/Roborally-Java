package utilities.objects;

import viewModel.Client;

public class AliveObj implements ClientObjectHandler<AliveObj>{

    /**
     * creates Alive Object
     * server sends Object every 5 seconds to client and client acknowledges
     */
    public AliveObj() {}

    @Override
    public void action(Client client, AliveObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleAliveObj(client);
    }
}
