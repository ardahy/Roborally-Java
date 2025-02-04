package utilities.objects;

import viewModel.Client;

public class ReplaceCardObj implements ClientObjectHandler<ReplaceCardObj>{

    private int register;
    private String newCard;
    private int clientID;

    public ReplaceCardObj(int register, String newCard, int clientID){
        this.register = register;
        this.newCard = newCard;
        this.clientID = clientID;
    }

    public int getRegister(){
        return this.register;
    }

    public String getNewCard(){
        return this.newCard;
    }

    public int getClientID(){
        return this.clientID;
    }

    @Override
    public void action(Client client, ReplaceCardObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleReplaceCard(client, bodyObject);
    }
}