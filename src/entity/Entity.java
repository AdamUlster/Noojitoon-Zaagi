package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    protected GamePanel gp;//call on the game panel class

//    DECLARE IMAGE BOOLEANS: BOOLEANS THAT DETERMINE WHICH IMAGE TO DRAW
    public BufferedImage image1, image2, image3;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;//movement image boolenas
    public BufferedImage attackUp1, attackUp2, attackUp3, attackDown1, attackDown2,
            attackDown3, attackLeft1, attackLeft2, attackLeft3, attackRight1,
            attackRight2, attackRight3;//primary attack booleans
    public BufferedImage specialUp1, specialUp2, specialUp3, specialUp4, specialUp5, specialUp6,
            specialUp7;//special moves for up direction
    public BufferedImage specialDown1, specialDown2, specialDown3, specialDown4, specialDown5, specialDown6,
            specialDown7;//special moves for down direction
    public BufferedImage specialLeft1, specialLeft2, specialLeft3, specialLeft4, specialLeft5, specialLeft6,
            specialLeft7;//special moves for left direction
    public BufferedImage specialRight1, specialRight2, specialRight3, specialRight4, specialRight5, specialRight6,
            specialRight7;//special moves for right direction

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
    int healthBarCounter = 0;// entity health

    //STATE
    public int worldX, worldY;//position relative to the world
    public int solidAreaDefaultX, solidAreaDefaultY;//position of entity collision box
    public String direction = "down";//default direction; unless otherwise declared, entity is facing down
    public int spriteNum = 1;//set default player sprite to bear
    public boolean invincible = false; // sets whether the entity is immune to damage
    public boolean collisionOn = false;//sets whether entity is a collidable object, entity starts out as a non
    // collidable object
    boolean attacking = false;//boolean to determine if the player is attacking; entity is not attacking
    boolean specialAttacking = false;//boolean to determine if the entity is using a special attack
    public boolean displayDeathMessage = false; // whether the death message should be displayed
    public boolean alive = true; // keeps track of whether the projectile is alive
    public boolean dead = false; // sets whether the entity is dead
    public boolean isDying = false; // sets whether the entity is dying
    public boolean deadFlicker = false; // sets whether the entity should be flickering dead
    public boolean healthBarOn = false; // whether the monster has a health bar above them
    public boolean onPath = false; // keeps track of whether an entity should be following the best path to get to a certain location

    //CHARACTER ATTRIBUTES
    public int maxHealth; // maximum number of lives the entity has
    public int health; // current number of lives the entity has
    public String name;// entity name
    public boolean collision = false;
    public int type; // 0 = player, 1 = NPC, 2 = monster, 3 = projectile
    public int speed;// entity speed
    public int attack;// attacking value; how much damage an attack should decrease the health by
    public int defense;// resistance to the attack; reduces the theoretical damage taken

//    PROJECTILES
    public Projectile projectile;// import projectile class for the turtle spirit primary fire
    public TargetingProjectile targetProjectile;//import targeting projectile class for eagle special fire

    //ITEM ATTRIBUTES
    public int useCost;

    //creat eentity
    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {
    }

    public void damageReaction() {} // controls the monster's reaction to taking damage. This method is modified in the individual monster classes

    public void checkCollision() { // checks the entity collision with other tiles and other entities
        Spirit currentSpirit = gp.player.getCurrentSpirit(); // gets the current spirit

        collisionOn = false;//turn entity into a non collidable object
        gp.cChecker.checkTile(this);//  check if entity is touching a tile
        gp.cChecker.checkObject(this, false);//check i entity is touching an object
        gp.cChecker.checkEntity(this, gp.npc);//check if entity is touching a npc, applies to non npc entities
        gp.cChecker.checkEntity(this, gp.monster);
        //check if entity is touching a monster, applies to non monster entities

        boolean contactPlayer = gp.cChecker.checkPlayer(this);
        //check it entity is touching a player, applies to non-player entities

        if (this.type == 2 && contactPlayer) { // if this class is a monster and the monster has made contact with the player
            if (!gp.player.invincible) {//check if player is not invincible
                int damage = attack - gp.player.defense;//determine how much damage should be dealt to the player via
                // attack and defense values

                currentSpirit.setHealth(currentSpirit.getHealth() - damage);//damage player based on calculated value
                gp.player.invincible = true; // the player is now invincible for a given period of time
            }
        }
    }

