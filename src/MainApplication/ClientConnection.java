package MainApplication;

import MessageTypes.ChatMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ClientConnection implements Runnable {
    private Socket socket;
    private InetAddress inetAddress;
    private int clientNo;
    private Thread thread;

    private BlockingQueue<ChatMessage> publishMessageQueue; //passed from ChatServer
    private List<ClientConnection> clientConnectionList;

    public ClientConnection(Socket socket, InetAddress inetAddress, int clientNo, List<ClientConnection> clientConnectionList, BlockingQueue<ChatMessage> publishMessageQueue) {
        this.socket = socket;
        this.inetAddress = inetAddress;
        this.clientNo = clientNo;
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;

        this.clientConnectionList.add(this);

        thread = new Thread(this);
        thread.start();
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
            ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());

            while(true){
                ChatMessage received = (ChatMessage)inputFromClient.readObject();
                System.out.println("From client: " + received);

                publishMessageQueue.put(received);
            }
        }
        catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Client " + clientNo + "'s connection lost..");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found exception catched");
        }
        catch (InterruptedException e) {
            //e.printStackTrace();
            System.out.println("Exception from publishMessageQueue");
        }
        finally {
            clientConnectionList.remove(this);
        }

    }


}
