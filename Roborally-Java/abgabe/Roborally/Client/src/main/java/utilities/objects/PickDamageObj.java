package utilities.objects;

import viewModel.Client;

public class PickDamageObj implements ClientObjectHandler<PickDamageObj>{

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

    @Override
    public void action(Client client, PickDamageObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handlePickDamage(client, bodyObject);
    }
}

