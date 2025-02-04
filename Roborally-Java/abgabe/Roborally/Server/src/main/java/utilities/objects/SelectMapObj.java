package utilities.objects;

import server.ClientHandler;
import server.Server;

public class SelectMapObj {

    private String[] availableMaps;

    /**
     * creates SelectMap Object
     * server sends Object to the first player that is ready
     * @param availableMaps : DizzyHighway, DeathTrap, ExtraCrispy, LostBearings
     */
    public SelectMapObj(String[] availableMaps) {
        this.availableMaps = availableMaps;
    }

    public String[] getAvailableMaps(){
        return this.availableMaps;
    }

}
