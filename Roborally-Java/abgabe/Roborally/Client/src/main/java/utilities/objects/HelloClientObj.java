package utilities.objects;

import viewModel.Client;


public class HelloClientObj implements ClientObjectHandler<HelloClientObj>{

    private String protocol;

    /**
     * creates HelloClient Object
     * @param protocol: server sends protocol version to client
     */
    public HelloClientObj(String protocol) {

        this.protocol = protocol;
    }

    public String getProtocol(){
        return protocol;

    }

    @Override
    public void action(Client client, HelloClientObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleHelloClient(client, bodyObject);

    }

}
