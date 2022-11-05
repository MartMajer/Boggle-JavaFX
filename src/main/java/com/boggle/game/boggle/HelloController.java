package com.boggle.game.boggle;


import com.boggle.game.model.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static com.boggle.game.boggle.EndRoundController.static_overall;
import static com.boggle.game.boggle.HighscoreController.arrayList_Highscore;
import static com.boggle.game.model.StoredDetailsModel.*;


import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextFlow;
//import com.boggle.game.model.NavState;
import com.boggle.game.model.chat.Message;
import com.boggle.game.network.ClientStream;
import com.boggle.game.network.IClient;
import com.boggle.game.network.IServer;
import com.boggle.game.network.ServerStream;

public class HelloController implements Initializable {

    @FXML
    private TextField playerOneNameTextField;

    @FXML
    private GridPane gp_mode;

    @FXML
    private GridPane gp_singleplayer;
    @FXML
    private AnchorPane gp_multiplayer_new_room;
    @FXML
    private GridPane gp_multiplayer_mode;
    @FXML
    private GridPane gp_join_room;
    @FXML
    private Button btn_back_singleplayer;
    @FXML
    private Button btn_highcsore_modal;
    @FXML
    private    Button btn_load;
    public static PlayerDetailsModel playerDetails;

    public static boolean singleplayer_game=false;

    public static Integer roundCounter = 1;

    public static Boolean game_loaded=false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (game_loaded == true) {
            btn_load.setVisible(false);
        }

    }

    public void singleplayer_btn(){
        gp_mode.setVisible(false);
        gp_singleplayer.setVisible(true);
    }

    public void load_btn() throws IOException, ClassNotFoundException {
        gp_mode.setVisible(false);

        PlayerDetailsModel model = new PlayerDetailsModel();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("playerdetails.txt"))) {
            model = (PlayerDetailsModel)ois.readObject();
        }

        game_loaded = true;

        setPlayerDetails(model);

        roundCounter = model.get_round_int();
        static_overall = model.get_overall_int();

        new EndRoundModel();
    }

    public void multiplayer_btn(){
        gp_mode.setVisible(false);
        gp_multiplayer_mode.setVisible(true);
    }
    public void join_room_btn(){
        gp_mode.setVisible(false);
        gp_multiplayer_mode.setVisible(false);
        gp_multiplayer_new_room.setVisible(false);
        gp_join_room.setVisible(true);
    }
    public void create_new_room_btn(){
        gp_mode.setVisible(false);
        gp_multiplayer_mode.setVisible(false);
        gp_multiplayer_new_room.setVisible(true);
    }

    public void back_btn(){
        gp_mode.setVisible(true);
        gp_singleplayer.setVisible(false);
        gp_multiplayer_new_room.setVisible(false);
        gp_multiplayer_mode.setVisible(false);
        gp_join_room.setVisible(false);
    }

    public void highscore_modal(){
        HighscoreController modal =   new HighscoreController();
        highscore_list();
        modal.highScoreCont();
    }

    public void startgame()  {

        roundCounter += 1;

        singleplayer_game = true;

        String playerName;

        if (roundCounter == 2){

            //// Load those only once in program

            overall_P1 = 0;

            highscore_list();

            //insert hardcode users to populate list
             //arrayList_Highscore.add(new HighscoreModel(25,"Marac"));
             //arrayList_Highscore.add(new HighscoreModel(20,"Ivana"));
             //arrayList_Highscore.add(new HighscoreModel(15,"Marko"));
             //arrayList_Highscore.add(new HighscoreModel(10,"Lana"));
             //arrayList_Highscore.add(new HighscoreModel(7,"Robi"));
             //arrayList_Highscore.add(new HighscoreModel(3,"Lovorka"));
             //arrayList_Highscore.add(new HighscoreModel(15,"Miro"));
             //arrayList_Highscore.add(new HighscoreModel(32,"Koko"));
             //arrayList_Highscore.add(new HighscoreModel(1,"Damira"));
             //arrayList_Highscore.add(new HighscoreModel(0,"Ognjen"));


            playerName = playerOneNameTextField.getText();

        }
        else {

            playerName = P1;

        }


        playerDetails = new PlayerDetailsModel(playerName);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameScreen.fxml"));

        Scene scene = null;

        try {


            FileInputStream inputstream = new FileInputStream("src/main/resources/Assets/bg_1.jpg");
            Image image = new Image(inputstream);
            ImageView imageView = new ImageView(image);

            imageView.setX(50);
            imageView.setY(25);
            imageView.setFitHeight(455);
            imageView.setFitWidth(500);

            imageView.setPreserveRatio(true);

            Group root = new Group(imageView);

            scene = new Scene(fxmlLoader.load(), 600, 400);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = HelloApplication.getMainStage();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    private void highscore_list() {
        ArrayList<HighscoreModel> model;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("boggle-board1.txt"))) {
            model = (ArrayList<HighscoreModel>)ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        for (var item : model
        ) {
            System.out.println(item.toString());
        }

        arrayList_Highscore = ((ArrayList<HighscoreModel>) model.clone());

    }

    public static PlayerDetailsModel getPlayerDetails() {

        return playerDetails;
    }
    public static PlayerDetailsModel setPlayerDetails(PlayerDetailsModel d) {

        return  playerDetails = d;
    }
}