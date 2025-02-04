package view;

import com.google.gson.internal.bind.util.ISO8601Utils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.StartClient;
import model.TimerForGame;
import viewModel.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

import javafx.beans.value.ChangeListener;

import java.net.URL;
import java.util.*;
/**
 *Controller class for Map Extra Crispy. Implementation of GUI objects by Ilinur und Rea
 *Data Bindings by Ilinur, Rea, Arda, Aigerim, Benedikt
 * @author Iinur, Arda, Aigerim, Rea, Benedikt
 **/

public class ExtraCrispy implements Initializable {

        private Client client;
        private ObservableList<Node> gridPaneChildren = FXCollections.observableArrayList();
        private ObservableList<String> previousMovements = FXCollections.observableArrayList();
        private ObservableList<String> currentPositions = FXCollections.observableArrayList();
        private ObservableList<Node> childrenVisible = FXCollections.observableArrayList();
        private ObservableList<Node> gridPaneC = FXCollections.observableArrayList();
        private ObservableList<Node> gridPaneChild = FXCollections.observableArrayList();
        private ObservableList<Node> gridPane1 = FXCollections.observableArrayList();
        private ObservableList<Node> gridPane2 = FXCollections.observableArrayList();
        private ObservableList<Node> gridPane3 = FXCollections.observableArrayList();
        private ObservableList<Node> gridPane4 = FXCollections.observableArrayList();
        private ObservableList<Node> gridPane5 = FXCollections.observableArrayList();
        private ObservableList<Node> gridPane6 = FXCollections.observableArrayList();

        @FXML
        private Label labelRegister0, labelRegister1, labelRegister2, labelRegister3, labelRegister4;

        @FXML
        private ImageView blue_0_0, blue_0_1, blue_0_2, blue_0_3, blue_0_4, blue_0_5, blue_0_6, blue_0_7, blue_0_8, blue_0_9;

        @FXML
        private ImageView blue_10_0, blue_10_1, blue_10_2, blue_10_3, blue_10_4, blue_10_5, blue_10_6, blue_10_7, blue_10_8, blue_10_9;

        @FXML
        private ImageView blue_11_0, blue_11_1, blue_11_2, blue_11_3, blue_11_4, blue_11_5, blue_11_6, blue_11_7, blue_11_8, blue_11_9;

        @FXML
        private ImageView blue_12_0, blue_12_1, blue_12_2, blue_12_3, blue_12_4, blue_12_5, blue_12_6, blue_12_7, blue_12_8, blue_12_9;

        @FXML
        private ImageView blue_1_0, blue_1_1, blue_1_2, blue_1_3, blue_1_4, blue_1_5, blue_1_6, blue_1_7, blue_1_8, blue_1_9;

        @FXML
        private ImageView blue_2_0, blue_2_1, blue_2_2, blue_2_3, blue_2_4, blue_2_5, blue_2_6, blue_2_7, blue_2_8, blue_2_9;

        @FXML
        private ImageView blue_3_0, blue_3_1, blue_3_2, blue_3_3, blue_3_4, blue_3_5, blue_3_6, blue_3_7, blue_3_8, blue_3_9;

        @FXML
        private ImageView blue_4_0, blue_4_1, blue_4_2, blue_4_3, blue_4_4, blue_4_5, blue_4_6, blue_4_7, blue_4_8, blue_4_9;

        @FXML
        private ImageView blue_5_0, blue_5_1, blue_5_2, blue_5_3, blue_5_4, blue_5_5, blue_5_6, blue_5_7, blue_5_8, blue_5_9;

        @FXML
        private ImageView blue_6_0, blue_6_1, blue_6_2, blue_6_3, blue_6_4, blue_6_5, blue_6_6, blue_6_7, blue_6_8, blue_6_9;

        @FXML
        private ImageView blue_7_0, blue_7_1, blue_7_2, blue_7_3, blue_7_4, blue_7_5, blue_7_6, blue_7_7, blue_7_8, blue_7_9;

        @FXML
        private ImageView blue_8_0, blue_8_1, blue_8_2, blue_8_3, blue_8_4, blue_8_5, blue_8_6, blue_8_7, blue_8_8, blue_8_9;

        @FXML
        private ImageView blue_9_0, blue_9_1, blue_9_2, blue_9_3, blue_9_4, blue_9_5, blue_9_6, blue_9_7, blue_9_8, blue_9_9;

        @FXML
        private ImageView green_0_0, green_0_1, green_0_2, green_0_3, green_0_4, green_0_5, green_0_6, green_0_7, green_0_8, green_0_9;

        @FXML
        private ImageView green_10_0, green_10_1, green_10_2, green_10_3, green_10_4, green_10_5, green_10_6, green_10_7, green_10_8, green_10_9;

        @FXML
        private ImageView green_11_0, green_11_1, green_11_2, green_11_3, green_11_4, green_11_5, green_11_6, green_11_7, green_11_8, green_11_9;

        @FXML
        private ImageView green_12_0, green_12_1, green_12_2, green_12_3, green_12_4, green_12_5, green_12_6, green_12_7, green_12_8, green_12_9;

        @FXML
        private ImageView green_1_0, green_1_1, green_1_2, green_1_3, green_1_4, green_1_5, green_1_6, green_1_7, green_1_8, green_1_9;

        @FXML
        private ImageView green_2_0, green_2_1, green_2_2, green_2_3, green_2_4, green_2_5, green_2_6, green_2_7, green_2_8, green_2_9;

        @FXML
        private ImageView green_3_0, green_3_1, green_3_, green_3_3, green_3_4, green_3_5, green_3_6, green_3_7, green_3_8, green_3_9;

        @FXML
        private ImageView green_4_0, green_4_1, green_4_2, green_4_3, green_4_4, green_4_5, green_4_6, green_4_7, green_4_8, green_4_9;

        @FXML
        private ImageView green_5_0, green_5_1, green_5_2, green_5_3, green_5_4, green_5_5, green_5_6, green_5_7, green_5_8, green_5_9;

        @FXML
        private ImageView green_6_0, green_6_1, green_6_2, green_6_3, green_6_4, green_6_5, green_6_6, green_6_7, green_6_8, green_6_9;

        @FXML
        private ImageView green_7_0, green_7_1, green_7_2, green_7_3, green_7_4, green_7_5, green_7_6, green_7_7, green_7_8, green_7_9;

        @FXML
        private ImageView green_8_0, green_8_1, green_8_2, green_8_3, green_8_4, green_8_5, green_8_6, green_8_7, green_8_8, green_8_9;

        @FXML
        private ImageView green_9_0;

