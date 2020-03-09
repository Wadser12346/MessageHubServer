package MainApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class HandleAClient implements Runnable {
    private Socket socket;
    private InetAddress inetAddress;
    private int clientNo;

    public HandleAClient(Socket socket, InetAddress inetAddress, int clientNo){
        this.socket = socket;
        this.inetAddress = inetAddress;
        this.clientNo = clientNo;
    }

    public Socket getSocket() {
        return socket;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getClientNo() {
        return clientNo;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
            DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

            while(true){
                String message = inputFromClient.readUTF();
                System.out.println(inetAddress.getHostName() + ": " + message);
                outputToClient.writeUTF(message); //send message back to client
            }

        }
        catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Connection lost..");
        }
    }

}
