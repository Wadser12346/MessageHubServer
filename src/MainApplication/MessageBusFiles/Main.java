package MainApplication.MessageBusFiles;

import javafx.application.Application;
import javafx.stage.Stage;


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
