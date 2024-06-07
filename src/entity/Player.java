package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    KeyHandler keyH;//call on keyhandler class

    public boolean[] activeSpirit = new boolean[3];// boolean values that determine which spirit is currently being used
    public int previousSpirit;// sets the default spirit to spirit #1, spirit bear

    public final int screenX;
    public final int screenY;

    public int numTotems = 0; // keeps track of the number of totems the player has collected

    public Player(GamePanel gp, KeyHandler keyH) { //create default attributes (constructor)
      
        super(gp);//call on GamePanel class
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 32, 32); // initiates the rectangle

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();//sets default values for the player
        getPlayerImage();
    }

    public void setDefaultValues() {//create default values to spawn the player
      
        worldX = gp.tileSize * 50; // sets the default position x-coordinate
        worldY = gp.tileSize * 50; //sets the default position y-coordinate
        speed = 4;//sets speed to 4

        direction = "right";//can input any direction
        activeSpirit[0] = true;//starts the player with the bear spirit

        // Default health values for each spirit
        maxHealth[0] = 6; // bear health
        health[0] = maxHealth[0]; // sets the current number of lives for the bear
        maxHealth[1] = 6; // eagle health
        health[1] = 5; // sets the current number of lives for the eagle
    }

    public void getPlayerImage() {
        if (activeSpirit[0]) {//walking animation for only the bear pngs
            //call on setup method to find image files
            up1 = setup("bear/bear_up");
            up2 = setup("bear/bear_up_2");
            down1 = setup("bear/bear_down");
            down2 = setup("bear/bear_down_2");
            left1 = setup("bear/bear_left");
            left2 = setup("bear/bear_left_2");
            right1 = setup("bear/bear_right");
            right2 = setup("bear/bear_right_2");

            System.out.println("image loading started");
        }
        if (activeSpirit[1]) {//walking animation for only the eagle pngs
            up1 = setup("eagle/eagle_up");
            up2 = setup("eagle/eagle_up_2");
            down1 = setup("eagle/eagle_down");
            down2 = setup("eagle/eagle_down_2");
            left1 = setup("eagle/eagle_left");
            left2 = setup("eagle/eagle_left_2");
            right1 = setup("eagle/eagle_right");
            right2 = setup("eagle/eagle_right_2");
        }
        System.out.println("new sprite loaded");
    }


    public void update() {

        if (keyH.upPressed == true || keyH.downPressed == true ||
                keyH.leftPressed == true || keyH.rightPressed == true) {//walking animations only occur if the key is being pressed
            if (keyH.upPressed == true) {//move up
                direction = "up";
            } else if (keyH.downPressed == true) {//move down
                direction = "down";
            } else if (keyH.leftPressed == true) {//move left
                //remove the else portion to make x and y movements independent
                direction = "left";
            } else if (keyH.rightPressed == true) {//move right
                direction = "right";
            }

            // check tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //check object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //check npc /monster? collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // player can only move if collision is false
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
        if (keyH.onePressed) {
            switchSpirit(0); // switches to the bear
        } else if (keyH.twoPressed) {
            switchSpirit(1); // switches to the eagle
        }
    }

    public void switchSpirit(int spirit) {
        activeSpirit[spirit] = true; // set the new spirit to bear
        activeSpirit[previousSpirit] = false; // the previous spirit is no longer active
        previousSpirit = spirit; // reset the previous spirit
        getPlayerImage(); // reset the image pulls via getPlayerImage method
    }

    public void pickUpObject(int index) {
        if (index != 999) { // if index is 999, no index was touched
            String objectName = gp.obj[index].name;

            switch (objectName) {
                case "Totem":
                    numTotems++; // increases the number of totems the user has collected
                    gp.obj[index] = null; // removes the object
                    gp.ui.showMessage("You picked up a totem!");
                    break;
            }
        }
    }

    public void interactNPC(int i) {
        if (i != 999) {
//            gp.ui.showMessage("You insensitive schmuck! You are hitting an NPC!");
            System.out.println("you are hitting an npc");
        }
    }

    public void draw(Graphics2D g2) {
        //g2.setColor(Color.white);//set colour of the painter to white
        //
        //g2.fillRect(x, y, gp.tileSize, gp.tileSize);//create and fill something

        BufferedImage image = null;
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
        g2.drawImage(image, screenX, screenY, null);//draws the image, null means we cannot type
    }
}