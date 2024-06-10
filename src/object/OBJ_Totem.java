package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Totem extends Entity {
    public double hitboxScale = 0.80;//scaling factor for hitbox
    public OBJ_Totem(GamePanel gp) { // constructor
        super(gp); // calls the entity class

        name = "Totem";
        down1 = setup("objects/totem", 1,1);
        collision = true;

        //create hitbox in the center
        solidArea.width = (int)(gp.tileSize * hitboxScale);
        solidArea.height = (int)(gp.tileSize * hitboxScale);
        solidArea.x = (gp.tileSize - solidArea.width) / 2;
        solidArea.y = (gp.tileSize - solidArea.height) / 2;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}