package com.boggle.game.boggle;


import com.boggle.game.model.*;
import com.boggle.game.model.chat.Message;
import com.boggle.game.rmi.GameServer;
import com.boggle.game.rmi.GameServerImpl;
import com.boggle.game.rmi.ServerConnectionManager;
import com.boggle.game.socket.ClientStream;
import com.boggle.game.socket.IClient;
import com.boggle.game.socket.IServer;
import com.boggle.game.socket.ServerStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static com.boggle.game.boggle.EndRound.staticOverallPlayer1;
import static com.boggle.game.model.StoredDetails.player1;
import static com.boggle.game.model.StoredDetails.overallScorePlayer1;
import static com.boggle.game.socket.ServerStream.privateIP;

public class GameChooser extends UnicastRemoteObject implements Initializable {


    public static boolean GAME_LOADED = false;
    public static boolean SERVER;
    public static boolean CLIENT;
    public static boolean SINGLE_PLAYER;
    private static final int ROOM_CAPACITY = 2;
    public static String RMI_IP_ADDRESS;

    public static boolean NEW_ROUND_MULTIPLAYER = false;

    public static ArrayList<HighscoreModel> arrayListHighscore = new ArrayList<>();


    @FXML
    private TextFlow lblErrorNicknameS;
    @FXML
    public Button btnStartGameMultiplayer;
    @FXML
    private TextField tfSingleplayerNickname;
    @FXML
    private GridPane gpMode;
    @FXML
    private GridPane gpSingleplayer;
    @FXML
    private AnchorPane gpMultiplayerNewRoomServer;
    @FXML
    private AnchorPane gpMultiplayerNewRoomClient;
    @FXML
    private GridPane gpMultiplayerMode;
    @FXML
    private GridPane gpJoinRoom;
    @FXML
    private GridPane gpCreateRoom;
    @FXML
    private Button btnBackSingleplayer;
    @FXML
    private Button btnSingleplayerStart;
    @FXML
    private Button btnHighcsoreModal;
    @FXML
    private Button btnLoad;
    @FXML
    private TextField textFieldChatS;
    @FXML
    private TextField textFieldNicknameS;
    @FXML
    private TextArea textAreaChatS;
    @FXML
    private TextArea textAreaChatC;
    @FXML
    private ListView<HBox> listViewUsersS;
    private ArrayList<Label> listNicknameS;
    @FXML
    private ListView<HBox> listViewUsersC;
    private ArrayList<Label> listNicknameC;
    private int connectedUsers;
    private IServer server;
    public static PlayerDetails playerDetails;
    public static Integer roundCounter = 1;
    private SimpleDateFormat timeformatter;
    private IClient client;
    private static final Pattern PATTERN_NICKNAME = Pattern.compile("^[a-zA-Z0-9]{3,15}$");

    private static final Pattern PATTERN_IP = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    @FXML
    private Button btnCreateNewLobby;
    @FXML
    private Label lblServerIP;
    @FXML
    private TextFlow lblErrorNicknameSP;
    @FXML
    private TextField tfChatC;
    @FXML
    private TextField tfIP;
    @FXML
    private TextField tfNicknameC;
    @FXML
    private Button btnJER;
    @FXML
    private TextFlow lblErrorNicknameC;
    @FXML
    private Label lblErrorIP;

    @FXML
    private CheckBox checkbox;

    private boolean isReady;

    public Label lblPlayerName;
    private GameServerImpl gameServer;
    private GameServer gameClient;
    private ServerConnectionManager serverConnectionManager;

    private boolean LOAD_XML = false;


