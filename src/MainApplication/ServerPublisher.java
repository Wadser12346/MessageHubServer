package MainApplication;

import MessageTypes.ChatMessage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        File logFile = new File(dateFormat.format(date) + ".txt");
        try {
            FileWriter fw = new FileWriter(logFile, true);
            PrintWriter printWriter = new PrintWriter(fw);
            printWriter.print("Server Publish started at " + new Date() + '\n');
            printWriter.flush();
            while(true){
                try {
                    ChatMessage toPublish = publishMessageQueue.take();
                    System.out.println("Server Publish: " + toPublish);
                    printWriter.print("Server Publish: " + toPublish + '\n');
                    printWriter.flush();
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
        catch (IOException e) {
            e.printStackTrace();
        }





    }
}
