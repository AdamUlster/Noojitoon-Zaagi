package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_Miasma extends Entity {

    private MON_Miasma(GamePanel gp) {
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

    private void getImage() {
        up1 = setup("monsters/miasma_1", 1,1);
        up2 = setup("monsters/miasma_2", 1,1);
        down1 = setup("monsters/miasma_1", 1,1);
        down2 = setup("monsters/miasma_2", 1,1);
        left1 = setup("monsters/miasma_1", 1,1);
        left2 = setup("monsters/miasma_2", 1,1);
        right1 = setup("monsters/miasma_1", 1,1);
        right2 = setup("monsters/miasma_2", 1,1);
    }

    public void update() { // overwrites the parent class's update method
        super.update(); // calls on the parent's class update method

        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistance = (xDistance + yDistance) / gp.tileSize;

        if (!onPath && tileDistance < 5) { // if the monster is within 5 tiles of the player
            int i = new Random().nextInt(100) + 1; // picks a random number from 1 to 100
            if (i > 50) {
                onPath = true; // half the time, it doesn't follow the player
            }
        }
        if (onPath && tileDistance > 15) { // makes the monsters disappear once the player is a certain distance away
            onPath = false;
        }
    }

    public void setAction() {

        if (onPath) {
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
            searchPathToPlayer(goalCol, goalRow);
        }
        else {

            // random monster behaviour
            actionLockCounter++;
            if (actionLockCounter == 120) {

                Random random = new Random();
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

    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
    }
}
