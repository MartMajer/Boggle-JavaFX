package com.boggle.game.network;

public interface ICli {
    public void sendChatMessage(String content);
    public void sendReady(boolean ready);
    public void sendClose();
}
