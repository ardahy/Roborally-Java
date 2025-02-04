package utilities.objects;

import server.ClientHandler;
import server.Server;


public class RefillShopObj {

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


}
