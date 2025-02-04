package utilities.objects;

import viewModel.Client;


public class ExchangeShopObj implements ClientObjectHandler<ExchangeShopObj>{

    private String[] cards;

    /**
     * creates ExchangeShopObj Object
     * server broadcasts to clients the cards that have been put into UpgradeShop
     * @param cards : contains new Upgrade cards
     */
    public ExchangeShopObj(String[] cards ){
        this.cards = cards;
    }

    public String[] getCards(){
        return cards;
    }

    @Override
    public void action(Client client, ExchangeShopObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleExchangeShop(client, bodyObject);
    }

}