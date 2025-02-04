package model.cards.damageCards;

import model.cards.Card;
import model.Player;
import server.Server;

/**
 * When you program a worm damage card, you must immediately reboot your robot
 */
public class Worm extends Card {

    String cardName = "Worm";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {


        server.addInWormPile(cardName);

    }
}
