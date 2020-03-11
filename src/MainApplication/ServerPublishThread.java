package MainApplication;

import MessageTypes.ChatMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerPublishThread implements Runnable {
    BlockingQueue<ChatMessage> publishMessageQueue;
    private List<Thread> clientListenThreadList; //THREAD OR CLIENTCONNECTION?????
    private List<ClientConnection> clientConnectionList;

    private List<ObjectOutputStream> clientOutputStreams;


    public ServerPublishThread() {
        publishMessageQueue = new ArrayBlockingQueue<>(100);
    }

    public ServerPublishThread(List<Thread> clientListenThreadList, List<ClientConnection> clientConnectionList) throws IOException {
        int capacity = 100;
        publishMessageQueue = new ArrayBlockingQueue<>(capacity);

        this.clientConnectionList = clientConnectionList;
        this.clientListenThreadList = clientListenThreadList;

        //populate outputstreams
        clientOutputStreams = new ArrayList<>();
        for(int i = 0; i < capacity; i++){
            ObjectOutputStream obj = new ObjectOutputStream(this.clientConnectionList.get(i).getSocket().getOutputStream());
            clientOutputStreams.add(obj);
        }
    }

    @Override
    public void run() {
        while(true){
            System.out.println("Producer: ");
            try {
                ChatMessage toPublish = publishMessageQueue.take();
                System.out.println(toPublish);

            }
            catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("take interrupted");
            }
        }
    }
}
