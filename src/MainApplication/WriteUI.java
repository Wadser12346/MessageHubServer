package MainApplication;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class WriteUI {
    private static TextArea chatLogTextArea;

    public static void setChatLogTextArea(TextArea ta){
        chatLogTextArea = ta;
    }

    public static void writeToChatLog(String message){
        Platform.runLater(()->
                chatLogTextArea.appendText(message));
    }
}
