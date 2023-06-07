package com.boggle.game.rmi;

import com.boggle.game.model.BoggleSolverModel;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameServer extends Remote {
    void sendGameBoard(char[][] gameBoard, BoggleSolverModel _solver) throws RemoteException;
    void updateScore(Integer newScore) throws RemoteException;
    Integer getScore( ) throws RemoteException;
    char[][] getGameBoard() throws RemoteException;
    void clientUpdateScore(int score) throws RemoteException;
    Integer getClientScore( ) throws RemoteException;
    public BoggleSolverModel getBoggleSolver() throws RemoteException;
    void sendTimeSync(int _time) throws RemoteException;
    int getTimeSync() throws RemoteException;
}