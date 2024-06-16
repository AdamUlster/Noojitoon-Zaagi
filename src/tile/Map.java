package tile;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map extends TileManager{
    private GamePanel gp;
    private BufferedImage worldMap;
    private boolean miniMapOn = true; // the mini map should be displayed
    public Map(GamePanel gp) {
        super(gp);
        this.gp = gp;
        createWorldMap();
    }
    private void createWorldMap() {

        // Gets the map size
        int worldMapWidth = gp.getTileSize() * gp.getMaxWorldCol();
        int worldMapHeight = gp.getTileSize() * gp.getMaxWorldRow();

        worldMap = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB); // Instantiates the map and uses a coloured buffered image
        Graphics2D g2 = worldMap.createGraphics(); // attaches graphics to the map

        int col = 0;
        int row = 0;

        while (col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()) {
            int tileNum = getMapTileNum()[col][row]; // gets the map tile

            // gets the x and y coordinates of the tile
            int x = gp.getTileSize() * col;
            int y = gp.getTileSize() * row;
            g2.drawImage(getTile()[tileNum].getImage(), x, y, null); // draws the tile

            col ++;
            if (col == gp.getMaxWorldCol()) { // moves on to the next row
                col = 0;
                row ++;
            }
        }
    }

    public void drawFullMapScreen(Graphics2D g2) { // draws the full map
        g2.setColor(Color.black); // background colour
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());

        // Size of the map on the screen
        int width = 700;
        int height = 700;
        int x = gp.getScreenWidth() / 2 - width / 2; // centers the map
        int y = gp.getScreenHeight() / 2 - height / 2; // centers the map
        g2.drawImage(worldMap, x, y, width, height,null);

        // Draw the player on the map
        double scale = (double) gp.getTileSize() * gp.getMaxWorldCol() / width; // gets the scale which represents the size and position to draw the player
        int playerX = (int)(x + gp.getPlayer().getWorldX() / scale); // gets the player's scaled world X
        int playerY = (int)(y + gp.getPlayer().getWorldY() / scale); // gets the player's scaled world Y
        int playerSize = (gp.getTileSize() / 3);
        g2.drawImage(gp.getPlayer().getDown1(), playerX - 6, playerY - 6, playerSize, playerSize, null); // draws the player with an offset due to the place Java Swing draws a sprite from

        // Draw the totems on the map
        for (int i = 0; i < gp.getObj().length; i++) {
            if (gp.getObj()[i] != null && gp.getObj()[i].getName().equals("Totem")) {
                int totemX = (int)(x + gp.getObj()[i].getWorldX() / scale); // gets the totem's scaled world X
                int totemY = (int)(y + gp.getObj()[i].getWorldY() / scale); // gets the totem's scaled world Y
                int totemSize = (gp.getTileSize() / 3);
                g2.drawImage(gp.getObj()[i].getDown1(), totemX - 6, totemY - 8, totemSize, totemSize, null); // draws the totem with an offset due to the place Java Swing draws a sprite from
            }
        }
    }

    public void drawMiniMap(Graphics2D g2) { // draws the mini map
        if (miniMapOn) {
            int width = 250;
            int height = 250;
            int x = gp.getScreenWidth() - width - 50; // displays the map at the top right
            int y = 50;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f)); // Sets the opacity to draw the map
            g2.drawImage(worldMap, x, y, width, height, null); // draws the mini map

            // Draw the player on the mini map
            double scale = (double) gp.getTileSize() * gp.getMaxWorldCol() / width; // gets the scale which represents the size and position to draw the player
            int playerX = (int) (x + gp.getPlayer().getWorldX() / scale); // gets the player's scaled world X
            int playerY = (int) (y + gp.getPlayer().getWorldY() / scale); // gets the player's scaled world Y
            int playerSize = gp.getTileSize() / 4;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // Resets the opacity when drawing the player
            g2.drawImage(gp.getPlayer().getDown1(), playerX - 6, playerY - 6, playerSize, playerSize, null); // draws the player with an offset due to the place Java Swing draws a sprite from

            // Draw the totems on the mini map
            for (int i = 0; i < gp.getObj().length; i++) {
                if (gp.getObj()[i] != null && gp.getObj()[i].getName().equals("Totem")) {
                    int totemX = (int)(x + gp.getObj()[i].getWorldX() / scale); // gets the totem's scaled world X
                    int totemY = (int)(y + gp.getObj()[i].getWorldY() / scale); // gets the totem's scaled world Y
                    int totemSize = (gp.getTileSize() / 3);
                    g2.drawImage(gp.getObj()[i].getDown1(), totemX - 6, totemY - 6, totemSize, totemSize, null); // draws the totem with an offset due to the place Java Swing draws a sprite from
                }
            }
        }
    }

    // Get and set methods
    public boolean isMiniMapOn() {
        return miniMapOn;
    }

    public void setMiniMapOn(boolean miniMapOn) {
        this.miniMapOn = miniMapOn;
    }

    public BufferedImage getWorldMap() {
        return worldMap;
    }

    public void setWorldMap(BufferedImage worldMap) {
        this.worldMap = worldMap;
    }
}
