package MainApplication;

import CS4B.Messages.ChatMessage;
import CS4B.Messages.Packet;
import MainApplication.PacketWrapper.ServerPacket;
import MainApplication.Observer.ChatLogicObserver;
import MainApplication.Observer.ChatLogicSubject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChatroomPublisher implements Runnable, ChatLogicSubject {
    private List<ChatLogicObserver> chatLogicObservers;
    private String chatroomName;
    private List<ClientConnection> subscribedClientList;
    private BlockingQueue<ServerPacket> chatroomMessageQueue;


    public ChatroomPublisher(String chatroomName, List<ChatLogicObserver> chatLogicObservers) {
        this.chatroomName = chatroomName;
        this.chatLogicObservers = chatLogicObservers;
        chatroomMessageQueue = new ArrayBlockingQueue<>(500);
        this.subscribedClientList = new ArrayList<>();
    }

    public String getChatroomName() {
        return chatroomName;
    }

    void addToMessageQueue(ServerPacket serverPacket) throws InterruptedException {
        chatroomMessageQueue.put(serverPacket);
    }

    public void addClientToRoom(ClientConnection client){
        subscribedClientList.add(client);
    }

    //TODO: Move this function to inside packet so server can print necessary info easily, or update toString();
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
    public void run() {
        while(true){
            try {
                ServerPacket serverPacket = chatroomMessageQueue.take();
                String publishMessage = chatroomName + " publish: " + trimPacketMessage(serverPacket);
                System.out.println(publishMessage);

                for (ClientConnection c :
                        subscribedClientList) {
                    c.getObjectOutputStream().writeObject(serverPacket);
                }
                
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
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
        for (ChatLogicObserver c :
                chatLogicObservers) {
            c.onTextNotification(message);
        }
    }
}
