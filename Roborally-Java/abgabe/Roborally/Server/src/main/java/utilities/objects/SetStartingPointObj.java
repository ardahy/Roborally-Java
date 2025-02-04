package utilities.objects;

import server.ClientHandler;
import server.Server;

public class SetStartingPointObj implements ServerObjectHandler<SetStartingPointObj>{

    private int x;
    private int y;

    /**
     * creates SetStartingPoint Object
     * client chooses starting point
     * @param x : x-coordinate of starting point
     * @param y : y-coordinate of starting point
     */
    public SetStartingPointObj(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    @Override
    public void action(Server server, ClientHandler task, SetStartingPointObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleSetStartingPoint(server, task, bodyObject);

    }
}
