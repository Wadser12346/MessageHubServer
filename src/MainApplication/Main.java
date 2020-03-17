package MainApplication;

import MainApplication.Controller.UIServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class Main extends Application {

    private int clientNo = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        UIServerController UIServerController = new UIServerController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Server.fxml"));
        loader.setController(UIServerController);
        Parent serverUI =loader.load();

//        Parent root = FXMLLoader.load(getClass().getResource("FXML/Server.fxml"));
        primaryStage.setTitle("SERVER");
        primaryStage.setScene(new Scene(serverUI));

        primaryStage.show();

        ChatServer chatServer = new ChatServer();
        chatServer.setTextArea(UIServerController.getServerLogTextArea());
        UIServerController.setServerIPText();
        WriteUI.setChatLogTextArea(UIServerController.getServerLogTextArea());

        chatServer.addObserver(UIServerController);


    }

    public static void main(String[] args) {
        launch(args);
    }


}