    public GameChooser() throws RemoteException {
        super();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (GAME_LOADED) {
            btnLoad.setVisible(false);
            checkbox.setVisible(false);
        }

        this.btnSingleplayerStart.setDisable(true);
        this.btnCreateNewLobby.setDisable(true);

        this.timeformatter = new SimpleDateFormat("[HH:mm:ss]");
        this.listNicknameC = new ArrayList<Label>();
        this.listNicknameS = new ArrayList<Label>();
        isReady = false;

        for (int i = 0; i < ROOM_CAPACITY; i++) {
            // hbox client
            HBox hbox = new HBox();
            hbox.setPrefSize(280, 25);
            hbox.setSpacing(10);
            hbox.setVisible(false);

            // nickname client
            lblPlayerName = new Label("");
            lblPlayerName.setPrefWidth(200);
            lblPlayerName.setTextFill(Paint.valueOf("black"));
            hbox.getChildren().add(lblPlayerName);
            this.listNicknameC.add(lblPlayerName);

            lblPlayerName = new Label("");
            /*l.setPrefSize(25, 25);
            l.setStyle("-fx-background-color: red");
            l.setVisible(i == 0 ? false : true);*/
            hbox.getChildren().add(lblPlayerName);
            this.listViewUsersC.getItems().add(hbox);


            // hbox server
            hbox = new HBox();
            hbox.setPrefSize(300, 25);
            hbox.setSpacing(10);
            hbox.setVisible(false);

            // nickname server
            lblPlayerName = new Label("");
            lblPlayerName.setPrefWidth(180);
            lblPlayerName.setTextFill(Paint.valueOf("black"));
            hbox.getChildren().add(lblPlayerName);
            this.listNicknameS.add(lblPlayerName);

            lblPlayerName = new Label("");
           /* l.setPrefSize(25, 25);
            l.setStyle("-fx-background-color: red");
            l.setVisible(i == 0 ? false : true);*/
            hbox.getChildren().add(lblPlayerName);
            this.listViewUsersS.getItems().add(hbox);

        }
        connectedUsers = 0;
    }

    public void singleplayerBtn() {
        gpMode.setVisible(false);
        gpSingleplayer.setVisible(true);

        SINGLE_PLAYER = true;
    }

    @FXML
    public void loadFxmlToggle(ActionEvent event) {
        LOAD_XML = !LOAD_XML;
    }


    public void loadBtn() throws IOException, ClassNotFoundException {
        gpMode.setVisible(false);
        GAME_LOADED = true;

        String userHome = System.getProperty("user.home");
        String filePath = userHome + File.separator + "Documents" + File.separator + "playerdetails.ser";
        File file = new File(filePath);

        PlayerDetails player = new PlayerDetails();

        if (LOAD_XML) {
            XmlManager xml = new XmlManager();
            player = xml.xmlRead();
        } else {

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                player = (PlayerDetails) ois.readObject();
            }
        }


        setPlayerDetails(player);

        roundCounter = player.getRoundInt();
        staticOverallPlayer1 = player.getOverallInt();
        SINGLE_PLAYER = true;

