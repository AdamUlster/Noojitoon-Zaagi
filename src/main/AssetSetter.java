package main;

import entity.NPC;
import object.OBJ_Totem;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp) { // constructor
        this.gp = gp;
    }
    public void setObject() {
        gp.obj[0] = new OBJ_Totem(gp);
        gp.obj[0].worldX = 5 * gp.tileSize;
        gp.obj[0].worldY = 5 * gp.tileSize;
    }

    public void setNPC() {
        //create 1 npc
        gp.npc[0] = new NPC(gp);
        gp.npc[0].worldX = gp.tileSize*8;
        gp.npc[0].worldY = gp.tileSize*8;
    }
}