        @FXML
        private ImageView green_9_1;

        @FXML
        private ImageView green_9_2;

        @FXML
        private ImageView green_9_3;

        @FXML
        private ImageView green_9_4;

        @FXML
        private ImageView green_9_5;

        @FXML
        private ImageView green_9_6;

        @FXML
        private ImageView green_9_7;

        @FXML
        private ImageView green_9_8;

        @FXML
        private ImageView green_9_9;

        @FXML
        private ImageView orange_0_0;

        @FXML
        private ImageView orange_0_1;

        @FXML
        private ImageView orange_0_2;

        @FXML
        private ImageView orange_0_3;

        @FXML
        private ImageView orange_0_4;

        @FXML
        private ImageView orange_0_5;

        @FXML
        private ImageView orange_0_6;

        @FXML
        private ImageView orange_0_7;

        @FXML
        private ImageView orange_0_8;

        @FXML
        private ImageView orange_0_9;

        @FXML
        private ImageView orange_10_0;

        @FXML
        private ImageView orange_10_1;

        @FXML
        private ImageView orange_10_2;

        @FXML
        private ImageView orange_10_3;

        @FXML
        private ImageView orange_10_4;

        @FXML
        private ImageView orange_10_5;

        @FXML
        private ImageView orange_10_6;

        @FXML
        private ImageView orange_10_7;

        @FXML
        private ImageView orange_10_8;

        @FXML
        private ImageView orange_10_9;

        @FXML
        private ImageView orange_11_0;

        @FXML
        private ImageView orange_11_1;

        @FXML
        private ImageView orange_11_2;

        @FXML
        private ImageView orange_11_3;

        @FXML
        private ImageView orange_11_4;

        @FXML
        private ImageView orange_11_5;

        @FXML
        private ImageView orange_11_6;

        @FXML
        private ImageView orange_11_7;

        @FXML
        private ImageView orange_11_8;

        @FXML
        private ImageView orange_11_9;

        @FXML
        private ImageView orange_12_0;

        @FXML
        private ImageView orange_12_1;

        @FXML
        private ImageView orange_12_2;

        @FXML
        private ImageView orange_12_3;

        @FXML
        private ImageView orange_12_4;

        @FXML
        private ImageView orange_12_5;

        @FXML
        private ImageView orange_12_6;

        @FXML
        private ImageView orange_12_7;

        @FXML
        private ImageView orange_12_8;

        @FXML
        private ImageView orange_12_9;

        @FXML
        private ImageView orange_1_0;

        @FXML
        private ImageView orange_1_1;

        @FXML
        private ImageView orange_1_2;

        @FXML
        private ImageView orange_1_3;

        @FXML
        private ImageView orange_1_4;

        @FXML
        private ImageView orange_1_5;

        @FXML
        private ImageView orange_1_6;

        @FXML
        private ImageView orange_1_7;

        @FXML
        private ImageView orange_1_8;

        @FXML
        private ImageView orange_1_9;

        @FXML
        private ImageView orange_2_0;

        @FXML
        private ImageView orange_2_1;

        @FXML
        private ImageView orange_2_2;

        @FXML
        private ImageView orange_2_3;

        @FXML
        private ImageView orange_2_4;

        @FXML
        private ImageView orange_2_5;

        @FXML
        private ImageView orange_2_6;

        @FXML
        private ImageView orange_2_7;

        @FXML
        private ImageView orange_2_8;

        @FXML
        private ImageView orange_2_9;

        @FXML
        private ImageView orange_3_0;

        @FXML
        private ImageView orange_3_1;

        @FXML
        private ImageView orange_3_2;

        @FXML
        private ImageView orange_3_3;

        @FXML
        private ImageView orange_3_4;

        @FXML
        private ImageView orange_3_5;

        @FXML
        private ImageView orange_3_6;

        @FXML
        private ImageView orange_3_7;

        @FXML
        private ImageView orange_3_8;

        @FXML
        private ImageView orange_3_9;

        @FXML
        private ImageView orange_4_0;

        @FXML
        private ImageView orange_4_1;

        @FXML
        private ImageView orange_4_2;

        @FXML
        private ImageView orange_4_3;

        @FXML
        private ImageView orange_4_4;

        @FXML
        private ImageView orange_4_5;

        @FXML
        private ImageView orange_4_6;

        @FXML
        private ImageView orange_4_7;

        @FXML
        private ImageView orange_4_8;

        @FXML
        private ImageView orange_4_9;

        @FXML
        private ImageView orange_5_0;

        @FXML
        private ImageView orange_5_1;

        @FXML
        private ImageView orange_5_2;

        @FXML
        private ImageView orange_5_3;

        @FXML
        private ImageView orange_5_4;

        @FXML
        private ImageView orange_5_5;

        @FXML
        private ImageView orange_5_6;

        @FXML
        private ImageView orange_5_7;

        @FXML
        private ImageView orange_5_8;

        @FXML
        private ImageView orange_5_9;

        @FXML
        private ImageView orange_6_0;

        @FXML
        private ImageView orange_6_1;

        @FXML
        private ImageView orange_6_2;

        @FXML
        private ImageView orange_6_3;

        @FXML
        private ImageView orange_6_4;

        @FXML
        private ImageView orange_6_5;

        @FXML
        private ImageView orange_6_6;

        @FXML
        private ImageView orange_6_7;

        @FXML
        private ImageView orange_6_8;

        @FXML
        private ImageView orange_6_9;

        @FXML
        private ImageView orange_7_0;

        @FXML
        private ImageView orange_7_1;

        @FXML
        private ImageView orange_7_2;

        @FXML
        private ImageView orange_7_3;

        @FXML
        private ImageView orange_7_4;

        @FXML
        private ImageView orange_7_5;

        @FXML
        private ImageView orange_7_6;

        @FXML
        private ImageView orange_7_7;

        @FXML
        private ImageView orange_7_8;

        @FXML
        private ImageView orange_7_9;

        @FXML
        private ImageView orange_8_0;

        @FXML
        private ImageView orange_8_1;

        @FXML
        private ImageView orange_8_2;

        @FXML
        private ImageView orange_8_3;

        @FXML
        private ImageView orange_8_4;

        @FXML
        private ImageView orange_8_5;

        @FXML
        private ImageView orange_8_6;

        @FXML
        private ImageView orange_8_7;

        @FXML
        private ImageView orange_8_8;

        @FXML
        private ImageView orange_8_9;

        @FXML
        private ImageView orange_9_0;

        @FXML
        private ImageView orange_9_1;

        @FXML
        private ImageView orange_9_2;

