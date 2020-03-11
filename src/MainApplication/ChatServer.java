package MainApplication;

import MessageTypes.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChatServer {
    private List<Thread> clientListenThreadList;
    private List<ClientConnection> clientConnectionList;
    private BlockingQueue<ChatMessage> publishMessageQueue;


    public ChatServer() {
        clientListenThreadList = new ArrayList<>();
        publishMessageQueue = new ArrayBlockingQueue<>(100);
        clientConnectionList = new ArrayList<>();
    }



    public void main(){
        Thread listenNewClientThread = new Thread(new ListenNewClient(publishMessageQueue, clientListenThreadList, clientConnectionList));
        listenNewClientThread.start();


    }
}
