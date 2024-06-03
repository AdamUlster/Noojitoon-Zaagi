package main;

import object.OBJ_Totem;
import object.OBJ_Totem;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp) { // constructor
        this.gp = gp;
    }
    public void setObject() {
        gp.obj[0] = new OBJ_Totem();
        gp.obj[0].worldX = 23 * gp.tileSize;
        gp.obj[0].worldY = 7 * gp.tileSize;
    }
}