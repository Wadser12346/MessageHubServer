package MainApplication;

import java.io.Serializable;

/**
 * This class defines the object that will be sent between the client and the server
 * This class will have a picture and a string component as well as the user information attached
 */
public class ChatMessage implements Serializable {
    private String user;
    private StringMessage stringMessage;
    private PictureMessage pictureMessage;

    public ChatMessage(String user, StringMessage stringMessage, PictureMessage pictureMessage) {
        this.user = user;
        this.stringMessage = stringMessage;
        this.pictureMessage = pictureMessage;
    }

    public ChatMessage(String user, StringMessage stringMessage) {
        this(user, stringMessage, null);
    }

    public ChatMessage(String user, PictureMessage pictureMessage){
        this(user, null, pictureMessage);
    }

    public StringMessage getStringMessage() {
        return stringMessage;
    }

    public PictureMessage getPictureMessage() {
        return pictureMessage;
    }
}
