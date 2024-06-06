package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gp;//call on the gamepanel class
    KeyHandler keyH;//call on keyhandler class

    public boolean[] activeSpirit = new boolean[3];//boolean values that determine which spirit is currently being used
    public int previousSpirit;//sets the default spirit to spirit #1, spirit bear

    public final int screenX;
    public final int screenY;
    int numTotems = 0; // keeps track of the number of totems the player has collected

    public Player(GamePanel gp, KeyHandler keyH) { //create default attributes (constructor)
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 32, 32); // instantiates the rectangle

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();//sets default values for the player
        getPlayerImage();
    }

    public void setDefaultValues() {//create default values to spawn the player
        worldX = gp.tileSize * 3; // sets the default tile position x-coordinate
        worldY = gp.tileSize * 3; //sets the default tile position y-coordinate
        speed = 4;//sets speed to 4
        direction = "right";//can input any direction
        activeSpirit[0] = true;//starts the player with the bear spirit
    }

    public void getPlayerImage() {
        System.out.println("image loading started");
        try {
            if (activeSpirit[0]) {//walking animation for only the bear pngs
                //read through the res file and find the images
                up1 = ImageIO.read(getClass().getResourceAsStream("/bear/bear_up.png"));
                up2 = ImageIO.read(getClass().getResourceAsStream("/bear/bear_up_2.png"));
                down1 = ImageIO.read(getClass().getResourceAsStream("/bear/bear_down.png"));
                down2 = ImageIO.read(getClass().getResourceAsStream("/bear/bear_down_2.png"));
                left1 = ImageIO.read(getClass().getResourceAsStream("/bear/bear_left.png"));
                left2 = ImageIO.read(getClass().getResourceAsStream("/bear/bear_left_2.png"));
                right1 = ImageIO.read(getClass().getResourceAsStream("/bear/bear_right.png"));
                right2 = ImageIO.read(getClass().getResourceAsStream("/bear/bear_right_2.png"));
            }
            if (activeSpirit[1]) {//walking animation for only the eagle pngs
                up1 = ImageIO.read(getClass().getResourceAsStream("/eagle/eagle_up.png"));
                up2 = ImageIO.read(getClass().getResourceAsStream("/eagle/eagle_up_2.png"));
                down1 = ImageIO.read(getClass().getResourceAsStream("/eagle/eagle_down.png"));
                down2 = ImageIO.read(getClass().getResourceAsStream("/eagle/eagle_down_2.png"));
                left1 = ImageIO.read(getClass().getResourceAsStream("/eagle/eagle_left.png"));
                left2 = ImageIO.read(getClass().getResourceAsStream("/eagle/eagle_left_2.png"));
                right1 = ImageIO.read(getClass().getResourceAsStream("/eagle/eagle_right.png"));
                right2 = ImageIO.read(getClass().getResourceAsStream("/eagle/eagle_right_2.png"));
            }
//            up1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_up.png"));
//            up2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_up_2.png"));
//            down1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_down.png"));
//            down2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_down_2.png"));
//            left1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_left.png"));
//            left2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_left_2.png"));
//            right1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_right.png"));
//            right2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other " +
//
//                                                                                         "things\\my2dGame\\res\\player\\bear_right_2.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Image loading complete");
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
            System.out.println("key 1 active");
            activeSpirit[0] = true;//set the new spirit to bear
            activeSpirit[previousSpirit] = false;//the previous spirit is no longer active
            previousSpirit = 0;//reset the previous spirit
            getPlayerImage();//reset the image pulls via playerimage method
        } else if (keyH.twoPressed) {
            System.out.println("Key 2 active");
            activeSpirit[1] = true;//sets the new spirit to eagle
            activeSpirit[previousSpirit] = false;//sets the previous spirit to false
            previousSpirit = 1;//reset the previous spirit
            getPlayerImage();//resets the image pulls via playerimage method
        }
    }

    public void pickUpObject(int index) {
        if (index != 999) { // if index is 999, no index was touched
            String objectName = gp.obj[index].name;

            switch (objectName) {
                case "Totem":
                    numTotems ++; // increases the number of totems the user has collected
                    gp.obj[index] = null; // removes the object
                    System.out.println("Number of totems: " + numTotems);
                    break;
            }
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
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);//draws the image, null means we cannot type
    }
}