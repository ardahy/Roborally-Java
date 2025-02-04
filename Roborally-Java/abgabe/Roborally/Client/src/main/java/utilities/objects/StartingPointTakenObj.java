package utilities.objects;

import viewModel.Client;

public class StartingPointTakenObj implements ClientObjectHandler<StartingPointTakenObj>{

    private int x;
    private int y;
    private int clientID;

    /**
     * creates StartingPointTaken Object
     * server sends valid starting point to all clients
     * @param x : x-coordinate of starting point
     * @param y : y-coordinate of starting point
     * @param clientID : ID of client that has chosen starting point
     */
    public StartingPointTakenObj(int x, int y, int clientID){
        this.x = x;
        this.y = y;
        this.clientID = clientID;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getClientID(){
        return this.clientID;
    }

    @Override
    public void action(Client client, StartingPointTakenObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleStartingPointTaken(client, bodyObject);

    }
}
