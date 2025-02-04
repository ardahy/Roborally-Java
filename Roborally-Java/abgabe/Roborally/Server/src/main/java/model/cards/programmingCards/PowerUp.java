
package model.cards.programmingCards;

import model.cards.Card;
import model.Player;
import server.Server;
import utilities.objects.EnergyObj;
import utilities.objects.JSONMessage;

/**
 * This class is called when playing PowerUp
 * @author Ilinur
 */

public class PowerUp extends Card {

    String cardName = "PowerUp";

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {

        player.addEnergyCubes();
        server.removeEnergyCubeBank();
        EnergyObj getEnergyObj = new EnergyObj(player.getPlayerID(), player.getEnergyCubes(), "PowerUpCard");
        JSONMessage jsonMessageEnergyCubes = new JSONMessage("Energy", getEnergyObj );
        server.privateMessage(jsonMessageEnergyCubes, player.getPlayerID());


        player.addLastRegisterCard(cardName);


    }

}