        @FXML
        private ImageView orange_9_3;

        @FXML
        private ImageView orange_9_4;

        @FXML
        private ImageView orange_9_5;

        @FXML
        private ImageView orange_9_6;

        @FXML
        private ImageView orange_9_7;

        @FXML
        private ImageView orange_9_8;

        @FXML
        private ImageView orange_9_9;

        @FXML
        private ImageView purple_0_0;

        @FXML
        private ImageView purple_0_1;

        @FXML
        private ImageView purple_0_2;

        @FXML
        private ImageView purple_0_3;

        @FXML
        private ImageView purple_0_4;

        @FXML
        private ImageView purple_0_5;

        @FXML
        private ImageView purple_0_6;

        @FXML
        private ImageView purple_0_7;

        @FXML
        private ImageView purple_0_8;

        @FXML
        private ImageView purple_0_9;

        @FXML
        private ImageView purple_10_0;

        @FXML
        private ImageView purple_10_1;

        @FXML
        private ImageView purple_10_2;

        @FXML
        private ImageView purple_10_3;

        @FXML
        private ImageView purple_10_4;

        @FXML
        private ImageView purple_10_5;

        @FXML
        private ImageView purple_10_6;

        @FXML
        private ImageView purple_10_7;

        @FXML
        private ImageView purple_10_8;

        @FXML
        private ImageView purple_10_9;

        @FXML
        private ImageView purple_11_0;

        @FXML
        private ImageView purple_11_1;

        @FXML
        private ImageView purple_11_2;

        @FXML
        private ImageView purple_11_3;

        @FXML
        private ImageView purple_11_4;

        @FXML
        private ImageView purple_11_5;

        @FXML
        private ImageView purple_11_6;

        @FXML
        private ImageView purple_11_7;

        @FXML
        private ImageView purple_11_8;

        @FXML
        private ImageView purple_11_9;

        @FXML
        private ImageView purple_12_0;

        @FXML
        private ImageView purple_12_1;

        @FXML
        private ImageView purple_12_2;

        @FXML
        private ImageView purple_12_3;

        @FXML
        private ImageView purple_12_4;

        @FXML
        private ImageView purple_12_5;

        @FXML
        private ImageView purple_12_6;

        @FXML
        private ImageView purple_12_7;

        @FXML
        private ImageView purple_12_8;

        @FXML
        private ImageView purple_12_9;

        @FXML
        private ImageView purple_1_0;

        @FXML
        private ImageView purple_1_1;

        @FXML
        private ImageView purple_1_2;

        @FXML
        private ImageView purple_1_3;

        @FXML
        private ImageView purple_1_4;

        @FXML
        private ImageView purple_1_5;

        @FXML
        private ImageView purple_1_6;

        @FXML
        private ImageView purple_1_7;

        @FXML
        private ImageView purple_1_8;

        @FXML
        private ImageView purple_1_9;

        @FXML
        private ImageView purple_2_0;

        @FXML
        private ImageView purple_2_1;

        @FXML
        private ImageView purple_2_2;

        @FXML
        private ImageView purple_2_3;

        @FXML
        private ImageView purple_2_4;

        @FXML
        private ImageView purple_2_5;

        @FXML
        private ImageView purple_2_6;

        @FXML
        private ImageView purple_2_7;

        @FXML
        private ImageView purple_2_8;

        @FXML
        private ImageView purple_2_9;

        @FXML
        private ImageView purple_3_0;

        @FXML
        private ImageView purple_3_1;

        @FXML
        private ImageView purple_3_2;

        @FXML
        private ImageView purple_3_3;

        @FXML
        private ImageView purple_3_4;

        @FXML
        private ImageView purple_3_5;

        @FXML
        private ImageView purple_3_6;

        @FXML
        private ImageView purple_3_7;

        @FXML
        private ImageView purple_3_8;

        @FXML
        private ImageView purple_3_9;

        @FXML
        private ImageView purple_4_0;

        @FXML
        private ImageView purple_4_1;

        @FXML
        private ImageView purple_4_2;

        @FXML
        private ImageView purple_4_3;

        @FXML
        private ImageView purple_4_4;

        @FXML
        private ImageView purple_4_5;

        @FXML
        private ImageView purple_4_6;

        @FXML
        private ImageView purple_4_7;

        @FXML
        private ImageView purple_4_8;

        @FXML
        private ImageView purple_4_9;

        @FXML
        private ImageView purple_5_0;

        @FXML
        private ImageView purple_5_1;

        @FXML
        private ImageView purple_5_2;

        @FXML
        private ImageView purple_5_3;

        @FXML
        private ImageView purple_5_4;

        @FXML
        private ImageView purple_5_5;

        @FXML
        private ImageView purple_5_6;

        @FXML
        private ImageView purple_5_7;

        @FXML
        private ImageView purple_5_8;

        @FXML
        private ImageView purple_5_9;

        @FXML
        private ImageView purple_6_0;

        @FXML
        private ImageView purple_6_1;

        @FXML
        private ImageView purple_6_2;

        @FXML
        private ImageView purple_6_3;

        @FXML
        private ImageView purple_6_4;

        @FXML
        private ImageView purple_6_5;

        @FXML
        private ImageView purple_6_6;

        @FXML
        private ImageView purple_6_7;

        @FXML
        private ImageView purple_6_8;

        @FXML
        private ImageView purple_6_9;

        @FXML
        private ImageView purple_7_0;

        @FXML
        private ImageView purple_7_1;

        @FXML
        private ImageView purple_7_2;

        @FXML
        private ImageView purple_7_3;

        @FXML
        private ImageView purple_7_4;

        @FXML
        private ImageView purple_7_5;

        @FXML
        private ImageView purple_7_6;

        @FXML
        private ImageView purple_7_7;

        @FXML
        private ImageView purple_7_8;

        @FXML
        private ImageView purple_7_9;

        @FXML
        private ImageView purple_8_0;

        @FXML
        private ImageView purple_8_1;

        @FXML
        private ImageView purple_8_2;

        @FXML
        private ImageView purple_8_3;

        @FXML
        private ImageView purple_8_4;

        @FXML
        private ImageView purple_8_5;

        @FXML
        private ImageView purple_8_6;

        @FXML
        private ImageView purple_8_7;

        @FXML
        private ImageView purple_8_8;

        @FXML
        private ImageView purple_8_9;

        @FXML
        private ImageView purple_9_0;

        @FXML
        private ImageView purple_9_1;

        @FXML
        private ImageView purple_9_2;

        @FXML
        private ImageView purple_9_3;

        @FXML
        private ImageView purple_9_4;

