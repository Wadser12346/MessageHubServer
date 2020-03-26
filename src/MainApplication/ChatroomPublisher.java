package MainApplication;

import CS4B.Messages.Packet;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ChatroomPublisher implements Runnable {
    private String chatroomName;
    private List<ClientConnection> subscribedClients;
    private BlockingQueue<Packet> chatroomMessageQueue;




    @Override
    public void run() {

    }
}
