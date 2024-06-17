package entity;

import main.GamePanel;

public class TargetingProjectile extends Entity {
    private int target;//    INDEX OF THE MONSTER THE TARGETED PROJECTILE IS GOING TO

    //SET DEFAULT
    public TargetingProjectile(GamePanel gp) {
        super(gp);

        setType(3); // SET ENTITY TYPE TO PROJECTILE
    }

    void set(int worldX, int worldY, boolean alive, int target) { // passes the coordinates to create the fireball
        this.setWorldX(worldX);
        this.setWorldY(worldY);
        this.setDirection("up");
        this.setAlive(alive);
        this.setHealth(this.getMaxHealth());
    }

//    PASSES THE COORDINATES TO CREATE THE FIREBALL
    public void set(int worldX, int worldY, boolean alive, int target) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = "up";
        this.alive = alive;
        this.health = this.maxHealth;
        this.target = target;
    }

//    UPDATE METHOD TO MOVE THE PROJECTILE
    public void update() {
      //CALCULATE RELATIVE DISTANCE IN BOTH AXES
        int xDistance = gp.getMonster()[target].getWorldX() - getWorldX();//relative distance in both axis
        int yDistance = gp.getMonster()[target].getWorldY() - getWorldY();
        System.out.println(gp.getMonster()[target].getWorldX() + " " + gp.getMonster()[target].getWorldY());

        //FIND 1/30 OF THE DISTANCE FROM THE PROJECTILE TO THE TARGET
//        Y AXIS
        if (xDistance > 0) {
            setxMove((int) Math.ceil((double) xDistance / 30));
        }
        else {
            setxMove((int) Math.floor((double) xDistance / 30));
        }
//        Y AXIS
        if (yDistance > 0) {
            setyMove((int) Math.ceil((double) yDistance / 30));
        }
        else {
            setyMove((int) Math.floor((double) yDistance / 30));
        }

        //MOVE PROJECTILE USING CALCULATED DISTANCE, B/C IT TRAVELS 1/30 CLOSER EVERY TIME IT GIVES OFF THE EFFECT OF
        // SLOWING DOWN TO HIT THE TARGET, AND ALSO ALLOWS FOR IT TO CHASE AFTER THE MONSTER SHOULD IT START TO MOVE
        // AWAY FROM THE PROJECTILE
        setWorldX(getWorldX() + getxMove());
        setWorldY(getWorldY() + getyMove());
        System.out.println(getWorldX() + " " + getWorldY());

//        FIND THE INDEX OF THE MONSTER THAT THE PROJECTILE HITS, JUST IN CASE THE PROJECTILE HITS ANOTHER MONSTER
//        WHILE IT'S SEEKING ANOTHER MONSTER
        int monsterIndex = gp.getCChecker().checkEntity(this, gp.getMonster()); // gets the monster index that the projectile hits
      
      //        IF INDEX RETURNED IS 999, NO MONSTER HAS BEEN HIT
        if (monsterIndex != 999) { // if the projectile hits a monster
            gp.getPlayer().damageMonster(monsterIndex, getAttack()); / DAMAGES THE MONSTER BASED ON THE ATTACK OF THE
            // PROEJCTILE. EAGLE EYE ATTACK VALUE HAS BEEN SET SO THAT IT IS ALWAYS ONE-SHOT-KILL FOR ANY MONSTER
            setAlive(false); // EAGLE EYE SHOT DISAPPEARS AFTER HITTING A MONSTER, NO COOLDOWN LIKE TURTLE WATER JET.
            // EAGLE EYE CAN CHASE TARGET FOR AS LONG AS NEEDED
        }

//        DEBUGGING
//        PRINT OUT WORLD COORDINATES OF THE PROJECTILE TO SEE WHERE IT IS TRAVELLING
        System.out.println(worldX + " " + worldY);
    }
}