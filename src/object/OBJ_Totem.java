package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Totem extends Entity {
    public OBJ_Totem(GamePanel gp) { // constructor
        super(gp);

        name = "Totem";
        down1 = setup("objects/totem");
        collision = true;
    }
}