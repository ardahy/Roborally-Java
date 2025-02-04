package utilities.objects;

import viewModel.Client;

public class CardsYouGotNowObj implements ClientObjectHandler<CardsYouGotNowObj>{

    private String[] cards;

    /**
     * creates CardsYouGotNow Object
     * empty registers are filled randomly
     * @param cards
     */
    public CardsYouGotNowObj(String[] cards){
        this.cards = cards;
    }

    public String[] getCards(){
        return this.cards;
    }

    @Override
    public void action(Client client, CardsYouGotNowObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleCardsYouGotNow(client, bodyObject);
    }
}
