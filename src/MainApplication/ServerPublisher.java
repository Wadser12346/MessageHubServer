package MainApplication;

import MessageTypes.ChatMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerPublisher implements Runnable {
    //TODO: Trim down states that we don't need.

    //These lists are passed from ChatServer.

    BlockingQueue<ChatMessage> publishMessageQueue;
    private List<Thread> clientListenThreadList; //THREAD OR CLIENTCONNECTION?????
    private List<ClientConnection> clientConnectionList;

    private List<ObjectOutputStream> clientOutputStreams;

    ObjectOutputStream objectOutputStream;
    int capacity;

    public ServerPublisher(BlockingQueue<ChatMessage> publishMessageQueue , List<Thread> clientListenThreadList, List<ClientConnection> clientConnectionList) throws IOException {
        capacity = 100;

        this.publishMessageQueue = publishMessageQueue;

        this.clientConnectionList = clientConnectionList;
        this.clientListenThreadList = clientListenThreadList;

        objectOutputStream = null;
    }

    @Override
    public void run() {
        while(true){

            System.out.println("Producer: ");
            try {
                ChatMessage toPublish = publishMessageQueue.take();
                System.out.println(toPublish);
                for(int i = 0; i < clientConnectionList.size(); i++){
                    objectOutputStream = new ObjectOutputStream(clientConnectionList.get(i).getSocket().getOutputStream());
                    objectOutputStream.writeObject(toPublish);
                }

            }
            catch (InterruptedException | IOException e) {
                e.printStackTrace();
                System.out.println("take interrupted");
            }
        }
    }
}
