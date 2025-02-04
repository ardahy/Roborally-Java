package utilities.objects;

import server.ClientHandler;
import server.Server;

public class PlayCardObj implements ServerObjectHandler<PlayCardObj>{

    private String card;

    /**
     * creates PlayCard Object
     * client chooses card to play
     * @param card
     */
    public PlayCardObj(String card){
        this.card = card;
    }

    public String getCard(){
        return this.card;
    }

    @Override
    public void action(Server server, ClientHandler task, PlayCardObj bodyObject, ServerMessageHandler messageHandler) {
        messageHandler.handlePlayCard(server, task, bodyObject);

    }
}