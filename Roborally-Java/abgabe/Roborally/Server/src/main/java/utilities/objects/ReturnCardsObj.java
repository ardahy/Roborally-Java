package utilities.objects;

import server.ClientHandler;
import server.Server;

public class ReturnCardsObj implements ServerObjectHandler<ReturnCardsObj>{

    private String[] cards;

    public ReturnCardsObj(String[] cards){
        this.cards = cards;
    }
    public String[] getCards(){
        return this.cards;
    }

    @Override
    public void action(Server server, ClientHandler task, ReturnCardsObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handleReturnCards(server, task, bodyObject);

    }
}
