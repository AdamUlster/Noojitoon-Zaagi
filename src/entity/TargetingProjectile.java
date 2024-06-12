package entity;

import main.GamePanel;

public class TargetingProjectile extends Entity {
    public int target;//index of the monster this projectile is going to

    public TargetingProjectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, boolean alive, int target) { // passes the coordinates to create the fireball
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = "up";
        this.alive = alive;
        this.health = this.maxHealth;
        this.target = target;

       // solidArea.width = 1000;
        //solidArea.height = 1000;
        //solidArea.x = 1000;
        //solidArea.y = 1000;

        //solidAreaDefaultX = solidArea.x;
        //solidAreaDefaultY = solidArea.y;
    }

    public void update() {
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster); // gets the monster index that the projectile hits
        if (monsterIndex != 999) { // if the projectile hits a monster
            gp.player.damageMonster(monsterIndex, attack); // passes the projectile's attack to a monster
            alive = false; // the projectile disappears after hitting a monster
        }

        int xDistance = gp.monster[target].worldX - worldX;//relative distance in both axis
        int yDistance = gp.monster[target].worldY - worldY;

        //fin the distance
        int xMove = xDistance / 30;
        int yMove = yDistance / 30;

        //move the projectile
        worldX += xMove;
        worldY += yMove;
        System.out.println(worldX / gp.tileSize + " " + worldY / gp.tileSize);

        //find relative position between targeting projectile and the monster
        //change positions so the projectile moves closer to the monster

    }
}
