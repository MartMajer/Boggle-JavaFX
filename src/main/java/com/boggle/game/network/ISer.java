package com.boggle.game.network;

public interface ISer {
    void sendChatMessage(String content);

    boolean checkCanStartGame();

    void sendClose();
}
