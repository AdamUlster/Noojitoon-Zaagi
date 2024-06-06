package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends SuperObject {
    public OBJ_Heart() { // constructor
        name = "Heart";
        try {
            image1 = ImageIO.read(getClass().getResourceAsStream("/objects/heart_full.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/objects/heart_half.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/objects/heart_blank.png"));

            image1 = scaleImage(image1, 150, 150);
            image2 = scaleImage(image2, 150, 150);
            image3 = scaleImage(image3, 150, 150);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}