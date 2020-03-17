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
        UIServerController uiServerController = new UIServerController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Server.fxml"));
        loader.setController(uiServerController);
        Parent serverUI =loader.load();

//        Parent root = FXMLLoader.load(getClass().getResource("FXML/Server.fxml"));
        primaryStage.setTitle("SERVER");
        primaryStage.setScene(new Scene(serverUI));

        ChatServer chatServer = new ChatServer();
        uiServerController.setServerIPText();
        WriteUI.setChatLogTextArea(uiServerController.getServerLogTextArea());

        chatServer.addObserver(uiServerController);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
