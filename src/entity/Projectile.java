package entity;

import main.GamePanel;

public class Projectile extends Entity {
    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) { // passes the coordinates to create the fireball
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.health = this.maxHealth;
    }

    public void update() {
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster); // gets the monster index that the projectile hits
        if (monsterIndex != 999) { // if the projectile hits a monster
            gp.player.damageMonster(monsterIndex, attack); // passes the projectile's attack to a monster
            alive = false; // the projectile disappears after hitting a monster
        }

        switch (direction) { // allows for the projectile to be launched
            case "up":
                worldY -= speed;
                break;
            case "down":
                worldY += speed;
                break;
            case "left":
                worldX -= speed;
                break;
            case "right":
                worldX += speed;
                break;
        }
        health --;
        if (health <= 0) {
            alive = false; // the projectile disappears when it has no more health
        }

    }
}
