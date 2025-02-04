package utilities.objects;

import viewModel.Client;


public class RefillShopObj implements ClientObjectHandler<RefillShopObj>{

    private String[] cards;

    /**
     * creates RefillShop Object
     * server broadcasts to clients the cards that have been put into UpgradeShop
     * @param cards : contains new Upgrade cards
     */
    public RefillShopObj(String[] cards ){
        this.cards = cards;
    }

    public String[] getCards(){
        return cards;
    }

    @Override
    public void action(Client client, RefillShopObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleRefillShop(client, bodyObject);
    }

}
