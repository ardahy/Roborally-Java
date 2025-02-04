package utilities.objects;

import server.ClientHandler;
import server.Server;

public class SelectedCardObj implements ServerObjectHandler<SelectedCardObj>{

    private String card;
    private int register;

    /**
     * creates SelectedCard Object
     * client sends selected card and register to server
     * @param card
     * @param register : register in which the card is put in
     */
    public SelectedCardObj(String card, int register){
        this.card = card;
        this.register = register;
    }

    public String getCard(){
        return this.card;
    }

    public int getRegister(){
        return this.register;
    }

    @Override
    public void action(Server server, ClientHandler task, SelectedCardObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleSelectedCard(server, task, bodyObject);

    }
}
