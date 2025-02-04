package model;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.field.Field;

import utilities.Converter;
import utilities.objects.GameStartedObj;
import utilities.objects.JSONMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class Model to restore all data
 * @author Ilinur, Arda, Aigerim, Rea, Benedikt
 */
public class Model {
    private int clientID;
    private int firstReadyPlayerID;
    private IntegerProperty firstReadyPlayerIDProperty;
    private StringProperty messageHistory;
    private ObservableList<String> yourCardsInHand;
    private ObservableList<String> upgradeCards;
    private ObservableList<String> damageCards;
    private StringProperty newCardInRegister0;
    private StringProperty newCardInRegister1;
    private StringProperty newCardInRegister2;
    private StringProperty newCardInRegister3;
    private StringProperty newCardInRegister4;
    private IntegerProperty currentPlayerID;
    private int currentPlayerIDInteger;
    private BooleanProperty readyButtonVisible;
    private BooleanProperty notReadyButtonVisible;
    private BooleanProperty readyTextVisible;
    private BooleanProperty nextCardButtonVisible;
    private BooleanProperty startPoint1;
    private BooleanProperty startPoint2;
    private BooleanProperty startPoint3;
    private BooleanProperty startPoint4;
    private BooleanProperty startPoint5;
    private BooleanProperty startPoint6;
    private StringProperty userName;
    private IntegerProperty energyCubes;
    private IntegerProperty checkPoints;
    private StringProperty renameRobot;
    private StringProperty checkPointMoved;
    private BooleanProperty startButton;


    private int x = 0;
    private int y = 0;
    private String robotColor;
    private int robotID;
    private ArrayList<String> currentPositions = new ArrayList<>();
    private ArrayList<String> temporaryUpgradeCardList = new ArrayList<>();
    private ArrayList<String> permanentUpgradeCardList = new ArrayList<>();
    private ObservableList<String> myUpgradeCardList;
    private ObservableList<String> currentPositionsObservable;
    private String cardName;
    private String upgradeCardName;
    private String upgradeCardInGap1Name;
    private String upgradeCardInGap2Name;
    private String upgradeCardInGap3Name;
    private String upgradeCardInGap4Name;
    private String upgradeCardInGap5Name;
    private String upgradeCardInGap6Name;
    private int register = 0;
    private int currentRegister = 0;
    private StringProperty card;
    private StringProperty c0;
    private StringProperty c1;
    private StringProperty c2;
    private StringProperty c3;
    private StringProperty c4;
    private StringProperty upgradeCard;
    private StringProperty upgradeCardInGap1;
    private StringProperty upgradeCardInGap2;
    private StringProperty upgradeCardInGap3;
    private StringProperty upgradeCardInGap4;
    private StringProperty upgradeCardInGap5;
    private StringProperty upgradeCardInGap6;

    //TODO new
    private StringProperty phaseInfo;
    private StringProperty toDoInfo;


    private StringProperty sProp;
    private StringProperty position;
    private boolean register0isEmpty = true;
    private boolean register1isEmpty = true;
    private boolean register2isEmpty = true;
    private boolean register3isEmpty = true;
    private boolean register4isEmpty = true;
    Boolean[] emptyRegisters = new Boolean[5];
    private ListProperty<String> list;
    private ListProperty<String> upgradeShop;
    private ListProperty<String> listPosition;
    private ListProperty<String> myUpgradeCardsListProperty;
    private ListProperty<String> damageCardList;
    private int phase = 0;
    private ObservableList<String> maps = FXCollections.observableArrayList("DizzyHighway", "ExtraCrispy", "LostBearings", "DeathTrap", "Twister");

