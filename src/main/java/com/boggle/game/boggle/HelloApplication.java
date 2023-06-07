package com.boggle.game.boggle;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private HelloController controller;
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("GameModeChooser.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Boggle!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        mainStage = stage;
    }

    @Override
    public void stop() {
        if (controller != null) {
            controller.closeConnection();
        }
        Platform.exit();
        System.exit(0);
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}