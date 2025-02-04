package utilities.objects;

import server.ClientHandler;
import server.Server;

public class EnergyObj {

    private int clientID;
    private int count;
    private String source;

    /**
     * creates Energy Object
     * @param clientID
     * @param count
     * @param source : PowerUpCard/EnergySpace
     */
    public EnergyObj(int clientID, int count, String source){
        this.clientID = clientID;
        this.count = count;
        this.source = source;
    }

    public int getClientID(){
        return this.clientID;
    }

    public int getCount(){
        return this.count;
    }

    public String getSource(){
        return this.source;
    }

}
