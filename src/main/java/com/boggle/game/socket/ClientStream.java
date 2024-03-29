package com.boggle.game.socket;


import com.boggle.game.boggle.GameChooser;
import com.boggle.game.model.Player;
import com.boggle.game.model.chat.Message;
import com.boggle.game.model.chat.MessageType;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static com.boggle.game.boggle.GameChooser.RMI_IP_ADDRESS;

public class ClientStream implements IClient {
    private final GameChooser controller;
    private final ClientListener clientListener;

    private final String nickname;

    private OutputStream os;
    private ObjectOutputStream output;
    private InputStream is;
    private ObjectInputStream input;

    public ClientStream(GameChooser controller, String address, int port, String nickname) {
        this.controller = controller;
        this.nickname = nickname;

        this.clientListener = new ClientListener(address, port);
        this.clientListener.start();
    }

    private class ClientListener extends Thread {
        private Socket socket;
        private final String address;
        private final int port;

        public ClientListener(String address, int port) {
            this.address = address;
            this.port = port;
        }

        @Override
        public void run() {
            System.out.println("Client: running. Nickname: " + nickname);
            try {
                this.socket = new Socket(address, port);


                os = this.socket.getOutputStream();
                output = new ObjectOutputStream(os);
                is = this.socket.getInputStream();
                input = new ObjectInputStream(is);

                String rmiServerIp = (String) input.readObject();
                RMI_IP_ADDRESS = rmiServerIp;
                System.out.println("For RMI: " + rmiServerIp);

                //CONNECT message
                Message msg = new Message(MessageType.CONNECT, controller.getCurrentTimestamp(), nickname, "");
                output.writeObject(msg);

                while (this.socket.isConnected()) {
                    Message incomingMsg = (Message) input.readObject();
                    if (incomingMsg != null) {
                        System.out.println("Client (" + this.getId() + "): received " + incomingMsg); // test
                        switch (incomingMsg.getMsgType()) {
                            case CONNECT_FAILED: {
                                // show alert
                                controller.showAlert(AlertType.INFORMATION, "Connection failed", incomingMsg.getContent());

                                break;
                            }
                            case START_GAME: {
                                // Transition to the game screen and start the game
                                Platform.runLater(() -> {
                                    try {
                                        controller.startGame();
                                    } catch (RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                break;
                            }
                            case CONNECT_OK: {
                                // stop loading icon & switch to Client Room View
                                controller.switchToClientRoom();

                                // reset the user list
                                controller.resetList();

                                // get user list from OK message
                                controller.updateUserList(extractUserList(incomingMsg.getContent()));

                                // add the message to the chat textArea
                                controller.addToTextArea(incomingMsg.getTimestamp() + " " + nickname + " has joined the room");

                                break;
                            }
                            case READY: {

                                controller.addToTextArea(incomingMsg);

                                break;
                            }
                            case CHAT: {

                                // add the message to the chat textArea
                                controller.addToTextArea(incomingMsg);

                                break;
                            }
                            case USER_JOINED: {
                                // add the message to the chat textArea
                                controller.addToTextArea(incomingMsg.getTimestamp() + " " + incomingMsg.getNickname() + " has joined the room");

                                // add the user and update the list
                                controller.addUser(new Player(incomingMsg.getNickname()));

                                break;
                            }
                            case DISCONNECT: {
                                // the room has been closed (connection lost from the server)
                                if (incomingMsg.getNickname().equals(nickname)) {
                                    // switch view
                                    controller.backBtn();

                                    // close connection(?)

                                    // show alert
                                    controller.showAlert(AlertType.INFORMATION, "Disconnected from server", incomingMsg.getContent());
                                }
                                // another user disconnected
                                else {
                                    // add the message to the chat textArea
                                    controller.addToTextArea(incomingMsg.getTimestamp() + " " + incomingMsg.getNickname() + " has left the room");

                                    // controller: remove user from list
                                    controller.removeUser(incomingMsg.getNickname());
                                }

                                break;
                            }
                            default: {
                                System.out.println("Client: received unknow message type: " + incomingMsg);
                                break;
                            }
                        }
                    }
                }
            } catch (SocketException e) {
                System.out.println("Socket exception");
                if (e instanceof ConnectException) {
                    controller.showAlert(AlertType.ERROR, "Connection failed", e.getMessage());

                } else if (e.getMessage().equals("Connection reset")) {
                    System.out.println("Stream closed");
                } else e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Stream closed");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void sendMessage(Message message) {
        try {
            this.output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendChatMessage(String content) {
        Message msg = new Message(MessageType.CHAT, this.controller.getCurrentTimestamp(), this.nickname, content);

        // send the message
        this.sendMessage(msg);

        // add the message to the textArea
        this.controller.addToTextArea(msg);
    }

    @Override
    public void sendReady(boolean ready) {
        Message msg = new Message(MessageType.READY, this.controller.getCurrentTimestamp(), "SYSTEM", "Player " + this.nickname + " " + ((ready) ? "is READY" : "is NOT READY"));


        // send ready message
        this.sendMessage(msg);
    }

    @Override
    public void sendClose() {
        Message msg = new Message(MessageType.DISCONNECT, controller.getCurrentTimestamp(), this.nickname, "");

        // send disconnect message
        this.sendMessage(msg);
    }

    private List<Player> extractUserList(String s) {
        List<Player> list = new ArrayList<Player>();

        String[] sTmp = s.split(";");
        for (int i = 0; i < sTmp.length; i++) {
            String[] sNickReady = sTmp[i].split(",");
            Player u = new Player(sNickReady[0]);
            u.setReady(Boolean.parseBoolean(sNickReady[1]));
            list.add(u);
        }

        return list;
    }


}
