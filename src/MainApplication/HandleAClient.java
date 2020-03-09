package MainApplication;


import java.io.*;
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
            //TODO: ERROR IS HERE< CHECK INPUT STREAM TYPE
            ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());

            while(true){
                ChatMessage received = (ChatMessage)inputFromClient.readObject();
                String message = received.getStringMessage().toString();
                System.out.println(inetAddress.getHostName() + ": " + message);
                outputToClient.writeObject(received); //send message back to client
            }
        }
        catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Connection lost..");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found exception catched");
        }
    }

}
