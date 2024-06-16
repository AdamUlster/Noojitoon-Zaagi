package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

//MANAGES TILES ON THE MAP
public class TileManager {
    GamePanel gp;// CALL ON THE GAME PANEL CLASS
    public Tile[] tile;//CREATE ARRAY FOR THE TYPES OF TIES
    public int mapTileNum[][];//CREATE A 2D ARRAY FOR EVERY TILE ON THE MAP

    public boolean drawPath = false;//DOES NOT DISPLAY THE PATHFINDING TILES

//    CONSTRUCTOR
    public TileManager(GamePanel gp) {
        this.gp = gp;//CALL ON GAME PANEL CLASS

//        CREATE 8 TYPES OF TILES
        tile = new Tile[8];

//        CREATE THE MAP TILE TYPE 2D ARRAY BASED ON HOW BIG THE WORLD MAP SHOULD BE
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();//USE METHOD TO EXTRACT TILE IMAGES

        //CALL ON THE TEXT FILE CONTAINING THE TYPES OF FILES TO CREATE THE MAP
        loadMap("/maps/map_1.txt");
    }

//    RETRIEVES THE TILE PNG FILES FROM THE RESOURCE FOLDER
    public void getTileImage() {


//        SETS UP EVERY KIND OF TILE, AND WHETHER IT IS A SOLID TILE (IE WATER) OR THE PLAYER CAN TRAVEL ON IT (IE LAND)
        setup(0, "tree", true);// TREE TILE
        setup(1, "brick", false);//BRICK / STONE TILE
        setup(2, "grass", false);// GRASS TILE
        setup(3, "water", true);//WATER TILE
        setup(4, "dirt", false);//DIRT TILE
        setup(5, "snow", false);// SNOW TILE
        setup(6, "lilypad", false);// LILY PAD TILE
        setup(7, "snowy_tree", true);// SNOWY TREE TILE
    }

//    SET UP METHOD FOR RETRIEVING IMAGES OF TILES
    public void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();//CREATE A UTILITY TOOL FROM UTILITY TOOL CLASS

        try {
            tile[index] = new Tile();//CREATE TILE IN TILE ARRAY

//            RETRIEVE TILE FILE
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);//SCALE TILE IMAGE
            tile[index].collision = collision;// SET TILE COLLISION
        } catch (IOException e) {//IN CASE FILE DOES NOT EXIST
            e.printStackTrace();
        }
    }

//    LOADS THE MAP IN TERMS OF IMAGES, AND NOT TILE TYPES
//    READS THROUGH THE MAP FILE AND ASSIGNS INTO THE TILE ARRAY
    public void loadMap(String mapPath) {
        try {
//            EXTRACT MAP TEXT FILE FROM THE RESOURCE FOLDER
            InputStream is = getClass().getResourceAsStream(mapPath);

//            DECLARE POSITION OF THE SCANNER
            int row = 0;
            int col = 0;

//            CREATE A SCANNER THAT GOES THROUGH THE FILE
            Scanner mr = new Scanner(is);
//            ITERATE THROUGH EACH LINE IN THE FILE
            while (mr.hasNextLine()) {
                String fileLine = mr.nextLine();// READ LINE IN THE FILE
                String[] elements = fileLine.split(" ");//SPLITS THE FILE LINE INTO ITS ELEMENTS

//                ITERATE THROUGH EACH ELEMENT IN THE ELEMENT ARRAY
                while (col < gp.maxWorldCol) {
//                    COPY FILE ELEMENT INTO THE CORRESPONDING INDEX IN THE MAP TILE ARRAY
                    mapTileNum[col][row] = Integer.parseInt(elements[col]);
                    col++;//MOVE TO NEXT TILE POSITION
                }
//                RESET THE LOOP AFTER THE LAST TILE IN THE ROW HAS BEEN REACHED
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            mr.close();//CLOSE SCANNER
        } catch (Exception e) {//IN CASE TILE FILE DOES NOT EXIST
            e.printStackTrace();
        }
    }

//    DRAWS THE TILES VIA FILE
    public void draw(Graphics2D g2) {//uses a file and loops to draw the tiles more efficiently

        int worldCol = 0;
        int worldRow = 0;

//        LOOPS THROUGH EACH POSSIBLE TILE POSITION VIA ROWS AND COLUMNS
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

//            EXTRACT TILE TYPE FROM MAP TILE ARRYA USING ITS GIVEN POSITION AS INDEXES
            int tileNum = mapTileNum[worldCol][worldRow];

//            TILE POSITION ON THE MAP
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

//            CALCULATES THE ADJUSTMENT FACTOR FOR THE TILES WHEN THEY ARE DRAWN USING THE DATA OF WHERE THE PLAYER
//            SHOULD BE, EG TILES SHOULD BE DRAWN 5 PIXELS TO THE RIGHT IF THE PLAYER IS 5 PIXELS TO THE LEFT OF A
//            TILE TO ENSURE IT STAYS IN THE CENTER OF THE SCREEN
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

//            CHECK IF THE TILE IS NEAR THE PLAYER IN EITHER OF THE FOUR DIRECTIONS

//            RENDERING PERFORMANCE IS IMPROVED MASSIVELY IF ONLY THE SURROUNDING TILES ARE DRAWN INSTEAD OF ALL THE
//            TILES IN THE MAP ARE DRAWN
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

//                DRAW TILE USING ITS POSITION RELATIVE TO THE SCREEN
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++;// MOVE ON TO THE NEXT TILE

//            CHECKS WHEN THE DRAW METHOD HAS REACHED THE END OF THE ROW
            if (worldCol == gp.maxWorldCol) {
//                RESET ROW TO START
                worldCol = 0;

//                MOVE ON TO THE NEXT ROW
                worldRow++;//draw will now draw the column next to it
            }
        }


//        WALKTHROUGH PATH

//        CHECKS IF THE PATH SHOULD BE DRAWN
        if (drawPath) {
            g2.setColor(new Color(255, 0, 0 ,70)); //SET COLOUR TO RED WITH REDUCED OPACITY

//            ITERATE THROUGH EACH COORDINATE IN THE PATH FINDING METHOD, SCALE IT TO WORLD SIZE, AND PRINTS IT
            for (int i = 0; i < gp.pFinderToTotem.pathList.size(); i++) {
//                GET WORLD COORDINATES OF PATH TILE
                int worldX = gp.pFinderToTotem.pathList.get(i).col * gp.tileSize;
                int worldY = gp.pFinderToTotem.pathList.get(i).row * gp.tileSize;

//                GET COORDINATES OF PATH TILE RELATIVE TO SCREEN
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

//                DRAW PATH TILE IF IT IS NEAR THE PLAYER, TO SAVE ON RENDERING PERFORMANCE
                if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                        worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                        worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                        worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                    g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
                }
            }
        }
    }
}