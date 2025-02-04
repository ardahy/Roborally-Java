package utilities.objects;

import server.ClientHandler;
import server.Server;

public class ChooseRegisterObj implements ServerObjectHandler<ChooseRegisterObj>{

    private int register;

    public ChooseRegisterObj(int register){
        this.register = register;
    }

    public int getRegister(){
        return this.register;
    }
    @Override
    public void action(Server server, ClientHandler task, ChooseRegisterObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleChooseRegister(server, task, bodyObject);
    }
}
