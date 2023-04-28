package com.boggle.game.boggle;


import com.boggle.game.model.*;
import com.boggle.game.model.chat.Message;
import com.boggle.game.network.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static com.boggle.game.boggle.EndRoundController.static_overall;
import static com.boggle.game.boggle.HighscoreController.arrayList_Highscore;
import static com.boggle.game.model.StoredDetailsModel.P1;
import static com.boggle.game.model.StoredDetailsModel.overall_P1;


public class HelloController implements Initializable {

    public IRMI gameScreen;
    private IServer server;
    public static boolean SERVER;
    public static boolean CLIENT;

    private static final int ROOM_CAPACITY = 2;

    @FXML
    private TextFlow labelErrorNicknameS;
    @FXML
    private Button start_game_multiplayer;
    @FXML
    private TextField playerOneNameTextField;

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


    public static PlayerDetailsModel playerDetails;

    public static boolean SINGLEPLAYER =false;

    public static Integer roundCounter = 1;

    public static Boolean game_loaded=false;

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

    public static String textFieldNicknameC_static;
    public static String textFieldNicknameS_static;

    public  RMIServer rmi;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        rmi = new RMIServer();

        if (game_loaded == true) {
            btn_load.setVisible(false);
        }

        this.singleplayer_start.setDisable(true);
        this.btn_create_newLobby.setDisable(true);

        this.tformatter = new SimpleDateFormat("[HH:mm:ss]");
        this.listNicknameC = new ArrayList<Label>();
        this.listNicknameS = new ArrayList<Label>();

        for(int i = 0; i < ROOM_CAPACITY; i++)
        {
            // hbox client
            HBox hbox = new HBox();
            hbox.setPrefSize(100, 25);
            hbox.setSpacing(10);
            hbox.setVisible(false);
            // nickname client
            Label l = new Label("");
            l.setPrefWidth(200);
            l.setTextFill(Paint.valueOf("black"));
            hbox.getChildren().add(l);
            this.listNicknameC.add(l);

            this.listViewUsersC.getItems().add(hbox);
            // hbox server
            hbox = new HBox();
            hbox.setPrefSize(100, 25);
            hbox.setSpacing(10);
            hbox.setVisible(false);
            // nickname server
            l = new Label("");
            l.setPrefWidth(180);
            l.setTextFill(Paint.valueOf("black"));
            hbox.getChildren().add(l);
            this.listNicknameS.add(l);

            this.listViewUsersS.getItems().add(hbox);
        }

