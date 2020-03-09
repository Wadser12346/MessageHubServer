package Message;

import java.io.Serializable;

/**
 * This class defines the object that will be sent between the client and the server
 * This class will have a picture and a string component as well as the user information attached
 */
public class ChatMessage implements Serializable {

    private StringMessage stringMessage;
    private PictureMessage pictureMessage;

    public ChatMessage(StringMessage stringMessage, PictureMessage pictureMessage) {
        this.stringMessage = stringMessage;
        this.pictureMessage = pictureMessage;
    }

    public ChatMessage(StringMessage stringMessage) {
        this(stringMessage, null);
    }

    public ChatMessage(PictureMessage pictureMessage){
        this(null, pictureMessage);
    }

    public StringMessage getStringMessage() {
        return stringMessage;
    }

    public PictureMessage getPictureMessage() {
        return pictureMessage;
    }
}
