package view;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.StartClient;
import viewModel.Client;
import javafx.scene.control.CheckBox;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * a popup opens when an MemorySwap is played
 * @author Ilinur
 */


public class MemorySwap {

    private Client client;
    public ArrayList<String> selectedCards = new ArrayList<>();

    Stage window = new Stage();

    int column = 0;


    public MemorySwap() {
        initializeClient();
    }

    public void initializeClient() {
        client = StartClient.getClient();
    }

    public CheckBox checkBox;
    public Button button;


    public void openMemorySwapPopUp(Client client) {

        ObservableList<String> cards = client.getYourCardsInHand();
        cards.remove("Null");

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Memory Swap");

        //StringBuilder builder = new StringBuilder("Selected items :");
        GridPane grid = new GridPane();
        ;
        for(String card : cards) {
            checkBox = new CheckBox(card);
            //checkBox.getChildren().add(new Label (card));
            String pathToImage = getClass().getResource("/images/" + card + ".png").toExternalForm();
            Image img = new Image(pathToImage);
            ImageView view = new ImageView(img);
            view.setFitHeight(90);
            view.setFitWidth(60);
            grid.setHgap(10);
            selectionListener(checkBox, card);
            view.setLayoutX(50);
            grid.add(view, column++, 0);
            grid.add(checkBox, column++, 0);

            checkBox.setStyle(
                    "-fx-text-size: 1;"
                            + "-fx-text-fill: #232324"
            );

        }

        Button button = new Button();
        button.setText("OK");
        button.setOnAction(e -> selectedCard());

        Label labelTitel = new Label();
        labelTitel.setText("Choose three cards to put on top of your deck");
        labelTitel.setFont(Font.font("Krungthep",26));
        labelTitel.setStyle("-fx-text-fill: white");

        VBox layout = new VBox(50);
        layout.getChildren().addAll(labelTitel, grid, button);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setStyle("-fx-background-color: #232324");

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();


    }

    public void selectionListener(CheckBox checkBox, String name){
        checkBox.selectedProperty().addListener(((observableValue, selected, current) -> {
            if(current){
                selectedCards.add(name);
            }
            else{
                selectedCards.remove(name);

            }
        }
        ));
    }

    public void selectedCard(){

        if(selectedCards.size() < 3 ){
            Platform.runLater(() -> {
                ErrorPopUp popup = new ErrorPopUp();
                popup.openError("Please select 3 cards");
            });
        }
        else if(selectedCards.size() > 3){
            Platform.runLater(() -> {
                ErrorPopUp popup = new ErrorPopUp();
                popup.openError("Please do not select more than 3 cards");
            });
        }
        else {
            window.close();
        }
        String [] chosenCards = selectedCards.toArray(new String[0]);
        client.returnCardsMemorySwap(chosenCards);
    }

}

