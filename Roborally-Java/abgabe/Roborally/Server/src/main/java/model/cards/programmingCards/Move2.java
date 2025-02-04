package model.cards.programmingCards;

import model.cards.Card;
import model.Player;
import server.Server;

public class Move2 extends Card {

    String cardName = "Move2";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {
        Move1 move1 = new Move1();
        for(int i = 0; i < 2 ; i++){
            move1.playCard(player,server);
        }
        player.addLastRegisterCard(cardName);

    }
}



