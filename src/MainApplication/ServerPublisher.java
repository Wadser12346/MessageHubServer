package MainApplication;

import CS4B.Messages.ChatMessage;
import CS4B.Messages.ChatroomList;
import CS4B.Messages.Packet;
import MainApplication.Observer.ChatLogicObserver;
import MainApplication.Observer.ChatLogicSubject;

import MainApplication.PacketWrapper.ServerPacket;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.*;
import java.lang.reflect.Array;
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

    public ServerPublisher(BlockingQueue<ServerPacket> publishMessageQueue, List<ClientConnection> clientConnectionList, ArrayList<String> chatrooms) {
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;


        chatLogicObservers = new ArrayList<>();

        Thread thread = new Thread(this);
        thread.start();


    }

    public void run2(){
        while(true){
            try {
                ServerPacket serverPacket = publishMessageQueue.take();

                //if the serverPacket is a request from client to be added to a chatroom
                if(serverPacket.getPacket().getMessageType().equals("JoinChatroom")){
                    for (ChatroomPublisher c :
                            chatroomPublisherList) {
                        if (c.getChatroomName().equals(serverPacket.getPacket().getChatroomName())){
                            c.addClientToRoom(serverPacket.getClientConnection());
                        }
                    }
                }
                else{
                    for (ChatroomPublisher c :
                            chatroomPublisherList) {
                        if(c.equals(serverPacket.getPacket().getChatroomName())){
                            c.addToMessageQueue(serverPacket);
                        }
                    }
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void run() {

        while(true){
            try {
                ServerPacket toPublish = publishMessageQueue.take();

                String publishMessage = "Server Publish: " + trimPacketMessage(toPublish) + '\n';
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

    private String trimPacketMessage(ServerPacket serverPacket){
        Packet packet = serverPacket.getPacket();
        if(packet.getMessage() instanceof ChatMessage){
            ChatMessage cm = (ChatMessage)packet.getMessage();
            StringBuilder stringBuilder = new StringBuilder("");
            stringBuilder.append(packet.getUser() + ": ");
            stringBuilder.append(cm.getStringMessage());

            if(cm.hasPictureMessage()){
                stringBuilder.append(" + Image");
            }

            return stringBuilder.toString();
        }

        return packet.toString();
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
