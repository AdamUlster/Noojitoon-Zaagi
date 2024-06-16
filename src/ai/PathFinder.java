package ai;

// Note: We did not develop the following code ourselves related to pathfinding
// We used an A* Pathfinding algorithm. However, we thought it was an important feature
// to include in our game so we did so

import main.GamePanel;

import java.util.ArrayList;

public class PathFinder {

    private GamePanel gp;
    private Node[][] node;
    private ArrayList<Node> openList = new ArrayList<>();
    private ArrayList<Node> pathList = new ArrayList<>();
    private Node startNode, goalNode, currentNode; // starting square, goal square, and current square
    private boolean goalReached = false;
    private int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }
    private void instantiateNodes() { // creates a node for every tile on the map
        node = new Node[gp.getMaxWorldCol()][gp.getMaxWorldRow()];

        int col = 0;
        int row = 0;

        while (col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()) {
            node[col][row] = new Node(col, row);

            col++;
            if (col == gp.getMaxWorldCol()) { // moves on to the next row
                col = 0;
                row ++;
            }
        }
    }

    private void resetNodes() { // resets all the nodes
        int col = 0;
        int row = 0;

        while (col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()) {
            node[col][row].setOpen(false);
            node[col][row].setChecked(false);
            node[col][row].setSolid(false);

            col++;
            if (col == gp.getMaxWorldCol()) { // moves on to the next row
                col = 0;
                row ++;
            }
        }

        // Reset other settings
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes(); // resets all the nodes

        // Set start and goal nodes
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            // Set solid node by checking solid tiles
            int tileNum = gp.tileM.mapTileNum[col][row];
            if (gp.tileM.tile[tileNum].collision) {
                node[col][row].setSolid(true);
            }

            // Set cost
            getCost(node[col][row]);

            col++;
            if (col == gp.maxWorldCol) { // moves on to the next row
                col = 0;
                row++;
            }
        }
    }

    private void getCost(Node node) {

        // G cost
        int xDistance = Math.abs(node.getCol() - startNode.getCol());
        int yDistance = Math.abs(node.getRow() - startNode.getRow());
        node.setGCost(xDistance + yDistance);

        // H cost
        xDistance = Math.abs(node.getCol() - goalNode.getCol());
        yDistance = Math.abs(node.getRow() - goalNode.getRow());
        node.setHCost(xDistance + yDistance);

        // F cost
        node.setFCost(node.getGCost() + node.getHCost());
    }

    public boolean search() {
        while (!goalReached && step < 100000) {
            int col = currentNode.getCol();
            int row = currentNode.getRow();

            // Check the current node
            currentNode.setChecked(true);
            openList.remove(currentNode);

            // Open the up node
            if (row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }

            // Open the left node
            if (col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }

            // Open the down node
            if (row + 1 < gp.maxWorldRow) {
                openNode(node[col][row + 1]);
            }

            // Open the right node
            if (col + 1 < gp.maxWorldRow) {
                openNode(node[col + 1][row]);
            }

            // Find the best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;
            for (int i = 0; i < openList.size(); i++) {

                // Check if the current node's F cost is better than the best F cost
                if (openList.get(i).getFCost() < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).getFCost();
                }

                // If the F cost is equal, check the G cost
                else if (openList.get(i).getFCost() == bestNodefCost) {
                    if (openList.get(i).getGCost() < openList.get(bestNodeIndex).getGCost()) {
                        bestNodeIndex = i;
                    }
                }
            }
            if (openList.isEmpty()) { // if there is no node in openList
                break; // end the loop
            }

            // After the loop, bestNodeIndex is the next node
            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step ++;
        }
        return goalReached;
    }

    private void openNode(Node node) {
        if (!node.isOpen() && !node.isChecked() && !node.isSolid()) {
            node.setOpen(true);
            node.setParent(currentNode);
            openList.add(node);
        }
    }

    private void trackThePath() { // works backwards to get the path to travel
        Node current = goalNode; // starts at the ending node

        while (current != startNode) {
            pathList.add(0, current); // adds the current node to the front of the list to go in reverse order
            current = current.getParent();
        }
    }

    // Get and set methods

    public ArrayList<Node> getOpenList() {
        return openList;
    }

    public void setOpenList(ArrayList<Node> openList) {
        this.openList = openList;
    }

    public ArrayList<Node> getPathList() {
        return pathList;
    }

    public void setPathList(ArrayList<Node> pathList) {
        this.pathList = pathList;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getGoalNode() {
        return goalNode;
    }

    public void setGoalNode(Node goalNode) {
        this.goalNode = goalNode;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public boolean isGoalReached() {
        return goalReached;
    }

    public void setGoalReached(boolean goalReached) {
        this.goalReached = goalReached;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Node[][] getNode() {
        return node;
    }

    public void setNode(Node[][] node) {
        this.node = node;
    }
}
