package tile;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map extends TileManager{
    GamePanel gp;// CALL ON GAME PANEL CLASS
    BufferedImage worldMap;// CALL ON WORLD MAP CLASS

//    DISPLAYS MINI MAP
    public boolean miniMapOn = true; // the mini map should be displayed

//    CONSTRUCTOR CLASS
    public Map(GamePanel gp) {
        super(gp);
        this.gp = gp;
        createWorldMap();// CREATE WORLD MAP
    }
    public void createWorldMap() {

//        GET MAP SIZE
        int worldMapWidth = gp.tileSize * gp.maxWorldCol;
        int worldMapHeight = gp.tileSize * gp.maxWorldRow;

//        INSTANTIATES THE MAP AND USES A COLOURED BUFFERED IMAGE
        worldMap = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB);
//        ATTACHES GRAPHICS TO THE MAP
        Graphics2D g2 = (Graphics2D) worldMap.createGraphics();

        int col = 0;
        int row = 0;

//        ITERATE THROUGH ALL TILES
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            int tileNum = mapTileNum[col][row]; // GET TILE VALUE FROM 2D ARRAY CONTAINING THE MAP

//            GET COORDINATES OF THE TILE
            int x = gp.tileSize * col;
            int y = gp.tileSize * row;
            g2.drawImage(tile[tileNum].image, x, y, null); // DRAW TILE

            col ++;//INCREMENT TO DRAW THE NEXT ADJACENT TILE
            if (col == gp.maxWorldCol) { //MOVES ONTO THE NEXT ROW IF ALL TILES IN THE ROW HAVE BEEN DRAWN
                col = 0;
                row ++;
            }
        }
    }

//    DRAW FULLSCREEN MAP WHEN M KEY HAS BEEN PRESSED
    public void drawFullMapScreen(Graphics2D g2) {
        g2.setColor(Color.black); // BACKGROUND COLOUR
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

//        SIZE OF THE MAP ON THE SCREEN
        int width = 700;
        int height = 700;

//        CENTERS THE MAP ON THE SCREEN
        int x = gp.screenWidth / 2 - width / 2;
        int y = gp.screenHeight / 2 - height / 2;
        g2.drawImage(worldMap, x, y, width, height,null);

//        DRAW PLAYER ON THE MAP
//        GETS SCALE AND SCALES POSITION OF PLAYER FOR DRAWING ON THE SCREEN
        double scale = (double) gp.tileSize * gp.maxWorldCol / width;

//        GET PLAYER'S COORDINATED AND SCALES THEM
        int playerX = (int)(x + gp.player.worldX / scale);
        int playerY = (int)(y + gp.player.worldY / scale);
        int playerSize = (gp.tileSize / 3);

//        DRAWS THE PLAYER WITH AN OFFSET DUE TO THE PLACE JAVA SWING DRAWS A SPRITE FROM
        g2.drawImage(gp.player.down1, playerX - 6, playerY - 6, playerSize, playerSize, null);

//        DRAW THE TOTEMS ON THE MAP
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null && gp.obj[i].name.equals("Totem")) {

//                GET TOTEM'S COORDINATES AND SCALES THEM FOR THE MAP
                int totemX = (int)(x + gp.obj[i].worldX / scale);
                int totemY = (int)(y + gp.obj[i].worldY / scale);
                int totemSize = (gp.tileSize / 3);

//                DRAWS THE TOTEM WITH AN OFFSET DUE TO THE PLACE JAVA SWING DRAWS A SPRITE FROM
                g2.drawImage(gp.obj[i].down1, totemX - 6, totemY - 8, totemSize, totemSize, null);
            }
        }
    }

//    DRAWS MINI MAP
    public void drawMiniMap(Graphics2D g2) {
        //CHECKS IF MINI MAP HAS BEEN TOGGLED ON
        if (miniMapOn) {
            //MINI MAP DIMENSIONS ON THE SCREEN
            int width = 250;
            int height = 250;

//            MINI MAP COORDINATES, DISPLAYS AT THE TOP RIGHT
            int x = gp.screenWidth - width - 50;
            int y = 50;

//            SETS OPACITY TO DRAW THE MAP
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));

//            DRAW MINI MAP
            g2.drawImage(worldMap, x, y, width, height, null); // draws the mini map

//            DRAW PLAYER ON TEH MINI MAP

//            GETS PLAYER COORDINATED, AND SCALES THEM FOR THE MINI MAP
            double scale = (double) gp.tileSize * gp.maxWorldCol / width;
            int playerX = (int) (x + gp.player.worldX / scale);
            int playerY = (int) (y + gp.player.worldY / scale);
            int playerSize = gp.tileSize / 4;

//            RESET OPACITY WHEN DRAWING THE PLAYER, AND DRAWS THE PLAYER WITH AN OFFSET DUE TO THE WAY JAVA SWING
//            DRAWS A SPRITE FROM
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2.drawImage(gp.player.down1, playerX - 6, playerY - 6, playerSize, playerSize, null);

//            DRAWS TOTEMS ON TEH MINI MAP

//            ITERATE THROUGH ALL OBJECTS
            for (int i = 0; i < gp.obj.length; i++) {

//                CHECK IF THE OBJECT IS A TOTEM AND HAS NOT YET BEEN COLLECTED BY THE PLAYER
                if (gp.obj[i] != null && gp.obj[i].name.equals("Totem")) {

//                    GETS TOTEM COORDINATES, AND SCALES THEM FOR THE MINI MAP
                    int totemX = (int)(x + gp.obj[i].worldX / scale);
                    int totemY = (int)(y + gp.obj[i].worldY / scale);
                    int totemSize = (gp.tileSize / 3);

//                    DRAWS THE TOTEM WITH AN OFFSET DUE TO THE PLACE JAVA SWING DRAWS SPRITES FROM
                    g2.drawImage(gp.obj[i].down1, totemX - 6, totemY - 6, totemSize, totemSize, null);
                }
            }
        }
    }
}
