package ai;

// Note: We did not develop the following code ourselves related to pathfinding
// We used an A* Pathfinding algorithm. However, we thought it was an important feature
// to include in our game so we did so

import main.GamePanel;

import java.util.ArrayList;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode; // starting square, goal square, and current square
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }
    public void instantiateNodes() { // creates a node for every tile on the map
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row] = new Node(col, row);

            col++;
            if (col == gp.maxWorldCol) { // moves on to the next row
                col = 0;
                row ++;
            }
        }
    }

    public void resetNodes() { // resets all the nodes
        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if (col == gp.maxWorldCol) { // moves on to the next row
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
                node[col][row].solid = true;
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

    public void getCost(Node node) {

        // G cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // H cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // F cost
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {
        while (!goalReached && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            // Check the current node
            currentNode.checked = true;
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
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }

                // If the F cost is equal, check the G cost
                else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }
            if (openList.size() == 0) { // if there is no node in openList
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

    public void trackThePath() { // works backwards to get the path to travel
        Node currentNode = goalNode; // starts at the ending node

        while (currentNode != startNode) {
            pathList.add(0, currentNode); // adds the current node to the front of the list to go in reverse order
            currentNode = currentNode.parent;
        }
    }

    public void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
}
