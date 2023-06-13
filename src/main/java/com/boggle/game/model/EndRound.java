package com.boggle.game.model;

import com.boggle.game.boggle.HelloApplication;
import com.boggle.game.rmi.GameServer;
import com.boggle.game.rmi.GameServerImpl;
import com.boggle.game.rmi.ServerConnectionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class EndRound {

    private final GameServerImpl gameServer;
    private final GameServer gameClient;
    private final ServerConnectionManager serverConnectionManager;

    public EndRound(GameServerImpl gameServer, GameServer gameClient, ServerConnectionManager serverConnectionManager) {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("EndRound.fxml"));

        this.gameClient = gameClient;
        this.gameServer = gameServer;
        this.serverConnectionManager = serverConnectionManager;

        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        com.boggle.game.boggle.EndRound controller = fxmlLoader.getController();
        controller.setGameData(this.gameServer, this.gameClient, serverConnectionManager);

        Stage stage = HelloApplication.getMainStage();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
