package MainApplication;

import CS4B.Messages.ChatMessage;
import CS4B.Messages.Packet;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChatroomPublisher implements Runnable {
    private String chatroomName;
    private List<ClientConnection> subscribedClientList;
    private BlockingQueue<Packet> chatroomMessageQueue;

    public ChatroomPublisher(String chatroomName, List<ClientConnection> subscribedClientList) {
        this.chatroomName = chatroomName;
        this.subscribedClientList = subscribedClientList;

        chatroomMessageQueue = new ArrayBlockingQueue<>(500);
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof String){
            return obj.equals(chatroomName);
        }
        return false;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    void addToMessageQueue(Packet packet) throws InterruptedException {
        chatroomMessageQueue.put(packet);
    }

    public void addClientToRoom(ClientConnection client){
        subscribedClientList.add(client);
    }

    //TODO: Move this function to inside packet so server can print necessary info easily, or update toString();
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
    public void run() {
        while(true){
            try {
                Packet toPublish = chatroomMessageQueue.take();
                String publishMessage = chatroomName + " publish: " + trimPacketMessage(toPublish);
                System.out.println(publishMessage);

                for (ClientConnection c :
                        subscribedClientList) {
                    c.getObjectOutputStream().writeObject(toPublish);
                }
                
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
