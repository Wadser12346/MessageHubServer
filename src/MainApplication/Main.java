package MainApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class Main extends Application {

    private int clientNo = 0;
    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("FXML/Server.fxml"));
//        primaryStage.setTitle("SERVER");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();

        StartServer server = new StartServer();

//        new Thread( () -> {
//            try {
//                ServerSocket serverSocket = new ServerSocket(8000);
//                System.out.println("MultiThreaded server started at " + new Date() + '\n');
//
//                while(true){
//                    Socket socket = serverSocket.accept();
//                    System.out.println("Starting thread for client " + clientNo + " at " + new Date() + '\n');
//                    clientNo++;
//
//                    InetAddress inetAddress = socket.getInetAddress();
//                    System.out.println("Client " + clientNo  + "'s host name is " + inetAddress.getHostName() + "\n");
//                    System.out.println("Client " + clientNo + "'s host address is " + inetAddress.getHostAddress() + "\n");
//
//                    Thread thread1 = new Thread(new HandleAClient(socket, inetAddress));
//                    thread1.start();
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }).start();
    }

    public static void main(String[] args) {
        launch(args);




    }

//    static class HandleAClient implements Runnable{
//        private Socket socket;
//        private InetAddress inetAddress;
//
//        public HandleAClient(Socket socket, InetAddress inetAddress){
//            this.socket = socket;
//            this.inetAddress = inetAddress;
//        }
//
//        @Override
//        public void run() {
//            try {
//                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
//                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
//
//                while(true){
//                    String message = inputFromClient.readUTF();
//                    System.out.println(inetAddress.getHostName() + ": " + message);
//                    outputToClient.writeUTF(message); //send message back to client
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    static class StartServerTask implements Runnable{
//        int clientNo = 0;
//
//        @Override
//        public void run() {
//            try {
//                ServerSocket serverSocket = new ServerSocket(8000);
//                System.out.println("MultiThreaded server started at " + new Date() + '\n');
//
//                while(true){
//                    Socket socket = serverSocket.accept();
//                    clientNo++;
//                    System.out.println("Starting thread for client " + clientNo + " at " + new Date() + '\n');
//
//                    InetAddress inetAddress = socket.getInetAddress();
//                    System.out.println("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
//                    System.out.println("Client " + clientNo + "'s host name is " + inetAddress.getHostAddress() + "\n");
//
////                    Thread thread1 = new Thread(new HandleAClient(socket, inetAddress));
////                    thread1.start();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