    public Model() {
        messageHistory = new SimpleStringProperty("");
        newCardInRegister0 = new SimpleStringProperty("");
        newCardInRegister1 = new SimpleStringProperty("");
        newCardInRegister2 = new SimpleStringProperty("");
        newCardInRegister3 = new SimpleStringProperty("");
        newCardInRegister4 = new SimpleStringProperty("");
        yourCardsInHand = FXCollections.observableArrayList();
        myUpgradeCardList = FXCollections.observableArrayList();
        upgradeCards = FXCollections.observableArrayList();
        currentPositionsObservable = FXCollections.observableArrayList();
        damageCards = FXCollections.observableArrayList();
        currentPlayerID = new SimpleIntegerProperty();
        readyButtonVisible = new SimpleBooleanProperty(false);
        notReadyButtonVisible = new SimpleBooleanProperty(false);
        readyTextVisible = new SimpleBooleanProperty(false);
        nextCardButtonVisible = new SimpleBooleanProperty(false);
        position = new SimpleStringProperty();
        sProp = new SimpleStringProperty();
        c0 = new SimpleStringProperty();
        c1 = new SimpleStringProperty();
        c2 = new SimpleStringProperty();
        c3 = new SimpleStringProperty();
        c4 = new SimpleStringProperty();
        upgradeCard = new SimpleStringProperty();
        upgradeCardInGap1 = new SimpleStringProperty();
        upgradeCardInGap2 = new SimpleStringProperty();
        upgradeCardInGap3 = new SimpleStringProperty();
        upgradeCardInGap4 = new SimpleStringProperty();
        upgradeCardInGap5 = new SimpleStringProperty();
        upgradeCardInGap6 = new SimpleStringProperty();
        card = new SimpleStringProperty();
        startPoint1 = new SimpleBooleanProperty(true);
        startPoint2 = new SimpleBooleanProperty(true);
        startPoint3 = new SimpleBooleanProperty(true);
        startPoint4 = new SimpleBooleanProperty(true);
        startPoint5 = new SimpleBooleanProperty(true);
        startPoint6 = new SimpleBooleanProperty(true);
        phaseInfo = new SimpleStringProperty();
        toDoInfo = new SimpleStringProperty();
        userName = new SimpleStringProperty();
        energyCubes = new SimpleIntegerProperty();
        checkPoints = new SimpleIntegerProperty();
        renameRobot = new SimpleStringProperty();
        checkPointMoved = new SimpleStringProperty();
        startButton = new SimpleBooleanProperty(false);
    }

    /**
     * save chat history
     * @param message
     * @author Aigerim
     */
    public void receiveMessage(String message) {
        String oldMessageHistory = messageHistory.get();
        String newMessageHistory = oldMessageHistory + " \n " + message;
        messageHistory.setValue(newMessageHistory);
    }

    /**
     * set cards in register
     * @param card
     * @param register
     * @author Benedikt
     */
    public void setNewCardInRegister(String card, int register) {
        switch (register){
            case 0:
                newCardInRegister0.setValue(card);
                register0isEmpty = card.equals("Null");
                break;
            case 1:
                newCardInRegister1.setValue(card);
                register1isEmpty = card.equals("Null");
                break;
            case 2:
                newCardInRegister2.setValue(card);
                register2isEmpty = card.equals("Null");
                break;
            case 3:
                newCardInRegister3.setValue(card);
                register3isEmpty = card.equals("Null");
                break;
            case 4:
                newCardInRegister4.setValue(card);
                register4isEmpty = card.equals("Null");
                break;

        }
    }

    /**
     * get chat history
     * @return messageHistory
     * @author Aigerim
     */
    public StringProperty getMessageHistory() {
        return messageHistory;
    }

    /**
     * get client's ID
     * @return
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * save client's ID
     * @param clientID
     */
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    /**
     * Builds a List of boolean values, each representing whether a register is empty or not
     */
    public void buildNextEmptyRegisterList() {
        emptyRegisters[0] = register0isEmpty;
        emptyRegisters[1] = register1isEmpty;
        emptyRegisters[2] = register2isEmpty;
        emptyRegisters[3] = register3isEmpty;
        emptyRegisters[4] = register4isEmpty;
    }

    /**
     * Sets the boolean for every register whether it is empty to true if a new round starts
     */
    public void setAllRegistersEmptyToTrue(){
        register0isEmpty = true;
        register1isEmpty = true;
        register2isEmpty = true;
        register3isEmpty = true;
        register4isEmpty = true;
    }

    /**
     * Searches the EmptyRegisters List for the next empty register, returns the index and sets it to false
     * @return index of the next empty register
     */
    public int findNextEmptyRegister(){
        for(int i = 0; i<emptyRegisters.length; i++) {
            if (emptyRegisters[i]) {
                emptyRegisters[i] = false;
                return i;
            }
        }
        return 5;
    }


    /**
     * save first ready player ID number
     * @param ID
     */
    public void setFirstReadyPlayerIDProperty(int ID) {
        firstReadyPlayerIDProperty = new SimpleIntegerProperty(ID);
    }

