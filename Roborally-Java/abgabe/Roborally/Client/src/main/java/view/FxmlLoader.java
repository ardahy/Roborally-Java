package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import main.StartClient;

import java.net.URL;

/**
 * this method is called when a new scene needs to be loaded
 * @author Ilinur
 */

public class FxmlLoader {

    private Pane view;

    public Pane getPage(String fileName){
        try{
            URL fileUrl = StartClient.class.getResource("/" + fileName + ".fxml");

            if(fileUrl == null) {
                throw new java.io.FileNotFoundException("File does not exist");
            }

            view = new FXMLLoader().load(fileUrl);

        } catch (Exception e){
        } return view;
    }
}
