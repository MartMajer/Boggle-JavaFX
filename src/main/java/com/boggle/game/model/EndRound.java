package com.boggle.game.model;

import com.boggle.game.boggle.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EndRound {

    public EndRound() {


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
