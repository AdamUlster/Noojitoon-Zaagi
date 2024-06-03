package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Totem extends SuperObject{
    public OBJ_Totem() { // constructor
        name = "Key";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/totem.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
