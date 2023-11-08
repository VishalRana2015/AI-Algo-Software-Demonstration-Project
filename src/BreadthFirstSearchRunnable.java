import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class BreadthFirstSearchRunnable implements AlgoRunnerRunnable {
    boolean exit;
    int delay;
    MapComp mapComp;

    @Override
    public boolean takeExit() {
        return this.exit;
    }

    @Override
    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    public int getDelay() {
        return this.delay;
    }

    @Override
    public void setDelay(int delay) {
        this.delay = delay;
    }

    BreadthFirstSearchRunnable(MapComp mapComp) {
        this.mapComp = mapComp;
        this.delay = 100;
        this.exit = false;
    }

    @Override
    public void run() {
        mapComp.setStatus("Running Breadth First Search Algorithm...");
        System.out.println("hashCode : " + mapComp.hashCode());
        // clearing data from the component if any
        mapComp.clear();
        int rows, cols;
        rows = mapComp.getRows();
        cols = mapComp.getCols();
        Node[][] nodes = new Node[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                nodes[i][j] = new Node(i, j);
        }
        // Nodes created and initialized
        System.out.println("Nodes created");
        int startingCellRow, startingCellColumn, destinationCellRow, destinationCellColumn; // stands for sourceRow, sourceColumn , destinationRow, destinationColumn
        startingCellRow = mapComp.getSrcRow();
        startingCellColumn = mapComp.getSrcCol();
        destinationCellRow = mapComp.getDstRow();
        destinationCellColumn = mapComp.getDstCol();
        LinkedHashSet<Node> openList = new LinkedHashSet<>();
        LinkedHashSet<Node> closedList = new LinkedHashSet<>();
        openList.add(nodes[startingCellRow][startingCellColumn]);
        while (!openList.isEmpty() && !exit) {
            System.out.println("Current thread : " + Thread.currentThread().getName() + " running ...");
            System.out.println("In open");
            Node currentNode = openList.iterator().next();
            openList.remove(currentNode);
            if (goalTest(currentNode, destinationCellRow, destinationCellColumn)) {
                System.out.println("Goal found");
                LinkedList<Node> list = reconstructPath(currentNode);
                // set the path and return
                int[] rowarray = new int[list.size()];
                int[] colarray = new int[list.size()];
                Iterator itr = list.iterator();
                int index = 0;
                while (itr.hasNext()) {
                    Node node = (Node) itr.next();
                    rowarray[index] = node.getRow();
                    colarray[index] = node.getCol();
                    index++;
                }
                mapComp.setPath(rowarray, colarray);
                System.out.println("Path set");
                return;
            }

            HashSet<Node> neighbourSet = moveGen4neighbour(currentNode, nodes, rows, cols);
            openList.remove(currentNode);
            closedList.add(currentNode);
            Iterator<Node> itr = neighbourSet.iterator();
            while (itr.hasNext()) {
                Node node = itr.next();
                if (openList.contains(node) || closedList.contains(node)) {
                    itr.remove();
                } else {
                    node.setParent(currentNode);
                    mapComp.addToOpen(node.getRow(), node.getCol());
                }
            }
            openList.addAll(neighbourSet);
            System.out.println("neighbours added");
            mapComp.removeFromOpen(currentNode.getRow(), currentNode.getCol());
            mapComp.addToClose(currentNode.getRow(), currentNode.getCol());
            try {
                Thread.sleep(getDelay());
            } catch (Exception e) {
                System.out.println("Exception caught :" + e.getMessage());
            }
        }
        System.out.println("returning from run");
    }

//    private LinkedList<Node> moveGen8neighbour(Node N) {
//        LinkedList<Node> set = new LinkedList<Node>();
//        int nrow, ncol; // stands for newRow , newColumn
//        int Nrow, Ncol;
//        Nrow = N.getRow();
//        Ncol = N.getCol();
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                nrow = Nrow - 1 + i;
//                ncol = Ncol - 1 + j;
//                if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !mapComp.isObstacleHaveCell(nrow, ncol))
//                    set.add(nodes[nrow][ncol]);
//
//            }
//        }
//        // end of loop
//        return set;
//    }

    private HashSet<Node> moveGen4neighbour(Node currentNode, Node[][] nodes, int rows, int cols) {
        HashSet<Node> set = new HashSet<>();
        int nrow, ncol; // stands for nextCellRow and nextCellColumn
        int currentCellRow, currentCellColumn; //
        currentCellRow = currentNode.getRow();
        currentCellColumn = currentNode.getCol();
        nrow = currentCellRow - 1;
        ncol = currentCellColumn;
        if (nrow >= 0 && nrow < rows && !mapComp.isObstacleHaveCell(nrow, ncol))
            set.add(nodes[nrow][ncol]);
        nrow = currentCellRow + 1;
        if (nrow >= 0 && nrow < rows && !mapComp.isObstacleHaveCell(nrow, ncol))
            set.add(nodes[nrow][ncol]);
        nrow = currentCellRow;
        ncol = currentCellColumn - 1;
        if (ncol >= 0 && ncol < cols && !mapComp.isObstacleHaveCell(nrow, ncol))
            set.add(nodes[nrow][ncol]);
        ncol = currentCellColumn + 1;
        if (ncol >= 0 && ncol < cols && !mapComp.isObstacleHaveCell(nrow, ncol))
            set.add(nodes[nrow][ncol]);
        return set;
    }

    private boolean goalTest(Node currentNode, int destinationRow, int destinationColumn) {
        if (currentNode.getRow() == destinationRow && currentNode.getCol() == destinationColumn)
            return true;
        return false;
    }

    public LinkedList<Node> reconstructPath(Node node) {
        LinkedList<Node> list = new LinkedList<Node>();
        while (node != null) {
            list.add(node);
            node = node.parent;
        }
        return list;
    }
}

