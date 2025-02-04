package model.cards.specialProgrammingCards;

import model.Player;
import model.cards.programmingCards.*;
import model.cards.Card;
import server.Server;

public class RepeatRoutine extends Card {

    String cardName = "Again";
    private String cardToPlay;


    @Override
    public String getCardName() {
        return cardName;
    }




    @Override
    public void playCard(Player player, Server server) {



        String lastRegisterCard = player.getLastRegisterCard();
        Move1 move1 = new Move1();
        Move2 move2 = new Move2();
        Move3 move3 = new Move3();
        BackUp backUp = new BackUp();
        TurnLeft turnLeft = new TurnLeft();
        TurnRight turnRight = new TurnRight();
        UTurn uTurn = new UTurn();
        PowerUp powerUp = new PowerUp();
        Again again = new Again();

        switch (lastRegisterCard){

            case "Move1":
                //Move1 move1 = new Move1();
                move1.playCard(player, server);
                break;

            case "Move2":
                //Move2 move2 = new Move2();
                move2.playCard(player, server);
                break;

            case "Move3":
                //Move3 move3 = new Move3();
                move3.playCard(player, server);
                break;

            case "BackUp":
                //BackUp backUp = new BackUp();
                backUp.playCard(player, server);
                break;

            case "TurnLeft":
                //TurnLeft turnLeft = new TurnLeft();
                turnLeft.playCard(player, server);
                break;

            case "TurnRight":
                //TurnRight turnRight = new TurnRight();
                turnRight.playCard(player, server);
                break;

            case "UTurn":
                //UTurn uTurn = new UTurn();
                uTurn.playCard(player, server);
                break;

            case "PowerUp":
                powerUp.playCard(player, server);


            case "Spam":
            case "TrojanHorse":
            case "Virus":
            case "Worm":

                do {
                    // TODO drawcard methode hinzufügen in Player
                   // String cardToPlayNow = player.drawCard();
                    // this.cardToPlay = cardToPlayNow;

                    if(cardToPlay.equals("Move1")){
                        move1.playCard(player, server);
                    }

                    if(cardToPlay.equals("Move2")){
                        move2.playCard(player, server);
                    }

                    if(cardToPlay.equals("Move3")){
                        move3.playCard(player, server);
                    }

                    if(cardToPlay.equals("TurnLeft")){
                        turnLeft.playCard(player, server);
                    }

                    if(cardToPlay.equals("TurnRight")){
                        turnRight.playCard(player, server);
                    }

                    if(cardToPlay.equals("UTurn")){
                        uTurn.playCard(player, server);
                    }

                    if(cardToPlay.equals("PowerUp")){
                        powerUp.playCard(player, server);
                    }

                    if(cardToPlay.equals("Again")){
                        again.playCard(player, server);
                    }

                }

                while (cardToPlay.equals("Spam") || cardToPlay.equals("TrojanHorse") || cardToPlay.equals("Virus") || cardToPlay.equals("Worm") );
                break;

        }
    }
}

