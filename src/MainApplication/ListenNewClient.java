package MainApplication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListenNewClient implements Runnable {
    int clientNo;
    List<Thread> listClientConnections;




    public ListenNewClient() {
        clientNo = 0;
    }

    public ListenNewClient(List<Thread> listClientConnections) {
        this.listClientConnections = listClientConnections;
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

                Thread clientConnectionThread = new Thread(new ClientConnection(socket, inetAddress, clientNo));
                listClientConnections.add(clientConnectionThread);
                clientConnectionThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
