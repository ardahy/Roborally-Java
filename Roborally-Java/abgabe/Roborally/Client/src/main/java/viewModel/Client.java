package viewModel;

import java.util.logging.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import model.AIAssistant;
import model.Model;
import model.TimerForGame;
import model.clientgame.Position;
import org.json.JSONException;
import org.json.JSONObject;
import utilities.Converter;
import utilities.objects.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import view.AdminPrivilege;
import view.ErrorPopUp;

import static main.StartClient.client;

/**
 * Class Client creates client
 * @author Benedikt, Ilinur, Arda, Rea, Aigerim
 */

public class Client extends Thread {

    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private Scanner sc = null;
    private ClientMessageHandler clientMessageHandler = new ClientMessageHandler();
    private static final Logger logger = Logger.getLogger(Client.class.getName());
    boolean append2 = true;
    private FileHandler handler2;
    public static final String ANSI_GREEN = "\u001B[32m";
    private String group = "Desperate Drosseln";
    private boolean isAI = false;
    private static String mapSelected;
    private boolean chooseMapPossible = true;
    private boolean gameStarted = false;
    private int chosenRobot;
    private String playerName;
    private ArrayList<String> playedUpgradeCardsThisRound = new ArrayList<>();
    private int x = 0;
    private int y = 0;
    private boolean setX = false;
    private boolean setY = false;
    private String userName = "";
    private Model model;
    private BooleanProperty showMaps = new SimpleBooleanProperty(false);
    private BooleanProperty showScene = new SimpleBooleanProperty(false);
    private TimerForGame timer;
    private Timeline timeLine;
    private StringProperty timerFace = new SimpleStringProperty("0:30");

    //KI Attribute
    private ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter(); // ObjectWriter converts Java Objects to Json Strings
    private AIAssistant aIAssistant;
    private static boolean aIHasChosenFigure = false;
    private int figure = 1;
    private ArrayList<Integer> iDsOfAIs = new ArrayList<>();
    private boolean aIHasSetStartingPoint = false;
    private static ArrayList<Position> nonTakenStartingPoints = new ArrayList<>();
    private boolean firstAIToSetStartingPoint = true;

