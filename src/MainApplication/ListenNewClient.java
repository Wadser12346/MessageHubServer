package MainApplication;

import MessageTypes.ChatMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ListenNewClient implements Runnable {
    int clientNo;
    private BlockingQueue<ChatMessage> publishMessageQueue; //Only here so this queue can be passed to ClientConnection

    List<Thread> clientListenThreadList;
    List<ClientConnection> clientConnectionList;


    public ListenNewClient(BlockingQueue<ChatMessage> publishMessageQueue, List<Thread> clientListenThreadList, List<ClientConnection> clientConnectionList) {
        this.publishMessageQueue = publishMessageQueue;
        this.clientListenThreadList = clientListenThreadList;
        this.clientConnectionList = clientConnectionList;

        clientNo = 0;
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
                System.out.println("Client " + clientNo + "'s host name is " + inetAddress.getHostAddress());

                ClientConnection clientConnection = new ClientConnection(socket, inetAddress, clientNo, publishMessageQueue);
//                Thread clientConnectionThread = new Thread(clientConnection);

                clientConnectionList.add(clientConnection);
//                clientListenThreadList.add(clientConnectionThread);
//                clientConnectionThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
