package MainApplication;

import MessageTypes.ChatMessage;

import java.io.IOException;
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

    public void main() throws IOException {

        ServerPublisher serverPublisher = new ServerPublisher(publishMessageQueue,clientConnectionList);
        ListenNewClient listenNewClient = new ListenNewClient(publishMessageQueue, clientConnectionList);


    }
}