    /**
     * get first ready player ID for choose the map function
     * @return
     */
    public int getFirstReadyPlayerID() {
        return firstReadyPlayerID;
    }

    /**
     * get converted ObservableArrayList to ListProperty
     * @return
     * @auhtor Aigerim
     */
    public ListProperty<String> getYourCardsInHandProperty() {
        list = new SimpleListProperty<>(yourCardsInHand);
        return list;
    }

    public ObservableList<String> getYourCardsInHand(){
        return yourCardsInHand;
    }


    public ListProperty<String> getAvailableDamageCardsProperty() {
        damageCardList = new SimpleListProperty<>(damageCards);
        return damageCardList;
    }

    public ObservableList<String> getAvailableDamageCards(){
        return damageCards;
    }

    public void setAvailableDamageCards(String[] damageCard) {
        damageCards.clear();
        damageCards.addAll(Arrays.asList(damageCard));
        if(damageCard.length != 0) {
            damageCards.add("Null");
        }
    }

    public ListProperty<String> getUpgradeShopProperty() {
        upgradeShop = new SimpleListProperty<>(upgradeCards);
        return upgradeShop;
    }

    public ListProperty<String> getMyUpgradeCardsProperty() {
        myUpgradeCardsListProperty = new SimpleListProperty<>(myUpgradeCardList);
        return myUpgradeCardsListProperty;
    }

    public ListProperty<String> getCurrentPositionsProperty(){
        listPosition = new SimpleListProperty<>(currentPositionsObservable);
        return listPosition;
    }

    /**
     *save 9 cards in ObservableArrayList
     * @param yourCardsInHands
     * @author Aigerim
     */
    public void setYourCardsInHand(String[] yourCardsInHands) {
        yourCardsInHand.clear();
        yourCardsInHand.addAll(Arrays.asList(yourCardsInHands));
        if(yourCardsInHands.length != 0) {
            yourCardsInHand.add("Null");
        }
    }

    /**
     * Sets the upgrade shop in Gui
     * @param upgradeCards
     * @author Benedikt
     */
    public void setUpgradeShop(String[] upgradeCards) {
        this.upgradeCards.clear();
        this.upgradeCards.addAll(Arrays.asList(upgradeCards));
        this.upgradeCards.add("Null");
    }

    /**
     * clear list of update cards in observable list
     * @author Aigerim
     */
    public void clearUpdateShop() {
        this.upgradeCards.clear();
    }


