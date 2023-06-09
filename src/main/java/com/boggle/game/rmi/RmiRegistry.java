package com.boggle.game.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiRegistry {


    private final GameServerImpl gameServer;

    public RmiRegistry(GameServerImpl gameServer) {

        this.gameServer = gameServer;

        try {
            // Create RMI registry and start it on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("RMI registry ready...");

            // Instantiate your remote object
            gameServer = new GameServerImpl();

            // Bind your remote object to the RMI registry
            registry.bind("MyRemoteObject", gameServer);

            System.out.println("Server is ready...");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public GameServerImpl getGameServer() {
        return gameServer;
    }
}
