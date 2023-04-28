package com.boggle.game.network;

public interface IServer {
    void sendChatMessage(String content);

    boolean checkCanStartGame();

    void sendClose();
}
