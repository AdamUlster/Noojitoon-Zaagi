package entity;

import main.GamePanel;

public class TargetingProjectile extends Entity {
    public int target;//index of the monster this projectile is going to

    public TargetingProjectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, int target) { // passes the coordinates to create the fireball
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.health = this.maxHealth;
        this.target = target;
    }

    public void update() {
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster); // gets the monster index that the projectile hits
        if (monsterIndex != 999) { // if the projectile hits a monster
            gp.player.damageMonster(monsterIndex, attack); // passes the projectile's attack to a monster
            alive = false; // the projectile disappears after hitting a monster
        }

        //find relative position between targeting projectile and the monster
        //change positions so the projectile moves closer to the monster


    }
}
