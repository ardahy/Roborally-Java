package utilities.objects;

import server.ClientHandler;
import server.Server;

public class NotYourCardsObj{

    private int clientID;
    private int cardsInHand;

    /**
     * creates NotYourCards Object
     * server broadcasts quantity of cards that another player has
     * @param clientID
     * @param cardsInHand
     */
    public NotYourCardsObj(int clientID, int cardsInHand){
        this.clientID = clientID;
        this.cardsInHand = cardsInHand;
    }

    public int getClientID(){
        return this.clientID;
    }

    public int getCardsInHand(){
        return cardsInHand;
    }

}

