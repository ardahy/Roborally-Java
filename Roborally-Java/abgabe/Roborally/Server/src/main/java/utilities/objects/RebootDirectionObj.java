package utilities.objects;

import server.ClientHandler;
import server.Server;

public class RebootDirectionObj implements ServerObjectHandler<RebootDirectionObj> {

    private String direction;

    /**
     * creates RebootDirection Object
     * client chooses direction of robot
     * @param direction : top/right/bottom/left
     */
    public RebootDirectionObj(String direction){
        this.direction = direction;
    }

    public String getDirection(){
        return this.direction;
    }

    @Override
    public void action(Server server, ClientHandler task, RebootDirectionObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleRebootDirection(server, task , bodyObject);

    }
}