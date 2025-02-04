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

public class GameFinished {

    public String robotColor;
    ImageView view;
    ImageView viewS;

    /**
     * this pop that appears at end of the game is for the winner
     * @param name winner name
     * @param robotID winner robot ID
     * @author Ilinur
     */
    public void openGameFinishedPopUp(String name, int robotID){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Game Finished");
        window.setMinWidth(500);
        window.setMinHeight(600);

        Label label = new Label();
        label.setText(name + " you have won" );
        label.setFont(Font.font ("Krungthep", FontWeight.BOLD, 30));
        label.setStyle("-fx-text-fill: white");

        getRobotColor(robotID);
        String pathToImage = getClass().getResource("/images/robo" + robotColor + ".png").toExternalForm();
        String pathToImageS = getClass().getResource("/images/star.png").toExternalForm();

        Image img = new Image(pathToImage);
        view = new ImageView(img);
        view.setFitWidth(200);
        view.setFitHeight(300);
        view.setLayoutX(170);
        view.setLayoutY(50);

        Image imgS = new Image(pathToImageS);
        viewS = new ImageView(imgS);
        viewS.setFitWidth(200);
        viewS.setFitHeight(300);
        viewS.setLayoutX(170);
        viewS.setLayoutY(50);

        Label endLabel = new Label();
        endLabel.setText("If you want to start a new game please restart the program");
        endLabel.setFont(Font.font ("Krungthep", 20));
        endLabel.setStyle("-fx-text-fill: white");

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
