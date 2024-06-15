package entity;

import main.GamePanel;

public class TargetingProjectile extends Entity {
//    INDEX OF THE MONSTER THE TARGETED PROJECTILE IS GOING TO
    public int target;

    //SET DEFAULT
    public TargetingProjectile(GamePanel gp) {
        super(gp);

        type = 3; // SET ENTITY TYPE TO PROJECTILE
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
        int xDistance = gp.monster[target].worldX - worldX;
        int yDistance = gp.monster[target].worldY - worldY;
        System.out.println(gp.monster[target].worldX + " " + gp.monster[target].worldY);

        //FIND 1/30 OF THE DISTANCE FROM THE PROJECTILE TO THE TARGET
//        Y AXIS
        if (xDistance > 0) {
            xMove = (int) Math.ceil((double) xDistance / 30);
        }
        else {
            xMove = (int) Math.floor((double) xDistance / 30);
        }
//        Y AXIS
        if (yDistance > 0) {
            yMove = (int) Math.ceil((double) yDistance / 30);
        }
        else {
            yMove = (int) Math.floor((double) yDistance / 30);
        }

        //MOVE PROJECTILE USING CALCULATED DISTANCE, B/C IT TRAVELS 1/30 CLOSER EVERY TIME IT GIVES OFF THE EFFECT OF
        // SLOWING DOWN TO HIT THE TARGET, AND ALSO ALLOWS FOR IT TO CHASE AFTER THE MONSTER SHOULD IT START TO MOVE
        // AWAY FROM THE PROJECTILE
        worldX += xMove;
        worldY += yMove;

//        DEBUGGING
//        PRINT OUT WORLD COORDINATES OF THE PROJECTILE TO SEE WHERE IT IS TRAVELLING
        System.out.println(worldX + " " + worldY);

//        FIND THE INDEX OF THE MONSTER THAT THE PROJECTILE HITS, JUST IN CASE THE PROJECTILE HITS ANOTHER MONSTER
//        WHILE IT'S SEEKING ANOTHER MONSTER
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);

//        IF INDEX RETURNED IS 999, NO MONSTER HAS BEEN HIT
        if (monsterIndex != 999) {
            gp.player.damageMonster(monsterIndex, attack); // DAMAGES THE MONSTER BASED ON THE ATTACK OF THE
            // PROEJCTILE. EAGLE EYE ATTACK VALUE HAS BEEN SET SO THAT IT IS ALWAYS ONE-SHOT-KILL FOR ANY MONSTER
            alive = false; // EAGLE EYE SHOT DISAPPEARS AFTER HITTING A MONSTER, NO COOLDOWN LIKE TURTLE WATER JET.
            // EAGLE EYE CAN CHASE TARGET FOR AS LONG AS NEEDED
        }
    }
}