package utilities.objects;

import viewModel.Client;

public class MapSelectedObj implements ClientObjectHandler<MapSelectedObj> {

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
        return this.map;
    }

    @Override
    public void action(Client client, MapSelectedObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleMapSelected(client, bodyObject);

    }
}
