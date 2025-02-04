package utilities.objects;

import viewModel.Client;

public class SetStatusObj implements ClientObjectHandler<SetStatusObj>{

    private boolean ready;

    /**
     * creates SetStatus Object
     * client chooses if he is ready or not ready
     * @param ready : true/false
     */
    public SetStatusObj(boolean ready){
        this.ready = ready;
    }

    public boolean getReady(){
        return this.ready;
    }

    @Override
    public void action(Client client, SetStatusObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleSetStatus(client, bodyObject);

    }

}
