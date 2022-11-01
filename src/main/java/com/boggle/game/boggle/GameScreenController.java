package com.boggle.game.boggle;

import com.boggle.game.model.*;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

import static com.boggle.game.boggle.HelloController.*;
import static com.boggle.game.boggle.StartPlayerTwo_modal._startPlayerTwo;
import static com.boggle.game.boggle.StartPlayerTwo_modal.stage_m;
import static com.boggle.game.model.StoredDetailsModel.*;

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
    private Label chosenLetter;
    @FXML
    private Label _currentWordLabel;
    @FXML
    private Button buttonCheckWord_btn;
    @FXML
    private VBox _vBox;
    @FXML
    private Label _lbScore;
    @FXML
    private Label _notAWord;
    @FXML
    private Button _endRound;
    @FXML
    private MenuItem _save_state;
    @FXML
    private MenuItem _load_state;

    private Button gameBoard[][];


    private BoggleSolverModel _solver;


    private static final int GAME_BOARD_WIDTH = 4;
    private static final int GAME_BOARD_HEIGHT = 4;
    public static char[][] _charArray;

    private GridPane _gridPane;
    private boolean[][] _isClicked;
    private Stack<Integer> _iStack;
    private Stack<Integer> _jStack;


    private boolean _gameOver;
    private int _size = 4;
    public int _time;

    private static int time_const = 121;
    private String _currentWord = "";

    public Timeline _timeline;

    private static List<String> _listOfCheckedWords;
    private static List<String> _listOfCheckedWords_temp;

    StartPlayerTwo_modal modal = new StartPlayerTwo_modal();

    private AddPointsModel _addPoints;

    public int controlInt = 1;


    private Integer score = 0;


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

    public void setUpBoard() throws IOException {

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
        new PreviewBoardController();
        _solver = new BoggleSolverModel(_charArray);


    }


    public void boggle() throws IOException {

        _gameOver = false;
        _isClicked = new boolean[_size][_size];
        _gridPane = new GridPane();
        _iStack = new Stack<>();
        _jStack = new Stack<>();
        _gridPane.setFocusTraversable(true);
        _addPoints = new AddPointsModel();
        _listOfCheckedWords = new ArrayList<>();
        _listOfCheckedWords_temp = new ArrayList<>();


        this.clearBoolArray();


        if (_startPlayerTwo == false) {
            this.setUpTimeline();
        }


    }

    /*
     * This method loops through and clears the isClicked boolean array
     */
    private void clearBoolArray() {
        for (int i = 0; i < _size; i++) {
            for (int j = 0; j < _size; j++) {
                _isClicked[i][j] = false;
            }
        }
    }

    public void setUpTimeline() {
        //Official boggle game time is set at 2 minutes
        _time = 121;
        KeyFrame kf = new KeyFrame(Duration.seconds(1), new TimeHandler());
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

            } else {
                _time = _time - 1;

                _timeLabel.setText("Time remaining: " + _time + " seconds");
            }
            event.consume();
        }
    }

    private void endGame() {

        switch (controlInt) {
            case 1:

                //////////////// store player 1 details and start player 2 game! //////////////
                ///////////////////////////////////////////////////////////////////////////////

                _listOfCheckedWords_temp = new ArrayList<String>(_listOfCheckedWords);
                getPlayerOneDetails().setRoundDetails(_listOfCheckedWords_temp, _solver._wordsFound, score);


                _player_1.setText("");
                _player_2.setText(getPlayerTwoDetails().getPlayerName());

                _listOfCheckedWords.clear();

                setUpTimeline();

                reset_states();

                //Timeline is stopped
                _timeline.stop();

                _startPlayerTwo = true;

                ///////modal for mid round pause
                modal.StartPlayerTwo_modal();

                delay(1, () -> _time = 121);// _timeLabel.setText("Time remaining: 120 seconds") );
                delay(1, () -> _timeLabel.setVisible(false));

                stage_m.setOnHidden(e -> _time = 121);
                stage_m.setOnHidden(e -> _timeLabel.setVisible(true));


                break;

            case 2:

                ///////////////// store player 2 details ////////////////////////////////////////

                getPlayerTwoDetails().setRoundDetails(_listOfCheckedWords, _solver._wordsFound, score);

                reset_states();

                //Timeline is stopped
                _timeline.stop();


                //boolean is set to true to the key and click handlers and buttons know to not respond to inputs
                _gameOver = true;


                new EndRoundModel();
        }


        controlInt++;

        if (controlInt > 50000) {
            controlInt = 5;
        }

    }


    private void addLetterToLabel(int i, int j) {

        _currentWord = _currentWord + _charArray[i][j];
        _currentWordLabel.setText("Current word: " + _currentWord);

    }


    public void buttonPressed_end_round(ActionEvent event) {

        _time = 0;

    }


    public void buttonPressed(Event q) {

        //adds clicked button to label and stores it for later comparing
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                if (q.getSource() == gameBoard[i][j]) {
                    addLetterToLabel(i, j);
                }
            }
        }
    }

    public void buttonCheckWord(ActionEvent actionEvent) {

        if (_solver._wordsFound.contains(_currentWord)) {
            _notAWord.setText("");

            if (!_listOfCheckedWords.contains(_currentWord)) {
                //add points to player and display on board
                _addPoints.setPoints(_currentWord);
                score = _addPoints.getPoints();
                _lbScore.setText(score.toString());

                //add found words to pane
                _vBox.getChildren().add(new Label(_currentWord));
                _listOfCheckedWords.add(_currentWord);


                //reset labels
                _currentWord = "";
                _currentWordLabel.setText("Current word: ");
            }
        } else {
            _notAWord.setText("NOT A WORD! - " + _currentWord);

            //reset labels
            _currentWord = "";
            _currentWordLabel.setText("Current word: ");
        }


    }


    private void reset_states() {


        _currentWord = "";
        _lbScore.setText("");

        _addPoints.setPoints();

        _currentWordLabel.setText("Current word: ");
        _vBox.getChildren().clear();

        score = 0;
    }

    public char[][] getBoardLetters() {

        return _charArray;

    }


    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }


    public void btn_save() {

        StoredDetailsModel model = new StoredDetailsModel(
                getPlayerOneDetails().getPlayerName(),
                getPlayerTwoDetails().getPlayerName(),
                getPlayerOneDetails().get_score_int(),
                getPlayerTwoDetails().get_score_int(),
                roundCounter.toString(),
                _time);


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("boggle-board.ser"))) {
            oos.writeObject(model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void btn_load() throws IOException, ClassNotFoundException {


        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("boggle-board.ser"))) {
            StoredDetailsModel model = (StoredDetailsModel) ois.readObject();

            _solver._wordsFound.clear();
            _solver._wordsFound = _wordsFound_stored;

            _player_1.setText(P1);
            _player_2.setText(P2);

            playerOneDetails.playerName = P1;
            playerTwoDetails.playerName = P2;

            _time = _time_stored;


        }
    }
}





