package utilities.objects;

import viewModel.Client;


public class YourCardsObj implements ClientObjectHandler<YourCardsObj>{

    private String[] cardsInHand;

    /**
     * creates YourCards Object
     * server sends private message to client with his cards
     * @param cardsInHand : contains client's cards
     */
    public YourCardsObj(String[] cardsInHand ){
        this.cardsInHand = cardsInHand;
    }
    public String[] getCardsInHand(){
        return cardsInHand;
    }

    @Override
    public void action(Client client, YourCardsObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleYourCards(client, bodyObject);
    }

}

