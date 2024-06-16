package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Water_Jet extends Projectile {
    private GamePanel gp;

    public OBJ_Water_Jet(GamePanel gp) {
        super(gp);
        this.gp = gp;

        setName("Water Jet");
        setSpeed(10);
        setMaxHealth(80);
        setHealth(getMaxHealth());
        setAttack(2);
        setUseCost(1);
        setAlive(false);
        getImage();
    }

    public void getImage() {
        setUp1(setup("objects/water_jet_up", 1, 1));
        setUp2(setup("objects/water_jet_up", 1, 1));
        setDown1(setup("objects/water_jet_down", 1, 1));
        setDown2(setup("objects/water_jet_down", 1, 1));
        setLeft1(setup("objects/water_jet_left", 1, 1));
        setLeft2(setup("objects/water_jet_left", 1, 1));
        setRight1(setup("objects/water_jet_right", 1, 1));
        setRight2(setup("objects/water_jet_right", 1, 1));
    }
}
