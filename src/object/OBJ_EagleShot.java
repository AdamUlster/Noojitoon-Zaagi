package object;

import entity.Projectile;
import entity.TargetingProjectile;
import main.GamePanel;

public class OBJ_EagleShot extends TargetingProjectile {
    GamePanel gp;

    public OBJ_EagleShot(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Eagle Shot";
        speed = 20;
        maxHealth = 80;
        health = maxHealth;
        attack = 10;
        useCost = 1;
        alive = false;
        getImage();
    }
    public void getImage() {
        up1 = setup("objects/eagle_shot", 1, 1);
        up2 = setup("objects/eagle_shot", 1, 1);
        down1 = setup("objects/eagle_shot", 1, 1);
        down2 = setup("objects/eagle_shot", 1, 1);
        left1 = setup("objects/eagle_shot", 1, 1);
        left2 = setup("objects/eagle_shot", 1, 1);
        right1 = setup("objects/eagle_shot", 1, 1);
        right2 = setup("objects/eagle_shot", 1, 1);
    }
}