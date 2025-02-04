package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.StartClient;
import viewModel.Client;

/**
 * a popup opens when an AdminPrivilege is played
 * @author Ilinur
 */

public class AdminPrivilege {

    private Client client;


    Stage window = new  Stage();

    int column = 0;

    String[] register = {"0", "1", "2", "3", "4"};

    public AdminPrivilege(){
        initializeClient();
    }

    public void initializeClient(){
        client = StartClient.getClient();
    }


    public void openAdminPrivilegePopUp(){

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("AdminPrivilege");

        GridPane grid = new GridPane();
        for(String card : register){
            Button button = new Button();
            button.setId(card);
            button.setStyle("-fx-background-color: #232324");
            button.setOnAction(e -> selectedCard(button.getId()));

            String pathToImage = getClass().getResource("/images/" + card + ".png").toExternalForm();

            Image img = new Image(pathToImage);
            ImageView view = new ImageView(img);
            view.setFitWidth(60);
            view.setFitHeight(60);
            view.setPreserveRatio(true);
            button.setGraphic(view);
            grid.add(button, column ++, 0);
            grid.setHgap(10);
            grid.setLayoutX(80);
        }

        Label label = new Label();
        label.setText("Choose in which register you want to use the priority");
        label.setFont(Font.font ("Krungthep", 26));
        label.setStyle("-fx-text-fill: white");

        Pane stack = new Pane();
        stack.getChildren().addAll(grid);
        VBox layout = new VBox(50);
        layout.getChildren().addAll(label, stack);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER_RIGHT);
        layout.setStyle("-fx-background-color: #232324");

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public void selectedCard(String register){

        window.close();
        int chosenRegister = Integer.parseInt(register);
        client.adminPrivilegeRegister(chosenRegister);
    }

}




