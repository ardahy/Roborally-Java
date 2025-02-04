package utilities.objects;

import viewModel.Client;

public class PlayerTurningObj  implements ClientObjectHandler<PlayerTurningObj> {

    private int clientID;
    private String rotation;

    /**
     * creates PlayerTurning Object
     * @param clientID
     * @param rotation : clockwise/counterclockwise
     */
    public PlayerTurningObj(int clientID, String rotation){
        this.clientID = clientID;
        this.rotation = rotation;
    }

    public int getClientID(){
        return this.clientID;
    }

    public String getRotation(){
        return this.rotation;
    }

    @Override
    public void action(Client client, PlayerTurningObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handlePlayerTurning(client, bodyObject);
    }

}
