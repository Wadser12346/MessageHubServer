package MainApplication;

import MessageTypes.ChatMessage;

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

public class ChatServer {
    private List<ClientConnection> clientConnectionList;
    private BlockingQueue<ChatMessage> publishMessageQueue;

    private ServerPublisher serverPublisher;
    private ListenNewClient listenNewClient;
    private PrintWriter printWriter;


    public ChatServer() {
        publishMessageQueue = new ArrayBlockingQueue<>(100);
        clientConnectionList = new ArrayList<>();
    }

    public void main() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        File logFile = new File(dateFormat.format(new Date()) + ".txt");
        FileWriter fw = new FileWriter(logFile, true);
        printWriter = new PrintWriter(fw);

        serverPublisher = new ServerPublisher(publishMessageQueue,clientConnectionList, printWriter);
        listenNewClient = new ListenNewClient(publishMessageQueue, clientConnectionList, printWriter);

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
}
