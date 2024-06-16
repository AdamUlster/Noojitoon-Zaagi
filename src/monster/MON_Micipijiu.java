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

//BEHAVIOR AND MOVEMENT METHOD
    public void setAction() {

//        RANDOM MONSTER BEHAVIOR
        Random random = new Random();
        actionLockCounter++;// INCREASES MOVEMENT COUNTER SO MONSTER ONLY MOVES EVERY 120 FRAMES
        if (actionLockCounter == 120) {//IF 120 FRAMES HAVE PASSED PICK A NEW DIRECTION AT RANDOM

            int i = random.nextInt(100) + 1;//PICKS A RANDOM NUMBER BETWEEN 1 AND 100

            //RANDOMLY CHOOSES A DIRECTION TO TRAVEL IN
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
            actionLockCounter = 0;//RESET MOVEMENT COUNTER AFTER PICKING A NEW DIRECTION AT RANDOM
        }
    }
}
