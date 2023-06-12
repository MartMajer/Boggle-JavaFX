package com.boggle.game.rmi;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

import static com.boggle.game.boggle.HelloController.RMI_IP_ADDRESS;

public class SetupJNDI {


    private final GameServer gameServer;

    public SetupJNDI(GameServer gameServer) {

        this.gameServer = gameServer;

        try {

            System.out.println("Server IP CHECk_TEST: "+ RMI_IP_ADDRESS);
            System.setProperty("java.rmi.server.hostname", RMI_IP_ADDRESS);

            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
            env.put(Context.PROVIDER_URL, "rmi://"+RMI_IP_ADDRESS+":1099");
            Context namingContext = new InitialContext(env);



            // Bind the remote object
            namingContext.bind("MyRemoteObject", gameServer);

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
