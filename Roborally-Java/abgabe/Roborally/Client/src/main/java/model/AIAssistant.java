package model;

import java.io.*;
import java.util.Arrays;
import java.util.logging.*;
import model.clientgame.Position;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Class saves important inforamtion for AI
 * @author Benedikt
 */
public class AIAssistant {

    private String map = "empty";
    private boolean aIHasSetStartingPoint = false;
    private Position myStartingPoint;
    private static ArrayList<Position> nonTakenStartingPoints = new ArrayList<>();
    private boolean isAllowedToProgramm = true;
    private int round = 0;
    private static final Logger logger =
            Logger.getLogger(AIAssistant.class.getName());
    boolean append5 = true;
    private FileHandler handler5;


    /**
     * adds starting points to List nonTakenStartingPoints depending on which map was selected
     */
    public void buildStartingPoints(){
        logger.info("KI reserving starting point");
        if(map.equals("DeathTrap")){
            Position startPoint11_1 = new Position(11,1);
            nonTakenStartingPoints.add(startPoint11_1);
            Position startPoint12_3 = new Position(12,3);
            nonTakenStartingPoints.add(startPoint12_3);
            Position startPoint11_4 = new Position(11,4);
            nonTakenStartingPoints.add(startPoint11_4);
            Position startPoint11_5 = new Position(11,5);
            nonTakenStartingPoints.add(startPoint11_5);
            Position startPoint12_6 = new Position(12,6);
            nonTakenStartingPoints.add(startPoint12_6);
            Position startPoint11_8 = new Position(1,8);
            nonTakenStartingPoints.add(startPoint11_8);
        }
        else {
            Position startPoint1_1 = new Position(1,1);
            nonTakenStartingPoints.add(startPoint1_1);
            Position startPoint0_3 = new Position(0,3);
            nonTakenStartingPoints.add(startPoint0_3);
            Position startPoint1_4 = new Position(1,4);
            nonTakenStartingPoints.add(startPoint1_4);
            Position startPoint1_5 = new Position(1,5);
            nonTakenStartingPoints.add(startPoint1_5);
            Position startPoint0_6 = new Position(0,6);
            nonTakenStartingPoints.add(startPoint0_6);
            Position startPoint1_8 = new Position(1,8);
            nonTakenStartingPoints.add(startPoint1_8);
        }
        try {
            handler5 = new FileHandler("./logs/LoggingAIAssistant.log",
                    append5);
            logger.addHandler(handler5);
            SimpleFormatter formatter = new SimpleFormatter();
            handler5.setFormatter(formatter);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method builds preferrednstarting pointsfor Dizzy Highway
     */
    public void buildPreferredStartingPointsDizzy(){
        Position startPoint1_8 = new Position(0,3);
        nonTakenStartingPoints.add(startPoint1_8);
        Position startPoint0_6 = new Position(0,6);
        nonTakenStartingPoints.add(startPoint0_6);
        Position startPoint1_5 = new Position(1,5);
        nonTakenStartingPoints.add(startPoint1_5);
        Position startPoint1_4 = new Position(1,4);
        nonTakenStartingPoints.add(startPoint1_4);
        Position startPoint0_3 = new Position(1,8);
        nonTakenStartingPoints.add(startPoint0_3);
        Position startPoint1_1 = new Position(1,1);
        nonTakenStartingPoints.add(startPoint1_1);
    }

    /**
     * Removes a point from nonTakenStartingPoints
     * @param x The x coordinate of the point that is being removed
     * @param y The y coordinate of the point that is being removed
     */
    public void removeTakenStartingPoint(int x, int y){
        for(Position startingPoint : nonTakenStartingPoints){
            if(startingPoint.getX() == x && startingPoint.getY() == y){
                nonTakenStartingPoints.remove(startingPoint);
                break;
            }
        }
    }

    /**
     * Converts an JSON Array to String Array
     * @param jSONArray
     * @return
     */
    public String[] convertJsonArrayToStringArray(JSONArray jSONArray) {
        if(jSONArray == null) {
            return null;
        }
        String[] stringArray = new String[jSONArray.length()];
        for(int i=0; i<stringArray.length; i++) {
            stringArray[i] = jSONArray.optString(i);
        }
        return stringArray;
    }

    /**
     * If possible AI walks a certain path in DizzyHighway
     * @param cardsInHand
     * @return
     */
    public ArrayList<String> playDizzyHighwaySmartly(String[] cardsInHand){
        logger.info("KI plays the cards");
        round++;
        ArrayList<String> cardsForRegister = new ArrayList<>();
        ArrayList<String> cardsInHandList = new ArrayList<>(Arrays.asList(cardsInHand));
        if(map.equals("DizzyHighway")) {
            if (round == 1) {
                if (cardsInHandList.contains("Move3") && cardsInHandList.contains("Move1")) {
                    cardsForRegister.add("Move3");
                    cardsForRegister.add("Move1");
                    cardsInHandList.remove("Move3");
                    cardsInHandList.remove("Move1");
                } else if (cardsInHandList.contains("Move2") && cardsInHandList.contains("Move2")) {
                    cardsForRegister.add("Move2");
                    cardsForRegister.add("Move2");
                    cardsInHandList.remove("Move2");
                    cardsInHandList.remove("Move2");
                } else {
                    cardsForRegister.clear();
                }

                if (cardsForRegister.size() == 2 && cardsInHandList.contains("Move1")) {
                    cardsForRegister.add("Move1");
                    cardsInHandList.remove("Move1");
                } else {
                    cardsForRegister.clear();
                }

                if (cardsForRegister.size() == 3 && cardsInHandList.contains("TurnRight") && cardsInHandList.contains("TurnLeft")) {
                    cardsForRegister.add("TurnRight");
                    cardsForRegister.add("TurnLeft");
                    cardsInHandList.remove("TurnRight");
                    cardsInHandList.remove("TurnLeft");
                } else {
                    cardsForRegister.clear();
                }
            }

            if (round == 2) {
                if (checkEnoughMove1(cardsInHandList)) {
                    while (cardsInHandList.contains("Move1")) {
                        cardsForRegister.add("Move1");
                        cardsInHandList.remove("Move1");
                    }
                    int againCardsNeeded = 5 - cardsForRegister.size();
                    for (int i = 0; i < againCardsNeeded; i++) {
                        cardsForRegister.add("Again");
                        cardsInHandList.remove("Again");
                    }
                } else {
                    cardsForRegister.clear();
                }
            }

            if (round == 3) {
                if (cardsInHandList.contains("Move1")) {
                    cardsForRegister.add("Move1");
                    cardsInHandList.remove("Move1");
                    if (cardsInHandList.contains("Move1")) {
                        cardsForRegister.add("Move1");
                        cardsInHandList.remove("Move1");
                        for (int i = 0; i < 3; i++) {
                            cardsForRegister.add(cardsInHandList.get(0));
                            cardsInHandList.remove(0);
                        }
                    } else {
                        cardsForRegister.clear();
                    }
                } else {
                    cardsForRegister.clear();
                }
            }
        }

        return cardsForRegister;
    }

    /**
     *Checks whether AI has enough Move1 or Again
     * @param cardsInHandList
     * @return
     */
    public boolean checkEnoughMove1(ArrayList<String> cardsInHandList) {
        int countMove1 = 0;
        int countAgain = 0;

        for (String card : cardsInHandList) {
            if (card.equals("Move1")) {
                countMove1++;
            } else if (card.equals("Again")) {
                countMove1++;
            }
        }

        if ((countMove1 + countAgain) > 5) {
            return true;
        }
        return false;

    }

    public ArrayList<Position> getNonTakenStartingPoints() {
        return nonTakenStartingPoints;
    }

    public static void setNonTakenStartingPoints(ArrayList<Position> nonTakenStartingPoints) {
        AIAssistant.nonTakenStartingPoints = nonTakenStartingPoints;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Position getMyStartingPoint() {
        return myStartingPoint;
    }

    public void setMyStartingPoint(Position myStartingPoint) {
        this.myStartingPoint = myStartingPoint;
    }

}

