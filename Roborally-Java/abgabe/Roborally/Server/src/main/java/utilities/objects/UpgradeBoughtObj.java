package utilities.objects;

import server.ClientHandler;
import server.Server;

public class UpgradeBoughtObj{

    private int clientID;
    private String card;

    /**
     * creates UpgradeBoughtObj Object
     * informs all players of the upgrade card the client has bought
     * @param clientID : the buyer's ID
     * @param card : the card the client bought
     */
    public UpgradeBoughtObj(int clientID, String card){
        this.clientID = clientID;
        this.card = card;
    }

    public int getClientID(){
        return this.clientID;
    }

    public String getCard(){
        return this.card;
    }

}


