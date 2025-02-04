package model.cards.specialProgrammingCards;

import model.Player;
import model.cards.Card;
import server.Server;


public class SpamFolder extends Card {

    String cardName = "SpamFolder";
    private Server server;

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {

        player.getDiscardPile().remove("Spam");

        server.addInSpamPile("Spam");

    }
}

