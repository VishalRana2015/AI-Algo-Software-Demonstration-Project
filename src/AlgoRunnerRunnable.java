import java.util.HashSet;
import java.util.Objects;

public interface AlgoRunnerRunnable extends Runnable {
    public boolean takeExit();

    public void setExit(boolean exit);

    HashSet<Node> moveGen4Neighbour(Node currentNode);

    void addStatusListener(StatusListener<String> statusListener);

    HashSet<Node> moveGen8Neighbour(Node currentNode);
}
