package com.boggle.game.socket;

import java.io.*;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.boggle.game.boggle.HelloController;
import com.boggle.game.model.PlayerModel;
import com.boggle.game.model.chat.Message;
import com.boggle.game.model.chat.MessageType;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

public class ServerStream implements IServer {

    private static final int PORT = 9001;
    private int minToStartGame = 2;
    private int maxNumUsers = 6;
    private HelloController controller;
    private String nickname;

    public static String privateIP;


    private ServerListener serverListener;

    private ArrayList<PlayerModel> players;
    private ArrayList<ObjectOutputStream> writers;
    private ArrayList<PlayerModel> bannedPlayers;

    public ServerStream(HelloController controller, String nickname)
    {
        this.controller = controller;
        this.nickname = nickname;


        this.players = new ArrayList<PlayerModel>();
        PlayerModel u = new PlayerModel(nickname);
        u.setReady(true);
        this.players.add(u);
        this.writers = new ArrayList<ObjectOutputStream>();
        this.writers.add(null);


        try {
            this.serverListener = new ServerListener(PORT);
            this.serverListener.start();

           // this.controller.(); // if everything is ok, we can switch to Server Room View
        } catch (IOException e) {
            System.out.println("Server: ServerSocket creation failed");
            if(e instanceof BindException)
            {
                System.out.println("Server: another socket is already binded to this address and port");
                try(final DatagramSocket socket = new DatagramSocket()) {
                    socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                    privateIP = socket.getLocalAddress().getHostAddress();
                    this.controller.showAlert(AlertType.ERROR, "Room creation failed", "Another socket is already binded to " + privateIP + ":" + PORT);
                } catch (SocketException e1) {
                    e.printStackTrace();
                } catch (UnknownHostException e1) {
                    e.printStackTrace();
                }
            }
        }
    }



    private class ServerListener extends Thread {

        private ServerSocket listener;

        public ServerListener(int port) throws IOException
        {
            this.listener = new ServerSocket(port);
            System.out.println("Server (" + this.getId() + "): listening for connections on port " + PORT);
        }

