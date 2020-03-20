package MainApplication;

import MainApplication.Observer.ChatLogicObserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatLogSaver implements ChatLogicObserver {
    PrintWriter printWriter;


    public void startLog() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String filename = new String(new Date() + ".txt");
        File logFile = new File("out/production/MessageHubServer/ServerLogsText/" + dateFormat.format(new Date()) + ".txt");
        FileWriter fw = new FileWriter(logFile, true);
        printWriter = new PrintWriter(fw);
    }

    @Override
    public void onTextNotification(String message) {
        printWriter.print(message);
        printWriter.flush();
    }
}
