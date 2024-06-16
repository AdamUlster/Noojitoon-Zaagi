package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_Micipijiu extends Entity {

    private double widthHitboxScale = 0.70;//scaling factor for the hitbox width
    private double heightHitboxScale = 0.35;//scaling factor for the hitbox height
    public MON_Micipijiu(GamePanel gp) {
        super(gp);// CALL ON GAME PANEL CLASS

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

        getImage();//RETRIEVE SPRITE IMAGES
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
        int tileDistance = (int)Math.sqrt(Math.pow(xDistance, 2.0) + Math.pow(yDistance, 2)) / gp.tileSize;

        if (!isOnPath() && tileDistance < 4) { // if the monster is within 5 tiles of the player
            int i = new Random().nextInt(100) + 1; // picks a random number from 1 to 100
            if (i > 50) {
                setOnPath(true); // half the time, it doesn't follow the player
            }else {
                onPath = false;
            }
        }
        if (isOnPath() && tileDistance > 15) { // makes the monsters disappear once the player is a certain distance away
            setOnPath(false);
        }
    }

//    MONSTER BEHAVIOR AND MOVEMENT
    public void setAction() {

  //        ACTIVELY FOLLOWS PLAYER IF AGGRO IS ON
        if (isOnPath()) {
            int goalCol = (gp.getPlayer().getWorldX() + gp.getPlayer().getSolidArea().x) / gp.getTileSize();
            int goalRow = (gp.getPlayer().getWorldY() + gp.getPlayer().getSolidArea().y) / gp.getTileSize();
            searchPathToPlayer(goalCol, goalRow);
        }
        else {

//            RANDOM BEHAVIOR
            setActionLockCounter(getActionLockCounter() + 1);
            if (getActionLockCounter() == 120) {

                Random random = new Random();
                int i = random.nextInt(100) + 1;//PICKS A RANDOM NUMBER BETWEEN 1 AND 100

//                SELECT A RANDOM DIRECTION TO TRAVEL IN
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

                setActionLockCounter(0);//RESET COUNTER SO MONSTER TRAVELS IN THAT DIRECTION FOR 2 SECONDS
            }
        }
    }

//    TURNS ON AGGRO IF MONSTER HAS BEEN DAMAGED
    public void damageReaction() {
        setActionLockCounter(0);
        setOnPath(true);
    }
}
