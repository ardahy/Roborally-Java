package utilities.objects;

import viewModel.Client;

public class CardSelectedObj implements ClientObjectHandler<CardSelectedObj>{

    private int clientID;
    private int register;
    private boolean filled;

    /**
     * creates CardSelected Object
     * server broadcast if register was filled or emptied
     * @param clientID
     * @param register
     * @param filled : true/false
     */
    public CardSelectedObj(int clientID, int register, boolean filled){
        this.clientID = clientID;
        this.register = register;
        this.filled = filled;
    }

    public int getClientID(){
        return this.clientID;
    }

    public int getRegister(){
        return this.register;
    }

    public boolean getFilled(){
        return this.filled;
    }

    @Override
    public void action(Client client, CardSelectedObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleCardSelected(client, bodyObject);
    }


}
