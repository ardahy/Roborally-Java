package model;

import server.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Contains all information the server needs for each Player
 * @author Ilinur, Aika, Arda, Rea, Benedikt
 */
public class Player {

    private String name;
    private int playerID;
    private int figure;
    private ArrayList<String> cardsInHand = new ArrayList<>();
    private ArrayList<String> programmingDeck = new ArrayList<>();
    private ArrayList<String> discardPile = new ArrayList<>();
    private String[] register = new String[5];
    private boolean isReady = false;
    private int robotX;
    private int robotY;
    private int energyCubes;
    private int checkpointTokens;
    private String robotDirection;
    private boolean playerHasSetStartingPoint = false;
    private String lastRegisterCard = "";
    private ArrayList<String> upgradeCards = new ArrayList<>();
    private Server server;
    private int startingPointX;
    private int StartingPointY;
    int currentRegisterNumber;
    private boolean boughtRearLaser;


    public Player(String name, int playerID, int figure){
        this.name = name;
        this.playerID = playerID;
        this.figure = figure;
        this.boughtRearLaser = false;
    }

    public String getRobotDirection() {
        return robotDirection;
    }

    public void setRobotDirection(String direction) {
        robotDirection = direction;
    }

    public ArrayList<String> getDiscardPile() {
        return discardPile;
    }

    public void addLastRegisterCard(String card){
        lastRegisterCard = card;
    }

    public String getName(){
        return this.name;
    }

    public int getPlayerID(){
        return this.playerID;
    }

    public int getFigure(){
        return this.figure;
    }

    public void addCardInHand(String cardInHand) {
        cardsInHand.add(cardInHand);
    }

    public void removeCardFromRegister(String cardRemoved, int register) {
        this.register[register] = null;
        discardPile.add(cardRemoved);
    }

    public void removeCardFromProgrammingDeck(String removeCard){
        programmingDeck.remove(0);

    }

    /**
     * Adds all cards from discard pile to programming deck and shuffles
     * @author Benedikt
     */
    public void shuffleCardsWhenDeckEmpty() {
        for(String card : discardPile) {
            if(card != null) {
                programmingDeck.add(card);
            }
        }
        discardPile.clear();
        Collections.shuffle(programmingDeck);
    }

    public void addProgrammingDeck(int index, String card){
        programmingDeck.add(index, card);
    }

    public void addRegister(int space, String card) {
        if(card.equals("Null")){
            register[space] = null;
        }
        else {
            register[space] = card;
        }
    }

    public void removeFromHand(String card){
        cardsInHand.remove(card);
    }

    public boolean isCardInHand(String card){
        if(card.equals("Null")){
            return true;
        }

        for(String s: cardsInHand){
            if(s.equals(card)) {
                return true;
            }
        }
        return false;
    }

    public void drawThreeCards(){
        if(programmingDeck.size() < 3){
            shuffleCardsWhenDeckEmpty();
        }
        String card = programmingDeck.get(0);
        programmingDeck.remove(0);
        cardsInHand.add(card);
        card = programmingDeck.get(0);
        programmingDeck.remove(0);
        cardsInHand.add(card);
        card = programmingDeck.get(0);
        programmingDeck.remove(0);
        cardsInHand.add(card);
    }

    public ArrayList<String> getCardsInHand(){
        return cardsInHand;
    }

    public String[] getRegister() {
        return register;
    }

    // Set status of player
    public void setIsReady(boolean isPlayerReady) {
        isReady = isPlayerReady;
    }

    public boolean getIsReady(){
        return isReady;
    }

    public boolean isRegisterFull() {
        for(int i = 0; i < register.length; i++) {
            if (register[i] == null) {
                return false;
            }
        }
        return true;
    }

    public void addInDiscardPile(String card){
        discardPile.add(card);
    }


    public void discardAll(){
        ArrayList<String> toRemove = new ArrayList<>();
        for (String card : cardsInHand) {
            toRemove.add(card);
            discardPile.add(card);
        }
        cardsInHand.removeAll(toRemove);
    }

    /**
     * Draws a card for Cards You Got Now
     * @param register the empty register the card is for
     * @return a card fopr empty register
     * @author Benedikt
     */
    public String drawCardForEmptyRegister(int register) {
        if(programmingDeck.isEmpty()) {
            shuffleCardsWhenDeckEmpty();
        }
        Random rand = new Random();
        int indexOfRandomCard = rand.nextInt(programmingDeck.size());
        String card = programmingDeck.get(indexOfRandomCard);
        if(card.equals("Again") && register == 0){
            card = drawCardForEmptyRegister(register);
        }
        else {
            programmingDeck.remove(card);
        }
        return card;
    }

