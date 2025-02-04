package utilities.objects;

import viewModel.Client;

public class CheckpointMovedObj implements ClientObjectHandler<CheckpointMovedObj>{



    private int checkpointID;
    private int x;
    private int y;


    public CheckpointMovedObj(int checkpointID, int x, int y){
        this.checkpointID = checkpointID;
        this.x = x;
        this.y = y;
    }

    public  int getCheckpointID(){
        return this.checkpointID;
    }

    public  int getX(){
        return this.x;
    }
    public  int getY(){
        return this.y;
    }

    @Override
    public void action(Client client, CheckpointMovedObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleCheckpointMoved(client, bodyObject);
    }

}



