package utilities.objects;


import server.ClientHandler;
import server.Server;

public class HelloServerObj implements ServerObjectHandler<HelloServerObj> {

    private String group;
    private boolean isAI;
    private String protocol;

    /**
     * creates HelloServer Object
     * @param group : Desperate Drosseln
     * @param isAI : false/true
     * @param protocol : client sends protocol version to server
     */
    public HelloServerObj(String group, boolean isAI, String protocol) {
        this.group = group;
        this.isAI = isAI;
        this.protocol = protocol;
    }

    public String getGroup() {
        return group;
    }

    public boolean getIsAI() {
        return isAI;
    }

    public String getProtocol(){
        return protocol;
    }

    @Override
    public void action(Server server, ClientHandler task, HelloServerObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleHelloServer(server, task, bodyObject);
    }

}
