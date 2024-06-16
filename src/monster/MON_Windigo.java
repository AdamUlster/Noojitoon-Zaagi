package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_Windigo extends Entity {

    public double hitboxScale = 0.75;//SCALING FACTOR FOR HITBOX

    public MON_Windigo(GamePanel gp) {
        super(gp);

        type = 2; // SET ENTITY TYPE TO MONSTER
        name = "Windigo";
        speed = 3;//TRAVELS AT 3 PIXELS PER FRAME
        maxHealth = 4;//SET MAXIMUM HEALTH
        health = maxHealth;//SET CURRENT HEALTH TO FULL
        attack = 5;//HOW MUCH MONSTER CONTACT WILL DAMAGE THE PLAYER FOR
        defense = 0;

//        SETS THE COLLISION BOX FOR THE MONSTER
        solidArea.width = (int)(gp.tileSize * hitboxScale);
        solidArea.height = (int)(gp.tileSize * hitboxScale);
        solidArea.x = (gp.tileSize - solidArea.width) /2;
        solidArea.y = (gp.tileSize - solidArea.height) / 2;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();//RETRIEVES ALL THE SPIRTE IMAGE FILES
    }

//    METHOD FOR RECALLING ALL THE SPRITE IMAGE FILES FROM FILE PATH VIA SETUP METHOD IN ENTITY CLASS
    public void getImage() {
        up1 = setup("monsters/windigo", 1,1);
        up2 = setup("monsters/windigo", 1,1);
        down1 = setup("monsters/windigo", 1,1);
        down2 = setup("monsters/windigo", 1,1);
        left1 = setup("monsters/windigo", 1,1);
        left2 = setup("monsters/windigo", 1,1);
        right1 = setup("monsters/windigo", 1,1);
        right2 = setup("monsters/windigo", 1,1);
    }

//    OVERWRITES THE PARENT CLASS'S UPDATE METHOD
    public void update() {
//        CALL ON PARENT CLASS UPDATE METHOD
        super.update();

//        CALCULATE DISTANCE FROM THE PLAYER
        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistance = (xDistance + yDistance) / gp.tileSize;

//        CHECK IF THE PLAYER IS IN AGGRO MODE AND IS WITHIN FIVE TILES OF THE PLAYER
        if (!onPath && tileDistance < 5) {
            int i = new Random().nextInt(100) + 1; // PICK A RANDOM NUMBER FROM 1 TO 100
            if (i > 50) {
                onPath = true; // HALF THE TIME IT DOESN'T FOLLOW THE PLAYER
            }
        }
//        TURNS OFF AGGRO ONCE THE PLAYER IS A CERTAIN DISTANCE AWAY
        if (onPath && tileDistance > 15) {
            onPath = false;
        }
    }

    public void setAction() {
//CHECK IF AGGRO IS ON
        if (onPath) {
//            CALCULATE THE TARGET POSITION OF THE PLAYER
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
            searchPathToPlayer(goalCol, goalRow);//USE PATH FINDING METHOD TO GET PATH TO THE PLAYER
        }
        else {// OTHERWISE, PERFORM RANDOM MONSTER BEHAVIOR
            actionLockCounter++;// INCREASE MOVEMENT COUNTER SO MOVEMENT ONLY OCCURS EVERY 120 FRAMES
            if (actionLockCounter == 120) {//CHANGE DIRECTION AFTER 120 FRAMES

                Random random = new Random();
                int i = random.nextInt(100) + 1;// PICK A RANDOM NUMBER FROM 1 TO 100

//                PICKS A RANDOM DIRECTION BASED ON THE RANDOM NUMBER
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
                actionLockCounter = 0;//RESET THE MOVEMENT COUNTER AFTER RANDOMLY PICKING A DIRECTION
            }
        }
    }

//    TURNS ON AGGRO IF THE PLAYER HAS JUST BEEN DAMAGED BY THE PLAYER
    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
    }
}
