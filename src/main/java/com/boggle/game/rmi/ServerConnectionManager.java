package com.boggle.game.rmi;

import java.util.concurrent.atomic.AtomicReference;

public class ServerConnectionManager {

private SetupJNDI setupJNDI;
private RmiRegistry rmiRegistry;

    public ServerConnectionManager(GameServerImpl gameServer) {

        this.rmiRegistry= new RmiRegistry(gameServer);
        this.setupJNDI= new SetupJNDI(gameServer);
    }

    public RmiRegistry getRMIService() {
        return this.rmiRegistry;
    }

    public SetupJNDI getJNDIService() {
        return this.setupJNDI;
    }

}
