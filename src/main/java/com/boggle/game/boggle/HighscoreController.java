package com.boggle.game.boggle;

import com.boggle.game.model.HighscoreModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class HighscoreController implements Initializable, Serializable {

    @FXML
    private VBox _highScore_name;
    @FXML
    private VBox _highScore_score;

    public static ArrayList<HighscoreModel> arrayList_Highscore = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        Collections.sort(arrayList_Highscore);

       while(arrayList_Highscore.size() > 10){

            // Calculate index of last element
            int index = arrayList_Highscore.size() - 1;

            arrayList_Highscore.remove(index);
        }




        for (var item :
                arrayList_Highscore ) {
            _highScore_score.getChildren().add(new Label(item.toString()));
        }

    }



    public void highScoreCont(){

        Scene scene = null;

        try {


            Parent root = FXMLLoader.load(HelloApplication.class.getResource("HighScore.fxml"));
            scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Hello!");
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = HelloApplication.getMainStage();







    }


}
