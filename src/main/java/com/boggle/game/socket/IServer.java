package com.boggle.game.socket;

import java.rmi.RemoteException;

public interface IServer {
    void sendChatMessage(String content);

    boolean checkCanStartGame();

    void sendClose();

    void startGame() throws RemoteException;
}
