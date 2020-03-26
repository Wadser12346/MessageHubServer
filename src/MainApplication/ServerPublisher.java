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

    public ServerPublisher(BlockingQueue<Packet> publishMessageQueue, List<ClientConnection> clientConnectionList) {
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;

        Thread thread = new Thread(this);
        thread.start();

        chatLogicObservers = new ArrayList<>();
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
