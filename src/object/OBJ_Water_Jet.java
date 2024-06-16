package object;

import entity.Projectile;
import main.GamePanel;

//PROJECTILE OF THE TURTLE SPIRIT PRIMARY ATTACK
public class OBJ_Water_Jet extends Projectile {
    GamePanel gp;// CALL ON GAME PANEL CLASS

    public OBJ_Water_Jet(GamePanel gp) {
//        CALL ON PROJECTILE CLASS
        super(gp);
        this.gp = gp;

        name = "Water Jet";
        speed = 10;// MOVES AT 10 PIXELS PER FRAME
        maxHealth = 80;// DETERMINES HOW LONG WATER JET STAYS ON SCREEN FOR
        health = maxHealth;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
    }

//    IMAGE RETRIEVAL METHOD
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
