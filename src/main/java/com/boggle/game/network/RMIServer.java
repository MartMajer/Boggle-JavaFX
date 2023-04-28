package com.boggle.game.network;

import com.boggle.game.boggle.HelloController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static com.boggle.game.boggle.GameScreenController._charArray;

public class RMIServer implements IRMI {

    public char[][] charArray;

    @Override
    public char[][] getBoardArray() throws RemoteException {
        return _charArray;
    }


    @Override
    public void setBoardArray() throws RemoteException {
        charArray=_charArray;
    }

    @Override
    public void sendMessage(String s) throws RemoteException {
        System.out.println(s);
        HelloController start =  new HelloController();
        start.startgame();
    }
    @Override
    public String getMessage(String text) throws RemoteException {
        return "[RMI-Client]Your message is: " + text;
    }
    @Override
    public void getStartGame(){
        HelloController start =  new HelloController();
        start.startgame();

    }

    public void serverStart() {
        Registry reg = null;
        try {
            reg = LocateRegistry.createRegistry(1099);
        } catch (Exception e) {
            System.out.println("[RMI]ERROR: Could not create the registry.");
            e.printStackTrace();
        }
        RMIServer serverObject = new RMIServer();
        System.out.println("[RMI]Waiting...");
        try {
            reg.rebind("server", (IRMI) UnicastRemoteObject.exportObject(serverObject, 0));
        } catch (Exception e) {
            System.out.println("[RMI]ERROR: Failed to register the server object.");
            e.printStackTrace();
        }
    }

}
