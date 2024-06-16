package entity;

import main.GamePanel;

public class TargetingProjectile extends Entity {
    private int target;//index of the monster this projectile is going to

    public TargetingProjectile(GamePanel gp) {
        super(gp);

        setType(3); // sets this entity's type to a projectile
    }

    void set(int worldX, int worldY, boolean alive, int target) { // passes the coordinates to create the fireball
        this.setWorldX(worldX);
        this.setWorldY(worldY);
        this.setDirection("up");
        this.setAlive(alive);
        this.setHealth(this.getMaxHealth());
        this.target = target;

        // To adjust the hit box of the targeting projectile
       // solidArea.width = 1000;
        //solidArea.height = 1000;
        //solidArea.x = 1000;
        //solidArea.y = 1000;

        //solidAreaDefaultX = solidArea.x;
        //solidAreaDefaultY = solidArea.y;
    }

    public void update() {
        int xDistance = gp.getMonster()[target].getWorldX() - getWorldX();//relative distance in both axis
        int yDistance = gp.getMonster()[target].getWorldY() - getWorldY();
        System.out.println(gp.getMonster()[target].getWorldX() + " " + gp.getMonster()[target].getWorldY());

        //find the distance
        if (xDistance > 0) {
            setxMove((int) Math.ceil((double) xDistance / 30));
        }
        else {
            setxMove((int) Math.floor((double) xDistance / 30));
        }
        if (yDistance > 0) {
            setyMove((int) Math.ceil((double) yDistance / 30));
        }
        else {
            setyMove((int) Math.floor((double) yDistance / 30));
        }

        //move the projectile
        setWorldX(getWorldX() + getxMove());
        setWorldY(getWorldY() + getyMove());
        System.out.println(getWorldX() + " " + getWorldY());

        int monsterIndex = gp.getCChecker().checkEntity(this, gp.getMonster()); // gets the monster index that the projectile hits
        if (monsterIndex != 999) { // if the projectile hits a monster
            gp.getPlayer().damageMonster(monsterIndex, getAttack()); // passes the projectile's attack to a monster
            setAlive(false); // the projectile disappears after hitting a monster
        }

        //find relative position between targeting projectile and the monster
        //change positions so the projectile moves closer to the monster

    }
}