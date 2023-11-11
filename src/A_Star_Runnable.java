import java.util.*;

public class A_Star_Runnable implements AlgoRunnerRunnable {
    boolean exit;
    int delay;
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
        return this.delay;
    }

    @Override
    public void setDelay(int delay) {
        this.delay = delay;
    }

    A_Star_Runnable(MapComp mapComp){
        this.mapComp = mapComp;
        this.delay = mapComp.getDelay();
        this.exit = false;
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
        int srow, scol, drow, dcol; // stands for sourceRow, sourceColumn , destinationRow, destinationColumn
        srow = mapComp.getSrcRow(); // srow stands for source Cell's row number
        scol = mapComp.getSrcCol(); // scol stands for source cell's col number
        drow = mapComp.getDstRow(); // drow stands for destination row
        dcol = mapComp.getDstCol(); // dcol stands for destination col
        Comparator<Node> nodeManhattanComparator = (Node n1, Node n2) -> {
            // using Manhattan distance value
            int d1 = Math.abs(n1.getRow() - drow) + Math.abs(n1.getCol() - dcol);
            int d2 = Math.abs(n2.getRow() - drow) + Math.abs(n2.getCol() - dcol);
            int value1 = n1.getDistance() + d1;
            int value2 = d2 + n2.getDistance();
            if ( value1 == value2){
                // return cell which is nearest to destination
                return n1.getDistance() - n2.getDistance();
            }
            return value1-value2;
        };

        // clear any open, closed or pathSet
        mapComp.clear();
        PriorityQueue<Node> openQueue =new PriorityQueue<>(nodeManhattanComparator);
        Node startingNode= new Node(srow, scol);
        openQueue.add(startingNode);
        startingNode.setDistance(0);
        visited[srow][scol] = false; // 1 stands for to be processed, 2 stands for has been processed, 0 stands for not considered to be processed yet.
        while (!openQueue.isEmpty() && !exit) {
            Node currentNode = openQueue.remove();
            int totalDistance = currentNode.getDistance() + ( Math.abs(drow-currentNode.getRow()) + Math.abs(dcol-currentNode.getCol()));
            System.out.println(currentNode + ", heuristic distance to destination  : " +  ( Math.abs(drow-currentNode.getRow()) + Math.abs(dcol-currentNode.getCol())) + ", totalDistance : "+ totalDistance);
            if (goalTest(currentNode, drow, dcol)) {
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
            HashSet<Node> neighbourSet = moveGen4neightbour(currentNode, rows, cols);
            openQueue.remove(currentNode);
            if ( visited[currentNode.getRow()][currentNode.getCol()]){
                continue;
            }
            visited[currentNode.getRow()][currentNode.getCol()] = true;
            Iterator<Node> itr = neighbourSet.iterator();
            while ( itr.hasNext()){
                Node node = itr.next();
                if ( visited[node.getRow()][node.getCol()]){
                    itr.remove();
                }
                else{
                    node.setParent(currentNode);
                    node.setDistance(currentNode.getDistance() + 1);
                    mapComp.addToOpen(node.getRow(), node.getCol());
                }
            }
            openQueue.addAll(neighbourSet);
            mapComp.removeFromOpen(currentNode.getRow(), currentNode.getCol());
            // open list of the component 'comp' is updated
            // updateing the closed list
            mapComp.addToClose(currentNode.getRow(), currentNode.getCol());
            // Now the closed list is also updated
            try {
                Thread.sleep(getDelay());
            } catch (Exception e) {
                System.out.println("Exception caught :" + e.getMessage());
            }
        }
    }


//            private TreeSet<Node> moveGen8neighbour(Node N) {
//                TreeSet<Node> set = new TreeSet<Node>(comparator);
//                int nrow, ncol; // stands for newRow , newColumn
//                int Nrow, Ncol;
//                Nrow = N.getRow();
//                Ncol = N.getCol();
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

    // Generate four touching neighbors
    public HashSet<Node> moveGen4neightbour(Node currentNode, int rows, int cols) {
        HashSet<Node> set = new HashSet<>();
        int nrow, ncol; // stands for newRow , newColumn
        int Nrow, Ncol;
        Nrow = currentNode.getRow();
        Ncol = currentNode.getCol();
        nrow = Nrow - 1;
        ncol = Ncol;
        // north
        if (nrow >= 0 && nrow < rows && !mapComp.isObstacleHaveCell(nrow, ncol)) {
            Node nn = new Node(nrow, ncol);
            nn.setDistance(currentNode.getDistance() + 1);
            set.add(nn);
        }

        // east
        nrow = Nrow;
        ncol = Ncol + 1;
        if (ncol >= 0 && ncol < cols && !mapComp.isObstacleHaveCell(nrow, ncol)) {
            Node nn = new Node(nrow, ncol);
            nn.setDistance(currentNode.getDistance() + 1);
            set.add(nn);
        }

        // south
        nrow = Nrow + 1;
        ncol =Ncol;
        if (nrow >= 0 && nrow < rows && !mapComp.isObstacleHaveCell(nrow, ncol)) {
            Node nn = new Node(nrow, ncol);
            nn.setDistance(currentNode.getDistance() + 1);
            set.add(nn);
        }

        //west
        nrow = Nrow;
        ncol = Ncol - 1;
        if (ncol >= 0 && ncol < cols && !mapComp.isObstacleHaveCell(nrow, ncol)) {
            Node nn = new Node(nrow, ncol);
            nn.setDistance(currentNode.getDistance() + 1);
            set.add(nn);
        }
        return set;
    }

    public boolean goalTest(Node node, int drow, int dcol) {
        if (node.getRow() == drow && node.getCol() == dcol)
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
