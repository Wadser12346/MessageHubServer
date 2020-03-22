package MainApplication;

import CS4B.Messages.ChatMessage;
import CS4B.Messages.Packet;
import MainApplication.Observer.ChatLogicObserver;
import MainApplication.Observer.ChatLogicSubject;

import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChatServer implements Runnable, ChatLogicSubject {
    private List<ChatLogicObserver> chatLogicObservers;
    private ArrayList<String> chatrooms;

    private List<ClientConnection> clientConnectionList;
    private BlockingQueue<Packet> publishMessageQueue;

    private ServerPublisher serverPublisher;
    private ListenNewClient listenNewClient;
    private Thread thread;

    public ChatServer() {
        publishMessageQueue = new ArrayBlockingQueue<>(100);
        clientConnectionList = new ArrayList<>();

        thread = new Thread(this);
        chatLogicObservers = new ArrayList<>();

        chatrooms = new ArrayList<>();
        chatrooms.add("Chatroom4B");
        chatrooms.add("Random");
    }

    public void startServer(){
        serverPublisher = new ServerPublisher(publishMessageQueue,clientConnectionList);
        listenNewClient = new ListenNewClient(publishMessageQueue, clientConnectionList, chatrooms);
        //add observers, since observer list is not passed down.
        for(int i = 0; i < chatLogicObservers.size(); i++){
            serverPublisher.addObserver(chatLogicObservers.get(i));
            listenNewClient.addObserver(chatLogicObservers.get(i));
        }
        String startMsg = "MultiThreaded server started at " + new Date() + '\n';
        System.out.println(startMsg);
        notifyObserverText(startMsg);
    }

    public void main() {

        Scanner in = new Scanner(System.in);
        int num = 1;
        while(num !=0){
            num = in.nextInt();
            if(num == 1){
                System.out.println("List of client Connection: ");
                printListClientConnection();
            }
        }
    }

    public List<ClientConnection> getClientConnectionList() {
        return clientConnectionList;
    }

    public void printListClientConnection(){
        System.out.println(clientConnectionList);
    }

    public void startThread(){
        thread.start();
    }

    @Override
    public void run() {
        main();
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
        for(int i = 0; i < chatLogicObservers.size(); i++){
            chatLogicObservers.get(i).onTextNotification(message);
        }
    }
}
