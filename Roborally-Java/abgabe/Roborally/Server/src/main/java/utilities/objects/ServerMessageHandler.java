package utilities.objects;

import java.util.logging.*;
import model.Player;
import model.cards.programmingCards.*;
import model.cards.damageCards.*;
import model.cards.upgradeCards.MemorySwap;
import model.cards.upgradeCards.SpamBlocker;
import model.field.*;
import server.ClientHandler;
import server.Server;
import utilities.Converter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class creates ServerMessageHandler, handle json messages
 * @author Benedikt, Ilinur, Arda, Rea, Aigerim
 */

public class ServerMessageHandler {

    private static final Logger logger = Logger.getLogger(ServerMessageHandler.class.getName());
    boolean append1 = false;
    private FileHandler handler1;
    private static ArrayList<Integer> takenRobots = new ArrayList<>(); // If a Player chooses a robot this robot is added to takenRobots
    private static ArrayList<Player> readyPlayers = new ArrayList<>(); // All clients with status ready
    private static boolean firstPersonToFinishRegister = true;
    private static boolean firstReadyPlayer = true;
    private static ArrayList<Player> playingPlayers = new ArrayList<>(); // The Players who are ready at game start
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static ArrayList<String> takenStartingPoints = new ArrayList<>(); // If a starting point is chosen it is added to takenStartingPoints
    public static ArrayList<String> currentPositions = new ArrayList<>();
    public static ArrayList<Integer> clientIDsThatHaveChosenARobot = new ArrayList<>(); // If a client has chosen a robot, the clientID is added to this List

