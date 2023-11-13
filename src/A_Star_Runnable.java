import java.util.*;

public class A_Star_Runnable extends AlgoRunnerRunnableImpl {

    A_Star_Runnable(MapComp mapComp) {
        super(mapComp);
    }

    @Override
    public void run() {
        mapComp.setStatus("Running BSF...");
        System.out.println("hashCode : " + mapComp.hashCode());
        int rows, cols;
        rows = mapComp.getRows();
        cols = mapComp.getCols();
        // Nodes created and initialized
        boolean[][] visited = new boolean[rows][cols];
        int sourceRow, sourceColumn, destinationRow, destinationColumn;
        sourceRow = mapComp.getSrcRow();
        sourceColumn = mapComp.getSrcCol();
        destinationRow = mapComp.getDstRow();
        destinationColumn = mapComp.getDstCol();
        Comparator<Node> nodeManhattanComparator = (Node n1, Node n2) -> {
            // using Manhattan distance value
            int d1 = Math.abs(n1.getRow() - destinationRow) + Math.abs(n1.getCol() - destinationColumn);
            int d2 = Math.abs(n2.getRow() - destinationRow) + Math.abs(n2.getCol() - destinationColumn);
            int value1 = n1.getDistance() + d1;
            int value2 = d2 + n2.getDistance();
            if (value1 == value2) {
                // return cell which is nearest to destination
                return n1.getDistance() - n2.getDistance();
            }
            return value1 - value2;
        };

        mapComp.clear();
        PriorityQueue<Node> openQueue = new PriorityQueue<>(nodeManhattanComparator);
        Node startingNode = new Node(sourceRow, sourceColumn);
        openQueue.add(startingNode);
        startingNode.setDistance(0);
        visited[sourceRow][sourceColumn] = false; // 1 stands for to be processed, 2 stands for has been processed, 0 stands for not considered to be processed yet.
        while (!openQueue.isEmpty() && !exit) {
            Node currentNode = openQueue.remove();
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

            mapComp.removeFromOpen(currentNode.getRow(), currentNode.getCol());
            // open list of the component 'comp' is updated
            // the closed list
            mapComp.addToClose(currentNode.getRow(), currentNode.getCol());
            // Now the closed list is also updated
            visited[currentNode.getRow()][currentNode.getCol()] = true;
            HashSet<Node> neighbourSet = moveGen4Neighbour(currentNode);
            Iterator<Node> itr = neighbourSet.iterator();
            while (itr.hasNext()) {
                Node node = itr.next();
                if (visited[node.getRow()][node.getCol()]) {
                    itr.remove();
                } else {
                    mapComp.addToOpen(node.getRow(), node.getCol());
                }
            }
            openQueue.addAll(neighbourSet);

            try {
                Thread.sleep(mapComp.getDelay());
            } catch (Exception e) {
                System.out.println("Exception caught :" + e.getMessage());
            }
        }
    }
}
