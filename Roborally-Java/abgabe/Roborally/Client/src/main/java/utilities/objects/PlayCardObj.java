package utilities.objects;

import viewModel.Client;

public class PlayCardObj implements ClientObjectHandler<PlayCardObj>{

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
    public void action(Client client, PlayCardObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handlePlayCard(client, bodyObject);
    }
}