    /**
     * get maps as nested ArrayList
     * @return
     * @author Aigerim
     */
    public ArrayList<ArrayList<ArrayList<Field>>> map() {
        try {
            Path maps = Paths.get("Client/src/main/resources/DizzyHighway.json");
            String mapJSON = Files.readString(maps, StandardCharsets.UTF_8);
            JSONMessage jsonMessage = Converter.deserializeJSON(mapJSON);
            GameStartedObj gameStartedBody = (GameStartedObj) jsonMessage.getMessageBody();
            ArrayList<ArrayList<ArrayList<Field>>> map = gameStartedBody.getMap();

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get map's detail information
     * @param nameField
     * @return
     * @author Aigerim
     */
    public ArrayList<ArrayList<ArrayList<Field>>> getFieldTypeString (String nameField) {
        ArrayList<ArrayList<ArrayList<Field>>> mapForYou = map();
        int i = -1;
        for (ArrayList<ArrayList<Field>> s : mapForYou) {
            int j = -1;
            ArrayList<ArrayList<Field>> a = s;
            i++;

            for (ArrayList<Field> l : a) {
                ArrayList<Field> r = l;
                j++;

                for (Field q : r) {
                    String type = q.getType();
                    if (type.equals("StartPoint")) {

                    }
                }

            }
        }

        return mapForYou;

    }

    /**
     * save current game Phase
     * @param newPhase
     */
    public void setPhase(int newPhase) {
        phase = newPhase;
    }

    public int getPhase() {
        return phase;
    }

    /**
     * get map's list as ListProperty
     * @return
     */
    public ListProperty<String> getMapsList() {
        ListProperty<String> mapsList = new SimpleListProperty<>(maps);
        return mapsList;
    }

    public void setCurrentPlayer(int playerID){
        currentPlayerID.setValue(playerID);
    }

    public void setCurrentPlayerIDInteger(int playerID){
        currentPlayerIDInteger = playerID;
    }

    public int getCurrentPlayerIDInteger() {
        return currentPlayerIDInteger;
    }

    public IntegerProperty getCurrentPlayerID(){
        return currentPlayerID;
    }

    public void setNextCardButtonVisible(boolean nextCardButtonVisible){
        this.nextCardButtonVisible.set(nextCardButtonVisible);
    }

    public BooleanProperty getNextCardButtonVisible(){
        return nextCardButtonVisible;
    }

    public void setReadyButtonVisible(boolean readyButtonVisible){
        this.readyButtonVisible.set(readyButtonVisible);
        this.notReadyButtonVisible.set(readyButtonVisible);
        this.readyTextVisible.set(readyButtonVisible);
    }

    public BooleanProperty getReadyButtonVisible(){
        return readyButtonVisible;
    }

    public BooleanProperty getNotReadyButtonVisible(){
        return notReadyButtonVisible;
    }

    public BooleanProperty getReadyTextVisible(){
        return readyTextVisible;
    }

    public void setNewPosition(int x, int y, int clientID){
        this.x = x;
        this.y = y;
        this.position.setValue(getRobotColor()+"_" + x + "_" + y);
    }

    public StringProperty getPosition (){
        return position;
    }

    /**
     * get Robot color according to the FigureID
     */
    public String getRobotColor (){
        switch(robotID){
            case 1:
                return "blue";
            case 2:
                return "red";
            case 3:
                return "purple";
            case 4:
                return "green";
            case 5:
                return "orange";
            case 6:
                return "yellow";
            default:
                return "false";
        }
    }

    public void setCurrentPositions(ArrayList<String> currentPositions){
        this.currentPositions = currentPositions ;
        String pos = currentPositions.toString();
        String pos2 = pos.replace("[","");
        String pos3 = pos2.replace("]","");
        sProp.setValue(pos3);

    }

    public ArrayList<String> getCurrentPositions(){
        return currentPositions;
    }


    public StringProperty currentPositionsAsString(){
        return sProp;
    }


    public void setRobotID(int id){
        this.robotID = id;
    }

    public void setNewCard(String cardChosen, int register){
        this.register = register;
        setCardName(cardChosen);
        switch(register){
            case 0:
                this.c0.setValue(getCardName()+"_"+"1");
                break;
            case 1:
                this.c1.setValue(getCardName()+"_"+"2");
                break;
            case 2:
                this.c2.setValue(getCardName()+"_"+"3");
                break;
            case 3:
                this.c3.setValue(getCardName()+"_"+"4");
                break;
            case 4:
                this.c4.setValue(getCardName()+"_"+"5");
                break;
        }
    }

    public StringProperty getCard(){
        return card;
    }

    public String getCardName (){
        switch(cardName){
            case "Move1":
                return "move1";
            case "Move2":
                return "move2";
            case "Move3":
                return "move3";
            case "TurnLeft":
                return "leftTurn";
            case "TurnRight":
                return "rightTurn";
            case "Again":
                return "again";
            case "PowerUp":
                return "powerUp";
            case "UTurn":
                return "uTurn";
            case "BackUp":
                return "moveBack";
            case "Spam":
                return "spam";
            case "TrojanHorse":
                return "trojanHorse";
            case "Worm":
                return "worm";
            case "Virus":
                return "virus";
            default:
                return "false";
        }
    }

    public void setNewUpgradeCard(String newUpgradeCard){
        setUpgradeCardName(newUpgradeCard);
        this.upgradeCard.setValue(getUpgradeCardName());
    }
    public String getUpgradeCardName(){
        switch(upgradeCardName){
            case "ADMIN PRIVILEGE":
                return "adminPrivilege";
            case "SPAM BLOCKER":
                return "spamBlocker";
            case "REAR LASER":
                return "rearLaser";
            case "MEMORY SWAP":
                return "memorySwap";
            default:
                return "false";
        }
    }

    /**
     * remove bought cards from choice box
     * @param boughtUpgradeCard
     * @author Aigerim
     */
    public void removeUpgradeCard(String boughtUpgradeCard) {
        if (boughtUpgradeCard.contains("SPAM BLOCKER")) {
            Platform.runLater(() -> myUpgradeCardList.remove(boughtUpgradeCard));
        } else if (boughtUpgradeCard.contains("MEMORY SWAP")) {
            Platform.runLater(() -> myUpgradeCardList.remove(boughtUpgradeCard));
        } else if (boughtUpgradeCard.contains("REAR LASER")) {

            Platform.runLater(() -> myUpgradeCardList.remove(boughtUpgradeCard));
        }
    }

    public void setNewUpgradeCardInGap1(String newUpgradeCardInGap1){
        setUpgradeCardInGap1Name(newUpgradeCardInGap1);
        this.upgradeCardInGap1.setValue(getUpgradeCardInGap1Name());
    }

    public String getUpgradeCardInGap1Name() {

        switch (upgradeCardInGap1Name) {
            case "SPAM BLOCKER":
                return "spamBlocker1";
            case "MEMORY SWAP":
                return "memorySwap1";
            default:
                return "false";
        }
    }

    public void setNewUpgradeCardInGap2(String newUpgradeCardInGap2){
        setUpgradeCardInGap2Name(newUpgradeCardInGap2);
        this.upgradeCardInGap2.setValue(getUpgradeCardInGap2Name());
    }

    public String getUpgradeCardInGap2Name() {

        switch (upgradeCardInGap2Name) {
            case "SPAM BLOCKER":
                return "spamBlocker2";
            case "MEMORY SWAP":
                return "memorySwap2";
            default:
                return "false";
        }
    }

    public void setNewUpgradeCardInGap3(String newUpgradeCardInGap3){
        setUpgradeCardInGap3Name(newUpgradeCardInGap3);
        this.upgradeCardInGap3.setValue(getUpgradeCardInGap3Name());
    }

    public String getUpgradeCardInGap3Name() {

        switch (upgradeCardInGap3Name) {
            case "SPAM BLOCKER":
                return "spamBlocker3";
            case "MEMORY SWAP":
                return "memorySwap3";
            default:
                return "false";
        }
    }

    public void setNewUpgradeCardInGap4(String newUpgradeCardInGap4){
        setUpgradeCardInGap4Name(newUpgradeCardInGap4);
        this.upgradeCardInGap4.setValue(getUpgradeCardInGap4Name());
    }

    public String getUpgradeCardInGap4Name() {

        switch (upgradeCardInGap4Name) {
            case "REAR LASER":
                return "rearLaser1";
            case "ADMIN PRIVILEGE":
                return "adminPrivilege1";
            default:
                return "false";
        }
    }

    public void setNewUpgradeCardInGap5(String newUpgradeCardInGap5){
        setUpgradeCardInGap5Name(newUpgradeCardInGap5);
        this.upgradeCardInGap5.setValue(getUpgradeCardInGap5Name());
    }

    public String getUpgradeCardInGap5Name() {

        switch (upgradeCardInGap5Name) {
            case "REAR LASER":
                return "rearLaser2";
            case "ADMIN PRIVILEGE":
                return "adminPrivilege2";
            default:
                return "false";
        }
    }

    public void setNewUpgradeCardInGap6(String newUpgradeCardInGap6){
        setUpgradeCardInGap6Name(newUpgradeCardInGap6);
        this.upgradeCardInGap6.setValue(getUpgradeCardInGap6Name());
    }

    public String getUpgradeCardInGap6Name() {

        switch (upgradeCardInGap6Name) {
            case "REAR LASER":
                return "rearLaser3";
            case "ADMIN PRIVILEGE":
                return "adminPrivilege3";
            default:
                return "false";
        }
    }

    public StringProperty currentCardsInRegister0asString(){
        return c0;
    }

    public StringProperty currentCardsInRegister1asString(){
        return c1;
    }

    public StringProperty currentCardsInRegister2asString(){
        return c2;
    }

    public StringProperty currentCardsInRegister3asString(){
        return c3;
    }

    public StringProperty currentCardsInRegister4asString(){
        return c4;
    }

    public void setCardName(String name){
        this.cardName = name;
    }

    public StringProperty upgradeCardAsString(){
        return upgradeCard;
    }

    public StringProperty upgradeCardInGap1AsString(){
        return upgradeCardInGap1;
    }

    public StringProperty upgradeCardInGap2AsString(){
        return upgradeCardInGap2;
    }

    public StringProperty upgradeCardInGap3AsString() {
        return upgradeCardInGap3;
    }

    public StringProperty upgradeCardInGap4AsString(){
        return upgradeCardInGap4;
    }

    public StringProperty upgradeCardInGap5AsString(){
        return upgradeCardInGap5;
    }

    public StringProperty upgradeCardInGap6AsString() {
        return upgradeCardInGap6;
    }

    public void setUpgradeCardName(String name){
        this.upgradeCardName = name;
    }

    public void setUpgradeCardInGap1Name(String name){
        this.upgradeCardInGap1Name = name;
    }
    public void setUpgradeCardInGap2Name(String name){
        this.upgradeCardInGap2Name = name;
    }
    public void setUpgradeCardInGap3Name(String name){
        this.upgradeCardInGap3Name = name;
    }
    public void setUpgradeCardInGap4Name(String name){
        this.upgradeCardInGap4Name = name;
    }
    public void setUpgradeCardInGap5Name(String name){
        this.upgradeCardInGap5Name = name;
    }
    public void setUpgradeCardInGap6Name(String name){
        this.upgradeCardInGap6Name = name;
    }

    public BooleanProperty invisibleStartPoint1(){
        return startPoint1;
    }

    public void setInvisibleStartPoint1() {
        startPoint1.setValue(false);
    }

    public BooleanProperty invisibleStartPoint2(){
        return startPoint2;
    }

    public void setInvisibleStartPoint2() {
        startPoint2.setValue(false);
    }

    public BooleanProperty invisibleStartPoint3(){
        return startPoint3;
    }

    public void setInvisibleStartPoint3() {
        startPoint3.setValue(false);
    }

    public BooleanProperty invisibleStartPoint4(){
        return startPoint4;
    }

    public void setInvisibleStartPoint4() {
        startPoint4.setValue(false);
    }

    public BooleanProperty invisibleStartPoint5(){
        return startPoint5;
    }

    public void setInvisibleStartPoint5() {
        startPoint5.setValue(false);
    }

    public BooleanProperty invisibleStartPoint6(){
        return startPoint6;
    }

    public void setInvisibleStartPoint6() {
        startPoint6.setValue(false);
    }

    public StringProperty getPhaseInfoProperty() {
        return phaseInfo;
    }

    public void setPhaseInfo(String phaseInfo) {
        this.phaseInfo.set(phaseInfo);
    }

    public StringProperty getToDoInfoProperty(){
        return toDoInfo;
    }

    public void setToDoInfo(String toDoInfo){
        this.toDoInfo.set(toDoInfo);
    }

    public StringProperty getUserNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public IntegerProperty getEnergyCubesProperty() {
        return energyCubes;
    }

    public void setEnergyCubes(int energyCubes) {
        this.energyCubes.set(energyCubes);
    }

    public IntegerProperty getCheckPointsProperty() {
        return checkPoints;
    }

    public void setCheckPoints(int checkPoints) {
        this.checkPoints.set(checkPoints);
    }

    public StringProperty getRenameRobot() {
        return renameRobot;
    }

    public void setRenameRobot(String renameRobot) {
        this.renameRobot.set(renameRobot);
    }

    public ArrayList<String> getTemporaryUpgradeCardList(){
        return temporaryUpgradeCardList;
    }

    public void addInTemporaryUpgradeCardList(String card){
        temporaryUpgradeCardList.add(card);
        myUpgradeCardList.add(card);
    }
    
    public void removeFromTemporaryUpgradeCardList(String card){
        temporaryUpgradeCardList.remove(card);
        myUpgradeCardList.remove(card);
    }

    public void setTemporaryUpgradeCardList(ArrayList<String> temporaryUpgradeCardList){
        this.temporaryUpgradeCardList = temporaryUpgradeCardList;
    }

    public ArrayList<String> getPermanentUpgradeCardList(){
        return permanentUpgradeCardList;
    }

    public void addInPermanentUpgradeCardList(String card){
        permanentUpgradeCardList.add(card);
        myUpgradeCardList.add(card);
    }

    public void setPermanentUpgradeCardList(ArrayList<String> permanentUpgradeCardList){
        this.permanentUpgradeCardList = permanentUpgradeCardList;
    }

    public int getRegister() {
        return currentRegister;
    }

    public void increaseRegister() {
        if(currentRegister == 4){
            currentRegister = 0;
        }
        else {
            this.currentRegister++;
        }
    }

    public StringProperty getCheckPointMoved (){
        return checkPointMoved;
    }

    public void setCheckPointMoved (String move){
        checkPointMoved.set(move);
    }

    public BooleanProperty getStartButtonProperty() {
        return startButton;
    }

    public void setStartButton() {
        startButton.setValue(true);
    }

}
