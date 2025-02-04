package utilities.objects;

import server.ClientHandler;
import server.Server;


public class YourCardsObj {

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

}
