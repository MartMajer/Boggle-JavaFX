package com.boggle.game.boggle;

import com.boggle.game.model.HighscoreModel;
import com.boggle.game.model.StoredDetailsModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.boggle.game.boggle.HelloController.*;
import static com.boggle.game.boggle.HighscoreController.arrayList_Highscore;
import static com.boggle.game.model.StoredDetailsModel.overall_P1;


public class EndRoundController implements Initializable {

    @FXML
    private Button _board;

    @FXML
    private Button _highscore;
    @FXML
    private Button _startNewRound;

    @FXML
    private Label _player_1_name;
    @FXML
    private Label _player_2_name;

    @FXML
    private Label _player_1_RoundScore;
    @FXML
    private Label _player_2_RoundScore;

    @FXML
    private Label _player_1_Overall;
    @FXML
    private Label _player_2_Overall;

    @FXML
    private VBox __player_1_Pane_Found;
    @FXML
    private VBox __player_2_Pane_Found;
    @FXML
    private VBox __player_1_Pane_Possible;
    @FXML
    private VBox __player_2_Pane_Possible;

    @FXML
    private Label _roundNumber;


    @FXML
    private AnchorPane ap_singleplayer;

    @FXML
    private AnchorPane ap_multiplayer_2;

    @FXML
    private AnchorPane ap_multiplayer_3;

    @FXML
    private AnchorPane ap_multiplayer_4;

    private StoredDetailsModel store;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        _player_1_name.setText(getPlayerDetails().getPlayerName());


        _player_1_RoundScore.setText(getPlayerDetails().get_score());

        if (singleplayer_game == true){
            ap_singleplayer.setVisible(true);
        }

        Integer temp = roundCounter - 1;
        _roundNumber.setText(temp.toString());

        arrayList_Highscore.add(new HighscoreModel(getPlayerDetails().get_score_int(),getPlayerDetails().getPlayerName()));



        for (var word : getPlayerDetails().get_listOfCheckedWords()) {
            //add found words to pane
            __player_1_Pane_Found.getChildren().add(new Label(word));

        }





        for (var word : getPlayerDetails().get_PossibleWords() ) {
            //add found words to pane
            __player_1_Pane_Possible.getChildren().add(new Label(word));

        }


        Integer temp1 = getPlayerDetails().get_score_int() + overall_P1;



        _player_1_Overall.setText(temp1.toString());




    }


    public void startNewRound(ActionEvent actionEvent) {

        HelloController hello = new HelloController();

        Integer temp = roundCounter - 1;

        store = new StoredDetailsModel(_player_1_name.getText(), getPlayerDetails().get_score_int(),temp.toString());



        if (singleplayer_game = true)
        {
            hello.startgame();

        }

    }



    public void highscore(ActionEvent actionEvent) {

        HighscoreController highscoreController = new HighscoreController();
        highscoreController.highScoreCont();




    }

    public void board(ActionEvent actionEvent) {

        Scene scene = null;

        try {


            Parent root = FXMLLoader.load(HelloApplication.class.getResource("PreviewBoard.fxml"));
            scene = new Scene(root);
            Stage stage = new Stage();

            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.setResizable(false);

            stage.show();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = HelloApplication.getMainStage();



    }








}
