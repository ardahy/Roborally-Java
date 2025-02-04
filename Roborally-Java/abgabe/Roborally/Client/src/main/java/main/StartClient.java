package main;

import viewModel.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Start Client Application
 * @author Aigerim
 */
public class StartClient extends Application {
    public static Client client;

    /**
     * start Client GUI
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        try{

            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("chatView.fxml"));
            primaryStage.setTitle("RoboRally Chat");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            primaryStage.show();
        }

        catch (IOException e) {

            e.printStackTrace();

        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * initialize the client
     * @param c
     */
    public static void initializeClient(Client c){
        client = c;
    }

    /**
     * get client
     * @return
     */
    public static Client getClient(){
        return client;
    }



}

