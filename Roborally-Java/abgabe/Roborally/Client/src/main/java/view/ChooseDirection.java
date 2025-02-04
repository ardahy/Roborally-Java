package view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.StartClient;
import utilities.objects.JSONMessage;
import viewModel.Client;

/**
 * this pop up appears when a player is rebooted and needs to select a new position
 * @author Ilinur
 */


public class ChooseDirection {

    private Client client;
    Stage window = new Stage();

    public ChooseDirection() {
        initializeClient();
    }
    public void initializeClient() {
        client = StartClient.getClient();
    }

    public void openChooseDirection() {

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Choose your Direction");


        Label label = new Label();
        label.setText("Choose your new Direction");
        label.setFont(Font.font("Krungthep", 26));
        label.setStyle("-fx-text-fill: white");

        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "top", "bottom", "left", "right")
        );

        cb.setOnAction( e-> selectedDirection((String) cb.getValue()));


        VBox layout = new VBox(50);
        layout.getChildren().addAll(label, cb);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #232324");

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();


    }


    public void selectedDirection(String direction) {

        window.close();
        String chosenDirection = direction;
        client.chosenDirectionReboot(chosenDirection);
    }
}
