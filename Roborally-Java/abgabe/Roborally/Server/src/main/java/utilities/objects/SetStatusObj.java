package utilities.objects;

import server.ClientHandler;
import server.Server;

public class SetStatusObj implements ServerObjectHandler<SetStatusObj>{

    private boolean ready;

    /**
     * creates SetStatus Object
     * client chooses if he is ready or not ready
     * @param ready : true/false
     */
    public SetStatusObj(boolean ready){

        this.ready = ready;
    }

    public boolean getReady(){
        return this.ready;
    }

    @Override
    public void action(Server server, ClientHandler task, SetStatusObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleSetStatus(server, task, bodyObject);

    }
}