//    UPDATE METHOD THAT RUNS ON EACH FRAME
    public void update() {
        setAction();
        checkCollision();//call on check collision method

//        USE SPEED VALUE TO MOVE ENTITY ACROSS WORLD COORDINATES
        if (!collisionOn) {// entity can only move if collision is false
            switch (direction) {//run thorugh entity direction to determine which direction it moves in
                case "up":// MOVE PLAYER UP IF FACING UP
                    worldY -= speed;
                    break;
                case "down":// MOVE PLAYER DOWN IF FACING DOWN
                    worldY += speed;
                    break;
                case "left"://MOVE PLAYER LEFT IF FACING LEFT
                    worldX -= speed;
                    break;
                case "right":// MOVE PLAYER RIGHT IF FACING RIGHT
                    worldX += speed;
                    break;
            }
        }

//        MOVEMENT ANIMATION
//        CREATE THE ILLUSION OF THE ENTITY 'MOVING' BY RAPIDLY SWITCHING BETWEEN TWO IMAGES
        spriteCounter++;//UPDATE THE SPRITE COUNTER UNTIL IT REACHES 12
        if (spriteCounter > 12) {// PLAYER IMAGE CHANGES ONCE EVERY 12 FRAMES, INCREASING THIS NUMBER MEANS ENTITY'
            // WALKS SLOW, DECREASING THIS NUMBER MAKES THE ENTITY 'WALK' FASTER
            if (spriteNum == 1) {// CHANGES THE PLAYER FROM THE FIRST WALKING SPRITE TO THE SECOND SPRITE
                spriteNum = 2;
            } else if (spriteNum == 2) {// CHANGES THE PLAYER FROM THE SECOND WALKING SPRITE TO THE FIRST SPRITE
                spriteNum = 1;
            }
            spriteCounter = 0;// RESET THE SPRITE COUNTER SO THE ANIMATION GOES BACK AND FORTH
        }
    }

