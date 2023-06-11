package com.boggle.game.rmi;

import com.boggle.game.model.BoggleSolverModel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GameServer extends Remote {
    void sendGameBoard(char[][] gameBoard, BoggleSolverModel _solver) throws RemoteException;
    void sendPlayer_1_score(Integer newScore) throws RemoteException;
    Integer getPlayer_1_score( ) throws RemoteException;
    char[][] getGameBoard() throws RemoteException;
    void sendPlayer_2_score(int score) throws RemoteException;
    Integer getPlayer_2_score( ) throws RemoteException;
    BoggleSolverModel getBoggleSolver() throws RemoteException;
    void sendTimeSync(int _time) throws RemoteException;
    int getTimeSync() throws RemoteException;
    void sendPlayer_1_name(String name) throws RemoteException;
    void sendPlayer_2_name(String name) throws RemoteException;
    String getPlayer_1_name() throws RemoteException;
    String getPlayer_2_name() throws RemoteException;
    Integer getPlayer_1_overall() throws RemoteException;
    Integer getPlayer_2_overall() throws RemoteException;
    void setPlayer_1_overall(Integer overall_player_1) throws RemoteException;
    void setPlayer_2_overall(Integer overall_player_2) throws RemoteException;

    void setPlayer_1_checked_words(List<String> checked_words) throws RemoteException;

    List<String> getPlayer_1_checked_words() throws RemoteException;

    void setPlayer_2_checked_words(List<String> checked_words) throws RemoteException;

    List<String> getPlayer_2_checked_words() throws RemoteException;

    void startNewRound() throws RemoteException;

    void addOverallScorePlayer1(int score) throws RemoteException;

    Integer getOverallScorePlayer1() throws RemoteException;

    void addOverallScorePlayer2(int score) throws RemoteException;

    Integer getOverallScorePlayer2() throws RemoteException;
}