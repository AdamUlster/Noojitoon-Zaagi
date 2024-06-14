package ai;

// Note: We did not develop the following code ourselves related to pathfinding
// We used an A* Pathfinding algorithm. However, we thought it was an important feature
// to include in our game so we did so

public class Node {

    Node parent;
    public int col;
    public int row;
    int gCost;
    int hCost;
    int fCost;
    boolean solid;
    boolean open;
    boolean checked;

    public Node (int col, int row) { // constructor
        this.col = col;
        this.row = row;
    }
}
