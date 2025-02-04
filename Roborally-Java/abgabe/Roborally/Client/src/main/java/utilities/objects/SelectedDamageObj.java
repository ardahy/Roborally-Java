package utilities.objects;

import viewModel.Client;

public class SelectedDamageObj implements ClientObjectHandler<SelectedDamageObj>{

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
    public void action(Client client, SelectedDamageObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleSelectedDamage(client, bodyObject);
    }
}
