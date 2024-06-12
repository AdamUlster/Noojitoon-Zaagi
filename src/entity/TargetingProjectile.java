package entity;

import main.GamePanel;

public class TargetingProjectile extends Entity {
    public int target;//index of the monster this projectile is going to

    public TargetingProjectile(GamePanel gp) {
        super(gp);

        type = 3; // sets this entity's type to a projectile
    }

    public void set(int worldX, int worldY, boolean alive, int target) { // passes the coordinates to create the fireball
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = "up";
        this.alive = alive;
        this.health = this.maxHealth;
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
        int xDistance = gp.monster[target].worldX - worldX;//relative distance in both axis
        int yDistance = gp.monster[target].worldY - worldY;
        System.out.println(gp.monster[target].worldX + " " + gp.monster[target].worldY);

        //find the distance
        if (xDistance > 0) {
            xMove = (int) Math.ceil((double) xDistance / 30);
        }
        else {
            xMove = (int) Math.floor((double) xDistance / 30);
        }
        if (yDistance > 0) {
            yMove = (int) Math.ceil((double) yDistance / 30);
        }
        else {
            yMove = (int) Math.floor((double) yDistance / 30);
        }

        //move the projectile
        worldX += xMove;
        worldY += yMove;
        System.out.println(worldX + " " + worldY);

        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster); // gets the monster index that the projectile hits
        if (monsterIndex != 999) { // if the projectile hits a monster
            gp.player.damageMonster(monsterIndex, attack); // passes the projectile's attack to a monster
            alive = false; // the projectile disappears after hitting a monster
        }

        //find relative position between targeting projectile and the monster
        //change positions so the projectile moves closer to the monster

    }
}