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
    public void start(Stage primaryStage) throws Exception {//        Parent root = FXMLLoader.load(getClass().getResource("FXML/Server.fxml"));
//        primaryStage.setTitle("SERVER");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();



    }

    public static void main(String[] args) {
        //launch(args);
        Thread thread = new Thread(new StartServer());
        thread.start();
    }

}
