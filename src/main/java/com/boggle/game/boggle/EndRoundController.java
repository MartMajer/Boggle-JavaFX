package com.boggle.game.boggle;

import com.boggle.game.model.HighscoreModel;
import com.boggle.game.model.PlayerDetailsModel;
import com.boggle.game.model.StoredDetailsModel;
import com.boggle.game.model.XmlManager;
import com.boggle.game.rmi.ClientConnectionManager;
import com.boggle.game.rmi.GameServer;
import com.boggle.game.rmi.GameServerImpl;
import com.boggle.game.rmi.ServerConnectionManager;
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
import javafx.scene.control.Menu;
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

import static com.boggle.game.boggle.GameScreenController.listOfCheckedWordsPlayer1;
import static com.boggle.game.boggle.GameScreenController.listOfCheckedWordsPlayer2;
import static com.boggle.game.boggle.HelloController.*;
import static com.boggle.game.model.StoredDetailsModel.overallScorePlayer1;


public class EndRoundController implements Initializable {

    @FXML
    private Button btnBoard;
    @FXML
    private Button btnHighscore;
    @FXML
    private Button btnStartNewRoundSingle;
    @FXML
    private Button btnStartNewRoundMulti;
    @FXML
    private Label btnSingleplayerName;
    @FXML
    private Label lblNamePlayer1;
    @FXML
    private Label lblnamePlayer2;
    @FXML
    private Label lblroundScoreSingleplayer;
    @FXML
    private Label lblroundScorePlayer1;
    @FXML
    private Label lblroundScorePlayer2;
    @FXML
    private Label lblsingleplayerOverall;
    @FXML
    private Label lbloverallPlayer1;
    @FXML
    private Label lbloverallPlayer2;
    @FXML
    private VBox vboxSingleplayerWordsFound;
    @FXML
    private VBox vboxMultiplayerWordsFound;
    @FXML
    private VBox vboxWordsFoundPlayer1;
    @FXML
    private VBox vboxWordsFoundPlayer2;
    @FXML
    private VBox vboxSingleplayerWordsPossible;
    @FXML
    private VBox vboxMultiplayerWordsPossible;
    @FXML
    private Label lblRoundNumberSingle;
    @FXML
    private Label lblRoundNumberMulti;
    @FXML
    private AnchorPane apSingleplayer;
    @FXML
    private AnchorPane apMultiplayer_2;
    @FXML
    private MenuItem mi_mainmenu;
    private final HighScoreController highscoreController = new HighScoreController();
    private StoredDetailsModel storedDetailsModel;
    public static Integer staticOverallPlayer1;
    private ServerConnectionManager serverConnectionManager;
    private ClientConnectionManager clientConnectionManager;
    private GameServer gameClient;
    private GameServerImpl gameServer;
    private int _time;
    private Timeline timeline;
    @FXML
    private Label lblTime;
    @FXML
    private AnchorPane apAbout;
    @FXML
    private Menu xmlMenubar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnSingleplayerName.setText(getPlayerDetails().getPlayerName());


        PlayerDetailsModel details = getPlayerDetails();
        if (details != null && details.getScore() != null) {
            lblroundScoreSingleplayer.setText(details.getScore().toString());
        }


        if (GAME_LOADED) {

            btnBoard.setDisable(true);
            btnHighscore.setDisable(true);

        }

        Integer temp = roundCounter - 1;
        lblRoundNumberSingle.setText(temp.toString());
        lblRoundNumberMulti.setText(temp.toString());

        arrayListHighscore.add(new HighscoreModel(getPlayerDetails().getScoreInt(), getPlayerDetails().getPlayerName()));