        connectedUsers = 0;
    }

    public void singleplayer_btn(){
        gp_mode.setVisible(false);
        gp_singleplayer.setVisible(true);

        SINGLEPLAYER = true;

    }

    public void load_btn() throws IOException, ClassNotFoundException {

        gp_mode.setVisible(false);

        PlayerDetailsModel model = new PlayerDetailsModel();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("playerdetails.ser"))) {
            model = (PlayerDetailsModel)ois.readObject();
        }

        game_loaded = true;
        SINGLEPLAYER = true;

        setPlayerDetails(model);

        roundCounter = model.get_round_int();
        static_overall = model.get_overall_int() + model.get_score_int();

        new EndRoundModel();
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

        textFieldNicknameC_static = textFieldNicknameC.getText();

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

       textFieldNicknameS_static = textFieldNicknameS.getText();


        rmi.serverStart();




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
        //this.start_game_multiplayer.setDisable(true);

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


        String text = "RMI Test Message";
        IRMI rmi = null;

        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            rmi = (IRMI) registry.lookup("server");
            System.out.println("[RMI]Connected to Server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rmi != null) {
            try {
                rmi.sendMessage(text);
               // rmi.getStartGame();
                System.out.println(rmi.getMessage(text));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.out.println("[RMI]Finished");
        }

    }


    public void back_btn(){

        this.singleplayer_start.setDisable(true);
        this.btn_create_newLobby.setDisable(true);
        this.textFieldNicknameS.setText("");
        this.playerOneNameTextField.setText("");
        this.textFieldNicknameS.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        this.playerOneNameTextField.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
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

        if( this.server != null ||  this.client != null){
            this.closeConnection();
        }



        SINGLEPLAYER = false;

        SERVER = false;
        CLIENT = false;


    }

    public void highscore_modal(){
        HighscoreController modal =   new HighscoreController();
        highscore_list();
        modal.highScoreCont();
    }

    private boolean checkNickname(String text)
    {
        // if OK return true
        return PATTERN_NICKNAME.matcher(text).matches() ? true : false;
    }

    public void startgame_multi() {

        //rmi.getStartGame();
        startgame();
    }

    public void startgame()  {

        roundCounter += 1;

        String playerName;

        if (roundCounter == 2){

            //// Load those only once in program

            overall_P1 = 0;

            highscore_list();

             //insert hardcode users to populate list
           //  arrayList_Highscore.add(new HighscoreModel(25,"Marac"));
           //  arrayList_Highscore.add(new HighscoreModel(20,"Ivana"));
           //  arrayList_Highscore.add(new HighscoreModel(15,"Marko"));
           //  arrayList_Highscore.add(new HighscoreModel(10,"Lana"));
           //  arrayList_Highscore.add(new HighscoreModel(7,"Robi"));
           //  arrayList_Highscore.add(new HighscoreModel(3,"Lovorka"));
           //  arrayList_Highscore.add(new HighscoreModel(15,"Miro"));
           //  arrayList_Highscore.add(new HighscoreModel(32,"Koko"));
           //  arrayList_Highscore.add(new HighscoreModel(1,"Damira"));
           //  arrayList_Highscore.add(new HighscoreModel(0,"Ognjen"));


            if (CLIENT){
                playerName = textFieldNicknameC_static;
            }
            else if (SERVER) {
                playerName = textFieldNicknameS_static;
            }
            else{
                playerName = playerOneNameTextField.getText();
            }



        }
        else {

            playerName = P1;

        }

        Platform.runLater(() -> {

            playerDetails = new PlayerDetailsModel(playerName);

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameScreen.fxml"));

            Scene scene = null;

            try {


                FileInputStream inputstream = new FileInputStream("src/main/resources/Assets/bg_1.jpg");
                Image image = new Image(inputstream);
                ImageView imageView = new ImageView(image);

                imageView.setX(50);
                imageView.setY(25);
                imageView.setFitHeight(455);
                imageView.setFitWidth(500);

                imageView.setPreserveRatio(true);

                Group root = new Group(imageView);

                scene = new Scene(fxmlLoader.load(), 600, 400);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Stage stage = HelloApplication.getMainStage();
            //Stage stage =new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        });

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

      for (var item : model
      ) {
          System.out.println(item.toString());
      }

      arrayList_Highscore = ((ArrayList<HighscoreModel>) model.clone());

    }

    public static PlayerDetailsModel getPlayerDetails() {

        return playerDetails;
    }
    public static PlayerDetailsModel setPlayerDetails(PlayerDetailsModel d) {

        return  playerDetails = d;
    }


    @FXML public void sendMessageS(ActionEvent event) throws RemoteException {
        String msg = this.textFieldChatS.getText();
        if(!msg.isEmpty() && !msg.isBlank())
            this.server.sendChatMessage(msg);
        this.textFieldChatS.setText("");


    }
    @FXML public void sendMessageC(ActionEvent event) throws RemoteException {
        String msg = this.textFieldChatC.getText();
        if(!msg.isEmpty() && !msg.isBlank())
            this.client.sendChatMessage(msg);
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
    public void addUser(User u)
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
    public void updateUserList(List<User> users)
    {
        Platform.runLater(() -> {
            if(CLIENT)
            {
                for(int i = 0; i < users.size(); i++)
                {
                    User u = users.get(i);
                    this.listNicknameC.get(i).setText(u.getNickname());
                    this.listViewUsersC.getItems().get(i).setVisible(true);
                }
                this.connectedUsers = users.size();
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
            String privateIP = socket.getLocalAddress().getHostAddress();
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
        else if (CLIENT){
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
        if(this.checkNickname(this.textFieldNicknameS.getText()) || this.checkNickname(this.playerOneNameTextField.getText()) )
        {   this.singleplayer_start.setDisable(false);
            this.btn_create_newLobby.setDisable(false);
            this.labelErrorNicknameS.setVisible(false);
            this.labelErrorNicknameSP.setVisible(false);
            this.textFieldNicknameS.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
            this.playerOneNameTextField.setStyle("-fx-border-width: 0px; -fx-focus-color: #039ED3;");
        }
        // NOK
        else
        {   this.singleplayer_start.setDisable(true);
            this.btn_create_newLobby.setDisable(true);
            this.labelErrorNicknameS.setVisible(true);
            this.labelErrorNicknameSP.setVisible(true);
            this.textFieldNicknameS.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            this.playerOneNameTextField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
        }
    }

}