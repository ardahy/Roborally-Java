package model.cards.damageCards;

import model.cards.Card;
import model.Player;
import server.Server;

/**
 * When you program a Trojan horse damage card, you must immediately take two SPAM damage cards
 */
public class TrojanHorse extends Card {

    String cardName = "Trojan Horse";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {

        Spam spam = new Spam();
        player.addInDiscardPile(spam.getCardName());
        player.addInDiscardPile(spam.getCardName());

        server.removeFromSpamPile(spam.getCardName());
        server.removeFromSpamPile(spam.getCardName());

        server.addInTrojanHorsePile(cardName);

    }
}
