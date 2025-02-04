package model.cards.upgradeCards;

import model.Player;
import model.cards.Card;
import server.Server;
import utilities.objects.JSONMessage;
import utilities.objects.PlayCardObj;
import utilities.objects.YourCardsObj;

import java.util.ArrayList;

public class MemorySwap extends Card {

    String cardName = "MEMORY SWAP";

    @Override
    public String getCardName() {
        return cardName;
    }
    /**
     *
     * @param player current Player
     * @param server current Server
     * @author Bene, Ilinur
     */
    @Override
    public void playCard(Player player, Server server) {
        player.drawThreeCards();
        String[] cardsInHandPlusThree = new String[player.getCardsInHand().size()];
        ArrayList<String> cardsInHandPlusThreeList = player.getCardsInHand();
        int i = 0;
        for(String newCard : cardsInHandPlusThreeList){
            cardsInHandPlusThree[i] = newCard;
            i++;
        }

        JSONMessage jsonMessageYourCards = new JSONMessage("YourCards", new YourCardsObj(cardsInHandPlusThree));
        server.privateMessage(jsonMessageYourCards, player.getPlayerID());

        JSONMessage jsonMessagePlayMemorySwap = new JSONMessage("PlayCard", new PlayCardObj(cardName));
        server.privateMessage(jsonMessagePlayMemorySwap, player.getPlayerID());
    }

}
