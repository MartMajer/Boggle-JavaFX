package com.boggle.game.rmi;

import com.boggle.game.model.BoggleSolverModel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameServerImpl extends UnicastRemoteObject implements GameServer {
    private Integer score;
    private Integer clientScore;

    private int _time;

    private char[][] gameBoard;
    private BoggleSolverModel _solver;

    public GameServerImpl() throws RemoteException {
        super();


    }

    @Override
    public void sendGameBoard(char[][] gameBoard, BoggleSolverModel _solver) throws RemoteException {
        this.gameBoard = gameBoard;
        this._solver = _solver;
    }

    @Override
    public void updateScore(Integer newScore) throws RemoteException {
        score = newScore;
    }
    @Override
    public  Integer getScore( ) throws RemoteException {
       return  score;
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
    public void clientUpdateScore(int score) throws RemoteException{
        clientScore = score;
    }

    @Override
    public Integer getClientScore( ) throws RemoteException{
        return clientScore;
    }
    @Override
    public void sendTimeSync(int _time) throws RemoteException {
         this._time = _time;
    }
    @Override
    public int getTimeSync() throws RemoteException {
        return this._time;
    }

}
