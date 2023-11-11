import java.util.Objects;

public class Node {
    private int row, col;
    private int distance;
    private Node parent;

    Node(int row, int col) {
        this.col = col;
        this.row = row;
        this.distance = 0;
        parent = null;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Node{" +
                "nrow=" + row +
                ", ncol=" + col +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return row == node.row &&
                col == node.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
