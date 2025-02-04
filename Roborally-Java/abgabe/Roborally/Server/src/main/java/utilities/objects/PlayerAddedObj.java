package utilities.objects;

import server.ClientHandler;
import server.Server;

public class PlayerAddedObj implements ServerObjectHandler<PlayerAddedObj>{

    private int clientID;
    private String name;
    private int figure;

    /**
     * creates PlayerAdded Object
     * server confirms valid Values
     * @param clientID
     * @param name
     * @param figure
     */
    public PlayerAddedObj(int clientID, String name, int figure){

        this.clientID = clientID;
        this.name = name;
        this.figure = figure;
    }

    public int getClientID(){ return this.clientID; }

    public String getName(){
        return this.name;
    }

    public int getFigure(){
        return this.figure;
    }

    @Override
    public void action(Server server, ClientHandler task, PlayerAddedObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handlePlayerAdded(bodyObject);

    }



}
