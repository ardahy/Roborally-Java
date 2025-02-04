package model.cards.programmingCards;

import model.Player;
import model.cards.Card;
import model.cards.damageCards.Spam;
import model.cards.damageCards.TrojanHorse;
import model.cards.damageCards.Virus;
import model.cards.damageCards.Worm;
import server.Server;

/**
 * This class is called when playing again
 * @author Ilinur
 */

public class Again extends Card {

    String cardName = "Again";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {
        String lastRegisterCard = player.getLastRegisterCard();

        switch (lastRegisterCard){

            case "Move1":
                Move1 move1 = new Move1();
                move1.playCard(player, server);
                break;

            case "Move2":
                Move2 move2 = new Move2();
                move2.playCard(player, server);
                break;

            case "Move3":
                Move3 move3 = new Move3();
                move3.playCard(player, server);
                break;

            case "BackUp":
                BackUp backUp = new BackUp();
                backUp.playCard(player, server);
                break;

            case "TurnLeft":
                TurnLeft turnLeft = new TurnLeft();
                turnLeft.playCard(player, server);
                break;

            case "TurnRight":
                TurnRight turnRight = new TurnRight();
                turnRight.playCard(player, server);
                break;

            case "UTurn":
                UTurn uTurn = new UTurn();
                uTurn.playCard(player, server);
                break;

            case "PowerUp":
                PowerUp powerUp = new PowerUp();
                powerUp.playCard(player, server);

            case "Spam":
                Spam spam = new Spam();
                spam.playCard(player, server);
                break;

            case "TrojanHorse":
                TrojanHorse trojanHorse = new TrojanHorse();
                trojanHorse.playCard(player, server);
                break;

            case "Virus":
                Virus virus = new Virus();
                virus.playCard(player, server);
                break;

            case "Worm":
                Worm worm = new Worm();
                worm.playCard(player, server);
                break;

        }
    }

}
