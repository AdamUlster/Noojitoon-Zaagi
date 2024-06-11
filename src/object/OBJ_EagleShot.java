package object;

import entity.Projectile;
import entity.TargetingProjectile;
import main.GamePanel;

public class OBJ_EagleShot extends TargetingProjectile {
    GamePanel gp;

    public OBJ_EagleShot(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "EagleShot";
        speed = 20;
        maxHealth = 80;
        health = maxHealth;
        attack = 10;
        useCost = 1;
        alive = false;
    }
    public void getImage() {

    }
}
