package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_Miasma extends Entity {

    public double hitboxScale = 0.70;//scaling factor for hitbox
    public MON_Miasma(GamePanel gp) {
        super(gp);

        type = 2; // sets this entity's type to a monster
        name = "Miasma";
        speed = 3;
        maxHealth = 4;
        health = maxHealth;
        attack = 5;
        defense = 0;

        // sets the collision box for the monster in the center
        solidArea.width = (int)(gp.tileSize * hitboxScale);
        solidArea.height = (int)(gp.tileSize * hitboxScale);
        solidArea.x = (gp.tileSize - solidArea.width)/2;
        solidArea.y = (gp.tileSize - solidArea.height) / 2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;



        getImage();
    }

    public void getImage() {
        up1 = setup("monsters/miasma_1", 1,1);
        up2 = setup("monsters/miasma_2", 1,1);
        down1 = setup("monsters/miasma_1", 1,1);
        down2 = setup("monsters/miasma_2", 1,1);
        left1 = setup("monsters/miasma_1", 1,1);
        left2 = setup("monsters/miasma_2", 1,1);
        right1 = setup("monsters/miasma_1", 1,1);
        right2 = setup("monsters/miasma_2", 1,1);
    }

    public void setAction() {

        // random monster behaviour
        Random random = new Random();
        actionLockCounter++;
        if (actionLockCounter == 120) {

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
