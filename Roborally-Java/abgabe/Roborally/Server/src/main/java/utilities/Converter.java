package utilities;

import model.field.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import utilities.objects.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Converts all String messages into JSON Messages and also the other way around
 */
public class Converter {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger(Converter.class.getName());
    /**
     * Converts JSON Messages into String by using a GSON Builder
     * @param jsonMessage The json Message which is converted into a String message
     * @return A String message is returned
     * @author Aigerim
     */
    public static String serializeJSON(JSONMessage jsonMessage) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(jsonMessage);
        return jsonString;
    }

    /**
     * Converts String messages into JSON Messages using GSON Builder
     * @param jsonMessage The String Message which is converted into a JSON message
     * @return A JSON Message is returned
     * @author Aigerim
     */
    public static JSONMessage deserializeJSON(String jsonMessage) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(JSONMessage.class, deserializer);
        Gson customGson = gsonBuilder.create();
        JSONMessage obj = customGson.fromJson(jsonMessage, JSONMessage.class);
        return obj;
    }

    /**
     * Converts JSON messages into java objects
     * @author  Aigerim, Benedikt, Ilinur, Rea, Arda
     */
    public static JsonDeserializer<JSONMessage> deserializer = new JsonDeserializer<JSONMessage>() {
        @Override
        public JSONMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationMessage) throws JsonParseException {
            // Get the overall JSON String with type and body
            JsonObject jsonMessage = jsonElement.getAsJsonObject();

            // Get only the messageBody part of the JSON String so we can access its variables
            JsonObject messageBody = jsonMessage.get("messageBody").getAsJsonObject();

            // Get the messageType of the JSON String
            String messageType = jsonMessage.get("messageType").getAsString();

            Gson tileParser = new GsonBuilder()
                    .registerTypeAdapter(Field.class, fieldDeserializer)
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();

            switch (messageType) {

                case "SendChat":
                    SendChatObj sendChatBody = new SendChatObj(
                            messageBody.get("message").getAsString(),
                            messageBody.get("to").getAsInt()
                    );
                    return new JSONMessage("SendChat", sendChatBody);

                case "HelloServer":
                    HelloServerObj helloServerObj = new HelloServerObj(
                            messageBody.get("group").getAsString(),
                            messageBody.get("isAI").getAsBoolean(),
                            messageBody.get("protocol").getAsString()
                    );
                    return new JSONMessage("HelloServer", helloServerObj);

                case "PlayerValues":
                    PlayerValuesObj playerValuesObj = new PlayerValuesObj(
                            messageBody.get("name").getAsString(),
                            messageBody.get("figure").getAsInt()
                            );
                    return new JSONMessage("PlayerValues", playerValuesObj);

                case "PlayerAdded":
                    PlayerAddedObj playerAddedObj = new PlayerAddedObj(
                            messageBody.get("clientID").getAsInt(),
                            messageBody.get("name").getAsString(),
                            messageBody.get("figure").getAsInt()
                    );
                    return new JSONMessage("PlayerAdded", playerAddedObj);

                case "Welcome":
                    WelcomeObj welcomeObj = new WelcomeObj(
                            messageBody.get("clientID").getAsInt()
                    );
                    return new JSONMessage("Welcome", welcomeObj);

                case "SetStatus":
                    SetStatusObj setStatusObj = new SetStatusObj(
                            messageBody.get("ready").getAsBoolean()
                    );
                    return new JSONMessage("SetStatus", setStatusObj);

                case "SelectMap":
                    SelectMapObj selectMapObj = new SelectMapObj(
                            new String[]{messageBody.get("availableMaps").getAsString()}
                    );
                    return new JSONMessage("SelectMap", selectMapObj);

                case "PlayCard":
                    PlayCardObj playCardObj = new PlayCardObj(
                            messageBody.get("card").getAsString()
                    );
                    return new JSONMessage("PlayCard", playCardObj);

                case "CardPlayed":
                    CardPlayedObj cardPlayedObj = new CardPlayedObj(
                            messageBody.get("clientID").getAsInt(),
                            messageBody.get("card").getAsString()
                    );
                    return new JSONMessage("CardPlayed", cardPlayedObj);

                case "NotYourCards":
                    NotYourCardsObj notYourCardsObj = new NotYourCardsObj(
                            messageBody.get("clientID").getAsInt(),
                            messageBody.get("cardsInHand").getAsInt()
                    );
                    return new JSONMessage("NotYourCards", notYourCardsObj);

                case "SelectedCard":
                    SelectedCardObj selectedCardObj = new SelectedCardObj(
                            messageBody.get("card").getAsString(),
                            messageBody.get("register").getAsInt()
                    );
                    return new JSONMessage("SelectedCard", selectedCardObj);

                case "CardSelected":
                    CardSelectedObj cardSelectedObj = new CardSelectedObj(
                            messageBody.get("clientID").getAsInt(),
                            messageBody.get("register").getAsInt(),
                            messageBody.get("filled").getAsBoolean()
                    );
                    return new JSONMessage("CardSelected", cardSelectedObj);

                case "SelectionFinished":
                    SelectionFinishedObj selectionFinishedObj = new SelectionFinishedObj(
                            messageBody.get("clientID").getAsInt()
                    );
                    return new JSONMessage("SelectionFinished", selectionFinishedObj);

                case "Error":
                    ErrorObj errorObj = new ErrorObj(
                            messageBody.get("error").getAsString()
                    );
                    return new JSONMessage("Error", errorObj);

                case "MapSelected":
                    MapSelectedObj mapSelectedObj = new MapSelectedObj(
                            messageBody.get("map").getAsString()
                    );
                    return new JSONMessage("MapSelected", mapSelectedObj);

                case "StartingPointTaken":
                    StartingPointTakenObj startingPointTakenObj = new StartingPointTakenObj(
                            messageBody.get("x").getAsInt(),
                            messageBody.get("y").getAsInt(),
                            messageBody.get("clientID").getAsInt()
                    );
                    return new JSONMessage("StartingPointTaken", startingPointTakenObj);

                case "SetStartingPoint":
                    SetStartingPointObj setStartingPointObj = new SetStartingPointObj(
                            messageBody.get("x").getAsInt(),
                            messageBody.get("y").getAsInt()
                    );
                    return new JSONMessage("SetStartingPoint", setStartingPointObj);

                case "Alive":
                    AliveObj aliveObjM = new AliveObj();
                    return new JSONMessage("Alive", aliveObjM);

                case "GameStarted":

                    JsonElement mapArray = messageBody.get("gameMap").getAsJsonArray();
                    Type types = new TypeToken<ArrayList<ArrayList<ArrayList<Field>>>>() {
                    }.getType();

                    ArrayList<ArrayList<ArrayList<Field>>> bodyMap = tileParser.fromJson(mapArray, types);

                    logger.info(ANSI_GREEN + "( JSONDECODER ): MAP SIZE CALCULATION FINISHED: " + bodyMap.size() * bodyMap.get(0).size() + " FIELDS." + ANSI_RESET + "\n");

                    GameStartedObj gameStartedBody = new GameStartedObj(
                            bodyMap
                    );
                    return new JSONMessage("GameStarted", gameStartedBody);

                case "GameFinished":
                    GameFinishedObj gameFinishedObj = new GameFinishedObj(
                            messageBody.get("clientID").getAsInt()
                    );
                    return new JSONMessage("GameFinished", gameFinishedObj);

                case "ConnectionUpdate":

                    ConnectionUpdateObj connectionUpdateObj = new ConnectionUpdateObj(
                            messageBody.get("clientID").getAsInt(),
                            messageBody.get("isConnected").getAsBoolean(),
                            messageBody.get("action").getAsString()
                    );
                    return new JSONMessage("ConnectionUpdate", connectionUpdateObj);

                case "DrawDamage":

                    JsonArray damageCardsArray;

                    damageCardsArray = messageBody.get("cards").getAsJsonArray();
                    String[] damageCards = new String[damageCardsArray.size()];
                    for (int i = 0; i < damageCardsArray.size(); i++) {
                        damageCards[i] = damageCardsArray.get(i).getAsString();
                    }

                    DrawDamageObj drawDamageObj = new DrawDamageObj(
                            messageBody.get("clientID").getAsInt(),
                            damageCards
                    );

                    return new JSONMessage("DrawDamage", drawDamageObj);

                case "PickDamage":

                    JsonArray pickDamageArray;

                    pickDamageArray = messageBody.get("availablePiles").getAsJsonArray();
                    String[] pickDamage = new String[pickDamageArray.size()];
                    for(int i = 0; i < pickDamageArray.size(); i++){
                        pickDamage[i] = pickDamageArray.get(i).getAsString();
                    }

                    PickDamageObj pickDamageObj = new PickDamageObj(
                            messageBody.get("count").getAsInt(),
                            pickDamage
                    );

                    return new JSONMessage("PickDamage", pickDamageObj);

                case "SelectedDamage":

                    JsonArray selectDamageArray;

                    selectDamageArray = messageBody.get("cards").getAsJsonArray();
                    String[] selectDamage = new String[selectDamageArray.size()];
                    for (int i = 0; i < selectDamageArray.size(); i++){
                        selectDamage[i] = selectDamageArray.get(i).getAsString();
                    }

                    SelectedDamageObj selectedDamageObj = new SelectedDamageObj(
                            selectDamage
                    );

                    return new JSONMessage("SelectedDamage", selectedDamageObj);


                case "TimerEnded":
                    JsonArray lateClientsArray;

                    lateClientsArray = messageBody.get("clientIDs").getAsJsonArray();
                    int[] lateClients = new int[lateClientsArray.size()];
                    for (int i = 0; i < lateClientsArray.size(); i++) {
                        lateClients[i] = lateClientsArray.get(i).getAsInt();
                    }

                    TimerEndedObj timerEndedObj = new TimerEndedObj(
                            lateClients
                    );
                    return new JSONMessage("TimerEnded", timerEndedObj);

                case "BuyUpgrade":
                    BuyUpgradeObj buyUpgradeObj = new BuyUpgradeObj(
                            messageBody.get("isBuying").getAsBoolean(),
                            messageBody.get("card").getAsString()
                    );
                    return new JSONMessage("BuyUpgrade", buyUpgradeObj);

                case "RebootDirection":
                    RebootDirectionObj rebootDirectionObj = new RebootDirectionObj(
                            messageBody.get("direction").getAsString()
                    );
                    return new JSONMessage("RebootDirection", rebootDirectionObj);

                case "ChooseRegister":
                    ChooseRegisterObj chooseRegisterObj = new ChooseRegisterObj(
                            messageBody.get("register").getAsInt()

                    );
                    return new JSONMessage("ChooseRegister", chooseRegisterObj);

                case "ReturnCards":
                    JsonArray returnCards;

                    returnCards = messageBody.get("cards").getAsJsonArray();
                    String[] selectedCards = new String[returnCards.size()];
                    for (int i = 0; i < returnCards.size(); i++) {
                        selectedCards[i] = returnCards.get(i).getAsString();
                    }

                    ReturnCardsObj returnCardsObj = new ReturnCardsObj(
                            selectedCards
                    );
                    return new JSONMessage("ReturnCards", returnCardsObj);


                case "CheckpointMoved":
                    CheckpointMovedObj checkpointMovedObj = new CheckpointMovedObj(
                            messageBody.get("checkpointID").getAsInt(),
                            messageBody.get("x").getAsInt(),
                            messageBody.get("y").getAsInt()

                    );
                    return new JSONMessage("CheckpointMoved", checkpointMovedObj);
                case "ReplaceCard":
                    ReplaceCardObj replaceCardObj= new ReplaceCardObj(
                            messageBody.get("register").getAsInt(),
                            messageBody.get("newCard").getAsString(),
                            messageBody.get("clientID").getAsInt()

                    );
                    return new JSONMessage("ReplaceCard", replaceCardObj);


                default:
                    return null;


            }

        }

    };

    /**
     * Converts JSON messages into java field's objects
     * @author  Aigerim Abdykadyrova
     */
    public static JsonDeserializer<Field> fieldDeserializer = new JsonDeserializer<Field>() {
        @Override
        public Field deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject tileObject = jsonElement.getAsJsonObject();
            String tileType = tileObject.get("type").getAsString();

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            switch (tileType) {
                case "ConveyorBelt":
                    ConveyorBelt result8 = gson.fromJson(jsonElement, ConveyorBelt.class);
                    return result8;

                case "RestartPoint":
                    RestartPoint result23 = gson.fromJson(jsonElement, RestartPoint.class);
                    return result23;

                case "Antenna":
                    Antenna result1 = gson.fromJson(jsonElement, Antenna.class);
                    return result1;

                case "Empty":
                    Empty result2 = gson.fromJson(jsonElement, Empty.class);
                    return result2;

                case "EnergySpace":
                    EnergySpace result3 = gson.fromJson(jsonElement, EnergySpace.class);
                    return result3;

                case "Laser":
                    Laser result4 = gson.fromJson(jsonElement, Laser.class);
                    return result4;

                case "RotatingBelt":
                    ConveyorBelt result5 = gson.fromJson(jsonElement, ConveyorBelt.class);
                    return result5;

                case "StartPoint":
                    StartPoint result6 = gson.fromJson(jsonElement, StartPoint.class);
                    return result6;

                case "Wall":
                    Wall result7 = gson.fromJson(jsonElement, Wall.class);
                    return result7;

                case "CheckPoint":
                    CheckPoint result9 = gson.fromJson(jsonElement, CheckPoint.class);
                    return result9;

                case "Pit":
                    Pit result10 = gson.fromJson(jsonElement, Pit.class);
                    return result10;
                case "PushPanel":
                    PushPanel result11 = gson.fromJson(jsonElement, PushPanel.class);
                    return result11;
                case "Gear":
                    Gear result12 = gson.fromJson(jsonElement, Gear.class);
                    return result12;
                default:
                    return null;
            }
        }
    };
}
