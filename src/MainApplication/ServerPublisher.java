package MainApplication;

import MainApplication.Observer.ChatLogicObserver;
import MainApplication.Observer.ChatLogicSubject;
import MessageTypes.ChatMessage;
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

    private BlockingQueue<ChatMessage> publishMessageQueue;
    private List<ClientConnection> clientConnectionList;

    private ObjectOutputStream objectOutputStream;
    private int capacity;
    private Thread thread;

    private PrintWriter printWriter;

    public ServerPublisher(BlockingQueue<ChatMessage> publishMessageQueue, List<ClientConnection> clientConnectionList, PrintWriter printWriter) {
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;
        this.printWriter = printWriter;
        objectOutputStream = null;

        thread = new Thread(this);
        thread.start();
        System.out.println("ServerPublisher Thread start");

        chatLogicObservers = new ArrayList<>();
    }

    @Override
    public void run() {
        String startServerMessage = "Server Publish started at " + new Date() + '\n';
        printWriter.print(startServerMessage);

        printWriter.flush();
        while(true){
            try {
                ChatMessage toPublish = publishMessageQueue.take();

                String publishMessage = "Server Publish: " + toPublish + '\n';
                System.out.println(publishMessage);
                notifyObserverText(publishMessage);
                printWriter.print(publishMessage);
                printWriter.flush();

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
