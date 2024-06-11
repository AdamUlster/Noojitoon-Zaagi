package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Eagle extends Projectile {
    GamePanel gp;

    public OBJ_Eagle (GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Eagle Shot";
        speed = 20;
        maxHealth = 80;
        health = maxHealth;
        attack = 2;
        useCost = 1;
        alive = false;
    }
    public void getImage() {

    }
}
