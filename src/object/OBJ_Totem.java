package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Totem extends Entity {
    public OBJ_Totem(GamePanel gp) { // constructor
        super(gp); // calls the entity class

        name = "Totem";
        down1 = setup("objects/totem", 1);
        collision = true;
    }
}