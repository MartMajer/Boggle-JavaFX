package com.boggle.game.boggle;

import com.boggle.game.model.BoggleSolver;
import com.boggle.game.model.EndRound;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;

import static com.boggle.game.boggle.HelloController.getPlayerOneDetails;

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

    @FXML
    private Label _timeLabel;
    @FXML
    private Label _player_1;
    @FXML
    private Label _player_2;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label chosenLetter;





    private Button gameBoard[][];


    private BoggleSolver _solver;


    private static final int GAME_BOARD_WIDTH = 4;
    private static final int GAME_BOARD_HEIGHT = 4;
    public char[][] _charArray;




    private GridPane _gridPane;
    private boolean[][] _isClicked;
    private ArrayList<String> _userFoundWords;
    private ArrayList<Rectangle> _rectangles;
    private Stack<Integer> _iStack;
    private Stack<Integer> _jStack;
    private Label _scoreLabel;

    private Label _statusLabel;
    @FXML
    private Label _currentWordLabel;
    private boolean _gameOver;
    private int _size = 4;
    private int _time;
    private int _score;
    private String _currentWord;

    private Timeline _timeline;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        gameBoard = new Button[GAME_BOARD_HEIGHT][GAME_BOARD_WIDTH];
        _charArray = new char[GAME_BOARD_WIDTH][GAME_BOARD_HEIGHT];

        _player_1.setText(getPlayerOneDetails().getPlayerName());
        //_player_2.setText(getPlayerTwoDetails().getPlayerName());

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


        try {
            setUpBoard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            boggle();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

        public void setUpBoard () throws IOException {

            SecureRandom r = new SecureRandom();

            ArrayList<String> dice = new ArrayList<>();
            dice.add("ZONEOS");
            dice.add("AMOAJR");
            dice.add("KVETII");
            dice.add("DŽINRA");
            dice.add("EJRIEF");
            dice.add("PGVOUĐ");
            dice.add("KINECT");
            dice.add("MĆAIDT");
            dice.add("ETSŠAA");
            dice.add("IBJULS");
            dice.add("ADSOŽV");
            dice.add("OMANOJ");
            dice.add("ESAAMČ");
            dice.add("ALUJNP");
            dice.add("KRUZHI");
            dice.add("LEOGIE");

            char letter;

            for (int i = 0; i < GAME_BOARD_WIDTH; i++) {
                for (int j = 0; j < GAME_BOARD_HEIGHT; j++) {


                    int n = r.nextInt(dice.size());
                    letter = dice.get(n).charAt(r.nextInt(dice.get(n).length()));
                    dice.remove(n);

                    String s = Character.toString(letter);
                    gameBoard[i][j].setText(s);

                    //finally the character is added to the array of characters in the correct location
                    _charArray[i][j] = letter;

                }
            }

            _solver = new BoggleSolver(_charArray);


        }




    public void boggle() throws IOException {
        _score = 0;
        _gameOver = false;
        _isClicked = new boolean[_size][_size];
        _userFoundWords = new ArrayList<>();
        _gridPane = new GridPane();
        _iStack = new Stack<>();
        _jStack = new Stack<>();
        _gridPane.setFocusTraversable(true);

        this.clearBoolArray();


        this.setUpTimeline();


    }

    /*
     * This method loops through and clears the isClicked boolean array
     */
    private void clearBoolArray() {
        for(int i = 0; i < _size; i++) {
            for (int j = 0; j < _size; j++) {
                _isClicked[i][j] = false;
            }
        }
    }

    private void setUpTimeline() {
        //Official boggle game time is set at 2 minutes
        _time = 12;
        KeyFrame kf = new KeyFrame(Duration.seconds(1), new  TimeHandler());
        _timeline = new Timeline(kf);
        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();
    }

    /*
     * This is the private inner class that is the timehandler.
     */
    private class TimeHandler implements EventHandler<ActionEvent> {



        @Override
        public void handle(ActionEvent event) {

            //When the timer reaches zero, the game will stop and the endGame method is called.
            if (_time == 0) {
                endGame();
            }
            else {
                _time = _time - 1;

                _timeLabel.setText("Time remaining: " + _time + " seconds");
            }
            event.consume();
        }
    }
    private void endGame() {
        _statusLabel.setText("Game ending...");
        //Timeline is stopped
        _timeline.stop();
        //boolean is set to true to the key and click handlers and buttons know to not respond to inputs
        _gameOver = true;
        //the postpane is created which displays all the found words
        new EndRound(_userFoundWords, _solver.getWords());
    }
    private boolean canAdd(int row, int col) {
        //handles the case there is nothing currently selected
        if (_iStack.empty() && _jStack.empty()) {
            return true;
        }
        //Checks if the row and col of the clicked letter is within 1 row/column of the last selected letter
        else if (!(_iStack.peek()-row == 0 && _jStack.peek()-col == 0) && (((_iStack.peek()-row == -1) || (_iStack.peek()-row == 0) || (_iStack.peek()-row == 1)) && ((_jStack.peek()-col == -1) || (_jStack.peek()-col == 0) || (_jStack.peek()-col == 1)))) {
            return true;
        }
        else {
            return false;
        }

    }

    private void addLetterToLabel(int i, int j) {
        _currentWord = _currentWord + _charArray[j][i];
        _currentWordLabel.setText("Current Word: " + _currentWord);
    }
    private boolean canRemove (int row, int col) {
        if ((_iStack.peek() == row) && (_jStack.peek() == col)) {
            return true;
        }
        else {
            return false;
        }
    }
    private void removeLetterFromLabel() {
        _currentWordLabel.setText("Current word: " + _currentWord.substring(0, _currentWord.length() - 1));
        _currentWord = _currentWord.substring(0, _currentWord.length() - 1);
    }
    public void buttonPressed(ActionEvent actionEvent) {



        mat_0_0.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

            addLetterToLabel(0, 0);

        });

        mat_0_1.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

            addLetterToLabel(0, 1);

        });

        mat_0_2.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

            addLetterToLabel(0, 0);

        });

        mat_0_3.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

            addLetterToLabel(0, 1);

        });










        _gridPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            //event handler does not respond if the game is over
            if (!_gameOver) {
                //gets the location of the rectangle that was clicked
                Node clicked = e.getPickResult().getIntersectedNode();
                int row = GridPane.getRowIndex(clicked);
                int col = GridPane.getColumnIndex(clicked);

                //checks if the clicked rectangle is already clicked and if its adjacent to an already clicked rectangle
                if (!_isClicked[row][col] && this.canAdd(row, col)) {
                    //sets color to red to indicate to the player that they have selected it
                    ((Rectangle) clicked).setStroke(Color.ORANGE);
                    _isClicked[row][col] = true;
                    //adds the location to the two Stacks so the game knows what the last selected rectangle is
                    _iStack.push(row);
                    _jStack.push(col);
                    //updates the label and string with the new letter
                    this.addLetterToLabel(row, col);

                }
                //if an already clicked rectangle is clicked
                else if (_isClicked[row][col] && this.canRemove(row, col)) {
                    //resets the color
                    ((Rectangle) clicked).setStroke(Color.WHITE);
                    //removes its location for the stack
                    _iStack.pop();
                    _jStack.pop();
                    _isClicked[row][col] = false;
                    //removes the letter from the current word
                    this.removeLetterFromLabel();
                }
                else {
                    return;
                }
            }
            e.consume();
        });




    }

    }





