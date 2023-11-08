import java.awt.*;
import java.lang.*;
import java.util.*;

public class AlgoDemo {
    // list of algorithms this class supports
    public static int BEST_FIRST_SEARCH = 0;
    public static int BREADTH_FIRST_SEARCH = 1;
    public static int DEPTH_FIRST_SEARCH = 2;
    public static int A_STAR = 3;

    private static class Node {
        int nrow, ncol;
        double f, g, h;
        Node parent;

        Node(int nrow, int ncol) {
            this.ncol = ncol;
            this.nrow = nrow;
            parent = null;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getParent() {
            return parent;
        }

        public int getNcol() {
            return ncol;
        }

        public int getNrow() {
            return nrow;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "nrow=" + nrow +
                    ", ncol=" + ncol +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return nrow == node.nrow &&
                    ncol == node.ncol;
        }

        @Override
        public int hashCode() {
            return Objects.hash(nrow, ncol);
        }
    }

    private static class NodeComparator implements Comparator<Node> {
        private int drow, dcol; // stands for destination row and destination column

        NodeComparator(int drow, int dcol) {
            this.dcol = dcol;
            this.drow = drow;
        }

        @Override
        public int compare(Node o1, Node o2) {
            double d1, d2;
            d1 = (int) Math.pow((o1.getNrow() - drow), 2) + (int) Math.pow((o1.getNcol() - dcol), 2);
            d2 = (int) Math.pow((o2.getNrow() - drow), 2) + (int) Math.pow((o2.getNcol() - dcol), 2);
            if (d1 < d2)
                return -1;
            if (d1 == d2)
                return 0;
            return 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeComparator that = (NodeComparator) o;
            return drow == that.drow &&
                    dcol == that.dcol;
        }

        @Override
        public int hashCode() {
            return Objects.hash(drow, dcol);
        }
    }
    public static void runBestFirstSearch(MapComp mapComp){
        BestFirstSearchRunnable runnable = new BestFirstSearchRunnable(mapComp);
        Thread thread = new Thread(runnable);
        mapComp.setAlgoRunnerRunnable(runnable);
        thread.start();
    }

    public static void runBreadthFirstSearch(MapComp mapComp){
        BreadthFirstSearchRunnable runnable = new BreadthFirstSearchRunnable(mapComp);
        Thread thread = new Thread(runnable);
        mapComp.setAlgoRunnerRunnable(runnable);
        thread.start();
    }

    public static void runDepthFirstSearch(MapComp mapComp){
        DepthFirstSearchRunnable runnable = new DepthFirstSearchRunnable(mapComp);
        Thread thread = new Thread(runnable);
        mapComp.setAlgoRunnerRunnable(runnable);
        thread.start();
    }

    public static void runAStarSearch(MapComp mapComp){
        A_Star_Runnable runnable = new A_Star_Runnable(mapComp);
        Thread thread = new Thread(runnable);
        mapComp.setAlgoRunnerRunnable(runnable);
        thread.start();
    }
}
