package entity;

import main.GamePanel;

import java.util.Random;

public class NPC extends Entity {
    public double hitboxScale = 0.65;
    public NPC(GamePanel gp) {
        super(gp);

        type = 1; // sets this entity's type to an NPC
        direction = "down";
        speed = 2;

        //keep solid area very small in case we decide to turn collisions on for the npc butterflies
        solidArea.width = 1;
        solidArea.height = 1;


        getImage();
    }

    public void getImage() {//summon the butterfly sprite images
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
        Random random = new Random();

        actionLockCounter++;//increase counter every frame
//        Descrition: NPC will move in one direction continously for 30 frames, then randomly pick another
//        direction to travel in
        if (actionLockCounter == 30) {//change movements every 30 frames

            int i = random.nextInt(100) + 1;//pick a randum number from 1 to 100

            if (i <= 25) {//travel up 1/4 of the time
                direction = "up";
            }
            if (i > 25 && i <= 50) {//travel down for 1/4 of the time
                direction = "down";
            }
            if (i > 50 && i <= 75) {//travel left for 1/4 of the time
                direction = "left";
            }
            if (i > 75 && i < 100) {//travel right for 1/4 of the time
                direction = "right";
            }
            actionLockCounter = 0;//reset counter so sprite moves continuously in the direction
        }

    }

}
