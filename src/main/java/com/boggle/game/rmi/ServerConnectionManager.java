package com.boggle.game.rmi;

public class ServerConnectionManager {

    private final SetupJNDI setupJNDI;
    private final RmiRegistry rmiRegistry;

    public ServerConnectionManager(GameServerImpl gameServer) {

        this.rmiRegistry = new RmiRegistry(gameServer);
        this.setupJNDI = new SetupJNDI(gameServer);
    }

    public RmiRegistry getRMIService() {
        return this.rmiRegistry;
    }

    public SetupJNDI getJNDIService() {
        return this.setupJNDI;
    }

}
