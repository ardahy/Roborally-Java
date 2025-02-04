package utilities.objects;

import viewModel.Client;

public class NotYourCardsObj implements ClientObjectHandler<NotYourCardsObj> {

    private int clientID;
    private int cardsInHand;

    /**
     * creates NotYourCards Object
     * server broadcasts quantity of cards that another player has
     * @param clientID
     * @param cardsInHand
     */
    public NotYourCardsObj(int clientID, int cardsInHand) {
        this.clientID = clientID;
        this.cardsInHand = cardsInHand;
    }

    public int getClientID() {
        return this.clientID;
    }

    public int getCardsInHand() {
        return cardsInHand;
    }

    @Override
    public void action(Client client, NotYourCardsObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleNotYourCards(client, bodyObject);

    }

}

