package model.cards.programmingCards;

import model.cards.Card;
import model.Player;
import server.Server;

public class UTurn extends Card {

    String cardName = "UTurn";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {

        String direction = player.getRobotDirection();

        switch (direction) {

            case "north":
                player.setRobotDirection("south");
                break;

            case "south":
                player.setRobotDirection("north");
                break;

            case "east":
                player.setRobotDirection("west");
                break;

            case "west":
                player.setRobotDirection("east");
                break;
        }

        player.addLastRegisterCard(cardName);
    }
}