    /**
     * Fills empty Registers for Cards You Got now
     * @return
     * @author Benedikt
     */
    public String[] fillEmptyRegisters() {
        int countEmptyRegisters = 0;
        for (String cardInRegister : register) {
            if (cardInRegister == null) {
                countEmptyRegisters++;
            }
        }

        String[] cardsYouGotNow = new String[countEmptyRegisters];
        int i = 0;
        for (String card : register) {
            if (card == null) {
                int indexToFill = Arrays.asList(register).indexOf(null);
                card = drawCardForEmptyRegister(indexToFill);
                cardsYouGotNow[i] = card;
                register[indexToFill] = card;
                // register[i] = card;
                i++;
            }
        }
        return cardsYouGotNow;
    }

    public void emptyRegisters(){
        discardPile.addAll(Arrays.asList(register));
        Arrays.fill(register, null);
    }

    public void emptyHand(){
        cardsInHand.clear();
    }

    public boolean isRegisterNull(int reg){
        return register[reg] == null;
    }

    public void deleteRegister(int registerDelete) {
        if(register[registerDelete] != null) {
            cardsInHand.add(register[registerDelete]);
        }
        register[registerDelete] = null;
    }

    public ArrayList<String> getProgrammingDeck(){
        return programmingDeck;
    }

    public void setProgrammingDeck(ArrayList<String> programmingDeck) {
        this.programmingDeck = programmingDeck;
    }

    public void setRobotX(int x){
        this.robotX = x;
    }

    public void setRobotY(int y){
        this.robotY = y;
    }

    public int getRobotX() {
        return robotX;
    }

    public int getRobotY() {
        return robotY;
    }

    /**
     * calculates distance from robot to priority antenna via Pythagoras
     * @param antennaX
     * @param antennaY
     * @return distance
     */
    public double calculateDistance(int antennaX, int antennaY){
        double distance = Math.sqrt(Math.abs(Math.pow(robotX - antennaX, 2)) + Math.abs(Math.pow(robotY - antennaY, 2)));
        return distance;
    }

    public String getCardInRegister(int register) {
        return this.register[register];
    }

    public void playerSetStartingPoint() {
        playerHasSetStartingPoint = true;
    }

    public boolean getPlayerHasSetStartingPoint(){
        return playerHasSetStartingPoint;
    }

    public String getLastRegisterCard() {
        return lastRegisterCard;
    }

    public void addEnergyCubes(){
        energyCubes++;
    }

    public int getEnergyCubes(){
        return energyCubes;
    }

    public void firstEnergyCubes() {
        int i;
        for (i = 0; i < 5; i++) {
            addEnergyCubes();
        }
    }

    public void addCheckpointTokens(){
        checkpointTokens++;
    }

    public int getCheckpointTokens(){
        return checkpointTokens;
    }

    public void addUpgradeCard(String card){
        upgradeCards.add(card);
    }

    /**
     * If player can afford upgrade card, the player pays for upgrade card
     * @param energyCubes the upgrade card's price
     * @return boolean whether the player could afford the upgrade card
     * @author Benedikt
     */
    public boolean canAfford(int energyCubes){
        if((this.energyCubes - energyCubes) >= 0){
            this.energyCubes = this.energyCubes - energyCubes;
            return true;
        }
        else{
            return false;
        }
    }

    /**
     *
     * @return player chosen startingPointX
     */
    public int getStartingPointX() {
        return startingPointX;
    }

    /**
     *
     * @param startingPointX player chosen startingPointX
     */

    public void setStartingPointX(int startingPointX) {
        this.startingPointX = startingPointX;
    }

    /**
     *
     * @return player chosen startingPointY
     */
    public int getStartingPointY() {
        return StartingPointY;
    }
    /**
     *
     * @param startingPointY player chosen startingPointY
     */
    public void setStartingPointY(int startingPointY) {
        StartingPointY = startingPointY;
    }

    /**
     *
     * @return true, if current register Number reaches 5
     */
    public boolean isCurrentRegisterFive(){
        if(currentRegisterNumber == 5){
            return true;
        }
        else return false;
    }

    public void setCurrentRegisterNumber0(int num){
        this.currentRegisterNumber = num;
    }

    /**
     * removes all spam cards in hand and adds them in discard pile
     * @author Rea
     */
    public void removeSpamCards(){
        for(int i = 0; i < cardsInHand.size(); i++){
            String currentCard = cardsInHand.get(i);
            if(currentCard.equals("Spam")){
                cardsInHand.remove(currentCard);
            }
        }
    }

    /**
     * takes first three cards of programming deck and puts them in hand
     * @author Rea
     */
    public void replaceWithDeck(){
        if(programmingDeck.size() >= 3) {
            String first = programmingDeck.get(0);
            String second = programmingDeck.get(1);
            String third = programmingDeck.get(2);

            cardsInHand.add(first);
            cardsInHand.add(second);
            cardsInHand.add(third);

            programmingDeck.remove(first);
            programmingDeck.remove(second);
            programmingDeck.remove(third);
        }

    }

    public boolean getBoughtRearLaser() {
        return boughtRearLaser;
    }

    public void setBoughtRearLaser(boolean boughtRearLaser) {
        this.boughtRearLaser = boughtRearLaser;
    }

}


