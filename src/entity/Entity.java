package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea; // the collision box of the character
    public boolean collisionOn = false;
    public int solidAreaDefaultX, solidAreaDefaultY;

    // Player health
    public int maxHealth; // maximum number of lives the player has
    public int health; // current number of lives the player has
}