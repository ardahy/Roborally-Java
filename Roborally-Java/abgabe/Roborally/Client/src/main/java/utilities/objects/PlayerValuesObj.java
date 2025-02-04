package utilities.objects;

import viewModel.Client;

public class PlayerValuesObj implements ClientObjectHandler<PlayerValuesObj> {

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
    public void action(Client client, PlayerValuesObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handlePlayerValues(client, bodyObject);

    }

}
