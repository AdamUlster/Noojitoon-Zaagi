package object;

import entity.Entity;
import main.GamePanel;

// HEART OBJECT THAT DISPLAYS THE SPIRIT'S HEALTH
public class OBJ_Heart extends Entity {
//    CONSTRUCTOR
    public OBJ_Heart(GamePanel gp) {
        super(gp); // CALLS ON ENTITY CLASS

        name = "Heart";

//        SETS UP AND SCALES IMAGE VIA SETUP METHOD AND FILE PATH
        image1 = setup("objects/heart_full", 1.5,1.5);
        image2 = setup("objects/heart_half", 1.5,1.5);
        image3 = setup("objects/heart_blank", 1.5,1.5);
    }
}