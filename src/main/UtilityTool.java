package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {

//    METHOD FOR SCALING IMAGES HERE INSTEAD OF REPEATING IT OVER AND OVER IN THE IMAGE SETUP
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
        //CREATE NEW BUFFERED IMAGE
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();

        //COPY THE INPUT IMAGE INTO THE NEW IMAGE, WITH NEW SCALING
        g2.drawImage(original, 0,0, width, height, null);
        g2.dispose();

        return scaledImage;//RETURN THE SCALED IMAGE
    }
}