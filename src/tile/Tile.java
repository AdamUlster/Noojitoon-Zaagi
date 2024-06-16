package tile;

import java.awt.image.BufferedImage;

//CONSTRUCTOR CLASS FOR TILE
public class Tile {
    private BufferedImage image;
    private boolean collision = false;

    // Get and set methods

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}