        @FXML
        private ImageView purple_9_5;

        @FXML
        private ImageView purple_9_6;

        @FXML
        private ImageView purple_9_7;

        @FXML
        private ImageView purple_9_8;

        @FXML
        private ImageView purple_9_9;

        @FXML
        private ImageView red_0_0;

        @FXML
        private ImageView red_0_1;

        @FXML
        private ImageView red_0_2;

        @FXML
        private ImageView red_0_3;

        @FXML
        private ImageView red_0_4;

        @FXML
        private ImageView red_0_5;

        @FXML
        private ImageView red_0_6;

        @FXML
        private ImageView red_0_7;

        @FXML
        private ImageView red_0_8;

        @FXML
        private ImageView red_0_9;

        @FXML
        private ImageView red_10_0;

        @FXML
        private ImageView red_10_1;

        @FXML
        private ImageView red_10_2;

        @FXML
        private ImageView red_10_3;

        @FXML
        private ImageView red_10_4;

        @FXML
        private ImageView red_10_5;

        @FXML
        private ImageView red_10_6;

        @FXML
        private ImageView red_10_7;

        @FXML
        private ImageView red_10_8;

        @FXML
        private ImageView red_10_9;

        @FXML
        private ImageView red_11_0;

        @FXML
        private ImageView red_11_1;

        @FXML
        private ImageView red_11_2;

        @FXML
        private ImageView red_11_3;

        @FXML
        private ImageView red_11_4;

        @FXML
        private ImageView red_11_5;

        @FXML
        private ImageView red_11_6;

        @FXML
        private ImageView red_11_7;

        @FXML
        private ImageView red_11_8;

        @FXML
        private ImageView red_11_9;

        @FXML
        private ImageView red_12_0;

        @FXML
        private ImageView red_12_1;

        @FXML
        private ImageView red_12_2;

        @FXML
        private ImageView red_12_3;

        @FXML
        private ImageView red_12_4;

        @FXML
        private ImageView red_12_5;

        @FXML
        private ImageView red_12_6;

        @FXML
        private ImageView red_12_7;

        @FXML
        private ImageView red_12_8;

        @FXML
        private ImageView red_12_9;

        @FXML
        private ImageView red_1_0;

        @FXML
        private ImageView red_1_1;

        @FXML
        private ImageView red_1_2;

        @FXML
        private ImageView red_1_3;

        @FXML
        private ImageView red_1_4;

        @FXML
        private ImageView red_1_5;

        @FXML
        private ImageView red_1_6;

        @FXML
        private ImageView red_1_7;

        @FXML
        private ImageView red_1_8;

        @FXML
        private ImageView red_1_9;

        @FXML
        private ImageView red_2_0;

        @FXML
        private ImageView red_2_1;

        @FXML
        private ImageView red_2_2;

        @FXML
        private ImageView red_2_3;

        @FXML
        private ImageView red_2_4;

        @FXML
        private ImageView red_2_5;

        @FXML
        private ImageView red_2_6;

        @FXML
        private ImageView red_2_7;

        @FXML
        private ImageView red_2_8;

        @FXML
        private ImageView red_2_9;

        @FXML
        private ImageView red_3_0;

        @FXML
        private ImageView red_3_1;

        @FXML
        private ImageView red_3_2;

        @FXML
        private ImageView red_3_3;

        @FXML
        private ImageView red_3_4;

        @FXML
        private ImageView red_3_5;

        @FXML
        private ImageView red_3_6;

        @FXML
        private ImageView red_3_7;

        @FXML
        private ImageView red_3_8;

        @FXML
        private ImageView red_3_9;

        @FXML
        private ImageView red_4_0;

        @FXML
        private ImageView red_4_1;

        @FXML
        private ImageView red_4_2;

        @FXML
        private ImageView red_4_3;

        @FXML
        private ImageView red_4_4;

        @FXML
        private ImageView red_4_5;

        @FXML
        private ImageView red_4_6;

        @FXML
        private ImageView red_4_7;

        @FXML
        private ImageView red_4_8;

        @FXML
        private ImageView red_4_9;

        @FXML
        private ImageView red_5_0;

        @FXML
        private ImageView red_5_1;

        @FXML
        private ImageView red_5_2;

        @FXML
        private ImageView red_5_3;

        @FXML
        private ImageView red_5_4;

        @FXML
        private ImageView red_5_5;

        @FXML
        private ImageView red_5_6;

        @FXML
        private ImageView red_5_7;

        @FXML
        private ImageView red_5_8;

        @FXML
        private ImageView red_5_9;

        @FXML
        private ImageView red_6_0;

        @FXML
        private ImageView red_6_1;

        @FXML
        private ImageView red_6_2;

        @FXML
        private ImageView red_6_3;

        @FXML
        private ImageView red_6_4;

        @FXML
        private ImageView red_6_5;

        @FXML
        private ImageView red_6_6;

        @FXML
        private ImageView red_6_7;

        @FXML
        private ImageView red_6_8;

        @FXML
        private ImageView red_6_9;

        @FXML
        private ImageView red_7_0;

        @FXML
        private ImageView red_7_1;

        @FXML
        private ImageView red_7_2;

        @FXML
        private ImageView red_7_3;

        @FXML
        private ImageView red_7_4;

        @FXML
        private ImageView red_7_5;

        @FXML
        private ImageView red_7_6;

        @FXML
        private ImageView red_7_7;

        @FXML
        private ImageView red_7_8;

        @FXML
        private ImageView red_7_9;

        @FXML
        private ImageView red_8_0;

        @FXML
        private ImageView red_8_1;

        @FXML
        private ImageView red_8_2;

        @FXML
        private ImageView red_8_3;

        @FXML
        private ImageView red_8_4;

        @FXML
        private ImageView red_8_5;

        @FXML
        private ImageView red_8_6;

        @FXML
        private ImageView red_8_7;

        @FXML
        private ImageView red_8_8;

        @FXML
        private ImageView red_8_9;

        @FXML
        private ImageView red_9_0;

        @FXML
        private ImageView red_9_1;

        @FXML
        private ImageView red_9_2;

        @FXML
        private ImageView red_9_3;

        @FXML
        private ImageView red_9_4;

        @FXML
        private ImageView red_9_5;

        @FXML
        private ImageView red_9_6;

        @FXML
        private ImageView red_9_7;

        @FXML
        private ImageView red_9_8;

        @FXML
        private ImageView red_9_9;

        @FXML
        private ImageView yellow_0_0;

        @FXML
        private ImageView yellow_0_1;

        @FXML
        private ImageView yellow_0_2;

