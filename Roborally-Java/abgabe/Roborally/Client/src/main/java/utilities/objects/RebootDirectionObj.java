package utilities.objects;

import viewModel.Client;

public class RebootDirectionObj implements ClientObjectHandler<RebootDirectionObj> {

    private String direction;

    /**
     * creates RebootDirection Object
     * client chooses direction of robot
     * @param direction : top/right/bottom/left
     */
    public RebootDirectionObj(String direction){
        this.direction = direction;
    }

    public String getDirection(){
        return this.direction;
    }

    @Override
    public void action(Client client, RebootDirectionObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleRebootDirection(client, bodyObject);
    }
}
