package MainApplication;

import CS4B.Messages.*;
import MainApplication.Observer.ChatLogicObserver;
import MainApplication.Observer.ChatLogicSubject;

import MainApplication.PacketWrapper.ServerPacket;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class ServerPublisher implements Runnable, ChatLogicSubject {
    private List<ChatLogicObserver> chatLogicObservers;

    private BlockingQueue<ServerPacket> publishMessageQueue;
    private List<ClientConnection> clientConnectionList;
    private List<ChatroomPublisher> chatroomPublisherList;
    private ArrayList<String> chatrooms;

    public ServerPublisher(BlockingQueue<ServerPacket> publishMessageQueue, List<ClientConnection> clientConnectionList, List<ChatLogicObserver> chatLogicObservers, ArrayList<String> chatrooms) {
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;
        this.chatLogicObservers = chatLogicObservers;
        this.chatrooms = chatrooms;

        chatroomPublisherList = new ArrayList<>();
        for(String s : chatrooms){
            chatroomPublisherList.add(new ChatroomPublisher(s, chatLogicObservers));
        }
        System.out.println("Size of chatroompublisher list: " + chatroomPublisherList.size());

        Thread thread = new Thread(this);
        thread.start();

    }

    @Override
    public void run(){
        while(true){
            try {
                ServerPacket serverPacket = publishMessageQueue.take();

                //if the serverPacket is a request from client to be added to a chatroom
                if(serverPacket.getPacket().getMessageType().equals("JoinChatroom")){
                    for (ChatroomPublisher c :
                            chatroomPublisherList) {
                        if (c.getChatroomName().equals(serverPacket.getPacket().getChatroomName())){
                            c.addClientToRoom(serverPacket.getClientConnection());

                            //send confirmation to client
                            String clientUser = serverPacket.getClientConnection().getId().toString();
                            String chatroomName = c.getChatroomName();
                            JoinSucessful joinSucessful = new JoinSucessful(chatroomName);
                            String messageTypeBack = "JoinSuccessful";
                            Packet confirmationPacket = new Packet(clientUser, chatroomName, joinSucessful, messageTypeBack);
                            serverPacket.getClientConnection().getObjectOutputStream().writeObject(confirmationPacket);
                        }
                    }
                }
                else if(serverPacket.getPacket().getMessageType().equals("NewChatroomRequest")){
                    //received new chatroom request, need to update list of chatrooms, create new chatroompublisher, and send it back to the client
                    NewChatroom newChatroom = (NewChatroom)serverPacket.getPacket().getMessage();

                    String msg = "Adding chatroom " + newChatroom.getNewChatroomName() + " to list";
                    System.out.println(msg);
                    notifyObserverText(msg);
                    chatrooms.add(newChatroom.getNewChatroomName());

                    for(ClientConnection c: clientConnectionList){
                        c.getObjectOutputStream().reset();
                        Packet packet = new Packet("Server", "N/A", new ChatroomList(chatrooms), "ChatroomList");
                        c.getObjectOutputStream().writeObject(packet);
                    }

                }
                else{
                    for (ChatroomPublisher c :
                            chatroomPublisherList) {
                        if(c.equals(serverPacket.getPacket().getChatroomName())){
                            c.addToMessageQueue(serverPacket);
                            System.out.println("Added to messageQueue");
                        }
                    }
                }


            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

//    @Override
    public void run2() {

        while(true){
            try {
                ServerPacket toPublish = publishMessageQueue.take();

                String publishMessage = "Server Publish: " + toPublish.getTrimmedMessage() + '\n';
                System.out.println(publishMessage);
                notifyObserverText(publishMessage);

                for(int i = 0; i < clientConnectionList.size(); i++){
                    clientConnectionList.get(i).getObjectOutputStream().writeObject(toPublish);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("take interrupted");
            }
            catch(IOException e){
                e.printStackTrace();
                System.out.println("IOException caught");
            }
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