        @FXML
        private ImageView yellow_0_3;

        @FXML
        private ImageView yellow_0_4;

        @FXML
        private ImageView yellow_0_5;

        @FXML
        private ImageView yellow_0_6;

        @FXML
        private ImageView yellow_0_7;

        @FXML
        private ImageView yellow_0_8;

        @FXML
        private ImageView yellow_0_9;

        @FXML
        private ImageView yellow_10_0;

        @FXML
        private ImageView yellow_10_1;

        @FXML
        private ImageView yellow_10_2;

        @FXML
        private ImageView yellow_10_3;

        @FXML
        private ImageView yellow_10_4;

        @FXML
        private ImageView yellow_10_5;

        @FXML
        private ImageView yellow_10_6;

        @FXML
        private ImageView yellow_10_7;

        @FXML
        private ImageView yellow_10_8;

        @FXML
        private ImageView yellow_10_9;

        @FXML
        private ImageView yellow_11_0;

        @FXML
        private ImageView yellow_11_1;

        @FXML
        private ImageView yellow_11_2;

        @FXML
        private ImageView yellow_11_3;

        @FXML
        private ImageView yellow_11_4;

        @FXML
        private ImageView yellow_11_5;

        @FXML
        private ImageView yellow_11_6;

        @FXML
        private ImageView yellow_11_7;

        @FXML
        private ImageView yellow_11_8;

        @FXML
        private ImageView yellow_11_9;

        @FXML
        private ImageView yellow_12_0;

        @FXML
        private ImageView yellow_12_1;

        @FXML
        private ImageView yellow_12_2;

        @FXML
        private ImageView yellow_12_3;

        @FXML
        private ImageView yellow_12_4;

        @FXML
        private ImageView yellow_12_5;

        @FXML
        private ImageView yellow_12_6;

        @FXML
        private ImageView yellow_12_7;

        @FXML
        private ImageView yellow_12_8;

        @FXML
        private ImageView yellow_12_9;

        @FXML
        private ImageView yellow_1_0;

        @FXML
        private ImageView yellow_1_1;

        @FXML
        private ImageView yellow_1_2;

        @FXML
        private ImageView yellow_1_3;

        @FXML
        private ImageView yellow_1_4;

        @FXML
        private ImageView yellow_1_5;

        @FXML
        private ImageView yellow_1_6;

        @FXML
        private ImageView yellow_1_7;

        @FXML
        private ImageView yellow_1_8;

        @FXML
        private ImageView yellow_1_9;

        @FXML
        private ImageView yellow_2_0;

        @FXML
        private ImageView yellow_2_1;

        @FXML
        private ImageView yellow_2_2;

        @FXML
        private ImageView yellow_2_3;

        @FXML
        private ImageView yellow_2_4;

        @FXML
        private ImageView yellow_2_5;

        @FXML
        private ImageView yellow_2_6;

        @FXML
        private ImageView yellow_2_7;

        @FXML
        private ImageView yellow_2_8;

        @FXML
        private ImageView yellow_2_9;

        @FXML
        private ImageView yellow_3_0;

        @FXML
        private ImageView yellow_3_1;

        @FXML
        private ImageView yellow_3_2;

        @FXML
        private ImageView yellow_3_3;

        @FXML
        private ImageView yellow_3_4;

        @FXML
        private ImageView yellow_3_5;

        @FXML
        private ImageView yellow_3_6;

        @FXML
        private ImageView yellow_3_7;

        @FXML
        private ImageView yellow_3_8;

        @FXML
        private ImageView yellow_3_9;

        @FXML
        private ImageView yellow_4_0;

        @FXML
        private ImageView yellow_4_1;

        @FXML
        private ImageView yellow_4_2;

        @FXML
        private ImageView yellow_4_3;

        @FXML
        private ImageView yellow_4_4;

        @FXML
        private ImageView yellow_4_5;

        @FXML
        private ImageView yellow_4_6;

        @FXML
        private ImageView yellow_4_7;

        @FXML
        private ImageView yellow_4_8;

        @FXML
        private ImageView yellow_4_9;

        @FXML
        private ImageView yellow_5_0;

        @FXML
        private ImageView yellow_5_1;

        @FXML
        private ImageView yellow_5_2;

        @FXML
        private ImageView yellow_5_3;

        @FXML
        private ImageView yellow_5_4;

        @FXML
        private ImageView yellow_5_5;

        @FXML
        private ImageView yellow_5_6;

        @FXML
        private ImageView yellow_5_7;

        @FXML
        private ImageView yellow_5_8;

        @FXML
        private ImageView yellow_5_9;

        @FXML
        private ImageView yellow_6_0;

        @FXML
        private ImageView yellow_6_1;

        @FXML
        private ImageView yellow_6_2;

        @FXML
        private ImageView yellow_6_3;

        @FXML
        private ImageView yellow_6_4;

        @FXML
        private ImageView yellow_6_5;

        @FXML
        private ImageView yellow_6_6;

        @FXML
        private ImageView yellow_6_7;

        @FXML
        private ImageView yellow_6_8;

        @FXML
        private ImageView yellow_6_9;

        @FXML
        private ImageView yellow_7_0;

        @FXML
        private ImageView yellow_7_1;

        @FXML
        private ImageView yellow_7_2;

        @FXML
        private ImageView yellow_7_3;

        @FXML
        private ImageView yellow_7_4;

        @FXML
        private ImageView yellow_7_5;

        @FXML
        private ImageView yellow_7_6;

        @FXML
        private ImageView yellow_7_7;

        @FXML
        private ImageView yellow_7_8;

        @FXML
        private ImageView yellow_7_9;

        @FXML
        private ImageView yellow_8_0;

        @FXML
        private ImageView yellow_8_1;

        @FXML
        private ImageView yellow_8_2;

        @FXML
        private ImageView yellow_8_3;

        @FXML
        private ImageView yellow_8_4;

        @FXML
        private ImageView yellow_8_5;

        @FXML
        private ImageView yellow_8_6;

        @FXML
        private ImageView yellow_8_7;

        @FXML
        private ImageView yellow_8_8;

        @FXML
        private ImageView yellow_8_9;

        @FXML
        private ImageView yellow_9_0;

        @FXML
        private ImageView yellow_9_1;

        @FXML
        private ImageView yellow_9_2;

        @FXML
        private ImageView yellow_9_3;

        @FXML
        private ImageView yellow_9_4;

        @FXML
        private ImageView yellow_9_5;

        @FXML
        private ImageView yellow_9_6;

        @FXML
        private ImageView yellow_9_7;

