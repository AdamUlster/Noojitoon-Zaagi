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

    // Dimensions of the rectangle
    public int x = 0;
    public int y = 0;
    public int width = 48;
    public int height = 48;
    public Rectangle solidArea = new Rectangle(x, y, width, height); // the collision box of the character
    public boolean collisionOn = false;
    public boolean invincible = false; // sets whether the entity is immune to damage
    public int invincibilityCounter = 0; // keeps track of how long the entity is invisible for
    public boolean displayDeathMessage = false; // whether the death message should be displayed
    public boolean dead = false; // sets whether the entity is dead
    public boolean deadFlicker = false; // sets whether the entity should be flickering dead
    public int deadCounter = 0; // keeps track of how long the entity is dead for
    public int actionLockCounter = 0; // sets a pause for random movements in the npcs and other things
    public int solidAreaDefaultX, solidAreaDefaultY;
    public BufferedImage image1, image2, image3;
    public String name;
    public boolean collision = false;
    public int type; // 0 = player, 1 = NPC, 2 = monster

    // Entity health
    public int maxHealth; // maximum number of lives the entity has
    public int health; // current number of lives the entity has

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {
    }
    public void update() {
        setAction();

        Spirit currentSpirit = gp.player.getCurrentSpirit(); // gets the current spirit

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this,false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.type == 2 && contactPlayer) { // if this class is a monster and the monster has made contact with the player
            if (!gp.player.invincible) {
                currentSpirit.health -= 1;
                gp.player.invincible = true; // the player is now invincible for a given period of time
            }

        }

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

            g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

    public BufferedImage setup(String imagePath, double scale) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/" + imagePath + ".png"));
            image = uTool.scaleImage(image, (int) (gp.tileSize * scale), (int) (gp.tileSize * scale));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;

    }
}