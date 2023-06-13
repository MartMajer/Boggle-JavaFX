package com.boggle.game.rmi;

import com.boggle.game.boggle.HelloController;
import com.boggle.game.model.BoggleSolverModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GameServerImpl extends UnicastRemoteObject implements GameServer {
    private Integer scorePlayer1;
    private Integer scorePlayer2;

    private int _time;

    private char[][] gameBoard;
    private BoggleSolverModel solver;

    private String namePlayer1;
    private String namePlayer2;

    private Integer overallPlayer1;
    private Integer overallPlayer2;

    private List<String> checkedWordsPlayer1;
    private List<String> checkedWordsPlayer2;

    private Integer overallScorePlayer1 = 0;
    private Integer overallScorePlayer2 = 0;

    public GameServerImpl() throws RemoteException {
        super();
    }

    @Override
    public void sendGameBoard(char[][] gameBoard, BoggleSolverModel solver) throws RemoteException {
        this.gameBoard = gameBoard;
        this.solver = solver;
    }

    @Override
    public void setScorePlayer1(Integer newScore) throws RemoteException {
        scorePlayer1 = newScore;
        // overall_player_1 += player1_score;

    }

    @Override
    public Integer getScorePlayer1() throws RemoteException {
        return scorePlayer1;
    }

    @Override
    public char[][] getGameBoard() throws RemoteException {
        return this.gameBoard;
    }

    @Override
    public BoggleSolverModel getBoggleSolver() throws RemoteException {
        return this.solver;
    }

    @Override
    public void setScorePlayer2(int score) throws RemoteException {
        scorePlayer2 = score;
        //overall_player_2 += player2_score;


    }

    @Override
    public Integer getScorePlayer2() throws RemoteException {
        return scorePlayer2;
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
    public void setNamePlayer1(String name) throws RemoteException {
        this.namePlayer1 = name;
    }

    @Override
    public void setNamePlayer2(String name) throws RemoteException {
        this.namePlayer2 = name;
    }

    @Override
    public String getNamePlayer1() throws RemoteException {
        return this.namePlayer1;
    }

    @Override
    public String getNamePlayer2() throws RemoteException {
        return this.namePlayer2;
    }

    @Override
    public Integer getOverallPlayer1() throws RemoteException {
        return this.overallPlayer1;
    }

    @Override
    public Integer getOverallPlayer2() throws RemoteException {
        return this.overallPlayer2;
    }

    @Override
    public void setOverallPlayer1(Integer overall_player_1) throws RemoteException {
        this.overallPlayer1 = overall_player_1;
    }

    @Override
    public void setOverallPlayer2(Integer overall_player_2) throws RemoteException {
        this.overallPlayer2 = overall_player_2;
    }


    @Override
    public void setCheckedWordsPlayer1(List<String> checked_words) throws RemoteException {
        this.checkedWordsPlayer1 = new ArrayList<>(checked_words);
    }

    @Override
    public List<String> getCheckedWordsPlayer1() throws RemoteException {
        return this.checkedWordsPlayer1;
    }

    @Override
    public void setCheckedWordsPlayer2(List<String> checked_words) throws RemoteException {
        this.checkedWordsPlayer2 = new ArrayList<>(checked_words);
    }

    @Override
    public List<String> getCheckedWordsPlayer2() throws RemoteException {
        return this.checkedWordsPlayer2;
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
            element.appendChild(xmlDocument.createTextNode(scorePlayer1 == null ? "" : String.valueOf(scorePlayer1)));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("player2_score");
            element.appendChild(xmlDocument.createTextNode(scorePlayer2 == null ? "" : String.valueOf(scorePlayer2)));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("time");
            element.appendChild(xmlDocument.createTextNode(_time == 0 ? "" : String.valueOf(_time)));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("player_1_name");
            element.appendChild(xmlDocument.createTextNode(namePlayer1 == null ? "" : namePlayer1));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("player_2_name");
            element.appendChild(xmlDocument.createTextNode(namePlayer2 == null ? "" : namePlayer2));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("overall_player_1");
            element.appendChild(xmlDocument.createTextNode(overallPlayer1 == null ? "" : String.valueOf(overallPlayer1)));
            rootElement.appendChild(element);

            element = xmlDocument.createElement("overall_player_2");
            element.appendChild(xmlDocument.createTextNode(overallPlayer2 == null ? "" : String.valueOf(overallPlayer2)));
            rootElement.appendChild(element);

            String player1CheckedWords = (checkedWordsPlayer1 == null) ? "" : String.join(", ", checkedWordsPlayer1);
            element = xmlDocument.createElement("player_1_checked_words");
            element.appendChild(xmlDocument.createTextNode(player1CheckedWords));
            rootElement.appendChild(element);

            String player2CheckedWords = (checkedWordsPlayer2 == null) ? "" : String.join(", ", checkedWordsPlayer2);
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
