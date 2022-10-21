package com.boggle.game.boggle;

import com.boggle.game.model.Highscore;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import static com.boggle.game.boggle.GameScreenController._time;


public class StartPlayerTwo_modal implements Initializable {


    @FXML
    private Button _start;


    private int time;
    private Stage stage;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }

    public void StartPlayerTwo_modal(){

        Scene scene = null;

        this.time = time;

        try {

            Parent root = FXMLLoader.load(HelloApplication.class.getResource("StartPlayerTwo.fxml"));
            scene = new Scene(root);
            Stage stage = new Stage();
            this.stage = stage;

            stage.setTitle("Hello!");
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = HelloApplication.getMainStage();

    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        _time = 120;
        Stage stage = (Stage) _start.getScene().getWindow();
        stage.close();

    }




}
