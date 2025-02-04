package view;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameFinishedLoser {

    public String robotColor;
    ImageView view;
    ImageView viewS;
    /**
     * this pop that appears at end of the game is for the loser
     * @param name loser name
     * @param robotID loser robot ID
     * @author Ilinur
     */
    public void openGameFinishedLoserPopUp(String name, int robotID){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Game Finished");
        window.setMinWidth(500);
        window.setMinHeight(600);

        Label label = new Label();
        label.setText(name + " you have lost" );
        label.setFont(Font.font ("Krungthep", FontWeight.BOLD, 30));
        label.setStyle("-fx-text-fill: white");

        getRobotColor(robotID);
        String pathToImage = getClass().getResource("/images/robo" + robotColor + ".png").toExternalForm();
        String pathToImageS = getClass().getResource("/images/gameOver.png").toExternalForm();

        Image img = new Image(pathToImage);
        view = new ImageView(img);
        view.setFitWidth(200);
        view.setFitHeight(300);
        view.setLayoutX(150);
        view.setLayoutY(70);

        Image imgS = new Image(pathToImageS);
        viewS = new ImageView(imgS);
        viewS.setFitWidth(200);
        viewS.setFitHeight(300);
        viewS.setLayoutX(150);
        viewS.setLayoutY(-80);

        Label endLabel = new Label();
        endLabel.setText("If you want to start a new game please restart the program");
        Label endLabel2 = new Label();
        //endLabel2.setText( winnerName + " has won the game");
        endLabel.setFont(Font.font ("Krungthep", 20));
        endLabel.setStyle("-fx-text-fill: white");
        endLabel2.setFont(Font.font ("Krungthep", 20));
        endLabel2.setStyle("-fx-text-fill: white");

        Pane stack = new Pane();
        stack.getChildren().addAll(viewS, view);
        VBox layout = new VBox(50);
        layout.getChildren().addAll(label, stack, endLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #232324");
        layout.setPadding(new Insets(20, 20, 20, 20));
        animation();
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public String getRobotColor (int id){
        switch(id){
            case 1:
                return robotColor = "Blue";
            case 2:
                return robotColor = "Red";
            case 3:
                return robotColor = "Purple";
            case 4:
                return robotColor = "Green";
            case 5:
                return robotColor = "Orange";
            case 6:
                return robotColor = "Yellow";
            default:
                return robotColor = "false";
        }
    }

    public void animation() {
        ScaleTransition st = new ScaleTransition();
        st.setNode(viewS);
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(1.5);
        st.setToY(1.5);
        st.setDuration(Duration.seconds(4));
        st.setCycleCount(ScaleTransition.INDEFINITE);
        st.play();
    }

}
