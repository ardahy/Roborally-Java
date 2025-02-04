package utilities.objects;

import server.ClientHandler;
import server.Server;

public class MapSelectedObj implements ServerObjectHandler<MapSelectedObj>{
    
    private String map;

    /**
     * creates MapSelected Object
     * first ready player chooses map
     * @param map : DizzyHighway, DeathTrap, ExtraCrispy, LostBearings
     */
    public MapSelectedObj(String map){
        this.map = map;
    }

    public String getMap(){
        return map;
    }

    @Override
    public void action(Server server, ClientHandler task, MapSelectedObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleMapSelected(server, task, bodyObject);

    }
}
