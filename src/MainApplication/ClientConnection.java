package MainApplication;

import MessageTypes.ChatMessage;
import javafx.application.Platform;
import javafx.scene.control.TextArea;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ClientConnection implements Runnable {
    private Socket socket;
    private InetAddress inetAddress;
    private int clientNo;
    private PrintWriter printWriter;
    private Thread thread;

    private BlockingQueue<ChatMessage> publishMessageQueue; //passed from ChatServer
    private List<ClientConnection> clientConnectionList;

    private TextArea chatLogTextArea;

    public ClientConnection(Socket socket, InetAddress inetAddress, int clientNo, List<ClientConnection> clientConnectionList, BlockingQueue<ChatMessage> publishMessageQueue) {
        this.socket = socket;
        this.inetAddress = inetAddress;
        this.clientNo = clientNo;
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;

        thread = new Thread(this);
        thread.start();
    }

    public ClientConnection(Socket socket, InetAddress inetAddress, int clientNo, List<ClientConnection> clientConnectionList, BlockingQueue<ChatMessage> publishMessageQueue,  PrintWriter printWriter) {
        this.socket = socket;
        this.inetAddress = inetAddress;
        this.clientNo = clientNo;
        this.printWriter = printWriter;
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;

        this.clientConnectionList.add(this);
        thread = new Thread(this);
        thread.start();
        System.out.println("Client Connection thread start");
    }

    public void setTextArea(TextArea ta){
        chatLogTextArea = ta;
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
                String msg = new String("From client: " + received + '\n');
                System.out.println("From client: " + received);
//                Platform.runLater(() ->
//                        chatLogTextArea.appendText("From Client " + received + '\n'));
                WriteUI.writeToChatLog(msg);
                publishMessageQueue.put(received);
            }
        }
        catch (IOException e) {
//            e.printStackTrace();
            String msg = new String("Client " + clientNo + "'s connection lost..");
            System.out.println(msg);
            printWriter.println(msg);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found exception catched");
        }
        catch (InterruptedException e) {
            System.out.println("Exception from publishMessageQueue");
        }
        finally {
            clientConnectionList.remove(this);
        }

    }


}
