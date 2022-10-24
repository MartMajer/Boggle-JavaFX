package com.boggle.game.model;

import com.boggle.game.boggle.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class EndRoundModel {

    public EndRoundModel() {


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
