import java.util.HashSet;

public interface AlgoRunnerRunnable extends Runnable {
    public boolean takeExit();

    public void setExit(boolean exit);

    HashSet<Node> moveGen4Neighbour(Node currentNode);

    HashSet<Node> moveGen8Neighbour(Node currentNode);
}
