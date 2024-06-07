package entity;

import main.GamePanel;

import java.util.Random;

public class NPC extends Entity {
    public NPC(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 2;

        getImage();
    }

    public void getImage() {
        up1 = setup("npc/npc_1");
        up2 = setup("npc/npc_2");
        down1 = setup("npc/npc_3");
        down2 = setup("npc/npc_4");
        left1 = setup("npc/npc_5");
        left2 = setup("npc/npc_6");
        right1 = setup("npc/npc_7");
        right2 = setup("npc/npc_8");
    }

    public void setAction() {
        Random random = new Random();
        actionLockCounter++;
        if (actionLockCounter == 120) {

            int i = random.nextInt(100) + 1;//pick a randum number from 1 to 100

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
            actionLockCounter = 0;
        }

    }

}
