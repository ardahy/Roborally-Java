package utilities.objects;

import viewModel.Client;
import java.util.ArrayList;

public class CurrentCardsObj implements ClientObjectHandler<CurrentCardsObj> {

    private ArrayList<ActiveCards> activeCards = new ArrayList<>();

    public ArrayList<ActiveCards> getActiveCards() {
        return activeCards;
    }

    /**
     * creates CurrentCards Object
     * server sends active cards to client
     * priority has to be calculated after every register
     * @param activeCards
     */
    public CurrentCardsObj(ArrayList<ActiveCards> activeCards) {
        this.activeCards = activeCards;
    }

    @Override
    public void action(Client client, CurrentCardsObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleCurrentCards(client, bodyObject);
    }

}
