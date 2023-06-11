package com.boggle.game.boggle;

import com.boggle.game.model.HighscoreModel;
import com.boggle.game.model.StoredDetailsModel;
import com.boggle.game.rmi.ClientConnectionManager;
import com.boggle.game.rmi.GameServer;
import com.boggle.game.rmi.GameServerImpl;
import com.boggle.game.rmi.ServerConnectionManager;
import com.boggle.game.socket.IServer;
import com.boggle.game.socket.ServerStream;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.naming.NamingException;
import java.io.*;
import java.lang.reflect.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static com.boggle.game.boggle.GameScreenController.player_1_listOfCheckedWords;
import static com.boggle.game.boggle.GameScreenController.player_2_listOfCheckedWords;
import static com.boggle.game.boggle.HelloController.*;
import static com.boggle.game.boggle.HighscoreController.arrayList_Highscore;
import static com.boggle.game.model.StoredDetailsModel.overall_P1;
import static com.boggle.game.boggle.HelloApplication.NEW_ROUND_MULTIPLAYER;



public class EndRoundController implements Initializable {

    @FXML
    private Button _board;

    @FXML
    private Button _highscore;
    @FXML
    private Button _startNewRound;
    @FXML
    private Button _startNewRound2;

    @FXML
    private Label _singleplayer_name;
    @FXML
    private Label _player_1_name;
    @FXML
    private Label _player_2_name;

    @FXML
    private Label _singleplayer_RoundScore;

    @FXML
    private Label _player_1_RoundScore;
    @FXML
    private Label _player_2_RoundScore;

    @FXML
    private Label _singleplayer_Overall;
    @FXML
    private Label _player_1_Overall;
    @FXML
    private Label _player_2_Overall;

    @FXML
    private VBox __singleplayer_Pane_Found;
    @FXML
    private VBox __multiplayer_Pane_Found;
    @FXML
    private VBox __player_2_Pane_Found;
    @FXML
    private VBox __player_1_Pane_Found;
    @FXML
    private VBox __player_Pane_Possible;
    @FXML
    private VBox __multiplayer_Pane_Possible;
    @FXML
    private VBox __player_2_Pane_Possible;

    @FXML
    private Label _roundNumber;
    @FXML
    private Label _roundNumber2;

    @FXML
    private AnchorPane ap_singleplayer;

    @FXML
    private AnchorPane ap_multiplayer_2;

    @FXML
    private AnchorPane ap_multiplayer_3;

    @FXML
    private AnchorPane ap_multiplayer_4;

    @FXML
    private MenuItem mi_mainmenu;
    private HighscoreController highscoreController = new HighscoreController();

    private StoredDetailsModel store;

    private static Stage stage;
    public static Integer static_overall_player_1;
    public static Integer static_overall_player_2;

    private ServerConnectionManager serverConnectionManager;
    private ClientConnectionManager clientConnectionManager;
    private GameServer gameClient;
    private GameServerImpl gameServer;
    private String testString;

