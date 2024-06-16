package object;

import entity.TargetingProjectile;
import main.GamePanel;

//SEEKing PROJECTILE FOR EAGLE SPECIAL ATTACK
public class OBJ_EagleShot extends TargetingProjectile {
    GamePanel gp;// CALL ON GAME PANEL CLASS

    public OBJ_EagleShot(GamePanel gp) {
//        CALL ON TARGETING PROJECTILE CLASS
        super(gp);
        this.gp = gp;

        name = "Eagle Shot";
        speed = 20;//EAGLE SHOT TRAVELS AT 20 FRAMES PER SECOND
        maxHealth = 80;
        health = maxHealth;
        attack = 30;// VERY HIGH ATTACK VALUE SO THAT IT ONE-SHOT-KILLS ANY MONSTER IT COMES INTO CONTACT WITH
        useCost = 1;
        alive = false;//DOES NOT APPEAR BY DEFAULT

//        RETRIEVE SPRITE IMAGES
        getImage();
    }

//    USE SETUP METHOD TO RETRIEVE IMAGE FILES
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