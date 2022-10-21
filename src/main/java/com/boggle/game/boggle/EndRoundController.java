package com.boggle.game.boggle;

import com.boggle.game.model.Highscore;
import com.boggle.game.model.StoredDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.boggle.game.boggle.HelloController.*;
import static com.boggle.game.boggle.HelloController.getPlayerOneDetails;
import static com.boggle.game.boggle.HighscoreController.arrayList_Highscore;
import static com.boggle.game.boggle.StartPlayerTwo_modal._startPlayerTwo;
import static com.boggle.game.model.StoredDetails.overall_P1;
import static com.boggle.game.model.StoredDetails.overall_P2;


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

    private StoredDetails store;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        _player_1_name.setText(getPlayerOneDetails().getPlayerName());
        _player_2_name.setText(getPlayerTwoDetails().getPlayerName());

        _player_1_RoundScore.setText(getPlayerOneDetails().get_score());
        _player_2_RoundScore.setText(getPlayerTwoDetails().get_score());


        Integer temp = roundCounter - 1;
        _roundNumber.setText(temp.toString());

        arrayList_Highscore.add(new Highscore(getPlayerOneDetails().get_score_int(),getPlayerOneDetails().getPlayerName()));
        arrayList_Highscore.add(new Highscore(getPlayerTwoDetails().get_score_int(),getPlayerTwoDetails().getPlayerName()));



        for (var word : getPlayerOneDetails().get_listOfCheckedWords()) {
            //add found words to pane
            __player_1_Pane_Found.getChildren().add(new Label(word));

        }
        for (var word : getPlayerTwoDetails().get_listOfCheckedWords()) {
            //add found words to pane
            __player_2_Pane_Found.getChildren().add(new Label(word));

        }




        for (var word : getPlayerOneDetails().get_PossibleWords() ) {
            //add found words to pane
            __player_1_Pane_Possible.getChildren().add(new Label(word));

        }
        for (var word : getPlayerTwoDetails().get_PossibleWords()) {
            //add found words to pane
            __player_2_Pane_Possible.getChildren().add(new Label(word));

        }

        Integer temp1 = getPlayerOneDetails().get_score_int() + overall_P1;
        Integer temp2 = getPlayerTwoDetails().get_score_int() + overall_P2;



        _player_1_Overall.setText(temp1.toString());
        _player_2_Overall.setText(temp2.toString());




    }


    public void startNewRound(ActionEvent actionEvent) {

        HelloController hello = new HelloController();

        store = new StoredDetails(_player_1_name.getText(),_player_2_name.getText(), getPlayerOneDetails().get_score_int(), getPlayerTwoDetails().get_score_int(),_roundNumber.getText());


        _startPlayerTwo = false;

        hello.startGame();

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


            stage.show();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = HelloApplication.getMainStage();



    }








}
