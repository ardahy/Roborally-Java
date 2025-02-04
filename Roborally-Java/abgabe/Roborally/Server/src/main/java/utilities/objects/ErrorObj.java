package utilities.objects;

import server.ClientHandler;
import server.Server;

public class ErrorObj implements ServerObjectHandler<ErrorObj>{

    private String error;

    /**
     * creates Error Object
     * server informs client if incorrect message is sent
     * client registers error and returns error message
     * @param error : error message
     */
    public ErrorObj(String error){
        this.error = error;
    }

    public String getError(){
        return this.error;
    }

    @Override
    public void action(Server server, ClientHandler task, ErrorObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleError(server, task, bodyObject);

    }
}
