package utilities.objects;

import viewModel.Client;

public class DrawDamageObj implements ClientObjectHandler<DrawDamageObj>{

    private int clientID;
    private String[] cards;

    /**
     * creates DrawDamage Object
     * @param clientID
     * @param cards
     */
    public DrawDamageObj(int clientID, String[] cards){
        this.clientID = clientID;
        this.cards = cards;
    }

    public int getClientID(){
        return this.clientID;
    }

    public String[] getCards(){
        return this.cards;
    }

    @Override
    public void action(Client client, DrawDamageObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleDrawDamage(client, bodyObject);
    }
}
