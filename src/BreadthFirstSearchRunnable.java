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
        int level = 0;
        while (!exit) {
            if (openList.isEmpty()) {
                level ++;
                System.out.println("Current level + : "+ level);
                // remove all nodes whose distance value is greater than the current level
                Iterator<Node> itr =nextLevelNodeList.iterator();
                while (itr.hasNext()){
                    Node node = itr.next();
                    System.out.print(node.getDistance() + ", " );
                    if ( node.getDistance() > level){
                        System.out.println("removing ");
                        itr.remove();
                    }
                }
                System.out.println();
                if (nextLevelNodeList.isEmpty()) {
                    exit = true;
                    continue;
                }
                // split this list in two parts
                ArrayList<Node> firstPart = new ArrayList<>(), secondPart = new ArrayList<>();
                itr = nextLevelNodeList.iterator();
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
                firstPart.sort((Node n1, Node n2) -> {
                    if ( n1.getRow() < n2.getRow()){
                        return -1;
                    }
                    else if ( n1.getRow() == n2.getRow()){
                        if ( n1.getRow() < startingCellRow) {
                            return n1.getCol() - n2.getCol();
                        }
                        else{
                            return n2.getCol() - n1.getCol();
                        }
                    }
                    return 1;
                });
                // sort second part in increasing order of column values
                secondPart.sort((Node n1, Node n2) -> {
                    if ( n2.getRow() < n1.getRow()){
                        return -1;
                    }
                    else if ( n2.getRow() == n1.getRow()){
                        if ( n2.getRow() < startingCellRow){
                            return n1.getCol()- n2.getCol();
                        }
                        else{
                            return n2.getCol() - n1.getCol();
                        }
                    }
                    return 1;
                });
                openList.addAll(firstPart); // it automatically makes add the elements in the same order as they are in the arrayList.
                openList.addAll(secondPart);
                nextLevelNodeList = new ArrayList<>();
            }

            Node currentNode = openList.remove();
            if (visited[currentNode.getRow()][currentNode.getCol()]) {
                continue;
            }
            visited[currentNode.getRow()][currentNode.getCol()] = true;
            mapComp.removeFromOpen(currentNode.getRow(), currentNode.getCol());
            mapComp.addToClose(currentNode.getRow(), currentNode.getCol());
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
            HashSet<Node> neighbourList = getNeighbours(currentNode);
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
            try {
                Thread.sleep(mapComp.getDelay());
            } catch (Exception e) {
                System.out.println("Exception caught :" + e.getMessage());
            }
        }
    }
}