    public Integer temp2 = 0;
    private int _time;
    private Timeline _timeline;
    @FXML
    private Label _timeLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {





        _singleplayer_name.setText(getPlayerDetails().getPlayerName());

        _singleplayer_RoundScore.setText(getPlayerDetails().get_score());

        if (GAME_LOADED == true){

            _board.setDisable(true);
            _highscore.setDisable(true);

        }





        Integer temp = roundCounter - 1;
        _roundNumber.setText(temp.toString());
        _roundNumber2.setText(temp.toString());

        arrayList_Highscore.add(new HighscoreModel(getPlayerDetails().get_score_int(), getPlayerDetails().getPlayerName()));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("highscore.ser"))) {

                oos.writeObject(arrayList_Highscore);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (var word : getPlayerDetails().get_listOfCheckedWords()) {
            //add found words to pane
            __singleplayer_Pane_Found.getChildren().add(new Label(word));
            if (CLIENT || SERVER){
                __player_1_Pane_Found.getChildren().add(new Label(word));
            }

        }

        for (var word : getPlayerDetails().get_PossibleWords()) {
            //add found words to pane
            __player_Pane_Possible.getChildren().add(new Label(word));
            __multiplayer_Pane_Possible.getChildren().add(new Label(word));
        }

      if (CLIENT){



            for (var word : player_1_listOfCheckedWords) {


                __player_2_Pane_Found.getChildren().add(new Label(word));

            }
        } else if (SERVER) {

            for (var word : player_2_listOfCheckedWords) {

                __player_2_Pane_Found.getChildren().add(new Label(word));

            }
        }

        Integer temp1 = 0;

        if (GAME_LOADED){
             temp1 = getPlayerDetails().get_score_int();
        }else {
             temp1 = getPlayerDetails().get_score_int() + overall_P1;

        }

        static_overall_player_1 = temp1;

        _singleplayer_Overall.setText(static_overall_player_1.toString());

        if (SINGLE_PLAYER == true && SERVER == false) {
            ap_singleplayer.setVisible(true);
        }else {

            NEW_ROUND_MULTIPLAYER = true;

            setUpTimeline();

            ap_multiplayer_2.setVisible(true);

            _player_1_name.setText(getPlayerDetails().getPlayerName());
            _player_1_RoundScore.setText(getPlayerDetails().get_score());
            _player_1_Overall.setText(static_overall_player_1.toString());
        }


    }

    public void savegame_menuitem(ActionEvent actionEvent) throws RuntimeException {

        roundCounter += 1;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("playerdetails.ser"))) {

            oos.writeObject(playerDetails);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void mainmenu_return(ActionEvent actionEvent) throws IOException {

        roundCounter = 1;

        HelloApplication main_menu = new HelloApplication();
        main_menu.start(HelloApplication.getMainStage());

    }


    public void startNewRound(ActionEvent actionEvent) throws RemoteException {

        HelloController hello = new HelloController();

        Integer temp = roundCounter - 1;

        store = new StoredDetailsModel(_singleplayer_name.getText(), getPlayerDetails().get_score_int(),temp.toString());

        if (SINGLE_PLAYER || GAME_LOADED )
        {
            GAME_LOADED=false;
            hello.startGame();

        } else if (SERVER) {


        }

    }



    public void highscore(ActionEvent actionEvent) {
        highscoreController.highScoreCont();
    }

    public void board(ActionEvent actionEvent) {

        Scene scene = null;

        try {


            Parent root = FXMLLoader.load(HelloApplication.class.getResource("PreviewBoard.fxml"));
            scene = new Scene(root);
            Stage stage = new Stage();

            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.setResizable(false);

            stage.show();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = HelloApplication.getMainStage();



    }

    public void setGameData(GameServerImpl gameServer, GameServer gameClient,ServerConnectionManager serverConnectionManager, String test) {
       this.gameServer = gameServer;
       this.gameClient = gameClient;
       this.serverConnectionManager=serverConnectionManager;
       this.testString = test;

        multiplayerSetup();
    }

    private void multiplayerSetup(){

        Platform.runLater(() -> {
            if (CLIENT || SERVER )
            {

                if (SERVER){
                    try {

                        _player_2_name.setText(gameServer.getPlayer_2_name());
                        _player_2_RoundScore.setText(gameServer.getPlayer_2_score().toString());

                        gameServer.addOverallScorePlayer2(gameServer.getPlayer_2_score());

                        _player_2_Overall.setText(gameServer.getOverallScorePlayer2().toString());

                        if (gameServer.getPlayer_2_checked_words() != null){
                            player_2_listOfCheckedWords = new ArrayList<>(gameServer.getPlayer_2_checked_words());
                        }

                        for (var word : player_2_listOfCheckedWords) {

                            __player_2_Pane_Found.getChildren().add(new Label(word));
                        }

                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

                else if (CLIENT){

                    _startNewRound2.setVisible(false);

                    try {
                        clientConnectionManager = new ClientConnectionManager();

                        _player_2_name.setText(gameClient.getPlayer_1_name());
                        _player_2_RoundScore.setText(gameClient.getPlayer_1_score().toString());

                        gameClient.addOverallScorePlayer1(gameClient.getPlayer_1_score());

                        _player_2_Overall.setText(gameClient.getOverallScorePlayer1().toString());

                        if (gameClient.getPlayer_1_checked_words() != null){
                            player_1_listOfCheckedWords = new ArrayList<>(gameClient.getPlayer_1_checked_words());
                        }

                        for (var word : player_1_listOfCheckedWords) {

                            __player_2_Pane_Found.getChildren().add(new Label(word));
                        }

                    } catch (NamingException e) {
                        throw new RuntimeException(e);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }







    public void generateDocumentation() throws ClassNotFoundException {

        try (FileWriter fileWriter = new FileWriter(new File("documentation.html"))) {

            List<Path> filesList = Files.walk(Path.of(".")).collect(Collectors.toList());

            System.out.println("Files list:");

            filesList = filesList.stream().filter(p ->
                            (p.getFileName().toString().endsWith(".class")
                                    && p.getFileName().toString().compareTo("module-info.class") != 0))
                    .collect(Collectors.toList());

            List<String> fqnList = new ArrayList<>();

            for(Path path : filesList) {
                System.out.println(path.toFile().getAbsolutePath().toString());
                StringTokenizer tokenizer = new StringTokenizer(path.toFile().getAbsolutePath().toString(), "\\");

                Boolean startJoining = false;
                String fqn = "";

                while(tokenizer.hasMoreTokens()) {
                    String newToken = tokenizer.nextToken();

                    if("classes".equals(newToken)) {
                        startJoining = true;
                        continue;
                    }

                    if(startJoining) {

                        if(newToken.contains(".class")) {
                            fqn += newToken.substring(0, newToken.lastIndexOf("."));
                        }
                        else {
                            fqn += newToken + ".";
                        }
                    }

                    System.out.println("Token: " + newToken);
                }

                System.out.println("FQN: " + fqn);

                fqnList.add(fqn);
            }

            StringBuilder classInfo = new StringBuilder();

            for(String fqn : fqnList) {
                Class clazz = Class.forName(fqn);

                classInfo.append("<h2>" + clazz.getSimpleName() + "</h2><br />");

                Field[] fields = clazz.getDeclaredFields();

                for(Field field : fields) {
                    classInfo.append("<h3>" + Modifier.toString(field.getModifiers()) + " " + field.getName() + "</h3>");
                }

                Constructor[] constructors = clazz.getConstructors();

                for(Constructor constructor : constructors) {

                    String paramsString = "";

                    for(int i = 0; i < constructor.getParameters().length; i++) {
                        Parameter p = constructor.getParameters()[i];
                        paramsString += Modifier.toString(p.getModifiers()) + " " + p.getType().getSimpleName()
                                + " " + p.getName();

                        if(i < (constructor.getParameters().length - 1)) {
                            paramsString += ", ";
                        }
                    }

                    classInfo.append("<h3>" + Modifier.toString(constructor.getModifiers()) + " " + constructor.getDeclaringClass().getSimpleName()
                            + " (" + paramsString + ")</h3>");
                }

                //

                Method[] methods = clazz.getDeclaredMethods();

                for(Method method : methods) {

                    String paramsString = "";

                    for(int i = 0; i < method.getParameters().length; i++) {
                        Parameter p = method.getParameters()[i];
                        paramsString += Modifier.toString(p.getModifiers()) + " " + p.getType().getSimpleName()
                                + " " + p.getName();

                        if(i < (method.getParameters().length - 1)) {
                            paramsString += ", ";
                        }
                    }

                    classInfo.append("<h3>" + Modifier.toString(method.getModifiers()) + " " + method.getName()
                            + " (" + paramsString + ")</h3>");
                }

                //
            }


            fileWriter.append("<!DOCTYPE html>")
                    .append("<html>")
                    .append("<head>")
                    .append("<title>HTML Documentation</title>")
                    .append("</head>")
                    .append("<body>")
                    .append("<h1>Class list</h1>")
                    .append(classInfo.toString())
                    .append("</body>")
                    .append("</html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }



    public void setUpTimeline() {
        //Official boggle game time is set at 2 minutes
        _time = 20;
        KeyFrame kf = new KeyFrame(Duration.seconds(1), new EndRoundController.TimeHandler());
        _timeline = new Timeline(kf);
        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();


    }

    /*
     * This is the private inner class that is the timehandler.
     */
    public class TimeHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {

            //When the timer reaches zero, the game will stop and the endGame method is called.

            if (_time == 1) {

                Integer temp = roundCounter - 1;

                store = new StoredDetailsModel(_singleplayer_name.getText(), getPlayerDetails().get_score_int(),temp.toString());


                NEW_ROUND_MULTIPLAYER = true;
                _timeline.stop();

                if (_timeline != null) {
                    _timeline.stop();
                    _timeline = null;
                }

                try {
                    newRoundStart();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

            } else {

                _time = _time - 1;
                _timeLabel.setText("Next round in: " + _time);
            }
            event.consume();
        }
    }

    public synchronized void newRoundStart() throws RemoteException {
        HelloController hello = new HelloController();
        hello.newRoundMultiplayer(gameServer, gameClient, serverConnectionManager, CLIENT, SERVER);
        hello.startGame();

    }





}
