package utilities.objects;

import server.ClientHandler;
import server.Server;

public class CardSelectedObj {

    private int clientID;
    private int register;
    private boolean filled;

    /**
     * creates CardSelected Object
     * server broadcast if register was filled or emptied
     * @param clientID
     * @param register
     * @param filled : true/false
     */
    public CardSelectedObj(int clientID, int register, boolean filled){
        this.clientID = clientID;
        this.register = register;
        this.filled = filled;
    }

    public int getClientID(){
        return this.clientID;
    }

    public int getRegister(){
        return this.register;
    }

    public boolean getFilled(){
        return this.filled;
    }

}
