package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];//create a 2d array for each tile element in the map

    public TileManager(GamePanel gp) {//set default

        this.gp = gp;

        tile = new Tile[10]; //kinds of tile types, for now there are 10 types of tiles, we can add more
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();//call on method to extract the tile pngs
        loadMap("/maps/map_1.txt");//load the formation of the tiles after inputing the file path of the map file
    }

    public void getTileImage() {//retrieves the tile png's from the resource files

        //setup(index of tile, tile file name, collision on or off);
            setup(0, "grass_1", false);
            setup(1, "brick_1", true);
            setup(2, "tree_1", true);
            setup(3, "water_1", true);
//            tile[0] = new Tile();
//            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/brick_1.png"));
//
//            tile[0] = new Tile();
//            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/brick_1.png"));

    }

    public void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String mapPath) {//reads through the map file and assigns into the tile array
        try {
            InputStream is = getClass().getResourceAsStream(mapPath);//extracts map file from resource folder

            int row = 0;
            int col = 0;
            Scanner mr = new Scanner(is);//create a scanner that goes through the file
            while (mr.hasNextLine()) {//iterate through each line in the file
                String fileLine = mr.nextLine();//read line in file
                String[] elements = fileLine.split(" ");//splits the file line into an array of strings
                while (col < gp.maxWorldCol) {//iterate through each element in the file
                    mapTileNum[col][row] = Integer.parseInt(elements[col]);//copy the file element into the
                    // corresponding index in the map tile array
                    col++;
                }
                if (col == gp.maxWorldCol) {//reset the loop for when the end of the column has been reached
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

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {//loops through each possible tile position
            // within
            // the screen
            int tileNum = mapTileNum[worldCol][worldRow];//extract the tile type from map tile array using its given
            // position

            int worldX = worldCol * gp.tileSize;//tile position on the map
            int worldY = worldRow * gp.tileSize;
            //calculates the adjustment factor for the tiles  when they are drawn using the data of where the player
            // should be
            int screenX = worldX - gp.player.worldX + gp.player.screenX;//tile position on the screen
            int screenY = worldY - gp.player.worldY + gp.player.screenY;


            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {//improves rendering efficiency by
                // checking if the tile being drawn are near the player in either of the four directions

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);//draw tile at
                // using its position relative to the screen

            }

            worldCol++;

            if (worldCol == gp.maxWorldCol) {//checks when draw method has reached the bottom of the column
                worldCol = 0;//resets column and x position
                worldRow++;//draw will now draw the column next to it
            }
        }
    }
}