        @FXML
        private ImageView yellow_9_8;

        @FXML
        private ImageView yellow_9_9;

        @FXML
        private ImageView card1;

        @FXML
        private ImageView card2;

        @FXML
        private ImageView card3;

        @FXML
        private ImageView card4;

        @FXML
        private ImageView card5;

        @FXML
        private ImageView move1_1;

        @FXML
        private ImageView move1_2;

        @FXML
        private ImageView move1_3;

        @FXML
        private ImageView move1_4;

        @FXML
        private ImageView move1_5;

        @FXML
        private ImageView move2_1;

        @FXML
        private ImageView move2_2;

        @FXML
        private ImageView move2_3;

        @FXML
        private ImageView move2_4;

        @FXML
        private ImageView move2_5;

        @FXML
        private ImageView move3_1;

        @FXML
        private ImageView move3_2;

        @FXML
        private ImageView move3_3;

        @FXML
        private ImageView move3_4;

        @FXML
        private ImageView move3_5;

        @FXML
        private ImageView leftTurn_1;

        @FXML
        private ImageView leftTurn_2;

        @FXML
        private ImageView leftTurn_3;

        @FXML
        private ImageView leftTurn_4;

        @FXML
        private ImageView leftTurn_5;

        @FXML
        private ImageView rightTurn_1;

        @FXML
        private ImageView rightTurn_2;

        @FXML
        private ImageView rightTurn_3;

        @FXML
        private ImageView rightTurn_4;

        @FXML
        private ImageView rightTurn_5;

        @FXML
        private ImageView moveBack_1;

        @FXML
        private ImageView moveBack_2;

        @FXML
        private ImageView moveBack_3;

        @FXML
        private ImageView moveBack_4;

        @FXML
        private ImageView moveBack_5;

        @FXML
        private ImageView powerUp_1;

        @FXML
        private ImageView powerUp_2;

        @FXML
        private ImageView powerUp_3;

        @FXML
        private ImageView powerUp_4;

        @FXML
        private ImageView powerUp_5;

        @FXML
        private ImageView again_1;

        @FXML
        private ImageView again_2;

        @FXML
        private ImageView again_3;

        @FXML
        private ImageView again_4;

        @FXML
        private ImageView again_5;

        @FXML
        private ImageView uTurn_1;

        @FXML
        private ImageView uTurn_2;

        @FXML
        private ImageView uTurn_3;

        @FXML
        private ImageView uTurn_4;

        @FXML
        private ImageView uTurn_5;

        @FXML
        private ImageView virus_1;

        @FXML
        private ImageView virus_2;

        @FXML
        private ImageView virus_3;

        @FXML
        private ImageView virus_4;

        @FXML
        private ImageView virus_5;

        @FXML
        private ImageView worm_1;

        @FXML
        private ImageView worm_2;

        @FXML
        private ImageView worm_3;

        @FXML
        private ImageView worm_4;

        @FXML
        private ImageView worm_5;

        @FXML
        private ImageView spam_1;

        @FXML
        private ImageView spam_2;

        @FXML
        private ImageView spam_3;

        @FXML
        private ImageView spam_4;

        @FXML
        private ImageView spam_5;

        @FXML
        private ImageView trojanHorse_1;

        @FXML
        private ImageView trojanHorse_2;

        @FXML
        private ImageView trojanHorse_3;

        @FXML
        private ImageView trojanHorse_4;

        @FXML
        private ImageView trojanHorse_5;

        @FXML
        private ImageView adminPrivilege;

        @FXML
        private ImageView memorySwap;

        @FXML
        private ImageView spamBlocker;

        @FXML
        private ImageView rearLaser;

        @FXML
        private Label currentPlayerLabel;

        @FXML
        private ChoiceBox<String> registerBox0, registerBox1, registerBox2, registerBox3, registerBox4;

        private ChoiceBox[] choiceBoxes;

        @FXML
        private ChoiceBox<String> chosenUpgradeCardBox = new ChoiceBox<>();

        @FXML
        private Button buy;

        @FXML
        private Button startPointFive, startPointFour, startPointOne, startPointSix, startPointThree, startPointTwo;

        @FXML
        private Text timerText;

        private boolean startAction = false;

        @FXML
        private GridPane gridPaneMap;

        @FXML
        private GridPane gridPaneCard;

        @FXML
        private GridPane gridPaneUpgrade;

        @FXML
        private GridPane gridPaneUpgradeGap1;

        @FXML
        private GridPane gridPaneUpgradeGap2;

        @FXML
        private GridPane gridPaneUpgradeGap3;

        @FXML
        private GridPane gridPaneUpgradeGap4;

        @FXML
        private GridPane gridPaneUpgradeGap5;

        @FXML
        private GridPane gridPaneUpgradeGap6;

        @FXML
        private Button nextCardButton;

        @FXML
        private ChoiceBox<String> playUpgradeBox = new ChoiceBox<>();

        @FXML
        private Label phase;

        @FXML
        private Label toDo;

        @FXML
        private Text nameText, energyText, checkpointText;


        private ArrayList<String> chosenCards = new ArrayList<>();

