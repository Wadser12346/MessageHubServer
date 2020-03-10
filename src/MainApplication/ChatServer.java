package MainApplication;

import MessageTypes.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChatServer {
    List<Thread> clientConnectionList;
    BlockingQueue<ChatMessage> publishMessageQueue;

    public ChatServer() {
        clientConnectionList = new ArrayList<>();
        publishMessageQueue = new ArrayBlockingQueue<>(100);
    }

    public void main(){
        Thread listenNewClientThread = new Thread(new ListenNewClient(clientConnectionList));
        listenNewClientThread.start();


    }
}
