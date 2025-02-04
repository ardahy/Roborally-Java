package utilities.objects;

import server.ClientHandler;
import server.Server;

public class PlayerValuesObj implements ServerObjectHandler<PlayerValuesObj>{


    private String name;
    private int figure;

    /**
     * creates PlayerValues Object
     * @param name : chosen name
     * @param figure : chosen robot figure, has to be unique
     */
    public PlayerValuesObj(String name, int figure){

        this.name = name;
        this.figure = figure;
    }


    public String getName(){
        return this.name;
    }

    public int getFigure(){
        return this.figure;
    }

    @Override
    public void action(Server server, ClientHandler task, PlayerValuesObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handlePlayerValues(server, task, bodyObject);

    }
}
