package com.boggle.game.rmi;

import com.boggle.game.boggle.HelloController;
import com.boggle.game.model.BoggleSolverModel;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class GameServerImpl extends UnicastRemoteObject implements GameServer {
    private Integer player1_score;
    private Integer player2_score;

    private int _time;

    private char[][] gameBoard;
    private BoggleSolverModel _solver;

    private String _player_1_name;
    private String _player_2_name;

    private Integer overall_player_1;
    private Integer overall_player_2;
    private List<String> player_1_checked_words;
    private List<String> player_2_checked_words;

    private Integer overallScorePlayer1 = 0;
    private Integer overallScorePlayer2 = 0;
    public GameServerImpl() throws RemoteException {
        super();
    }

    @Override
    public void sendGameBoard(char[][] gameBoard, BoggleSolverModel _solver) throws RemoteException {
        this.gameBoard = gameBoard;
        this._solver = _solver;
    }

    @Override
    public void sendPlayer_1_score(Integer newScore) throws RemoteException {
        player1_score = newScore;
       // overall_player_1 += player1_score;

    }
    @Override
    public  Integer getPlayer_1_score( ) throws RemoteException {
       return player1_score;
    }

    @Override
    public char[][] getGameBoard() throws RemoteException {
        return this.gameBoard;
    }
    @Override
    public BoggleSolverModel getBoggleSolver() throws RemoteException {
        return this._solver;
    }

    @Override
    public void sendPlayer_2_score(int score) throws RemoteException{
        player2_score = score;
        //overall_player_2 += player2_score;


    }

    @Override
    public Integer getPlayer_2_score( ) throws RemoteException{
        return player2_score;
    }
    @Override
    public void sendTimeSync(int _time) throws RemoteException {
         this._time = _time;
    }
    @Override
    public int getTimeSync() throws RemoteException {
        return this._time;
    }

    @Override
    public void sendPlayer_1_name(String name) throws RemoteException {
        this._player_1_name = name;
    }

    @Override
    public void sendPlayer_2_name(String name) throws RemoteException {
        this._player_2_name = name;
    }
    @Override
    public String getPlayer_1_name() throws RemoteException {
        return this._player_1_name;
    }
    @Override
    public String getPlayer_2_name() throws RemoteException {
        return this._player_2_name;
    }
    @Override
    public Integer getPlayer_1_overall() throws RemoteException {
        return this.overall_player_1;
    }
    @Override
    public Integer getPlayer_2_overall() throws RemoteException {
        return this.overall_player_2;
    }
    @Override
    public void setPlayer_1_overall(Integer overall_player_1) throws RemoteException {
        this.overall_player_1 = overall_player_1;
    }
    @Override
    public void setPlayer_2_overall(Integer overall_player_2) throws RemoteException {
       this.overall_player_2 = overall_player_2;
    }


    @Override
    public void setPlayer_1_checked_words(List<String> checked_words) throws RemoteException {
        this.player_1_checked_words =  new ArrayList<>(checked_words);
    }
    @Override
    public List<String> getPlayer_1_checked_words() throws RemoteException {
        return this.player_1_checked_words;
    }
    @Override
    public void setPlayer_2_checked_words(List<String> checked_words) throws RemoteException {
        this.player_2_checked_words =  new ArrayList<>(checked_words);
    }
    @Override
    public List<String> getPlayer_2_checked_words() throws RemoteException {
        return this.player_2_checked_words;
    }


    @Override
    public void startNewRound() throws RemoteException {
        HelloController hello = new HelloController();
        hello.startGame();
    }


    @Override
    public void addOverallScorePlayer1(int score) throws RemoteException {
        this.overallScorePlayer1 += score;
    }

    @Override
    public Integer getOverallScorePlayer1() throws RemoteException {
        return this.overallScorePlayer1;
    }

    @Override
    public void addOverallScorePlayer2(int score) throws RemoteException {
        this.overallScorePlayer2 += score;
    }

    @Override
    public Integer getOverallScorePlayer2() throws RemoteException {
        return this.overallScorePlayer2;
    }

    public void writeXml() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.newDocument();

            Element rootElement = xmlDocument.createElement("GameServer");

            xmlDocument.appendChild(rootElement);

            Element element = xmlDocument.createElement("player1_score");
            element.appendChild(xmlDocument.createTextNode(player1_score == null ? "" : String.valueOf(player1_score)));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("player2_score");
            element.appendChild(xmlDocument.createTextNode(player2_score == null ? "" : String.valueOf(player2_score)));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("time");
            element.appendChild(xmlDocument.createTextNode(_time == 0 ? "" : String.valueOf(_time)));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("player_1_name");
            element.appendChild(xmlDocument.createTextNode(_player_1_name == null ? "" : _player_1_name));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("player_2_name");
            element.appendChild(xmlDocument.createTextNode(_player_2_name == null ? "" : _player_2_name));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("overall_player_1");
            element.appendChild(xmlDocument.createTextNode(overall_player_1 == null ? "" : String.valueOf(overall_player_1)));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("overall_player_2");
            element.appendChild(xmlDocument.createTextNode(overall_player_2 == null ? "" : String.valueOf(overall_player_2)));
            rootElement.appendChild(element);

            String player1CheckedWords = (player_1_checked_words == null) ? "" : String.join(", ", player_1_checked_words);
            element = xmlDocument.createElement("player_1_checked_words");
            element.appendChild(xmlDocument.createTextNode(player1CheckedWords));
            rootElement.appendChild(element);

            String player2CheckedWords = (player_2_checked_words == null) ? "" : String.join(", ", player_2_checked_words);
            element = xmlDocument.createElement("player_2_checked_words");
            element.appendChild(xmlDocument.createTextNode(player2CheckedWords));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("overallScorePlayer1");
            element.appendChild(xmlDocument.createTextNode(overallScorePlayer1 == null ? "" : String.valueOf(overallScorePlayer1)));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("overallScorePlayer2");
            element.appendChild(xmlDocument.createTextNode(overallScorePlayer2 == null ? "" : String.valueOf(overallScorePlayer2)));
            rootElement.appendChild(element);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource xmlSource = new DOMSource(xmlDocument);
            StreamResult xmlResult = new StreamResult(new File("gameserver.xml"));

            transformer.transform(xmlSource, xmlResult);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
