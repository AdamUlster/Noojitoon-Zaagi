package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Wall extends Entity {
    public OBJ_Wall(GamePanel gp) {
        super(gp);

        name = "Wall";
        down1 = setup("objects/wall", 1,1);
        collision = true;

        //create hitbox in the center
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        solidArea.x = (gp.tileSize - solidArea.width) / 2;
        solidArea.y = (gp.tileSize - solidArea.height) / 2;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
