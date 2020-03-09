package MainApplication.MessageBusFiles;

import java.io.Serializable;

public class StringMessage implements Serializable {
    private String message;

    public StringMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
