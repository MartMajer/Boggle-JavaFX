package com.boggle.game.boggle;

import com.boggle.game.model.BoggleSolverModel;
import com.boggle.game.model.EndRoundModel;
import com.boggle.game.model.PointsModel;
import com.boggle.game.rmi.ClientConnectionManager;
import com.boggle.game.rmi.GameServer;
import com.boggle.game.rmi.GameServerImpl;
import com.boggle.game.rmi.ServerConnectionManager;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.boggle.game.boggle.EndRoundController.staticOverallPlayer1;
import static com.boggle.game.boggle.HelloController.*;


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
    private Label lblTime;
    @FXML
    private Label lblPlayerNickname;

    @FXML
    private Label lblChosenLetter;
    @FXML
    private Label lblCurrentWordLabel;
    @FXML
    private Button btnButtonCheckWord;
    @FXML
    private VBox vBoxFoundWords;
    @FXML
    private Label lblScore;
    @FXML
    private Label lblScorePlayer1;
    @FXML
    private Label lblScorePlayer2;
    @FXML
    private Label lblOpponentPlayer1;
    @FXML
    private Label lblOpponentPlayer2;

    @FXML
    private Label lblNotAWord;
    @FXML
    private Button btnEndRound;
    @FXML
    private MenuItem miSaveState;
    @FXML
    private MenuItem miLoadState;

    public Button[][] gameBoard;


    private BoggleSolverModel boggleSolver;


    private static final int GAME_BOARD_WIDTH = 4;
    private static final int GAME_BOARD_HEIGHT = 4;
    public static char[][] charArray;

    private GridPane gpBoggleGrid;
    private boolean[][] isClicked;
    private Stack<Integer> iStack;
    private Stack<Integer> jStack;


    private boolean isGameOver;
    private final int _size = 4;
    public int _time = 140;

    private String currentWord = "";
    public Timeline timeline;

    private static List<String> listOfCheckedWords;
    public static List<String> listOfCheckedWordsPlayer1;
    public static List<String> listOfCheckedWordsPlayer2;
    private static List<String> listOfCheckedWords_temp;

    private GameServerImpl gameServer;
    private GameServer gameClient;
    private PointsModel addPoints;
    private Integer scoreYour = 0;
    private Integer scoreOpponent = 0;
    private ArrayList<String> dictionary;
    private ServerConnectionManager serverConnectionManager;


    public GameScreenController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        makeLabelsVisible(false);

        DictionaryLoader dl = new DictionaryLoader();
        try {
            dictionary = dl.loadDictionary("src/main/resources/HR_DIC_UPC.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lblScorePlayer1.setText("0");

        initializeBoard();

        lblPlayerNickname.setText(getPlayerDetails().getPlayerName());

        if (CLIENT) {
            GAME_LOADED = false;
        }


        if (SINGLE_PLAYER || SERVER || GAME_LOADED) {

            try {

                setUpCubes();

                if (SERVER) {


                    if (!NEW_ROUND_MULTIPLAYER) {
                        this.gameServer = new GameServerImpl();
                        serverConnectionManager = new ServerConnectionManager(gameServer);
                    }

                    makeLabelsVisible(true);

                    gameServer.sendGameBoard(charArray, boggleSolver);
                    gameServer.setNamePlayer1(lblPlayerNickname.getText());

                }


                setUpBoggleGrid();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (CLIENT) {

            try {

                makeLabelsVisible(true);

                ClientConnectionManager clientConnectionManager = new ClientConnectionManager();


                gameClient = clientConnectionManager.getLookupNamingContext();
                gameClient.setNamePlayer2(lblPlayerNickname.getText());

                System.out.println(gameClient);

                charArray = gameClient.getGameBoard();

                boggleSolver = gameClient.getBoggleSolver();


                Platform.runLater(() -> {
                    for (int i = 0; i < GAME_BOARD_HEIGHT; i++) {
                        for (int j = 0; j < GAME_BOARD_WIDTH; j++) {
                            gameBoard[i][j].setText(String.valueOf(charArray[i][j]));
                        }
                    }
                });

                new PreviewBoardController();

                setUpBoggleGrid();

            } catch (Exception e) {
                System.err.println("Client exception: " + e);
                e.printStackTrace();
            }
        }
    }


    public void makeLabelsVisible(boolean bool) {
        this.lblScorePlayer1.setVisible(bool);
        this.lblScorePlayer2.setVisible(bool);
        this.lblOpponentPlayer1.setVisible(bool);
        this.lblOpponentPlayer2.setVisible(bool);
    }


    private void initializeBoard() {
        gameBoard = new Button[GAME_BOARD_HEIGHT][GAME_BOARD_WIDTH];
        charArray = new char[GAME_BOARD_WIDTH][GAME_BOARD_HEIGHT];

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
                    charArray[i][j] = letter;
                }
            }
        });
        executorService.shutdown();
        new PreviewBoardController();
        boggleSolver = new BoggleSolverModel(charArray, dictionary);
    }

    public synchronized void setGameData(GameServerImpl gameServer, GameServer gameClient, ServerConnectionManager serverConnectionManager) {
        this.gameServer = gameServer;
        this.gameClient = gameClient;
        this.serverConnectionManager = serverConnectionManager;

    }


    public void setUpBoggleGrid() throws IOException {

        isGameOver = false;
        isClicked = new boolean[_size][_size];
        gpBoggleGrid = new GridPane();
        iStack = new Stack<>();
        jStack = new Stack<>();
        gpBoggleGrid.setFocusTraversable(true);
        addPoints = new PointsModel();
        listOfCheckedWords = new ArrayList<>();
        listOfCheckedWords_temp = new ArrayList<>();

        listOfCheckedWordsPlayer2 = new ArrayList<>();
        listOfCheckedWordsPlayer1 = new ArrayList<>();

        this.clearBoolArray();

        setUpTimeline();

    }

    /*
     * This method loops through and clears the isClicked boolean array
     */
    private void clearBoolArray() {
        for (int i = 0; i < _size; i++) {
            for (int j = 0; j < _size; j++) {
                isClicked[i][j] = false;
            }
        }
    }

    public void setUpTimeline() {

        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }

        //Official boggle game time is set at 2 minutes
        _time = 140;
        KeyFrame kf = new KeyFrame(Duration.seconds(1), new TimeHandler());
        timeline = new Timeline(kf);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


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

                if (SERVER) {
                    try {
                        gameServer.setScorePlayer1(scoreYour);
                        if (_time <= 130) {
                            if (NEW_ROUND_MULTIPLAYER) {
                                gameServer.sendTimeSync(_time);
                            }
                            _time = gameServer.getTimeSync();
                        }
                        scoreOpponent = gameServer.getScorePlayer2();
                        if (scoreOpponent == null) {
                            scoreOpponent = 0;  // weird issue upon start - nullPointerExc must be rmi data speed so this snip is to overcome starting issue
                        }
                        lblScorePlayer1.setText(scoreOpponent.toString());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } else if (CLIENT) {
                    try {

                        gameClient.setScorePlayer2(scoreYour);
                        //if (_time == 138 || _time == 100 || _time == 50 || _time == 139 || _time == 101 || _time == 51)  {
                        gameClient.sendTimeSync(_time);

                        //}
                        scoreOpponent = gameClient.getScorePlayer1();
                        lblScorePlayer1.setText(scoreOpponent.toString());
                    } catch (RemoteException e) {

                        _time = 0;
                        Platform.runLater(() -> {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Dialog");
                            alert.setHeaderText("Connection Error");
                            alert.setContentText("The server has disconnected. The application will now exit.");

                            alert.showAndWait();

                            // Exit the application:
                            Platform.exit();
                            System.exit(0);
                        });


                    }
                }
                lblTime.setText("Time remaining: " + _time + " seconds");
            }
            event.consume();
        }
    }

    private void endGame() throws RemoteException {

        listOfCheckedWords_temp = new ArrayList<String>(listOfCheckedWords);
        getPlayerDetails().setRoundDetails(listOfCheckedWords_temp, boggleSolver.getWords(), scoreYour, roundCounter, staticOverallPlayer1);

        listOfCheckedWords.clear();
        reset_states();

        //boolean is set to true to the key and click handlers and buttons know to not respond to inputs
        isGameOver = true;
        timeline.stop();

        if (CLIENT || SERVER) {
            new EndRoundModel(gameServer, gameClient, serverConnectionManager);
        } else {
            new EndRoundModel(null, null, null);
        }

    }


    private void addLetterToLabel(int i, int j) {

        currentWord = currentWord + charArray[i][j];
        lblCurrentWordLabel.setText("Current word: " + currentWord);

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

        if (boggleSolver.getWords().contains(currentWord)) {

            lblNotAWord.setText("");

            if (!listOfCheckedWords.contains(currentWord)) {
                //add points to player and display on board
                addPoints.setPoints(currentWord);
                scoreYour = addPoints.getPoints();
                lblScore.setText(scoreYour.toString());

                //add found words to pane
                vBoxFoundWords.getChildren().add(new Label(currentWord));
                listOfCheckedWords.add(currentWord);

                if (CLIENT && !listOfCheckedWordsPlayer2.contains(currentWord)) {
                    listOfCheckedWordsPlayer2.add(currentWord);
                    gameClient.setCheckedWordsPlayer2(listOfCheckedWordsPlayer2);
                } else if (SERVER && !listOfCheckedWordsPlayer1.contains(currentWord)) {
                    listOfCheckedWordsPlayer1.add(currentWord);
                    gameServer.setCheckedWordsPlayer1(listOfCheckedWordsPlayer1);


                }


                //reset labels
                currentWord = "";
                lblCurrentWordLabel.setText("Current word: ");

            }
        } else {
            lblNotAWord.setText("NOT A WORD! - " + currentWord);

            //reset labels
            currentWord = "";
            lblCurrentWordLabel.setText("Current word: ");
        }

        if (listOfCheckedWords.contains(currentWord)) {
            currentWord = "";
            lblCurrentWordLabel.setText("Current word: ");
        }
    }

    private void reset_states() {

        currentWord = "";
        lblScore.setText("");

        addPoints.setPoints();

        lblCurrentWordLabel.setText("Current word: ");
        vBoxFoundWords.getChildren().clear();

        scoreYour = 0;
    }

}





