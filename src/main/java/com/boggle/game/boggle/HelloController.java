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
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static com.boggle.game.boggle.EndRoundController.static_overall_player_1;
import static com.boggle.game.boggle.HighscoreController.arrayList_Highscore;
import static com.boggle.game.model.StoredDetailsModel.P1;
import static com.boggle.game.model.StoredDetailsModel.overall_P1;
import static com.boggle.game.boggle.HelloApplication.NEW_ROUND_MULTIPLAYER;
import static com.boggle.game.socket.ServerStream.privateIP;

public class HelloController extends UnicastRemoteObject implements Initializable {


    public static boolean GAME_LOADED = false;
    public static boolean SERVER;
    public static boolean CLIENT;
    public static boolean SINGLE_PLAYER;
    private static final int ROOM_CAPACITY = 2;
    public static String RMI_IP_ADDRESS;


    @FXML
    private TextFlow labelErrorNicknameS;
    @FXML
    public Button start_game_multiplayer;
    @FXML
    private TextField singleplayerNickname;
    @FXML
    private GridPane gp_mode;
    @FXML
    private GridPane gp_singleplayer;
    @FXML
    private AnchorPane gp_multiplayer_new_room_server;
    @FXML
    private AnchorPane gp_multiplayer_new_room_client;
    @FXML
    private GridPane gp_multiplayer_mode;
    @FXML
    private GridPane gp_join_room;
    @FXML
    private GridPane gp_create_room;
    @FXML
    private Button btn_back_singleplayer;
    @FXML
    private Button  singleplayer_start;
    @FXML
    private Button btn_highcsore_modal;
    @FXML
    private Button btn_load;
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
    public static PlayerDetailsModel playerDetails;
    public static Integer roundCounter = 1;
    private SimpleDateFormat tformatter;
    private IClient client;
    private static final Pattern PATTERN_NICKNAME = Pattern.compile("^[a-zA-Z0-9]{3,15}$");

    private static final Pattern PATTERN_IP = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    @FXML
    private Button btn_create_newLobby;
    @FXML
    private Label labelServerIP;
    @FXML
    private TextFlow labelErrorNicknameSP;
    @FXML
    private TextField textFieldChatC;
    @FXML
    private TextField textFieldIP;
    @FXML private TextField textFieldNicknameC;
    @FXML private Button buttonJER;
    @FXML private TextFlow labelErrorNicknameC;
    @FXML private Label labelErrorIP;

    @FXML private CheckBox checkbox;

    private  boolean isReady;

    public Label l;
    private GameServerImpl gameServer;
    private GameServer gameClient;
    private ServerConnectionManager serverConnectionManager;

    private boolean LOAD_XML = false;


    public HelloController() throws RemoteException {
        super();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (GAME_LOADED == true) {
            btn_load.setVisible(false);
            checkbox.setVisible(false);
        }

        this.singleplayer_start.setDisable(true);
        this.btn_create_newLobby.setDisable(true);

        this.tformatter = new SimpleDateFormat("[HH:mm:ss]");
        this.listNicknameC = new ArrayList<Label>();
        this.listNicknameS = new ArrayList<Label>();
        isReady = false;

        for(int i = 0; i < ROOM_CAPACITY; i++)
        {
            // hbox client
            HBox hbox = new HBox();
            hbox.setPrefSize(280, 25);
            hbox.setSpacing(10);
            hbox.setVisible(false);

            // nickname client
            l = new Label("");
            l.setPrefWidth(200);
            l.setTextFill(Paint.valueOf("black"));
            hbox.getChildren().add(l);
            this.listNicknameC.add(l);

            l = new Label("");
            /*l.setPrefSize(25, 25);
            l.setStyle("-fx-background-color: red");
            l.setVisible(i == 0 ? false : true);*/
            hbox.getChildren().add(l);
            this.listViewUsersC.getItems().add(hbox);


            // hbox server
            hbox = new HBox();
            hbox.setPrefSize(300, 25);
            hbox.setSpacing(10);
            hbox.setVisible(false);

            // nickname server
            l = new Label("");
            l.setPrefWidth(180);
            l.setTextFill(Paint.valueOf("black"));
            hbox.getChildren().add(l);
            this.listNicknameS.add(l);

            l = new Label("");
           /* l.setPrefSize(25, 25);
            l.setStyle("-fx-background-color: red");
            l.setVisible(i == 0 ? false : true);*/
            hbox.getChildren().add(l);
            this.listViewUsersS.getItems().add(hbox);

        }
        connectedUsers = 0;
    }

