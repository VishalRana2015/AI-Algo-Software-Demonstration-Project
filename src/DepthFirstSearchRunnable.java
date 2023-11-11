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
        boolean[][] visited = new boolean[rows][cols];
        // Nodes created and initialized

        int sourceCellRow, sourceCellColumn, destinationCellRow, destinationCellColumn; // stands for sourceRow, sourceColumn , destinationRow, destinationColumn
        sourceCellRow = mapComp.getSrcRow();
        sourceCellColumn = mapComp.getSrcCol();
        destinationCellRow = mapComp.getDstRow();
        destinationCellColumn = mapComp.getDstCol();
        Stack<Node> openStack = new Stack<>();
        openStack.add(new Node(sourceCellRow, sourceCellColumn));
         // 1- to be processed, 2 - processed , 0- not even considered for processing
        while (!openStack.isEmpty() && !exit) {
            Node currentNode = openStack.pop();
            if ( visited[currentNode.getRow()][currentNode.getCol()]){
                continue;
            }
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
            HashSet<Node> neighbours = moveGen4neighbour(currentNode, rows, cols);
            visited[currentNode.getRow()][currentNode.getCol()] = true;
            Iterator<Node> itr = neighbours.iterator();
            mapComp.removeFromOpen(currentNode.getRow(), currentNode.getCol());
            while (itr.hasNext()) {
                Node node = itr.next();
                node.setParent(currentNode);
                openStack.add(node);
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

    private HashSet<Node> moveGen4neighbour(Node currentNode, int rows, int cols) {
        LinkedHashSet<Node> set = new LinkedHashSet<>();
        int neighbourRow, neighbourColumn; // stands for newRow , newColumn
        int currentCellRow, currentCellColumn;
        currentCellRow = currentNode.getRow();
        currentCellColumn = currentNode.getCol();
        neighbourRow = currentCellRow;
        neighbourColumn = currentCellColumn - 1;
        // west
        if (neighbourColumn >= 0 && neighbourColumn < cols && !mapComp.isObstacleHaveCell(neighbourRow, neighbourColumn)) {
            Node nn = new Node(neighbourRow, neighbourColumn);
            nn.setDistance(currentNode.getDistance() + 1);
            set.add(nn);
        }

        neighbourRow = currentCellRow - 1;
        neighbourColumn = currentCellColumn;
        // north
        if (neighbourRow >= 0 && neighbourRow < rows && !mapComp.isObstacleHaveCell(neighbourRow, neighbourColumn)) {
            Node nn = new Node(neighbourRow, neighbourColumn);
            nn.setDistance(currentNode.getDistance() + 1);
            set.add(nn);
        }
        neighbourRow = currentCellRow;
        neighbourColumn = currentCellColumn + 1;

        //east
        if (neighbourColumn >= 0 && neighbourColumn < cols && !mapComp.isObstacleHaveCell(neighbourRow, neighbourColumn)) {
            Node nn = new Node(neighbourRow, neighbourColumn);
            nn.setDistance(currentNode.getDistance() + 1);
            set.add(nn);
        }
        neighbourRow = currentCellRow + 1;
        neighbourColumn = currentCellColumn;

        //south
        if (neighbourRow >= 0 && neighbourRow < rows && !mapComp.isObstacleHaveCell(neighbourRow, neighbourColumn)) {
            Node nn = new Node(neighbourRow, neighbourColumn);
            nn.setDistance(currentNode.getDistance() + 1);
            set.add(nn);
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
            node = node.getParent();
        }
        return list;
    }

}
