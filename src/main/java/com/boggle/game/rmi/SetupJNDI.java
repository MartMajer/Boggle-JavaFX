package com.boggle.game.rmi;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

public class SetupJNDI {


    private final GameServer gameServer;

    public SetupJNDI(GameServer gameServer) {

        this.gameServer = gameServer;

        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
            env.put(Context.PROVIDER_URL, "rmi://localhost:1099");
            Context namingContext = new InitialContext(env);



            // Bind the remote object
            namingContext.bind("GameService", gameServer);

            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    /*
    RMI enables remote method invocation and communication between client and server,
    while JNDI provides a naming service to locate and access remote objects. Together,
    they facilitate remote communication and service registration in Java applications.
*/

}
