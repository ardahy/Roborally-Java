package view;

import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import viewModel.Client;
import main.StartClient;


import javax.script.Bindings;
import java.io.IOException;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.concurrent.locks.ReentrantLock;

/**
 *Controller class for first Scene. Implementation of GUI objects by Ilinur and Rea, Data Bindings by Ilinur,Rea,
 * Arda, Aigerim, Benedikt
 * @author Iinur, Aigerim, Rea, Benedikt
 */
public class ChatView implements Initializable {

    private Client client;
    private int chosenRobot;
    private boolean playerStatus;
    private static String mapSelected = "empty";
    private boolean firstReadyPlayer = true;

    @FXML
    private Text Choose;

    @FXML
    private TextArea chatBox;

    @FXML
    private RadioButton fifth;

    @FXML
    private RadioButton first;

    @FXML
    private RadioButton fourth;

    @FXML
    private Text name;

    @FXML
    private Text name1;

    @FXML
    private Button nameRobotButton;

    @FXML
    private TextField nameRobotField;

    @FXML
    private RadioButton notReadyButton;

    @FXML
    private RadioButton readyButton;

    @FXML
    private Text readyText;

    @FXML
    private Label admin;

    @FXML
    private RadioButton second;

    @FXML
    private Button sendButtonAll;

    @FXML
    private Button sendButtonPrivate;

    @FXML
    private RadioButton sixth;

    @FXML
    private TextField textField;

    @FXML
    private RadioButton third;

    @FXML
    private ToggleGroup status;

    @FXML
    private ToggleGroup robot;

    @FXML
    private Button SendServer;

    @FXML
    private Button changeButton;


    @FXML
    private BorderPane mainPane;

    @FXML
    private ChoiceBox<String> choiceBoxMap;

    @FXML
    private Label chosenMapField;

    @FXML
    private Button selectMapButton;

    private Thread clientThread;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        chosenMapField.setVisible(false);
        choiceBoxMap.setVisible(false);
        selectMapButton.setVisible(false);
        client = new Client();
        StartClient.initializeClient(client);
        clientThread = new Thread(client);
        clientThread.start();
        chatBox.textProperty().bind(client.getMessageHistory());

        // adds all the String elements of the observableArrayList as a option into the choice box
        //choiceBoxMap.getItems().addAll(maps);
        choiceBoxMap.setOnAction(this::getMaps);

        choiceBoxMap.itemsProperty().bind(client.getMapsList());
        choiceBoxMap.visibleProperty().bind(client.showMap());
        selectMapButton.visibleProperty().bind(client.showMap());
        chosenMapField.visibleProperty().bind(client.showMap());


        readyButton.visibleProperty().bind(client.getReadyButtonVisible());
        notReadyButton.visibleProperty().bind(client.getNotReadyButtonVisible());
        readyText.visibleProperty().bind(client.getReadyTextVisible());
        name.textProperty().bind(client.getRenameRobot());
        changeButton.visibleProperty().bind(client.getStartButtonProperty());

