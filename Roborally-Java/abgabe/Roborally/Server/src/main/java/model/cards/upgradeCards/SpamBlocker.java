package model.cards.upgradeCards;

import model.Player;
import model.cards.Card;
import server.Server;

public class SpamBlocker extends Card {

    String cardName = "SPAM BLOCKER";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {

        player.removeSpamCards();
        player.replaceWithDeck();


    }
}

