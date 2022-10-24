package com.boggle.game.boggle;


import com.boggle.game.model.HighscoreModel;
import com.boggle.game.model.PlayerDetailsModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.boggle.game.boggle.HighscoreController.arrayList_Highscore;
import static com.boggle.game.model.StoredDetailsModel.*;


public class HelloController implements Initializable {

    @FXML
    private TextField playerOneNameTextField;

    @FXML
    private TextField playerTwoNameTextField;

    private static PlayerDetailsModel playerOneDetails;

    private static PlayerDetailsModel playerTwoDetails;

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

            arrayList_Highscore.add(new HighscoreModel(25,"Jozefina"));
            arrayList_Highscore.add(new HighscoreModel(20,"Ivana"));
            arrayList_Highscore.add(new HighscoreModel(15,"Marko"));
            arrayList_Highscore.add(new HighscoreModel(10,"Lana"));
            arrayList_Highscore.add(new HighscoreModel(7,"Robi"));
            arrayList_Highscore.add(new HighscoreModel(3,"Lovorka"));
            arrayList_Highscore.add(new HighscoreModel(15,"Svemir"));
            arrayList_Highscore.add(new HighscoreModel(32,"Miludin"));
            arrayList_Highscore.add(new HighscoreModel(1,"Damira"));
            arrayList_Highscore.add(new HighscoreModel(0,"Ognjen"));



            playerOneName = playerOneNameTextField.getText();
            playerTwoName = playerTwoNameTextField.getText();
        }
        else {
            playerOneName = P1;
            playerTwoName = P2;


        }


        playerOneDetails = new PlayerDetailsModel(playerOneName);

        playerTwoDetails = new PlayerDetailsModel(playerTwoName);



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
        stage.show();

    }

    public static PlayerDetailsModel getPlayerOneDetails() {

        return playerOneDetails;
    }

    public static PlayerDetailsModel getPlayerTwoDetails() {

        return playerTwoDetails;
    }
}