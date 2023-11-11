import java.sql.Array;
import java.util.*;

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
        this.delay = mapComp.getDelay();
        this.exit = false;
    }

    @Override
    public void run() {
        // clearing data from the component if any
        mapComp.clear();
        int rows, cols;
        rows = mapComp.getRows();
        cols = mapComp.getCols();
        boolean[][] visited = new boolean[rows][cols];

        // Nodes created and initialized
        int startingCellRow, startingCellColumn, destinationCellRow, destinationCellColumn; // stands for sourceRow, sourceColumn , destinationRow, destinationColumn
        startingCellRow = mapComp.getSrcRow();
        startingCellColumn = mapComp.getSrcCol();
        destinationCellRow = mapComp.getDstRow();
        destinationCellColumn = mapComp.getDstCol();
        LinkedList<Node> openList = new LinkedList<>();
        openList.add(new Node(startingCellRow, startingCellColumn));
        ArrayList<Node> nextLevelNodeList = new ArrayList<>();
        while (!exit) {
            if (openList.isEmpty()) {
                if (nextLevelNodeList.isEmpty()) {
                    exit = true;
                    continue;
                }
                // split this list in two parts
                ArrayList<Node> firstPart = new ArrayList<>(), secondPart = new ArrayList<>();
                Iterator<Node> itr = nextLevelNodeList.iterator();
                while (itr.hasNext()) {
                    Node node = itr.next();
                    if (visited[node.getRow()][node.getCol()]) {
                        itr.remove();
                    } else {
                        if (node.getCol() >= startingCellColumn) {
                            firstPart.add(node);
                        } else {
                            secondPart.add(node);
                        }
                    }
                }
                // sort first in descending order of column values.
                firstPart.sort((Node n1, Node n2) -> n1.getRow() - n2.getRow());
                // sort second part in increasing order of column values
                secondPart.sort((Node n1, Node n2) -> n2.getRow() - n1.getRow());
                openList.addAll(firstPart); // it automatically makes add the elements in the same order as they are in the arrayList.
                openList.addAll(secondPart);
                nextLevelNodeList = new ArrayList<>();
            }

            Node currentNode = openList.remove();
            if (visited[currentNode.getRow()][currentNode.getCol()]) {
                continue;
            }
            visited[currentNode.getRow()][currentNode.getCol()] = true;
            if (goalTest(currentNode, destinationCellRow, destinationCellColumn)) {
                LinkedList<Node> list = reconstructPath(currentNode);
                // set the path and return
                int[] rowarray = new int[list.size()];
                int[] colarray = new int[list.size()];
                Iterator<Node> itr = list.iterator();
                int index = 0;
                while (itr.hasNext()) {
                    Node node = itr.next();
                    rowarray[index] = node.getRow();
                    colarray[index] = node.getCol();
                    index++;
                }
                mapComp.setPath(rowarray, colarray);
                return;
            }
            LinkedList<Node> neighbourList = moveGen4neighbour(currentNode, rows, cols);
            Iterator<Node> itr = neighbourList.iterator();
            while (itr.hasNext()) {
                Node node = itr.next();
                if (visited[node.getRow()][node.getCol()]) {
                    itr.remove();
                } else {
                    node.setParent(currentNode);
                    mapComp.addToOpen(node.getRow(), node.getCol());
                }
            }
            nextLevelNodeList.addAll(neighbourList);
            mapComp.removeFromOpen(currentNode.getRow(), currentNode.getCol());
            mapComp.addToClose(currentNode.getRow(), currentNode.getCol());
            try {
                Thread.sleep(getDelay());
            } catch (Exception e) {
                System.out.println("Exception caught :" + e.getMessage());
            }
        }
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

    private LinkedList<Node> moveGen4neighbour(Node currentNode, int rows, int cols) {
        LinkedList<Node> neighbourList = new LinkedList<>();
        int nrow, ncol; // stands for nextCellRow and nextCellColumn
        int currentCellRow, currentCellColumn; //
        currentCellRow = currentNode.getRow();
        currentCellColumn = currentNode.getCol();
        nrow = currentCellRow - 1;
        ncol = currentCellColumn;
        // north
        if (nrow >= 0 && nrow < rows && !mapComp.isObstacleHaveCell(nrow, ncol)) {
            Node nn = new Node(nrow, ncol);
            nn.setDistance(currentNode.getDistance() + 1);
            neighbourList.add(nn);
        }
        // east
        nrow = currentCellRow;
        ncol = currentCellColumn + 1;
        if (ncol >= 0 && ncol < cols && !mapComp.isObstacleHaveCell(nrow, ncol)) {
            Node nn = new Node(nrow, ncol);
            nn.setDistance(currentNode.getDistance() + 1);
            neighbourList.add(nn);
        }
        // south
        ncol = currentCellColumn;
        nrow = currentCellRow + 1;
        if (nrow >= 0 && nrow < rows && !mapComp.isObstacleHaveCell(nrow, ncol)) {
            Node nn = new Node(nrow, ncol);
            nn.setDistance(currentNode.getDistance() + 1);
            neighbourList.add(nn);
        }
        // west
        nrow = currentCellRow;
        ncol = currentCellColumn - 1;
        if (ncol >= 0 && ncol < cols && !mapComp.isObstacleHaveCell(nrow, ncol)) {
            Node nn = new Node(nrow, ncol);
            nn.setDistance(currentNode.getDistance() + 1);
            neighbourList.add(nn);
        }
        return neighbourList;
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
            node = node.getParent();
        }
        return list;
    }
}

