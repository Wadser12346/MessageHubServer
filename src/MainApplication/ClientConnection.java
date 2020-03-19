package MainApplication;

import CS4B.Messages.ChatMessage;
import CS4B.Messages.ChatroomList;
import CS4B.Messages.Packet;
import MainApplication.Observer.ChatLogicObserver;
import MainApplication.Observer.ChatLogicSubject;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ClientConnection implements Runnable, ChatLogicSubject {
    private List<ChatLogicObserver> chatLogicObservers;

    private Socket socket;
    private InetAddress inetAddress;
    private ObjectOutputStream objectOutputStream;

    private int clientNo;
    private PrintWriter printWriter;

    //For now it lives here, however may need to store this in ChatServer.
    private ArrayList<String> chatrooms;

    private BlockingQueue<Packet> publishMessageQueue; //passed from ChatServer
    private List<ClientConnection> clientConnectionList;

    public ClientConnection(Socket socket, InetAddress inetAddress, int clientNo, List<ClientConnection> clientConnectionList, BlockingQueue<Packet> publishMessageQueue,  PrintWriter printWriter) {
        this.socket = socket;
        this.inetAddress = inetAddress;
        this.clientNo = clientNo;
        this.printWriter = printWriter;
        this.publishMessageQueue = publishMessageQueue;
        this.clientConnectionList = clientConnectionList;

        this.clientConnectionList.add(this);
        Thread thread = new Thread(this);
        thread.start();

        chatLogicObservers = new ArrayList<>();

        chatrooms = new ArrayList<>();
        chatrooms.add("Chatroom4B");
        chatrooms.add("Random");
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

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());



            while(true){
                Packet received = (Packet) inputFromClient.readObject();
                if (received.getMessageType().equals("ChatMessage")){
                    String msg = "From client: " + received + '\n';
                    System.out.println(msg);
                    notifyObserverText(msg);
                    publishMessageQueue.put(received);
                }
                else if(received.getMessageType().equals("RequestChatroomList")){
                    //Send list of chatrooms when client requests..
                    System.out.println("Sending Chatroom List");
                    objectOutputStream.writeObject(new Packet("Server", "N/A", new ChatroomList(chatrooms), "ChatroomList"));
                }


            }
        }
        catch (IOException e) {
            String msg = "Client " + clientNo + "'s connection lost..";
            System.out.println(msg);
            printWriter.println(msg);
            printWriter.flush();
            notifyObserverText(msg);
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


    @Override
    public void addObserver(ChatLogicObserver obs) {
        chatLogicObservers.add(obs);
    }

    @Override
    public void removeObserver(ChatLogicObserver obs) {
        chatLogicObservers.remove(obs);
    }

    @Override
    public void notifyObserverText(String message) {
        for(ChatLogicObserver x: chatLogicObservers){
            x.onTextNotification(message);
        }
    }
}
