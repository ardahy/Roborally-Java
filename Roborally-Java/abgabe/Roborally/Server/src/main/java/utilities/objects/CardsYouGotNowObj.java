package utilities.objects;

import server.ClientHandler;
import server.Server;

public class CardsYouGotNowObj {

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

}
