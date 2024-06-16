package entity;

import main.GamePanel;

public class Projectile extends Entity {
    private Entity user;

    public Projectile(GamePanel gp) {
        super(gp);

        setType(3); // sets this entity's type to a projectile
    }

    void set(int worldX, int worldY, String direction, boolean alive, Entity user) { // passes the coordinates to create the fireball
        this.setWorldX(worldX);
        this.setWorldY(worldY);
        this.setDirection(direction);
        this.setAlive(alive);
=======
        type = 3; // SET ENTITY TYPE TO PROJECTILE
    }

//    PASSES THE COORDINATES TO CREATE THE WATER JET
    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.setHealth(this.getMaxHealth());
    }

//    UPDATE METHOD TO MOVE THE WATER JET
    public void update() {
        int monsterIndex = gp.getCChecker().checkEntity(this, gp.getMonster()); // gets the monster index that the projectile hits
        if (monsterIndex != 999) { // if the projectile hits a monster
            gp.getPlayer().damageMonster(monsterIndex, getAttack()); // passes the projectile's attack to a monster
            setAlive(false); // the projectile disappears after hitting a monster
        }

        switch (getDirection()) { // allows for the projectile to be launched
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
                setWorldY(getWorldY() - getSpeed());
                break;
            case "down":
                setWorldY(getWorldY() + getSpeed());
                break;
            case "left":
                setWorldX(getWorldX() - getSpeed());
                break;
            case "right":
                setWorldX(getWorldX() + getSpeed());
                break;
        }
        setHealth(getHealth() - 1);
        if (getHealth() <= 0) {
            setAlive(false); // the projectile disappears when it has no more health
        //DECREASED WATER JET EXISTENCE COUNTER
        health --;

//        PROJECTILE DISAPPEARS WHEN IT HAS NO MORE HEALTH
        if (health <= 0) {
            alive = false;
        }
    }
}