        new com.boggle.game.model.EndRound(null, null, null);
    }

    public void multiplayerBtn() {
        gpMode.setVisible(false);
        gpMultiplayerMode.setVisible(true);
    }

    public void joinRoomBtn() {
        gpMode.setVisible(false);
        gpMultiplayerMode.setVisible(false);
        gpMultiplayerNewRoomServer.setVisible(false);
        gpJoinRoom.setVisible(true);

        CLIENT = true;

    }

    public void createNewRoomBtn() {
        gpMode.setVisible(false);
        gpMultiplayerMode.setVisible(false);
        gpCreateRoom.setVisible(true);
    }

    @FXML
    public void createLobbyBtn(ActionEvent event) {

        gpCreateRoom.setVisible(false);
        gpMultiplayerNewRoomServer.setVisible(true);

        SERVER = true;
        NEW_ROUND_MULTIPLAYER = false;


        if (!this.checkNickname(this.textFieldNicknameS.getText())) {
            this.showAlert(AlertType.ERROR, "Invalid nickname", "The nickname bust be from 3 to 15 alphanumeric char long.");
            this.btnCreateNewLobby.setDisable(true);
            this.lblErrorNicknameS.setVisible(true);
            this.textFieldNicknameS.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            return;
        }

        this.setServerAddress();
        lblServerIP.setVisible(true);
        this.textAreaChatS.setText(this.getCurrentTimestamp() + " " + this.textFieldNicknameS.getText() + " created the room");

        // create new room -> start server (if OK switch to Server Room View)
        this.server = new ServerStream(this, this.textFieldNicknameS.getText());
        this.client = null;

        // reset the user list
        this.resetList();

        // reset buttons
        this.btnStartGameMultiplayer.setDisable(true);

        // set the first list element (the server) to visibile
        this.listNicknameS.get(0).setText(this.textFieldNicknameS.getText());
        this.listViewUsersS.getItems().get(0).setVisible(true);

        this.connectedUsers = 1;


    }

    public void switchToClientRoom() {
        this.gpSingleplayer.setVisible(false);
        this.gpMultiplayerNewRoomServer.setVisible(false);
        this.gpMultiplayerMode.setVisible(false);
        this.gpJoinRoom.setVisible(false);
        this.gpCreateRoom.setVisible(false);
        this.gpMultiplayerNewRoomClient.setVisible(true);


    }


    public void backBtn() {

        this.btnSingleplayerStart.setDisable(true);
        this.btnCreateNewLobby.setDisable(true);
        this.textFieldNicknameS.setText("");
        this.tfSingleplayerNickname.setText("");
        this.textFieldNicknameS.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        this.tfSingleplayerNickname.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        this.lblErrorNicknameS.setVisible(false);
        this.lblErrorNicknameSP.setVisible(false);

        this.btnJER.setDisable(true);
        this.gpMode.setVisible(true);
        this.gpSingleplayer.setVisible(false);
        this.gpMultiplayerNewRoomServer.setVisible(false);
        this.gpMultiplayerMode.setVisible(false);
        this.gpJoinRoom.setVisible(false);
        this.gpCreateRoom.setVisible(false);
        this.gpMultiplayerNewRoomClient.setVisible(false);
        this.lblServerIP.setVisible(false);

        this.lblErrorNicknameC.setVisible(false);
        this.lblErrorIP.setVisible(false);
        this.tfNicknameC.setText("");
        this.tfNicknameC.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        this.tfIP.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        this.tfIP.setText("");

        this.closeConnection();

        SERVER = false;
        CLIENT = false;
        SINGLE_PLAYER = false;
        GAME_LOADED = false;

    }

    public void highScoreModal() {
        HighScoreView m = new HighScoreView();
        m.highScoreCont();

    }

    private boolean checkNickname(String text) {
        return PATTERN_NICKNAME.matcher(text).matches();
    }


    public void startGame() throws RemoteException {

        roundCounter += 1;

        String playerName;

        if (roundCounter == 2) {

            //// Load those only once in program

            if (!NEW_ROUND_MULTIPLAYER) {
                overallScorePlayer1 = 0;
            }

            //highscore_list();
            playerName = tfSingleplayerNickname.getText();

            if (SERVER) {
                if (textFieldNicknameS != null) {
                    playerName = textFieldNicknameS.getText();
                }
            }
            if (CLIENT) {
                if (tfNicknameC != null) {
                    playerName = tfNicknameC.getText();
                }
            }
        } else {
            playerName = player1;
        }

        playerDetails = new PlayerDetails(playerName);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameScreen.fxml"));
        if (NEW_ROUND_MULTIPLAYER) {
            fxmlLoader.setControllerFactory(param -> {
                GameScreen gsc = new GameScreen();
                gsc.setGameData(gameServer, gameClient, serverConnectionManager);
                return gsc;
            });
        }
        Scene scene = null;
        try {
            // load() called only once
            Parent root = fxmlLoader.load();
            scene = new Scene(root, 600, 400);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = HelloApplication.getMainStage();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static PlayerDetails getPlayerDetails() {

        return playerDetails;
    }

    public static PlayerDetails setPlayerDetails(PlayerDetails d) {

        return playerDetails = d;
    }


    @FXML
    public void sendMessageS(ActionEvent event) {
        String msg = this.textFieldChatS.getText();
        if (!msg.isEmpty() && !msg.isBlank())
            this.server.sendChatMessage(msg);
        this.textFieldChatS.setText("");
    }

    @FXML
    public void startMultiplayerGame() throws RemoteException {
        this.server.startGame();
    }

    @FXML
    public void sendMessageC(ActionEvent event) {
        String msg = this.tfChatC.getText();
        if (!msg.isEmpty() && !msg.isBlank())
            this.client.sendChatMessage(msg);
        this.tfChatC.setText("");
    }

    @FXML
    public void sendReady(ActionEvent event) {
        isReady = !isReady;
        this.client.sendReady(isReady);
        this.tfChatC.setText("");
    }


    public void addToTextArea(String text) {
        // client
        if (CLIENT) {
            if (this.textAreaChatC.getText().isEmpty())
                this.textAreaChatC.setText(text);
            else this.textAreaChatC.appendText("\n" + text);
        }
        // server
        else if (SERVER) {
            this.textAreaChatS.appendText("\n" + text);
        }
    }

    public void addToTextArea(Message message) {
        this.addToTextArea(message.getTimestamp() + " " + message.getNickname() + ": " + message.getContent());
    }

    public String getCurrentTimestamp() {
        Date date = new Date(System.currentTimeMillis());
        String timestamp = this.timeformatter.format(date);

        return timestamp;
    }

    public void showAlert(AlertType aType, String header, String content) {
        Platform.runLater(() -> {
            Alert a = new Alert(aType);
            a.setTitle("Information Dialog");
            a.setHeaderText(header);
            a.setContentText(content);
            a.show();
        });
    }

    public void addUser(Player u) {
        Platform.runLater(() -> {
            if (CLIENT) {
                this.listNicknameC.get(this.connectedUsers).setText(u.getNickname());
                this.listViewUsersC.getItems().get(this.connectedUsers).setVisible(true);
                this.connectedUsers++;
            } else if (SERVER) {
                this.listNicknameS.get(this.connectedUsers).setText(u.getNickname());
                this.listViewUsersS.getItems().get(this.connectedUsers).setVisible(true);
                this.connectedUsers++;
            }
        });

    }

    public void resetList() {
        if (CLIENT) {
            Platform.runLater(() -> {
                for (int i = 0; i < ROOM_CAPACITY; i++) {
                    this.listViewUsersC.getItems().get(i).setVisible(false);
                    this.listNicknameC.get(i).setText("");

                }
            });
        } else if (SERVER) {
            for (int i = 0; i < ROOM_CAPACITY; i++) {
                this.listViewUsersS.getItems().get(i).setVisible(false);
                this.listNicknameS.get(i).setText("");
            }
        }
    }

    public void updateUserList(List<Player> players) {
        Platform.runLater(() -> {
            if (CLIENT) {
                for (int i = 0; i < players.size(); i++) {
                    Player u = players.get(i);
                    this.listNicknameC.get(i).setText(u.getNickname());
                    this.listViewUsersC.getItems().get(i).setVisible(true);
                }
                this.connectedUsers = players.size();
            } else if (SERVER) {
                // for the moment it's never used from the server
            }
        });
    }

    public void removeUser(String nickname) {
        Platform.runLater(() -> {
            boolean found = false;
            if (CLIENT) {
                // NB: we have to move by one position back every user, to fill the empty space left by the removed one
                for (int i = 1; i < this.connectedUsers; i++) {
                    if (found) {
                        // we move every entry up by 1, overriding the one to remove
                        this.listNicknameC.get(i - 1).setText(this.listNicknameC.get(i).getText());

                    }
                    if (this.listNicknameC.get(i).getText().equals(nickname))
                        found = true;
                }
                // we hide the last entry
                this.listViewUsersC.getItems().get(this.connectedUsers - 1).setVisible(false);
                this.listNicknameC.get(this.connectedUsers - 1).setText("");

                this.connectedUsers--;
            } else if (SERVER) {
                // NB: we have to move by one position back every user, to fill the empty space left by the removed one
                for (int i = 1; i < this.connectedUsers; i++) {
                    if (found) {
                        // we move every entry up by 1, overriding the one to remove
                        this.listNicknameS.get(i - 1).setText(this.listNicknameS.get(i).getText());

                    }
                    if (this.listNicknameS.get(i).getText().equals(nickname))
                        found = true;
                }
                // we hide the last entry
                this.listViewUsersS.getItems().get(this.connectedUsers - 1).setVisible(false);
                this.listNicknameS.get(this.connectedUsers - 1).setText("");

                this.connectedUsers--;

                this.btnStartGameMultiplayer.setDisable(!this.server.checkCanStartGame());
            }
        });
    }


    private void setServerAddress() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            privateIP = socket.getLocalAddress().getHostAddress();
            RMI_IP_ADDRESS = privateIP;
            this.lblServerIP.setText("Private Server IP address: " + privateIP);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (SERVER) {
            this.server.sendClose();
            this.server = null;
        } else if (CLIENT && client != null) {
            this.client.sendClose();
            this.client = null;
        }
    }


    @FXML
    public void validateNicknameAddressC() {

        // Check nickname and IP validity
        boolean isNicknameValid = this.checkNickname(this.tfNicknameC.getText());
        boolean isIpValid = this.checkIP(this.tfIP.getText()) || this.tfIP.getText().isEmpty();

        // Set default values
        this.btnJER.setDisable(true);
        this.lblErrorNicknameC.setVisible(!isNicknameValid);
        this.lblErrorIP.setVisible(!isIpValid);
        this.tfNicknameC.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        this.tfIP.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");

        // Check conditions to modify default values
        if (isNicknameValid && isIpValid) {
            this.btnJER.setDisable(false);
        }
        if (!isNicknameValid) {
            this.tfNicknameC.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
        }
        if (!isIpValid && !this.tfIP.getText().isEmpty()) {
            this.tfIP.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
        }
    }


    private boolean checkIP(String text) {
        // if OK return true
        return PATTERN_IP.matcher(text).matches();
    }

    @FXML
    public void joinExistingRoom(ActionEvent event) {


        if (!this.checkNickname(this.tfNicknameC.getText())) {
            this.showAlert(AlertType.ERROR, "Invalid nickname", "The nickname bust be from 3 to 15 alphanumeric char long.");
            this.btnJER.setDisable(true);
            this.lblErrorNicknameC.setVisible(true);
            this.tfNicknameC.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            return;
        }
        if (!this.checkIP(this.tfIP.getText()) && !this.tfIP.getText().isEmpty()) {
            this.showAlert(AlertType.ERROR, "Invalid IP Address", "The address must be X.X.X.X or empty (localhost).");
            this.btnJER.setDisable(true);
            this.lblErrorIP.setVisible(true);
            this.tfIP.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            return;
        }

        CLIENT = true;

        // reset textArea
        this.textAreaChatC.setText("");

        // connect to existing room -> start client (if OK switch to Client Room View)
        this.client = new ClientStream(this, this.tfIP.getText(), 9001, this.tfNicknameC.getText());
        this.server = null;


    }

    @FXML
    public void validateNicknameS() {
        // OK
        if (this.checkNickname(this.textFieldNicknameS.getText()) || this.checkNickname(this.tfSingleplayerNickname.getText())) {
            this.btnSingleplayerStart.setDisable(false);
            this.btnCreateNewLobby.setDisable(false);
            this.lblErrorNicknameS.setVisible(false);
            this.lblErrorNicknameSP.setVisible(false);
            this.textFieldNicknameS.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
            this.tfSingleplayerNickname.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        }
        // NOK
        else {
            this.btnSingleplayerStart.setDisable(true);
            this.btnCreateNewLobby.setDisable(true);
            this.lblErrorNicknameS.setVisible(true);
            this.lblErrorNicknameSP.setVisible(true);
            this.textFieldNicknameS.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            this.tfSingleplayerNickname.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
        }
    }

    public synchronized void newRoundMultiplayer(GameServerImpl gameServer, GameServer gameClient, ServerConnectionManager serverConnectionManager, boolean client, boolean server) {
        this.gameServer = gameServer;
        this.gameClient = gameClient;
        this.serverConnectionManager = serverConnectionManager;
        CLIENT = client;
        SERVER = server;

    }
}