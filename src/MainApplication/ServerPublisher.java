package MainApplication;

import MessageTypes.ChatMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerPublisher implements Runnable {
    BlockingQueue<ChatMessage> publishMessageQueue;
    private List<ClientConnection> clientConnectionList;

    ObjectOutputStream objectOutputStream;
    int capacity;
    Thread thread;

    public ServerPublisher(BlockingQueue<ChatMessage> publishMessageQueue, List<ClientConnection> clientConnectionList) throws IOException {
        capacity = 100;

        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;

        objectOutputStream = null;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(true){
            try {
                ChatMessage toPublish = publishMessageQueue.take();
                System.out.println("Server Publish: " + toPublish);
                for(int i = 0; i < clientConnectionList.size(); i++){
                    objectOutputStream = new ObjectOutputStream(clientConnectionList.get(i).getSocket().getOutputStream());
                    objectOutputStream.writeObject(toPublish);
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
}
