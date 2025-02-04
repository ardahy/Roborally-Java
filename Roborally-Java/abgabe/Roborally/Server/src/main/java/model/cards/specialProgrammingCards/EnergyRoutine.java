package model.cards.specialProgrammingCards;

import model.Player;
import model.cards.Card;
import server.Server;

public class EnergyRoutine extends Card {

    private Server server;

    String cardName = "EnergyRoutine";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {

        server.removeEnergyCubeBank();
        player.addEnergyCubes();
    }

}

