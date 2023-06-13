package com.boggle.game.rmi;

import com.boggle.game.model.BoggleSolver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GameServer extends Remote {
    void sendGameBoard(char[][] gameBoard, BoggleSolver _solver) throws RemoteException;

    void setScorePlayer1(Integer newScore) throws RemoteException;

    Integer getScorePlayer1() throws RemoteException;

    char[][] getGameBoard() throws RemoteException;

    void setScorePlayer2(int score) throws RemoteException;

    Integer getScorePlayer2() throws RemoteException;

    BoggleSolver getBoggleSolver() throws RemoteException;

    void sendTimeSync(int _time) throws RemoteException;

    int getTimeSync() throws RemoteException;

    void setNamePlayer1(String name) throws RemoteException;

    void setNamePlayer2(String name) throws RemoteException;

    String getNamePlayer1() throws RemoteException;

    String getNamePlayer2() throws RemoteException;

    Integer getOverallPlayer1() throws RemoteException;

    Integer getOverallPlayer2() throws RemoteException;

    void setOverallPlayer1(Integer overall_player_1) throws RemoteException;

    void setOverallPlayer2(Integer overall_player_2) throws RemoteException;

    void setCheckedWordsPlayer1(List<String> checked_words) throws RemoteException;

    List<String> getCheckedWordsPlayer1() throws RemoteException;

    void setCheckedWordsPlayer2(List<String> checked_words) throws RemoteException;

    List<String> getCheckedWordsPlayer2() throws RemoteException;

    void startNewRound() throws RemoteException;

    void addOverallScorePlayer1(int score) throws RemoteException;

    Integer getOverallScorePlayer1() throws RemoteException;

    void addOverallScorePlayer2(int score) throws RemoteException;

    Integer getOverallScorePlayer2() throws RemoteException;
}