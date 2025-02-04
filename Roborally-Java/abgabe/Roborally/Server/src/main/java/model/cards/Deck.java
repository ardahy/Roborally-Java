package model.cards;

import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.Collections ;

public class Deck{
    static ArrayList<String> cardsPlayer1 ;
    static ArrayList<String> cardsPlayer2 ;
    static ArrayList<String> cardsPlayer3 ;
    static ArrayList<String> cardsPlayer4 ;
    static ArrayList<String> cardsPlayer5 ;
    static ArrayList<String> cardsPlayer6 ;
    static ArrayList<String> permanentUpgradeCards ;
    static ArrayList<String> temporaryUpgradeCards ;
    static ArrayList<String> programmingCards ;
    static ArrayList<String> specialProgrammingCards ;
    static ArrayList<String> damageCards ;

    public Deck(){
        permanentUpgradeCards.add("ADMIN PRIVILEGE");
        permanentUpgradeCards.add("CORRUPTION WAVE");
        permanentUpgradeCards.add("BLUE SCREEN OF DEATH");
        permanentUpgradeCards.add("CRAB LEGS");
        permanentUpgradeCards.add("BRAKES");
        permanentUpgradeCards.add("DEFLECTOR SHIELD");
        permanentUpgradeCards.add("CACHE MEMORY");
        permanentUpgradeCards.add("DEFRAG GIZMO");
        permanentUpgradeCards.add("DOUBLE BARREL LASER");
        permanentUpgradeCards.add("MODULAR CHASSIS");
        permanentUpgradeCards.add("FIREWALL");
        permanentUpgradeCards.add("PRESSOR BEAM");
        permanentUpgradeCards.add("HOVER UNIT");
        permanentUpgradeCards.add("RAIL GUN");
        permanentUpgradeCards.add("MEMORY STICK");
        permanentUpgradeCards.add("RAMMING GEAR");
        permanentUpgradeCards.add("MINI HOWITZER");
        permanentUpgradeCards.add("REAR LASER");
        permanentUpgradeCards.add("SCRAMBLER");
        permanentUpgradeCards.add("TRACTOR BEAM");
        permanentUpgradeCards.add("SIDE ARMS");
        permanentUpgradeCards.add("TROJAN NEEDLER");
        permanentUpgradeCards.add("TELEPORTER");
        permanentUpgradeCards.add("VIRUS MODULE");

        String [] temporaryUpgrades ={"BOINK","HACK","ENERGY ROUTINE","MANUAL SORT","MEMORY SWAP","REPEAT ROUTINE",
                "REBOOT","SANDBOX ROUTINE","RECHARGE","SPAM BLOCKER","RECOMPILE","SPAM FOLDER ROUTINE","REFRESH","SPEED ROUTINE"
                ,"WEASEL ROUTINE","WEASEL ROUTINE","ZOOP"};
        temporaryUpgradeCards.addAll(Arrays.asList(temporaryUpgrades));

        String [] specialProgramming = {"ENERGY ROUTINE","SANDBOX ROUTINE","WEASEL ROUTINE","SPEED ROUTINE",
                "SPAM FOLDER","REPEAT ROUTINE"};
        specialProgrammingCards.addAll(Arrays.asList(specialProgramming));

        String [] programming = {"MOVE 1","MOVE 2","MOVE 3","TURN RIGHT","TURN LEFT","U-TURN","BACK UP","POWER UP",
                "AGAIN"};
        programmingCards.addAll(Arrays.asList(programming));

        String [] damage = {"SPAM","TROJAN HORSE","WORM","VIRUS"};
        damageCards.addAll(Arrays.asList(damage));

    }

    public static void shufflePermanent (){
        Collections.shuffle(permanentUpgradeCards);
    }
    public static void shuffleTemporary () {
        Collections.shuffle(temporaryUpgradeCards);
    }
    public static void shuffleDamage () {
        Collections.shuffle(damageCards);
    }
    public static void shuffleProgramming () {
        Collections.shuffle(programmingCards);
    }
    public static void shuffleSpecialProgramming () {
        Collections.shuffle(specialProgrammingCards);
    }

    public static void handOutProgrammingCards(int playerNumber){
        switch(playerNumber){

            case 1:
                for(int i = 1;i<=9;i++){
                    cardsPlayer1.add(programmingCards.get(0));
                    programmingCards.remove(0);
                }
            case 2:
                for(int i = 1;i<=9;i++){
                    cardsPlayer2.add(programmingCards.get(0));
                    programmingCards.remove(0);
                }
            case 3:
                for(int i = 1;i<=9;i++){
                    cardsPlayer3.add(programmingCards.get(0));
                    programmingCards.remove(0);
                }
            case 4:
                for(int i = 1;i<=9;i++){
                    cardsPlayer4.add(programmingCards.get(0));
                    programmingCards.remove(0);
                }
            case 5:
                for(int i = 1;i<=9;i++){
                    cardsPlayer5.add(programmingCards.get(0));
                    programmingCards.remove(0);
                }
            case 6:
                for(int i = 1;i<=9;i++){
                    cardsPlayer6.add(programmingCards.get(0));
                    programmingCards.remove(0);
                }
        }
    }

    public static void chooseProgrammingCards(int playerNumber,int first,int second,int third,int fourth,int fifth){
        ArrayList <String> chosenCards = new ArrayList<String>();
        switch(playerNumber){
            case 1:
                chosenCards.add(cardsPlayer1.get(first));
                chosenCards.add(cardsPlayer1.get(second));
                chosenCards.add(cardsPlayer1.get(third));
                chosenCards.add(cardsPlayer1.get(fourth));
                chosenCards.add(cardsPlayer1.get(fifth));
                cardsPlayer1 = chosenCards;

            case 2:
                chosenCards.add(cardsPlayer2.get(first));
                chosenCards.add(cardsPlayer2.get(second));
                chosenCards.add(cardsPlayer2.get(third));
                chosenCards.add(cardsPlayer2.get(fourth));
                chosenCards.add(cardsPlayer2.get(fifth));
                cardsPlayer2 = chosenCards;

            case 3:
                chosenCards.add(cardsPlayer3.get(first));
                chosenCards.add(cardsPlayer3.get(second));
                chosenCards.add(cardsPlayer3.get(third));
                chosenCards.add(cardsPlayer3.get(fourth));
                chosenCards.add(cardsPlayer3.get(fifth));
                cardsPlayer3 = chosenCards;

            case 4:
                chosenCards.add(cardsPlayer4.get(first));
                chosenCards.add(cardsPlayer4.get(second));
                chosenCards.add(cardsPlayer4.get(third));
                chosenCards.add(cardsPlayer4.get(fourth));
                chosenCards.add(cardsPlayer4.get(fifth));
                cardsPlayer4 = chosenCards;

            case 5:
                chosenCards.add(cardsPlayer5.get(first));
                chosenCards.add(cardsPlayer5.get(second));
                chosenCards.add(cardsPlayer5.get(third));
                chosenCards.add(cardsPlayer5.get(fourth));
                chosenCards.add(cardsPlayer5.get(fifth));
                cardsPlayer5 = chosenCards;

            case 6:
                chosenCards.add(cardsPlayer6.get(first));
                chosenCards.add(cardsPlayer6.get(second));
                chosenCards.add(cardsPlayer6.get(third));
                chosenCards.add(cardsPlayer6.get(fourth));
                chosenCards.add(cardsPlayer6.get(fifth));
                cardsPlayer6 = chosenCards;
        }

    }








}
