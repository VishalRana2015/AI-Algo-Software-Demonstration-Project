import java.awt.*;
import java.util.*;

public class DepthFirstSearchRunnable extends AlgoRunnerRunnableImpl {
    DepthFirstSearchRunnable(MapComp mapComp) {
        super(mapComp);
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
        Stack<Node> openStack = new Stack<>();
        openStack.add(new Node(sourceCellRow, sourceCellColumn));
        // 1- to be processed, 2 - processed , 0- not even considered for processing
        while (!openStack.isEmpty() && !exit) {
            Node currentNode = openStack.pop();
            if (visited[currentNode.getRow()][currentNode.getCol()]) {
                continue;
            }
            if (goalTest(currentNode)) {
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
            HashSet<Node> neighbours = moveGen4Neighbour(currentNode);
            visited[currentNode.getRow()][currentNode.getCol()] = true;
            // remove current node from open list
            mapComp.removeFromOpen(currentNode.getRow(), currentNode.getCol());
            // add it to closed list
            mapComp.addToClose(currentNode.getRow(), currentNode.getCol());

            Iterator<Node> itr = neighbours.iterator();
            while (itr.hasNext()) {
                Node node = itr.next();
                if ( visited[node.getRow()][node.getCol()]){
                    itr.remove();
                }else {
                    openStack.add(node);
                    mapComp.addToOpen(node.getRow(), node.getCol());
                }
            }
            try {
                Thread.sleep(mapComp.getDelay());
            } catch (Exception e) {
                System.out.println("Exception caught :" + e.getMessage());
            }
        }

    }

    @Override
    public HashSet<Node> moveGen8Neighbour(Node currentNode) {
        LinkedHashSet<Node> set = new LinkedHashSet<>();
        int nrow, ncol; // stands for newRow , newColumn
        int Nrow, Ncol;
        Nrow = currentNode.getRow();
        Ncol = currentNode.getCol();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                nrow = Nrow - 1 + i;
                ncol = Ncol - 1 + j;
                if (nrow >= 0 && nrow < mapComp.getRows() && ncol >= 0 && ncol < mapComp.getCols() && !mapComp.isObstacleHaveCell(nrow, ncol))
                    set.add(new Node(nrow, ncol));

            }
        }
        // end of loop
        return set;
    }

    @Override
    public HashSet<Node> moveGen4Neighbour(Node currentNode) {
        LinkedHashSet<Node> set = new LinkedHashSet<>();
        int neighbourRow, neighbourColumn; // stands for newRow , newColumn
        int currentCellRow, currentCellColumn;
        currentCellRow = currentNode.getRow();
        currentCellColumn = currentNode.getCol();
        neighbourRow = currentCellRow;
        neighbourColumn = currentCellColumn - 1;
        // west
        if (neighbourColumn >= 0 && neighbourColumn < mapComp.getCols() && !mapComp.isObstacleHaveCell(neighbourRow, neighbourColumn)) {
            Node nn = new Node(neighbourRow, neighbourColumn);
            nn.setDistance(currentNode.getDistance() + 1);
            set.add(nn);
        }

        neighbourRow = currentCellRow - 1;
        neighbourColumn = currentCellColumn;
        // north
        if (neighbourRow >= 0 && neighbourRow < mapComp.getRows() && !mapComp.isObstacleHaveCell(neighbourRow, neighbourColumn)) {
            Node nn = new Node(neighbourRow, neighbourColumn);
            nn.setDistance(currentNode.getDistance() + 1);
            set.add(nn);
        }
        neighbourRow = currentCellRow;
        neighbourColumn = currentCellColumn + 1;

        //east
        if (neighbourColumn >= 0 && neighbourColumn < mapComp.getCols() && !mapComp.isObstacleHaveCell(neighbourRow, neighbourColumn)) {
            Node nn = new Node(neighbourRow, neighbourColumn);
            nn.setDistance(currentNode.getDistance() + 1);
            set.add(nn);
        }
        neighbourRow = currentCellRow + 1;
        neighbourColumn = currentCellColumn;

        //south
        if (neighbourRow >= 0 && neighbourRow < mapComp.getRows() && !mapComp.isObstacleHaveCell(neighbourRow, neighbourColumn)) {
            Node nn = new Node(neighbourRow, neighbourColumn);
            nn.setDistance(currentNode.getDistance() + 1);
            set.add(nn);
        }
        return set;
    }
}
