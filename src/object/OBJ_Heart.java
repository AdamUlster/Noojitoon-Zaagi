package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends Entity {
    public OBJ_Heart(GamePanel gp) { // constructor
        super(gp); // calls the entity class

        name = "Heart";

        // Makes the images bigger
        image1 = setup("objects/heart_full", 1.5,1.5);
        image2 = setup("objects/heart_half", 1.5,1.5);
        image3 = setup("objects/heart_blank", 1.5,1.5);
    }
}