    public void singleplayer_btn(){
        gp_mode.setVisible(false);
        gp_singleplayer.setVisible(true);

        SINGLE_PLAYER = true;
    }

    @FXML public void load_fxml_toggle(ActionEvent event){
        LOAD_XML = !LOAD_XML;
    }



    public void load_btn() throws IOException, ClassNotFoundException {
        gp_mode.setVisible(false);
        GAME_LOADED = true;

        PlayerDetailsModel player = new PlayerDetailsModel();

        if (LOAD_XML){
            XmlManager xml = new XmlManager();
            player = xml.xmlRead();
        }
        else{

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("playerdetails.ser"))) {
                player = (PlayerDetailsModel)ois.readObject();
            }
        }


        setPlayerDetails(player);

        roundCounter = player.get_round_int();
        static_overall_player_1 = player.get_overall_int();
        SINGLE_PLAYER = true;

        new EndRoundModel(null, null, null);
    }

    public void multiplayer_btn(){
        gp_mode.setVisible(false);
        gp_multiplayer_mode.setVisible(true);
    }
    public void join_room_btn(){
        gp_mode.setVisible(false);
        gp_multiplayer_mode.setVisible(false);
        gp_multiplayer_new_room_server.setVisible(false);
        gp_join_room.setVisible(true);

        CLIENT = true;

    }
    public void create_new_room_btn(){
        gp_mode.setVisible(false);
        gp_multiplayer_mode.setVisible(false);
        gp_create_room.setVisible(true);
    }

    @FXML public void create_lobby_btn(ActionEvent event){

        gp_create_room.setVisible(false);
        gp_multiplayer_new_room_server.setVisible(true);

        SERVER = true;
        NEW_ROUND_MULTIPLAYER=false;


        if(!this.checkNickname(this.textFieldNicknameS.getText()))
        {
            this.showAlert(AlertType.ERROR, "Invalid nickname", "The nickname bust be from 3 to 15 alphanumeric char long.");
            this.btn_create_newLobby.setDisable(true);
            this.labelErrorNicknameS.setVisible(true);
            this.textFieldNicknameS.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            return;
        }

        this.setServerAddress();
        labelServerIP.setVisible(true);
        this.textAreaChatS.setText(this.getCurrentTimestamp() + " " + this.textFieldNicknameS.getText() + " created the room");

        // create new room -> start server (if OK switch to Server Room View)
        this.server = new ServerStream(this, this.textFieldNicknameS.getText());
        this.client = null;

        // reset the user list
        this.resetList();

        // reset buttons
        this.start_game_multiplayer.setDisable(true);

        // set the first list element (the server) to visibile
        this.listNicknameS.get(0).setText(this.textFieldNicknameS.getText());
        this.listViewUsersS.getItems().get(0).setVisible(true);

        this.connectedUsers = 1;



    }

    public void switchToClientRoom()
    {
     this.gp_singleplayer.setVisible(false);
     this.gp_multiplayer_new_room_server.setVisible(false);
     this.gp_multiplayer_mode.setVisible(false);
     this.gp_join_room.setVisible(false);
     this.gp_create_room.setVisible(false);
     this.gp_multiplayer_new_room_client.setVisible(true);


    }


    public void back_btn(){

        this.singleplayer_start.setDisable(true);
        this.btn_create_newLobby.setDisable(true);
        this.textFieldNicknameS.setText("");
        this.singleplayerNickname.setText("");
        this.textFieldNicknameS.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        this.singleplayerNickname.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        this.labelErrorNicknameS.setVisible(false);
        this.labelErrorNicknameSP.setVisible(false);

        this.buttonJER.setDisable(true);
        this.gp_mode.setVisible(true);
        this.gp_singleplayer.setVisible(false);
        this.gp_multiplayer_new_room_server.setVisible(false);
        this.gp_multiplayer_mode.setVisible(false);
        this.gp_join_room.setVisible(false);
        this.gp_create_room.setVisible(false);
        this.gp_multiplayer_new_room_client.setVisible(false);
        this.labelServerIP.setVisible(false);

        this.labelErrorNicknameC.setVisible(false);
        this.labelErrorIP.setVisible(false);
        this.textFieldNicknameC.setText("");
        this.textFieldNicknameC.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        this.textFieldIP.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        this.textFieldIP.setText("");

        this.closeConnection();

        SERVER = false;
        CLIENT = false;
        SINGLE_PLAYER = false;
        GAME_LOADED = false;

    }

    public void highscoreModal(){
        HighscoreController modal =   new HighscoreController();
        highscore_list();
        modal.highScoreCont();
    }

    private boolean checkNickname(String text)
    {
        // if OK return true
        return PATTERN_NICKNAME.matcher(text).matches() ? true : false;
    }


    public void startGame() throws RemoteException   {

        roundCounter += 1;

        String playerName;

        if (roundCounter == 2){

            //// Load those only once in program

            if (!NEW_ROUND_MULTIPLAYER){
                overall_P1 = 0;
            }


            highscore_list();

             //insert hardcoded users to populate list
           /*
              arrayList_Highscore.add(new HighscoreModel(25,"Marac"));
              arrayList_Highscore.add(new HighscoreModel(20,"Ivana"));
              arrayList_Highscore.add(new HighscoreModel(15,"Marko"));
              arrayList_Highscore.add(new HighscoreModel(10,"Lana"));
              arrayList_Highscore.add(new HighscoreModel(7,"Robi"));
              arrayList_Highscore.add(new HighscoreModel(3,"Lovorka"));
              arrayList_Highscore.add(new HighscoreModel(15,"Miro"));
              arrayList_Highscore.add(new HighscoreModel(32,"Koko"));
              arrayList_Highscore.add(new HighscoreModel(1,"Damira"));
              arrayList_Highscore.add(new HighscoreModel(0,"Ognjen"));
            */
            playerName = singleplayerNickname.getText();

           if (SERVER){
               if (textFieldNicknameS != null ){
                   playerName = textFieldNicknameS.getText();
               }
           }
           if(CLIENT){
               if (textFieldNicknameC != null ){
                   playerName = textFieldNicknameC.getText();
               }
           }

        }
        else {

            playerName = P1;

        }


        playerDetails = new PlayerDetailsModel(playerName);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameScreen.fxml"));
        if (NEW_ROUND_MULTIPLAYER){
            fxmlLoader.setControllerFactory(param -> {
                GameScreenController gsc = new GameScreenController();
                gsc.setGameData(gameServer,gameClient,serverConnectionManager);
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

    private void highscore_list() {
        ArrayList<HighscoreModel> model;

      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("highscore.ser"))) {
          model = (ArrayList<HighscoreModel>)ois.readObject();
      } catch (IOException e) {
          throw new RuntimeException(e);
      } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
      }

/*      for (var item : model
      ) {
          System.out.println(item.toString());
      }*/

      arrayList_Highscore = ((ArrayList<HighscoreModel>) model.clone());

    }

    public static PlayerDetailsModel getPlayerDetails() {

        return playerDetails;
    }
    public static PlayerDetailsModel setPlayerDetails(PlayerDetailsModel d) {

        return  playerDetails = d;
    }


    @FXML public void sendMessageS(ActionEvent event)
    {
        String msg = this.textFieldChatS.getText();
        if(!msg.isEmpty() && !msg.isBlank())
            this.server.sendChatMessage(msg);
        this.textFieldChatS.setText("");
    }

    @FXML public void startMultiplayerGame() throws RemoteException {
        this.server.startGame();
    }
    @FXML public void sendMessageC(ActionEvent event)
    {
        String msg = this.textFieldChatC.getText();
        if(!msg.isEmpty() && !msg.isBlank())
            this.client.sendChatMessage(msg);
        this.textFieldChatC.setText("");
    }

    @FXML public void sendReady(ActionEvent event)
    {
        isReady = !isReady;
        this.client.sendReady(isReady);
        this.textFieldChatC.setText("");
    }


    public void addToTextArea(String text)
    {
        // client
        if(CLIENT)
        {
            if(this.textAreaChatC.getText().isEmpty())
                this.textAreaChatC.setText(text);
            else this.textAreaChatC.appendText("\n" + text);
        }
        // server
        else if(SERVER)
        {
            this.textAreaChatS.appendText("\n" + text);
        }
    }
    public void addToTextArea(Message message)
    {
        this.addToTextArea(message.getTimestamp() + " " + message.getNickname() + ": " + message.getContent());
    }

    public String getCurrentTimestamp()
    {
        Date date = new Date(System.currentTimeMillis());
        String timestamp = this.tformatter.format(date);

        return timestamp;
    }
    public void showAlert(AlertType aType, String header, String content)
    {
        Platform.runLater(() -> {
            Alert a = new Alert(aType);
            a.setTitle("Information Dialog");
            a.setHeaderText(header);
            a.setContentText(content);
            a.show();
        });
    }
    public void addUser(PlayerModel u)
    {
        Platform.runLater(() -> {
            if(CLIENT)
            {
                this.listNicknameC.get(this.connectedUsers).setText(u.getNickname());
                this.listViewUsersC.getItems().get(this.connectedUsers).setVisible(true);
                this.connectedUsers++;
            }
            else if(SERVER)
            {
                this.listNicknameS.get(this.connectedUsers).setText(u.getNickname());
                this.listViewUsersS.getItems().get(this.connectedUsers).setVisible(true);
                this.connectedUsers++;
            }
        });

    }
    public void resetList()
    {
        if(CLIENT)
        {
            Platform.runLater(() -> {
                for(int i = 0; i < ROOM_CAPACITY; i++)
                {
                    this.listViewUsersC.getItems().get(i).setVisible(false);
                    this.listNicknameC.get(i).setText("");

            }});
        }
        else if(SERVER)
        {
            for(int i = 0; i < ROOM_CAPACITY; i++)
            {
                this.listViewUsersS.getItems().get(i).setVisible(false);
                this.listNicknameS.get(i).setText("");
            }
        }
    }
    public void updateUserList(List<PlayerModel> players)
    {
        Platform.runLater(() -> {
            if(CLIENT)
            {
                for(int i = 0; i < players.size(); i++)
                {
                    PlayerModel u = players.get(i);
                    this.listNicknameC.get(i).setText(u.getNickname());
                    this.listViewUsersC.getItems().get(i).setVisible(true);
                }
                this.connectedUsers = players.size();
            }
            else if(SERVER)
            {
                // for the moment it's never used from the server
            }
        });
    }
    public void removeUser(String nickname)
    {
        Platform.runLater(() -> {
            boolean found = false;
            if(CLIENT)
            {
                // NB: we have to move by one position back every user, to fill the empty space left by the removed one
                for(int i = 1; i < this.connectedUsers; i++)
                {
                    if(found)
                    {
                        // we move every entry up by 1, overriding the one to remove
                        this.listNicknameC.get(i - 1).setText(this.listNicknameC.get(i).getText());

                    }
                    if(this.listNicknameC.get(i).getText().equals(nickname))
                        found = true;
                }
                // we hide the last entry
                this.listViewUsersC.getItems().get(this.connectedUsers - 1).setVisible(false);
                this.listNicknameC.get(this.connectedUsers - 1).setText("");

                this.connectedUsers--;
            }
            else if(SERVER)
            {
                // NB: we have to move by one position back every user, to fill the empty space left by the removed one
                for(int i = 1; i < this.connectedUsers; i++)
                {
                    if(found)
                    {
                        // we move every entry up by 1, overriding the one to remove
                        this.listNicknameS.get(i - 1).setText(this.listNicknameS.get(i).getText());

                    }
                    if(this.listNicknameS.get(i).getText().equals(nickname))
                        found = true;
                }
                // we hide the last entry
                this.listViewUsersS.getItems().get(this.connectedUsers - 1).setVisible(false);
                this.listNicknameS.get(this.connectedUsers - 1).setText("");

                this.connectedUsers--;

                this.start_game_multiplayer.setDisable(!this.server.checkCanStartGame());
            }
        });
    }


    private void setServerAddress()
    {
        try(final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
              privateIP = socket.getLocalAddress().getHostAddress();
                RMI_IP_ADDRESS = privateIP;
            this.labelServerIP.setText("Private Server IP address: " + privateIP);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public void closeConnection()
    {
        if (SERVER){
            this.server.sendClose();
            this.server = null;}
        else if (CLIENT && client != null){
            this.client.sendClose();
            this.client = null;}
    }


    @FXML public void validateNicknameAddressC()
    {

        // nickname OK & address OK (or empty)
        if (this.checkNickname(this.textFieldNicknameC.getText()) && (this.checkIP(this.textFieldIP.getText()) || this.textFieldIP.getText().isEmpty()))
        {
            this.buttonJER.setDisable(false);
            this.labelErrorNicknameC.setVisible(false);
            this.labelErrorIP.setVisible(false);
            // reset borders & focus (nickname)
            this.textFieldNicknameC.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
            // reset borders & focus (address)
            this.textFieldIP.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        }
        // nickname OK & address NOT (nor empty)
       else if(this.checkNickname(this.textFieldNicknameC.getText()) && !(checkIP(this.textFieldIP.getText()) || this.textFieldIP.getText().isEmpty()))
        {
            this.buttonJER.setDisable(true);
            this.labelErrorNicknameC.setVisible(false);
            this.labelErrorIP.setVisible(true);
            // reset borders & focus (nickname)
            this.textFieldNicknameC.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
            // reset borders & focus (address)
            this.textFieldIP.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
        }
        else if(this.checkNickname(this.textFieldNicknameC.getText()) && !(checkIP(this.textFieldIP.getText()) || !this.textFieldIP.getText().isEmpty()))
        {
            this.buttonJER.setDisable(true);
            this.labelErrorNicknameC.setVisible(false);
            this.labelErrorIP.setVisible(true);
            // reset borders & focus (nickname)
            this.textFieldNicknameC.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
            // reset borders & focus (address)
            this.textFieldIP.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
        }
        // nickname NOT & address OK (or empty)
        else if(!this.checkNickname(this.textFieldNicknameC.getText()) && (checkIP(this.textFieldIP.getText()) || this.textFieldIP.getText().isEmpty()))
        {
            this.buttonJER.setDisable(true);
            this.labelErrorNicknameC.setVisible(true);
            this.labelErrorIP.setVisible(false);
            // red borders & focus (nickname)
            this.textFieldNicknameC.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            // reset borders & focus (address)
            this.textFieldIP.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        }
        // nickname NOT & address NOT (nor empty)
        else {
            this.buttonJER.setDisable(true);
            this.labelErrorNicknameC.setVisible(true);
            this.labelErrorIP.setVisible(true);
            // red borders & focus (nickname)
            this.textFieldNicknameC.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            // red borders & focus (address)
            this.textFieldIP.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");

        }


    }
    private boolean checkIP(String text)
    {
        // if OK return true
        return PATTERN_IP.matcher(text).matches() ? true : false;
    }
    @FXML public void joinExistingRoom(ActionEvent event)
    {


        if(!this.checkNickname(this.textFieldNicknameC.getText()))
        {
            this.showAlert(AlertType.ERROR, "Invalid nickname", "The nickname bust be from 3 to 15 alphanumeric char long.");
            this.buttonJER.setDisable(true);
            this.labelErrorNicknameC.setVisible(true);
            this.textFieldNicknameC.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            return;
        }
        if(!this.checkIP(this.textFieldIP.getText()) && !this.textFieldIP.getText().isEmpty())
        {
            this.showAlert(AlertType.ERROR, "Invalid IP Address", "The address must be X.X.X.X or empty (localhost).");
            this.buttonJER.setDisable(true);
            this.labelErrorIP.setVisible(true);
            this.textFieldIP.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            return;
        }

        CLIENT = true;

        // reset textArea
        this.textAreaChatC.setText("");

        // connect to existing room -> start client (if OK switch to Client Room View)
        this.client = new ClientStream(this, this.textFieldIP.getText(), 9001, this.textFieldNicknameC.getText());
        this.server = null;



    }

    @FXML public void validateNicknameS()
    {
        // OK
        if(this.checkNickname(this.textFieldNicknameS.getText()) || this.checkNickname(this.singleplayerNickname.getText()) )
        {   this.singleplayer_start.setDisable(false);
            this.btn_create_newLobby.setDisable(false);
            this.labelErrorNicknameS.setVisible(false);
            this.labelErrorNicknameSP.setVisible(false);
            this.textFieldNicknameS.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
            this.singleplayerNickname.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        }
        // NOK
        else
        {   this.singleplayer_start.setDisable(true);
            this.btn_create_newLobby.setDisable(true);
            this.labelErrorNicknameS.setVisible(true);
            this.labelErrorNicknameSP.setVisible(true);
            this.textFieldNicknameS.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            this.singleplayerNickname.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
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