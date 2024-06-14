package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    protected GamePanel gp;//import the game panel

    //DECLARE IMAGE BOOLEANS: BOOLEANS THAT DETERMINE WHICH IMAGE TO DRAW
    public BufferedImage image1, image2, image3;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;//movement image boolean
    public BufferedImage attackUp1, attackUp2, attackUp3, attackDown1, attackDown2, attackDown3, attackLeft1, attackLeft2, attackLeft3, attackRight1, attackRight2, attackRight3;//primary attack booleans
    public BufferedImage specialUp1, specialUp2, specialUp3, specialUp4, specialUp5, specialUp6, specialUp7;//special moves for up direction
    public BufferedImage specialDown1, specialDown2, specialDown3, specialDown4, specialDown5, specialDown6, specialDown7;//special moves for down direction
    public BufferedImage specialLeft1, specialLeft2, specialLeft3, specialLeft4, specialLeft5, specialLeft6, specialLeft7;//special moves for left direction
    public BufferedImage specialRight1, specialRight2, specialRight3, specialRight4, specialRight5, specialRight6, specialRight7;//special moves for right direction

    public int x = 0;
    public int y = 0;
    public int width = 48;
    public int height = 48;
    public int xMove = 0; // keeps track of the x movement for projectiles
    public int yMove = 0; // keeps track of the y movement for projectiles
    public Rectangle solidArea = new Rectangle(x, y, width, height); // the collision box of the characterdd
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0); // the part of the spirit that is attacking the monster

    //COUNTERS
    public int deadCounter = 0; // keeps track of how long the entity is dead for
    public int actionLockCounter = 0; // sets a pause for random movements in the npcs and other things
    public int spriteCounter = 0;//track how long an image has been in frame for to switch images for animation
    public int shotAvailableCounter = 0; // sets a counter that tracks whether the user can shoot a projectile
    int healthBarCounter = 0;//health of the entity

    //STATE
    public int worldX, worldY;//position relative to the world
    public int solidAreaDefaultX, solidAreaDefaultY;//position of the collision box
    public String direction = "down";//default direction
    public int spriteNum = 1;//set default sprite to bear
    public boolean invincible = false; // sets whether the entity is immune to damage
    public boolean collisionOn = false;//sets whether entity is a collideable object
    boolean attacking = false;//boolean to determine if the player is attacking
    boolean specialAttacking = false;//boolean to determine if the player is using special attack
    public boolean displayDeathMessage = false; // whether the death message should be displayed
    public boolean projectileAlive = true; // keeps track of whether the projectile is alive
    public boolean dead = false; // sets whether the entity is dead
    public boolean isDying = false; // sets whether the entity is dying
    public boolean deadFlicker = false; // sets whether the entity should be flickering dead
    public boolean healthBarOn = false; // whether the monster has a health bar above them

    //CHARACTER ATTRIBUTES
    public int maxHealth; // maximum number of lives the entity has
    public int health; // current number of lives the entity has
    public String name;//name of the entity
    public boolean collision = false;
    public int type; // 0 = player, 1 = NPC, 2 = monster, 3 = projectile
    public int speed;//entity speed
    public int attack;//attacking value; how much health an attack decreases
    public int defense;//resistance to attack
    public Projectile projectile;//import projectile class for turtle primary fire
    public TargetingProjectile targetProjectile;//import targeting projectile class for eagle special fire

    //ITEM ATTRIBUTES
    public int useCost;

    public Entity(GamePanel gp) {//create entity
        this.gp = gp;
    }

    public void setAction() {
    }

    public void damageReaction() {
    } // controls the monster's reaction to taking damage. This method is modified in the individual monster classes

    public void update() {//update method that will occur every frame
        setAction();

        Spirit currentSpirit = gp.player.getCurrentSpirit(); // gets the current spirit

        collisionOn = false;//turn entity into a non collideable object
        gp.cChecker.checkTile(this);//check if entity is touching a tile
        gp.cChecker.checkObject(this, false);//check if entity is touching an object
        gp.cChecker.checkEntity(this, gp.npc);//check if entity is touching an npc
        gp.cChecker.checkEntity(this, gp.monster);//check if entity is touching a monster
        boolean contactPlayer = gp.cChecker.checkPlayer(this);//check if entity is touching a player

        if (this.type == 2 && contactPlayer) { // if this class is a monster and the monster has made contact with the player
            if (!gp.player.invincible) {//check if player is not invincible
                int damage = attack - gp.player.defense;//determine how much damage is done to player via attack and defense values
                currentSpirit.setHealth(currentSpirit.getHealth() - damage);//damage player based on calculated value
                gp.player.invincible = true; // the player is now invincible for a given period of time
            }
        }

        // entity can only move if collision is false
        if (!collisionOn) {
            switch (direction) {//check entity direction to determine which direction it moves in
                case "up"://move player up if facing up
                    worldY -= speed;
                    break;
                case "down"://move player down if facing down
                    worldY += speed;
                    break;
                case "left"://move player left if facing left
                    worldX -= speed;
                    break;
                case "right"://move player right if facing right
                    worldX += speed;
                    break;
            }
        }

        //MOVEMENT ANIMATION
        //create the illusion of the entity 'moving' by rapidly switching between images
        spriteCounter++;//update the sprite counter
        if (spriteCounter > 12) {//player image changes once every 12 frames, can adjust by increasing or decreasing
            if (spriteNum == 1) {//changes the player to first walking sprite to second sprite
                spriteNum = 2;
            } else if (spriteNum == 2) {//changes the player sprite from second to first
                spriteNum = 1;
            }
            spriteCounter = 0;//resets the sprite counter
        }
    }

    //DRAW METHOD
    public void draw(Graphics2D g2) {

        BufferedImage image = null;//call on buffered image
        int screenX = worldX - gp.player.worldX + gp.player.screenX;//determine position on the screen
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        //DRAWING CODE FOR ENTITY MOVEMENT
        if (!attacking && !specialAttacking) {//if entity is not attacking, or special attacking, it is moving
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && // only draws an object if it is in the user's field of view
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                switch (direction) {//check the direction, based on the direction it picks a different image
                    case "up"://draw up sprites if facing up
                        if (spriteNum == 1) {
                            image = up1;
                        }
                        if (spriteNum == 2) {
                            image = up2;
                        }
                        break;
                    case "down"://draw down sprites if facing down
                        if (spriteNum == 1) {
                            image = down1;
                        }
                        if (spriteNum == 2) {
                            image = down2;
                        }
                        break;
                    case "left"://draw left sprites if facing left
                        if (spriteNum == 1) {
                            image = left1;
                        }
                        if (spriteNum == 2) {
                            image = left2;
                        }
                        break;
                    case "right"://draw right sprites if facing right
                        if (spriteNum == 1) {
                            image = right1;
                        }
                        if (spriteNum == 2) {
                            image = right2;
                        }
                        break;
                }

                // DISPLAY MONSTER HEALTH BAR
                if (type == 2) { // if the entity is a monster and the health bar should be displayed
                    int oneHealthLength = gp.tileSize / maxHealth; // size of one of a monster's lives
                    double healthBarValue = oneHealthLength * health;//calculate health bar

                    g2.setColor(new Color(35, 35, 35));//set colour to black
                    g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12); // displays the outline above the health bar

                    g2.setColor(new Color(255, 0, 0));//set colour to red
                    g2.fillRect(screenX, screenY - 15, (int) healthBarValue, 10); // subtracts from screenY to display above monster
                    // and draws the rectangle the same size as the monster's health

                    healthBarCounter++;

                    if (healthBarCounter > 600) { // after 10 seconds after its appearance, the monster's health bar disappears
                        healthBarCounter = 0;
                        healthBarOn = false;
                    }
                }
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null); // draws the attacking
                // animation based on image to be printed, position on the screen, and size of the image
            }

            // DEBUGGING
//         g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

    //IMAGE SETUP METHOD
    public BufferedImage setup(String imagePath, double widthScale, double heightScale) {//takes the file path, and
        // the scaling factors
        UtilityTool uTool = new UtilityTool();//call on utility tool class
        BufferedImage image = null;//create buffered image

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/" + imagePath + ".png"));//read the file path based
            // on the inputs
            image = uTool.scaleImage(image, (int) (gp.tileSize * widthScale), (int) (gp.tileSize * heightScale));
            //scale image
        } catch (IOException e) {//in case file path does not exist
            e.printStackTrace();
        }
        return image;
    }
}