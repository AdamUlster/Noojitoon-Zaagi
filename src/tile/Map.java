package tile;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map extends TileManager{
    GamePanel gp;
    BufferedImage worldMap;
    public boolean miniMapOn = true; // the mini map should be displayed
    public Map(GamePanel gp) {
        super(gp);
        this.gp = gp;
        createWorldMap();
    }
    public void createWorldMap() {

        // Gets the map size
        int worldMapWidth = gp.tileSize * gp.maxWorldCol;
        int worldMapHeight = gp.tileSize * gp.maxWorldRow;

        worldMap = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB); // Instantiates the map and uses a coloured buffered image
        Graphics2D g2 = (Graphics2D) worldMap.createGraphics(); // attaches graphics to the map

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            int tileNum = mapTileNum[col][row]; // gets the map tile

            // gets the x and y coordinates of the tile
            int x = gp.tileSize * col;
            int y = gp.tileSize * row;
            g2.drawImage(tile[tileNum].image, x, y, null); // draws the tile

            col ++;
            if (col == gp.maxWorldCol) { // moves on to the next row
                col = 0;
                row ++;
            }
        }
    }

    public void drawFullMapScreen(Graphics2D g2) { // draws the full map
        g2.setColor(Color.black); // background colour
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Size of the map on the screen
        int width = 700;
        int height = 700;
        int x = gp.screenWidth / 2 - width / 2; // centers the map
        int y = gp.screenHeight / 2 - height / 2; // centers the map
        g2.drawImage(worldMap, x, y, width, height,null);

        // Draw the player on the map
        double scale = (double) gp.tileSize * gp.maxWorldCol / width; // gets the scale which represents the size and position to draw the player
        int playerX = (int)(x + gp.player.worldX / scale); // gets the player's scaled world X
        int playerY = (int)(y + gp.player.worldY / scale); // gets the player's scaled world Y
        int playerSize = (gp.tileSize / 3);
        g2.drawImage(gp.player.down1, playerX - 6, playerY - 6, playerSize, playerSize, null); // draws the player with an offset due to the place Java Swing draws a sprite from
    }

    public void drawMiniMap(Graphics2D g2) { // draws the mini map
        if (miniMapOn) {
            int width = 250;
            int height = 250;
            int x = gp.screenWidth - width - 50; // displays the map at the top right
            int y = 50;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f)); // Sets the opacity to draw the map
            g2.drawImage(worldMap, x, y, width, height, null); // draws the mini map

            // Draw the player on the mini map
            double scale = (double) gp.tileSize * gp.maxWorldCol / width; // gets the scale which represents the size and position to draw the player
            int playerX = (int) (x + gp.player.worldX / scale); // gets the player's scaled world X
            int playerY = (int) (y + gp.player.worldY / scale); // gets the player's scaled world Y
            int playerSize = gp.tileSize / 4;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f)); // Resets the opacity when drawing the player
            g2.drawImage(gp.player.down1, playerX - 6, playerY - 6, playerSize, playerSize, null); // draws the player with an offset due to the place Java Swing draws a sprite from
        }
    }
}
