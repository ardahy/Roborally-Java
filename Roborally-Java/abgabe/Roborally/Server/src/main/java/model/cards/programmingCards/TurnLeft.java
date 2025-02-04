package model.cards.programmingCards;

import model.cards.Card;
import model.Player;
import server.Server;

public class TurnLeft extends Card {

    String cardName = "TurnLeft";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {

        String direction = player.getRobotDirection();

        switch (direction) {

            case "north":
                player.setRobotDirection("west");
                break;

            case "south":
                player.setRobotDirection("east");
                break;

            case "east":
                player.setRobotDirection("north");
                break;

            case "west":
                player.setRobotDirection("south");
                break;

        }

        player.addLastRegisterCard(cardName);
    }
}

