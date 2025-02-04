package utilities.objects;

import server.ClientHandler;
import server.Server;

public class PickDamageObj {

    private int count;

    private String[] availablePiles;

    /**
     * creates PickDamage Object
     * @param count
     */
    public PickDamageObj(int count, String[] availablePiles){
        this.count = count;
        this.availablePiles = availablePiles;
    }

    public int getCount(){
        return this.count;
    }

    public String[] getAvailablePiles(){
        return this.availablePiles;
    }

}


