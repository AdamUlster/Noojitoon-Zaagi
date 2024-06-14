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

        solidArea.width = 0;
        solidArea.height = 0;
//        solidArea.width = (int)(gp.tileSize * hitboxScale);
//        solidArea.height = (int)(gp.tileSize * hitboxScale);
//        solidArea.x = (gp.tileSize - solidArea.width) / 2;
//        solidArea.y = (gp.tileSize - solidArea.height) / 2;
//
//        solidAreaDefaultX = solidArea.x;
//        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1 = setup("npc/butterfly_1", 0.75,0.75);
        up2 = setup("npc/butterfly_2", 0.75,0.75);
        down1 = setup("npc/butterfly_1", 0.75,0.75);
        down2 = setup("npc/butterfly_2", 0.75,0.75);
        left1 = setup("npc/butterfly_1", 0.75,0.75);
        left2 = setup("npc/butterfly_2", 0.75,0.75);
        right1 = setup("npc/butterfly_1", 0.75,0.75);
        right2 = setup("npc/butterfly_2", 0.75,0.75);
    }

    public void setAction() {
        Random random = new Random();
        actionLockCounter++;
        if (actionLockCounter == 30) {

            int i = random.nextInt(100) + 1;//pick a random number from 1 to 100

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
