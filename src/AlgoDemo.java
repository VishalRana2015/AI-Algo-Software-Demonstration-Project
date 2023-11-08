import java.awt.*;
import java.lang.*;
import java.util.*;

public class AlgoDemo {
    // list of algorithms this class supports
    public static int BSF = 1;
    public static int BFS = 2;
    public static int DFS = 3;
    public static int AStar = 4;

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
    // Best Search First
    public static void runBSF(MapComp comp) {
        comp.setStatus("Running BSF...");
        System.out.println("hashCode : "+ comp.hashCode());
        int rows, cols;
        rows = comp.getRows();
        cols = comp.getCols();
        Node[][] nodes = new Node[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                nodes[i][j] = new Node(i, j);
        }
        // Nodes created and initialized

        int srow, scol, drow, dcol; // stands for sourceRow, sourceColumn , destinationRow, destinationColumn
        srow = comp.getSrcRow();
        scol = comp.getSrcCol();
        drow = comp.getDstRow();
        dcol = comp.getDstCol();
        NodeComparator comparator = new NodeComparator(drow, dcol);
        comp.clear();
        Runnable runn = new Runnable() {
            boolean exit = false;
            @Override
            public void run() {
                TreeSet<Node> open = new TreeSet<Node>(comparator);
                TreeSet<Node> closed = new TreeSet<Node>(comparator);
                open.add(nodes[srow][scol]);
                while (!open.isEmpty()) {
                    Node N = open.pollFirst();
                    if (goalTest(N)) {
                        LinkedList<Node> list = reconstructPath(N);
                        // set the path and return
                        int[] rowarray = new int[list.size()];
                        int[] colarray = new int[list.size()];
                        Iterator itr = list.iterator();
                        int index = 0;
                        while (itr.hasNext()) {
                            Node node = (Node) itr.next();
                            rowarray[index] = node.getNrow();
                            colarray[index] = node.getNcol();
                            index++;
                        }
                        comp.setPath(rowarray, colarray);
                        return;
                    }
                    TreeSet<Node> neighbours = moveGen4neightbour(N);
                    open.remove(N);
                    closed.add(N);
                    neighbours.removeAll(closed);
                    neighbours.removeAll(open);
                    Iterator itr = neighbours.iterator();
                    while (itr.hasNext()) {
                        Node child = (Node) itr.next();
                        child.setParent(N);
                    }
                    open.addAll(neighbours);

                    comp.removeFromOpen(N.getNrow(), N.getNcol());
                    itr = neighbours.iterator();
                    while (itr.hasNext()) {
                        Node node = (Node) itr.next();
                        comp.addToOpen(node.getNrow(), node.getNcol());
                    }
                    // open list of the component 'comp' is updated
                    // updateing the closed list
                    comp.addToClose(N.getNrow(), N.getNcol());
                    // Now the closed list is also updated
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        System.out.println("Exception caught :" + e.getMessage());
                    }

                }

            }


            private TreeSet<Node> moveGen8neighbour(Node N) {
                TreeSet<Node> set = new TreeSet<Node>(comparator);
                int nrow, ncol; // stands for newRow , newColumn
                int Nrow, Ncol;
                Nrow = N.getNrow();
                Ncol = N.getNcol();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        nrow = Nrow - 1 + i;
                        ncol = Ncol - 1 + j;
                        if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                            set.add(nodes[nrow][ncol]);

                    }
                }
                // end of loop
                return set;
            }

            // WE will use moveGen4
            private TreeSet<Node> moveGen4neightbour(Node N) {
                TreeSet<Node> set = new TreeSet<Node>(comparator);
                int nrow, ncol; // stands for newRow , newColumn
                int Nrow, Ncol;
                Nrow = N.getNrow();
                Ncol = N.getNcol();
                nrow = Nrow - 1;
                ncol = Ncol;
                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                    set.add(nodes[nrow][ncol]);
                nrow = Nrow + 1;
                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                    set.add(nodes[nrow][ncol]);
                nrow = Nrow;
                ncol = Ncol - 1;
                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                    set.add(nodes[nrow][ncol]);
                ncol = Ncol + 1;
                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                    set.add(nodes[nrow][ncol]);

                // end of loop
                return set;
            }

            private boolean goalTest(Node n) {
                if (n.getNrow() == drow && n.getNcol() == dcol)
                    return true;
                return false;
            }

            public LinkedList<Node> reconstructPath(Node N) {
                LinkedList<Node> list = new LinkedList<Node>();
                while (N != null) {
                    list.add(N);
                    N = N.parent;
                }

                return list;
            }
        };
        Thread thread = new Thread(runn);
        // This can't be the daemon thread since it output have to be seen visually as expected
        thread.start();
    }

    // Breadth first search
    public static void runBFS(MapComp comp) {
        comp.setStatus("Running BFS ...");
        System.out.println("hashCode : " + comp.hashCode());
        comp.clear();
        int rows, cols;
        rows = comp.getRows();
        cols = comp.getCols();
        Node[][] nodes = new Node[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                nodes[i][j] = new Node(i, j);
        }
        // Nodes created and initialized
        System.out.println("Nodes created");
        int srow, scol, drow, dcol; // stands for sourceRow, sourceColumn , destinationRow, destinationColumn
        srow = comp.getSrcRow();
        scol = comp.getSrcCol();
        drow = comp.getDstRow();
        dcol = comp.getDstCol();
        NodeComparator comparator = new NodeComparator(drow, dcol);

        Runnable runn = new Runnable() {
            @Override
            public void run() {
                System.out.println("In run method");
                LinkedList<Node> open = new LinkedList<Node>();
                LinkedList<Node> closed = new LinkedList<Node>();
                open.add(nodes[srow][scol]);
                while (!open.isEmpty()) {
                    System.out.println("Current thread : "+ Thread.currentThread().getName() + " running ...");
                    System.out.println("In open");
                    Node N = open.pollFirst();
                    if (goalTest(N)) {
                        System.out.println("Goal found");
                        LinkedList<Node> list = reconstructPath(N);
                        // set the path and return
                        int[] rowarray = new int[list.size()];
                        int[] colarray = new int[list.size()];
                        Iterator itr = list.iterator();
                        int index = 0;
                        while (itr.hasNext()) {
                            Node node = (Node) itr.next();
                            rowarray[index] = node.getNrow();
                            colarray[index] = node.getNcol();
                            index++;
                        }
                        comp.setPath(rowarray, colarray);
                        System.out.println("Path set");
                        return;
                    }

                    LinkedList<Node> neighbours = moveGen4neighbour(N);
                    open.remove(N);
                    closed.add(N);
                    neighbours.removeAll(closed);
                    neighbours.removeAll(open);
                    Iterator itr = neighbours.iterator();
                    while (itr.hasNext()) {
                        Node child = (Node) itr.next();
                        child.setParent(N);
                    }
                    open.addAll(neighbours);
                    System.out.println("neighbours added");

                    comp.removeFromOpen(N.getNrow(), N.getNcol());
                    itr = neighbours.iterator();
                    while (itr.hasNext()) {
                        Node node = (Node) itr.next();
                        comp.addToOpen(node.getNrow(), node.getNcol());
                    }
                    // open list of the component 'comp' is updated
                    // updateing the closed list
                    comp.addToClose(N.getNrow(), N.getNcol());
                    // Now the closed list is also updated
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        System.out.println("Exception caught :" + e.getMessage());
                    }
                }
                System.out.println("returning from run");
            }

            private LinkedList<Node> moveGen8neighbour(Node N) {
                LinkedList<Node> set = new LinkedList<Node>();
                int nrow, ncol; // stands for newRow , newColumn
                int Nrow, Ncol;
                Nrow = N.getNrow();
                Ncol = N.getNcol();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        nrow = Nrow - 1 + i;
                        ncol = Ncol - 1 + j;
                        if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                            set.add(nodes[nrow][ncol]);

                    }
                }
                // end of loop
                return set;
            }

            private LinkedList<Node> moveGen4neighbour(Node N) {
                LinkedList<Node> set = new LinkedList<Node>();
                int nrow, ncol; // stands for newRow , newColumn
                int Nrow, Ncol;
                Nrow = N.getNrow();
                Ncol = N.getNcol();
                nrow = Nrow - 1;
                ncol = Ncol;
                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                    set.add(nodes[nrow][ncol]);
                nrow = Nrow + 1;
                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                    set.add(nodes[nrow][ncol]);
                nrow = Nrow;
                ncol = Ncol - 1;
                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                    set.add(nodes[nrow][ncol]);
                ncol = Ncol + 1;
                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                    set.add(nodes[nrow][ncol]);

                // end of loop
                return set;
            }

            private boolean goalTest(Node n) {
                if (n.getNrow() == drow && n.getNcol() == dcol)
                    return true;
                return false;
            }

            public LinkedList<Node> reconstructPath(Node N) {
                LinkedList<Node> list = new LinkedList<Node>();
                while (N != null) {
                    list.add(N);
                    N = N.parent;
                }

                return list;
            }
        };
        Thread thread = new Thread(runn);
        // This can't be the daemon thread since it output have to be seen visually as expected
        thread.start();
    }


    public static void runDFS(MapComp comp) {
        int rows, cols;
        rows = comp.getRows();
        cols = comp.getCols();
        Node[][] nodes = new Node[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                nodes[i][j] = new Node(i, j);
        }
        // Nodes created and initialized

        int srow, scol, drow, dcol; // stands for sourceRow, sourceColumn , destinationRow, destinationColumn
        srow = comp.getSrcRow();
        scol = comp.getSrcCol();
        drow = comp.getDstRow();
        dcol = comp.getDstCol();
        NodeComparator comparator = new NodeComparator(drow, dcol);

        Runnable runn = new Runnable() {
            @Override
            public void run() {
                Stack<Node> open = new Stack<Node>();
                Stack<Node> closed = new Stack<Node>();
                open.add(nodes[srow][scol]);
                while (!open.isEmpty()) {
                    System.out.println("Current thread : "+ Thread.currentThread().getName() + " running ...");
                    Node N = open.pop();
                    if (goalTest(N)) {
                        LinkedList<Node> list = reconstructPath(N);
                        // set the path and return
                        int[] rowarray = new int[list.size()];
                        int[] colarray = new int[list.size()];
                        Iterator itr = list.iterator();
                        int index = 0;
                        while (itr.hasNext()) {
                            Node node = (Node) itr.next();
                            rowarray[index] = node.getNrow();
                            colarray[index] = node.getNcol();
                            index++;
                        }
                        comp.setPath(rowarray, colarray);
                        return;
                    }
                    Stack<Node> neighbours = moveGen4neighbour(N);
                    open.remove(N);
                    closed.add(N);
                    showCells(neighbours, "Neighbours");
                    neighbours.removeAll(closed);
                    neighbours.removeAll(open);
                    showCells(neighbours, "Neighbours");
                    Iterator itr = neighbours.iterator();
                    while (itr.hasNext()) {
                        Node child = (Node) itr.next();
                        child.setParent(N);
                    }
                    open.addAll(neighbours);

                    comp.removeFromOpen(N.getNrow(), N.getNcol());
                    itr = neighbours.iterator();
                    while (itr.hasNext()) {
                        Node node = (Node) itr.next();
                        comp.addToOpen(node.getNrow(), node.getNcol());
                    }
                    // open list of the component 'comp' is updated
                    // updateing the closed list
                    comp.setCurrent(new Point(N.getNrow(), N.getNcol()));
                    comp.addToClose(N.getNrow(), N.getNcol());
                    // Now the closed list is also updated
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        System.out.println("Exception caught :" + e.getMessage());
                    }
                    i = i + 1;
                    System.out.println("printing  : " + i);
                }
                System.out.println("Done");

            }

            private void showCells(Stack<Node> list, String name) {
                Iterator itr = list.iterator();
                System.out.print(name + " : ");
                while (itr.hasNext()) {
                    System.out.print(((Node) itr.next()).toString() + ",");
                }
                System.out.println();
            }

            public int i = 0;

            private Stack<Node> moveGen8neighbour(Node N) {
                Stack<Node> set = new Stack<Node>();
                int nrow, ncol; // stands for newRow , newColumn
                int Nrow, Ncol;
                Nrow = N.getNrow();
                Ncol = N.getNcol();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        nrow = Nrow - 1 + i;
                        ncol = Ncol - 1 + j;
                        if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                            set.add(nodes[nrow][ncol]);

                    }
                }
                // end of loop
                return set;
            }

            private Stack<Node> moveGen4neighbour(Node N) {
                Stack<Node> set = new Stack<Node>();
                int nrow, ncol; // stands for newRow , newColumn
                int Nrow, Ncol;
                Nrow = N.getNrow();
                Ncol = N.getNcol();
                nrow = Nrow;
                ncol = Ncol - 1;
                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                    set.add(nodes[nrow][ncol]);

                nrow = Nrow - 1;
                ncol = Ncol;
                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                    set.add(nodes[nrow][ncol]);
                nrow = Nrow;
                ncol = Ncol + 1;

                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                    set.add(nodes[nrow][ncol]);
                nrow = Nrow + 1;
                ncol = Ncol;
                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !comp.isObstacleHaveCell(nrow, ncol))
                    set.add(nodes[nrow][ncol]);

                // end of loop
                return set;
            }

            private boolean goalTest(Node n) {
                if (n.getNrow() == drow && n.getNcol() == dcol)
                    return true;
                return false;
            }

            public LinkedList<Node> reconstructPath(Node N) {
                LinkedList<Node> list = new LinkedList<Node>();
                while (N != null) {
                    list.add(N);
                    N = N.parent;
                }
                return list;
            }
        };
        Thread thread = new Thread(runn);
        // This can't be the daemon thread since it output have to be seen visually as expected
        thread.start();
    }
}
