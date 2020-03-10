package MainApplication;

import MessageTypes.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerPublishThread implements Runnable {
    BlockingQueue<ChatMessage> publishMessageQueue;


    public ServerPublishThread() {
        publishMessageQueue = new ArrayBlockingQueue<>(100);
    }

    @Override
    public void run() {

    }
}