        @Override
        public void run()
        {
            try {
                while(true)
                {
                    new Handler(this.listener.accept()).start();
                }
            } catch(SocketException e) {
                System.out.println("Server (" + this.getId() + "): " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Server: error while trying to accept a new connection");
                e.printStackTrace();
            } finally {
                try {
                    this.listener.close(); // CHECK
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void closeSocket()
        {
            try {
                this.listener.close();
            } catch (IOException e) {
                System.out.println("IOException while closing the socket");
                e.printStackTrace();
            }
        }
    }

    private class Handler extends Thread {
        private Socket socket;

        private InputStream is;
        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;


        public Handler(Socket socket)
        {
            System.out.println("Server (" + this.getId() + "): connection accepted");
            this.socket = socket;
        }

        @Override
        public void run()
        {
            try {
                // NB: the order of these is important (server: input -> output)
                this.is = this.socket.getInputStream();
                this.input = new ObjectInputStream(this.is);
                this.os = this.socket.getOutputStream();
                this.output = new ObjectOutputStream(this.os);

                while(this.socket.isConnected())
                {
                    Message incomingMsg = (Message) this.input.readObject();
                    if(incomingMsg != null)
                    {
                        System.out.println("Server (" + this.getId() + "): received " + incomingMsg.toString());
                        switch(incomingMsg.getMsgType())
                        {
                            case CONNECT:
                            {
                                System.out.println("Server: connect message received");

                                Message mReply = new Message();
                                mReply.setTimestamp(controller.getCurrentTimestamp());

                                 if(players.size() == maxNumUsers)
                                {
                                    mReply.setMsgType(MessageType.CONNECT_FAILED);
                                    mReply.setNickname("");
                                    mReply.setContent("The room is full");
                                }

                                // a username with the same nickname is already inside the room
                                else if(checkDuplicateNickname(incomingMsg.getNickname()))
                                {
                                    mReply.setMsgType(MessageType.CONNECT_FAILED);
                                    mReply.setNickname("");
                                    mReply.setContent("Nickname '" + incomingMsg.getNickname() + "' already present");
                                }
                                // the connection can be accepted
                                else
                                {
                                    // add user and writer to list
                                    PlayerModel u = new PlayerModel(incomingMsg.getNickname(), this.socket.getInetAddress());
                                    players.add(u);
                                    writers.add(this.output);
                                    controller.addUser(u);

                                    // forward to other users the new user joined
                                    mReply.setMsgType(MessageType.USER_JOINED);
                                    mReply.setNickname(incomingMsg.getNickname());
                                    forwardMessage(mReply);

                                    // create OK message, containing the updated user list
                                    mReply.setMsgType(MessageType.CONNECT_OK);
                                    mReply.setNickname(nickname);
                                    mReply.setContent(getUserList());

                                    // add the message to the chat textArea
                                    controller.addToTextArea(mReply.getTimestamp() + " " + incomingMsg.getNickname() + " has joined the room");
                                }
                                // send back a reply for the CONNECT request
                                this.output.writeObject(mReply);

                                break;
                            }
                            case CHAT: {
                                // add the message to the chat textArea
                                controller.addToTextArea(incomingMsg);

                                // forward the ready message
                                forwardMessage(incomingMsg);

                                break;
                            }
                            case READY: {
                                // add the message to the chat textArea
                                controller.addToTextArea(incomingMsg);

                                // update the readiness of the user who sent the message


                                for (PlayerModel player : players) {
                                    System.out.println(player.getNickname() + " is ready? " + player.isReady() );

                                    if (player.getNickname().equals(incomingMsg.getContent().split(" ")[1])) {

                                        player.setReady(!player.isReady());
                                        System.out.println(player.getNickname() + " is ready? " + player.isReady() );
                                        break;
                                    }
                                }

                                // forward the ready message
                                forwardMessage(incomingMsg);

                                // check if the game can start now
                                Platform.runLater(() -> {
                                    if (checkCanStartGame()) {
                                        // if all users are ready and there are enough users to start the game, enable the start game button
                                        controller.start_game_multiplayer.setDisable(false);
                                    } else {
                                        // if not all users are ready or there are not enough users to start the game, disable the start game button
                                        controller.start_game_multiplayer.setDisable(true);
                                    }
                                });


                                break;
                            }

                            case DISCONNECT:
                            {
                                // add the message to the chat textArea
                                controller.addToTextArea(incomingMsg.getTimestamp() + " " + incomingMsg.getNickname() + " has left the room");

                                // forward disconnection to others
                                forwardMessage(incomingMsg);

                                // update controller list view
                                controller.removeUser(incomingMsg.getNickname());

                                // remove user and writer from the list
                                for(int i = 1; i < players.size(); i++)
                                {
                                    if(players.get(i).getNickname().equals(incomingMsg.getNickname()))
                                    {
                                        players.remove(i);
                                        writers.remove(i);
                                        break;
                                    }
                                }



                                // close the connection(?)
                                socket.close();

                                // stop the thread(?)
                                //interrupt();

                                break;
                            }
                            default:
                            {
                                System.out.println("Server: received unknow message type: " + incomingMsg.toString());
                                break;
                            }
                        }
                    }
                }

            } catch(SocketException e) {
                // "Connection reset" when the other endpoint disconnects
                if(e.getMessage().contains("Connection reset"))
                    System.out.println("Stream closed");
                    // "java.net.SocketException: Socket closed" - received DISCONNECT
                else if(e.getMessage().contains("Socket closed"))
                    System.out.println("Socket closed");
                else e.printStackTrace();
            } catch (IOException e) {
                // if we close the socket when kick/disconnect is received, then:
                // when the server kicks an user, IOException is thrown because the thread which is listening, tries to read from the stream, but the socket has been closed from the other endpoint
                System.out.println("Error stream (" + this.getId() + ")");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendMessage(Message message)
    {
        // send the message to each user except the server
        for(int i = 1; i < this.players.size(); i++)
        {
            try {
                this.writers.get(i).writeObject(message);
            } catch (IOException e) {
                System.out.println("IOException while trying to send message to client");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendChatMessage(String content)
    {
        Message msg = new Message(MessageType.CHAT, this.controller.getCurrentTimestamp(), this.nickname, content);

        // send the chat message to everyone
        this.sendMessage(msg);

        // add the chat message to the textArea
        this.controller.addToTextArea(msg);
    }

    @Override
    public void startGame() throws RemoteException {
        Message msg = new Message(MessageType.START_GAME, this.controller.getCurrentTimestamp(), this.nickname, "Game has started");
        controller.startGame();
        this.sendMessage(msg);
    }


    @Override
    public boolean checkCanStartGame()
    {
        for(PlayerModel u : this.players)
        {
            if(!u.isReady())
                return false;
        }
        return this.players.size() >= this.minToStartGame ? true : false;
    }

    @Override
    public void sendClose()
    {
        Message msg = new Message(MessageType.DISCONNECT, controller.getCurrentTimestamp(), this.nickname, "Server room closed");

        // send the message to each user except the server (NB: it's not a normal sendMessage
        for(int i = 1; i < this.players.size(); i++)
        {
            msg.setNickname(this.players.get(i).getNickname());
            try {
                this.writers.get(i).writeObject(msg);
            } catch (IOException e) {
                // remove the writer at index i?
                e.printStackTrace();
            }
        }

        // close the socket
        this.serverListener.closeSocket();
    }

    private void forwardMessage(Message msg)
    {
        // forward the message to each connected client, except the one that sent the message first
        for(int i = 1; i < this.players.size(); i++)
        {
            if(!msg.getNickname().equals(this.players.get(i).getNickname()))
            {
                try {
                    this.writers.get(i).writeObject(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkDuplicateNickname(String nickname)
    {
        for(PlayerModel u : this.players)
        {
            if(u.getNickname().equals(nickname))
                return true; // nickname already present
        }
        return false;
    }

    private String getUserList()
    {
        String list = "";
        for(int i = 0; i < this.players.size(); i++)
        {
            PlayerModel u = this.players.get(i);
            list += u.getNickname() + "," + u.isReady();
            list += (i == this.players.size() - 1 ? "" : ";");
        }

        return list;
    }




}