        @FXML
        public void initialize(URL url, ResourceBundle resourceBundle) {
            try {

                initializeClient();

                gridPaneChildren = gridPaneMap.getChildren();

                nextCardButton.visibleProperty().bind(client.getNextCardButtonVisible());
                client.getCurrentPositionsAsString().addListener(new ChangeListener<String>() {

                        /**
                         *
                         * @param observableValue
                         * @param oldValue old player position
                         * @param newValue new player position
                         * @author Arda, Ilinur
                         */
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        String[] str = newValue.split(", ");
                        for (String s : str) {
                            for (Node n : gridPaneChildren) {
                                if (n.getId() != null) {
                                    if (s.contains(n.getId())) {
                                        String direction = s.split("-")[1];
                                        switch (direction) {
                                            case "north":
                                                n.setRotate(0);
                                                break;
                                            case "south":
                                                n.setRotate(180);
                                                break;
                                            case "west":
                                                n.setRotate(270);
                                                break;
                                            case "east":
                                                n.setRotate(90);
                                                break;
                                        }

                                    }
                                }
                            }
                        }

                        ArrayList<String> onlyPosition = new ArrayList<>();
                        for (String r : str) {
                            String positionWithColour = r.split("-")[0];
                            onlyPosition.add(positionWithColour.split("_", 2)[1]);
                        }
                        String justPosition = onlyPosition.toString();
                        String justPosition1 = justPosition.replace("[", "");
                        String justPosition2 = justPosition1.replace("]", "");
                        String[] position = justPosition2.split(", ");

                        for (String p : position) {
                            for (Node n : gridPaneChildren) {
                                if (n.getId() != null) {
                                    if (n.getId().contains(p) && n.getId().contains("energy")) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        ArrayList<String> onlyColorAndPosition = new ArrayList<>();
                        ArrayList<String> onlyColorAndPositionOld = new ArrayList<>();


                        for (String s : str) {
                            onlyColorAndPosition.add(s.split("-")[0]);
                        }

                        if (oldValue != null) {
                            for (String s : oldValue.split(", ")) {
                                onlyColorAndPositionOld.add(s.split("-")[0]);
                            }
                        }

                        String colorAndPosition = onlyColorAndPosition.toString();
                        String colorAndPosition2 = colorAndPosition.replace("[", "");
                        String colorAndPosition3 = colorAndPosition2.replace("]", "");

                        String colorAndPositionOld = onlyColorAndPositionOld.toString();
                        String colorAndPositionOld2 = colorAndPositionOld.replace("[", "");
                        String colorAndPositionOld3 = colorAndPositionOld2.replace("]", "");

                        boolean startPoint = false;
                        String[] toDelete;
                        if (oldValue == null) {
                            toDelete = new String[0];
                            startPoint = true;
                        } else {
                            toDelete = colorAndPositionOld3.split(", ");
                        }
                        String[] changes = colorAndPosition3.split(", ");

                        if (!startPoint && toDelete != null) {

                            for (String s2 : toDelete) {
                                for (Node n : gridPaneChildren) {
                                    if (n.getId() != null) {
                                        if (n.getId().equals(s2)) {
                                            n.setVisible(false);
                                        }
                                    }
                                }
                            }
                        }

                        for (String s : changes) {
                            for (Node n : gridPaneChildren) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(s)) {
                                        n.setVisible(true);
                                    }
                                }
                            }
                        }
                    }
                });


                startPointOne.visibleProperty().bind(client.invisibleStartPoint1());
                startPointTwo.visibleProperty().bind(client.invisibleStartPoint2());
                startPointThree.visibleProperty().bind(client.invisibleStartPoint3());
                startPointFour.visibleProperty().bind(client.invisibleStartPoint4());
                startPointFive.visibleProperty().bind(client.invisibleStartPoint5());
                startPointSix.visibleProperty().bind(client.invisibleStartPoint6());
                //TODO new
                phase.textProperty().bind(client.getPhaseInfoProperty());
                toDo.textProperty().bind(client.getToDoInfoProperty());

                nameText.textProperty().bind(client.getUserNameProperty());
                energyText.textProperty().bind(client.getEnergyCubesProperty().asString());
                checkpointText.textProperty().bind(client.getCheckPointsProperty().asString());

                gridPane1 = gridPaneUpgradeGap1.getChildren();

                client.getUpgradeCardInGap1AsString().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (oldValue != null) {
                            for (Node n : gridPane1) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        for (Node n : gridPane1) {
                            if (n.getId() != null) {
                                if (newValue != null) {
                                    if (n.getId().equals(newValue)) {
                                        n.setVisible(true);
                                    }
                                } else {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }


                    }
                });


                gridPane2 = gridPaneUpgradeGap2.getChildren();

                client.getUpgradeCardInGap2AsString().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (oldValue != null) {
                            for (Node n : gridPane2) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        for (Node n : gridPane2) {
                            if (n.getId() != null) {
                                if (newValue != null) {
                                    if (n.getId().equals(newValue)) {
                                        n.setVisible(true);
                                    }
                                } else {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }


                    }
                });


                gridPane3 = gridPaneUpgradeGap3.getChildren();

                client.getUpgradeCardInGap3AsString().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (oldValue != null) {
                            for (Node n : gridPane3) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        for (Node n : gridPane3) {
                            if (n.getId() != null) {
                                if (newValue != null) {
                                    if (n.getId().equals(newValue)) {
                                        n.setVisible(true);
                                    }
                                } else {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }


                    }
                });

                gridPane4 = gridPaneUpgradeGap4.getChildren();

                client.getUpgradeCardInGap4AsString().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (oldValue != null) {
                            for (Node n : gridPane4) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        for (Node n : gridPane4) {
                            if (n.getId() != null) {
                                if (newValue != null) {
                                    if (n.getId().equals(newValue)) {
                                        n.setVisible(true);
                                    }
                                } else {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }


                    }
                });

                gridPane5 = gridPaneUpgradeGap5.getChildren();

                client.getUpgradeCardInGap5AsString().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (oldValue != null) {
                            for (Node n : gridPane5) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        for (Node n : gridPane5) {
                            if (n.getId() != null) {
                                if (newValue != null) {
                                    if (n.getId().equals(newValue)) {
                                        n.setVisible(true);
                                    }
                                } else {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }


                    }
                });

                gridPane6 = gridPaneUpgradeGap6.getChildren();

                client.getUpgradeCardInGap6AsString().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (oldValue != null) {
                            for (Node n : gridPane6) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        for (Node n : gridPane6) {
                            if (n.getId() != null) {
                                if (newValue != null) {
                                    if (n.getId().equals(newValue)) {
                                        n.setVisible(true);
                                    }
                                } else {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }


                    }
                });


                gridPaneChild = gridPaneUpgrade.getChildren();
                client.getUpgradeCardAsString().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (oldValue != null) {
                            for (Node n : gridPaneChild) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        for (Node n : gridPaneChild) {
                            if (n.getId() != null) {
                                if(newValue != null ) {
                                    if (n.getId().equals(newValue)) {
                                        n.setVisible(true);
                                    }
                                }else{
                                    if(n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }


                    }
                });


                gridPaneC = gridPaneCard.getChildren();
                client.getCurrentCardsInReg0asString().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (oldValue != null) {
                            for (Node n : gridPaneC) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        for (Node n : gridPaneC) {
                            if (n.getId() != null) {
                                if(newValue != null ) {
                                    if (n.getId().equals(newValue)) {
                                        n.setVisible(true);
                                    }
                                }else{
                                    if(n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }


                    }
                });


                client.getCurrentCardsInReg1asString().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (oldValue != null) {
                            for (Node n : gridPaneC) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        for (Node n : gridPaneC) {
                            if (n.getId() != null) {
                                if(newValue != null ) {
                                    if (n.getId().equals(newValue)) {
                                        n.setVisible(true);
                                    }
                                }else{
                                    if(n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }


                    }
                });

                client.getCurrentCardsInReg2asString().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (oldValue != null) {
                            for (Node n : gridPaneC) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        for (Node n : gridPaneC) {
                            if (n.getId() != null) {
                                if(newValue != null ) {
                                    if (n.getId().equals(newValue)) {
                                        n.setVisible(true);
                                    }
                                }else{
                                    if(n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }


                    }
                });

                client.getCurrentCardsInReg3asString().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (oldValue != null) {
                            for (Node n : gridPaneC) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        for (Node n : gridPaneC) {
                            if (n.getId() != null) {
                                if(newValue != null ) {
                                    if (n.getId().equals(newValue)) {
                                        n.setVisible(true);
                                    }
                                }else{
                                    if(n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }


                    }
                });

                client.getCurrentCardsInReg4asString().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

                        if (oldValue != null) {
                            for (Node n : gridPaneC) {
                                if (n.getId() != null) {
                                    if (n.getId().equals(oldValue)) {
                                        n.setVisible(false);
                                    }
                                }
                            }
                        }

                        for (Node n : gridPaneC) {
                            if (n.getId() != null) {
                                if (n.getId().equals(newValue)) {
                                    n.setVisible(true);
                                }
                            }
                        }


                    }
                });

                choiceBoxes = new ChoiceBox[]{registerBox0, registerBox1, registerBox2, registerBox3, registerBox4};
                addCardsToChoiceBox();
                chosenUpgradeCardBox.itemsProperty().bindBidirectional(client.getUpgradeShopProperty());
                playUpgradeBox.itemsProperty().bindBidirectional(client.getMyUpgradeCardsProperty());
                timerText.textProperty().bindBidirectional(client.getCurrentTimers());
                currentPlayerLabel.textProperty().bind(client.getCurrentPlayer().asString());
            }catch (Exception e){
            }
        }


        public Node getChildNode(String id){
            for(Node n:gridPaneChildren){
                if((n.getId()).equals(id)){
                    return n;
                }
            }
            return null;
        }


        @FXML
        public void actionStartPointOne() {
            client.setStartPoint(1,1);
        }

        @FXML
        public void actionStartPointTwo() {
            client.setStartPoint(0,3);
        }

        @FXML
        public void actionStartPointThree() {
            client.setStartPoint(1,4);
        }

        @FXML
        public void actionStartPointFour() {
            client.setStartPoint(1,5);
        }

        @FXML
        public void actionStartPointFive() {
            client.setStartPoint(0, 6);
        }

        @FXML
        public void actionStartPointSix() {
            client.setStartPoint(1,8);
        }

        public void addCardsToChoiceBox() {
            for (ChoiceBox box : choiceBoxes ) {
                box.itemsProperty().bindBidirectional(client.getYourCardsInHandProperty());
            }
        }

        public void removeCard(String cards){
            for (ChoiceBox box : choiceBoxes ) {
                if (!cards.contains("Null")) {
                    box.getItems().remove(cards);
                }
            }
        }

        @FXML
        public void chosenRegisterCard0() {
            String cardInRegister0 = registerBox0.getSelectionModel().getSelectedItem();
            registerBox0.getSelectionModel().clearSelection();
            client.sendCardInRegisterToServer(0, cardInRegister0);
        }

        @FXML
        public void chosenRegisterCard1() {
            String cardInRegister1 = registerBox1.getSelectionModel().getSelectedItem();
            registerBox1.getSelectionModel().clearSelection();
            client.sendCardInRegisterToServer(1, cardInRegister1);
        }

        @FXML
        public void chosenRegisterCard2() {
            String cardInRegister2 = registerBox2.getSelectionModel().getSelectedItem();
            registerBox2.getSelectionModel().clearSelection();
            client.sendCardInRegisterToServer(2, cardInRegister2);
        }

        @FXML
        public void chosenRegisterCard3() {
            String cardInRegister3 = registerBox3.getSelectionModel().getSelectedItem();
            registerBox3.getSelectionModel().clearSelection();
            client.sendCardInRegisterToServer(3, cardInRegister3);
        }

        @FXML
        public void chosenRegisterCard4() {
            String cardInRegister4 = registerBox4.getSelectionModel().getSelectedItem();
            registerBox4.getSelectionModel().clearSelection();
            client.sendCardInRegisterToServer(4, cardInRegister4);
        }

        public void initializeClient(){
            client = StartClient.getClient();
        }

        /**
         * Tells the server to play the next card
         * @author Benedikt
         */
        @FXML
        public void playNextCard(){
            if(client.getPhase() == 3) {
                client.playNextCard();
            }
            else {
                Platform.runLater(() -> {
                    ErrorPopUp popup = new ErrorPopUp();
                    popup.openError("You can only play next card during activation phase!");
                });
            }
        }

        /**
         * Sends the card the player wants to buy to server
         * Error pop ups in case something is not allowed
         */
        @FXML
        public void buyUpgradeCard(){
            String upgradeCard = chosenUpgradeCardBox.getSelectionModel().getSelectedItem();
            if(client.getPhase() == 1) {
                if (upgradeCard != null) {
                    client.buyUpgradeCard(upgradeCard);
                }
                else {
                    Platform.runLater(() -> {
                        ErrorPopUp popup = new ErrorPopUp();
                        popup.openError("Please choose a card from the choicebox.");
                    });
                }
            }
            else{
                Platform.runLater(() -> {
                    ErrorPopUp popup = new ErrorPopUp();
                    popup.openError("You can only buy Upgrade cards during upgrade phase!");
                });
            }
        }

        /**
         * Lets you look at an upgrade card before you buy it
         * @author Benedikt
         */
        @FXML
        public void lookAtUpgradeCard(){
            String upgradeCardFromChoiceBox = chosenUpgradeCardBox.getSelectionModel().getSelectedItem();
            if(upgradeCardFromChoiceBox != null) {
                Platform.runLater(() -> client.setNewUpgradeCard(upgradeCardFromChoiceBox));
            }
        }

        /**
         * Sends the Upgrade Card the player wants to play to server
         * @author Benedikt
         */
        @FXML
        public void playUpgradeCard() {
                String upgradeCard = playUpgradeBox.getSelectionModel().getSelectedItem();
                if(client.getPhase() == 2) {
                        if (upgradeCard != null) {
                                client.playUpgradeCard(upgradeCard);
                        } else {
                                Platform.runLater(() -> {
                                        ErrorPopUp popup = new ErrorPopUp();
                                        popup.openError("Your choicebox is empty!");
                                });
                        }
                }
                else{
                        Platform.runLater(() -> {
                                ErrorPopUp popup = new ErrorPopUp();
                                popup.openError("You can only play Upgrade cards during programming phase!");
                        });
                }
        }
    }