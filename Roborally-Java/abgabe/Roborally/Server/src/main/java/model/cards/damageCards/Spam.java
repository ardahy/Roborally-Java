package model.cards.damageCards;

import model.Player;
import model.cards.Card;
import server.Server;

public class Spam extends Card {

    String cardName = "Spam";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {

        server.addInSpamPile(cardName);

    }

}
