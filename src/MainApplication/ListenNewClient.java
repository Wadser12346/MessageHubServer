package MainApplication;

import MessageTypes.ChatMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ListenNewClient implements Runnable {
    int clientNo;
    private BlockingQueue<ChatMessage> publishMessageQueue; //Only here so this queue can be passed to ClientConnection
    private List<ClientConnection> clientConnectionList;

    private Thread thread;
    private PrintWriter printWriter;

    public ListenNewClient(BlockingQueue<ChatMessage> publishMessageQueue, List<ClientConnection> clientConnectionList) {
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;

        clientNo = 0;
        thread = new Thread(this);
        thread.start();
    }

    public ListenNewClient(BlockingQueue<ChatMessage> publishMessageQueue, List<ClientConnection> clientConnectionList, PrintWriter printWriter) {
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;
        this.printWriter = printWriter;

        clientNo = 0;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("MultiThreaded server started at " + new Date() + '\n');

            while(true){
                Socket socket = serverSocket.accept();
                clientNo++;
                System.out.println("Starting thread for client " + clientNo + " at " + new Date() + '\n');

                InetAddress inetAddress = socket.getInetAddress();
                System.out.println("Client " + clientNo + "'s host name is " + inetAddress.getHostName());
                System.out.println("Client " + clientNo + "'s host address is " + inetAddress.getHostAddress());

                ClientConnection clientConnection = new ClientConnection(socket, inetAddress, clientNo, clientConnectionList, publishMessageQueue, printWriter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
