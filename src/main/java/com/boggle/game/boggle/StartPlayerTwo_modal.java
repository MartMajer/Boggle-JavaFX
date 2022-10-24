package com.boggle.game.boggle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class StartPlayerTwo_modal implements Initializable {


    @FXML
    private Button _start;




    private Stage stage;

    public static Stage stage_m;

    public static boolean _startPlayerTwo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }

    public void StartPlayerTwo_modal(){

        Scene scene = null;


        try {

            Parent root = FXMLLoader.load(HelloApplication.class.getResource("StartPlayerTwo.fxml"));
            scene = new Scene(root);
             stage_m = new Stage();
            this.stage = stage_m;

            stage_m.setTitle("Hello!");
            stage_m.setScene(scene);

            stage_m.initModality(Modality.APPLICATION_MODAL);

            stage_m.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = HelloApplication.getMainStage();

    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {

        _startPlayerTwo = false;



        Stage stage = (Stage) _start.getScene().getWindow();
        stage.close();

    }




}
