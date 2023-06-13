package com.boggle.game.boggle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static com.boggle.game.boggle.GameScreenController.charArray;

public class PreviewBoardController implements Initializable {

    @FXML
    private Label mat_0_01;
    @FXML
    private Label mat_1_01;
    @FXML
    private Label mat_2_01;
    @FXML
    private Label mat_3_01;

    @FXML
    private Label mat_0_11;
    @FXML
    private Label mat_1_11;
    @FXML
    private Label mat_2_11;
    @FXML
    private Label mat_3_11;

    @FXML
    private Label mat_0_21;
    @FXML
    private Label mat_1_21;
    @FXML
    private Label mat_2_21;
    @FXML
    private Label mat_3_21;

    @FXML
    private Label mat_0_31;
    @FXML
    private Label mat_1_31;
    @FXML
    private Label mat_2_31;
    @FXML
    private Label mat_3_31;


    private char[][] gameBoard;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameBoard = charArray.clone();

        mat_0_01.setText(Character.toString(gameBoard[0][0]));
        mat_0_11.setText(Character.toString(gameBoard[0][1]));
        mat_0_21.setText(Character.toString(gameBoard[0][2]));
        mat_0_31.setText(Character.toString(gameBoard[0][3]));

        mat_1_01.setText(Character.toString(gameBoard[1][0]));
        mat_1_11.setText(Character.toString(gameBoard[1][1]));
        mat_1_21.setText(Character.toString(gameBoard[1][2]));
        mat_1_31.setText(Character.toString(gameBoard[1][3]));

        mat_2_01.setText(Character.toString(gameBoard[2][0]));
        mat_2_11.setText(Character.toString(gameBoard[2][1]));
        mat_2_21.setText(Character.toString(gameBoard[2][2]));
        mat_2_31.setText(Character.toString(gameBoard[2][3]));

        mat_3_01.setText(Character.toString(gameBoard[3][0]));
        mat_3_11.setText(Character.toString(gameBoard[3][1]));
        mat_3_21.setText(Character.toString(gameBoard[3][2]));
        mat_3_31.setText(Character.toString(gameBoard[3][3]));


    }

}
