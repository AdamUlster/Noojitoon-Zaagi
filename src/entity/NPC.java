package entity;

import main.GamePanel;

import java.util.Random;

public class NPC extends Entity {
    public double hitboxScale = 0.65;// HITBOX SCALING FACTOR

    public NPC(GamePanel gp) {//DEFAULT NPC CONSTRUCTOR
        super(gp);//CALL ON GAMEPANEL CLASS

        type = 1; // SET ENTITY TYPE TO NPC
        direction = "down";// SET DEFAULT DIRECTION TO DOWN
        speed = 2;//SET SPEED TO TWO PIXELS PER FRAME

//        KEEP SOLID AREA VERY SMALL N CASE WE DECIDE TO TURN COLLISIONS ON FOR THE NPC BUTTERFLIES
        solidArea.width = 1;
        solidArea.height = 1;

        getImage();// CALL ON GET IMAGE CLASS TO READ AND RETRIEVE THE SPRITE IMAGES
    }

    public void getImage() {//read and retrieve butterfly sprite images
        up1 = setup("npc/butterfly_1", 0.75,0.75);
        up2 = setup("npc/butterfly_2", 0.75,0.75);
        down1 = setup("npc/butterfly_1", 0.75,0.75);
        down2 = setup("npc/butterfly_2", 0.75,0.75);
        left1 = setup("npc/butterfly_1", 0.75,0.75);
        left2 = setup("npc/butterfly_2", 0.75,0.75);
        right1 = setup("npc/butterfly_1", 0.75,0.75);
        right2 = setup("npc/butterfly_2", 0.75,0.75);
    }

//    MOVEMENT
    public void setAction() {
        Random random = new Random();// CREATE RANDOMIZER
        actionLockCounter++;//INCREASE COUNTER WITH EACH FRAME
        if (actionLockCounter == 30) {// NPC WILL MOVE CONTINUOUSLY IN ONE DIRECTION FOR 30 FRAMES, THEN RANDOMLY PICK
            // ANOTHER DIRECTION TO TRAVEL IN

            int i = random.nextInt(100) + 1;//PICKS A RANDOM NUMBER BETWEEN 1 AND 100

            if (i <= 25) {//TRAVEL UP 1/4 OF THE TIME
                direction = "up";
            }
            if (i > 25 && i <= 50) {//TRAVEL DOWN 1/4 OF THE TIME
                direction = "down";
            }
            if (i > 50 && i <= 75) {//TRAVEL LEFT 1/4 OF THE TIME
                direction = "left";
            }
            if (i > 75 && i < 100) {//TRAVEL RIGHT 1/4 OF THE IME
                direction = "right";
            }
            actionLockCounter = 0;//RESET COUNTER
        }
    }
}
