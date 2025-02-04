package view;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.StartClient;
import viewModel.Client;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

public class DamageCardPopUp {

    private Client client;
    public ArrayList<String> selectedCards = new ArrayList<>();

    Stage window = new  Stage();

    int column = 0;


    public DamageCardPopUp(){
        initializeClient();
    }

    public void initializeClient(){
        client = StartClient.getClient();
    }

    public CheckBox checkBox;
    public CheckBox checkBox2;
    public Button button;


    /**
     * this pop up appears when the spam deck is empty and the player needs to choose other damage cards
     * @author Rea
     */
    public void openDamageCardPopUp(){

        ObservableList<String> cards = client.getAvailableDamageCards();
        cards.remove("Null");

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Damage Cards");


        GridPane grid = new GridPane();
        //damageCards.setWidth(650);
        //damageCards.setHeight(400);

        for(String card : cards){
            checkBox = new CheckBox(card);
            checkBox2 = new CheckBox(card);
            //Button button = new Button();
            //button.setId(card);
            //button.setOnAction(e -> selectedCard(button));

            String pathToImage = getClass().getResource("/images/" + card + ".png").toExternalForm();

            Image img = new Image(pathToImage);
            ImageView view = new ImageView(img);
            ImageView view2 = new ImageView(img);
            view.setFitHeight(90);
            view.setFitWidth(60);
            view2.setFitHeight(90);
            view2.setFitWidth(60);
            grid.setHgap(10);
            //view.setPreserveRatio(true);

            //button.setGraphic(view);
            selectionListener(checkBox, card);


            //grid.add(checkBox2, column ++, 1);
            //grid.add(view2, column ++, 0);

            grid.add(view, column ++, 0);
            grid.add(checkBox, column++, 1);


        }

        Button button = new Button();
        button.setText("OK");
        button.setOnAction(e -> selectedCard());

        Label label = new Label();
        label.setText("Choose two damage Cards");
        label.setFont(Font.font ("Krungthep", 26));
        label.setStyle("-fx-text-fill: white");


        VBox layout = new VBox(50);
        layout.getChildren().addAll(label, grid, button);
        layout.setAlignment(Pos.CENTER);
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

        if(selectedCards.size() < 2 || selectedCards.size() > 2){
            Platform.runLater(() -> {
                ErrorPopUp popup = new ErrorPopUp();
                popup.openError("Please select exactly 2 cards");
            });
        }
        else {
            window.close();
        }
        String [] chosenCards = selectedCards.toArray(new String[0]);
        client.returnPickDamage(chosenCards);


    }


}



