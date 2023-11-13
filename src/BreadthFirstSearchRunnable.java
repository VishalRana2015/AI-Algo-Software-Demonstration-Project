import java.sql.Array;
import java.util.*;

public class BreadthFirstSearchRunnable extends AlgoRunnerRunnableImpl {

    BreadthFirstSearchRunnable(MapComp mapComp) {
        super(mapComp);
    }

    @Override
    public void run() {
        // clearing data from the component if any
        mapComp.clear();
        boolean[][] visited = new boolean[mapComp.getRows()][mapComp.getCols()];

        // Nodes created and initialized
        int startingCellRow, startingCellColumn, destinationCellRow, destinationCellColumn; // stands for sourceRow, sourceColumn , destinationRow, destinationColumn
        startingCellRow = mapComp.getSrcRow();
        startingCellColumn = mapComp.getSrcCol();
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
            if (goalTest(currentNode)) {
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
            HashSet<Node> neighbourList = moveGen4Neighbour(currentNode);
            Iterator<Node> itr = neighbourList.iterator();
            while (itr.hasNext()) {
                Node node = itr.next();
                if (visited[node.getRow()][node.getCol()]) {
                    itr.remove();
                } else {
                    mapComp.addToOpen(node.getRow(), node.getCol());
                }
            }
            nextLevelNodeList.addAll(neighbourList);
            mapComp.removeFromOpen(currentNode.getRow(), currentNode.getCol());
            mapComp.addToClose(currentNode.getRow(), currentNode.getCol());
            try {
                Thread.sleep(mapComp.getDelay());
            } catch (Exception e) {
                System.out.println("Exception caught :" + e.getMessage());
            }
        }
    }
}

