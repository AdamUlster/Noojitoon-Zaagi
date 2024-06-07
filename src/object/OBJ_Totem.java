package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Totem extends SuperObject {
    GamePanel gp;
    public OBJ_Totem(GamePanel gp) { // constructor
        name = "Totem";
        try {
            image1 = ImageIO.read(getClass().getResourceAsStream("/objects/totem.png"));
            uTool.scaleImage(image1, gp.tileSize, gp.tileSize);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}