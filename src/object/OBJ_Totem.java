package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Totem extends SuperObject {
    public OBJ_Totem() { // constructor
        name = "Totem";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/totem"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