        client.getShowScene().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                actionChangeScene();
            }
        });
    }

    // adds the chosen value into the Label
    public void getMaps(ActionEvent event) {
        String myMaps = choiceBoxMap.getValue();
        chosenMapField.setText(myMaps);
    }

    @FXML
    public void actionSendAll() {
        client.sendMessage(textField.getText().trim());
        textField.clear();
    }

    @FXML
    public void actionSendPrivate() {

        String message = textField.getText().trim();
        char id = message.charAt(0);

        if (Character.isDigit(id)) {
            int receiverID = Integer.parseInt(String.valueOf(id));
            message = message.substring(1) + " (p)";
            client.sendPrivateMessage(message, receiverID);
            textField.clear();
        } else {
            client.sendPrivateMessage(message, -2);
        }

    }

    @FXML
    public void actionSendServer() {
        client.messageServer(textField.getText().trim());
        textField.clear();
    }

    @FXML
    void chooseFifth(ActionEvent event) {

        chosenRobot = 5;
        client.setChosenRobot(chosenRobot);

    }

    @FXML
    void chooseFirst(ActionEvent event) {

        chosenRobot = 1;
        client.setChosenRobot(chosenRobot);
    }

    @FXML
    void chooseFourth(ActionEvent event) {

        chosenRobot = 4;
        client.setChosenRobot(chosenRobot);
    }

    @FXML
    void chooseSecond(ActionEvent event) {

        chosenRobot = 2;
        client.setChosenRobot(chosenRobot);
    }

    @FXML
    void chooseSixth(ActionEvent event) {

        chosenRobot = 6;
        client.setChosenRobot(chosenRobot);
    }

    @FXML
    void chooseThird(ActionEvent event) {

        chosenRobot = 3;
        client.setChosenRobot(chosenRobot);
    }


    @FXML
    void nameRobot() {

        client.chooseValues(nameRobotField.getText().trim(), chosenRobot);
        client.setPlayerName(nameRobotField.getText().trim());
        name1.setText(nameRobotField.getText());
        nameRobotField.clear();
        client.setRenameRobot("");
    }

    @FXML
    void nameRobotButton() {

        nameRobot();
    }

    @FXML
    void notReady() {

        client.setStatus(false);
    }

    @FXML
    void ready() {

        client.setStatus(true);
    }


    @FXML
    void notReadyButton() {
        notReady();
    }

    @FXML
    void readyButton() {

        ready();
    }

    @FXML
    public void sendButtonAll() {

        actionSendAll();
    }

    @FXML
    public void sendButtonPrivate() {

        actionSendPrivate();
    }

    @FXML
    public void sendMessagePressed(KeyEvent keyEvent) {

        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            actionSendPrivate();
        }
    }

    @FXML
    public void sendServer() {
        actionSendServer();
    }

    /**
     * changes scene to the selected map
     * @author Benedikt, Ilinur
     */
    @FXML
    public void actionChangeScene() {

        if (client.isGameStarted()) {

            // For the players that did not select the map themselves
            if (mapSelected.equals("empty")) {
                FxmlLoader object = new FxmlLoader();

                switch (client.getMapSelected()) {

                    case "DizzyHighway": {
                        Pane view = object.getPage("dizzyHighway");
                        mainPane.setCenter(view);
                        break;
                    }
                    case "ExtraCrispy": {
                        Pane view = object.getPage("extraCrispy");
                        mainPane.setCenter(view);
                        break;
                    }
                    case "LostBearings": {
                        Pane view = object.getPage("lostBearings");
                        mainPane.setCenter(view);
                        break;
                    }
                    case "DeathTrap": {
                        Pane view = object.getPage("deathTrap");
                        mainPane.setCenter(view);
                        break;
                    }
                    case "Twister": {
                        Pane view = object.getPage("twister");
                        mainPane.setCenter(view);
                        break;
                    }
                    default:
                        break;
                }

                // For the player that has selected the map
            } else {
                FxmlLoader object = new FxmlLoader();

                switch (mapSelected) {

                    case "DizzyHighway": {
                        Pane view = object.getPage("dizzyHighway");
                        mainPane.setCenter(view);
                        break;
                    }
                    case "ExtraCrispy": {
                        Pane view = object.getPage("extraCrispy");
                        mainPane.setCenter(view);
                        break;
                    }
                    case "LostBearings": {
                        Pane view = object.getPage("lostBearings");
                        mainPane.setCenter(view);
                        break;
                    }
                    case "DeathTrap": {
                        Pane view = object.getPage("deathTrap");
                        mainPane.setCenter(view);
                        break;
                    }
                    case "Twister": {
                        Pane view = object.getPage("twister");
                        mainPane.setCenter(view);
                        break;
                    }
                    default:
                        break;
                }
            }

        }else{
            String error = "The game has not started yet.";
            Platform.runLater(()-> {
                ErrorPopUp popup = new ErrorPopUp();
                popup.openError(error);
            });
        }
    }

    /**
     * gets the chosen element from choiceBox and set's it as the selected map
     */
    @FXML
    private void handleButtonMapSelected(ActionEvent event) {
        mapSelected = choiceBoxMap.getSelectionModel().getSelectedItem();
        client.mapSelected(mapSelected);
    }

}
