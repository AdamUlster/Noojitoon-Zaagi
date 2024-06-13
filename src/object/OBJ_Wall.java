package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Wall extends Entity {
    public OBJ_Wall(GamePanel gp) {
        super(gp);

        name = "Wall";
        down1 = setup("objects/wall", 1,1);
        collision = true;
    }
}
