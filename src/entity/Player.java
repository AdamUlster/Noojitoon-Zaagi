package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    KeyHandler keyH;//call on keyhandler class

    public Spirit[] spirits = new Spirit[3];
    public int currentSpiritIndex = 0; // keeps track of the current spirit

    public final int screenX;
    public final int screenY;

    public int numTotems = 0; // keeps track of the number of totems the player has collected

    public Player(GamePanel gp, KeyHandler keyH) { //create default attributes (constructor)

        super(gp); // call on Entity class
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 32, 32); // initiates the rectangle

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();//sets default values for the player
        getPlayerImage();
    }

    public Spirit getCurrentSpirit() { // gets the current spirit
        return spirits[currentSpiritIndex];
    }

    public void setDefaultValues() {//create default values to spawn the player

        worldX = gp.tileSize * 50; // sets the default position x-coordinate
        worldY = gp.tileSize * 50; //sets the default position y-coordinate
        speed = 4;//sets speed to 4
        direction = "right";//can input any direction

        // Initializes the spirits and their health values
        spirits[0] = new Spirit(gp, "Bear");
        spirits[0].maxHealth = 6;
        spirits[0].health = 6;
        spirits[1] = new Spirit(gp, "Eagle");
        spirits[1].maxHealth = 6;
        spirits[1].health = 5;
        spirits[2] = new Spirit(gp, "Turtle");
        spirits[2].maxHealth = 8;
        spirits[2].health = 8;
        switchSpirit(0); // the player is the bear spirit to start
    }

    public void getPlayerImage() {
        Spirit currentSpirit = getCurrentSpirit(); // gets the current spirit
        if (currentSpirit.name.equals("Bear")) { // walking animation for only the bear pngs
            // call on setup method to find image files
            up1 = setup("bear/bear_up", 1);
            up2 = setup("bear/bear_up_2", 1);
            down1 = setup("bear/bear_down", 1);
            down2 = setup("bear/bear_down_2", 1);
            left1 = setup("bear/bear_left", 1);
            left2 = setup("bear/bear_left_2", 1);
            right1 = setup("bear/bear_right", 1);
            right2 = setup("bear/bear_right_2", 1);

            System.out.println("image loading started");
        }
        else if (currentSpirit.name.equals("Eagle")) { // walking animation for only the eagle pngs
            up1 = setup("eagle/eagle_up", 1);
            up2 = setup("eagle/eagle_up_2", 1);
            down1 = setup("eagle/eagle_down", 1);
            down2 = setup("eagle/eagle_down_2", 1);
            left1 = setup("eagle/eagle_left", 1);
            left2 = setup("eagle/eagle_left_2", 1);
            right1 = setup("eagle/eagle_right", 1);
            right2 = setup("eagle/eagle_right_2", 1);
        }
        else if (currentSpirit.name.equals("Turtle")) {
            up1 = setup("turtle/turtle_up", 1.2);
            up2 = setup("turtle/turtle_up_2", 1.2);
            down1 = setup("turtle/turtle_down", 1.2);
            down2 = setup("turtle/turtle_down_2", 1.2);
            left1 = setup("turtle/turtle_left", 1.2);
            left2 = setup("turtle/turtle_left_2", 1.2);
            right1 = setup("turtle/turtle_right", 1.2);
            right2 = setup("turtle/turtle_right_2", 1.2);
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

            // check object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // check npc collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // check monster collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex); // runs when the user makes contact with the monster

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
        else if (keyH.threePressed) {
            switchSpirit(2);
        }

        // Gives the player 1 second of invincibility after making contact with a monster
        if (invincible) {
            invicibilityCounter ++;
            if (invicibilityCounter > 60) {
                invincible = false;
                invicibilityCounter = 0;
            }
        }
    }

    public void switchSpirit(int spiritIndex) {
        currentSpiritIndex = spiritIndex; // sets the current spirit index to the spirit index
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
            System.out.println("you are hitting an npc");
        }
    }

    public void contactMonster(int index) {
        Spirit currentSpirit = gp.player.getCurrentSpirit(); // gets the current spirit

        if (index != 999) { // if index is 999, no monster was touched
            if (invincible == false) {
                currentSpirit.setHealth(currentSpirit.getHealth() - 1);
                invincible = true;
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
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f)); // reduces the opacity to 70% to show when the player is invincible
        }

        g2.drawImage(image, screenX, screenY, null);//draws the image, null means we cannot type

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // resets the opacity for future images
    }
}