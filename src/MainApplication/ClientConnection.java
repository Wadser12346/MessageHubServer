package MainApplication;

import CS4B.Messages.ChatMessage;
import CS4B.Messages.ChatroomList;
import CS4B.Messages.NewChatroom;
import CS4B.Messages.Packet;
import MainApplication.Observer.ChatLogicObserver;
import MainApplication.Observer.ChatLogicSubject;
import MainApplication.PacketWrapper.ServerPacket;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class ClientConnection implements Runnable, ChatLogicSubject {
    private List<ChatLogicObserver> chatLogicObservers;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private int clientNo;
    private UUID id;

    //For now it lives here, however may need to store this in ChatServer.
    private ArrayList<String> chatrooms;

    private BlockingQueue<ServerPacket> publishMessageQueue; //passed from ChatServer
    private List<ClientConnection> clientConnectionList;

    public ClientConnection(Socket socket, int clientNo, List<ClientConnection> clientConnectionList, BlockingQueue<ServerPacket> publishMessageQueue, List<ChatLogicObserver> chatLogicObservers, ArrayList<String> chatrooms) {
        this.socket = socket;
        this.clientNo = clientNo;
        this.chatrooms = chatrooms;
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;
        this.clientConnectionList.add(this);
        this.chatLogicObservers = chatLogicObservers;

        id = UUID.randomUUID();

        Thread thread = new Thread(this);
        thread.start();
    }

    public UUID getId() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getClientNo() {
        return clientNo;
    }

    public synchronized ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            while(true){
                Packet packet = (Packet)inputFromClient.readObject();
                ServerPacket received = new ServerPacket(this, packet);

                String packetReceivedType = "Packet received type: " + packet.getMessageType();
                System.out.println(packetReceivedType);
                notifyObserverText(packetReceivedType);

                if (received.getPacket().getMessageType().equals("ChatMessage")){
                    String msg = "From client: " + getLast4ID() + " : " + received.getTrimmedMessage() + '\n';
                    System.out.println(msg);
                    notifyObserverText(msg);
                    publishMessageQueue.put(received);
                }
                else if(received.getPacket().getMessageType().equals("RequestChatroomList")){
                    //Send list of chatrooms when client requests..
                    System.out.println("Sending Chatroom List");
                    objectOutputStream.writeObject(new Packet("Server", "N/A", new ChatroomList(chatrooms), "ChatroomList"));
                }
                //HANDLED IN SERVERPUBLISHER
//                else if(received.getPacket().getMessageType().equals("NewChatroomRequest")){
//                    //received new chatroom request, need to update list of chatrooms and send it back to client
//                    NewChatroom newChatroom = (NewChatroom)received.getPacket().getMessage();
//                    String msg = "Adding chatroom " + newChatroom.getNewChatroomName() + " to list";
//                    System.out.println(msg);
//                    notifyObserverText(msg);
//                    chatrooms.add(newChatroom.getNewChatroomName());
//
//                    objectOutputStream.reset();
//                    //publishMessageQueue.put(new Packet("Server", "N/A", new ChatroomList(chatrooms), "ChatroomList"));
//                    Packet packet = new Packet("Server", "N/A", new ChatroomList(chatrooms), "ChatroomList");
//                    ServerPacket sv = new ServerPacket(this, packet);
//                    publishMessageQueue.put(sv);
//                }
                else if(received.getPacket().getMessageType().equals("DisconnectMessageClient")){
                    //received disconnect message from client
                    publishMessageQueue.put(received);
                    String msg = "client" + getLast4ID() + " disconnect";
                    notifyObserverText(msg);
                    System.out.println(msg);
                    break;
                }
                else{
                    publishMessageQueue.put(received);
                }
            }
        }
        catch (IOException e) {
            String msg = "Client " + clientNo + "'s connection lost..";
            System.out.println(msg);
            notifyObserverText(msg);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found exception catched");
        }
        catch (InterruptedException e) {
            System.out.println("Exception from publishMessageQueue");
        }
        finally {
            clientConnectionList.remove(this);
        }
    }

    private String getLast4ID(){
        String stringID = id.toString();
        if(stringID.length() >4){
            return stringID.substring(stringID.length()-4);
        }
        else{
            return stringID;
        }
    }

    @Override
    public void addObserver(ChatLogicObserver obs) {
        chatLogicObservers.add(obs);
    }

    @Override
    public void removeObserver(ChatLogicObserver obs) {
        chatLogicObservers.remove(obs);
    }

    @Override
    public void notifyObserverText(String message) {
        for(ChatLogicObserver x: chatLogicObservers){
            x.onTextNotification(message);
        }
    }
}
