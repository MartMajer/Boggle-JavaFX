package com.boggle.game.socket;

public interface IClient {
    void sendChatMessage(String content);

    void sendReady(boolean ready);

    void sendClose();
}
