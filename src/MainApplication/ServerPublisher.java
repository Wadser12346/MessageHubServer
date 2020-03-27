package MainApplication;

import CS4B.Messages.ChatMessage;
import CS4B.Messages.ChatroomList;
import CS4B.Messages.Packet;
import MainApplication.Observer.ChatLogicObserver;
import MainApplication.Observer.ChatLogicSubject;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ServerPublisher implements Runnable, ChatLogicSubject {
    private List<ChatLogicObserver> chatLogicObservers;

    private BlockingQueue<Packet> publishMessageQueue;
    private List<ClientConnection> clientConnectionList;
    private List<ChatroomPublisher> chatroomPublisherList;

    public ServerPublisher(BlockingQueue<Packet> publishMessageQueue, List<ClientConnection> clientConnectionList, List<ChatLogicObserver> chatLogicObservers) {
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;
        this.chatLogicObservers = chatLogicObservers;

        Thread thread = new Thread(this);
        thread.start();
    }

    public void run2(){
        while(true){
            try {
                Packet toPublish = publishMessageQueue.take();

                if(toPublish.getMessageType().equals("ConnectToChatroom")){
                    //client picked a chatroom and is now subscribed to this chatroom.
                    //add client to this list of subscribed clients of this chatroom
                    //need to send client history of chat (50 chats)
                    String chatroomName = toPublish.getChatroomName();
                    String userName = toPublish.getUser();

                    for(int i = 0; i < clientConnectionList.size(); i++){
//                        if(clientConnectionList.get(i).get)
                    }
                }

                for (ChatroomPublisher c :
                        chatroomPublisherList) {
                    if(c.getChatroomName().equals(toPublish.getChatroomName())){
                        c.addToMessageQueue(toPublish);
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
                Packet toPublish = publishMessageQueue.take();

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

    private String trimPacketMessage(Packet packet){
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
