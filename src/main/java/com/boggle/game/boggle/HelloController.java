package com.boggle.game.boggle;


import com.boggle.game.model.PlayerDetails;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TextField playerOneNameTextField;

    @FXML
    private TextField playerTwoNameTextField;

    @FXML
    private RadioButton xRadioButton;

    @FXML
    private RadioButton oRadioButton;

    private static PlayerDetails playerOneDetails;

    private static PlayerDetails playerTwoDetails;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup radioButtonGroup = new ToggleGroup();
    }

    public void startGame() {
        String playerOneName = playerOneNameTextField.getText();
        String playerTwoName = playerTwoNameTextField.getText();

        playerOneDetails = new PlayerDetails(playerOneName);

        playerTwoDetails = new PlayerDetails(playerTwoName);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameScreen.fxml"));

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

    public static PlayerDetails getPlayerOneDetails() {
        return playerOneDetails;
    }

    public static PlayerDetails getPlayerTwoDetails() {
        return playerTwoDetails;
    }
}