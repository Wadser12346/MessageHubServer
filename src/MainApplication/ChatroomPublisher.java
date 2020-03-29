package MainApplication;

import CS4B.Messages.ChatMessage;
import CS4B.Messages.Packet;
import MainApplication.PacketWrapper.ServerPacket;
import MainApplication.Observer.ChatLogicObserver;
import MainApplication.Observer.ChatLogicSubject;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChatroomPublisher implements Runnable, ChatLogicSubject {
    private List<ChatLogicObserver> chatLogicObservers;
    private String chatroomName;
    private List<ClientConnection> subscribedClientList;
    private BlockingQueue<ServerPacket> chatroomMessageQueue;

    private ArrayDeque<Packet> chatHistory;


    public ChatroomPublisher(String chatroomName, List<ChatLogicObserver> chatLogicObservers) {
        this.chatroomName = chatroomName;
        this.chatLogicObservers = chatLogicObservers;
        chatroomMessageQueue = new ArrayBlockingQueue<>(500);
        this.subscribedClientList = new ArrayList<>();

        chatHistory = new ArrayDeque<>();
    }

    public void addToHistory(Packet packet){
        chatHistory.add(packet);
        while(chatHistory.size() > 50){
            chatHistory.removeFirst();
        }

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


    @Override
    public void run() {
        while(true){
            try {
                ServerPacket serverPacket = chatroomMessageQueue.take();
                String publishMessage = chatroomName + " publish: " + serverPacket.getTrimmedMessage();
                System.out.println(publishMessage);

                Packet packet = serverPacket.getPacket();

                for (ClientConnection c :
                        subscribedClientList) {
                    c.getObjectOutputStream().writeObject(packet);
                }
                addToHistory(packet);
                
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
