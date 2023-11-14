import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

public abstract class AlgoRunnerRunnableImpl implements AlgoRunnerRunnable{
    boolean exit;


    @Override
    public boolean takeExit() {
        return exit;
    }

    @Override
    public void setExit(boolean exit) {
        this.exit = exit;
    }


    MapComp mapComp;
    AlgoRunnerRunnableImpl(MapComp comp){
        this.mapComp = comp;
        this.exit = false;
    }

    public HashSet<Node> getNeighbours(Node currentNode){
        if ( mapComp.isFourDir()){
            return moveGen4Neighbour(currentNode);
        }
        return moveGen8Neighbour(currentNode);
    }


    // In clock wise direction starting from north-West
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
                    addCell(currentNode, nrow, ncol, set);
            }
        }
        // end of loop
        return set;
    }

    // Generate four touching neighbors in clock wise direction starting from North
    public HashSet<Node> moveGen4Neighbour(Node currentNode) {
        LinkedHashSet<Node> set = new LinkedHashSet<>();
        int nrow, ncol; // stands for newRow , newColumn
        int Nrow, Ncol;
        Nrow = currentNode.getRow();
        Ncol = currentNode.getCol();
        nrow = Nrow - 1;
        ncol = Ncol;
        // north
        if (nrow >= 0 && !mapComp.isObstacleHaveCell(nrow, ncol)) {
            addCell(currentNode, nrow, ncol, set);
        }

        // east
        nrow = Nrow;
        ncol = Ncol + 1;
        if (ncol < mapComp.getCols() && !mapComp.isObstacleHaveCell(nrow, ncol)) {
            addCell(currentNode, nrow, ncol, set);
        }

        // south
        nrow = Nrow + 1;
        ncol = Ncol;
        if (nrow < mapComp.getRows() && !mapComp.isObstacleHaveCell(nrow, ncol)) {
            addCell(currentNode, nrow, ncol, set);
        }

        //west
        nrow = Nrow;
        ncol = Ncol - 1;
        if (ncol >= 0 && !mapComp.isObstacleHaveCell(nrow, ncol)) {
            addCell(currentNode, nrow, ncol, set);
        }
        return set;
    }

    protected void addCell(Node currentNode, int nrow, int ncol, LinkedHashSet<Node> set){
        Node nn = new Node(nrow, ncol);
        nn.setParent(currentNode);
        nn.setDistance(currentNode.getDistance() + 1);
        set.add(nn);
    }

    public boolean goalTest(Node node) {
        if (node.getRow() == mapComp.getDstRow() && node.getCol() == mapComp.getDstCol())
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
