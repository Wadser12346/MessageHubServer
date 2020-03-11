package MainApplication;



import MessageTypes.ChatMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientConnection implements Runnable {
    private Socket socket;
    private InetAddress inetAddress;
    private int clientNo;

    private BlockingQueue<ChatMessage> publishMessageQueue;

    public ClientConnection(Socket socket, InetAddress inetAddress, int clientNo){
        this.socket = socket;
        this.inetAddress = inetAddress;
        this.clientNo = clientNo;
    }

    public ClientConnection(Socket socket, InetAddress inetAddress, int clientNo, BlockingQueue<ChatMessage> publishMessageQueue) {
        this.socket = socket;
        this.inetAddress = inetAddress;
        this.clientNo = clientNo;
        this.publishMessageQueue = publishMessageQueue;
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
           // ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());

            while(true){
                ChatMessage received = (ChatMessage)inputFromClient.readObject();
                String message = received.getStringMessage().toString();
                System.out.println(inetAddress.getHostName() + ": " + message);
                //outputToClient.writeObject(received); //send message back to client

                publishMessageQueue.put(received);
            }
        }
        catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Connection lost..");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found exception catched");
        }
        catch (InterruptedException e) {
            //e.printStackTrace();
            System.out.println("Exception from publishMessageQueue");
        }
    }


}
