import java.awt.*;
import java.util.*;

public class DepthFirstSearchRunnable implements AlgoRunnerRunnable {
    int delay;
    boolean exit;
    MapComp mapComp;

    @Override
    public boolean takeExit() {
        return exit;
    }

    @Override
    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public void setDelay(int delay) {
        this.delay = delay;
    }

    DepthFirstSearchRunnable(MapComp mapComp) {
        this.mapComp = mapComp;
        this.delay = mapComp.getDelay();
    }

    @Override
    public void run() {
        int rows, cols;
        rows = mapComp.getRows();
        cols = mapComp.getCols();
        Node[][] nodes = new Node[rows][cols];
        int[][] visitedStatus = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                nodes[i][j] = new Node(i, j);
        }
        // Nodes created and initialized

        int sourceCellRow, sourceCellColumn, destinationCellRow, destinationCellColumn; // stands for sourceRow, sourceColumn , destinationRow, destinationColumn
        sourceCellRow = mapComp.getSrcRow();
        sourceCellColumn = mapComp.getSrcCol();
        destinationCellRow = mapComp.getDstRow();
        destinationCellColumn = mapComp.getDstCol();
        Stack<Node> openStack = new Stack<>();
        HashSet<Node> closedSet = new HashSet<>();
        openStack.add(nodes[sourceCellRow][sourceCellColumn]);
        visitedStatus[sourceCellRow][sourceCellColumn] = 1; // 1- to be processed, 2 - processed , 0- not even considered for processing
        while (!openStack.isEmpty() && !exit) {
            System.out.println("Current thread : " + Thread.currentThread().getName() + " running ...");
            Node currentNode = openStack.pop();
            if (goalTest(currentNode, destinationCellRow, destinationCellColumn)) {
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
                return;
            }
            HashSet<Node> neighbours = moveGen4neighbour(currentNode, nodes, rows, cols);
            closedSet.add(currentNode);
            visitedStatus[currentNode.row][currentNode.col] = 2;
            showCells(neighbours, "Neighbours");
            Iterator<Node> itr = neighbours.iterator();
            while (itr.hasNext()) {
                Node node = itr.next();
                if (visitedStatus[node.row][node.col] == 1 || visitedStatus[node.row][node.col] == 2) {
                    itr.remove();
                } else {
                    node.setParent(currentNode);
                    visitedStatus[node.row][node.col]= 1;
                    openStack.add(node);
                }
            }
            //showCells(neighbours, "Neighbours");
            mapComp.removeFromOpen(currentNode.getRow(), currentNode.getCol());
            itr = neighbours.iterator();
            while (itr.hasNext()) {
                Node node = (Node) itr.next();
                mapComp.addToOpen(node.getRow(), node.getCol());
            }
            // open list of the component 'comp' is updated
            // updateing the closed list
            mapComp.setCurrent(new Point(currentNode.getRow(), currentNode.getCol()));
            mapComp.addToClose(currentNode.getRow(), currentNode.getCol());
            // Now the closed list is also updated
            try {
                Thread.sleep(getDelay());
            } catch (Exception e) {
                System.out.println("Exception caught :" + e.getMessage());
            }
        }
        System.out.println("Done");

    }

    private void showCells(HashSet<Node> list, String name) {
        Iterator itr = list.iterator();
        System.out.print(name + " : ");
        while (itr.hasNext()) {
            System.out.print(((Node) itr.next()).toString() + ",");
        }
        System.out.println();
    }


//            private Stack<AlgoDemo.Node> moveGen8neighbour(AlgoDemo.Node N) {
//                Stack<AlgoDemo.Node> set = new Stack<AlgoDemo.Node>();
//                int nrow, ncol; // stands for newRow , newColumn
//                int Nrow, Ncol;
//                Nrow = N.getNrow();
//                Ncol = N.getNcol();
//                for (int i = 0; i < 3; i++) {
//                    for (int j = 0; j < 3; j++) {
//                        nrow = Nrow - 1 + i;
//                        ncol = Ncol - 1 + j;
//                        if (nrow >= 0 && nrow < rows && ncol >= 0 && ncol < cols && !mapComp.isObstacleHaveCell(nrow, ncol))
//                            set.add(nodes[nrow][ncol]);
//
//                    }
//                }
//                // end of loop
//                return set;
//            }

    private HashSet<Node> moveGen4neighbour(Node currentNode, Node[][] nodes, int rows, int cols) {
        LinkedHashSet<Node> set = new LinkedHashSet<>();
        int neighbourRow, neighbourColumn; // stands for newRow , newColumn
        int currentCellRow, currentCellColumn;
        currentCellRow = currentNode.getRow();
        currentCellColumn = currentNode.getCol();
        neighbourRow = currentCellRow;
        neighbourColumn = currentCellColumn - 1;
        // west
        if (neighbourColumn >= 0 && neighbourColumn < cols && !mapComp.isObstacleHaveCell(neighbourRow, neighbourColumn)) {
            set.add(nodes[neighbourRow][neighbourColumn]);
        }

        neighbourRow = currentCellRow - 1;
        neighbourColumn = currentCellColumn;
        // north
        if (neighbourRow >= 0 && neighbourRow < rows && !mapComp.isObstacleHaveCell(neighbourRow, neighbourColumn)) {
            set.add(nodes[neighbourRow][neighbourColumn]);
        }
        neighbourRow = currentCellRow;
        neighbourColumn = currentCellColumn + 1;

        //east
        if (neighbourColumn >= 0 && neighbourColumn < cols && !mapComp.isObstacleHaveCell(neighbourRow, neighbourColumn)) {
            set.add(nodes[neighbourRow][neighbourColumn]);
        }
        neighbourRow = currentCellRow + 1;
        neighbourColumn = currentCellColumn;

        //south
        if (neighbourRow >= 0 && neighbourRow < rows && !mapComp.isObstacleHaveCell(neighbourRow, neighbourColumn)) {
            set.add(nodes[neighbourRow][neighbourColumn]);
        }
        return set;
    }

    private boolean goalTest(Node currentNode, int destinationRow, int destinationColumn) {
        if (currentNode.getRow() == destinationRow && currentNode.getCol() == destinationColumn)
            return true;
        return false;
    }

    public LinkedList<Node> reconstructPath(Node node) {
        LinkedList<Node> list = new LinkedList<>();
        while (node != null) {
            list.add(node);
            node = node.parent;
        }
        return list;
    }

}
