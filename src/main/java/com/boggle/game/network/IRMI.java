package com.boggle.game.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRMI extends Remote  {

    public char[][] getBoardArray() throws RemoteException;

    void setBoardArray() throws RemoteException;

    public void sendMessage(String text) throws RemoteException;

    public String getMessage(String text) throws RemoteException;

    void getStartGame()  throws RemoteException;
}