        String userHome = System.getProperty("user.home");
        String filePath = userHome + File.separator + "Documents" + File.separator + "highscore.ser";
        File file = new File(filePath);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {

            oos.writeObject(arrayListHighscore);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (var word : getPlayerDetails().getListOfCheckedWords()) {
            //add found words to pane
            vboxSingleplayerWordsFound.getChildren().add(new Label(word));
            if (CLIENT || SERVER) {
                vboxWordsFoundPlayer2.getChildren().add(new Label(word));
            }

        }

        for (var word : getPlayerDetails().getPossibleWords()) {
            //add found words to pane
            vboxSingleplayerWordsPossible.getChildren().add(new Label(word));
            vboxMultiplayerWordsPossible.getChildren().add(new Label(word));
        }

        if (CLIENT) {


            for (var word : listOfCheckedWordsPlayer1) {


                vboxWordsFoundPlayer1.getChildren().add(new Label(word));

            }
        } else if (SERVER) {

            for (var word : listOfCheckedWordsPlayer2) {

                vboxWordsFoundPlayer1.getChildren().add(new Label(word));

            }
        }

        Integer temp1 = 0;

        if (GAME_LOADED) {
            temp1 = getPlayerDetails().getScoreInt();
        } else {
            temp1 = getPlayerDetails().getScoreInt() + overallScorePlayer1;

        }


        staticOverallPlayer1 = temp1;
        playerDetails.setOverall(staticOverallPlayer1);

        lblsingleplayerOverall.setText(staticOverallPlayer1.toString());


        if (SINGLE_PLAYER && !SERVER) {
            apSingleplayer.setVisible(true);
            xmlMenubar.setVisible(false);
        } else {

            NEW_ROUND_MULTIPLAYER = true;

            setUpTimeline();

            apMultiplayer_2.setVisible(true);

            lblNamePlayer1.setText(getPlayerDetails().getPlayerName());
            lblroundScorePlayer1.setText(getPlayerDetails().getScore().toString());
            lbloverallPlayer1.setText(staticOverallPlayer1.toString());
        }


    }


