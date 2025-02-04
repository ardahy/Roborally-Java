package utilities.objects;

import java.io.*;
import java.util.logging.*;

import com.fasterxml.jackson.databind.util.ClassUtil;
import view.*;
import viewModel.Client;
import javafx.application.Platform;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientMessageHandler {
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    private Integer[] cYx = new Integer[2];
    private static int currentRegister = 0;

    private static final Logger logger = Logger.getLogger(ClientMessageHandler.class.getName());
    private boolean acceptConnection = true;
    boolean append3 = true;
    private FileHandler handler3;
    private String nameWinner;
    private int robotWinner;
    private String nameL;
    private int robotL;



    public ClientMessageHandler() {
        try {
            handler3 = new FileHandler("./logs/LoggingClientMessageHandler.log",
                    append3);
            logger.addHandler(handler3);
            SimpleFormatter formatter = new SimpleFormatter();
            handler3.setFormatter(formatter);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles all received messages
     * @param client
     * @param bodyObject
     */
    public void handleReceivedChat(Client client, ReceivedChatObj bodyObject) {
        String message = bodyObject.getMessage();
        logger.info(ANSI_CYAN + "( MESSAGE ): Entered handleReceivedChat()" + ANSI_RESET);
        String receivedMessage = "Message from " + bodyObject.getFrom() + ": " + message;
        if(!(bodyObject.getMessage().contains("Play next Card."))) {
            Platform.runLater(() -> client.receiveMessage(receivedMessage));
        }
    }

    public void handleHelloClient(Client client, HelloClientObj helloClientObj) {
        Platform.runLater(() -> client.receiveMessage(helloClientObj.getProtocol()));
        logger.info(ANSI_CYAN + "( MESSAGE ): HelloClient protocol " +
                "version 2.1" + ANSI_RESET);
    }

    /**
     * Error pop ups for clients
     * @param client
     * @param bodyObject
     */
    public void handleErrorMessage(Client client, ErrorObj bodyObject) {
        logger.warning(ANSI_CYAN + "( MESSAGE ): " + bodyObject.getError() + ANSI_RESET);
        if(!(bodyObject.getError().equals("Waiting until a map is selected..."))) {
            if(!client.isAI()) {
                Platform.runLater(() -> {
                    ErrorPopUp popup = new ErrorPopUp();
                    popup.openError(bodyObject.getError());
                });
            }

        }

        if(bodyObject.getError().equals("Your robot is taken.")) {
            String renameYourRobot = "Please rename your robot";
            client.setRenameRobot(renameYourRobot);
        }

    }

    public void handleMapSelected(Client client, MapSelectedObj bodyObject){
        client.setMapSelected(bodyObject.getMap());
        logger.info(ANSI_CYAN + "( MESSAGE ): handleMapSelected im Client " +
                "messageHandler: " + client.getMapSelected() + ANSI_RESET);
    }

    public void handleWelcome(Client client, WelcomeObj bodyObject) {
        String yourID = "Your ID is: " + bodyObject.getClientID();
        Platform.runLater(() -> client.receiveMessage(yourID));
        client.setClientID(bodyObject.getClientID());
        logger.info(ANSI_CYAN + "( MESSAGE ): bodyObject.getClientID()" + ANSI_RESET);
    }

    /**
     * player gets his 9 cards for game and shows in GUI
     * @param client
     * @param bodyObject
     */
    public void handleYourCards(Client client, YourCardsObj bodyObject) {
        Platform.runLater(() -> client.setYourCardsInHand(bodyObject.getCardsInHand()));
        String arrayToString = "Your cards are: " +  Arrays.toString(bodyObject.getCardsInHand());
        logger.info(ANSI_CYAN + arrayToString + ANSI_RESET);
    }

    /**
     * Handles all Played cards
     * @param client
     * @param bodyObject
     */
    public void handlePlayCard(Client client, PlayCardObj bodyObject) {
        String cardsInHand = "You played: " + bodyObject.getCard();
        if(!bodyObject.getCard().equals("MEMORY SWAP")){
            client.sendServerPlayedCard(bodyObject.getCard());
        }
        else{
            Platform.runLater(() ->{
                MemorySwap memorySwap = new MemorySwap();
                memorySwap.openMemorySwapPopUp(client);
            });
        }
        logger.info(ANSI_CYAN + "( MESSAGE ): " + cardsInHand + ANSI_RESET);
    }


    public void handleCardPlayed(Client client, CardPlayedObj bodyObject) {
        int clientID = bodyObject.getClientID();
        String cardPlayedMessage = "Player " + clientID + " played " + bodyObject.getCard();
        logger.info(ANSI_CYAN + "( MESSAGE ): "+bodyObject.getClientID() + " "+  bodyObject.getCard() + ANSI_RESET);
        if(bodyObject.getCard().equals("MEMORY SWAP") || bodyObject.getCard().equals("SPAM BLOCKER")) {
            if (client.getTemporaryUpgradeCardList().size() == 1) {
                Platform.runLater(() -> client.setNewUpgradeCardInGap1("Null"));
                Platform.runLater(() -> client.setNewUpgradeCardInGap2("Null"));
                Platform.runLater(() -> client.setNewUpgradeCardInGap3("Null"));
            } else if (client.getTemporaryUpgradeCardList().size() == 2) {
                if (bodyObject.getCard().equals("SPAM BLOCKER")) {
                    if (client.getTemporaryUpgradeCardList().get(1).equals("SPAM BLOCKER")) {
                        Platform.runLater(() -> client.setNewUpgradeCardInGap2("Null"));
                    } else if (client.getTemporaryUpgradeCardList().get(0).equals("SPAM BLOCKER")) {
                        Platform.runLater(() -> client.setNewUpgradeCardInGap1("Null"));
                    }
                } else if (bodyObject.getCard().equals("MEMORY SWAP")) {
                    if (client.getTemporaryUpgradeCardList().get(1).equals("MEMORY SWAP")) {
                        Platform.runLater(() -> client.setNewUpgradeCardInGap2("Null"));
                    } else if (client.getTemporaryUpgradeCardList().get(0).equals("MEMORY SWAP")) {
                        Platform.runLater(() -> client.setNewUpgradeCardInGap1("Null"));
                    }
                }
            } else if (client.getTemporaryUpgradeCardList().size() == 3) {
                if (bodyObject.getCard().equals("SPAM BLOCKER")) {
                    if (client.getTemporaryUpgradeCardList().get(2).equals("SPAM BLOCKER")) {
                        Platform.runLater(() -> client.setNewUpgradeCardInGap3("Null"));
                    } else if (client.getTemporaryUpgradeCardList().get(1).equals("SPAM BLOCKER")) {
                        Platform.runLater(() -> client.setNewUpgradeCardInGap2("Null"));
                    } else if (client.getTemporaryUpgradeCardList().get(0).equals("SPAM BLOCKER")) {
                        Platform.runLater(() -> client.setNewUpgradeCardInGap1("Null"));
                    }
                }
                 else if (bodyObject.getCard().equals("MEMORY SWAP")) {
                    if (client.getTemporaryUpgradeCardList().get(2).equals("MEMORY SWAP")) {
                        Platform.runLater(() -> client.setNewUpgradeCardInGap3("Null"));
                    } else if (client.getTemporaryUpgradeCardList().get(1).equals("MEMORY SWAP")) {
                        Platform.runLater(() -> client.setNewUpgradeCardInGap2("Null"));
                    } else if (client.getTemporaryUpgradeCardList().get(0).equals("MEMORY SWAP")) {
                        Platform.runLater(() -> client.setNewUpgradeCardInGap1("Null"));
                    }
                }
            }
            client.removeFromTemporaryUpgradeCardList(bodyObject.getCard());
        }
        if(bodyObject.getCard().equals("MEMORY SWAP") || bodyObject.getCard().equals("SPAM BLOCKER") || bodyObject.getCard().equals("ADMIN PRIVILEGE") || bodyObject.getCard().equals("REAR LASER")){
            Platform.runLater(() -> client.receiveMessage(cardPlayedMessage));
        }
    }

    public void handleNotYourCards(Client client, NotYourCardsObj bodyObject) {
        int clientID = bodyObject.getClientID();
        String notYourCardsMessage = "Player " + clientID + " has " + bodyObject.getCardsInHand() + " cards in hand.";
        logger.info(ANSI_CYAN + "( MESSAGE ): " + notYourCardsMessage + ANSI_RESET);
    }

    /**
     * handles the clients possibilities in different Phases
     * @param client
     * @param bodyObject
     * @author Benedikt
     */
    public void handleCurrentPlayer(Client client, CurrentPlayerObj bodyObject){
        int clientID = bodyObject.getClientID();
        String currentPlayerMessage = "The Current Player is " + clientID;
        Platform.runLater(() -> client.setCurrentPlayer(bodyObject.getClientID()));
        client.setCurrentPlayerInteger(bodyObject.getClientID());
        logger.info(ANSI_CYAN + "( MESSAGE ): " + currentPlayerMessage + ANSI_RESET);

        // Phase 0
        if(client.getPhase() == 0){
            if (!(clientID == client.getClientID())) {
                Platform.runLater(() -> client.setToDoInfo("Waiting for Player " + bodyObject.getClientID() + " to set starting point."));
            } else {
                Platform.runLater(() -> client.setToDoInfo("Please set your starting point."));
            }
        }

        // Phase 1
        if(client.getPhase() == 1) {
            if (!(clientID == client.getClientID())) {
                Platform.runLater(() -> client.setToDoInfo("Waiting for Player " + bodyObject.getClientID() + " to purchase an upgrade card."));
            } else {
                Platform.runLater(() -> client.setToDoInfo("Please purchase an upgrade card or choose Null if you do not want to purchase one."));
            }
        }

        // Phase 3
        if(client.getPhase() == 3) {
            if (clientID == client.getClientID()) {
                Platform.runLater(() -> client.setToDoInfo("Please press next card."));
                Platform.runLater(() -> client.setNextCardButtonVisible(true));
            } else if (clientID == 0) {
                Platform.runLater(() -> client.setToDoInfo("Board Elements Activate"));
                Platform.runLater(() -> client.setNextCardButtonVisible(false));
            } else  {
                Platform.runLater(() -> client.setToDoInfo("Waiting for Player " + bodyObject.getClientID() + " to press next card."));
                Platform.runLater(() -> client.setNextCardButtonVisible(false));
            }
        }

    }

    /**
     * Updates infos and methods depending on the phase
     * @param client
     * @param bodyObject
     * @author Benedikt
     */
    public void handleActivePhase(Client client, ActivePhaseObj bodyObject) {
        client.setPhase(bodyObject.getPhase());
        Platform.runLater(client :: clearUpdateShopList);
        String activePhaseMessage =
                "The Current Phase is " + bodyObject.getPhase();
        logger.info(ANSI_CYAN + "( MESSAGE ): " + activePhaseMessage + ANSI_RESET);

        if(bodyObject.getPhase()==0){
            Platform.runLater(() -> client.setPhaseInfo("Construction"));
            Platform.runLater(() -> client.setToDoInfo("Pick a starting point"));
        }

        if(bodyObject.getPhase()==1){
            Platform.runLater(() -> client.setPhaseInfo("Upgrade"));
            Platform.runLater(() -> client.setToDoInfo(""));
            client.setAllRegistersEmptyToTrue();
            client.removeAdminPrivilegeFromPlayedUpgradeCardsThisRound();
        }

        if(bodyObject.getPhase()==2){
            Platform.runLater(() -> client.setPhaseInfo("Programming"));
            Platform.runLater(() -> client.setToDoInfo("Fill your registers. If you want to empty a register, choose null"));

        }

        if(bodyObject.getPhase()==3){
            Platform.runLater(() -> client.setPhaseInfo("Activation"));
            Platform.runLater(() -> client.setToDoInfo("If you're the current player, press next card"));
        }

    }
    /**
     * Handles Sendchat objects
     * @param client
     * @param bodyObject
     */
    public void handleSendChat(Client client, SendChatObj bodyObject) {
        String map = bodyObject.getMessage().substring(12);

        if(bodyObject.getMessage().equals("Game starts now.")){
            client.setGameStarted(true);
            Platform.runLater(client :: setScene);
            logger.info(ANSI_CYAN + "( MESSAGE ): Game starts now" + ANSI_RESET);
            Platform.runLater(() -> client.receiveMessage(bodyObject.getMessage()));
        }
        else if(bodyObject.getMessage().equals("Info about current players:")){
            Platform.runLater(() -> client.receiveMessage(bodyObject.getMessage()));
        }
        else if(bodyObject.getMessage().startsWith("FirstReadyPlayersID")){
            String iDString = bodyObject.getMessage().substring(19);
            int iD = Integer.valueOf(iDString);
            logger.info(ANSI_CYAN + "( MESSAGE ): " + "FirstReadyPlayersID: " + iD + ANSI_RESET);
            client.setFirstReadyPlayerIDProperty(iD);
            client.setShowMap();
        }
        else if(bodyObject.getMessage().startsWith("The map is: ") && !map.equals("empty")){
            logger.info(ANSI_CYAN + "( MESSAGE ): " + "Info about map: " + map + ANSI_RESET);
            client.setMapSelected(map);
            Platform.runLater(() -> client.receiveMessage(bodyObject.getMessage()));
        }
        else if(bodyObject.getMessage().equals("If you're the current player, press next card")){
            Platform.runLater(() -> client.setToDoInfo("If you're the current player, press next card"));
        }
        else if(bodyObject.getMessage().startsWith("Current positions are: ")){
            String currentPosition = bodyObject.getMessage().replace("Current positions are: [", "");
            String currentPositionServer = currentPosition.replace("]", "");
            ArrayList<String> currentPositionList = new ArrayList<>(Arrays.asList(currentPositionServer.split(" ,")));
            Platform.runLater(()->client.setCurrentPositions(currentPositionList));
        }
        else if(bodyObject.getMessage().equals("Your registers have been emptied.")){
            Platform.runLater(() -> client.setNewCard("null", 0));
            Platform.runLater(() -> client.setNewCard("null", 1));
            Platform.runLater(() -> client.setNewCard("null", 2));
            Platform.runLater(() -> client.setNewCard("null", 3));
            Platform.runLater(() -> client.setNewCard("null", 4));
        }else if(bodyObject.getMessage().contains("has been shot")) {
            Platform.runLater(() -> client.receiveMessage(bodyObject.getMessage()));
            char clientIDChar = bodyObject.getMessage().charAt(0);
            int clientID = Integer.parseInt(String.valueOf(clientIDChar));
            if (clientID == client.getClientID()) {
                if(!client.isAI()) {
                    Platform.runLater(() -> {
                        ErrorPopUp popup = new ErrorPopUp();
                        popup.openShotInfo("You have been shot. You have received one Spam card.");
                    });
                }
            }
        }else if (bodyObject.getMessage().contains("These players have been hit by wall lasers "))  {
            String[] shotClients = bodyObject.getMessage().split(",");
            for(String shotClient : shotClients){
                if(shotClient.equals(client.getClientID()+"")){
                    if(!client.isAI()) {
                        Platform.runLater(() -> {
                            ErrorPopUp popup = new ErrorPopUp();
                            popup.openShotInfo("You have been hit by a wall laser");
                        });
                    }
                }
            }
        }
    }

    /**
     * server sends private message with name and figure to client
     * @param client
     * @param bodyObject
     */
    public void handlePlayerValues(Client client, PlayerValuesObj bodyObject) {
        String playerValuesMessage =
                "You chose name: " + bodyObject.getName() + " and figure: " + bodyObject.getFigure();
        client.setRobotID(bodyObject.getFigure());
        logger.info(ANSI_CYAN + "( MESSAGE ): "+ playerValuesMessage + ANSI_RESET);

        // Make ready and not ready buttons visible
        Platform.runLater(() -> client.setReadyButtonVisible(true));

        // Display user name
        Platform.runLater(() -> client.setUserName(bodyObject.getName()));

    }

    /**
     * server broadcasts players name and figure
     * @param client
     * @param bodyObject
     */
    public void handlePlayerAdded(Client client, PlayerAddedObj bodyObject){
        String valuesMessage = "Player " + bodyObject.getClientID() + " has chosen name: " + bodyObject.getName() + " and figure: " + bodyObject.getFigure();
        Platform.runLater(() -> client.receiveMessage(valuesMessage));
        logger.info(ANSI_CYAN + "( MESSAGE ): " + valuesMessage + ANSI_RESET);
    }

    /**
     * server send private message of player's status to client
     * @param client
     * @param bodyObject
     */
    public void handleSetStatus(Client client, SetStatusObj bodyObject){
        logger.info(ANSI_CYAN + "( MESSAGE ): "+ bodyObject.getReady() + ANSI_RESET);
    }

    /**
     * server broadcasts player's status
     * @param client
     * @param bodyObject
     */
    public void handlePlayerStatus(Client client, PlayerStatusObj bodyObject){
        if(bodyObject.getReady()){
            String playerStatusMessage =
                    "Player " + bodyObject.getClientID() + " is ready";
            Platform.runLater(() -> client.receiveMessage(playerStatusMessage));
            logger.info(ANSI_CYAN + "( MESSAGE ): " + playerStatusMessage + ANSI_RESET);
        }
        else{
            String playerStatusMessage =
                    "Player " + bodyObject.getClientID() + " is not ready";
            Platform.runLater(() -> client.receiveMessage(playerStatusMessage));
            logger.info(ANSI_CYAN + "( MESSAGE ): " + playerStatusMessage + ANSI_RESET);
        }
    }

    public void handleShuffleCoding(Client client, ShuffleCodingObj bodyObject) {
        String shuffleMessage = "Your Discard Pile has been shuffled.";
        Platform.runLater(() -> client.receiveMessage(shuffleMessage));
        logger.info(ANSI_CYAN + "( MESSAGE ): " + shuffleMessage + ANSI_RESET);
    }

    public void handleMovement(Client client, MovementObj bodyObject) {
        String movementMessage = "Your new Position is: " + bodyObject.getX() + bodyObject.getY();

        Platform.runLater(() -> client.setNewPosition(bodyObject.getX(), bodyObject.getY(), bodyObject.getClientID()));
        logger.info(ANSI_CYAN + "( MESSAGE ): " + movementMessage + ANSI_RESET);
    }

    /**
     * Puts the selected card in register
     * @param client
     * @param bodyObject
     * @author Rea, Arda, Benedikt
     */
    public void handleSelectedCard(Client client, SelectedCardObj bodyObject) {
        if(bodyObject.getCard().equals("Null")) {
            String selectedCardNullMessage = "You have emptied register " + bodyObject.getRegister() + ".";
            logger.info(ANSI_CYAN + "( MESSAGE ): " + selectedCardNullMessage + ANSI_RESET);
        }
        else {
            String selectedCardMessage = "You have put " + bodyObject.getCard() + " in register " + bodyObject.getRegister() + ".";
            logger.info(ANSI_CYAN + "( MESSAGE ): Entered handleSelectedCard" +
                    "()" + selectedCardMessage + ANSI_RESET);
            // Platform.runLater(() -> client.setNewCardInRegister(bodyObject.getCard(), bodyObject.getRegister())); // Label updaten
        }
        Platform.runLater(() -> client.setNewCard(bodyObject.getCard(), bodyObject.getRegister()));
        Platform.runLater(() -> client.setNewCardInRegister(bodyObject.getCard(), bodyObject.getRegister())); // Label updaten
    }

    public void handleCardSelected(Client client, CardSelectedObj bodyObject) {
        String selectedCardMessage = "Player " + bodyObject.getClientID() + " has filled register " + bodyObject.getRegister() + ".";
        logger.info(ANSI_CYAN + "( MESSAGE ): " + selectedCardMessage+ ANSI_RESET);
    }

    public void handleSelectionFinished(Client client, SelectionFinishedObj bodyObject) {
        String selectionFinishedMessage = "Player " + bodyObject.getClientID() + " finished Selection.";
        logger.info(ANSI_CYAN + "( MESSAGE ): " + selectionFinishedMessage + ANSI_RESET);
    }

    public void handleTimerStarted(Client client, TimerStartedObj bodyObject) {
        client.startTimeLine();
        String selectionFinishedMessage = "Timer has started.";
        logger.info(ANSI_CYAN + "( MESSAGE ): " + selectionFinishedMessage + ANSI_RESET);
    }

    public void handleTimerEnded(Client client, TimerEndedObj bodyObject) {
        String timerEndedMessage = "Timer ended. Late players are: " +  Arrays.toString(bodyObject.getClientIDs());
        Platform.runLater(() -> client.receiveMessage(timerEndedMessage));
        logger.info(ANSI_CYAN + timerEndedMessage + ANSI_RESET);
    }

    /**
     * Finds the client's empty registers and fills them with cards from server
     * @param client
     * @param bodyObject
     * @author Benedikt
     */
    public void handleCardsYouGotNow(Client client, CardsYouGotNowObj bodyObject) {
        String cardsYouGotNowMessage = "These cards have been put in your empty registers: " +  Arrays.toString(bodyObject.getCards());
        logger.info(ANSI_CYAN + cardsYouGotNowMessage + ANSI_RESET);

        client.buildNextEmptyRegisterList();
        for(String card : bodyObject.getCards()){
            int nextEmptyRegister = client.findNextEmptyRegister();
            Platform.runLater(() -> client.setNewCardInRegister(card, nextEmptyRegister));
            Platform.runLater(() -> client.setNewCard(card, nextEmptyRegister));
            logger.info(ANSI_CYAN + "Card: " + card + ", nextEmptyRegister: " + nextEmptyRegister + ANSI_RESET);
        }

    }

    public void handlePlayerTurning(Client client, PlayerTurningObj bodyObject) {
        String playerTurningMessage = "Player " + bodyObject.getClientID() +"rotated" + bodyObject.getRotation();
        logger.info(ANSI_CYAN + "( MESSAGE ): " + playerTurningMessage + ANSI_RESET);
    }

    /**
     * Empties all registers and allows player to choose new direction
     * @param client
     * @param bodyObject
     * @author Rea, Ilinur
     */
    public void handleReboot(Client client, RebootObj bodyObject) {
        String rebootMessage = "Player " + bodyObject.getClientID() + " gets rebooted";
        Platform.runLater(() -> client.receiveMessage(rebootMessage));
        logger.info(ANSI_CYAN + "( MESSAGE ): " + rebootMessage + ANSI_RESET);

        if(bodyObject.getClientID() == client.getClientID()) {
            Platform.runLater(() -> client.setNewCard("null", 0));
            Platform.runLater(() -> client.setNewCard("null", 1));
            Platform.runLater(() -> client.setNewCard("null", 2));
            Platform.runLater(() -> client.setNewCard("null", 3));
            Platform.runLater(() -> client.setNewCard("null", 4));
        }

        if(!client.isAI()) {
            Platform.runLater(() -> {
                ChooseDirection chooseDirection = new ChooseDirection();
                chooseDirection.openChooseDirection();
            });
        }

    }

    public void handleRebootDirection(Client client, RebootDirectionObj bodyObject) {
        String rebootDirectionMessage = "Your new direction is " + bodyObject.getDirection();
        logger.info(ANSI_CYAN + "( MESSAGE ): " + rebootDirectionMessage + ANSI_RESET);
    }

    public void handleEnergy(Client client, EnergyObj bodyObject) {
        String energyMessage = "You have " + bodyObject.getCount() + " Energy cubes, you have obtained an energy cube through " +bodyObject.getSource();
        // Display energyCubes
        Platform.runLater(() -> client.setEnergyCubes(bodyObject.getCount()));
        logger.info(ANSI_CYAN + "( MESSAGE ): " + energyMessage + ANSI_RESET);
    }

    public void handleCheckPointReached(Client client, CheckPointReachedObj bodyObject) {
        String checkPointReachedMessage = "You have reached " + bodyObject.getNumber() + " checkpoints";
        Platform.runLater(() -> client.receiveMessage(checkPointReachedMessage));
        // display checkpoints
        Platform.runLater(() -> client.setCheckPoints(bodyObject.getNumber()));
        logger.info(ANSI_CYAN + "( MESSAGE ): " + checkPointReachedMessage + ANSI_RESET);
    }

    /**
     * Informs the Players about the winner
     * @param client
     * @param bodyObject
     * @author Ilinur, Arda
     */
    public void handleGameFinished(Client client, GameFinishedObj bodyObject) {
        String checkPointReachedMessage = "Player " + bodyObject.getClientID() + " wins!";
        Platform.runLater(() -> client.receiveMessage(checkPointReachedMessage));
        String name = client.getName();
        logger.info(ANSI_CYAN + "( MESSAGE ): " + checkPointReachedMessage + ANSI_RESET);
        if(!client.isAI()) {
            if (bodyObject.getClientID() == client.getClientID()) {
                nameWinner = client.getPlayerName();
                robotWinner = client.getChosenRobot();

                Platform.runLater(() -> {
                    GameFinished gameFinished = new GameFinished();
                    gameFinished.openGameFinishedPopUp(nameWinner, robotWinner);
                });
            } else {
                nameL = client.getPlayerName();
                robotL = client.getChosenRobot();
                Platform.runLater(() -> {
                    GameFinishedLoser gameFinishedLoser = new GameFinishedLoser();
                    gameFinishedLoser.openGameFinishedLoserPopUp(nameL, robotL);
                });
            }
        }
    }

    public void handleSelectMap(Client client, SelectMapObj bodyObject) {
        String selectMapMessage = "The available Maps are: " + Arrays.toString(bodyObject.getAvailableMaps()) + ". \n Please choose a Map and send it to server. Example: Map:DizzyHighway.";
        // client.setChooseMapPossible(true);
        logger.info(ANSI_CYAN + "( MESSAGE ): " + selectMapMessage + ANSI_RESET);
    }

    /**
     * Makes starting points invisible once client has chosen a valid starting point
     * @param client
     * @param bodyObject
     */
    public void handleSetStartingPoint(Client client, SetStartingPointObj bodyObject) {
        String setStartingPointMessage = "You have set your starting point to: X=" + bodyObject.getX() + " and Y=" + bodyObject.getY() + ".";
        client.setInvisibleStartPoint1();
        client.setInvisibleStartPoint2();
        client.setInvisibleStartPoint3();
        client.setInvisibleStartPoint4();
        client.setInvisibleStartPoint5();
        client.setInvisibleStartPoint6();
        logger.info(ANSI_CYAN + "( MESSAGE ): " + setStartingPointMessage + ANSI_RESET);

    }

    /**
     * Makes the startingpoint invisible which has been chosen
     * @param client
     * @param bodyObject
     * @author Rea
     */
    public void handleStartingPointTaken(Client client, StartingPointTakenObj bodyObject) {
        String startingPointTakenMessage =
                "Player " + bodyObject.getClientID() + " has set their starting point to: X=" + bodyObject.getX() + " and Y=" + bodyObject.getY() + ".";
        logger.info(ANSI_CYAN + "( MESSAGE ): " + startingPointTakenMessage  + ANSI_RESET);
        if(client.getMapSelected().equals("DeathTrap")){
            if (bodyObject.getX() == 11 && bodyObject.getY() == 1) {
                client.setInvisibleStartPoint1();
            }
            if (bodyObject.getX() == 12 && bodyObject.getY() == 3) {
                client.setInvisibleStartPoint2();
            }
            if (bodyObject.getX() == 11 && bodyObject.getY() == 4) {
                client.setInvisibleStartPoint3();
            }
            if (bodyObject.getX() == 11 && bodyObject.getY() == 5) {
                client.setInvisibleStartPoint4();
            }
            if (bodyObject.getX() == 12 && bodyObject.getY() == 6) {
                client.setInvisibleStartPoint5();
            }
            if (bodyObject.getX() == 11 && bodyObject.getY() == 8) {
                client.setInvisibleStartPoint6();
            }
        }
        else {
            if (bodyObject.getX() == 1 && bodyObject.getY() == 1) {
                client.setInvisibleStartPoint1();
            }
            if (bodyObject.getX() == 0 && bodyObject.getY() == 3) {
                client.setInvisibleStartPoint2();
            }
            if (bodyObject.getX() == 1 && bodyObject.getY() == 4) {
                client.setInvisibleStartPoint3();
            }
            if (bodyObject.getX() == 1 && bodyObject.getY() == 5) {
                client.setInvisibleStartPoint4();
            }
            if (bodyObject.getX() == 0 && bodyObject.getY() == 6) {
                client.setInvisibleStartPoint5();
            }
            if (bodyObject.getX() == 1 && bodyObject.getY() == 8) {
                client.setInvisibleStartPoint6();
            }
        }

        if(bodyObject.getClientID() == client.getClientID()){
            String setStartingPointMessage = "You have set your starting point to: X=" + bodyObject.getX() + " and Y=" + bodyObject.getY() + ".";
            client.setInvisibleStartPoint1();
            client.setInvisibleStartPoint2();
            client.setInvisibleStartPoint3();
            client.setInvisibleStartPoint4();
            client.setInvisibleStartPoint5();
            client.setInvisibleStartPoint6();
            logger.info(ANSI_CYAN + "( MESSAGE ): " + setStartingPointMessage  + ANSI_RESET);
        }

    }


    public void handleCurrentCards(Client client, CurrentCardsObj bodyObject) {
        ActiveCards activeCards = bodyObject.getActiveCards().get(0);
        String cards = activeCards.getCard();
        int playerID = activeCards.getPlayerID();
    }

    public void handleAliveObj(Client client) {
        logger.info(ANSI_CYAN + "( MESSAGE ): Alive Message response" + ANSI_RESET);
    }

    public void handleGameStarted(Client client, GameStartedObj bodyObject) {
        logger.info(ANSI_CYAN + "( MESSAGE ): " + bodyObject.getMap() + ANSI_RESET);
    }

    public void handleConnectionUpdate(Client client, ConnectionUpdateObj bodyObject){
        logger.info(ANSI_CYAN + "( MESSAGE ): Entered handleConnectionUpdate()" + ANSI_RESET);
    }

    public void handleDrawDamage(Client client, DrawDamageObj bodyObject){
        String damageCards = "You have drawn these damage cards: " + Arrays.toString(bodyObject.getCards());
        Platform.runLater(() -> client.receiveMessage(damageCards));
        logger.info(ANSI_CYAN + "( MESSAGE ): " + damageCards + ANSI_RESET);
    }

    /**
     * Opens Pop up to pick damage cards
     * @param client
     * @param bodyObject
     * @author Rea, Arda
     */
    public void handlePickDamage(Client client, PickDamageObj bodyObject){
        Platform.runLater(() -> client.setAvailableDamageCards(bodyObject.getAvailablePiles()));
        String howManyCards = "You need " + bodyObject.getCount() + " damage cards. The available piles are: " + Arrays.toString(bodyObject.getAvailablePiles());
        if(!client.isAI()) {
            Platform.runLater(() -> {
                DamageCardPopUp damageCardPopUp = new DamageCardPopUp();
                damageCardPopUp.openDamageCardPopUp();

            });
        }
        logger.info(ANSI_CYAN + "( MESSAGE ): " + howManyCards + ANSI_RESET);
    }

    public void handleSelectedDamage(Client client, SelectedDamageObj bodyObject){
        String whatCards = "You chose these cards: " + bodyObject.getCards();
        logger.info(ANSI_CYAN + "( MESSAGE ): " + whatCards + ANSI_RESET);
    }

    /**
     * Sets the upgradeshop
     * @param client
     * @param bodyObject
     * @author Benedikt
     */
    public void handleRefillShop(Client client, RefillShopObj bodyObject){
        Platform.runLater(() -> client.setUpgradeShop(bodyObject.getCards()));
        String newUpgradeCards = "The available Upgrade cards are: " + Arrays.toString(bodyObject.getCards());
        Platform.runLater(() -> client.receiveMessage(newUpgradeCards));
        logger.info(ANSI_CYAN + "( MESSAGE ): " + newUpgradeCards + ANSI_RESET);
    }

    public void handleExchangeShop(Client client, ExchangeShopObj bodyObject){
        Platform.runLater(() -> client.setUpgradeShop(bodyObject.getCards()));
        String newUpgradeCards = "The exchanged Upgrade cards are: " + Arrays.toString(bodyObject.getCards());
        Platform.runLater(() -> client.receiveMessage(newUpgradeCards));
        logger.info(ANSI_CYAN + "( MESSAGE ): " + newUpgradeCards + ANSI_RESET);
    }

    /**
     * Puts bought upgrade card in the player's upgrade cards spots in Gui
     * @param client
     * @param bodyObject
     * @author Rea
     */
    public void handleUpgradeBought(Client client, UpgradeBoughtObj bodyObject){
        String clientBoughtUpgradeCard = "Client " + bodyObject.getClientID() + " has bought " + bodyObject.getCard();
        Platform.runLater(() -> client.receiveMessage(clientBoughtUpgradeCard));
        logger.info(ANSI_CYAN + "( MESSAGE ): " + clientBoughtUpgradeCard + ANSI_RESET);

        String upgradeCard = bodyObject.getCard();
        if(bodyObject.getClientID() == client.getClientID()) {
            Platform.runLater(() -> client.setNewUpgradeCard(bodyObject.getCard()));
            if (bodyObject.getCard().equals("MEMORY SWAP") || bodyObject.getCard().equals("SPAM BLOCKER")) {
                client.addInTemporaryUpgradeCardList(bodyObject.getCard());
                if (client.getTemporaryUpgradeCardList().size() == 1) {
                    Platform.runLater(() -> client.setNewUpgradeCardInGap1(upgradeCard));
                } else if (client.getTemporaryUpgradeCardList().size() == 2) {
                    Platform.runLater(() -> client.setNewUpgradeCardInGap2(upgradeCard));
                } else if (client.getTemporaryUpgradeCardList().size() == 3) {
                    Platform.runLater(() -> client.setNewUpgradeCardInGap3(upgradeCard));
                }
            } else if (bodyObject.getCard().equals("REAR LASER") || bodyObject.getCard().equals("ADMIN PRIVILEGE")) {
                client.addInPermanentUpgradeCardList(bodyObject.getCard());
                if (client.getPermanentUpgradeCardList().size() == 1) {
                    Platform.runLater(() -> client.setNewUpgradeCardInGap4(upgradeCard));
                } else if (client.getPermanentUpgradeCardList().size() == 2) {
                    Platform.runLater(() -> client.setNewUpgradeCardInGap5(upgradeCard));
                } else if (client.getPermanentUpgradeCardList().size() == 3) {
                    Platform.runLater(() -> client.setNewUpgradeCardInGap6(upgradeCard));
                }
            }
        }
        logger.info(ANSI_CYAN + "( MESSAGE ): " + "t-upgrade cards:" + client.getTemporaryUpgradeCardList() + ANSI_RESET);
        logger.info(ANSI_CYAN + "( MESSAGE ): " + "p-upgrade cards:" + client.getPermanentUpgradeCardList() + ANSI_RESET);
    }


    public void handleBuyUpgrade(Client client, BuyUpgradeObj bodyObject){
        String buyUpgrade = "You have bought " + bodyObject.getCard();
        logger.info(ANSI_CYAN + "( MESSAGE ): " + buyUpgrade + ANSI_RESET);
    }


    public void handleReplaceCard(Client client, ReplaceCardObj bodyObject){
        String replace = "You have replaced your card with: " + bodyObject.getNewCard();
        Platform.runLater(() -> client.receiveMessage(replace));
        logger.info(ANSI_CYAN + "( MESSAGE ): " + replace + ANSI_RESET);
    }

    public void handleCheckpointMoved(Client client, CheckpointMovedObj bodyObject){
        String s = bodyObject.getCheckpointID() + "_" + bodyObject.getX() + "_" + bodyObject.getY();
        Platform.runLater(() -> client.setCheckPointMoved(s));
        logger.info(ANSI_CYAN + "( MESSAGE ): " + s + ANSI_RESET);

    }
}
