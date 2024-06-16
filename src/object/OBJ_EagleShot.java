package object;

import entity.Projectile;
import entity.TargetingProjectile;
import main.GamePanel;

public class OBJ_EagleShot extends TargetingProjectile {
    private GamePanel gp;

    public OBJ_EagleShot(GamePanel gp) {
        super(gp);
        this.gp = gp;

        setName("Eagle Shot");
        setSpeed(20);
        setMaxHealth(80);
        setHealth(maxHealth);
        setAttack(10);
        useCost = 1;
        alive = false;
        getImage();
    }
    private void getImage() {
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