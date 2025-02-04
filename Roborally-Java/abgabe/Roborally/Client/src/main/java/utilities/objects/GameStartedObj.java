package utilities.objects;

import viewModel.Client;
import model.field.Field;

import java.util.ArrayList;

public class GameStartedObj implements ClientObjectHandler<GameStartedObj> {
    private ArrayList<ArrayList<ArrayList<Field>>> mapBody;

    public GameStartedObj(ArrayList<ArrayList<ArrayList<Field>>> mapBody) {
        this.mapBody = mapBody;
    }

    public ArrayList<ArrayList<ArrayList<Field>>> getMap() {
        return mapBody;
    }

    @Override
    public void action(Client client, GameStartedObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleGameStarted(client, bodyObject);
    }
}
