package utilities.objects;


import viewModel.Client;

public class MovementObj implements ClientObjectHandler<MovementObj>{

    private int clientID;
    private int x;
    private int y;

    /**
     * creates Movement Object
     * @param clientID
     * @param x : number of moves in x-direction
     * @param y : number of moves in y-direction
     */
    public MovementObj(int clientID, int x, int y){
        this.clientID = clientID;
        this.x = x;
        this.y = y;
    }

    public int getClientID(){
        return this.clientID;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    @Override
    public void action(Client client, MovementObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleMovement(client, bodyObject);

    }
}
