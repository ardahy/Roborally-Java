package utilities.objects;

import server.ClientHandler;
import server.Server;

public class CheckpointMovedObj implements ServerObjectHandler<CheckpointMovedObj>{

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
    public void action(Server server, ClientHandler task, CheckpointMovedObj bodyObject, ServerMessageHandler messageHandler) {
        //messageHandler.handleCheckpointMoved(server, task, bodyObject);
        }

}
