package utilities.objects;


import viewModel.Client;

public class PlayerAddedObj implements ClientObjectHandler<PlayerAddedObj> {

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
    public PlayerAddedObj(int clientID, String name, int figure) {

        this.clientID = clientID;
        this.name = name;
        this.figure = figure;
    }

    public int getClientID() {
        return this.clientID;
    }

    public String getName() {
        return this.name;
    }

    public int getFigure() {
        return this.figure;
    }

    @Override
    public void action(Client client, PlayerAddedObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handlePlayerAdded(client, bodyObject);

    }
}
