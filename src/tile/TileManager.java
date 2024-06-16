package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class TileManager {
    private GamePanel gp;
    private Tile[] tile;
    private int mapTileNum[][];//create a 2d array for each tile element in the map

    private boolean drawPath = false;
    public TileManager(GamePanel gp) {//set default

        this.gp = gp;

        tile = new Tile[10]; //kinds of tile types, for now there are 10 types of tiles, we can add more
        mapTileNum = new int[gp.getMaxWorldCol()][gp.getMaxWorldRow()];
        getTileImage();//call on method to extract the tile pngs
        loadMap("/maps/map_1.txt");//load the formation of the tiles after inputing the file path of the map file
    }

    private void getTileImage() {//retrieves the tile png's from the resource files

        //setup(index of tile, tile file name, collision on or off);
        setup(0, "tree", true);//turn back to true
        setup(1, "brick", false);
        setup(2, "grass", false);
        setup(3, "water", true);//turn back to true
        setup(4, "dirt", false);
        setup(5, "snow", false);
        setup(6, "lilypad", false);
        setup(7, "snowy_tree", true);
    }

    private void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].setImage(ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png")));
            tile[index].setImage(uTool.scaleImage(tile[index].getImage(), gp.getTileSize(), gp.getTileSize()));
            tile[index].setCollision(collision);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMap(String mapPath) {//reads through the map file and assigns into the tile array
        try {
            InputStream is = getClass().getResourceAsStream(mapPath);//extracts map file from resource folder

            int row = 0;
            int col = 0;
            Scanner mr = new Scanner(is);//create a scanner that goes through the file
            while (mr.hasNextLine()) {//iterate through each line in the file
                String fileLine = mr.nextLine();//read line in file
                String[] elements = fileLine.split(" ");//splits the file line into an array of strings
                while (col < gp.getMaxWorldCol()) {//iterate through each element in the file
                    mapTileNum[col][row] = Integer.parseInt(elements[col]);//copy the file element into the
                    // corresponding index in the map tile array
                    col++;
                }
                if (col == gp.getMaxWorldCol()) {//reset the loop for when the end of the column has been reached
                    col = 0;
                    row++;
                }
            }
            mr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {//uses a file and loops to draw the tiles more efficiently

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.getMaxWorldCol() && worldRow < gp.getMaxWorldRow()) {//loops through each possible tile position
            // within
            // the screen
            int tileNum = mapTileNum[worldCol][worldRow];//extract the tile type from map tile array using its given
            // position

            int worldX = worldCol * gp.getTileSize();//tile position on the map
            int worldY = worldRow * gp.getTileSize();
            //calculates the adjustment factor for the tiles  when they are drawn using the data of where the player
            // should be
            int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();//tile position on the screen
            int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();


            if (worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
                    worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
                    worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
                    worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) {//improves rendering efficiency by
                // checking if the tile being drawn are near the player in either of the four directions

                g2.drawImage(tile[tileNum].getImage(), screenX, screenY, null);//draw tile at
                // using its position relative to the screen

            }

            worldCol++;

            if (worldCol == gp.getMaxWorldCol()) {//checks when draw method has reached the bottom of the column
                worldCol = 0;//resets column and x position
                worldRow++;//draw will now draw the column next to it
            }
        }

        if (drawPath) { // if the path should be drawn
            g2.setColor(new Color(255, 0, 0 ,70)); // red with a reduces opacity

            for (int i = 0; i < gp.getPFinderToTotem().getPathList().size(); i++) {
                int worldX = gp.getPFinderToTotem().getPathList().get(i).getCol() * gp.getTileSize();
                int worldY = gp.getPFinderToTotem().getPathList().get(i).getRow() * gp.getTileSize();
                int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();//tile position on the screen
                int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
                g2.fillRect(screenX, screenY, gp.getTileSize(), gp.getTileSize());
            }
        }
    }

    // Get and set methods
    public boolean isDrawPath() {
        return drawPath;
    }

    public void setDrawPath(boolean drawPath) {
        this.drawPath = drawPath;
    }

    public GamePanel getGp() {
        return gp;
    }

    public void setGp(GamePanel gp) {
        this.gp = gp;
    }

    public Tile[] getTile() {
        return tile;
    }

    public void setTile(Tile[] tile) {
        this.tile = tile;
    }

    public int[][] getMapTileNum() {
        return mapTileNum;
    }

    public void setMapTileNum(int[][] mapTileNum) {
        this.mapTileNum = mapTileNum;
    }
}