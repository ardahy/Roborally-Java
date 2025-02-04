package utilities.objects;

import server.ClientHandler;
import server.Server;

public class DrawDamageObj {

    private int clientID;
    private String[] cards;

    /**
     * creates DrawDamage Object
     * @param clientID
     * @param cards
     */
    public DrawDamageObj(int clientID, String[] cards){
        this.clientID = clientID;
        this.cards = cards;
    }

    public int getClientID(){
        return this.clientID;
    }

    public String[] getCards(){
        return this.cards;
    }

}
