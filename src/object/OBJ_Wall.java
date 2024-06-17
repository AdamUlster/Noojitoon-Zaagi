package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Wall extends Entity {
    public OBJ_Wall(GamePanel gp) {
        super(gp);

        setName("Wall");
        setDown1(setup("objects/wall", 1, 1));
        setCollision(true);

        //create hitbox in the center
        getSolidArea().width = gp.getTileSize();
        getSolidArea().height = gp.getTileSize();
        getSolidArea().x = (gp.getTileSize() - getSolidArea().width) / 2;
        getSolidArea().y = (gp.getTileSize() - getSolidArea().height) / 2;

        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);
    }
}
