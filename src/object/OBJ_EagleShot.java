package object;

import entity.TargetingProjectile;
import main.GamePanel;

//SEEKing PROJECTILE FOR EAGLE SPECIAL ATTACK
public class OBJ_EagleShot extends TargetingProjectile {
    private GamePanel gp;

    public OBJ_EagleShot(GamePanel gp) {
//        CALL ON TARGETING PROJECTILE CLASS
        super(gp);
        this.gp = gp;

        setName("Eagle Shot");
        setSpeed(20); //EAGLE SHOT TRAVELS AT 20 FRAMES PER SECOND
        setMaxHealth(80);
        setHealth(getMaxHealth());
        setAttack(30); // VERY HIGH ATTACK VALUE SO THAT IT ONE-SHOT-KILLS ANY MONSTER IT COMES INTO CONTACT WITH
        setUseCost(1);
        setAlive(false);
        getImage(); //        RETRIEVE SPRITE IMAGES
    }
  
  //    USE SETUP METHOD TO RETRIEVE IMAGE FILES
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