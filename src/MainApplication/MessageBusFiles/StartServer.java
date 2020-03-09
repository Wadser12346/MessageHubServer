package MainApplication.MessageBusFiles;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StartServer implements Runnable {
    int clientNo;
    List<Thread> listThreads;

    public StartServer() {
        clientNo = 0;
        listThreads = new ArrayList<>();
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

                Thread thread1 = new Thread(new HandleAClient(socket, inetAddress, clientNo));
                listThreads.add(thread1);
                thread1.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
