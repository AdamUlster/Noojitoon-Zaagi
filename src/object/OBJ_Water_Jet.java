package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Water_Jet extends Projectile {
    private GamePanel gp;

    public OBJ_Water_Jet(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Water Jet";
        speed = 10;
        maxHealth = 80;
        health = maxHealth;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
    }

    public void getImage() {
        up1 = setup("objects/water_jet_up", 1, 1);
        up2 = setup("objects/water_jet_up", 1, 1);
        down1 = setup("objects/water_jet_down", 1, 1);
        down2 = setup("objects/water_jet_down", 1, 1);
        left1 = setup("objects/water_jet_left", 1, 1);
        left2 = setup("objects/water_jet_left", 1, 1);
        right1 = setup("objects/water_jet_right", 1, 1);
        right2 = setup("objects/water_jet_right", 1, 1);
    }
}
