package com.boggle.game.rmi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class ClientConnectionManager {

    private  Context namingContext;

    public ClientConnectionManager() throws NamingException {

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
        env.put(Context.PROVIDER_URL, "rmi://localhost:1099");
        namingContext = new InitialContext(env);


    }

    public GameServer getLookupNamingContext() throws NamingException {
        return (GameServer) namingContext.lookup("rmi://localhost:1099/GameService");
    }
}