    /**
     * client constructor
     */
    public Client() {
        model = new Model();
        aIAssistant = new AIAssistant();
        try {
            handler2 = new FileHandler("./logs/LoggingClient.log",
                    append2);
            logger.addHandler(handler2);
            SimpleFormatter formatter = new SimpleFormatter();
            handler2.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Buffered Reader reads input message, afterwards the message is deserialized
     * Printwriter for output messages
     */
    @Override
    public void run() {
        try {
            socket = new Socket("localhost", 7000);
            // Für Test am Uni Server
            // socket = new Socket("sep21.dbs.ifi.lmu.de", 52019);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            isAI = false;
            JSONMessage jsonMessageHelloServer = new JSONMessage("HelloServer", new HelloServerObj("Desperate Drosseln", isAI, "Version 2.1"));
            output.println(Converter.serializeJSON(jsonMessageHelloServer));
            output.flush();
            String message;
            while (true) {
                if (isAI) {
                    playAI();
                } else {
                    message = input.readLine();
                    JSONMessage jsonMessage = Converter.deserializeJSON(message);
                    logger.info(ANSI_GREEN + "JSONDecoder in Client done: " + message + jsonMessage + ANSI_GREEN);
                    Class<?> reflection = (Class<?>) Class.forName("utilities.objects." + jsonMessage.getMessageType() + "Obj");
                    Object messageBodyObject = reflection.cast(jsonMessage.getMessageBody());
                    ClientObjectHandler actionMessage = (ClientObjectHandler) jsonMessage.getMessageBody();
                    actionMessage.action(this, messageBodyObject, clientMessageHandler);
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * Used to send Messages by the client
     * the entered String messages are sent as json Messages
     *
     * @param jMessage The message the Client has entered
     */
    public void sendMessage(String jMessage) {
        if (jMessage.contains("bye")) {
            Platform.exit();
        }
        JSONMessage jsonMessage = new JSONMessage("SendChat", new SendChatObj(jMessage, -1));
        output.println(Converter.serializeJSON(jsonMessage));
        output.flush();
    }

    /**
     * write private message
     *
     * @param message
     * @param receiverID
     */
    public void sendPrivateMessage(String message, int receiverID) {
        if (message.contains("bye")) {
            Platform.exit();
        }
        JSONMessage jsonMessage = new JSONMessage("SendChat", new SendChatObj(message, receiverID));
        output.println(Converter.serializeJSON(jsonMessage));
        output.flush();
    }

    /**
     * This method is not needed in the game but helps when there are problems. Certain things can be send to server via chat and "Server" button
     * @param messageToServer
     */
    public void messageServer(String messageToServer) {

        Platform.runLater(() -> {
            ErrorPopUp popup = new ErrorPopUp();
            popup.openError("This Button is only for problems during the game.");
        });

        char registerChar = messageToServer.charAt(0);
        int register = 6; // choose a register out of bound
        if(Character.isDigit(registerChar)) {
            register = Integer.parseInt(String.valueOf(registerChar));
        }
        String cardName = messageToServer.substring(1);

        if(Character.isDigit(registerChar) && register<5 && register>=0 && getPhase() == 2) {
            JSONMessage jsonMessageSelectedCard = new JSONMessage("SelectedCard", new SelectedCardObj(cardName, register));
            output.println(Converter.serializeJSON(jsonMessageSelectedCard));
            output.flush();
        }
        else if(messageToServer.startsWith("Map:") && getIsChooseMapPossible()) {
            chooseMapPossible = false;
            String map = messageToServer.substring(4);
            JSONMessage jsonMessageMapSelected = new JSONMessage("MapSelected", new MapSelectedObj(map));
            output.println(Converter.serializeJSON(jsonMessageMapSelected));
            output.flush();
        }
        else if(messageToServer.startsWith("x") && getPhase() == 0){
            messageToServer = messageToServer.substring(1); // remove x
            x = Integer.parseInt(messageToServer);
            setX = true;
            if(setY) {
                JSONMessage jsonMessageSetStartingPoint = new JSONMessage("SetStartingPoint", new SetStartingPointObj(x, y));
                output.println(Converter.serializeJSON(jsonMessageSetStartingPoint));
                output.flush();
            }
        }
        else if(messageToServer.startsWith("y") && getPhase() == 0){
            messageToServer = messageToServer.substring(1); // remove y
            y = Integer.parseInt(messageToServer);
            setY = true;
            if(setX) {
                JSONMessage jsonMessageSetStartingPoint = new JSONMessage("SetStartingPoint", new SetStartingPointObj(x, y));
                output.println(Converter.serializeJSON(jsonMessageSetStartingPoint));
                output.flush();
            }
        }
        else {
            String error = "Error";
            JSONMessage jsonMessageError = new JSONMessage("Error", new ErrorObj(error));
            output.println(Converter.serializeJSON(jsonMessageError));
            output.flush();
        }
    }

    /**
     * set new Scene for KI
     */
    public void setScene() {
        showScene.setValue(true);
    }

    /**
     * get new status scene for KI
     *
     * @return
     */
    public BooleanProperty getShowScene() {
        return showScene;
    }

    public void receiveMessage(String message) {
        model.receiveMessage(message);
    }

    public StringProperty getMessageHistory() {
        return model.getMessageHistory();
    }

    /**
     * Puts a card in a register
     * @param card
     * @param register
     */
    public void setNewCardInRegister(String card, int register) {
        model.setNewCardInRegister(card, register);
    }

    /**
     * Next empty register for Cards You got now
     */
    public void buildNextEmptyRegisterList() {
        model.buildNextEmptyRegisterList();
    }

    /**
     * After a round all registers booleans for if they are emty are set to true
     */
    public void setAllRegistersEmptyToTrue() {
        model.setAllRegistersEmptyToTrue();
    }

    /**
     * Finds next empty register
     * @return the next empoty register
     */
    public int findNextEmptyRegister() {
        return model.findNextEmptyRegister();
    }

    /**
     * returns ClientID
     */
    public int getClientID() {
        return model.getClientID();
    }

    /**
     * sets client ID
     * @param clientID players ID
     */
    public void setClientID(int clientID) {
        model.setClientID(clientID);
    }

    /**
     * Used for client to send server a card that the client played
     * @param card the card that is played
     */
    public void playCard(String card) {
        JSONMessage jsonMessagePlayCard = new JSONMessage("PlayCard", new PlayCardObj(card));
        output.println(Converter.serializeJSON(jsonMessagePlayCard));
        output.flush();
    }

    /**
     * creates new PlayerValues Object
     *
     * @param name
     * @param figure
     */
    public void chooseValues(String name, int figure) {
        JSONMessage jsonMessageValues = new JSONMessage("PlayerValues", new PlayerValuesObj(name, figure));
        output.println(Converter.serializeJSON(jsonMessageValues));
        output.flush();
    }

    /**
     * creates new SetStatus Object
     *
     * @param status
     */
    public void setStatus(boolean status) {
        JSONMessage jsonMessageStatus = new JSONMessage("SetStatus", new SetStatusObj(status));
        output.println(Converter.serializeJSON(jsonMessageStatus));
        output.flush();
    }

    /**
     * Used for client to send server a card that the client played
     * @param card the card that is played
     */
    public void sendServerPlayedCard(String card) {
        JSONMessage jsonMessagePlayCard = new JSONMessage("PlayCard", new PlayCardObj(card));
        output.println(Converter.serializeJSON(jsonMessagePlayCard));
        output.flush();
    }

    /**
     * @param direction, the direction the player has chosen, after being rebooted
     */
    public void chosenDirectionReboot(String direction) {
        JSONMessage jsonMessageDirection = new JSONMessage("RebootDirection", new RebootDirectionObj(direction));
        output.println(Converter.serializeJSON(jsonMessageDirection));
        output.flush();
    }

    /**
     * sets the current Phase
     * @param newPhase the new phase
     */
    public void setPhase(int newPhase) {
        model.setPhase(newPhase);
    }

    /**
     * @return the new phase
     */
    public int getPhase() {
        return model.getPhase();
    }

    /**
     * Checks if you can choose a map at the moment
     * @return
     */
    public boolean getIsChooseMapPossible() {
        return chooseMapPossible;
    }

    /**
     * @returns true when game has started
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Set gameStarted when the game has started
     * @param gameStarted
     */
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    /**
     * Set a clients starting point and send it to server
     * @param x x Coordinate of Starting point
     * @param y y Coordinate of Starting point
     * @author Benedikt
     */
    public void setStartPoint(int x, int y) {
        if (getCurrentPlayerIDInteger() == getClientID()) {
            JSONMessage jsonMessageSetStartingPoint = new JSONMessage("SetStartingPoint", new SetStartingPointObj(x, y));
            output.println(Converter.serializeJSON(jsonMessageSetStartingPoint));
            output.flush();
            this.x = x;
            this.y = y;
        } else {
            Platform.runLater(() -> {
                ErrorPopUp popup = new ErrorPopUp();
                popup.openError("It is not your turn to set your starting point");
            });
        }
    }

    /**
     * Send the selected map to server
     * @param map the selected map
     */
    public void mapSelected(String map) {
        JSONMessage jsonMessageMapSelected = new JSONMessage("MapSelected", new MapSelectedObj(map));
        output.println(Converter.serializeJSON(jsonMessageMapSelected));
        output.flush();
    }

    /**
     * @returns the map which was selected
     */
    public String getMapSelected() {
        return mapSelected;

    }

    /**
     * sets the map
     * @param mapSelected the map which was selected
     */
    public void setMapSelected(String mapSelected) {
        Client.mapSelected = mapSelected;
    }

    /**
     * sets the first player that presses ready
     * @param ID
     */
    public void setFirstReadyPlayerIDProperty(int ID) {
        model.setFirstReadyPlayerIDProperty(ID);
    }


    public int getFirstReadyPlayerID() {
        return model.getFirstReadyPlayerID();
    }

    /**
     * get card's list from model for GUI
     *
     * @return card's list as ListProperty
     */
    public ListProperty<String> getYourCardsInHandProperty() {
        return model.getYourCardsInHandProperty();
    }

    public ObservableList<String> getYourCardsInHand() {
        return model.getYourCardsInHand();
    }

    public ListProperty<String> getUpgradeShopProperty() {
        return model.getUpgradeShopProperty();
    }

    public ListProperty<String> getMyUpgradeCardsProperty() {
        return model.getMyUpgradeCardsProperty();
    }

    public ListProperty<String> getAvailableDamageCardsProperty() {
        return model.getAvailableDamageCardsProperty();
    }

    public ObservableList<String> getAvailableDamageCards() {
        return model.getAvailableDamageCards();
    }

    public void setAvailableDamageCards(String[] availableDamageCards) {
        model.setAvailableDamageCards(availableDamageCards);
    }

    /**
     * set card's list in model, that player gets from server
     *
     * @param yourCardsInHand as Array
     */
    public void setYourCardsInHand(String[] yourCardsInHand) {
        model.setYourCardsInHand(yourCardsInHand);
    }

    /**
     * clear update shop list
     *
     * @author Aigerim
     */
    public void clearUpdateShopList() {
        model.clearUpdateShop();
    }

    public void setUpgradeShop(String[] upgradeCards) {
        model.setUpgradeShop(upgradeCards);
    }

    /**
     * get maps from model class to choose map for game
     *
     * @return
     */
    public ListProperty<String> getMapsList() {
        return model.getMapsList();
    }

    public BooleanProperty showMap() {
        return showMaps;
    }

    public void setShowMap() {
        showMaps.setValue(true);
    }

    public void sendCardInRegisterToServer(int register, String cardName) {
        JSONMessage jsonMessageSelectedCard = new JSONMessage("SelectedCard", new SelectedCardObj(cardName, register));
        output.println(Converter.serializeJSON(jsonMessageSelectedCard));
        output.flush();
    }

    /**
     * Send server to play next card
     * @author Benedikt
     */
    public void playNextCard() {
        if (getCurrentPlayerIDInteger() == getClientID()) {
            int register = client.getRegister();
            if(!isAI()) {
                Platform.runLater(() -> client.setNewCard("null", register));
            }
            client.increaseRegister();
            String playNextCard = "Play next Card.";
            JSONMessage jsonMessagePlayNextCard = new JSONMessage("SendChat", new SendChatObj(playNextCard, -1));
            output.println(Converter.serializeJSON(jsonMessagePlayNextCard));
            output.flush();
        }
    }

    /**
     * sets currentPlayer for Gui
     * @param playerID
     */
    public void setCurrentPlayer(int playerID) {
        model.setCurrentPlayer(playerID);
    }

    /**
     * Sets the current Player as Int
     * @param playerID
     */
    public void setCurrentPlayerInteger(int playerID) {
        model.setCurrentPlayerIDInteger(playerID);
    }

    public int getCurrentPlayerIDInteger() {
        return model.getCurrentPlayerIDInteger();
    }

    public IntegerProperty getCurrentPlayer() {
        return model.getCurrentPlayerID();
    }

    public void setNextCardButtonVisible(boolean nextCardButtonVisible) {
        model.setNextCardButtonVisible(nextCardButtonVisible);
    }

    public BooleanProperty getNextCardButtonVisible() {
        return model.getNextCardButtonVisible();
    }

    public void setReadyButtonVisible(boolean readyButtonVisible) {
        model.setReadyButtonVisible(readyButtonVisible);
    }

    public BooleanProperty getReadyButtonVisible() {
        return model.getReadyButtonVisible();
    }

    public BooleanProperty getNotReadyButtonVisible() {
        return model.getNotReadyButtonVisible();
    }

    public BooleanProperty getReadyTextVisible() {
        return model.getReadyTextVisible();
    }

    public void setNewPosition(int x, int y, int clientID) {
        model.setNewPosition(x, y, clientID);
    }

    public StringProperty getPosition() {
        return model.getPosition();
    }

    public void setTimer(String timer) {
        Platform.runLater(() -> timerFace.setValue(timer));
    }

    public void startTimeLine() {
        timer = new TimerForGame("0:30");
        timeLine = new Timeline(
                new KeyFrame(Duration.seconds(1),

                        e -> {
                            timer.oneSecondPassed();
                            setTimer(timer.getCurrentTime());

                            if (timer.getCurrentTime().equals("0:0")) {
                                timeLine.stop();
                                setTimer("End");
                            }

                        }));

        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    public StringProperty getCurrentTimers() {

        return timerFace;
    }

    public String getRobotColor() {
        return model.getRobotColor();
    }

    public void setRobotID(int id) {
        model.setRobotID(id);
    }

    public void setCurrentPositions(ArrayList<String> currentPositions) {
        model.setCurrentPositions(currentPositions);
    }

    public ArrayList<String> getCurrentPositions() {
        return model.getCurrentPositions();
    }

    public ListProperty<String> getCurrentPositionsProperty() {
        return model.getCurrentPositionsProperty();
    }


    public StringProperty getCurrentPositionsAsString() {
        return model.currentPositionsAsString();
    }

    public void setNewCard(String card, int register) {
        model.setNewCard(card, register);
    }

    public String getCardName() {
        return model.getCardName();
    }

    public void setNewUpgradeCard(String card) {
        model.setNewUpgradeCard(card);
    }

    public String getUpgradeCardName() {
        return model.getUpgradeCardName();
    }

    public StringProperty getUpgradeCardAsString() {
        return model.upgradeCardAsString();
    }

    public void setNewUpgradeCardInGap1(String card) {
        model.setNewUpgradeCardInGap1(card);
    }

    public StringProperty getUpgradeCardInGap1AsString() {
        return model.upgradeCardInGap1AsString();
    }

    public void setNewUpgradeCardInGap2(String card) {
        model.setNewUpgradeCardInGap2(card);
    }

    public StringProperty getUpgradeCardInGap2AsString() {
        return model.upgradeCardInGap2AsString();
    }

    public void setNewUpgradeCardInGap3(String card) {
        model.setNewUpgradeCardInGap3(card);
    }

    public StringProperty getUpgradeCardInGap3AsString() {
        return model.upgradeCardInGap3AsString();
    }

    public void setNewUpgradeCardInGap4(String card) {
        model.setNewUpgradeCardInGap4(card);
    }

    public StringProperty getUpgradeCardInGap4AsString() {
        return model.upgradeCardInGap4AsString();
    }

    public void setNewUpgradeCardInGap5(String card) {
        model.setNewUpgradeCardInGap5(card);
    }

    public StringProperty getUpgradeCardInGap5AsString() {
        return model.upgradeCardInGap5AsString();
    }

    public void setNewUpgradeCardInGap6(String card) {
        model.setNewUpgradeCardInGap6(card);
    }

    public StringProperty getUpgradeCardInGap6AsString() {
        return model.upgradeCardInGap6AsString();
    }

    public StringProperty getCurrentCardsInReg0asString() {
        return model.currentCardsInRegister0asString();
    }

    public StringProperty getCurrentCardsInReg1asString() {
        return model.currentCardsInRegister1asString();
    }

    public StringProperty getCurrentCardsInReg2asString() {
        return model.currentCardsInRegister2asString();
    }

    public StringProperty getCurrentCardsInReg3asString() {
        return model.currentCardsInRegister3asString();
    }

    public StringProperty getCurrentCardsInReg4asString() {
        return model.currentCardsInRegister4asString();
    }


    public BooleanProperty invisibleStartPoint1() {
        return model.invisibleStartPoint1();
    }

    public void setInvisibleStartPoint1() {
        model.setInvisibleStartPoint1();
    }

    public BooleanProperty invisibleStartPoint2() {
        return model.invisibleStartPoint2();
    }

    public void setInvisibleStartPoint2() {
        model.setInvisibleStartPoint2();
    }

    public BooleanProperty invisibleStartPoint3() {
        return model.invisibleStartPoint3();
    }

    public void setInvisibleStartPoint3() {
        model.setInvisibleStartPoint3();
    }

    public BooleanProperty invisibleStartPoint4() {
        return model.invisibleStartPoint4();
    }

    public void setInvisibleStartPoint4() {
        model.setInvisibleStartPoint4();
    }

    public BooleanProperty invisibleStartPoint5() {
        return model.invisibleStartPoint5();
    }

    public void setInvisibleStartPoint5() {
        model.setInvisibleStartPoint5();
    }

    public BooleanProperty invisibleStartPoint6() {
        return model.invisibleStartPoint6();
    }

    public void setInvisibleStartPoint6() {
        model.setInvisibleStartPoint6();
    }

    public StringProperty getPhaseInfoProperty() {
        return model.getPhaseInfoProperty();
    }

    public void setPhaseInfo(String phaseInfo) {
        model.setPhaseInfo(phaseInfo);
    }

    public StringProperty getToDoInfoProperty() {
        return model.getToDoInfoProperty();
    }

    public void setToDoInfo(String toDoInfo) {
        model.setToDoInfo(toDoInfo);
    }

    /**
     * @return userName property from model
     */
    public StringProperty getUserNameProperty() {
        return model.getUserNameProperty();
    }

    /**
     * @param userName set userName in model
     */

    public void setUserName(String userName) {
        model.setUserName(userName);
    }

    /**
     * @return energyCube property from model
     */
    public IntegerProperty getEnergyCubesProperty() {
        return model.getEnergyCubesProperty();
    }

    /**
     * @param energyCubes set energyCubes in model
     */
    public void setEnergyCubes(int energyCubes) {
        model.setEnergyCubes(energyCubes);
    }

    /**
     * @return checkpoint property from model
     */

    public IntegerProperty getCheckPointsProperty() {
        return model.getCheckPointsProperty();
    }

    /**
     * @param checkPoints set checkPoints in model
     */

    public void setCheckPoints(int checkPoints) {
        model.setCheckPoints(checkPoints);
    }

    public BooleanProperty getStartButtonProperty() {
        return model.getStartButtonProperty();
    }

    public void setStartButton() {
        model.setStartButton();
    }

    public int getChosenRobot() {
        return chosenRobot;
    }

    public void setChosenRobot(int chosenRobot) {
        this.chosenRobot = chosenRobot;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }


    /**
     * Checks if buying an upgrade card is valid and then sends it to server
     * @param card
     * @author Benedikt
     */
    public void buyUpgradeCard(String card) {

        // int currentPlayer = getCurrentPlayer().getValue();
        int currentPlayer = getCurrentPlayerIDInteger();
        if (getPhase() == 1) {
            if (currentPlayer == getClientID()) {
                if (canAfford(getPriceOfUpgradeCard(card))) {
                    if (!card.equals("Null")) {
                        JSONMessage jsonMessageBuyUpgrade = new JSONMessage("BuyUpgrade", new BuyUpgradeObj(true, card));
                        output.println(Converter.serializeJSON(jsonMessageBuyUpgrade));
                        output.flush();
                    } else {
                        JSONMessage jsonMessageDoNotBuyUpgrade = new JSONMessage("BuyUpgrade", new BuyUpgradeObj(false, card));
                        output.println(Converter.serializeJSON(jsonMessageDoNotBuyUpgrade));
                        output.flush();
                    }
                } else {
                    String cannotAfford = "You cannot afford " + card;
                    Platform.runLater(() -> {
                        ErrorPopUp popup = new ErrorPopUp();
                        popup.openError(cannotAfford);

                    });
                }
            } else {
                String notYourTurn = "It is player " + currentPlayer + "'s turn.";
                Platform.runLater(() -> {
                    ErrorPopUp popup = new ErrorPopUp();
                    popup.openError(notYourTurn);

                });
            }
        } else {
            String notUpgradePhase = "It is not Upgrade Phase";
            Platform.runLater(() -> {
                ErrorPopUp popup = new ErrorPopUp();
                popup.openError(notUpgradePhase);

            });
        }

    }

    /**
     * Plays the upgrade card, depending on the card type
     * @param card
     */
    public void playUpgradeCard(String card) {
        switch (card) {
            case "ADMIN PRIVILEGE":
                if (!playedUpgradeCardsThisRound.contains("ADMIN PRIVILEGE")) {
                    playedUpgradeCardsThisRound.add("ADMIN PRIVILEGE");
                    Platform.runLater(() -> {
                        AdminPrivilege adminPrivilege = new AdminPrivilege();
                        adminPrivilege.openAdminPrivilegePopUp();
                    });
                } else {
                    Platform.runLater(() -> {
                        ErrorPopUp popup = new ErrorPopUp();
                        popup.openError("You have already played admin privilege this round.");
                    });
                }
                break;

            case "SPAM BLOCKER":
                playCard("SPAM BLOCKER");
                model.removeUpgradeCard(card);
                break;

            case "REAR LASER":
                if (!playedUpgradeCardsThisRound.contains("REAR LASER")) {
                    playedUpgradeCardsThisRound.add("REAR LASER");
                    playCard("REAR LASER");
                } else {
                    Platform.runLater(() -> {
                        ErrorPopUp popup = new ErrorPopUp();
                        popup.openError("You have already activated Rear Laser. The effect is permanent.");
                    });
                }
                break;

            case "MEMORY SWAP":
                playCard("MEMORY SWAP");
                model.removeUpgradeCard(card);
                break;
        }
    }

    public void removeAdminPrivilegeFromPlayedUpgradeCardsThisRound() {
        playedUpgradeCardsThisRound.remove("ADMIN PRIVILEGE");
    }

    /**
     * Gives each upgrade card's price
     * @param upgradeCard
     * @return
     */
    public int getPriceOfUpgradeCard(String upgradeCard) {

        switch (upgradeCard) {
            case "ADMIN PRIVILEGE":
            case "SPAM BLOCKER":
                return 3;
            case "REAR LASER":
                return 2;
            case "MEMORY SWAP":
                return 1;
            default:
                return 0;
        }
    }

    /**
     * Checks whether the player can afford the card, if yes the player pays the price
     * @param energyCubes
     * @return
     * @author Benedikt
     */
    public boolean canAfford(int energyCubes) {
        int newAmountOfEnergyCubes = model.getEnergyCubesProperty().getValue();
        if ((newAmountOfEnergyCubes - energyCubes) >= 0) {
            newAmountOfEnergyCubes = newAmountOfEnergyCubes - energyCubes;
            model.setEnergyCubes(newAmountOfEnergyCubes);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return rename message from Model
     */
    public StringProperty getRenameRobot() {
        return model.getRenameRobot();
    }

    /**
     * @param renameRobot set text in Model
     */
    public void setRenameRobot(String renameRobot) {
        model.setRenameRobot(renameRobot);
    }

    //permanent upgrade cards
    public ArrayList<String> getTemporaryUpgradeCardList() {
        return model.getTemporaryUpgradeCardList();
    }

    public void addInTemporaryUpgradeCardList(String card) {
        model.addInTemporaryUpgradeCardList(card);
    }

    public void removeFromTemporaryUpgradeCardList(String card) {
        model.removeFromTemporaryUpgradeCardList(card);
    }

    public void setTemporaryUpgradeCardList(ArrayList<String> temporaryUpgradeCardList) {
        model.setTemporaryUpgradeCardList(temporaryUpgradeCardList);
    }

    //permanent upgrade cards
    public ArrayList<String> getPermanentUpgradeCardList() {
        return model.getPermanentUpgradeCardList();
    }

    public void addInPermanentUpgradeCardList(String card) {
        model.addInPermanentUpgradeCardList(card);
    }

    public void setPermanentUpgradeCardList(ArrayList<String> permanentUpgradeCardList) {
        model.setPermanentUpgradeCardList(permanentUpgradeCardList);
    }

    /**
     * sends server which register the player wants admin privilege for
     * @param register
     */
    public void adminPrivilegeRegister(int register) {
        JSONMessage jsonMessageRegister = new JSONMessage("ChooseRegister", new ChooseRegisterObj(register));
        output.println(Converter.serializeJSON(jsonMessageRegister));
        output.flush();
    }


    public void returnCardsMemorySwap(String[] cards) {
        JSONMessage jsonMessageReturnCards = new JSONMessage("ReturnCards", new ReturnCardsObj(cards));
        output.println(Converter.serializeJSON(jsonMessageReturnCards));
        output.flush();
    }

    public void returnPickDamage(String[] cards) {
        JSONMessage jsonMessageReturnCards = new JSONMessage("SelectedDamage", new SelectedDamageObj(cards));
        output.println(Converter.serializeJSON(jsonMessageReturnCards));
        output.flush();
    }

    /**
     * @return
     */
    public int getRegister() {
        return model.getRegister();
    }

    public void increaseRegister() {
        model.increaseRegister();
    }

    public StringProperty getCheckPointMoved() {
        return model.getCheckPointMoved();
    }

    public void setCheckPointMoved(String moved) {
        model.setCheckPointMoved(moved);
    }


    /**
     * @returns whether client is AI
     */
    public boolean isAI() {
        return isAI;
    }

    /**
     * AI reacts to all incoming messages rationally
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @author Benedikt
     */
    public void playAI() throws IOException, ClassNotFoundException, JSONException {

        // AI reads every message incoming from server
        String message = input.readLine();
        JSONMessage jsonMessage = Converter.deserializeJSON(message);
        logger.info(ANSI_GREEN + "JSONDecoder in Client done: " + message + jsonMessage + ANSI_GREEN);
        Class<?> reflection = (Class<?>) Class.forName("utilities.objects." + jsonMessage.getMessageType() + "Obj");
        Object messageBodyObject = reflection.cast(jsonMessage.getMessageBody());
        ClientObjectHandler actionMessage = (ClientObjectHandler) jsonMessage.getMessageBody();
        actionMessage.action(this, messageBodyObject, clientMessageHandler);

        // Create new JSONObject to get the JSON Message's body
        JSONObject jsonObject = new JSONObject();
        if (!(jsonMessage.getMessageType().equals("TimerStarted")) && !(jsonMessage.getMessageType().equals("Alive"))) {
            // getMessageBody of jsonMessage
            Object object = jsonMessage.getMessageBody();
            // Convert Java Object to a JSON string with ObjectWriter
            String jsonMessageString = ow.writeValueAsString(object);
            // Convert jsonString to JSONObject
            jsonObject = new JSONObject(jsonMessageString);
        }

        // If AI has not chosen the figure and message type is not HelloClient then AI chooses a figure until it finds a non-taken figure
        if (!aIHasChosenFigure && !jsonMessage.getMessageType().equals("HelloClient")) {
            // If message type is PlayerValues then AI has chosen a figure and aIHasChosenFigure is set to true
            if (jsonMessage.getMessageType().equals("PlayerValues")) {
                aIHasChosenFigure = true;
            }

            // As long as the AI has not set its figure, the AI sends the integer of the next figure to server until a non-taken figure is set
            if (!aIHasChosenFigure) {
                JSONMessage jsonMessageValues = new JSONMessage("PlayerValues", new PlayerValuesObj("KI", figure));
                output.println(Converter.serializeJSON(jsonMessageValues));
                output.flush();
            }
            // If the AI has set its figure, the AI is ready
            else {
                JSONMessage jsonMessageStatus = new JSONMessage("SetStatus", new SetStatusObj(true));
                output.println(Converter.serializeJSON(jsonMessageStatus));
                output.flush();
            }
            figure++; // increase figure for next try to find non-taken figure
        }

        // The AI can set a random map
        if (jsonMessage.getMessageType().equals("SelectMap")) {
            // If AI is first Ready Player, AI selects a random map
            Random r = new Random();
            int randomItem = r.nextInt(getMapsList().size());
            String map = getMapsList().get(randomItem);
            JSONMessage jsonMessageMapSelected = new JSONMessage("MapSelected", new MapSelectedObj(map));
            output.println(Converter.serializeJSON(jsonMessageMapSelected));
            output.flush();
        }

        // Set map selected
        if (jsonMessage.getMessageType().equals("MapSelected")) {
            mapSelected = jsonObject.getString("map");
        }

        // If AI is the currentPlayer she acts accordingly
        if (jsonMessage.getMessageType().equals("CurrentPlayer")) {
            if (jsonObject.getInt("clientID") == getClientID()) {

                // In phase 0 she sets starting point
                if (getPhase() == 0) {
                    // AI takes a few moments to set starting point
                    TimerTask tasknew = new TimerTask() {
                        public void run() {
                            if (firstAIToSetStartingPoint) {
                                firstAIToSetStartingPoint = false;
                                aIAssistant.setMap(mapSelected);
                                if (mapSelected.equals("DizzyHighway")) {
                                    aIAssistant.buildPreferredStartingPointsDizzy();
                                } else {
                                    aIAssistant.buildStartingPoints();
                                }
                            }

                            // Get the coordinates of a nonTakenStartingPoint and send it to server
                            nonTakenStartingPoints.clear();
                            nonTakenStartingPoints.addAll(aIAssistant.getNonTakenStartingPoints());
                            int x = nonTakenStartingPoints.get(0).getX();
                            int y = nonTakenStartingPoints.get(0).getY();
                            JSONMessage jsonMessageSetStartingPoint = new JSONMessage("SetStartingPoint", new SetStartingPointObj(x, y));
                            output.println(Converter.serializeJSON(jsonMessageSetStartingPoint));
                            output.flush();
                        }
                    };
                    Timer timer = new Timer();
                    long delay = 2000;
                    timer.schedule(tasknew, delay);
                }

                // In Phase 1, AI does not buy an Upgrade card
                if (getPhase() == 1) {
                    buyUpgradeCard("Null");
                }

                // In Phase 3, AI waits a few moments to press next player
                if (getPhase() == 3) {

                    TimerTask tasknew = new TimerTask() {
                        public void run() {
                            playNextCard();
                        }
                    };
                    Timer timer = new Timer();
                    long delay = 2000;
                    timer.schedule(tasknew, delay);
                }
            }
        }

        // EveryTime a client sets an ID the AI checks whether she has already set a starting point. This makes sure that under every circumstances the AI will set a starting point.
        if (jsonMessage.getMessageType().equals("StartingPointTaken")) {
            // get the coordinated of the starting point another player has set and remove it from nonTakenStartingPoints
            int x = jsonObject.getInt("x");
            int y = jsonObject.getInt("y");
            // get Integer for clientID from jsonObject
            int clientID = jsonObject.getInt("clientID");
            // If the clientID velongs to the AI, the AI saves starting point
            if (clientID == getClientID()) {
                aIHasSetStartingPoint = true;
                Position myStartingPoint = new Position(x, y);
                aIAssistant.setMyStartingPoint(myStartingPoint);
                // If not, the AI saves the map and builds the starting points
            } else {
                if (aIAssistant.getMap().equals("empty")) {
                    aIAssistant.setMap(mapSelected);
                    if (mapSelected.equals("DizzyHighway")) {
                        aIAssistant.buildPreferredStartingPointsDizzy();
                    } else {
                        aIAssistant.buildStartingPoints();
                    }
                }
            }
            // AI removes the starting point from the available starting points
            aIAssistant.removeTakenStartingPoint(x, y);
        }

        // If AI gets cards handed out she chooses the first 5 cards and sends them to server. (For dizzyHighway she tries a certain path ;) )
        if (jsonMessage.getMessageType().equals("YourCards")) {
            if (jsonObject.getJSONArray("cardsInHand").length() == 9) {
                // get cards in hand
                String[] yourCardsInHandArray = aIAssistant.convertJsonArrayToStringArray(jsonObject.getJSONArray("cardsInHand"));
                ArrayList<String> yourCardsInHand = new ArrayList<>(Arrays.asList(yourCardsInHandArray));

                // If the AI gets lucky and has the right cards she walks a certain path to the checkpoint in dizzyHighway
                if (!aIAssistant.playDizzyHighwaySmartly(yourCardsInHandArray).isEmpty()) {
                    ArrayList<String> cardsForRegisters = aIAssistant.playDizzyHighwaySmartly(yourCardsInHandArray);
                    int register = 0;
                    for (String card : cardsForRegisters) {
                        JSONMessage jsonMessageSelectedCard = new JSONMessage("SelectedCard", new SelectedCardObj(card, register));
                        output.println(Converter.serializeJSON(jsonMessageSelectedCard));
                        output.flush();
                        register++;
                    }
                }

                // Choose random cards
                else {
                    // fill registers
                    for (int i = 0; i < 5; i++) {
                        String card = yourCardsInHand.get(0);
                        // Again cannot be put in register 0
                        if (card.equals("Again") && i == 0) {
                            card = yourCardsInHand.get(1);
                        }
                        // There are 2 again cards, so we have to check twice
                        if (card.equals("Again") && i == 0) {
                            card = yourCardsInHand.get(2);
                        }
                        yourCardsInHand.remove(card);

                        JSONMessage jsonMessageSelectedCard = new JSONMessage("SelectedCard", new SelectedCardObj(card, i));
                        output.println(Converter.serializeJSON(jsonMessageSelectedCard));
                        output.flush();
                    }
                }
            }
        }

        // AI always gets rebooted facing upwards
        if (jsonMessage.getMessageType().equals("Reboot")) {
            chosenDirectionReboot("top");
        }

        // AI always picks Virus and Trojan Horse
        if (jsonMessage.getMessageType().equals("PickDamage")) {
            String[] damageCards = new String[2];
            damageCards[0] = "Virus";
            damageCards[1] = "TrojanHorse";
            returnPickDamage(damageCards);
        }
    }

}
