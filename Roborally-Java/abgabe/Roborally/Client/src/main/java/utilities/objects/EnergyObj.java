package utilities.objects;

import viewModel.Client;

public class EnergyObj implements ClientObjectHandler<EnergyObj>{

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

    @Override
    public void action(Client client, EnergyObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleEnergy(client, bodyObject);
    }
}
