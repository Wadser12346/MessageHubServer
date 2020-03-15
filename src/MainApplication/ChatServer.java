package MainApplication;

import MessageTypes.ChatMessage;
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

public class ChatServer implements Runnable {
    private List<ClientConnection> clientConnectionList;
    private BlockingQueue<ChatMessage> publishMessageQueue;

    private ServerPublisher serverPublisher;
    private ListenNewClient listenNewClient;
    private PrintWriter printWriter;
    private Thread thread;

    private TextArea chatLogTextArea;

    public ChatServer() {
        publishMessageQueue = new ArrayBlockingQueue<>(100);
        clientConnectionList = new ArrayList<>();

        thread = new Thread(this);
        thread.start();
    }

    public void main() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        File logFile = new File(dateFormat.format(new Date()) + ".txt");
        FileWriter fw = new FileWriter(logFile, true);
        printWriter = new PrintWriter(fw);

        serverPublisher = new ServerPublisher(publishMessageQueue,clientConnectionList, printWriter);
        listenNewClient = new ListenNewClient(publishMessageQueue, clientConnectionList, printWriter);


        serverPublisher.setTextArea(chatLogTextArea);
        listenNewClient.setTextArea(chatLogTextArea);

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

    public void setTextArea(TextArea ta){
        chatLogTextArea = ta;
    }

    @Override
    public void run() {
        try {
            main();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
