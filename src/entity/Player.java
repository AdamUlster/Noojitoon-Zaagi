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

    //scaling factors for hitboxes
    public double bearHitboxScale = 0.75;//bear hit box scale
    public double eagleHitboxScale = 0.75;//eagle hit box scale
    public double turtleHitboxScale = 1;//turtle hit box scale

    public Player(GamePanel gp, KeyHandler keyH) { //create default attributes (constructor)

        super(gp); // call on Entity class
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

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
        spirits[0] = new Spirit(gp, "Bear", 6, 6, (int)(gp.tileSize * (1.0-bearHitboxScale))/2,
                (int)(gp.tileSize * (1.0-bearHitboxScale))/2, (int)(gp.tileSize * bearHitboxScale),
                (int)(gp.tileSize * bearHitboxScale));
        spirits[1] = new Spirit(gp, "Eagle", 6, 5, (int)(gp.tileSize * (1.0 - eagleHitboxScale)/2),
                (int)(gp.tileSize * (1.0 - eagleHitboxScale)/2), (int)(gp.tileSize * eagleHitboxScale),
                (int)(gp.tileSize * eagleHitboxScale));
        spirits[2] = new Spirit(gp, "Turtle", 8, 8, (int)(gp.tileSize * (1.0 - turtleHitboxScale)/2) + 40,
                (int)(gp.tileSize * (1.0 - turtleHitboxScale)/2) + 40, (int)(gp.tileSize * turtleHitboxScale),
                (int)(gp.tileSize * turtleHitboxScale));
        switchSpirit(0); // the player is the bear spirit to start
    }

    public void getPlayerImage() {
        Spirit currentSpirit = getCurrentSpirit(); // gets the current spirit

        // Sets the player's images to the current spirit's images
        up1 = currentSpirit.up1;
        up2 = currentSpirit.up2;
        down1 = currentSpirit.down1;
        down2 = currentSpirit.down2;
        left1 = currentSpirit.left1;
        left2 = currentSpirit.left2;
        right1 = currentSpirit.right1;
        right2 = currentSpirit.right2;

        System.out.println("new sprite loaded");
    }


    public void update() {

        if (keyH.upPressed || keyH.downPressed ||
                keyH.leftPressed || keyH.rightPressed) {//walking animations only occur if the key is being pressed
            if (keyH.upPressed) {//move up
                direction = "up";
            } else if (keyH.downPressed) { // move down
                direction = "down";
            } else if (keyH.leftPressed) { // move left
                //remove the else portion to make x and y movements independent
                direction = "left";
            } else if (keyH.rightPressed) { // move right
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
        } else if (keyH.threePressed) {
            switchSpirit(2);
        }

        // Gives the player 1 second of invincibility after making contact with a monster
        if (invincible) {
            invincibilityCounter ++;
            if (invincibilityCounter > 60) {
                invincible = false;
                invincibilityCounter = 0;
            }
        }

        if (gp.player.getCurrentSpirit().health <= 0) {
            displayDeathMessage = false;
            gp.player.getCurrentSpirit().dead = true;
            deadCounter++;
            if (deadCounter <= 240) {
                if (deadCounter % 30 == 0) {
                    if (deadFlicker) {
                        deadFlicker = false;
                    } else {
                        deadFlicker = true;
                    }
                }
            }
            else {
                displayDeathMessage = true; // display the death message
                deadFlicker = false;
                deadCounter = 0;
                int spiritIndex = nextAliveSpirit(); // gets the next alive spirit, returns -1 otherwise
                switchSpirit(spiritIndex);
            }
        }
    }

    public void switchSpirit(int spiritIndex) {
        currentSpiritIndex = spiritIndex; // sets the current spirit index to the spirit index
        getPlayerImage(); // reset the image pulls via getPlayerImage method

        // sets the player's hit box to the current spirit's hit box
        this.solidArea.x = getCurrentSpirit().solidArea.x;
        this.solidArea.y = getCurrentSpirit().solidArea.y;
        this.solidArea.width = getCurrentSpirit().solidArea.width;
        this.solidArea.height = getCurrentSpirit().solidArea.height;
        this.solidAreaDefaultX = getCurrentSpirit().x;
        this.solidAreaDefaultY = getCurrentSpirit().y;
    }

    public int nextAliveSpirit() {
        for (int i = currentSpiritIndex + 1; i < currentSpiritIndex + gp.player.spirits.length; i++) {
            int loopIndex = i % gp.player.spirits.length; // Calculates the loop index
            if (!gp.player.spirits[loopIndex].dead) {
                return loopIndex;
            }
        }
        return -1;
    }

    public void pickUpObject(int index) {
        if (index != 999) { // if index is 999, no object was touched
            String objectName = gp.obj[index].name;
            if (objectName.equals("Totem")) {
                numTotems++; // increases the number of totems the user has collected
                gp.obj[index] = null; // removes the object
                gp.ui.showMessage("You picked up a totem!");
            }
        }
    }

    public void interactNPC(int i) {
        if (i != 999) {
            System.out.println("you are hitting an npc");
        }
    }

    public void contactMonster(int index) { // modifies the player's invincibility if they make contact with a monster
        Spirit currentSpirit = gp.player.getCurrentSpirit(); // gets the current spirit

        if (index != 999) { // if index is 999, no monster was touched
            if (!invincible) {
                currentSpirit.setHealth(currentSpirit.getHealth() - 1);
                invincible = true;
            }
        }
    }

    public void draw(Graphics2D g2) {
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
        if ((invincible && !gp.player.getCurrentSpirit().dead) || deadFlicker) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f)); // reduces the opacity to 70% to show when the player is invincible
        }
        else {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        g2.drawImage(image, screenX, screenY, null);//draws the image, null means we cannot type

        // Temporary
        Spirit currentSpirit = gp.player.getCurrentSpirit();
        g2.fillRect(screenX + currentSpirit.solidArea.x, screenY + currentSpirit.solidArea.y, currentSpirit.solidArea.width, currentSpirit.solidArea.height);
        //

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // resets the opacity for future images
    }
}