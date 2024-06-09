package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_Windigo extends Entity {

    public MON_Windigo(GamePanel gp) {
        super(gp);

        type = 2; // sets this entity's type to a monster
        name = "Windigo";
        speed = 3;
        maxHealth = 4;
        health = maxHealth;

        // sets the collision box for the monster
        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1 = setup("monsters/windigo", 1);
        up2 = setup("monsters/windigo", 1);
        down1 = setup("monsters/windigo", 1);
        down2 = setup("monsters/windigo", 1);
        left1 = setup("monsters/windigo", 1);
        left2 = setup("monsters/windigo", 1);
        right1 = setup("monsters/windigo", 1);
        right2 = setup("monsters/windigo", 1);
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
