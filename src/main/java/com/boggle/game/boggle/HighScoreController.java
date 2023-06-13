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

import static com.boggle.game.boggle.HelloController.arrayListHighscore;


public class HighScoreController implements Initializable, Serializable {

    @FXML
    private VBox highScoreName;
    @FXML
    private VBox highScoreScore;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        highscore_list();

        Collections.sort(arrayListHighscore);

        while (arrayListHighscore.size() > 10) {

            // Calculate index of last element
            int index = arrayListHighscore.size() - 1;

            arrayListHighscore.remove(index);
        }
        for (var item : arrayListHighscore) {

            highScoreScore.getChildren().add(new Label(item.toString()));
        }
    }


    public void highScoreCont() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("HighScore.fxml"));
            loader.setController(this);
            Parent root = loader.load();

            Scene scene = new Scene(root);
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


    private void highscore_list() {


        ArrayList<HighscoreModel> model;

        try {
            String userHome = System.getProperty("user.home");
            String filePath = userHome + File.separator + "Documents" + File.separator + "highscore.ser";
            File file = new File(filePath);


            if (!file.exists() || file.length() < 280) {
                file.createNewFile();
                //insert hardcoded users to populate list
                arrayListHighscore.add(new HighscoreModel(25, "Marac"));
                arrayListHighscore.add(new HighscoreModel(20, "Ivana"));
                arrayListHighscore.add(new HighscoreModel(15, "Marko"));
                arrayListHighscore.add(new HighscoreModel(10, "Lana"));
                arrayListHighscore.add(new HighscoreModel(7, "Robi"));
                arrayListHighscore.add(new HighscoreModel(3, "Lovorka"));
                arrayListHighscore.add(new HighscoreModel(15, "Miro"));
                arrayListHighscore.add(new HighscoreModel(32, "Koko"));
                arrayListHighscore.add(new HighscoreModel(1, "Damira"));
                arrayListHighscore.add(new HighscoreModel(0, "Ognjen"));

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {

                    oos.writeObject(arrayListHighscore);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    arrayListHighscore = (ArrayList<HighscoreModel>) ois.readObject();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
