package com.boggle.game.boggle;

import com.boggle.game.model.*;

import com.boggle.game.rmi.*;
import com.boggle.game.utils.DictionaryLoader;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.boggle.game.boggle.EndRoundController.static_overall_player_1;
import static com.boggle.game.boggle.HelloController.*;
import static com.boggle.game.boggle.HelloApplication.NEW_ROUND_MULTIPLAYER;

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
    private Label _player_nickname;

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
    private Label _lbScore2;
    @FXML
    private Label _lbScore22;
    @FXML
    private Label _lbOpponent;
    @FXML
    private Label _lbOpponent2;

    @FXML
    private Label _notAWord;
    @FXML
    private Button _endRound;
    @FXML
    private MenuItem _save_state;
    @FXML
    private MenuItem _load_state;

    public Button gameBoard[][];


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
    public int _time = 140;

    private static int time_const = 121;
    private String _currentWord = "";
    public Timeline _timeline;

    private static List<String> _listOfCheckedWords;
    public static List<String> player_1_listOfCheckedWords;
    public static List<String> player_2_listOfCheckedWords;
    private static List<String> _listOfCheckedWords_temp;

    private GameServerImpl gameServer;
    private GameServer gameClient;
    private PointsModel _addPoints;
    private Integer scoreYour = 0;
    private Integer scoreOpponent = 0;
    private ArrayList<String> dictionary;
    private ServerConnectionManager serverConnectionManager;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        makeLabelsVisible(false);

        DictionaryLoader dl = new DictionaryLoader();
        try {
            dictionary = dl.loadDictionary("src/main/resources/HR_DIC_UPC.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        _lbScore2.setText("0");

        initializeBoard();

        _player_nickname.setText(getPlayerDetails().getPlayerName());

        if (CLIENT == true){
            GAME_LOADED = false;
        }


        if (SINGLE_PLAYER || SERVER || GAME_LOADED) {

            try {
                setUpCubes();



                    if (SERVER){


                        if (NEW_ROUND_MULTIPLAYER == false){
                            this.gameServer = new GameServerImpl();
                            serverConnectionManager = new ServerConnectionManager(gameServer);
                        }

                        makeLabelsVisible(true);

                        gameServer.sendGameBoard(_charArray, _solver);
                        gameServer.sendPlayer_1_name(_player_nickname.getText());

                    }


                setUpBoggleGrid();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (CLIENT) {

            try {

                makeLabelsVisible(true);

                ClientConnectionManager clientConnectionManager = new ClientConnectionManager();


                gameClient = clientConnectionManager.getLookupNamingContext();
                gameClient.sendPlayer_2_name(_player_nickname.getText());

                System.out.println(gameClient);

                _charArray = gameClient.getGameBoard();

                _solver = gameClient.getBoggleSolver();


                Platform.runLater(() -> {
                    for(int i = 0; i < GAME_BOARD_HEIGHT; i++) {
                        for(int j = 0; j < GAME_BOARD_WIDTH; j++) {
                            gameBoard[i][j].setText(String.valueOf(_charArray[i][j]));
                        }
                    }
                });

                new PreviewBoardController();

                setUpBoggleGrid();

            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }




    public void makeLabelsVisible(boolean bool) {
        this._lbScore2.setVisible(bool);
        this._lbScore22.setVisible(bool);
        this._lbOpponent.setVisible(bool);
        this._lbOpponent2.setVisible(bool);
    }


    private void initializeBoard() {
        gameBoard = new Button[GAME_BOARD_HEIGHT][GAME_BOARD_WIDTH];
        _charArray = new char[GAME_BOARD_WIDTH][GAME_BOARD_HEIGHT];

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
    }

    public void setUpCubes() throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(() -> {
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
        });
        executorService.shutdown();
        new PreviewBoardController();
        _solver = new BoggleSolverModel(_charArray, dictionary);
    }

    public void setGameData(GameServerImpl gameServer, GameServer gameClient,ServerConnectionManager serverConnectionManager) {
        this.gameServer = gameServer;
        this.gameClient = gameClient;
        this.serverConnectionManager=serverConnectionManager;

    }


    public void setUpBoggleGrid() throws IOException {

        _gameOver = false;
        _isClicked = new boolean[_size][_size];
        _gridPane = new GridPane();
        _iStack = new Stack<>();
        _jStack = new Stack<>();
        _gridPane.setFocusTraversable(true);
        _addPoints = new PointsModel();
        _listOfCheckedWords = new ArrayList<>();
        _listOfCheckedWords_temp = new ArrayList<>();

       player_2_listOfCheckedWords = new ArrayList<>();
       player_1_listOfCheckedWords = new ArrayList<>();

        this.clearBoolArray();

        setUpTimeline();

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

        if (_timeline != null) {
            _timeline.stop();
            _timeline = null;
        }

        //Official boggle game time is set at 2 minutes
        _time = 140;
        KeyFrame kf = new KeyFrame(Duration.seconds(1), new TimeHandler());
        _timeline = new Timeline(kf);
        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();


    }

    /*
     * This is the private inner class that is the timehandler.
     */
    public class TimeHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            //When the timer reaches zero, the game will stop and the endGame method is called.
            if (_time == 0) {
                try {
                    endGame();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                _time = _time - 1;

                if (SERVER){
                    try {
                        gameServer.sendPlayer_1_score(scoreYour);
                       if (_time <= 130){
                           if (NEW_ROUND_MULTIPLAYER){
                               gameServer.sendTimeSync(_time);
                           }
                            _time = gameServer.getTimeSync();
                        }
                        scoreOpponent = gameServer.getPlayer_2_score();
                        if (scoreOpponent == null) {
                            scoreOpponent = 0;  // weird issue upon start - nullPointerExc must be rmi data speed so this snip is to overcome starting issue
                        }
                        _lbScore2.setText(scoreOpponent.toString());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } else if (CLIENT) {
                    try {
                        gameClient.sendPlayer_2_score(scoreYour);
                        //if (_time == 138 || _time == 100 || _time == 50 || _time == 139 || _time == 101 || _time == 51)  {
                        gameClient.sendTimeSync(_time);

                        //}
                        scoreOpponent = gameClient.getPlayer_1_score();
                        _lbScore2.setText(scoreOpponent.toString());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
                _timeLabel.setText("Time remaining: " + _time + " seconds");
            }
            event.consume();
        }
    }

    private void endGame() throws RemoteException {

                _listOfCheckedWords_temp = new ArrayList<String>(_listOfCheckedWords);
                getPlayerDetails().setRoundDetails(_listOfCheckedWords_temp, _solver.getWords(), scoreYour, roundCounter, static_overall_player_1);

                _listOfCheckedWords.clear();
                reset_states();

                //boolean is set to true to the key and click handlers and buttons know to not respond to inputs
                _gameOver = true;
                _timeline.stop();

        if(CLIENT || SERVER) {
           new EndRoundModel(gameServer, gameClient, serverConnectionManager);
        } else {
           new EndRoundModel(null, null  , null);
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

    public void buttonCheckWord(ActionEvent actionEvent) throws RemoteException {

        if (_solver.getWords().contains(_currentWord)) {

            _notAWord.setText("");

            if (!_listOfCheckedWords.contains(_currentWord) ) {
                //add points to player and display on board
                _addPoints.setPoints(_currentWord);
                scoreYour = _addPoints.getPoints();
                _lbScore.setText(scoreYour.toString());

                //add found words to pane
                _vBox.getChildren().add(new Label(_currentWord));
                _listOfCheckedWords.add(_currentWord);

                if (CLIENT && !player_2_listOfCheckedWords.contains(_currentWord)){
                    player_2_listOfCheckedWords.add(_currentWord);
                    gameClient.setPlayer_2_checked_words(player_2_listOfCheckedWords);
                } else if (SERVER  && !player_1_listOfCheckedWords.contains(_currentWord)) {
                    player_1_listOfCheckedWords.add(_currentWord);
                    gameServer.setPlayer_1_checked_words(player_1_listOfCheckedWords);


                }


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

        if (_listOfCheckedWords.contains(_currentWord) )
        {
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

        scoreYour = 0;
    }

}