    public ServerMessageHandler() {
        try {
            handler1 = new FileHandler("./logs/LoggingServerMessageHandler.log",
                    append1);
            logger.addHandler(handler1);
            SimpleFormatter formatter = new SimpleFormatter();
            handler1.setFormatter(formatter);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private ReceivedChatObj receivedChatObj;


    /** Method handles all HelloServer Objects and gives each Client an ID number and disconnects if wrong protocol
     * @param server
     * @param task
     * @param bodyObject
     */
    public void handleHelloServer(Server server, ClientHandler task, HelloServerObj bodyObject) {
        logger.info(bodyObject.getGroup() + " " + bodyObject.getIsAI() + " " + bodyObject.getProtocol());

        if(server.getProtocolList().contains(bodyObject.getProtocol())) {

            // Get the last Client's ID an increase it by one
            int playersID = server.getPlayerID();
            playersID = playersID + 1;
            server.setCounterPlayerID(playersID);
            server.addToPlayerIDs(playersID);
            server.addToPlayerSocket(playersID, task.getSocket());

            // Send ClientID to Client
            JSONMessage clientID = new JSONMessage("Welcome", new WelcomeObj(playersID));
            server.privateMessage(clientID, server.getPlayerID());
        }

        else { // If the clients Protocol Type is invalid the client is disconnected
            try {
                Socket socket = task.getSocket();
                PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                String errorDisconnect = "You were disconnected due to an invalid Protocol Version.";
                JSONMessage errorDisconnectDueToWrongProtocol = new JSONMessage("Error", new ErrorObj(errorDisconnect));
                output.println(Converter.serializeJSON(errorDisconnectDueToWrongProtocol));
                output.flush();
                task.disconnectSocket(socket);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method handles all Player Values Objects. Player chooses name and Figure. This is then sent to all clients
     * @param server
     * @param task
     * @param bodyObject
     * @author Benedikt
     */
    public void handlePlayerValues(Server server, ClientHandler task, PlayerValuesObj bodyObject){

        int ids = server.getClientID(task.getSocket());
        int clientID = ids;

        // Check if chosen robot has not been chosen yet and if the player has already chosen a Robot
        if(!takenRobots.contains(bodyObject.getFigure()) && !clientIDsThatHaveChosenARobot.contains(clientID)) {
            takenRobots.add(bodyObject.getFigure());
            clientIDsThatHaveChosenARobot.add(clientID);
            logger.info(ANSI_CYAN + "Hier die Liste: "+ takenRobots + ANSI_CYAN);

            // Send message of chosen name and figure to the associated client
            JSONMessage jsonMessagePlayerValues = new JSONMessage("PlayerValues", new PlayerValuesObj(bodyObject.getName(), bodyObject.getFigure()));
            server.privateMessage(jsonMessagePlayerValues, clientID);
            // Send message of chosen name and figure to all clients
            JSONMessage jsonMessagePlayerAdded = new JSONMessage("PlayerAdded", new PlayerAddedObj(clientID, bodyObject.getName(), bodyObject.getFigure()));
            task.broadcast(jsonMessagePlayerAdded, -1);
            // new Player is created for the client. Player has Name, ID and Figure
            Player player = new Player(bodyObject.getName(), clientID, bodyObject.getFigure());
            // Player is added to servers currentPlayer's List
            server.addPlayer(player);

            // Every Client who has set Name and Figure gets an Info message about all other clients' names and figures
            ArrayList<Player> currentPlayers = server.getCurrentPlayers();
            String infoAboutCurrentPlayers = "Info about current players:";
            JSONMessage jsonMessageInfo = new JSONMessage("SendChat", new SendChatObj(infoAboutCurrentPlayers, player.getPlayerID()));
            server.privateMessage(jsonMessageInfo, player.getPlayerID());
            for(Player p : currentPlayers) {
                int clientIDp = p.getPlayerID();
                String name = p.getName();
                int figure = p.getFigure();
                JSONMessage jsonMessageCurrentPlayer = new JSONMessage("PlayerAdded", new PlayerAddedObj(clientIDp, name, figure));
                server.privateMessage(jsonMessageCurrentPlayer, player.getPlayerID());
            }
            String infoAboutMap = "The map is: " + server.getSelectedMap();
            JSONMessage jsonMessageInfoMap = new JSONMessage("SendChat", new SendChatObj(infoAboutMap, player.getPlayerID()));
            server.privateMessage(jsonMessageInfoMap, player.getPlayerID());
        }
        else if (clientIDsThatHaveChosenARobot.contains(clientID)){ // // Error message if Client has already chosen a Robot
            String robotAlreadyChosen = "You have already chosen a Robot.";
            JSONMessage jsonMessageErrorRobotTaken = new JSONMessage("Error", new ErrorObj(robotAlreadyChosen));
            server.privateMessage(jsonMessageErrorRobotTaken, clientID);
        }
        else { // Error message if Robot is already taken
            String robotTaken = "Your robot is taken.";
            JSONMessage jsonMessageErrorRobotTaken = new JSONMessage("Error", new ErrorObj(robotTaken));
            server.privateMessage(jsonMessageErrorRobotTaken, clientID);
        }
    }

    /**
     * Method handles all Player Added Objects
     */
    public void handlePlayerAdded(PlayerAddedObj bodyObject){
    }

    /**
     * Method handles all Set Status Objects and responds to clients pressing ready and not ready
     * @author Benedikt
     */
    public void handleSetStatus(Server server, ClientHandler task, SetStatusObj bodyObject){

        int ids = server.getClientID(task.getSocket());
        int clientID = ids;

        // Sends status message to the client
        JSONMessage jsonMessageStatus = new JSONMessage("SetStatus", new SetStatusObj(bodyObject.getReady()));
        server.privateMessage(jsonMessageStatus, clientID);
        // Informs all clients about changed ready Status of Client
        JSONMessage jsonMessagePlayerStatus = new JSONMessage("PlayerStatus", new PlayerStatusObj(clientID, bodyObject.getReady()));
        task.broadcast(jsonMessagePlayerStatus, -1);

        Player player = server.getPlayer(clientID); // get the according player
        // If player sets ready, player is added to readyPlayers
        if (bodyObject.getReady()) {
            readyPlayers.add(player);
            player.setIsReady(true);
            // The first Player who is ready chooses Map
            if(firstReadyPlayer){
                firstReadyPlayer = false;
                String[] availableMaps = new String[4];
                availableMaps[0] = "DizzyHighway";
                availableMaps[1] = "ExtraCrispy";
                availableMaps[2] = "LostBearings";
                availableMaps[3] = "DeathTrap";
                JSONMessage jsonMessageSelectMap = new JSONMessage("SelectMap", new SelectMapObj(availableMaps));
                server.privateMessage(jsonMessageSelectMap, clientID);
                String firstReadyPlayerID = "FirstReadyPlayersID" + clientID;
                // Selected map is sent to every one
                JSONMessage jsonMessageFirstReadyPlayerID = new JSONMessage("SendChat", new SendChatObj(firstReadyPlayerID, clientID));
                server.privateMessage(jsonMessageFirstReadyPlayerID, clientID);
            }
            if(server.getGameStarted()) { // Error message if the game has already started
                String errorGameHasStarted = "Game has already started. Please wait for new game.";
                JSONMessage jsonMessageErrorGameHasStarted = new JSONMessage("Error", new ErrorObj(errorGameHasStarted));
                server.privateMessage(jsonMessageErrorGameHasStarted, clientID);
            }
        }
        else { // If player sets not ready, player is removed from readyPlayers
            readyPlayers.remove(player);
            player.setIsReady(false);
        }
        logger.info(ANSI_CYAN + "Spieler die bereit sind: " + readyPlayers + ANSI_CYAN );

        logger.info(ANSI_CYAN + "server.getGameStarted() " + server.getGameStarted() + ANSI_CYAN );
        // If all players are ready and there is game currently played a new game starts
        if(server.allPlayersReady() && !server.getGameStarted()) {
            // Game only starts if a map is selected and there are between 2 and 6 players and all players are ready
            if (readyPlayers.size() > 1 && readyPlayers.size() < 7 && server.getMapIsSelected() && server.allCurrentPlayersAreReady() && !server.getGameStarted()) {
                String gameStarted = "Game starts now.";
                // Every Player is informed about game start
                JSONMessage jsonMessageGameStarted = new JSONMessage("SendChat", new SendChatObj(gameStarted, -1));
                server.broadcast(jsonMessageGameStarted);
                server.playPhaseNull(readyPlayers); // Phase Null begins
            } else if(!server.getMapIsSelected()) { // Error if a map has not been selected yet
                String errorNoMap = "Waiting until a map is selected...";
                JSONMessage jsonMessageErrorCardNotInHand = new JSONMessage("Error", new ErrorObj(errorNoMap));
                server.privateMessage(jsonMessageErrorCardNotInHand, clientID);
            }else if (!server.allCurrentPlayersAreReady()){ // Error if not all players are ready
                String errorNotAllPlayersReady = "Waiting for all Players to press ready.";
                JSONMessage jsonMessageErrorNotAllPlayersReady = new JSONMessage("Error", new ErrorObj(errorNotAllPlayersReady));
                server.privateMessage(jsonMessageErrorNotAllPlayersReady, clientID);
            } else { // Error message player count
                String errorGameCannotStart = "Waiting until there are 2-6 Players ready.";
                JSONMessage jsonMessageErrorCardNotInHand = new JSONMessage("Error", new ErrorObj(errorGameCannotStart));
                server.privateMessage(jsonMessageErrorCardNotInHand, clientID);
            }
        }
    }

    public void handlePlayerStatus(PlayerStatusObj bodyObject){
    }

    public void handleWelcome(WelcomeObj bodyObject) {
    }

    /**
     * Handles every played card sent by client to server. If the card is of the type "Activation", activates all field
     * elements such as Conveyor Belts and Lasers in the right order.
     * @param server
     * @param task
     * @param bodyObject
     * @author Ilinur, Arda
     */
    public void handlePlayCard(Server server, ClientHandler task, PlayCardObj bodyObject) {

        int ids = server.getClientID(task.getSocket());
        int clientID = ids;
        String card = bodyObject.getCard();


        Player currentPlayer = server.getPlayer(clientID);

        //TODo new
        if(!bodyObject.getCard().equals("Activate")) {

            JSONMessage jsonMessage = new JSONMessage("CardPlayed", new CardPlayedObj(clientID, card));
            task.broadcast(jsonMessage, -1);

            logger.info(ANSI_CYAN + "Coordinates before card is is played:" + currentPlayer.getRobotX() + currentPlayer.getRobotY() + ANSI_CYAN);

            Move1 move1 = new Move1();
            Move2 move2 = new Move2();
            Move3 move3 = new Move3();
            BackUp backUp = new BackUp();
            TurnLeft turnLeft = new TurnLeft();
            TurnRight turnRight = new TurnRight();
            PowerUp powerUp = new PowerUp();
            UTurn uTurn = new UTurn();
            Worm worm = new Worm();
            JSONMessage jsonMessageWorm = new JSONMessage("Reboot", new RebootObj(clientID));
            JSONMessage jsonMessageWormDir = new JSONMessage("RebootDirection", new RebootDirectionObj(currentPlayer.getRobotDirection()));

            if(currentPlayer.getProgrammingDeck().size() == 0){
                currentPlayer.shuffleCardsWhenDeckEmpty();
            }
            String firstCard = currentPlayer.getProgrammingDeck().get(0);
            JSONMessage jsonMessageReplace = new JSONMessage("ReplaceCard", new ReplaceCardObj(server.getCurrentRegister(), firstCard , clientID));


            switch (card) {
                case "Move1":
                    move1.playCard(currentPlayer, server);
                    server.checkReboot(currentPlayer);
                    break;

                case "Move2":
                    move2.playCard(currentPlayer, server);
                    server.checkReboot(currentPlayer);
                    break;

                case "Move3":
                    move3.playCard(currentPlayer, server);
                    server.checkReboot(currentPlayer);
                    break;

                case "BackUp":
                    backUp.playCard(currentPlayer, server);
                    server.checkReboot(currentPlayer);
                    break;

                case "PowerUp":
                    powerUp.playCard(currentPlayer, server);
                    break;

                case "TurnLeft":
                    turnLeft.playCard(currentPlayer, server);
                    break;

                case "TurnRight":
                    turnRight.playCard(currentPlayer, server);
                    break;

                case "UTurn":
                    uTurn.playCard(currentPlayer, server);
                    break;

                case "Spam":
                    Spam spam = new Spam();
                    spam.playCard(currentPlayer, server);
                    server.privateMessage(jsonMessageReplace, clientID);

                    switch (firstCard) {

                        case "Move1":
                            move1.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "Move2":
                            move2.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "Move3":
                            move3.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "BackUp":
                            backUp.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "PowerUp":
                            powerUp.playCard(currentPlayer, server);
                            break;

                        case "TurnLeft":
                            turnLeft.playCard(currentPlayer, server);
                            break;

                        case "TurnRight":
                            turnRight.playCard(currentPlayer, server);
                            break;

                        case "UTurn":
                            uTurn.playCard(currentPlayer, server);
                            break;


                        case "Again":
                            Again again = new Again();
                            again.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                    }
                    currentPlayer.getProgrammingDeck().remove(firstCard);
                    break;

                case "TrojanHorse":
                    TrojanHorse trojanHorse = new TrojanHorse();
                    trojanHorse.playCard(currentPlayer, server);
                    server.privateMessage(jsonMessageReplace, clientID);
                    switch (firstCard) {

                        case "Move1":
                            move1.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "Move2":
                            move2.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "Move3":
                            move3.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "BackUp":
                            backUp.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "PowerUp":
                            powerUp.playCard(currentPlayer, server);
                            break;

                        case "TurnLeft":
                            turnLeft.playCard(currentPlayer, server);
                            break;

                        case "TurnRight":
                            turnRight.playCard(currentPlayer, server);
                            break;

                        case "UTurn":
                            uTurn.playCard(currentPlayer, server);
                            break;


                        case "Again":
                            Again again = new Again();
                            again.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                    }
                    currentPlayer.getProgrammingDeck().remove(firstCard);
                    break;

                case "Virus":
                    Virus virus = new Virus();
                    virus.playCard(currentPlayer, server);
                    server.privateMessage(jsonMessageReplace, clientID);
                    switch (firstCard) {

                        case "Move1":
                            move1.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "Move2":
                            move2.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "Move3":
                            move3.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "BackUp":
                            backUp.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "PowerUp":
                            powerUp.playCard(currentPlayer, server);
                            break;

                        case "TurnLeft":
                            turnLeft.playCard(currentPlayer, server);
                            break;

                        case "TurnRight":
                            turnRight.playCard(currentPlayer, server);
                            break;

                        case "UTurn":
                            uTurn.playCard(currentPlayer, server);
                            break;


                        case "Again":
                            Again again = new Again();
                            again.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                    }
                    currentPlayer.getProgrammingDeck().remove(firstCard);
                    break;

                case "Worm":
                    worm.playCard(currentPlayer, server);
                    server.broadcast(jsonMessageWorm);
                    server.privateMessage(jsonMessageWormDir, clientID);
                    server.reboot(currentPlayer);
                    server.privateMessage(jsonMessageReplace, clientID);
                    switch (firstCard) {

                        case "Move1":
                            move1.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "Move2":
                            move2.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "Move3":
                            move3.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "BackUp":
                            backUp.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                        case "PowerUp":
                            powerUp.playCard(currentPlayer, server);
                            break;

                        case "TurnLeft":
                            turnLeft.playCard(currentPlayer, server);
                            break;

                        case "TurnRight":
                            turnRight.playCard(currentPlayer, server);
                            break;

                        case "UTurn":
                            uTurn.playCard(currentPlayer, server);
                            break;


                        case "Again":
                            Again again = new Again();
                            again.playCard(currentPlayer, server);
                            server.checkReboot(currentPlayer);
                            break;

                    }
                    currentPlayer.getProgrammingDeck().remove(firstCard);
                    break;

                case "Again": //play card of last register again
                    String lastRegisterCard = currentPlayer.getLastRegisterCard();
                    Again again = new Again();
                    again.playCard(currentPlayer, server);
                    server.checkReboot(currentPlayer);

                    if (lastRegisterCard.equals("Move1")) {
                        JSONMessage jsonMessageMovement = new JSONMessage("Movement", new MovementObj(clientID, currentPlayer.getRobotX(), currentPlayer.getRobotY()));
                        server.privateMessage(jsonMessageMovement, clientID);
                    } else if (lastRegisterCard.equals("Move2")) {
                        JSONMessage jsonMessageMovement2 = new JSONMessage("Movement", new MovementObj(clientID, currentPlayer.getRobotX(), currentPlayer.getRobotY()));
                        server.privateMessage(jsonMessageMovement2, clientID);
                    } else if (lastRegisterCard.equals("Move3")) {
                        JSONMessage jsonMessageMovement3 = new JSONMessage("Movement", new MovementObj(clientID, currentPlayer.getRobotX(), currentPlayer.getRobotY()));
                        server.privateMessage(jsonMessageMovement3, clientID);
                    } else if (lastRegisterCard.equals("BackUp")) {
                        JSONMessage jsonMessageBackUp = new JSONMessage("Movement", new MovementObj(clientID, currentPlayer.getRobotX(), currentPlayer.getRobotY()));
                        server.privateMessage(jsonMessageBackUp, clientID);
                    } else if (lastRegisterCard.equals("TurnLeft")) {
                        JSONMessage jsonMessageTurnLeft = new JSONMessage("PlayerTurning", new PlayerTurningObj(clientID, "counterclockwise"));
                        server.privateMessage(jsonMessageTurnLeft, clientID);
                    } else if (lastRegisterCard.equals("TurnRight")) {
                        JSONMessage jsonMessageTurnRight = new JSONMessage("PlayerTurning", new PlayerTurningObj(clientID, "clockwise"));
                        server.privateMessage(jsonMessageTurnRight, clientID);
                    } else if (lastRegisterCard.equals("UTurn")) {
                        JSONMessage jsonMessageUTurn = new JSONMessage("PlayerTurning", new PlayerTurningObj(clientID, "clockwise"));
                        server.privateMessage(jsonMessageUTurn, clientID);
                    }
                    break;

                case "ADMIN PRIVILEGE":
                    break;
                case "SPAM BLOCKER":
                    SpamBlocker spamBlocker = new SpamBlocker();
                    spamBlocker.playCard(currentPlayer, server);
                    ArrayList<String> yourNewCardsWithoutSpam = new ArrayList<>(currentPlayer.getCardsInHand());
                    String[] yourNewCardsWithoutSpamArray = new String[yourNewCardsWithoutSpam.size()];
                    int i = 0;
                    for(String newCard : yourNewCardsWithoutSpam){
                        yourNewCardsWithoutSpamArray[i] = newCard;
                        i++;
                    }
                    JSONMessage jsonMessageUpdateChoiceboxesNoSpam = new JSONMessage("YourCards", new YourCardsObj(yourNewCardsWithoutSpamArray));
                    server.privateMessage(jsonMessageUpdateChoiceboxesNoSpam, clientID);
                    logger.info(ANSI_CYAN + "programmingDeck:" + currentPlayer.getProgrammingDeck() + ANSI_CYAN);
                    logger.info(ANSI_CYAN + "cardsInHand:" + currentPlayer.getCardsInHand() + ANSI_CYAN);
                    break;
                case "MEMORY SWAP":
                    MemorySwap memorySwap = new MemorySwap();
                    memorySwap.playCard(currentPlayer, server);

                    break;
                case "REAR LASER":
                    currentPlayer.setBoughtRearLaser(true);
                    break;
                case "empty":
                    break;


                default:
                    String errorNotValidCard = "Not a valid Card.";
                    JSONMessage jsonMessageErrorNotValidCard = new JSONMessage("Error", new ErrorObj(errorNotValidCard));
                    server.privateMessage(jsonMessageErrorNotValidCard, clientID);
                    break;
            }
            logger.info(ANSI_CYAN + "Coordinates after the card is played:" + currentPlayer.getRobotX() + currentPlayer.getRobotY() + ANSI_CYAN);
            logger.info(ANSI_CYAN + "Direction after card is played" + currentPlayer.getRobotDirection() + ANSI_CYAN);

            if (card.equals("Move1") || card.equals("Move2") || card.equals("Move3")) {
                JSONMessage jsonMessageMovement1 = new JSONMessage("Movement", new MovementObj(clientID, currentPlayer.getRobotX(), currentPlayer.getRobotY()));
                server.privateMessage(jsonMessageMovement1, clientID);
            } else if (card.equals("BackUp")) {
                JSONMessage jsonMessageBackUp = new JSONMessage("Movement", new MovementObj(clientID, currentPlayer.getRobotX(), currentPlayer.getRobotY()));
                server.privateMessage(jsonMessageBackUp, clientID);
            } else if (card.equals("TurnLeft")) {
                JSONMessage jsonMessageTurnLeft = new JSONMessage("PlayerTurning", new PlayerTurningObj(clientID, "counterclockwise"));
                server.privateMessage(jsonMessageTurnLeft, clientID);
                logger.info(ANSI_CYAN + "Bewegung nach links, Roboter richtung:" + currentPlayer.getRobotDirection() + ANSI_CYAN);
            } else if (card.equals("TurnRight")) {
                JSONMessage jsonMessageTurnRight = new JSONMessage("PlayerTurning", new PlayerTurningObj(clientID, "clockwise"));
                server.privateMessage(jsonMessageTurnRight, clientID);
                logger.info(ANSI_CYAN + "Bewegung nach rechts, Roboter richtung:" + currentPlayer.getRobotDirection() + ANSI_CYAN);
            } else if (card.equals("UTurn")) {
                JSONMessage jsonMessageUTurn = new JSONMessage("PlayerTurning", new PlayerTurningObj(clientID, "clockwise"));
                server.privateMessage(jsonMessageUTurn, clientID);
            }

            currentPositions.clear();
            for (Player p : server.getPlayingPlayers()) {
                String position = server.getRobotColor(p.getFigure()) + "_" + p.getRobotX() + "_" + p.getRobotY() + "-" + p.getRobotDirection();
                currentPositions.add(position);
            }

            String currentPlayerPosition = "Current positions are: " + currentPositions.toString();
            JSONMessage sendPositions = new JSONMessage("SendChat", new SendChatObj(currentPlayerPosition, -1));
            server.broadcast(sendPositions);

        }else {

            //Activate conveyor belts
            for(Player p: server.getPlayingPlayers()) {
                boolean checkConveyor = server.checkConveyorBelt(p);
                if (checkConveyor) {
                    HashMap<String, ConveyorBelt> conveyorBelt = server.getConveyorBeltMap();
                    String playerPosition;
                    ConveyorBelt conveyorBeltField;
                    ArrayList<String> orientation;
                    int xPosition;
                    int yPosition;

                    playerPosition = p.getRobotX() + "_" + p.getRobotY();
                    conveyorBeltField = conveyorBelt.get(playerPosition);
                    orientation = conveyorBeltField.getOrientation();
                    int speed = conveyorBeltField.getSpeed();
                    xPosition = p.getRobotX();
                    yPosition = p.getRobotY();
                    String conveyorOrientation = orientation.get(0);

                    switch (conveyorOrientation) {
                        case "top":
                            if (speed == 1) {
                                p.setRobotY(yPosition - 1);

                                if (server.checkPit(p)) {
                                    server.checkIfPit(p);
                                }
                            }
                            if (speed == 2) {
                                p.setRobotY(yPosition - 1);
                                if (server.checkPit(p)) {
                                    server.checkIfPit(p);
                                } else {
                                    xPosition = p.getRobotX();
                                    yPosition = p.getRobotY();
                                    String playerPositionNew = xPosition + "_" + yPosition;

                                    if (conveyorBelt.containsKey(playerPositionNew)) {
                                        conveyorBeltField = conveyorBelt.get(playerPositionNew);
                                        orientation = conveyorBeltField.getOrientation();

                                        if (orientation.get(0).equals("top")) {
                                            p.setRobotY(yPosition - 1);

                                            if (server.checkPit(p)) {
                                                server.checkIfPit(p);
                                            }
                                        }
                                        if (orientation.get(0).equals("left")) {
                                            p.setRobotX(xPosition - 1);

                                            if (server.checkPit(p)) {
                                                server.checkIfPit(p);
                                            }
                                        }
                                        if (orientation.get(0).equals("right")) {
                                            p.setRobotX(xPosition + 1);

                                            if (server.checkPit(p)) {
                                                server.checkIfPit(p);
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case "bottom":
                            if (speed == 1) {
                                p.setRobotY(yPosition + 1);

                                if (server.checkPit(p)) {
                                    server.checkIfPit(p);
                                }
                            }
                            if (speed == 2) {
                                p.setRobotY(yPosition + 1);

                                if (server.checkPit(p)) {
                                    server.checkIfPit(p);
                                } else {
                                    xPosition = p.getRobotX();
                                    yPosition = p.getRobotY();
                                    String playerPositionNew = xPosition + "_" + yPosition;

                                    if (conveyorBelt.containsKey(playerPositionNew)) {
                                        conveyorBeltField = conveyorBelt.get(playerPositionNew);
                                        orientation = conveyorBeltField.getOrientation();

                                        if (orientation.get(0).equals("bottom")) {
                                            p.setRobotY(yPosition + 1);

                                            if (server.checkPit(p)) {
                                                server.checkIfPit(p);
                                            }
                                        }
                                        if (orientation.get(0).equals("left")) {
                                            p.setRobotX(xPosition - 1);

                                            if (server.checkPit(p)) {
                                                server.checkIfPit(p);
                                            }
                                        }
                                        if (orientation.get(0).equals("right")) {
                                            p.setRobotX(xPosition + 1);

                                            if (server.checkPit(p)) {
                                                server.checkIfPit(p);
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case "right":
                            if (speed == 1) {
                                p.setRobotX(xPosition + 1);

                                if (server.checkPit(p)) {
                                    server.checkIfPit(p);
                                }
                            }
                            if (speed == 2) {
                                p.setRobotX(xPosition + 1);
                                if (server.checkPit(p)) {
                                    server.checkIfPit(p);
                                } else {
                                    xPosition = p.getRobotX();
                                    yPosition = p.getRobotY();
                                    String playerPositionNew = xPosition + "_" + yPosition;

                                    if (conveyorBelt.containsKey(playerPositionNew)) {
                                        conveyorBeltField = conveyorBelt.get(playerPositionNew);
                                        orientation = conveyorBeltField.getOrientation();

                                        if (orientation.get(0).equals("right")) {
                                            p.setRobotX(xPosition + 1);

                                            if (server.checkPit(p)) {
                                                server.checkIfPit(p);
                                            }
                                        }
                                        if (orientation.get(0).equals("top")) {
                                            p.setRobotY(yPosition - 1);

                                            if (server.checkPit(p)) {
                                                server.checkIfPit(p);
                                            }
                                        }
                                        if (orientation.get(0).equals("bottom")) {
                                            p.setRobotY(yPosition + 1);

                                            if (server.checkPit(p)) {
                                                server.checkIfPit(p);
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case "left":
                            if (speed == 1) {
                                p.setRobotX(xPosition - 1);

                                if (server.checkPit(p)) {
                                    server.checkIfPit(p);
                                }
                            }
                            if (speed == 2) {
                                p.setRobotX(xPosition - 1);

                                if (server.checkPit(p)) {
                                    server.checkIfPit(p);
                                } else {
                                    xPosition = p.getRobotX();
                                    yPosition = p.getRobotY();
                                    String playerPositionNew = xPosition + "_" + yPosition;

                                    if (conveyorBelt.containsKey(playerPositionNew)) {
                                        conveyorBeltField = conveyorBelt.get(playerPositionNew);
                                        orientation = conveyorBeltField.getOrientation();

                                        if (orientation.get(0).equals("left")) {
                                            p.setRobotX(xPosition - 1);

                                            if (server.checkPit(p)) {
                                                server.checkIfPit(p);
                                            }
                                        }
                                        if (orientation.get(0).equals("top")) {
                                            p.setRobotY(yPosition - 1);

                                            if (server.checkPit(p)) {
                                                server.checkIfPit(p);
                                            }
                                        }
                                        if (orientation.get(0).equals("bottom")) {
                                            p.setRobotY(yPosition + 1);

                                            if (server.checkPit(p)) {
                                                server.checkIfPit(p);
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
            }

            //Move Checkpoints
            if(server.getSelectedMap().equals("Twister")) {
                server.getCheckPointMap();
                server.pushTwisterCheckPoints();
                for (String key : server.getCheckPointMap().keySet()) {
                    int id = server.getCheckPointMap().get(key).getCount();
                    String x = key.split("_")[0];
                    String y = key.split("_")[1];

                    CheckpointMovedObj checkpointMoved = new CheckpointMovedObj(id, Integer.parseInt(x), Integer.parseInt(y));
                    JSONMessage cpMoved = new JSONMessage("CheckpointMoved", checkpointMoved);
                    server.broadcast(cpMoved);
                }
            }

                //Activate PushPanel
            for (Player player : server.getPlayingPlayers()) {
                boolean checkPushPanel = server.checkPushPanel(player);
                if (checkPushPanel) {
                    HashMap<String, PushPanel> pushPanelHashMap = server.getPushPanelMap();
                    String playerPosition;
                    int xPosition;
                    int yPosition;
                    PushPanel pushPanelField;
                    ArrayList<String> orientation;
                    ArrayList<Integer> register;
                    xPosition = player.getRobotX();
                    yPosition = player.getRobotY();
                    playerPosition = player.getRobotX() + "_" + player.getRobotY();
                    pushPanelField = pushPanelHashMap.get(playerPosition);
                    orientation = pushPanelField.getOrientation();
                    register = pushPanelField.getRegister();

                    logger.info(ANSI_CYAN + "registers in PushPanel: " + Arrays.toString(new ArrayList[]{pushPanelField.getRegister()}) + ANSI_CYAN);

                    if (register.contains(server.getCurrentRegister())) {
                        switch (orientation.get(0)) {
                            case "top":
                                player.setRobotY(yPosition - 1);
                                if (server.checkPit(player)) {
                                    server.checkIfPit(player);
                                }
                                break;

                            case "bottom":
                                player.setRobotY(yPosition + 1);
                                if (server.checkPit(player)) {
                                    server.checkIfPit(player);
                                }
                                break;

                            case "right":
                                player.setRobotX(xPosition + 1);
                                if (server.checkPit(player)) {
                                    server.checkIfPit(player);
                                }
                                break;

                            case "left":
                                player.setRobotX(xPosition - 1);
                                if (server.checkPit(player)) {
                                    server.checkIfPit(player);
                                }
                                break;
                        }
                    }

                }
            }
            for (Player p : server.getPlayingPlayers()) {
                boolean rotatePlayer = server.checkGear(p);
                if (rotatePlayer) {
                    HashMap<String, Gear> gear = server.getGearMap();
                    int xPosition = p.getRobotX();
                    int yPosition = p.getRobotY();
                    String playerPosition = xPosition + "_" + yPosition;
                    String direction = p.getRobotDirection();
                    Gear gearField;
                    gearField = gear.get(playerPosition);
                    ArrayList<String> orientation = gearField.getOrientation();

                    if (orientation.get(0).equals("clockwise")) {

                        switch (direction) {
                            case "north":
                                p.setRobotDirection("east");
                                break;
                            case "south":
                                p.setRobotDirection("west");
                                break;
                            case "east":
                                p.setRobotDirection("south");
                                break;
                            case "west":
                                p.setRobotDirection("north");
                                break;

                        }
                    }
                    if (orientation.get(0).equals("counterclockwise")) {

                        switch (direction) {
                            case "north":
                                p.setRobotDirection("west");
                                break;
                            case "south":
                                p.setRobotDirection("east");
                                break;
                            case "east":
                                p.setRobotDirection("north");
                                break;
                            case "west":
                                p.setRobotDirection("south");
                                break;
                        }
                    }
                }
            }

                //Activate Board Lasers
                ArrayList <Player> playersShotByWallLasers = server.whoGotShotByWallLasers();
                if (playersShotByWallLasers.size() != 0) {
                    String gotShot = clientID + "These players have been hit by wall lasers ,";
                    for (Player pl : playersShotByWallLasers) {
                        if (!playersShotByWallLasers.get(0).equals(pl)){
                            gotShot += "," ;
                    }
                        gotShot += pl.getPlayerID();

                        String[] damageCards = {""};
                        ArrayList<String> damageCardsArray = new ArrayList<>();

                        if (!server.getSpamPile().isEmpty() && server.getSpamPile().size() >= 1) {

                            Spam spam = new Spam();
                            pl.addInDiscardPile(spam.getCardName());

                            server.removeFromSpamPile(spam.getCardName());

                            String[] spamCards = {"Spam"};

                            JSONMessage jsonMessageDamage = new JSONMessage("DrawDamage", new DrawDamageObj(pl.getPlayerID(), spamCards));
                            server.privateMessage(jsonMessageDamage, pl.getPlayerID());
                        }
                        else {

                            ArrayList<String> damageCardPileSizes = new ArrayList<>();
                            damageCardPileSizes.add("trojanHorsePile_" + server.getTrojanHorsePile().size());
                            damageCardPileSizes.add("wormPile_" + server.getWormPile().size());
                            damageCardPileSizes.add("virusPile_" + server.getVirusPile().size());
                            for (String s: damageCardPileSizes){
                                int size = Integer.parseInt(s.split("_")[1]);
                                if(size >=1) {
                                    if(s.contains("worm")){
                                        damageCardsArray.add("Worm");
                                    }else if (s.contains("trojanHorse")){
                                        damageCardsArray.add("TrojanHorse");
                                    }else if (s.contains("virus")){
                                        damageCardsArray.add("Virus");
                                    }
                                }
                                String [] dmgCards = new String[damageCardsArray.size()];
                                dmgCards = damageCardsArray.toArray(dmgCards);
                                damageCards = dmgCards ;
                            }
                            JSONMessage jsonMessagePick = new JSONMessage("PickDamage", new PickDamageObj(2, damageCards));
                            server.privateMessage(jsonMessagePick, pl.getPlayerID());



                        }

                    }
                    SendChatObj shotPlayers = new SendChatObj(gotShot, -1);
                    JSONMessage gotShotMessage = new JSONMessage("SendChat", shotPlayers);
                    server.broadcast(gotShotMessage);
                }

                //Activate Robot Lasers
                for (Player p: server.getPlayingPlayers()){
                    int shot = server.howManyTimesShotByOtherRobots(p);
                    if(shot != 0){
                        for (int i = 1;i <= shot; i++){

                            String[] damageCards = {""};
                            ArrayList<String> damageCardsArray = new ArrayList<>();

                            if (!server.getSpamPile().isEmpty() && server.getSpamPile().size() >= 1) {

                                Spam spam = new Spam();
                                p.addInDiscardPile(spam.getCardName());

                                server.removeFromSpamPile(spam.getCardName());

                                String[] spamCards = {"Spam"};

                                JSONMessage jsonMessageDamage = new JSONMessage("DrawDamage", new DrawDamageObj(p.getPlayerID(), spamCards));
                                server.privateMessage(jsonMessageDamage, p.getPlayerID());
                            }
                            else {

                                ArrayList<String> damageCardPileSizes = new ArrayList<>();
                                damageCardPileSizes.add("trojanHorsePile_" + server.getTrojanHorsePile().size());
                                damageCardPileSizes.add("wormPile_" + server.getWormPile().size());
                                damageCardPileSizes.add("virusPile_" + server.getVirusPile().size());
                                for (String s: damageCardPileSizes){
                                    int size = Integer.parseInt(s.split("_")[1]);
                                    if(size >=1) {
                                        if(s.contains("worm")){
                                            damageCardsArray.add("Worm");
                                        }else if (s.contains("trojanHorse")){
                                            damageCardsArray.add("TrojanHorse");
                                        }else if (s.contains("virus")){
                                            damageCardsArray.add("Virus");
                                        }
                                    }
                                    String [] dmgCards = new String[damageCardsArray.size()];
                                    dmgCards = damageCardsArray.toArray(dmgCards);
                                    damageCards = dmgCards ;
                                }
                                JSONMessage jsonMessagePick = new JSONMessage("PickDamage", new PickDamageObj(2, damageCards));
                                server.privateMessage(jsonMessagePick, p.getPlayerID());



                            }
                        }


                        SendChatObj shotPlayer = new SendChatObj(p.getPlayerID() + " has been shot " + shot + " time(s)!", -1);
                        JSONMessage gotShotMessage2 = new JSONMessage("SendChat", shotPlayer);
                        server.broadcast(gotShotMessage2);
                    }
                }
            //Activate EnergyCubes on LastRegister
            for (Player p : server.getPlayingPlayers()) {
                if (server.checkLastRegisterEnergySpace(p)) {
                    p.addEnergyCubes();
                    server.removeEnergyCubeBank();
                    EnergyObj reachedEnergySpace = new EnergyObj(p.getPlayerID(), p.getEnergyCubes(), "EnergySpace");
                    JSONMessage addingEnergyCube = new JSONMessage("Energy", reachedEnergySpace);
                    server.privateMessage(addingEnergyCube, p.getPlayerID());
                }
            }
                //Activate EnergyCubes
            for (Player p : server.getPlayingPlayers()) {
                boolean addEnergyCube = server.checkEnergySpace(p);
                if (addEnergyCube) {
                    logger.info(ANSI_CYAN + "CurrentEnergyCubes in boolean vor add" + p.getEnergyCubes() + ANSI_CYAN);
                    p.addEnergyCubes();
                    EnergyObj reachedEnergySpace = new EnergyObj(p.getPlayerID(), p.getEnergyCubes(), "EnergySpace");
                    JSONMessage addingEnergyCube = new JSONMessage("Energy", reachedEnergySpace);
                    server.privateMessage(addingEnergyCube, p.getPlayerID());
                }
            }

            //Activate CheckPoints
            for (Player p : server.getPlayingPlayers()) {
                boolean addCheckpoint = server.checkPointControl(p);
                if (addCheckpoint) {
                    CheckPointReachedObj reachedCheckPoint = new CheckPointReachedObj(p.getPlayerID(), p.getCheckpointTokens() + 1);
                    p.addCheckpointTokens();
                    JSONMessage addingCheckPoint = new JSONMessage("CheckPointReached", reachedCheckPoint);
                    server.privateMessage(addingCheckPoint, p.getPlayerID());
                }
            }

            //Is game finished?
            for (Player p : playingPlayers){
                switch (server.getSelectedMap()) {
                    case "DizzyHighway":
                        if (p.getCheckpointTokens() == 1){
                            GameFinishedObj gameFinished = new GameFinishedObj(p.getPlayerID()) ;
                            JSONMessage finishingGame = new JSONMessage("GameFinished", gameFinished);
                            server.broadcast(finishingGame);
                        }
                        break;
                    case "DeathTrap" :
                        if (p.getCheckpointTokens() == 5){
                            GameFinishedObj gameFinished = new GameFinishedObj(p.getPlayerID()) ;
                            JSONMessage finishingGame = new JSONMessage("GameFinished", gameFinished);
                            server.broadcast(finishingGame);
                        }
                        break;

                    case "LostBearings" , "Twister" , "ExtraCrispy" :
                        if (p.getCheckpointTokens() == 4){
                            GameFinishedObj gameFinished = new GameFinishedObj(p.getPlayerID()) ;
                            JSONMessage finishingGame = new JSONMessage("GameFinished", gameFinished);
                            server.broadcast(finishingGame);
                        }
                        break;
                }
            }

            for (Player p : playingPlayers) {
                String position = server.getRobotColor(p.getFigure()) + "_" + p.getRobotX() + "_" + p.getRobotY() + "-" + p.getRobotDirection();
                for (int i = 0; i < currentPositions.size(); i++) {
                    if (currentPositions.get(i).contains(server.getRobotColor(p.getFigure()))) {
                        currentPositions.remove(currentPositions.get(i));
                        currentPositions.add(position);
                    }
                }
            }

            if (server.isRegisterOver()) {
                server.setRegisterOver(false);
                String currentPlayerPosition1 = "Current positions are: " + currentPositions.toString();
                JSONMessage sendPositions1 = new JSONMessage("SendChat", new SendChatObj(currentPlayerPosition1, -1));
                server.broadcast(sendPositions1);
            }
        }

        if(currentPlayer.isCurrentRegisterFive()){
            logger.info(ANSI_CYAN + "current register is five" + ANSI_CYAN);
            currentPlayer.setCurrentRegisterNumber0(0);
        }

    }

    /**
     * Handles all cards selected for the registers
     * If a player has filled all registers the timer starts
     * Late client receive "CardsYouGotNow" message
     * Error messages for every problem that can occur
     * @param server
     * @param task
     * @param bodyObject
     * @author Benedikt
     */
    public void handleSelectedCard(Server server, ClientHandler task, SelectedCardObj bodyObject) {
        String card = bodyObject.getCard();
        int register = bodyObject.getRegister();

        int ids = server.getClientID(task.getSocket());
        int clientID = ids;
        Player player = server.getPlayer(clientID);

        if (player.isCardInHand(card) && player.isRegisterNull(register) && !(card.equals("Again") && register == 0)) {

            player.addRegister(register, card);
            player.removeFromHand(card);
            logger.info(ANSI_CYAN + "Your Register: " + Arrays.toString(player.getRegister()) + ANSI_CYAN);
            JSONMessage jsonMessagePutInRegister = new JSONMessage("SelectedCard", new SelectedCardObj(card, bodyObject.getRegister()));
            server.privateMessage(jsonMessagePutInRegister, clientID);
            JSONMessage jsonMessageTellAllPutInRegister = new JSONMessage("CardSelected", new CardSelectedObj(clientID, bodyObject.getRegister(), true));
            task.broadcast(jsonMessageTellAllPutInRegister, -1);
            // Update Choiceboxes with cardsInHand
            String[] updatedCardsInHand = new String[player.getCardsInHand().size()];
            int i = 0;
            for(String updatedCard : player.getCardsInHand()){
                updatedCardsInHand[i] = updatedCard;
                i++;
            }
            JSONMessage jsonMessageUpdateChoiceboxes = new JSONMessage("YourCards", new YourCardsObj(updatedCardsInHand));
            server.privateMessage(jsonMessageUpdateChoiceboxes, clientID);

            if (player.isRegisterFull()) {
                JSONMessage jsonMessageSelectionFinished = new JSONMessage("SelectionFinished", new SelectionFinishedObj(player.getPlayerID()));
                server.broadcast(jsonMessageSelectionFinished);
                if (firstPersonToFinishRegister) {
                    firstPersonToFinishRegister = false;
                    // Timer starten
                    JSONMessage jsonMessageTimerStarted = new JSONMessage("TimerStarted", new TimerStartedObj());
                    server.broadcast(jsonMessageTimerStarted);

                    TimerTask tasknew = new TimerTask() {
                        public void run() {
                            playingPlayers = server.getPlayingPlayers();
                            // How many clients have not filled their register.
                            int countLateClients = 0;
                            for (Player p : playingPlayers) {
                                if (!p.isRegisterFull()) {
                                    countLateClients++;
                                }
                            }
                            logger.info(ANSI_CYAN + "Number of late clients: " + countLateClients + ANSI_CYAN);
                            // Add these clients to lateClients
                            int[] lateClients = new int[countLateClients];
                            int i = 0;
                            for (Player p : playingPlayers) {
                                if (!p.isRegisterFull()) {
                                    lateClients[i] = p.getPlayerID();
                                    logger.info(ANSI_CYAN + "ID of late Player: " + lateClients[i] + ANSI_CYAN);
                                    i++;
                                }
                            }
                            JSONMessage jsonMessageTimerEnded = new JSONMessage("TimerEnded", new TimerEndedObj(lateClients));
                            server.broadcast(jsonMessageTimerEnded);

                            // All players discard their cards
                            for (Player player : playingPlayers) {
                                player.discardAll();
                            }

                            // Create List of LateClients as Players
                            ArrayList<Player> latePlayers = new ArrayList<>();
                            for (int playerID : lateClients) {
                                Player player = server.getPlayer(playerID);
                                latePlayers.add(player);
                            }
                            // Punishment for late Players
                            for (Player player : latePlayers) {
                                // player.discardAll();
                                String[] cardsYouGotKnow = player.fillEmptyRegisters();
                                JSONMessage jsonMessageCardsYouGotKnow = new JSONMessage("CardsYouGotNow", new CardsYouGotNowObj(cardsYouGotKnow));
                                server.privateMessage(jsonMessageCardsYouGotKnow, player.getPlayerID());
                            }
                            // empty all Players hands
                            server.emptyAllPlayersHands();
                            // Set Activation phase
                            JSONMessage jsonMessageActivationPhase = new JSONMessage("ActivePhase", new ActivePhaseObj(3));
                            task.broadcast(jsonMessageActivationPhase, -1);
                            firstPersonToFinishRegister = true;
                            server.playActivationPhase(0);
                        }
                    };
                    Timer timer = new Timer();
                    long delay = 30000;
                    timer.schedule(tasknew, delay);
                }
            }

        } else if (card.equals("Null")){
            player.deleteRegister(register);
            JSONMessage jsonMessagePutInRegister = new JSONMessage("SelectedCard", new SelectedCardObj(card, bodyObject.getRegister()));
            server.privateMessage(jsonMessagePutInRegister, clientID);
            // Update Choiceboxes with cardsInHand
            String[] updatedCardsInHand = new String[player.getCardsInHand().size()];
            int i = 0;
            for(String updatedCard : player.getCardsInHand()){
                updatedCardsInHand[i] = updatedCard;
                i++;
            }
            JSONMessage jsonMessageUpdateChoiceboxes = new JSONMessage("YourCards", new YourCardsObj(updatedCardsInHand));
            server.privateMessage(jsonMessageUpdateChoiceboxes, clientID);
        } else if (!player.isCardInHand(card)) {
            String errorCardNotInHand = "This card is not in your hand.";
            JSONMessage jsonMessageErrorCardNotInHand = new JSONMessage("Error", new ErrorObj(errorCardNotInHand));
            server.privateMessage(jsonMessageErrorCardNotInHand, clientID);
        } else if (card.equals("Again") && register == 0){
            String errorAgainInRegister0 = "Again cannot be put in register 0.";
            JSONMessage jsonMessageErrorAgainInRegister0 = new JSONMessage("Error", new ErrorObj(errorAgainInRegister0));
            server.privateMessage(jsonMessageErrorAgainInRegister0, clientID);
        }
        else {
            String errorRegisterFilled = "Register is already filled. Send Null to empty register.";
            JSONMessage jsonMessageErrorCardNotInHand = new JSONMessage("Error", new ErrorObj(errorRegisterFilled));
            server.privateMessage(jsonMessageErrorCardNotInHand, clientID);
        }
    }

    /** Method handles all SendChat Objects
     * @param server
     * @param task
     * @param bodyObject
     */
    public void handleSendChat(Server server, ClientHandler task, SendChatObj bodyObject) {
        String playerMessage = bodyObject.getMessage();
        Socket clients = task.getSocket();
        int ids = server.getClientID(task.getSocket());
        int clientID = ids; // client ID of the client sending the message
        logger.info("Message from client " + clientID);
        int to = bodyObject.getTo(); // ID of the addressed client or -1 for broadcast

        if (to == -1) { // broadcast
            JSONMessage jsonMessage = new JSONMessage("ReceivedChat", new ReceivedChatObj(playerMessage, clientID, false));
            task.broadcast(jsonMessage, to);
            if(bodyObject.getMessage().equals("Play next Card.")){
                // server.setCurrentPlayer(clientID);
                server.playNextCard();
            } else if(bodyObject.getMessage().equals("bye")) {
                try {
                    task.disconnectSocket(clients);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (server.isClientIDValid(to)) { // private message to an existing client
            JSONMessage jsonMessage = new JSONMessage("ReceivedChat", new ReceivedChatObj(playerMessage, clientID, true));
            server.privateMessage(jsonMessage, to);
        } else { // Error message
            String error = "You did not choose a valid player.";
            JSONMessage jsonMessage = new JSONMessage("Error", new ErrorObj(error));
            server.privateMessage(jsonMessage, clientID);
        }

    }

    /**
     * Handles errors and informs the client
     * @param server
     * @param task
     * @param bodyObject
     */
    public void handleError(Server server, ClientHandler task, ErrorObj bodyObject) {
        int ids = server.getClientID(task.getSocket());
        int clientID = ids;
        JSONMessage jsonMessageError = new JSONMessage("Error", new ErrorObj(bodyObject.getError()));
        server.privateMessage(jsonMessageError, clientID);
    }


    public void handleRebootDirection(Server server, ClientHandler task, RebootDirectionObj bodyObj){
        String direction = bodyObj.getDirection();
        int ids = server.getClientID(task.getSocket());
        int clientID = ids;
        Player player = server.getPlayer(clientID);

        switch (direction){
            case "top":
                player.setRobotDirection("south");
                break;
            case "bottom":
                player.setRobotDirection("north");
                break;
            case "left":
                player.setRobotDirection("east");
                break;
            case "right":
                player.setRobotDirection("west");
                break;
        }

        logger.info(ANSI_CYAN + "in server handleReboot: " + bodyObj.getDirection() + ANSI_CYAN);

        String addToCurrentPositions = server.getRobotColor(player.getFigure()) + "_" + String.valueOf(player.getRobotX()) + "_" + String.valueOf(player.getRobotY()) + "-" + player.getRobotDirection();
        currentPositions.add(addToCurrentPositions);

        String currentPlayerPosition = "Current positions are: " + currentPositions.toString();
        JSONMessage sendPositions = new JSONMessage("SendChat", new SendChatObj(currentPlayerPosition, -1));
        server.broadcast(sendPositions);

        JSONMessage jsonMessageReboot = new JSONMessage("RebootDirection", new RebootDirectionObj(bodyObj.getDirection()));
        server.privateMessage(jsonMessageReboot, clientID);

    }

    /**
     * Handles Map selected and if all current players are ready starts the game
     * @param server
     * @param task
     * @param bodyObj
     * @author Aika, Benedikt
     */
    public void handleMapSelected(Server server, ClientHandler task, MapSelectedObj bodyObj) {
        int ids = server.getClientID(task.getSocket());
        int clientID = ids;

        JSONMessage jsonMessageMapSelected = new JSONMessage("MapSelected", new MapSelectedObj(bodyObj.getMap()));
        server.broadcast(jsonMessageMapSelected);
        server.setMapIsSelected();

        String mapName = bodyObj.getMap();
        server.setSelectedMap(mapName);

        Path maps = Paths.get("Server/src/main/resources/" + mapName + ".json");
        String mapJSON = null;
        try {
            mapJSON = Files.readString(maps, StandardCharsets.UTF_8);
            JSONMessage gameMap = new JSONMessage("GameStarted", new GameStartedObj(server.map(mapName)));
            server.buildMap(server.getSelectedMap());
            //server.buildMap("DizzyHighway");
            server.getWallMap();
            //end edit
            task.broadcast(gameMap, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(readyPlayers.size() > 1 && readyPlayers.size() < 7 && server.allCurrentPlayersAreReady() && !server.getGameStarted()){
            server.playPhaseNull(readyPlayers); // Phase Null begins
            String gameStarted = "Game starts now.";
            // Every Player is informed about game start
            JSONMessage jsonMessageGameStarted = new JSONMessage("SendChat", new SendChatObj(gameStarted, -1));
            server.broadcast(jsonMessageGameStarted);
        }
    }

    /**
     * Handles the chosen Starting Points by Players and starts the upgradephase if all players have chosen starting point
     * @param server
     * @param task
     * @param bodyObj
     * @author Benedikt
     */
    public void handleSetStartingPoint(Server server, ClientHandler task, SetStartingPointObj bodyObj) {
        int ids = server.getClientID(task.getSocket());
        int clientID = ids;
        Player player = server.getPlayer(clientID);
        int playersStartingPointX = bodyObj.getX();
        int playersStartingPointY = bodyObj.getY();
        player.setRobotX(playersStartingPointX);
        player.setRobotY(playersStartingPointY);
        player.setStartingPointX(playersStartingPointX);
        player.setStartingPointY(playersStartingPointY);

        String startingPoint = String.valueOf(bodyObj.getX()) + String.valueOf(bodyObj.getY());
        String addToCurrentPositions = server.getRobotColor(player.getFigure()) + "_" + String.valueOf(bodyObj.getX()) + "_" + String.valueOf(bodyObj.getY()) + "-" + player.getRobotDirection();
        currentPositions.add(addToCurrentPositions);

        if(!takenStartingPoints.contains(startingPoint) && !player.getPlayerHasSetStartingPoint()) {
            takenStartingPoints.add(startingPoint);

            JSONMessage jsonMessageStartingPointTaken = new JSONMessage("StartingPointTaken", new StartingPointTakenObj(playersStartingPointX, playersStartingPointY, clientID));
            server.broadcast(jsonMessageStartingPointTaken);
            player.playerSetStartingPoint();
            logger.info(ANSI_CYAN + "getPlayerHasSetStartingPoint: " + player.getPlayerHasSetStartingPoint() + ANSI_CYAN);
            logger.info(ANSI_CYAN + "allPlayersSetStartingPoint: " + server.allPlayersSetStartingPoint() + ANSI_CYAN);

            if(server.allPlayersSetStartingPoint()){
                server.spamDeck();
                server.trojanHorseDeck();
                server.wormDeck();
                server.virusDeck();
                server.playUpgradePhase();
            }
            else {
                int nextPlayer = server.getNextPlayersNoStartingPoint();
                JSONMessage jsonMessageNextPlayer = new JSONMessage("CurrentPlayer", new CurrentPlayerObj(nextPlayer));
                server.broadcast(jsonMessageNextPlayer);
                server.removePlayerFromPlayersNoStartingPoint();
            }
        }
        else {
            String error = "Either the position is already taken or you have already chosen a starting point.";
            JSONMessage jsonMessageErrorInvalidPosition = new JSONMessage("Error", new ErrorObj(error));
            server.privateMessage(jsonMessageErrorInvalidPosition, clientID);
        }

        String currentPlayerPosition = "Current positions are: " + currentPositions.toString();
        JSONMessage sendPositions = new JSONMessage("SendChat", new SendChatObj(currentPlayerPosition, -1));
        server.broadcast(sendPositions);
    }

    public void handleAliveObj() {
        logger.info(ANSI_CYAN + "( MESSAGE ): Alive Message response" + ANSI_RESET);
    }

    public void handleConnectionUpdate(Server server, ConnectionUpdateObj bodyObject){
        String connectionUpdateMessage = "Player " + bodyObject.getClientID() + " has lost its connection.";
        JSONMessage connection = new JSONMessage("SendChat", new SendChatObj(connectionUpdateMessage, -1));
        server.broadcast(connection);
    }

    public void handleSelectedDamage(Server server, ClientHandler task, SelectedDamageObj bodyObject){
        int ids = server.getClientID(task.getSocket());
        int clientID = ids;
        Player player = server.getPlayer(clientID);
        for(String card : bodyObject.getCards()) {

            player.addInDiscardPile(card);
            logger.info(ANSI_CYAN + "discard pile: " + player.getDiscardPile() + ANSI_CYAN);
            if(card.equals("TrojanHorse")){
                server.removeFromTrojanHorsePile(card);
            }
            else if(card.equals("Virus")){
                server.removeFromVirusPile(card);
            }
            else if(card.equals("Worm")){
                server.removeFromWormPile(card);
            }
        }
    }

    public void handleChooseRegister(Server server, ClientHandler task, ChooseRegisterObj bodyObject){
        int ids = server.getClientID(task.getSocket());
        int clientID = ids;
        logger.info(ANSI_CYAN + "chosen Register AdminP: " + bodyObject.getRegister() + ANSI_CYAN);
        if(!server.addToAdminPrivileges(bodyObject.getRegister(), clientID)){
            String errorAlreadyAdminPrivilege = "A player already requested admin privilege for this register.";
            JSONMessage jsonMessage = new JSONMessage("UpgradeBought", new ErrorObj(errorAlreadyAdminPrivilege));
            server.privateMessage(jsonMessage, clientID);
        };
    }

    /**
     * Handles Cards returned by playing MemorySwap
     * @param server
     * @param task
     * @param bodyObj
     * @author Ilinur
     */

    public void handleReturnCards(Server server, ClientHandler task, ReturnCardsObj bodyObj){
        int ids = server.getClientID(task.getSocket());
        int clientID = ids;
        Player player = server.getPlayer(clientID);
        int i = 0;
        for(String card : bodyObj.getCards()) {
            player.addProgrammingDeck(0, card);
            player.removeFromHand(card);
        }
        String[] cardsInHandReturnCards = new String[player.getCardsInHand().size()];
        for(String newCard : player.getCardsInHand()){
            cardsInHandReturnCards[i] = newCard;
            i++;
        }
        JSONMessage jsonMessageYourCards = new JSONMessage("YourCards", new YourCardsObj(cardsInHandReturnCards));
        server.privateMessage(jsonMessageYourCards, clientID);
    }

    /**
     * Handles all Upgrade cards bought by Client and checks whether bying the card is valid
     * @param server
     * @param task
     * @param bodyObject
     * @author Benedikt
     */
    public void handleBuyUpgrade(Server server, ClientHandler task, BuyUpgradeObj bodyObject){
        int ids = server.getClientID(task.getSocket());
        int clientID = ids;
        Player player = server.getPlayer(clientID);
        String upgradeCard = bodyObject.getCard();
        int priceOfUpgradeCard = server.getPriceOfUpgradeCard(upgradeCard);

        if((server.isUpgradeCardInUpgradeShop(upgradeCard)) && bodyObject.getIsBuying() && player.canAfford(priceOfUpgradeCard)) {
            player.addUpgradeCard(upgradeCard);
            JSONMessage jsonMessageUpgradeBought = new JSONMessage("UpgradeBought", new UpgradeBoughtObj(clientID, upgradeCard));
            server.broadcast(jsonMessageUpgradeBought);
            logger.info(ANSI_CYAN + "player.getEnergyCubes" + player.getEnergyCubes() + ANSI_CYAN);
            // Remove Card from UpgradeShop
            server.removeCardFromUpgradeShop(upgradeCard);
            // Update ChoiceBox of UpgradeShop so only available cards are visible.
            ArrayList<String> upgradeShopList = server.getUpgradeShop();
            String[] upgradeShopArray = new String[upgradeShopList.size()];
            int i = 0;
            for(String card : upgradeShopList){
                upgradeShopArray[i] = card;
                i++;
            }
            JSONMessage jsonMessageUpgradeShop = new JSONMessage("RefillShop", new RefillShopObj(upgradeShopArray));
            server.broadcast(jsonMessageUpgradeShop);
            server.nextPlayer();
        }
        else if(!bodyObject.getIsBuying()){
            String noUpgradeCardBought = "You chose not to buy an upgrade card.";
            JSONMessage jsonMessageUpgradeBought = new JSONMessage("SendChat", new SendChatObj(noUpgradeCardBought, clientID));
            server.privateMessage(jsonMessageUpgradeBought, clientID);
            server.nextPlayer();
        }
        else if(!player.canAfford(priceOfUpgradeCard)){
            String errorTooPoor = "You are too poor for this card.";
            JSONMessage jsonMessage = new JSONMessage("UpgradeBought", new ErrorObj(errorTooPoor));
            server.privateMessage(jsonMessage, clientID);
        }
        else {
            String errorUpgradeCard = "You did not choose a valid upgrade Card.";
            JSONMessage jsonMessage = new JSONMessage("UpgradeBought", new ErrorObj(errorUpgradeCard));
            server.privateMessage(jsonMessage, clientID);
        }
    }


}


