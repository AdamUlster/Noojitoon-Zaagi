package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_Miasma extends Entity {

    public double hitboxScale = 0.70;// SCALING FACTOR FOR THE HITBOX
    public MON_Miasma(GamePanel gp) {
        super(gp);//CALL ON GAME PANEL CLASS

        setType(2); // sets this entity's type to a monster
        setName("Miasma");
        setSpeed(3);
        setMaxHealth(4);
        setHealth(getMaxHealth());
        setAttack(5);
        setDefense(0);

        // sets the collision box for the monster in the center
        getSolidArea().width = (int)(gp.getTileSize() * hitboxScale);
        getSolidArea().height = (int)(gp.getTileSize() * hitboxScale);
        getSolidArea().x = (gp.getTileSize() - getSolidArea().width)/2;
        getSolidArea().y = (gp.getTileSize() - getSolidArea().height) / 2;
        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);
        getImage();//RETRIEVE IMAGE VIA METHOD
    }

    private void getImage() {
        setUp1(setup("monsters/miasma_1", 1, 1));
        setUp2(setup("monsters/miasma_2", 1, 1));
        setDown1(setup("monsters/miasma_1", 1, 1));
        setDown2(setup("monsters/miasma_2", 1, 1));
        setLeft1(setup("monsters/miasma_1", 1, 1));
        setLeft2(setup("monsters/miasma_2", 1, 1));
        setRight1(setup("monsters/miasma_1", 1, 1));
        setRight2(setup("monsters/miasma_2", 1, 1));
    }

//    MONSTER BEHAVIOR AND MOVEMENT
    public void setAction() {

        // RANDOM MOVEMENT BEHAVIOR
        Random random = new Random();
        actionLockCounter++;//INCREASES ACTION LOCK COUNTER SO DIRECTION CHANGE ONLY OCCURS EVERY 120 FRAMES
        if (actionLockCounter == 120) {// CHANGES DIRECTION ONLY EVERY 120 FRAMES OR EVERY 2 SECONDS

            int i = random.nextInt(100) + 1;// PICKS A RANDOM NUMBER BETWEEN 1 AND 100

            //CHOOSES A RANDOM DIRECTION TO TRAVEL IN
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
            actionLockCounter = 0;//RESET DIRECTION CHANGE COUNTER
          
    public void update() { // overwrites the parent class's update method
        super.update(); // calls on the parent's class update method

        int xDistance = Math.abs(getWorldX() - gp.getPlayer().getWorldX());
        int yDistance = Math.abs(getWorldY() - gp.getPlayer().getWorldY());
        int tileDistance = (xDistance + yDistance) / gp.getTileSize();

        if (!isOnPath() && tileDistance < 5) { // if the monster is within 5 tiles of the player
            int i = new Random().nextInt(100) + 1; // picks a random number from 1 to 100
            if (i > 50) {
                setOnPath(true); // half the time, it doesn't follow the player
            }
        }
        if (isOnPath() && tileDistance > 15) { // makes the monsters disappear once the player is a certain distance away
            setOnPath(false);
        }
    }

    public void setAction() {

        if (isOnPath()) {
            int goalCol = (gp.getPlayer().getWorldX() + gp.getPlayer().getSolidArea().x) / gp.getTileSize();
            int goalRow = (gp.getPlayer().getWorldY() + gp.getPlayer().getSolidArea().y) / gp.getTileSize();
            searchPathToPlayer(goalCol, goalRow);
        }
        else {

            // random monster behaviour
            setActionLockCounter(getActionLockCounter() + 1);
            if (getActionLockCounter() == 120) {

                Random random = new Random();
                int i = random.nextInt(100) + 1;//pick a random number from 1 to 100

                if (i <= 25) {
                    setDirection("up");
                }
                if (i > 25 && i <= 50) {
                    setDirection("down");
                }
                if (i > 50 && i <= 75) {
                    setDirection("left");
                }
                if (i > 75 && i < 100) {
                    setDirection("right");
                }
                setActionLockCounter(0);
            }
        }
    }

    public void damageReaction() {
        setActionLockCounter(0);
        setOnPath(true);
    }

    public double getHitboxScale() {
        return hitboxScale;
    }

    public void setHitboxScale(double hitboxScale) {
        this.hitboxScale = hitboxScale;
    }
}
