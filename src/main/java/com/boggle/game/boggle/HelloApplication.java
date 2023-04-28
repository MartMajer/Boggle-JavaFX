package com.boggle.game.boggle;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloApplication extends Application {
    private HelloController controller;
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {


        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dayOfMonth = String.valueOf(day);


        SimpleDateFormat sdf = new SimpleDateFormat("d");
        String s = sdf.format(new Date( ));
        System.out.println(s);



        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("GameModeChooser.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Boggle!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        mainStage = stage;


    }
    @Override
    public void stop()
    {

        this.controller.closeConnection();
        Platform.exit();
        System.exit(0);
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }
}