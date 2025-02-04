package utilities.objects;

import server.ClientHandler;
import server.Server;

public class TimerEndedObj {

    private int[] clientIDs;

    /**
     * creates TimerEnded Object
     * server broadcasts when timer is over
     * clients that did not finish filling their registers are also broadcasted
     * @param clientIDs
     */
    public TimerEndedObj(int[] clientIDs){
        this.clientIDs = clientIDs;
    }

    public int[] getClientIDs(){
        return this.clientIDs;
    }

}
