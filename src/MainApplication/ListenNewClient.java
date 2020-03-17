package MainApplication;

import MainApplication.Observer.ChatLogicObserver;
import MainApplication.Observer.ChatLogicSubject;
import MessageTypes.ChatMessage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ListenNewClient implements Runnable, ChatLogicSubject {
    private List<ChatLogicObserver> chatLogicObservers;

    int clientNo;
    private BlockingQueue<ChatMessage> publishMessageQueue; //Only here so this queue can be passed to ClientConnection
    private List<ClientConnection> clientConnectionList;

    private Thread thread;
    private PrintWriter printWriter;

    public ListenNewClient(BlockingQueue<ChatMessage> publishMessageQueue, List<ClientConnection> clientConnectionList, PrintWriter printWriter) {
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;
        this.printWriter = printWriter;
        chatLogicObservers = new ArrayList<>();

        clientNo = 0;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);

            while(true){
                Socket socket = serverSocket.accept();
                clientNo++;
                String startingMessage = "Starting thread for client " + clientNo + " at " + new Date() + '\n';

                System.out.println(startingMessage);
                printWriter.print(startingMessage);
                printWriter.flush();
                notifyObserverText(startingMessage);

                InetAddress inetAddress = socket.getInetAddress();
                String clientInfoMessage = "Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n'
                        + "Client " + clientNo + "'s host address is " + inetAddress.getHostAddress() + '\n';
                System.out.println(clientInfoMessage);
                printWriter.print(clientInfoMessage);
                printWriter.flush();
                notifyObserverText(clientInfoMessage);

                ClientConnection clientConnection = new ClientConnection(socket, inetAddress, clientNo, clientConnectionList, publishMessageQueue, printWriter);

                for(int i = 0; i < chatLogicObservers.size(); i++){
                    clientConnection.addObserver(chatLogicObservers.get(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        for(int i = 0; i < chatLogicObservers.size(); i++){
            chatLogicObservers.get(i).onTextNotification(message);
        }
    }
}
