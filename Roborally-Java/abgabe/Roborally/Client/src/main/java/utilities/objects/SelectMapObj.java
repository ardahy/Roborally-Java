package utilities.objects;

import viewModel.Client;

public class SelectMapObj implements ClientObjectHandler<SelectMapObj>{

    private String[] availableMaps;

    /**
     * creates SelectMap Object
     * server sends Object to the first player that is ready
     * @param availableMaps : DizzyHighway, DeathTrap, ExtraCrispy, LostBearings
     */
    public SelectMapObj(String[] availableMaps){
        this.availableMaps = availableMaps;
    }

    public String[] getAvailableMaps(){
        return this.availableMaps;
    }

    @Override
    public void action(Client client, SelectMapObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleSelectMap(client, bodyObject);

    }
}
