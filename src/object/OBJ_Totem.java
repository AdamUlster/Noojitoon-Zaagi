package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Totem extends Entity {
    private double hitboxScale = 0.80;//scaling factor for hitbox
    public OBJ_Totem(GamePanel gp) { // constructor
        super(gp); // calls the entity class

        setName("Totem");
        setDown1(setup("objects/totem", 1, 1));
        setCollision(true);

        //create hitbox in the center
        getSolidArea().width = (int)(gp.getTileSize() * hitboxScale);
        getSolidArea().height = (int)(gp.getTileSize() * hitboxScale);
        getSolidArea().x = (gp.getTileSize() - getSolidArea().width) / 2;
        getSolidArea().y = (gp.getTileSize() - getSolidArea().height) / 2;

        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);
    }
}