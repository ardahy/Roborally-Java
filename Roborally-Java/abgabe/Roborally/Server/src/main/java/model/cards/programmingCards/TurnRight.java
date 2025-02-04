package model.cards.programmingCards;

import model.cards.Card;
import model.Player;
import server.Server;

public class TurnRight extends Card {

    String cardName = "TurnRight";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {

        String direction = player.getRobotDirection();

        switch (direction) {

            case "north":
                player.setRobotDirection("east");
                break;

            case "south":
                player.setRobotDirection("west");
                break;

            case "east":
                player.setRobotDirection("south");
                break;

            case "west":
                player.setRobotDirection("north");
                break;

        }

        player.addLastRegisterCard(cardName);
    }
}
