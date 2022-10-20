package com.boggle.game.boggle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EndRoundController implements Initializable {

    private List<String> _listOfCheckedWords;
    private List<String> _words;

    public EndRoundController(List<String> listOfCheckedWords, List<String> words) {

        _listOfCheckedWords=listOfCheckedWords;
        _words=words;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("EndRound.fxml"));

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

}