    public void savegame_menuitem(ActionEvent actionEvent) throws RuntimeException {

        roundCounter += 1;

        String userHome = System.getProperty("user.home");
        String filePath = userHome + File.separator + "Documents" + File.separator + "playerdetails.ser";
        File file = new File(filePath);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(playerDetails);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void savegame_XML_menuitem(ActionEvent actionEvent) throws RuntimeException {

        roundCounter += 1;

        XmlManager xml = new XmlManager();
        xml.xmlWrite(playerDetails);

    }

    @FXML
    public void show_about(ActionEvent actionEvent) throws RuntimeException {

        apAbout.setVisible(true);

    }

    public void btn_about_back(ActionEvent actionEvent) throws RuntimeException {

        apAbout.setVisible(false);

    }

    public void saveXml_menuitem(ActionEvent actionEvent) throws RuntimeException {

        gameServer.writeXml();
    }

    public void readXml_menuitem(ActionEvent actionEvent) throws RuntimeException {

        XmlManager xmlReader = new XmlManager();
        xmlReader.parseXml();
    }

    public void mainmenu_return(ActionEvent actionEvent) throws IOException {

        roundCounter = 1;

        HelloApplication main_menu = new HelloApplication();
        main_menu.start(HelloApplication.getMainStage());

    }


    public void startNewRound(ActionEvent actionEvent) throws RemoteException {

        HelloController hello = new HelloController();

        Integer temp = roundCounter - 1;

        storedDetailsModel = new StoredDetailsModel(btnSingleplayerName.getText(), getPlayerDetails().getScoreInt(), temp.toString());

        if (SINGLE_PLAYER || GAME_LOADED) {
            GAME_LOADED = false;
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

    public void setGameData(GameServerImpl gameServer, GameServer gameClient, ServerConnectionManager serverConnectionManager) {
        this.gameServer = gameServer;
        this.gameClient = gameClient;
        this.serverConnectionManager = serverConnectionManager;

        multiplayerSetup();
    }

    private void multiplayerSetup() {

        Platform.runLater(() -> {
            if (CLIENT || SERVER) {

                if (SERVER) {
                    try {

                        lblnamePlayer2.setText(gameServer.getNamePlayer2());
                        lblroundScorePlayer2.setText(gameServer.getScorePlayer2().toString());

                        gameServer.addOverallScorePlayer2(gameServer.getScorePlayer2());

                        lbloverallPlayer2.setText(gameServer.getOverallScorePlayer2().toString());

                        if (gameServer.getCheckedWordsPlayer2() != null) {
                            listOfCheckedWordsPlayer2 = new ArrayList<>(gameServer.getCheckedWordsPlayer2());
                        }

                        for (var word : listOfCheckedWordsPlayer2) {

                            vboxWordsFoundPlayer1.getChildren().add(new Label(word));
                        }

                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } else if (CLIENT) {

                    btnStartNewRoundMulti.setVisible(false);

                    try {
                        clientConnectionManager = new ClientConnectionManager();

                        lblnamePlayer2.setText(gameClient.getNamePlayer1());
                        lblroundScorePlayer2.setText(gameClient.getScorePlayer1().toString());

                        gameClient.addOverallScorePlayer1(gameClient.getScorePlayer1());

                        lbloverallPlayer2.setText(gameClient.getOverallScorePlayer1().toString());

                        if (gameClient.getCheckedWordsPlayer1() != null) {
                            listOfCheckedWordsPlayer1 = new ArrayList<>(gameClient.getCheckedWordsPlayer1());
                        }

                        for (var word : listOfCheckedWordsPlayer1) {

                            vboxWordsFoundPlayer1.getChildren().add(new Label(word));
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

            for (Path path : filesList) {
                System.out.println(path.toFile().getAbsolutePath());
                StringTokenizer tokenizer = new StringTokenizer(path.toFile().getAbsolutePath(), "\\");

                Boolean startJoining = false;
                String fqn = "";

                while (tokenizer.hasMoreTokens()) {
                    String newToken = tokenizer.nextToken();

                    if ("classes".equals(newToken)) {
                        startJoining = true;
                        continue;
                    }

                    if (startJoining) {

                        if (newToken.contains(".class")) {
                            fqn += newToken.substring(0, newToken.lastIndexOf("."));
                        } else {
                            fqn += newToken + ".";
                        }
                    }

                    System.out.println("Token: " + newToken);
                }

                System.out.println("FQN: " + fqn);

                fqnList.add(fqn);
            }

            StringBuilder classInfo = new StringBuilder();

            for (String fqn : fqnList) {
                Class clazz = Class.forName(fqn);

                classInfo.append("<h2>" + clazz.getSimpleName() + "</h2><br />");

                Field[] fields = clazz.getDeclaredFields();

                for (Field field : fields) {
                    classInfo.append("<h3>" + Modifier.toString(field.getModifiers()) + " " + field.getName() + "</h3>");
                }

                Constructor[] constructors = clazz.getConstructors();

                for (Constructor constructor : constructors) {

                    String paramsString = "";

                    for (int i = 0; i < constructor.getParameters().length; i++) {
                        Parameter p = constructor.getParameters()[i];
                        paramsString += Modifier.toString(p.getModifiers()) + " " + p.getType().getSimpleName()
                                + " " + p.getName();

                        if (i < (constructor.getParameters().length - 1)) {
                            paramsString += ", ";
                        }
                    }

                    classInfo.append("<h3>" + Modifier.toString(constructor.getModifiers()) + " " + constructor.getDeclaringClass().getSimpleName()
                            + " (" + paramsString + ")</h3>");
                }

                //

                Method[] methods = clazz.getDeclaredMethods();

                for (Method method : methods) {

                    String paramsString = "";

                    for (int i = 0; i < method.getParameters().length; i++) {
                        Parameter p = method.getParameters()[i];
                        paramsString += Modifier.toString(p.getModifiers()) + " " + p.getType().getSimpleName()
                                + " " + p.getName();

                        if (i < (method.getParameters().length - 1)) {
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

            if (_time == 1) {

                Integer temp = roundCounter - 1;

                storedDetailsModel = new StoredDetailsModel(btnSingleplayerName.getText(), getPlayerDetails().getScoreInt(), temp.toString());


                NEW_ROUND_MULTIPLAYER = true;
                timeline.stop();

                if (timeline != null) {
                    timeline.stop();
                    timeline = null;
                }

                try {
                    newRoundStart();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

            } else {

                _time = _time - 1;
                lblTime.setText("Next round in: " + _time);
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
