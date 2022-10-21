package com.boggle.game.boggle;


import com.boggle.game.model.Highscore;
import com.boggle.game.model.PlayerDetails;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.boggle.game.boggle.HighscoreController.arrayList_Highscore;
import static com.boggle.game.model.StoredDetails.*;


public class HelloController implements Initializable {

    @FXML
    private TextField playerOneNameTextField;

    @FXML
    private TextField playerTwoNameTextField;

    private static PlayerDetails playerOneDetails;

    private static PlayerDetails playerTwoDetails;

    public static Integer roundCounter = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup radioButtonGroup = new ToggleGroup();
    }

    public void startGame() {

        roundCounter += 1;



        String playerOneName;
        String playerTwoName;

        if (roundCounter == 2){

            //// Load those only once in program

            overall_P1 = 0;
            overall_P2 = 0;

            arrayList_Highscore.add(new Highscore(25,"Jozefina"));
            arrayList_Highscore.add(new Highscore(20,"Ivana"));
            arrayList_Highscore.add(new Highscore(15,"Marko"));
            arrayList_Highscore.add(new Highscore(10,"Lana"));
            arrayList_Highscore.add(new Highscore(7,"Robi"));
            arrayList_Highscore.add(new Highscore(3,"Lovorka"));
            arrayList_Highscore.add(new Highscore(15,"Svemir"));
            arrayList_Highscore.add(new Highscore(32,"Miludin"));
            arrayList_Highscore.add(new Highscore(1,"Damira"));
            arrayList_Highscore.add(new Highscore(0,"Ognjen"));



            playerOneName = playerOneNameTextField.getText();
            playerTwoName = playerTwoNameTextField.getText();
        }
        else {
            playerOneName = P1;
            playerTwoName = P2;


        }


        playerOneDetails = new PlayerDetails(playerOneName);

        playerTwoDetails = new PlayerDetails(playerTwoName);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameScreen.fxml"));

        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = HelloApplication.getMainStage();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static PlayerDetails getPlayerOneDetails() {

        return playerOneDetails;
    }

    public static PlayerDetails getPlayerTwoDetails() {

        return playerTwoDetails;
    }
}