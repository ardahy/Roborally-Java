package utilities.objects;

import viewModel.Client;

public class RebootObj implements ClientObjectHandler<RebootObj> {

    private int clientID;

    /**
     * creates Reboot Object
     * coordinates of Reboot field are sent per Movement Object
     * @param clientID
     */
    public RebootObj(int clientID) {
        this.clientID = clientID;
    }

    public int getClientID() {
        return this.clientID;
    }

    @Override
    public void action(Client client, RebootObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleReboot(client, bodyObject);
    }

}
