package utilities.objects;

import viewModel.Client;

public class SetStartingPointObj implements ClientObjectHandler<SetStartingPointObj>{

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
    public void action(Client client, SetStartingPointObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleSetStartingPoint(client, bodyObject);

    }
}
