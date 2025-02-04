package server;

import java.util.logging.*;
import model.Player;
import model.cards.damageCards.*;
import model.cards.programmingCards.*;
import model.field.*;
import utilities.Converter;
import utilities.objects.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class creates Server
 * creates an ArrayList of all Clients currently connected to Server
 * @author Benedikt, Ilinur, Arda, Rea, Aigerim
 */
public class Server {

    private boolean acceptConnection = true;
    boolean append = true;
    private FileHandler handler;
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private ArrayList<ClientHandler> connectedClients = new ArrayList<>();
    private HashMap<Integer, Socket> clientWriter = new HashMap<>();
    private int playerID = 0;
    private ArrayList<Integer> playerIDs = new ArrayList<Integer>();
    private ArrayList<String> protocolList = new ArrayList<>();
    public static final String ANSI_GREEN = "\u001B[32m";
    private ArrayList<String> spamPile = new ArrayList<>();
    private ArrayList<String> trojanHorsePile = new ArrayList<>();
    private ArrayList<String> wormPile = new ArrayList<>();
    private ArrayList<String> virusPile = new ArrayList<>();
    private ArrayList<Player> currentPlayers = new ArrayList<>(); // All clients that have chosen robot and name
    private ArrayList<Player> playingPlayers = new ArrayList<>(); // All clients that are ready when the game starts
    private ArrayList<Player> playersNoStartingPoint = new ArrayList<>(); // All clients that have not set their starting points
    private ArrayList<String> cardsInCorrectOrder = new ArrayList<>(); // The card that is being played next
    int phase = 0;
    private boolean gameStarted = false;
    private boolean firstRound = true;
    private boolean mapIsSelected = false;
    private String selectedMap = "empty";
    private ArrayList<ArrayList<ArrayList<Field>>> map = new ArrayList<ArrayList<ArrayList<Field>>>();
    // TODO energycubebank
    private int energyCubeBank = 48;
    private ArrayList<String> upgradeShop = new ArrayList<>();
    private ArrayList<String> upgradeCards = new ArrayList<>();
    private int[] priorityArray;
    private int currentPositionInPriorityArray = 1;
    private boolean isRegisterOver;
    private int countPlayedCards;
    private ArrayList<Integer> playerRebootedID = new ArrayList<>();
    int currentRegisterNumber;
    int currentRegister;
    private HashMap<Integer, Integer> adminPrivileges = new HashMap<Integer,
            Integer>();
    private HashMap<String, EnergySpace> energySpaceMap ;
    private HashMap<String, CheckPoint> movedTwisterCheckpoints = new HashMap<>();
    private ArrayList<String> energyKeys = new ArrayList<>();
    private HashMap<String, StartPoint> startPointMap = new HashMap<>() ;


    private boolean playNextCardBlocked;



