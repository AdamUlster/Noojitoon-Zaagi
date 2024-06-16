package object;

import entity.Entity;
import main.GamePanel;

// TOTEMS THAT ARE COLLECTED BY THE PLAYER
public class OBJ_Totem extends Entity {
//    SCALING FACTOR FOR HIT BOX
    public double hitboxScale = 0.80;

//    CONSTRUCTOR
    public OBJ_Totem(GamePanel gp) {
        super(gp); // CALL ENTITY CLASS

        name = "Totem";
//        SINCE DEFAULT DIRECTION IS DOWN
        down1 = setup("objects/totem", 1,1);
        collision = true;// MAKE TOTEM AN COLLIDEABLE OBKECT

//        SET HIT BOX TO THE CENTER
        solidArea.width = (int)(gp.tileSize * hitboxScale);
        solidArea.height = (int)(gp.tileSize * hitboxScale);
        solidArea.x = (gp.tileSize - solidArea.width) / 2;
        solidArea.y = (gp.tileSize - solidArea.height) / 2;

//        SET UP COLLISION BOX COORDINATES
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}