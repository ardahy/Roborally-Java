package model.cards.damageCards;

import model.cards.Card;
import model.Player;
import server.Server;

/**
 * When you program a virus damage card, any robot on the board within a six-space radius of you
 * must immediately take a virus card from the draw pile.
 */
public class Virus extends Card {

    String cardName = "Virus";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {

        if(server.isInSixSpaceRadius(player)){
            player.addCardInHand(cardName);
            server.removeFromSpamPile(cardName);
        }

        server.addInVirusPile(cardName);

    }
}
