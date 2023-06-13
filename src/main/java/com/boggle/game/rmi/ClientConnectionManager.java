package com.boggle.game.rmi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

import static com.boggle.game.boggle.HelloController.RMI_IP_ADDRESS;

public class ClientConnectionManager {

    private final Context namingContext;

    public ClientConnectionManager() throws NamingException {

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
        env.put(Context.PROVIDER_URL, "rmi://" + RMI_IP_ADDRESS + ":1099");
        namingContext = new InitialContext(env);


    }

    public GameServer getLookupNamingContext() throws NamingException {
        System.out.println("CHECK SERVER " + RMI_IP_ADDRESS);
        return (GameServer) namingContext.lookup("rmi://" + RMI_IP_ADDRESS + ":1099/MyRemoteObject");
    }
}
