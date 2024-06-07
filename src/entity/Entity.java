package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    GamePanel gp;
    public int worldX, worldY;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); // the collision box of the character
    public boolean collisionOn = false;
    public int actionLockCounter = 0;//sets a pause for random movements in the npcs and other things
    public int solidAreaDefaultX, solidAreaDefaultY;
    public BufferedImage image1, image2, image3;
    public String name;
    public boolean collision = false;

    // Player health
    public int[] maxHealth = new int[3]; // maximum number of lives the player has
    public int[] health = new int[3]; // current number of lives the player has

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {
    }
    public void update() {
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this,false);
        gp.cChecker.checkPlayer(this);

        // entity can only move if collision is false
        if (!collisionOn) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }

        spriteCounter++;
        if (spriteCounter > 12) {//player image changes once every 12 frames, can adjust by increasing or decreasing
            if (spriteNum == 1) {//changes the player to first walking sprite to second sprite
                spriteNum = 2;
            } else if (spriteNum == 2) {//changes the player sprite from second to first
                spriteNum = 1;
            }
            spriteCounter = 0;//resets the sprite counter
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && // only draws an object if it is in the user's field of view
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (direction) {//check the direction, based on the direction it picks a different image
                case "up":
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                    break;
                case "down":
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                    break;
                case "left":
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                    break;
                case "right":
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                    break;
            }
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    public BufferedImage setup(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/" + imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;

    }
}