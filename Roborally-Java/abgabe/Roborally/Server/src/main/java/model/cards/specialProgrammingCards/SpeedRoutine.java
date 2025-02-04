package model.cards.specialProgrammingCards;

import model.Player;
import model.cards.Card;
import server.Server;


public class SpeedRoutine extends Card {

    String cardName = "SpeedRoutine";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {

        String direction = player.getRobotDirection();
        int xPosition = player.getRobotX();
        int yPosition = player.getRobotY();
        switch (direction) {

            case "north":
                if(nextMovePossible()) {
                    player.setRobotY(yPosition - 3);
                }
                break;

            case "south":
                if(nextMovePossible()) {
                    player.setRobotY(yPosition + 3);
                }
                break;

            case "east":
                if(nextMovePossible()) {
                    player.setRobotX(xPosition - 3);
                }
                break;

            case "west":
                if(nextMovePossible()) {
                    player.setRobotX(xPosition + 3);
                }
                break;

        }


    }
}