    public Server() {
        try {
            handler = new FileHandler("./logs/LoggingServer.log", append);
            logger.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create Server
     * as long as acceptConnection is true clients are added to connectedClients
     * Player ID is allocated to client
     * for each Client a new ClientHandler Thread is created and started
     *
     * @param port Port number of Server
     * @author Aigerim
     */
    public void createServer(int port) throws IOException {

        ServerSocket serverSocket = new ServerSocket(port);
        logger.severe(ANSI_GREEN + "Starting server..." + ANSI_GREEN);

        try {
            while (acceptConnection) {
                logger.info(ANSI_GREEN + "Waiting for new client connection..." + ANSI_GREEN);
                Socket socket = serverSocket.accept();
                logger.info(ANSI_GREEN + "Client parameters " + socket.getInetAddress().getHostAddress() + ANSI_GREEN);
                ClientHandler clientHandler = new ClientHandler(socket, this);
                Thread clientHandlerThread = new Thread(clientHandler);
                connectedClients.add(clientHandler);

                clientHandlerThread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            {
                serverSocket.close();
                logger.info(ANSI_GREEN + "Server shut down..." + ANSI_GREEN);

            }
        }

    }


    /**
     * Returns the Player's ID number
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Gives a new Client an ID number (increased by one every time a new client connects)
     *
     * @param counterID ID number of the connecting client
     */
    public void setCounterPlayerID(int counterID) {
        this.playerID = counterID;
    }

    /**
     * Returns the ArrayList of all Connected
     * @author Aigerim
     */
    public ArrayList<ClientHandler> getConnectedClients() {
        return connectedClients;
    }

    /**
     * Returns the client ID
     * @param socket
     * @author Aigerim
     */
    public Integer getClientID(Socket socket) {
        for (Integer key : getKeys(clientWriter, socket)) {
            return key;
        }
        return null;
    }

    /**
     * Sends a private Message from to server to a specific client using clientID
     * @param message The message that is sent
     * @param clientToSend ID of the client to which the message is sent
     * @author Aigerim
     */
    public synchronized void privateMessage(JSONMessage message, int clientToSend) {
        Socket idSocket = clientWriter.get(clientToSend);
        try {
            PrintWriter output = new PrintWriter(new OutputStreamWriter(idSocket.getOutputStream(), "UTF-8"));
            output.println(Converter.serializeJSON(message));
            output.flush();
            logger.info("SEND_CHAT: Contend of ReceivedChat " + message + clientToSend +" " + Converter.serializeJSON(message));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the message to all clients
     * @param message The message that is sent
     * @author Aigerim
     */
    public void broadcast(JSONMessage message) {
        for(Player p: currentPlayers) {
            int id = p.getPlayerID();
            privateMessage(message, id);
        }
    }

    /**
     * get clients IDs
     * @param clientWriter
     * @param value
     * @return
     * @author Aigerim
     */
    private static Set<Integer> getKeys(HashMap<Integer, Socket> clientWriter, Socket value) {
        Set<Integer> result = new HashSet<>();
        if (clientWriter.containsValue(value)) {
            for (Map.Entry<Integer, Socket> entry : clientWriter.entrySet()) {
                if (Objects.equals(entry.getValue(), value)) {
                    result.add(entry.getKey());
                }
            }
        }
        return result;
    }

    /**
     * Checks whether an ID exists and returns the associated boolean value
     * @param IDnumber the ID that is being checked
     * @return true if clientID exists
     */
    public boolean isClientIDValid(int IDnumber) {
        if (playerIDs.contains(IDnumber)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds all valid protocol versions to List
     * @return List of all valid Protocol versions
     */
    public ArrayList<String> getProtocolList() {
        protocolList.add("Version 0.1");
        protocolList.add("Version 0.2");
        protocolList.add("Version 1.0");
        protocolList.add("Version 2.0");
        protocolList.add("Version 2.1");
        return protocolList;
    }

    /**
     * @return Returns Array list of all player IDs
     */
    public ArrayList<Integer> getPlayerIDs() {
        return playerIDs;
    }

    /**
     * Removes a player from player IDs
     * @param ID the ID belonging to the removed player
     */
    public void removePlayerID(int ID) {
        playerIDs.remove(ID);
    }

    /**
     * Removes clientHandler from connected clients
     * @param clientHandler the clientHandler which is removed
     * @author Aigerim
     */
    public void removeClientHandler(ClientHandler clientHandler) {
        connectedClients.remove(clientHandler);
    }

    /**
     * adds a player ID to the List with all player IDs
     * @param playerID the player ID which is added
     */
    public void addToPlayerIDs(int playerID) {
        playerIDs.add(playerID);
    }

    /**
     * add clients socket to the list
     * @param ID
     * @param socket
     * @author Aigerim
     */
    public void addToPlayerSocket(int ID, Socket socket) {
        clientWriter.put(ID, socket);
    }

    /**
     * Puts all programming cards in Array
     * @authors Aigerim, Arda, Benedikt
     */
    public String[] buildProgrammingDeck() {

        String[] programmingCardDeck = new String[20];
        for (int i = 0; i < 5; i++) {
            programmingCardDeck[i] = "Move1";
        }
        for (int i = 5; i < 8; i++) {
            programmingCardDeck[i] = "Move2";
        }
        for (int i = 8; i < 11; i++) {
            programmingCardDeck[i] = "TurnRight";
        }
        for (int i = 11; i < 14; i++) {
            programmingCardDeck[i] = "TurnLeft";
        }
        for (int i = 14; i < 16; i++) {
            programmingCardDeck[i] = "Again";
        }
        programmingCardDeck[16] = "UTurn";
        programmingCardDeck[17] = "BackUp";
        programmingCardDeck[18] = "PowerUp";
        programmingCardDeck[19] = "Move3";

        return programmingCardDeck;
    }

    public void spamDeck(){
        for(int i = 0; i < 32; i++){
            spamPile.add("Spam");
        }
    }


    public void virusDeck(){
        for(int i = 0; i < 18; i++){
            virusPile.add("Virus");
        }
    }

    public void trojanHorseDeck(){
        for(int i = 0; i < 12; i++){
            trojanHorsePile.add("TrojanHorse");
        }
    }

    public void wormDeck(){
        for(int i = 0; i < 6; i++){
            wormPile.add("Worm");
        }
    }

    public void addInSpamPile(String card){ spamPile.add(card);}

    public void addInTrojanHorsePile(String card){ trojanHorsePile.add(card);}

    public void addInWormPile(String card){ wormPile.add(card);}

    public void addInVirusPile(String card){ virusPile.add(card);}

    public void removeFromSpamPile(String card){
        spamPile.remove(card);
    }

    public void removeFromTrojanHorsePile(String card){
        trojanHorsePile.remove(card);
    }

    public void removeFromWormPile(String card){
        wormPile.remove(card);
    }

    public void removeFromVirusPile(String card){
        virusPile.remove(card);
    }

    /**
     * Adds programming Deck to a player and chooses 9 random cards for cards in hand
     * @param ID the client ID of the player, which receives the cards
     * @return 9 random cards as String Array
     * @author Benedikt
     */
    public String[] handOut9Cards(int ID) {

        Player player = getPlayer(ID);
        if(firstRound) {
            String[] programmingCardDeck = buildProgrammingDeck();
            // Put Programming Deck in Player class
            ArrayList<String> programmingCardDeckList = new ArrayList<>();
            Collections.addAll(programmingCardDeckList, programmingCardDeck);
            Collections.shuffle(programmingCardDeckList);
            player.setProgrammingDeck(programmingCardDeckList);
            logger.info(ANSI_GREEN + "Programming Deck in First Round of Player " + player.getPlayerID() + " : " + player.getProgrammingDeck() + ANSI_GREEN);
        }
        ArrayList<String> playersProgrammingDeck = player.getProgrammingDeck();
        String[] cards9 = new String[9];
        for (int i = 0; i < 9; i++) {
            playersProgrammingDeck = player.getProgrammingDeck();
            if(playersProgrammingDeck.size() == 0){
                player.shuffleCardsWhenDeckEmpty();
                playersProgrammingDeck = player.getProgrammingDeck();
                logger.info(ANSI_GREEN + "Programming Deck after shuffeling of Player " + player.getPlayerID() + " : " + player.getProgrammingDeck() + ANSI_GREEN);
            }
                cards9[i] = playersProgrammingDeck.get(0);
                player.removeCardFromProgrammingDeck(playersProgrammingDeck.get(0));
        }
        // playersProgrammingDeck.removeAll(cardsToRemove);
        player.setProgrammingDeck(playersProgrammingDeck);

        return cards9;
    }

    /**
     * Checks if the Game has already started
     * @return true when gameStarted
     */
    public boolean getGameStarted() {
        return gameStarted;
    }


    /**
     * adds a Player that has chosen name and figure to currentPlayers
     * @param player the player that is being added
     */
    public void addPlayer(Player player){
        currentPlayers.add(player);
    }

    /**
     * Returns the according player for a given ID number
     * @param ID The players ID
     * @return the Player
     */
    public Player getPlayer(int ID) {
        for(Player p : currentPlayers) {
            if(p.getPlayerID() == ID) {
                return p;
            }
        }
        return null;
    }

    /**
     * @return The players that have chosen robot and name
     */
    public ArrayList<Player> getCurrentPlayers() {
        return currentPlayers;
    }

    /**
     * @return The players that are ready when the game starts. These are the ones that actually play the game
     */
    public ArrayList<Player> getPlayingPlayers() {
        return playingPlayers;
    }


    /**
     * Calculates for all players the distance to antenna and then builds an array of players sorted by distance to antenna
     * @return the List of players sorted by priority
     * @author Arda, Benedikt, Aigerim
     */
    public int[] getPriorityArrayClientIDs() {
        int[] priorityList = new int[playingPlayers.size()];

        final Map<Integer, Double> map = new HashMap<>();
        //double [] distanceValues = new double [playingPlayers.size()];
        for(int i=0;i< playingPlayers.size();i++) {
            if(selectedMap.equals("DizzyHighway") || selectedMap.equals("ExtraCrispy") || selectedMap.equals("LostBearings") || selectedMap.equals("Twister")) {
                //distanceValues[i] = playingPlayers.get(i).calculateDistance(0, 4);
                map.put(playingPlayers.get(i).getPlayerID(), playingPlayers.get(i).calculateDistance(0, 4));
            }
            else {
                //distanceValues[i] = playingPlayers.get(i).calculateDistance(12, 5);
                map.put(playingPlayers.get(i).getPlayerID(), playingPlayers.get(i).calculateDistance(12, 5));
            }
        }

        Map<Integer, Double> result = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        List<Integer> ids = new LinkedList<>();
        for (Integer iu : result.keySet()) {
            ids.add(iu);
        }

        for(int i= 0; i <ids.size(); i++) {
            priorityList[i] = ids.get(i);
        }
        return priorityList;
    }

    /**
     * signalize that a map has been set
     */
    public void setMapIsSelected() {
        mapIsSelected = true;
    }

    /**
     * @return if a map has been selected
     */
    public boolean getMapIsSelected() {
        return mapIsSelected;
    }

    /**
     * Checks if all players have set their starting point
     * @return true if all players did so
     */
    public boolean allPlayersSetStartingPoint(){
        for(Player p : playingPlayers){
            if(!p.getPlayerHasSetStartingPoint()){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether all players are ready
     * @return true if all players are ready
     */
    public boolean allPlayersReady(){
        for(Player p : playingPlayers){
            if(!p.getIsReady()){
                return false;
            }
        }
        return true;
    }

    /**
     * Removed one energy Cube from Energy Cube bank
     */
    public void removeEnergyCubeBank(){
        energyCubeBank--;
    }


    /**
     * @return the map which has been selected
     */
    public String getSelectedMap() {
        return selectedMap;
    }

    /**
     * Sets the map
     * @param selectedMap the map that has been chosen
     */
    public void setSelectedMap(String selectedMap) {
        this.selectedMap = selectedMap;
    }


    /**
     * checks if player has fallen off the board, into a pit, or has been shot
     * @param player
     * @author Rea
     */
    public void checkReboot(Player player) {

        if (fallenOffBoard(player)) {
            String knockedOff = "You were knocked off the board!";
            JSONMessage jsonMessageKnockedOff = new JSONMessage("SendChat", new SendChatObj(knockedOff, player.getPlayerID()));
            privateMessage(jsonMessageKnockedOff, player.getPlayerID());
            reboot(player);
        }

        if (fallenOfPlayerBoard(player)) {
            String pit = "You were knocked off the board!";
            JSONMessage jsonMessagePit = new JSONMessage("SendChat", new SendChatObj(pit, player.getPlayerID()));
            privateMessage(jsonMessagePit, player.getPlayerID());
            reboot(player);
        }
    }

    /**
     * Reboots the player. Adds damage cards accordingly. Also checks if other robots need to be pushed because of the rebooted
     * robot.
     * @param player
     * @author Rea,Arda
     */
    public void reboot(Player player){
        String[] damageCards = {""};

        if (!spamPile.isEmpty() && spamPile.size() != 1) {

            Spam spam = new Spam();
            player.addInDiscardPile(spam.getCardName());
            player.addInDiscardPile(spam.getCardName());

            removeFromSpamPile(spam.getCardName());
            removeFromSpamPile(spam.getCardName());

            String[] spamCards = {"Spam", "Spam"};

            JSONMessage jsonMessageDamage = new JSONMessage("DrawDamage", new DrawDamageObj(player.getPlayerID(), spamCards));
            privateMessage(jsonMessageDamage, player.getPlayerID());
        }
        else {
            if (wormPile.isEmpty()) {
                if (virusPile.isEmpty()) {
                    if(trojanHorsePile.size() > 1) {
                        damageCards = new String[]{"TrojanHorse", "TrojanHorse"};
                    }
                    else if (trojanHorsePile.size() == 1){
                        damageCards = new String[]{"TrojanHorse"};
                    }
                } else if (trojanHorsePile.isEmpty()) {
                    if(virusPile.size() > 1) {
                        damageCards = new String[]{"Virus", "Virus"};
                    }
                    else if (virusPile.size() == 1){
                        damageCards = new String[]{"Virus"};
                    }
                } else {
                    if(trojanHorsePile.size() > 1 && virusPile.size() > 1){
                        damageCards = new String[]{"TrojanHorse", "TrojanHorse", "Virus", "Virus"};
                    }
                    else if(trojanHorsePile.size() > 1 && virusPile.size() == 1){
                        damageCards = new String[]{"TrojanHorse", "TrojanHorse", "Virus"};
                    }
                    else if(trojanHorsePile.size() == 1 && virusPile.size() > 1){
                        damageCards = new String[]{"TrojanHorse", "Virus", "Virus"};
                    }
                    else if(trojanHorsePile.size() == 1 && virusPile.size() == 1){
                        damageCards = new String[]{"TrojanHorse", "Virus"};
                    }
                }

            } else if (virusPile.isEmpty()) {
                if (trojanHorsePile.isEmpty()) {
                    if(wormPile.size() > 1) {
                        damageCards = new String[]{"Worm", "Worm"};
                    }
                    else if(wormPile.size() == 1){
                        damageCards = new String[]{"Worm"};
                    }

                } else {
                    if(trojanHorsePile.size() > 1 && wormPile.size() > 1) {
                        damageCards = new String[]{"TrojanHorse", "TrojanHorse", "Worm", "Worm"};
                    }
                    else if(trojanHorsePile.size() > 1 && wormPile.size() == 1){
                        damageCards = new String[]{"TrojanHorse", "TrojanHorse", "Worm"};
                    }
                    else if(trojanHorsePile.size() == 1 && wormPile.size() > 1){
                        damageCards = new String[]{"TrojanHorse", "Worm", "Worm"};
                    }
                    else if(trojanHorsePile.size() == 1 && wormPile.size() == 1){
                        damageCards = new String[]{"TrojanHorse", "Worm"};
                    }
                }
            } else if (trojanHorsePile.isEmpty()) {
                if(virusPile.size() > 1 && wormPile.size() > 1) {
                    damageCards = new String[]{"Virus", "Virus", "Worm", "Worm"};
                }
                else if(virusPile.size() > 1 && wormPile.size() == 1){
                    damageCards = new String[]{"Virus", "Virus", "Worm"};
                }
                else if(virusPile.size() == 1 && wormPile.size() > 1){
                    damageCards = new String[]{"Virus", "Worm", "Worm"};
                }
                else if(virusPile.size() == 1 && wormPile.size() == 1){
                    damageCards = new String[]{"Virus", "Worm"};
                }
            } else {
                if(virusPile.size() > 1 && wormPile.size() > 1 && trojanHorsePile.size() > 1) {
                    damageCards = new String[]{"Virus", "Virus", "Worm", "Worm", "TrojanHorse", "TrojanHorse"};
                }
                else if(virusPile.size() == 1 && wormPile.size() > 1 && trojanHorsePile.size() > 1){
                    damageCards = new String[]{"Virus", "Worm", "Worm", "TrojanHorse", "TrojanHorse"};
                }
                else if(virusPile.size() > 1 && wormPile.size() == 1 && trojanHorsePile.size() > 1){
                    damageCards = new String[]{"Virus", "Virus", "Worm", "TrojanHorse", "TrojanHorse"};
                }
                else if(virusPile.size() > 1 && wormPile.size() > 1 && trojanHorsePile.size() == 1){
                    damageCards = new String[]{"Virus", "Virus", "Worm", "Worm", "TrojanHorse"};
                }
                else if(virusPile.size() == 1 && wormPile.size() == 1 && trojanHorsePile.size() > 1){
                    damageCards = new String[]{"Virus", "Worm", "TrojanHorse", "TrojanHorse"};
                }
                else if(virusPile.size() == 1 && wormPile.size() > 1 && trojanHorsePile.size() == 1){
                    damageCards = new String[]{"Virus", "Worm", "Worm", "TrojanHorse"};
                }
                else if(virusPile.size() > 1 && wormPile.size() == 1 && trojanHorsePile.size() == 1){
                    damageCards = new String[]{"Virus", "Virus", "Worm", "TrojanHorse"};
                }
            }
            JSONMessage jsonMessagePick = new JSONMessage("PickDamage", new PickDamageObj(2, damageCards));
            privateMessage(jsonMessagePick, player.getPlayerID());

        }
        player.discardAll();
        playerRebootedID.add(player.getPlayerID());
        // player.discardProgrammingCards();

        if(fallenOfPlayerBoard(player)){
            player.setRobotDirection("south");
            boolean startPointOccupied = false;
            for (Player p:playingPlayers){
                if(p.getRobotX() == player.getStartingPointX() && p.getRobotY() == player.getStartingPointY()){
                    startPointOccupied = true;
                }
            }

            if(!startPointOccupied) {
                player.setRobotX(player.getStartingPointX());
                player.setRobotY(player.getStartingPointY());
            }else{
                for (String key: getStartPointMap().keySet()){
                    int x = Integer.parseInt(key.split("_")[0]);
                    int y = Integer.parseInt(key.split("_")[1]);
                    boolean positionEmpty = true;
                    for (Player pl:playingPlayers){
                        if(pl.getRobotX() == x && pl.getRobotY() == y){
                            positionEmpty = false;
                        }
                    }
                    if (positionEmpty){
                        player.setRobotX(x);
                        player.setRobotY(y);
                        break;
                    }
                }
            }

        }
        else {
            placeOnReboot(player);
        }
        JSONMessage jsonMessageReboot = new JSONMessage("Reboot", new RebootObj(player.getPlayerID()));
        privateMessage(jsonMessageReboot, player.getPlayerID());
    }



    /**
     * Checks if all clients that have chosen name and figure are ready
     * @return boolean value if all currentPlayer's have status ready
     */
    public boolean allCurrentPlayersAreReady(){
        for(Player p : currentPlayers) {
            if(!p.getIsReady()){
                return false;
            }
        }
        return true;
    }

    /**
     * Adds all Upgrade Cards to List
     */
    public void buildUpgradeCards(){
        //permanent
        for(int i=0; i<10; i++) {
            upgradeCards.add("ADMIN PRIVILEGE");
        }
        for(int i=0; i<10; i++) {
            upgradeCards.add("REAR LASER");
        }
        // Temporary
        for(int i=0; i<10; i++) {
            upgradeCards.add("MEMORY SWAP");
        }
        for(int i=0; i<10; i++) {
            upgradeCards.add("SPAM BLOCKER");
        }
    }

    /**
     * Refills the Upgradeshop until there are as many cards as players
     * @author Benedikt
     */
    public void refillShop(){
        int amountOfNewUpgradeCards = playingPlayers.size() - upgradeShop.size();
        for(int i=0; i<amountOfNewUpgradeCards; i++){
            Random rand = new Random();
            // Eventuell muss hier plus 1
            int indexOfRandomUpgradeCard = rand.nextInt(upgradeCards.size());
            String card = upgradeCards.get(indexOfRandomUpgradeCard);
            upgradeShop.add(card);
            upgradeCards.remove(card);
        }
        String[] newUpgradeShop = new String[upgradeShop.size()];
        int i = 0;
        for(String upgradeCard : upgradeShop){
            newUpgradeShop[i] =  upgradeCard;
            i++;
        }
        JSONMessage jsonMessageRefillShop = new JSONMessage("RefillShop", new RefillShopObj(newUpgradeShop));
        broadcast(jsonMessageRefillShop);
    }

    /**
     * If last Round No One Bought Upgrade Cards, the shop is being exchanged
     * @author Benedikt
     */
    public void exchangeShop(){
        ArrayList<String> toRemoveFromUpgradeShop = new ArrayList<>(upgradeShop);
        upgradeShop.removeAll(toRemoveFromUpgradeShop);
        String[] newUpgradeCards = new String[playingPlayers.size()];
        for(int i=0; i<playingPlayers.size(); i++){
            Random rand = new Random();
            // Eventuell muss hier plus 1
            int indexOfRandomUpgradeCard = rand.nextInt(upgradeCards.size());
            String card = upgradeCards.get(indexOfRandomUpgradeCard);
            upgradeShop.add(card);
            newUpgradeCards[i] = card;
            upgradeCards.remove(card);
        }

        JSONMessage jsonMessageExchangeShop = new JSONMessage("ExchangeShop", new RefillShopObj(newUpgradeCards));
        broadcast(jsonMessageExchangeShop);
    }

    /**
     * Checks whether an upgrade is in the Upgradeshop to make sure the card is valid
     * @param upgradeCard the card that is being checked
     * @return whether card is valid
     */
    public boolean isUpgradeCardInUpgradeShop(String upgradeCard){
        return upgradeShop.contains(upgradeCard);
    }

    /**
     * removes a card from upgradeshop when bought
     * @param card the card that was bought
     */
    public void removeCardFromUpgradeShop(String card){
        upgradeShop.remove(card);
    }

    /**
     * @return Returns the cards in Upgrade Shop
     */
    public ArrayList<String> getUpgradeShop() {
        return upgradeShop;
    }

    /**
     * Returns the price of an upgrade card
     * @param upgradeCard the card we are interested in
     * @return the erpice of the card
     */
    public int getPriceOfUpgradeCard(String upgradeCard){

        switch (upgradeCard) {
            case "ADMIN PRIVILEGE":
            case "SPAM BLOCKER":
                return 3;
            case "REAR LASER":
                return 2;
            case "MEMORY SWAP":
                return 1;
            default :
                return 0;
        }
    }

    /**
     * Adds a client ID and a register to AdminPrivileges
     * @param register the register the client wants priority for
     * @param clientID the client that asks for admin privilege
     * @return
     */
    public boolean addToAdminPrivileges(int register, int clientID){
        if(!adminPrivileges.containsKey(register)) {
            adminPrivileges.put(register, clientID);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Changes the priority array according to admin privileges
     * @param oldPriorityArray The priority array as it was before admin privilege
     * @param key The register for which a client wants priority
     * @return The chanhed priority array
     * @author Benedikt
     */
    public int[] changePriorityArray(int[] oldPriorityArray, int key){
        ArrayList<Integer> newPriorityList = new ArrayList<>();
        for (int i = 0; i < oldPriorityArray.length; i++) {
            newPriorityList.add(i, oldPriorityArray[i]);
        }
        int clientID = adminPrivileges.get(key);
        newPriorityList.remove((Integer) clientID);
        newPriorityList.add(0, clientID);
        oldPriorityArray = newPriorityList.stream().mapToInt(i->i).toArray();
        adminPrivileges.remove(key);
        return oldPriorityArray;
    }

    /**
     * Empties all players hands
     */
    public void emptyAllPlayersHands(){
        for(Player p : playingPlayers){
            p.emptyHand();
            String[] cardsInHand = p.getCardsInHand().toArray(new String[0]);
            JSONMessage jsonMessageYourCards = new JSONMessage("YourCards", new YourCardsObj(cardsInHand));
            privateMessage(jsonMessageYourCards, p.getPlayerID());
        }
    }

    /**
     * @return retruns the next Player that has not set his starting point
     */
    public int getNextPlayersNoStartingPoint() {
        return playersNoStartingPoint.get(0).getPlayerID();
    }

    /**
     * If a player has set his starting point, he is removed from the List of players with no starting point
     */
    public void removePlayerFromPlayersNoStartingPoint() {
        this.playersNoStartingPoint.remove(0);
    }

    /**
     * Game starts and every player chooses starting point
     * @param readyPlayers all clients that have set figure and name and are ready
     * @author Benedikt
     */
    public void playPhaseNull(ArrayList<Player> readyPlayers) {

        gameStarted = true;
        // Add all readyPlayers to playing players
        playingPlayers.addAll(readyPlayers);

        // Aufbau Phase
        JSONMessage jsonMessageActivePhase0 = new JSONMessage("ActivePhase", new ActivePhaseObj(0));
        broadcast(jsonMessageActivePhase0);
        phase = 0;

        for (Player p : playingPlayers) {
            // Set Starting point
            if(getSelectedMap().equals("DeathTrap")){
                p.setRobotDirection("east");
            }
            else {
                p.setRobotDirection("west");
            }
        }
        // First Player to set starting point is determined
        playersNoStartingPoint.addAll(playingPlayers);
        int firstPlayer = playersNoStartingPoint.get(0).getPlayerID();
        playersNoStartingPoint.remove(0);
        JSONMessage jsonMessageSetStartingPoint = new JSONMessage("CurrentPlayer", new CurrentPlayerObj(firstPlayer));
        broadcast(jsonMessageSetStartingPoint);
    }

    /**
     * Upgradeshop is built, refilled or exchanged and the first player to buy an upgrade card is determined
     * @author Benedikt
     */
    public void playUpgradePhase() {
        // Upgrade Phase
        JSONMessage jsonMessageActivePhase1 = new JSONMessage("ActivePhase", new ActivePhaseObj(1));
        broadcast(jsonMessageActivePhase1);
        phase = 1;
        if(firstRound){
            buildUpgradeCards();
            for (Player p : playingPlayers){
                p.firstEnergyCubes();
                EnergyObj getEnergyObj = new EnergyObj(p.getPlayerID(), p.getEnergyCubes(), "EnergySpace");
                JSONMessage jsonMessageEnergyCubes = new JSONMessage("Energy", getEnergyObj );
                privateMessage(jsonMessageEnergyCubes, p.getPlayerID());
            }
        }

        if(upgradeShop.size() != playingPlayers.size()){
            // upgradeShop.clear();
            refillShop();
        }
        else {
            exchangeShop();
        }
        // Determine priorities
        priorityArray = getPriorityArrayClientIDs();
        int currentPlayerID = priorityArray[0];
        // Inform about current player
        JSONMessage jsonMessageCurrentPlayer = new JSONMessage("CurrentPlayer", new CurrentPlayerObj(currentPlayerID));
        broadcast(jsonMessageCurrentPlayer);
    }

    /**
     * Determines the next player to buy an upgarde card. If all players have bought one, programming phase is played.
     * @author Benedikt
     */
    public void nextPlayer(){
        if(currentPositionInPriorityArray == playingPlayers.size()){
            playProgrammingPhase();
        }
        else {
            int currentPlayerID = priorityArray[currentPositionInPriorityArray];
            // Inform about current player
            JSONMessage jsonMessageCurrentPlayer = new JSONMessage("CurrentPlayer", new CurrentPlayerObj(currentPlayerID));
            broadcast(jsonMessageCurrentPlayer);
            currentPositionInPriorityArray++;
        }
    }

    /**
     * Each player gets 9 cards and is asked to fill registers
     * @author Benedikt
     */
    public void playProgrammingPhase() {
        // Programming Phase
        JSONMessage jsonMessageActivePhase2 = new JSONMessage("ActivePhase", new ActivePhaseObj(2));
        broadcast(jsonMessageActivePhase2);
        phase = 2;

        // HandOutCards
        for (Player p : playingPlayers) {
            String[] cardsInHandPlayer = handOut9Cards(p.getPlayerID());

            JSONMessage jsonMessageYourCards = new JSONMessage("YourCards", new YourCardsObj(cardsInHandPlayer));
            privateMessage(jsonMessageYourCards, p.getPlayerID());

            for (String card : cardsInHandPlayer) {
                p.addCardInHand(card);
            }
            // Fill registers
            String infoFillRegister = "Please fill your registers 0 to 4.";
            JSONMessage jsonMessageInfoFillRegister = new JSONMessage("SendChat", new SendChatObj(infoFillRegister, -1));
            privateMessage(jsonMessageInfoFillRegister, p.getPlayerID());
        }
    }

    /**
     * Puts cards and the associated clientIDs from every player in a List in the correct order
     * @author Benedikt
     */
    public void playActivationPhase(int register){
        // Activation Phase
        phase = 3;
        int[] priorityArray = getPriorityArrayClientIDs();
        if(adminPrivileges.containsKey(register)){
            int[] priorityArrayAdminPrivilege = changePriorityArray(priorityArray, register);
            priorityArray = priorityArrayAdminPrivilege;
        }
        // For every played Card
        for (int j = 0; j < priorityArray.length; j++) {
            Player player = getPlayer(priorityArray[j]);
            String card = player.getCardInRegister(register);
            player.removeCardFromRegister(card, register);
            cardsInCorrectOrder.add(player.getPlayerID() + card);
        }
        this.currentPositionInPriorityArray = 1;

        char playerIDAsChar = cardsInCorrectOrder.get(0).charAt(0);
        int firstPlayer = Integer.parseInt(String.valueOf(playerIDAsChar));
        JSONMessage jsonMessageCurrentPlayer = new JSONMessage("CurrentPlayer", new CurrentPlayerObj(firstPlayer));
        broadcast(jsonMessageCurrentPlayer);

    }

    /**
     * Always plays the first card out of the List created in playActivationPhase.
     * Sends a message who the current player is
     * Checks if field objects activate, if yes it waits a few moments and board elements activate
     * @author Benedikt
     */
    public void playNextCard(){
        if(!playNextCardBlocked) {
            countPlayedCards++;
            if (!playerRebootedID.isEmpty()) {
                for (String card : cardsInCorrectOrder) {
                    char playerIDAsChar = card.charAt(0);
                    int playersID = Integer.parseInt(String.valueOf(playerIDAsChar));
                    if (playerRebootedID.contains(playersID)) {
                        String emptyCard = playersID + "empty";
                        cardsInCorrectOrder.set(cardsInCorrectOrder.indexOf(card), emptyCard);
                    }
                }
            }

            if (!cardsInCorrectOrder.isEmpty()) {
                // get first element of the cards sorted by priority of clients
                String nextCard = cardsInCorrectOrder.get(0);
                // remove the card
                cardsInCorrectOrder.remove(0);
                // get the player ID to which the card belongs
                char playerIDAsChar = nextCard.charAt(0);
                int playerID = Integer.parseInt(String.valueOf(playerIDAsChar));
                // Send the next player that it will be his turn
                char nextPlayerIDAsChar = nextCard.charAt(0);
                if (cardsInCorrectOrder.size() > 0) {
                    nextPlayerIDAsChar = cardsInCorrectOrder.get(0).charAt(0);
                }
                int nextPlayerID = Integer.parseInt(String.valueOf(nextPlayerIDAsChar));
                JSONMessage jsonMessageCurrentPlayer = new JSONMessage("CurrentPlayer", new CurrentPlayerObj(nextPlayerID));
                broadcast(jsonMessageCurrentPlayer);
                // get the card that is played next
                nextCard = nextCard.substring(1);
                // If card is Again: get last played card
                if (nextCard.equals("Again")) {
                    nextCard = getPlayer(playerID).getLastRegisterCard();
                }
                // Send to client which card is played
                JSONMessage jsonMessagePlayCard = new JSONMessage("PlayCard", new PlayCardObj(nextCard));
                privateMessage(jsonMessagePlayCard, playerID);
            }

            if (countPlayedCards == playingPlayers.size()) {
                isRegisterOver = true;
                countPlayedCards = 0;
                currentRegister++;
                playNextCardBlocked = true;

                JSONMessage jsonMessageCurrentPlayer = new JSONMessage("CurrentPlayer", new CurrentPlayerObj(0));
                broadcast(jsonMessageCurrentPlayer);

                TimerTask tasknew = new TimerTask() {
                    public void run() {
                        JSONMessage jsonMessagePlayCard = new JSONMessage("PlayCard", new PlayCardObj("Activate"));
                        privateMessage(jsonMessagePlayCard, playerID);
                        JSONMessage jsonMessageCurrentPlayer = new JSONMessage("CurrentPlayer", new CurrentPlayerObj(0));
                        broadcast(jsonMessageCurrentPlayer);
                        // Calculate new priorityarray
                        if (currentRegister < 5) {
                            playActivationPhase(currentRegister);
                        }

                        // If all cards were played, a new round starts with Upgrade Phase
                        if (currentRegister == 5) {
                            isRegisterOver = true;
                            countPlayedCards = 0;

                            for (Player p : playingPlayers) {
                                p.emptyRegisters();
                                String emptyRegisterMessage = "Your registers have been emptied.";
                                JSONMessage jsonMessageEmptyRegister = new JSONMessage("SendChat", new SendChatObj(emptyRegisterMessage, p.getPlayerID()));
                                privateMessage(jsonMessageEmptyRegister, p.getPlayerID());
                            }
                            currentRegister = 0;
                            firstRound = false;
                            playerRebootedID.clear();
                            playUpgradePhase();
                        }
                        playNextCardBlocked = false;
                    }
                };
                Timer timer = new Timer();
                long delay = 1500;
                timer.schedule(tasknew, delay);

            }
        }
    }

    /**
     * build map
     * @param map
     */
    public void buildMap(String map){
        if(map.equals("DizzyHighway")) {
            this.map = map("DizzyHighway");
        } else if (map.equals("DeathTrap")) {
            this.map = map("DeathTrap");
        } else if (map.equals("ExtraCrispy")) {
            this.map = map("ExtraCrispy");
        } else if (map.equals("LostBearings")) {
            this.map = map("LostBearings");
        } else if (map.equals("Twister")) {
            this.map = map("Twister");
        } else {
            logger.info(ANSI_GREEN + "this map doesn't exist" + ANSI_GREEN);
        }
    }

    /**
     * read json file, deserialize json map und get field's object information
     * @param mapName
     * @return
     * @author Aigerim Abdykadyrova
     */
    public ArrayList<ArrayList<ArrayList<Field>>> map(String mapName) {
        try {
            Path maps = Paths.get("Server/src/main/resources/" +
                    mapName+".json");
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
     *
     * @return HashMap of all Wall field objects and add PushPanel as Wall
     * @author Arda
     */
    public HashMap<String, Wall> getWallMap(){

        HashMap<String,Wall> walls = new HashMap<>() ;
        for(int i = 0;i<map.size(); i++){
            for(int k = 0;k<map.get(0).size();k ++){
                for(Field f:map.get(i).get(k)){
                    if(f instanceof Wall){
                        walls.put(i+"_"+k,(Wall)f);
                    }
                }
            }
        }
        if(selectedMap.equals("DeathTrap")){
            for(String key : getAddPushPanelToWall().keySet()){
                walls.put(key,getAddPushPanelToWall().get(key));
            }
           ArrayList<String> orientationTop = new ArrayList<>();
            orientationTop.add ("top");
            ArrayList<String> orientationLeft = new ArrayList<>();
            orientationLeft.add ("left");
            ArrayList<String> orientationBottom = new ArrayList<>();
            orientationBottom.add ("bottom");
            walls.put("0_4",new Wall("Start A",orientationBottom,null));
            walls.put("0_5",new Wall("Start A",orientationLeft,null));
            walls.put("0_6",new Wall("Start A",orientationTop,null));


       }else{
            ArrayList<String> orientationTop = new ArrayList<>();
            orientationTop.add ("top");
            ArrayList<String> orientationRight = new ArrayList<>();
            orientationRight.add ("right");
            ArrayList<String> orientationBottom = new ArrayList<>();
            orientationBottom.add ("bottom");
            walls.put("0_3",new Wall("Start A",orientationBottom,null));
            walls.put("0_4",new Wall("Start A",orientationRight,null));
            walls.put("0_5",new Wall("Start A",orientationTop,null));


        }
        return walls;
    }
    /**
     *
     * @param p current player
     * @return true if player is on conveyor belt, false if not
     */
    public boolean checkConveyorBelt (Player p){
        HashMap<String, ConveyorBelt> conveyorBeltMap = getConveyorBeltMap();
        String playerPosition = p.getRobotX() + "_" + p.getRobotY();
        if(conveyorBeltMap.containsKey(playerPosition)){
            return true;
        }
        return false;
    }

    /**
     *
     * @return HashMap of all CheckPoint field objects
     * @author Arda, Ilinur
     */
    public HashMap<String, CheckPoint> getCheckPointMap() {
        HashMap<String, CheckPoint> checkPoint = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            for (int k = 0; k < map.get(0).size(); k++) {
                for (Field f : map.get(i).get(k)) {
                    if (f instanceof CheckPoint) {
                        checkPoint.put(i + "_" + k, (CheckPoint) f);
                    }
                }
            }
        }

        if (this.selectedMap.equals("Twister")) {
            if (this.movedTwisterCheckpoints.size() == 0) {
                movedTwisterCheckpoints = checkPoint;
                return checkPoint;
            } else {
                return movedTwisterCheckpoints;
            }
        } else {
            return checkPoint;
        }
    }

    /**
     *
     * @param p current player
     * @return true, if player is on checkPoint
     */
    public boolean checkPointControl (Player p){
        HashMap<String, CheckPoint> control = getCheckPointMap();
        String playerPosition = p.getRobotX() + "_" + p.getRobotY();
        if(control.containsKey(playerPosition)){
            CheckPoint c = control.get(playerPosition);
            if(c.getCount()-1 == p.getCheckpointTokens()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     *
     * @return HashMap of all ConveyorBelt field objects
     */
    public HashMap<String, ConveyorBelt> getConveyorBeltMap() {
        HashMap<String, ConveyorBelt> conveyorBelt = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            for (int k = 0; k < map.get(0).size(); k++) {
                for (Field f : map.get(i).get(k)) {
                    if (f instanceof ConveyorBelt) {
                        conveyorBelt.put(i + "_" + k, (ConveyorBelt) f);
                    }
                }
            }
        }
        return conveyorBelt;
    }

    /**
     *
     * @return HashMap of all Laser field Objects
     */
    public HashMap<String, Laser> getLaserMap() {
        HashMap<String, Laser> laser = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            for (int k = 0; k < map.get(0).size(); k++) {
                for (Field f : map.get(i).get(k)) {
                    if (f instanceof Laser) {
                        laser.put(i + "_" + k, (Laser) f);
                    }
                }
            }
        }
        return laser;
    }

    /**
     * for programming virus damage cards
     * @param player is the current player
     * @return true, if other player is within a six-space radius of current player
     */
    public boolean isInSixSpaceRadius(Player player){
        for(Player otherPlayer : playingPlayers) {

            if(otherPlayer.getPlayerID() != player.getPlayerID()) {

                if (player.getRobotX() - otherPlayer.getRobotX() <= 6 && player.getRobotX() - otherPlayer.getRobotX() >= -6 && player.getRobotY() - otherPlayer.getRobotY() <= 6 && player.getRobotY() - otherPlayer.getRobotY() >= -6) {
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * initialize EnergySpace hashMap
     */
    public void initializeEnergySpaceMap() {
        HashMap<String, EnergySpace> energySpaceMap = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            for (int k = 0; k < map.get(0).size(); k++) {
                for (Field f : map.get(i).get(k)) {
                    if (f instanceof EnergySpace) {
                        energySpaceMap.put(i + "_" + k,(EnergySpace) f);
                    }
                }
            }
        }
        this.energySpaceMap = energySpaceMap;
    }

    /**
     *
     * @param newMap, a new map is created, when player has already removed energyCube from field
     */
    public void setEnergySpaceMap(HashMap<String, EnergySpace> newMap) {
        this.energySpaceMap = newMap;
    }

    /**
     *
     * @return initialized EnergySpace map
     */
    public HashMap<String, EnergySpace> getEnergySpaceMap(){
        if(this.energySpaceMap == null){
            initializeEnergySpaceMap();
        }
        return this.energySpaceMap;
    }

    /**
     *
     * @param p current player
     * @return true, if player is on energySpace
     * @author Ilinur
     */
    public boolean checkEnergySpace (Player p){
        HashMap<String, EnergySpace> energySpaceMap = getEnergySpaceMap();
        String playerPosition = p.getRobotX() + "_" + p.getRobotY();
        if(energySpaceMap.containsKey(playerPosition)){
            for(String key : energySpaceMap.keySet()){
                if(key.equals(playerPosition)){
                    energyKeys.add(key);
                }
            }
            energySpaceMap.remove(playerPosition);
            setEnergySpaceMap(energySpaceMap);

            return true;
        }
        {
            return false;
        }
    }

    /**
     * 
     * @param p current player
     * @return true if the player ends the last register on a energyField and if there is no energyCube left on it
     */
    public boolean checkLastRegisterEnergySpace(Player p){
        String playerPosition = p.getRobotX() + "_" + p.getRobotY();
        if(energyKeys.contains(playerPosition) && currentRegister == 5  && !checkEnergySpace(p)){
            return true;
        }
        else return false;
    }

    public void placeOnReboot(Player player){

        for (int i = 0; i < map.size(); i++){
            for (int k = 0; k < map.get(0).size(); k++){
                for (Field f : map.get(i).get(k)){
                    if(f instanceof RestartPoint){
                        player.setRobotX(i);
                        player.setRobotY(k);
                    }
                }
            }
        }
        Move1 move1 = new Move1();
        player.setRobotDirection(getRebootFieldOrientation());
        move1.pushRobots(player,this);
        player.setRobotDirection("south");

    }

    /**
     *
     * @return hashMap of pit field objects
     */
    public HashMap<String, Pit> getPitMap() {
        HashMap<String,Pit> pitMap = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            for (int k = 0; k < map.get(0).size(); k++) {
                for (Field f : map.get(i).get(k)) {
                    if (f instanceof Pit) {
                        pitMap.put(i + "_" + k,(Pit) f);
                    }
                }
            }
        }
        return pitMap;
    }

    /**
     *
     * @param p current player
     * @return true, if current player is on pit
     */
    public boolean checkPit (Player p){
        HashMap<String, Pit> pitMap = getPitMap();
        String playerPosition = p.getRobotX() + "_" + p.getRobotY();
        if(pitMap.containsKey(playerPosition)){
            return true;
        }
        return false;
    }

    /**
     * reboot player if on pit
     * @param p current player
     */
    public void checkIfPit(Player p) {
        boolean checkOnPit = checkPit(p);
        if (checkOnPit) {
            reboot(p);
        }
    }

    /**
     *
     * @return hashMap of gear Field objects
     */
    public HashMap<String, Gear> getGearMap() {
        HashMap<String,Gear> gearMap = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            for (int k = 0; k < map.get(0).size(); k++) {
                for (Field f : map.get(i).get(k)) {
                    if (f instanceof Gear) {
                        gearMap.put(i + "_" + k,(Gear) f);
                    }
                }
            }
        }
        return gearMap;
    }

    /**
     *
     * @param p current player
     * @return true, if player is on gear
     */
    public boolean checkGear (Player p){
        HashMap<String, Gear> gearMap = getGearMap();
        String playerPosition = p.getRobotX() + "_" + p.getRobotY();
        if(gearMap.containsKey(playerPosition)){
            return true;
        }
        return false;
    }

    /**
     *
     * @return HashMap of PushPanel Field objects
     */
    public HashMap<String, PushPanel> getPushPanelMap() {
        HashMap<String,PushPanel> pushPanelMap = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            for (int k = 0; k < map.get(0).size(); k++) {
                for (Field f : map.get(i).get(k)) {
                    if (f instanceof PushPanel) {
                        pushPanelMap.put(i + "_" + k,(PushPanel) f);
                    }
                }
            }
        }
        return pushPanelMap;
    }

    /**
     *
     * @return HashMap of Antenna Field objects
     */
    public HashMap<String, Antenna> getAntennaMap() {
        HashMap<String,Antenna> antennaMap = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            for (int k = 0; k < map.get(0).size(); k++) {
                for (Field f : map.get(i).get(k)) {
                    if (f instanceof Antenna) {
                        antennaMap.put(i + "_" + k,(Antenna) f);
                    }
                }
            }
        }
        return antennaMap;
    }

    /**
     *
     * @param p current player
     * @return true, if player is on pushPanel
     */
    public boolean checkPushPanel (Player p){
        HashMap<String, PushPanel> pushPanelHashMap = getPushPanelMap();
        String playerPosition = p.getRobotX() + "_" + p.getRobotY();
        if(pushPanelHashMap.containsKey(playerPosition)){
            return true;
        }
        return false;
    }

    /**
     *
     * @return pushPanel as wall objects
     * @author Arda, Ilinur
     */
    public HashMap<String, Wall> getAddPushPanelToWall(){
        HashMap<String, PushPanel> pushPanel = getPushPanelMap();
        HashMap<String, Wall> pushPanelWall = new HashMap<>();

        for(String key : pushPanel.keySet()){
            Wall w = new Wall(pushPanel.get(key).getIsOnBoard(),oppositeOrientationPushPanel(pushPanel.get(key).getOrientation()),pushPanel.get(key).getRegister());
            pushPanelWall.put(key,w);
        }

        return pushPanelWall;
    }
    
    /**
     *
     * @param orientations PushPanel orientation
     * @return opposite orientation of PushPanel for Wall
     */
    public ArrayList<String> oppositeOrientationPushPanel(ArrayList<String> orientations){
        ArrayList<String> correctOrientation = new ArrayList<>();
        String orientation = orientations.get(0);

        switch (orientation){
            case "top":
                correctOrientation.add("bottom");
                break;
            case "bottom":
                correctOrientation.add("top");
                break;
            case "right":
                correctOrientation.add("left");
                break;
            case "left":
                correctOrientation.add("right");
                break;
        }
        return correctOrientation;
    }

    /**
     * @return if all cards from a certain register were played
     */
    public boolean isRegisterOver() {
        return isRegisterOver;
    }

    /**
     * When all cards from a certain register were played isRegisterOver is true
     * @param registerOver
     */
    public void setRegisterOver(boolean registerOver) {
        isRegisterOver = registerOver;
    }

    /**
     *
     * @return true, if robot has fallen off the board
     * @author Ilinur, Rea
     */
    public boolean fallenOffBoard(Player player) {
        if (getSelectedMap().equals("DeathTrap")) {
            if (player.getRobotX() < 0 || player.getRobotY() > 9 || player.getRobotY() < 0) {
                return true;
            } else {
                return false;
            }
        }
        else {
            if (player.getRobotX() > 12 || player.getRobotY() > 9 || player.getRobotY() < 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     *
     * @param player current playing
     * @return true, if player has fallen of player mat field
     * @author Ilinur
     */
    public boolean fallenOfPlayerBoard(Player player){
        if(getSelectedMap().equals("DeathTrap")){
            if (player.getRobotX() > 12 || player.getRobotY() > 9 && player.getRobotX() == 10 || player.getRobotY() > 9 && player.getRobotX() == 11 || player.getRobotY() > 9 && player.getRobotX() == 12
                    || player.getRobotY() < 0 && player.getRobotX() == 10 || player.getRobotY() < 0 && player.getRobotX() == 11 || player.getRobotY() < 0 && player.getRobotX() == 12) {
                return true;
            } else {
                return false;
            }
        }
        else {
            if (player.getRobotX() < 0 || player.getRobotY() > 9 && player.getRobotX() == 0 || player.getRobotY() > 9 && player.getRobotX() == 1 || player.getRobotY() > 9 && player.getRobotX() == 2
            || player.getRobotY() < 0 && player.getRobotX() == 0 || player.getRobotY() < 0 && player.getRobotX() == 1 || player.getRobotY() < 0 && player.getRobotX() == 2 ) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Checks which players have been shot by wall lasers. Check for a particular laser stops when a robot is found and
     * shot, or a wall is encountered.
     * @return A list of players who have been shot.
     * @author Arda
     */
    public ArrayList<Player> whoGotShotByWallLasers() {
        ArrayList<Player> toReturn = new ArrayList<>();
        for (String key : getLaserMap().keySet()) {
            Laser l = getLaserMap().get(key);
            String laserOrientation = l.getOrientation().get(0);
            int laserX = Integer.parseInt(key.split("_")[0]);
            int laserY = Integer.parseInt(key.split("_")[1]);
            boolean wallFound = false;
            boolean playerFound = false;

            switch (laserOrientation) {
                case "top":
                    //check for robot, then check for bottom wall, lastly check for top wall
                    for (int i = laserY; i >= 0; i--) {
                        if (!wallFound && !playerFound) {
                            for (Player p : getPlayingPlayers()) {
                                if (p.getRobotX() == laserX && p.getRobotY() == i) {
                                    toReturn.add(p);
                                    playerFound = true;
                                }
                                //check for walls,
                                if (getWallMap().containsKey(laserX + "_" + i)) {
                                    if (getWallMap().get(laserX + "_" + i).getOrientation().get(0).equals("top")) {
                                        wallFound = true;

                                    }
                                }
                                int k = i - 1;
                                if (getWallMap().containsKey(laserX + "_" + k)) {
                                    if (getWallMap().get(laserX + "_" + k).getOrientation().get(0).equals("bottom")) {
                                        wallFound = true;

                                    }
                                }
                            }
                        }
                    }
                    break;

                case "bottom":

                    for (int i = laserY; i <= 9; i++) {
                        if (!wallFound && !playerFound) {
                            for (Player p : getPlayingPlayers()) {
                                if (p.getRobotX() == laserX && p.getRobotY() == i) {
                                    toReturn.add(p);
                                    playerFound = true;

                                }
                                //check for walls,
                                if (getWallMap().containsKey(laserX + "_" + i)) {
                                    if (getWallMap().get(laserX + "_" + i).getOrientation().get(0).equals("bottom")) {
                                        wallFound = true;

                                    }
                                }
                                int k = i + 1;
                                if (getWallMap().containsKey(laserX + "_" + k)) {
                                    if (getWallMap().get(laserX + "_" + k).getOrientation().get(0).equals("top")) {
                                        wallFound = true;

                                    }
                                }
                            }
                        }
                    }
                    break;

                case "right":

                    for (int i = laserX; i <= 12; i++) {
                        if (!wallFound && !playerFound) {
                            for (Player p : getPlayingPlayers()) {
                                if (p.getRobotX() == i && p.getRobotY() == laserY) {
                                    toReturn.add(p);
                                    playerFound = true;

                                }
                                //check for walls,
                                if (getWallMap().containsKey(i + "_" + laserY)) {
                                    if (getWallMap().get(i + "_" + laserY).getOrientation().get(0).equals("right")) {
                                        wallFound = true;

                                    }
                                }
                                int k = i + 1;
                                if (getWallMap().containsKey(k + "_" + laserY)) {
                                    if (getWallMap().get(k + "_" + laserY).getOrientation().get(0).equals("left")) {
                                        wallFound = true;

                                    }
                                }
                            }
                        }
                    }
                    break;

                case "left":

                    for (int i = laserX; i >= 0; i--) {
                        if (!wallFound && !playerFound) {
                            for (Player p : getPlayingPlayers()) {
                                if (p.getRobotX() == i && p.getRobotY() == laserY) {
                                    toReturn.add(p);
                                    playerFound = true;
                                }
                                //check for walls,
                                if (getWallMap().containsKey(i + "_" + laserY)) {
                                    if (getWallMap().get(i + "_" + laserY).getOrientation().get(0).equals("left")) {
                                        wallFound = true;

                                    }
                                }
                                int k = i - 1;
                                if (getWallMap().containsKey(k + "_" + laserY)) {
                                    if (getWallMap().get(k + "_" + laserY).getOrientation().get(0).equals("right")) {
                                        wallFound = true;

                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        }
        return toReturn;
    }

    /**
     * Checks if a robot has been shot by another robot and if yes, how many times they've been shot. It's also taken
     * into account if a player has bought rear laser.
     * @param player
     * @return the amount of times player has been shot.
     * @author Arda
     */
    public int howManyTimesShotByOtherRobots (Player player){
        ArrayList<Player> playingPlayersAndRearLaser = (ArrayList<Player>) playingPlayers.clone();
        Random random = new Random();
        for (Player p : getPlayingPlayers()){
            if (p.getBoughtRearLaser()){
                Player pl = new Player ("RearLaser",random.nextInt(900000),10);
                pl.setRobotX(p.getRobotX());
                pl.setRobotY(p.getRobotY());
                switch(p.getRobotDirection()){
                    case "north":
                        pl.setRobotDirection("south");
                        break;
                    case "south":
                        pl.setRobotDirection("north");
                        break;
                    case "east":
                        pl.setRobotDirection("west");
                        break;
                    case "west":
                        pl.setRobotDirection("east");
                        break;
                }

                playingPlayersAndRearLaser.add(pl);
            }
        }

        int shot = 0;

        for (Player p : playingPlayersAndRearLaser){
            if(!playerRebootedID.contains(p.getPlayerID())) {

                String pDirection = p.getRobotDirection();
                int pRobotX = p.getRobotX();
                int pRobotY = p.getRobotY();
                boolean robotFound = false;
                boolean wallFound = false;

                switch (pDirection) {
                    case "north":
                        for (int i = pRobotY; i <= 9; i++) {

                            if (getWallMap().containsKey(pRobotX + "_" + i)) {
                                if (getWallMap().get(pRobotX + "_" + i).getOrientation().get(0).equals("bottom")) {
                                    wallFound = true;
                                }
                            }

                            int k = i + 1;
                            if (getWallMap().containsKey(pRobotX + "_" + k)) {
                                if (getWallMap().get(pRobotX + "_" + k).getOrientation().get(0).equals("top")) {
                                    wallFound = true;
                                }
                            }

                            if (!robotFound && !wallFound) {
                                if (player.getRobotX() == pRobotX && player.getRobotY() == k) {
                                    shot++;
                                    robotFound = true;
                                }
                            }
                        }
                        break;

                    case "south":
                        for (int i = pRobotY; i >= 0; i--) {

                            if (getWallMap().containsKey(pRobotX + "_" + i)) {
                                if (getWallMap().get(pRobotX + "_" + i).getOrientation().get(0).equals("top")) {
                                    wallFound = true;
                                }
                            }
                            int k = i - 1;
                            if (getWallMap().containsKey(pRobotX + "_" + k)) {
                                if (getWallMap().get(pRobotX + "_" + k).getOrientation().get(0).equals("bottom")) {
                                    wallFound = true;
                                }
                            }
                            if (!robotFound && !wallFound) {
                                if (player.getRobotX() == pRobotX && player.getRobotY() == k) {
                                    shot++;
                                    robotFound = true;

                                }
                            }
                        }
                        break;

                    case "west":
                        for (int i = pRobotX; i <= 12; i++) {
                            if (getWallMap().containsKey(i + "_" + pRobotY)) {
                                if (getWallMap().get(i + "_" + pRobotY).getOrientation().get(0).equals("right")) {
                                    wallFound = true;
                                }
                            }
                            int k = i + 1;
                            if (getWallMap().containsKey(k + "_" + pRobotY)) {
                                if (getWallMap().get(k + "_" + pRobotY).getOrientation().get(0).equals("left")) {
                                    wallFound = true;
                                }
                            }
                            if (!robotFound && !wallFound) {
                                if (player.getRobotX() == k && player.getRobotY() == pRobotY) {
                                    shot++;
                                    robotFound = true;
                                }
                            }
                        }
                        break;

                    case "east":
                        for (int i = pRobotX; i >= 0; i--) {

                            if (getWallMap().containsKey(i + "_" + pRobotY)) {
                                if (getWallMap().get(i + "_" + pRobotY).getOrientation().get(0).equals("left")) {
                                    wallFound = true;
                                }
                            }
                            int k = i - 1;
                            if (getWallMap().containsKey(k + "_" + pRobotY)) {
                                if (getWallMap().get(k + "_" + pRobotY).getOrientation().get(0).equals("right")) {
                                    wallFound = true;
                                }
                            }
                            if (!robotFound && !wallFound) {
                                if (player.getRobotX() == k && player.getRobotY() == pRobotY) {
                                    shot++;
                                    robotFound = true;
                                }
                            }
                        }
                        break;
                }
            }
        }
        return shot;
    }


    /**
     *
     *   @param id figure ID
     * @return color of roboter according to the figure ID
     * @author Arda
     */
    public String getRobotColor (int id){
        switch(id){
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

    /**
     * change checkPoint position on TwisterMap
     * @author Arda, Ilinur
     */
    public void pushTwisterCheckPoints() {
        HashMap<String,CheckPoint> movingCheckPoints = new HashMap<>();

        for (String key: movedTwisterCheckpoints.keySet()){
            switch (movedTwisterCheckpoints.get(key).getCount()){
                case 1:
                    if (key.equals("10_1")){
                        movingCheckPoints.put( "11_2", movedTwisterCheckpoints.get(key));
                    }
                    if(key.equals("11_2")){
                        movingCheckPoints.put( "10_3", movedTwisterCheckpoints.get(key));
                    }
                    if(key.equals("10_3")){
                        movingCheckPoints.put( "9_2", movedTwisterCheckpoints.get(key));
                    }
                    if(key.equals("9_2")){
                        movingCheckPoints.put( "10_1", movedTwisterCheckpoints.get(key));
                    }
                case 2:
                    if (key.equals("6_7")){
                        movingCheckPoints.put( "5_8", movedTwisterCheckpoints.get(key));
                    }
                    if(key.equals("5_8")){
                        movingCheckPoints.put( "4_7", movedTwisterCheckpoints.get(key));
                    }
                    if(key.equals("4_7")){
                        movingCheckPoints.put( "5_6", movedTwisterCheckpoints.get(key));
                    }
                    if(key.equals("5_6")){
                        movingCheckPoints.put( "6_7", movedTwisterCheckpoints.get(key));
                    }
                case 3:
                    if (key.equals("5_3")){
                        movingCheckPoints.put( "4_2", movedTwisterCheckpoints.get(key));
                    }
                    if(key.equals("4_2")){
                        movingCheckPoints.put( "5_1", movedTwisterCheckpoints.get(key) );
                    }
                    if(key.equals("5_1")){
                        movingCheckPoints.put( "6_2", movedTwisterCheckpoints.get(key) );
                    }
                    if(key.equals("6_2")){
                        movingCheckPoints.put( "5_3", movedTwisterCheckpoints.get(key));
                    }
                case 4:
                    if (key.equals("9_7")){
                        movingCheckPoints.put( "10_6", movedTwisterCheckpoints.get(key));
                    }
                    if(key.equals("10_6")){
                        movingCheckPoints.put( "11_7", movedTwisterCheckpoints.get(key));
                    }
                    if(key.equals("11_7")){
                        movingCheckPoints.put( "10_8", movedTwisterCheckpoints.get(key));
                    }
                    if(key.equals("10_8")){
                        movingCheckPoints.put( "9_7", movedTwisterCheckpoints.get(key));
                    }

            }
        }

        movedTwisterCheckpoints = movingCheckPoints;
    }

    /**
     *Returns the orientation of the Reboot Field.
     * @return orientation
     * @author Arda
     */
    public String getRebootFieldOrientation (){
        switch(selectedMap){
            case "LostBearings" , "Twister" , "ExtraCrispy" :
                return "west" ;
            case "DizzyHighway" :
                return "north" ;
            case "DeathTrap":
                return "east";
        }
        return "south";
    }

    /**
     * Creates a HashMap of starting points.
     */
    public void initializeStartPointMap() {
        HashMap<String,StartPoint> startPointMap = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            for (int k = 0; k < map.get(0).size(); k++) {
                for (Field f : map.get(i).get(k)) {
                    if (f instanceof StartPoint) {
                        startPointMap.put(i + "_" + k,(StartPoint) f);
                    }
                }
            }
        }
        this.startPointMap = startPointMap;
    }

    public HashMap<String,StartPoint> getStartPointMap(){
        if (this.startPointMap.size() == 0){
            initializeStartPointMap();
        }

        return this.startPointMap;
    }

    /**
     * @return the current register
     */
    public int getCurrentRegister() {
        return currentRegister;
    }

    public ArrayList<String> getTrojanHorsePile() {
        return trojanHorsePile;
    }

    public ArrayList<String> getVirusPile() {
        return virusPile;
    }

    public ArrayList<String> getSpamPile() {
        return spamPile;
    }

    public ArrayList<String> getWormPile() {
        return wormPile;
    }

}

