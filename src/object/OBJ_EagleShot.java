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
        setHealth(getMaxHealth());
        setAttack(30);
        setUseCost(1);
        setAlive(false);
        getImage();
    }
    private void getImage() {
        setUp1(setup("objects/eagle_shot", 1, 1));
        setUp2(setup("objects/eagle_shot", 1, 1));
        setDown1(setup("objects/eagle_shot", 1, 1));
        setDown2(setup("objects/eagle_shot", 1, 1));
        setLeft1(setup("objects/eagle_shot", 1, 1));
        setLeft2(setup("objects/eagle_shot", 1, 1));
        setRight1(setup("objects/eagle_shot", 1, 1));
        setRight2(setup("objects/eagle_shot", 1, 1));
    }
}