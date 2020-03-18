package MainApplication.Observer;

import java.util.List;

public interface ChatLogicSubject {
    public void addObserver(ChatLogicObserver obs);
    public void removeObserver(ChatLogicObserver obs);
    public void notifyObserverText(String message);
}
