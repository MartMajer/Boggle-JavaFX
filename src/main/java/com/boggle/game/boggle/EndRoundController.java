package com.boggle.game.boggle;

import com.boggle.game.model.HighscoreModel;
import com.boggle.game.model.StoredDetailsModel;
import javafx.event.ActionEvent;
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

import java.io.*;
import java.lang.reflect.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static com.boggle.game.boggle.GameScreenController._charArray;
import static com.boggle.game.boggle.HelloController.*;
import static com.boggle.game.boggle.HighscoreController.arrayList_Highscore;

public class EndRoundController implements Initializable {

    @FXML
    private Button _board;

    @FXML
    private Button _highscore;
    @FXML
    private Button _startNewRound;

    @FXML
    private Label _player_1_name;
    @FXML
    private Label _player_2_name;

    @FXML
    private Label _player_1_RoundScore;
    @FXML
    private Label _player_2_RoundScore;

    @FXML
    private Label _player_1_Overall;
    @FXML
    private Label _player_2_Overall;

    @FXML
    private VBox __player_1_Pane_Found;
    @FXML
    private VBox __player_2_Pane_Found;
    @FXML
    private VBox __player_1_Pane_Possible;
    @FXML
    private VBox __player_2_Pane_Possible;

    @FXML
    private Label _roundNumber;


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
    public static Integer static_overall = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        _player_1_name.setText(getPlayerDetails().getPlayerName());

        _player_1_RoundScore.setText(getPlayerDetails().get_score());

        if (game_loaded == true){

            _board.setDisable(true);
            // game_loaded=false;
            _highscore.setDisable(true);


        }

        if (SINGLEPLAYER == true) {
            ap_singleplayer.setVisible(true);
        }

        Integer temp = roundCounter - 1;
        _roundNumber.setText(temp.toString());

        arrayList_Highscore.add(new HighscoreModel(getPlayerDetails().get_score_int(), getPlayerDetails().getPlayerName()));
        if(game_loaded == false) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("highscore.ser"))) {

                oos.writeObject(arrayList_Highscore);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (var word : getPlayerDetails().get_listOfCheckedWords()) {
            //add found words to pane
            __player_1_Pane_Found.getChildren().add(new Label(word));

        }


        for (var word : getPlayerDetails().get_PossibleWords()) {
            //add found words to pane
            __player_1_Pane_Possible.getChildren().add(new Label(word));

        }



            Integer temp1 = getPlayerDetails().get_score_int() + getPlayerDetails().get_overall_int();

            static_overall = temp1;


        _player_1_Overall.setText(static_overall.toString());

    }

    public void savegame_menuitem(ActionEvent actionEvent) throws IOException, RuntimeException, ClassNotFoundException {

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


    public void startNewRound(ActionEvent actionEvent) {

        HelloController hello = new HelloController();

        Integer temp = roundCounter - 1;

        game_loaded=false;

        store = new StoredDetailsModel(_player_1_name.getText(), getPlayerDetails().get_score_int(),temp.toString(), _charArray);



        if (SINGLEPLAYER == true)
        {
            hello.startgame();

        }
        else{
            System.out.println("MULTIPLAYER method not implemented");
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

            StringBuilder classInformation = new StringBuilder();

            for(String fqn : fqnList) {
                Class classe = Class.forName(fqn);

                classInformation.append("</br>");
                classInformation.append("<h1 style=\"color:blue;\">" + classe.getSimpleName() + "</h1><br />");

                Field[] fields = classe.getDeclaredFields();

                for(Field field : fields) {
                    classInformation.append("<h2>" + Modifier.toString(field.getModifiers()) + " " + field.getName() + "</h2>");
                }

                Constructor[] constructors = classe.getConstructors();

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

                    classInformation.append("<h3>" + Modifier.toString(constructor.getModifiers()) + " " + constructor.getDeclaringClass().getSimpleName()
                            + " (" + paramsString + ")</h3>");
                }

                //

                Method[] methods = classe.getDeclaredMethods();

                classInformation.append("</br>");

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

                    classInformation.append("<h3 style=\"color:purple;\">" + Modifier.toString(method.getModifiers()) + " " + method.getName()
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
                    .append("<h1 style=\"color:red;\">Class list</h1>")
                    .append(classInformation.toString())
                    .append("</body>")
                    .append("</html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }





}
