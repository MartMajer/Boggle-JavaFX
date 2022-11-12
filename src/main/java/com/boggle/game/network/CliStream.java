package com.boggle.game.network;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.boggle.game.boggle.HelloController;
import  com.boggle.game.model.User;
import  com.boggle.game.model.chat.*;

import javafx.scene.control.Alert.AlertType;

public class CliStream implements ICli {
    private HelloController controller;
    private ClientListener clientListener;

    private String nickname;

    private OutputStream os;
    private ObjectOutputStream output;
    private InputStream is;
    private ObjectInputStream input;

    public CliStream(HelloController controller, String address, int port, String nickname)
    {
        this.controller = controller;
        this.nickname = nickname;

        this.clientListener = new ClientListener(address, port);
        this.clientListener.start();
    }

    private class ClientListener extends Thread {
        private Socket socket;
        private String address;
        private int port;

        public ClientListener(String address, int port)
        {
            this.address = address;
            this.port = port;
        }

        @Override
        public void run()
        {
            System.out.println("Client: running. Nickname: " + nickname);
            try {
                this.socket = new Socket(address, port);

                // NB: the order of these is important (client: output -> input)
                os = this.socket.getOutputStream();
                output = new ObjectOutputStream(os);
                is = this.socket.getInputStream();
                input = new ObjectInputStream(is);

                // send CONNECT message
                Message msg = new Message(MessageType.CONNECT, controller.getCurrentTimestamp(), nickname, "");
                output.writeObject(msg);

                while(this.socket.isConnected())
                {
                    Message incomingMsg = (Message) input.readObject();
                    if(incomingMsg != null)
                    {
                        System.out.println("Client (" + this.getId() + "): received " + incomingMsg.toString()); // test
                        switch(incomingMsg.getMsgType())
                        {
                            case CONNECT_FAILED:
                            {


                                // show alert
                                controller.showAlert(AlertType.INFORMATION, "Connection failed", incomingMsg.getContent());

                                break;
                            }
                            case CONNECT_OK:
                            {
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
                            case CHAT:
                            {
                                // add the message to the chat textArea
                                controller.addToTextArea(incomingMsg);

                                break;
                            }
                            case USER_JOINED:
                            {
                                // add the message to the chat textArea
                                controller.addToTextArea(incomingMsg.getTimestamp() + " " + incomingMsg.getNickname() + " has joined the room");;

                                // add the user and update the list
                                controller.addUser(new User(incomingMsg.getNickname()));

                                break;
                            }
                            case DISCONNECT:
                            {
                                // the room has been closed (connection lost from the server)
                                if(incomingMsg.getNickname().equals(nickname))
                                {
                                    // switch view
                                    controller.back_btn();

                                    // close connection(?)

                                    // show alert
                                    controller.showAlert(AlertType.INFORMATION, "Disconnected from server", incomingMsg.getContent());
                                }
                                // another user disconnected
                                else
                                {
                                    // add the message to the chat textArea
                                    controller.addToTextArea(incomingMsg.getTimestamp() + " " + incomingMsg.getNickname() + " has left the room");

                                    // controller: remove user from list
                                    controller.removeUser(incomingMsg.getNickname());
                                }

                                break;
                            }
                            default:
                            {
                                System.out.println("Client: received unknow message type: " + incomingMsg.toString());
                                break;
                            }
                        }
                    }
                }
            } catch(SocketException e) {
                System.out.println("Socket exception");
                if(e instanceof ConnectException)
                {
                    controller.showAlert(AlertType.ERROR, "Connection failed", e.getMessage());

                }
                else if(e.getMessage().equals("Connection reset"))
                {
                    System.out.println("Stream closed");
                }
                else e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Stream closed");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(Message message)
    {
        try {
            this.output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendChatMessage(String content)
    {
        Message msg = new Message(MessageType.CHAT, this.controller.getCurrentTimestamp(), this.nickname, content);

        // send the message
        this.sendMessage(msg);

        // add the message to the textArea
        this.controller.addToTextArea(msg);
    }

    @Override
    public void sendReady(boolean ready)
    {
        Message msg = new Message(MessageType.READY, this.controller.getCurrentTimestamp(), this.nickname, "" + ready);

        // send ready message
        this.sendMessage(msg);
    }

    @Override
    public void sendClose()
    {
        Message msg = new Message(MessageType.DISCONNECT, controller.getCurrentTimestamp(), this.nickname, "");

        // send disconnect message
        this.sendMessage(msg);
    }

    private List<User> extractUserList(String s)
    {
        List<User> list = new ArrayList<User>();

        String[] sTmp = s.split(";");
        for(int i = 0; i < sTmp.length; i++)
        {
            String[] sNickReady = sTmp[i].split(",");
            User u = new User(sNickReady[0]);
            u.setReady(Boolean.parseBoolean(sNickReady[1]));
            list.add(u);
        }

        return list;
    }



}
