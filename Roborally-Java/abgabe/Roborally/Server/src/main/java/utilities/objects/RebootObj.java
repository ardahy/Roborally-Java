package utilities.objects;

import server.ClientHandler;
import server.Server;

public class RebootObj {

    private int clientID;

    /**
     * creates Reboot Object
     * coordinates of Reboot field are sent per Movement Object
     * @param clientID
     */
    public RebootObj(int clientID){
        this.clientID = clientID;
    }

    public int getClientID(){
        return this.clientID;
    }

}
