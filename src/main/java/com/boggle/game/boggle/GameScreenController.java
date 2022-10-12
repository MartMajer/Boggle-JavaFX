package com.boggle.game.boggle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameScreenController implements Initializable {

    @FXML
    private Button mat_0_0;
    @FXML
    private Button mat_1_0;
    @FXML
    private Button mat_2_0;
    @FXML
    private Button mat_3_0;

    @FXML
    private Button mat_0_1;
    @FXML
    private Button mat_1_1;
    @FXML
    private Button mat_2_1;
    @FXML
    private Button mat_3_1;

    @FXML
    private Button mat_0_2;
    @FXML
    private Button mat_1_2;
    @FXML
    private Button mat_2_2;
    @FXML
    private Button mat_3_2;

    @FXML
    private Button mat_0_3;
    @FXML
    private Button mat_1_3;
    @FXML
    private Button mat_2_3;
    @FXML
    private Button mat_3_3;

    private Button gameBoard[][];

    private static final int GAME_BOARD_WIDTH = 4;
    private static final int GAME_BOARD_HEIGHT = 4;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        gameBoard = new Button[GAME_BOARD_HEIGHT][GAME_BOARD_WIDTH];



        gameBoard[0][0] = mat_0_0;
        gameBoard[0][1] = mat_0_1;
        gameBoard[0][2] = mat_0_2;
        gameBoard[0][3] = mat_0_3;

        gameBoard[1][0] = mat_1_0;
        gameBoard[1][1] = mat_1_1;
        gameBoard[1][2] = mat_1_2;
        gameBoard[1][3] = mat_1_3;

        gameBoard[2][0] = mat_2_0;
        gameBoard[2][1] = mat_2_1;
        gameBoard[2][2] = mat_2_2;
        gameBoard[2][3] = mat_2_3;

        gameBoard[3][0] = mat_3_0;
        gameBoard[3][1] = mat_3_1;
        gameBoard[3][2] = mat_3_2;
        gameBoard[3][3] = mat_3_3;

        setUpBoard();

    }

    public void setUpBoard(){

        SecureRandom r = new SecureRandom();

        ArrayList<String> dice = new ArrayList<>();
        dice.add("AEANEG");
        dice.add("AHSPCO");
        dice.add("ASPFFK");
        dice.add("OBJOAB");
        dice.add("IOTMUC");
        dice.add("RYVDEL");
        dice.add("LREIXD");
        dice.add("EIUNES");
        dice.add("WNGEEH");
        dice.add("LNHNRZ");
        dice.add("TSTIYD");
        dice.add("OWTOAT");
        dice.add("ERTTYL");
        dice.add("TOESSI");
        dice.add("TERWHV");
        dice.add("NUIHMQ");


        char letter;

        for(int i = 0; i < GAME_BOARD_WIDTH; i++) {
            for(int j = 0; j < GAME_BOARD_HEIGHT; j++) {
                Label label = new Label();

                int n = r.nextInt(dice.size());
                letter = dice.get(n).charAt(r.nextInt(dice.get(n).length()));
                dice.remove(n);

                String s = Character.toString(letter);
                gameBoard[i][j].setText(s);

            }
        }

    }
}
