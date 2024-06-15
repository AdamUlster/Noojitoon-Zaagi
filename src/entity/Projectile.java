package entity;

import main.GamePanel;

public class Projectile extends Entity {
    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);

        type = 3; // SET ENTITY TYPE TO PROJECTILE
    }

//    PASSES THE COORDINATES TO CREATE THE WATER JET
    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.health = this.maxHealth;
    }

//    UPDATE METHOD TO MOVE THE WATER JET
    public void update() {
//        CHECK IF PROJECTILE HAS HIT A MONSTER, AND RETURN THE INDEX OF THE MONSTER IF IT DOES
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);

//        INDEX WILL BE 999 IF NO MONSTER HAS BEEN HIT
        if (monsterIndex != 999) { // if the projectile hits a monster
            gp.player.damageMonster(monsterIndex, attack); // DAMAGE MONSTER
            alive = false; // PROJECTILE DISAPPEARS AFTER HITTING AMONSTER
        }

//        MOVES PROJECTILE IN WHICH EVER DIRECTION THE PLAYER WAS FACING WHEN LAUNCHED
        switch (direction) {
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
        //DECREASED WATER JET EXISTENCE COUNTER
        health --;

//        PROJECTILE DISAPPEARS WHEN IT HAS NO MORE HEALTH
        if (health <= 0) {
            alive = false;
        }
    }
}
