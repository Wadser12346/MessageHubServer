package MainApplication;

import MainApplication.Controller.ServerController;
import MainApplication.Observer.ChatLogicObserver;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;


public class Main extends Application implements ChatLogicObserver {

    private int clientNo = 0;
    private TextArea textArea;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ServerController serverController = new ServerController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Server.fxml"));
        loader.setController(serverController);
        Parent serverUI =loader.load();

//        Parent root = FXMLLoader.load(getClass().getResource("FXML/Server.fxml"));
        primaryStage.setTitle("SERVER");
        primaryStage.setScene(new Scene(serverUI));

        primaryStage.show();

        ChatServer chatServer = new ChatServer();
        chatServer.setTextArea(serverController.getServerLogTextArea());
        serverController.setServerIPText();
        WriteUI.setChatLogTextArea(serverController.getServerLogTextArea());

        textArea = serverController.getServerLogTextArea();

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onTextNotification(String message) {
        textArea.appendText(message);
    }
}
