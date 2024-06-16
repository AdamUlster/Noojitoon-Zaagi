package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_Micipijiu extends Entity {

//    SCALING FACTOR FOR HIT BOX WIDTH AND HEIGHT
    public double widthHitboxScale = 0.70;
    public double heightHitboxScale = 0.35;

//    CONSTRUCTOR METHOD
    public MON_Micipijiu(GamePanel gp) {
        super(gp);// CALL ON GAME PANEL CLASS

        type = 2; // SET ENTITY TYPE TO MONSTER
        name = "Micipijiu";
        speed = 3;// SPEED IS 3 PIXELS PER FRAME
        maxHealth = 4;//SET MAXIMUM HEALTH
        health = maxHealth;//SET CURRENT HEALTH TO FULL
        attack = 5;//ATTACK VALUE THAT WILL DEAL DAMAGE PLAYER
        defense = 0;

//        SET COLLISION BOX FOR THE MONSTER IN THE CENTER
        solidArea.width = (int)(gp.tileSize * widthHitboxScale);
        solidArea.height = (int)(gp.tileSize * heightHitboxScale);
        solidArea.x = (gp.tileSize - solidArea.width) /2;
        solidArea.y = (gp.tileSize - solidArea.height) /2;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();//RETRIEVE SPRITE IMAGES
    }

//    USE SETUP METHOD TO RETRIEVE IMAGES FROM FILE PATH
    public void getImage() {
        up1 = setup("monsters/micipijiu_right", 1,1);
        up2 = setup("monsters/micipijiu_right", 1,1);
        down1 = setup("monsters/micipijiu_right", 1,1);
        down2 = setup("monsters/micipijiu_right", 1,1);
        left1 = setup("monsters/micipijiu_left", 1,1);
        left2 = setup("monsters/micipijiu_left", 1,1);
        right1 = setup("monsters/micipijiu_right", 1,1);
        right2 = setup("monsters/micipijiu_right", 1,1);
    }



    public void update() { // overwrites the parent class's update method
        super.update(); // calls on the parent's class update method

        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistance = (int)Math.sqrt(Math.pow(xDistance, 2.0) + Math.pow(yDistance, 2)) / gp.tileSize;

        if (!onPath && tileDistance < 4) { // if the monster is within 4 tiles of the player
            int i = new Random().nextInt(100) + 1; // picks a random number from 1 to 100
            if (i > 50) {
                onPath = true; // half the time, it doesn't follow the player
            } else {
                onPath = false;
            }
        }
        if (onPath && tileDistance > 15) { // makes the monsters disappear once the player is a certain distance away
            onPath = false;
        }
    }

//    MONSTER BEHAVIOR AND MOVEMENT
    public void setAction() {

//        ACTIVELY FOLLOWS PLAYER IF AGGRO IS ON
        if (onPath) {
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
            searchPathToPlayer(goalCol, goalRow);
        }
        else {

//            RANDOM BEHAVIOR
            actionLockCounter++;
            if (actionLockCounter == 120) {//CHANGE DIRECTION EVERY 2 SECONDS

                Random random = new Random();
                int i = random.nextInt(100) + 1;//PICKS A RANDOM NUMBER BETWEEN 1 AND 100

//                SELECT A RANDOM DIRECTION TO TRAVEL IN
                if (i <= 25) {
                    direction = "up";
                }
                if (i > 25 && i <= 50) {
                    direction = "down";
                }
                if (i > 50 && i <= 75) {
                    direction = "left";
                }
                if (i > 75 && i < 100) {
                    direction = "right";
                }
                actionLockCounter = 0;//RESET COUNTER SO MONSTER TRAVELS IN THAT DIRECTION FOR 2 SECONDS
            }
        }
    }

//    TURNS ON AGGRO IF MONSTER HAS BEEN DAMAGED
    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
    }
}
