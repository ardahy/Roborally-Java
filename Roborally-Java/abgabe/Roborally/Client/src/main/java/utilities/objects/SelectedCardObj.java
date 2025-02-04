package utilities.objects;

import viewModel.Client;

public class SelectedCardObj implements ClientObjectHandler<SelectedCardObj>{

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
    public void action(Client client, SelectedCardObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleSelectedCard(client, bodyObject);
    }
}
