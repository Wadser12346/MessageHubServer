package MainApplication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class StartServer implements Runnable {
    int clientNo = 0;

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
                System.out.println("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
                System.out.println("Client " + clientNo + "'s host name is " + inetAddress.getHostAddress() + "\n");

                    Thread thread1 = new Thread(new HandleAClient(socket, inetAddress));
                    thread1.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
