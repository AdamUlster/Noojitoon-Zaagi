package object;

import entity.Projectile;
import main.GamePanel;

//PROJECTILE OF THE TURTLE SPIRIT PRIMARY ATTACK
public class OBJ_Water_Jet extends Projectile {
    private GamePanel gp;

    public OBJ_Water_Jet(GamePanel gp) {
//        CALL ON PROJECTILE CLASS
        super(gp);
        this.gp = gp;

        setName("Water Jet");
        setSpeed(10); // MOVES AT 10 PIXELS PER FRAME
        setMaxHealth(80); // DETERMINES HOW LONG WATER JET STAYS ON SCREEN FOR
        setHealth(getMaxHealth());
        setAttack(2);
        setUseCost(1);
        setAlive(false);
        getImage();
    }

//    IMAGE RETRIEVAL METHOD
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