//    DRAW METHOD
    public void draw(Graphics2D g2) {

        BufferedImage image = null;// CALL ON BUFFERED IMAGE CLASS

//        DETERMINE POSITION ON THE SCREEN
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (!attacking && !specialAttacking) {// IF THE ENTITY IS NOT ATTACKING, OR SPECIAL ATTACKING, IT IS MOVING
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {// ONLY DRAWS AN OBJECT IT IS IN USER FOV
                switch (direction) {//check the direction, based on the direction it picks a different image
                    case "up":// DRAW UP SPRITES IF FACING UP
                        if (spriteNum == 1) {image = up1;}
                        if (spriteNum == 2) {image = up2;}
                        break;
                    case "down"://DRAW DOWN SPRITES IF FACING DOWN
                        if (spriteNum == 1) {image = down1;}
                        if (spriteNum == 2) {image = down2;}
                        break;
                    case "left":// DRAW LEFT SPRITES IF FACING LEFT
                        if (spriteNum == 1) {image = left1;}
                        if (spriteNum == 2) {image = left2;}
                        break;
                    case "right":// DRAW RIGHT SPRITES IF FACING RIGHT
                        if (spriteNum == 1) {image = right1;}
                        if (spriteNum == 2) {image = right2;}
                        break;
                }

                // MONSTER HEALTH BAR
                if (type == 2) { // IF THE ENTITY IS A MONSTER THEN DISPLAY ITS HEALTH BAR
                    double oneHealthLength = gp.tileSize / maxHealth; // SIZE OF THE BAR REPRESENTING MONSTER'S HEALTH
                    double healthBarValue = oneHealthLength * health;// CALCULATE HOW MUCH HEALTH MONSTER HAS

                    g2.setColor(new Color(35, 35, 35));//SET COLOUR TO BLACK
                    //DISPLAY HOW MUCH HEALTH IS MISSING
                    g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

                    g2.setColor(new Color(255, 0, 0));//SET COLOUR TO RED
//                    SUBTRACS FROM SCREENY TO DISPLAY ABOVE MONSTER AND DRAW RECTANGLE THE SAME SIZE AS MONSTER HEALTH
                    g2.fillRect(screenX, screenY - 15, (int) healthBarValue, 10);

                    healthBarCounter++;// HEALTH BAR DRAWING COUNTER

                    // AFTER 10 SECONDS AFTER APPEARING MONSTER HEALTH BAR DISAPPEARS
                    if (healthBarCounter > 600) {
                        healthBarCounter = 0;
                        healthBarOn = false;
                    }
                }
//                DRAW ATTACKING ANIMATION SPRITE BASED ON IMAGE TO BE PRINTED, POSITION ON THE SCREEN, AND SIZE OF
//                THE IMAGE
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            // DEBUGGING; DRAW ENTITY COLLISION BOX
//           g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

//    IMAGE SET UP METHOD
    public BufferedImage setup(String imagePath, double widthScale, double heightScale) {// ACCEPTS IMAGE FILE PATH
        // AND SCALING FACTOR ON BOTH WIDTH AND HEIGHT
        UtilityTool uTool = new UtilityTool();//CLALL ON UTILITY TOOL CLASS
        BufferedImage image = null;//CREATE NEW BUFFERED IMAGE TO BE RETURNED

        try {
            //READ FILE PATH FROM RESOURCE FOLDER FROM INPUTS
            image = ImageIO.read(getClass().getResourceAsStream("/" + imagePath + ".png"));
            //USE UTILITY TOOL TO SCALE IMAGE TO SIZE
            image = uTool.scaleImage(image, (int) (gp.tileSize * widthScale), (int) (gp.tileSize * heightScale));
        } catch (IOException e) {// TRY CATCH IN CASE FILE PATH DOES NOT WORK
            e.printStackTrace();
        }
        return image;
    }

//    PATH FINDING METHOD
    public void searchPathToTotem(int goalCol, int goalRow) {//ACCEPTS COORDINATE INPUTS OF THE END GOAL POSITION

//        GET ENTITY'S CURRENT TILE
        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

//        CREATE NODES USING STARTING AND ENDING POINTS BY CALLING ON P-FINDER CLASS
        gp.pFinderToTotem.setNodes(startCol, startRow, goalCol, goalRow);

        if (gp.pFinderToTotem.search()) { // A PATH HAS BEEN FOUND

//            GET THE ENTITY'S NEXT COORDINATES USING THE PATH TO FOLLOW
            int nextX = gp.pFinderToTotem.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinderToTotem.pathList.get(0).row * gp.tileSize;

//            GET ENTITY'S SOLID AREA
            int entLeftX = worldX + solidArea.x;
            int entRightX = worldX + solidArea.x + solidArea.width;
            int entTopY = worldY + solidArea.y;
            int entBottomY = worldY + solidArea.y + solidArea.height;

//            IMPLEMENTING THE PATHFINDING ALGORITHM
            if (entTopY > nextY && entLeftX >= nextX && entRightX < nextX + gp.tileSize) {
                direction = "up";
            }
            else if (entTopY < nextY && entLeftX >= nextX && entRightX < nextX + gp.tileSize) {
                direction = "down";
            }
            else if (entTopY >= nextY && entBottomY < nextY + gp.tileSize) { // THE ENTITY CAN GO EITHER LEFT OR RIGHT
                if (entLeftX > nextX) {
                    direction = "left";
                }
                if (entLeftX < nextX) {
                    direction = "right";
                }
            }
            else if (entTopY > nextY && entLeftX > nextX) { // ENTITY CAN GO EITHER UP OR LEFT
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            }
            else if (entTopY > nextY && entLeftX < nextX) { // THE ENTITY CAN GO EITHER UP OR RIGHT
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }
            else if (entTopY < nextY && entLeftX > nextX) { // ENTITY CAN EITHER GO UP OR DOWN
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            }
            else if (entTopY < nextY && entLeftX < nextX) { // ENTITY CAN GO EITHER DOWN OR RIGHT
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }

            int nextCol = gp.pFinderToTotem.pathList.get(0).col;
            int nexRow = gp.pFinderToTotem.pathList.get(0).row;
            if (nextCol == goalCol && nexRow == goalRow) {
                onPath = false; // STOP ENTITY WHEN IT REACHING THE DESTINATION
            }
        }
    }

    public void searchPathToPlayer(int goalCol, int goalRow) {
        // GET ENTITY CURRENT TILE
        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pFinderToPlayer.setNodes(startCol, startRow, goalCol, goalRow);

        if (gp.pFinderToPlayer.search()) { // PATH HAS BEEN FOUND

//            GET ENTITY'S NEXT COORDINATES USING THE PATH TO FOLLOW
            int nextX = gp.pFinderToPlayer.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinderToPlayer.pathList.get(0).row * gp.tileSize;

//            GET ENTITY'S SOLID AREA
            int entLeftX = worldX + solidArea.x;
            int entRightX = worldX + solidArea.x + solidArea.width;
            int entTopY = worldY + solidArea.y;
            int entBottomY = worldY + solidArea.y + solidArea.height;

            if (entTopY > nextY && entLeftX >= nextX && entRightX < nextX + gp.tileSize) {
                direction = "up";
            }
            else if (entTopY < nextY && entLeftX >= nextX && entRightX < nextX + gp.tileSize) {
                direction = "down";
            }
            else if (entTopY >= nextY && entBottomY < nextY + gp.tileSize) { //ENTITY CAN GO EITHER LEFT OR RIGHT
                if (entLeftX > nextX) {
                    direction = "left";
                }
                if (entLeftX < nextX) {
                    direction = "right";
                }
            }
            else if (entTopY > nextY && entLeftX > nextX) { // ENTITY CAN GO EITHER UP OR LEFT
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            }
            else if (entTopY > nextY && entLeftX < nextX) { // ENTITY CAN GO EITHER UP OR RIGHT
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }
            else if (entTopY < nextY && entLeftX > nextX) { // ENTITY CAN GO EITHER DOWN OR LEFT
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            }
            else if (entTopY < nextY && entLeftX < nextX) { // ENTITY CAN GO EITHER DOWN OR RIGHT
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }

            int nextCol = gp.pFinderToPlayer.pathList.get(0).col;
            int nexRow = gp.pFinderToPlayer.pathList.get(0).row;
            if (nextCol == goalCol && nexRow == goalRow) {
                onPath = false; // STOP ENTITY WHEN IT REACHES ITS DESTINATION
            }
        }
    }
}