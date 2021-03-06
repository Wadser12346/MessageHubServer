package MainApplication.Controller;

import MainApplication.Observer.ChatLogicObserver;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class UIServerController implements ChatLogicObserver {

    @FXML
    private TextArea serverLogTextArea;
    @FXML
    private TextField serverIPTextField;

    public TextArea getServerLogTextArea() {
        return serverLogTextArea;
    }

    public TextField getServerIPTextField() {
        return serverIPTextField;
    }

    public void setServerIPText() throws UnknownHostException {
        serverIPTextField.setText(InetAddress.getLocalHost().getHostAddress());
    }

    @Override
    public void onTextNotification(String message) {
        serverLogTextArea.appendText(message);
    }


}
