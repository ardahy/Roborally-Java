package utilities.objects;

import server.ClientHandler;
import server.Server;

public class SelectedDamageObj implements ServerObjectHandler<SelectedDamageObj>{

    private String[] cards;

    /**
     * creates SelectedDamage Object
     * @param cards
     */
    public SelectedDamageObj(String[] cards){
        this.cards = cards;
    }

    public String[] getCards(){
        return this.cards;
    }

    @Override
    public void action(Server server, ClientHandler task, SelectedDamageObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleSelectedDamage(server, task, bodyObject);

    }
}
