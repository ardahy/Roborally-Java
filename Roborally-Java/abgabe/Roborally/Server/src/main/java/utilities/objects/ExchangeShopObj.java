package utilities.objects;

import server.ClientHandler;
import server.Server;


public class ExchangeShopObj {

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

}
