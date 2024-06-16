package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_Micipijiu extends Entity {

    private double widthHitboxScale = 0.70;//scaling factor for the hitbox width
    private double heightHitboxScale = 0.35;//scaling factor for the hitbox height
    public MON_Micipijiu(GamePanel gp) {
        super(gp);

        setType(2); // sets this entity's type to a monster
        setName("Micipijiu");
        setSpeed(3);
        setMaxHealth(4);
        setHealth(getMaxHealth());
        setAttack(5);
        setDefense(0);


        // sets the collision box for the monster in the center
        getSolidArea().width = (int)(gp.getTileSize() * widthHitboxScale);
        getSolidArea().height = (int)(gp.getTileSize() * heightHitboxScale);
        getSolidArea().x = (gp.getTileSize() - getSolidArea().width) /2;
        getSolidArea().y = (gp.getTileSize() - getSolidArea().height) /2;

        //solidArea.width = 42 * (int)(gp.scale * 0.65);
//        solidArea.height = 30 * (int)(gp.scale * 0.65);
        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);

        getImage();
    }

    private void getImage() {
        setUp1(setup("monsters/micipijiu_right", 1, 1));
        setUp2(setup("monsters/micipijiu_right", 1, 1));
        setDown1(setup("monsters/micipijiu_right", 1, 1));
        setDown2(setup("monsters/micipijiu_right", 1, 1));
        setLeft1(setup("monsters/micipijiu_left", 1, 1));
        setLeft2(setup("monsters/micipijiu_left", 1, 1));
        setRight1(setup("monsters/micipijiu_right", 1, 1));
        setRight2(setup("monsters/micipijiu_right", 1, 1));
    }

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
}
