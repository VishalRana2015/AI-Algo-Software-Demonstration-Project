import java.util.*;

public class BestFirstSearchRunnable extends AlgoRunnerRunnableImpl {
    BestFirstSearchRunnable(MapComp comp) {
        super(comp);
    }

    @Override
    public void run() {
        String prefix = "Best First Search: ";
        updateStatus(prefix + "Starting ...");
        // Nodes created and initialized
        boolean[][] visited = new boolean[mapComp.getRows()][mapComp.getCols()];
        int sourceRow, sourceColumn, destinationRow, destinationColumn;
        sourceRow = mapComp.getSrcRow();
        sourceColumn = mapComp.getSrcCol();
        destinationRow = mapComp.getDstRow();
        destinationColumn = mapComp.getDstCol();
        Comparator<Node> nodeManhattanComparator = (Node n1, Node n2) -> {
            // using Manhattan distance value
            int d1 = Math.abs(n1.getRow() - destinationRow) + Math.abs(n1.getCol() - destinationColumn);
            int d2 = Math.abs(n2.getRow() - destinationRow) + Math.abs(n2.getCol() - destinationColumn);
            return d1 - d2;
        };

        // clear any open, closed or pathSet
        mapComp.clear();
        PriorityQueue<Node> openQueue = new PriorityQueue<>(nodeManhattanComparator);
        openQueue.add(new Node(sourceRow, sourceColumn));
        visited[sourceRow][sourceColumn] = false; // 1 stands for to be processed, 2 stands for has been processed, 0 stands for not considered to be processed yet.
        updateStatus(prefix + "In Progress ...");
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
                updateStatus(prefix + "Destination found, path length: " + rowarray.length);
                return;
            }
            HashSet<Node> neighbourSet = getNeighbours(currentNode);  // It never returns an obstacle as a neighbour cell.
            visited[currentNode.getRow()][currentNode.getCol()] = true;
            // remove cell of openList
            mapComp.removeFromOpen(currentNode.getRow(), currentNode.getCol());
            // add to close
            mapComp.addToClose(currentNode.getRow(), currentNode.getCol());

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
        updateStatus(prefix + "Path not found");
    }
}
