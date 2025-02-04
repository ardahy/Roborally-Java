package utilities.objects;

import viewModel.Client;

public class CardPlayedObj implements ClientObjectHandler<CardPlayedObj> {

    private int clientID;
    private String card;

    /**
     * creates CardPlayed Object
     * server sends played card to all clients
     * @param clientID
     * @param card
     */
    public CardPlayedObj(int clientID, String card) {
        this.clientID = clientID;
        this.card = card;
    }

    public int getClientID() {
        return this.clientID;
    }

    public String getCard() {
        return this.card;
    }

    @Override
    public void action(Client client, CardPlayedObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleCardPlayed(client, bodyObject);
    }

}
