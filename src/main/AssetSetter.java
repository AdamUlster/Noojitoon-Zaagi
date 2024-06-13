package main;

import entity.NPC;
import monster.MON_Miasma;
import monster.MON_Micipijiu;
import monster.MON_Windigo;
import object.OBJ_Totem;
import object.OBJ_Wall;

import java.util.Random;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp) { // constructor
        this.gp = gp;
    }
    public void setObject() {
        gp.obj[0] = new OBJ_Totem(gp); // spawns a totem
        gp.obj[0].worldX = gp.tileSize * 2;
        gp.obj[0].worldY = gp.tileSize * 55;

        gp.obj[1] = new OBJ_Totem(gp);
        gp.obj[1].worldX = gp.tileSize * 14;
        gp.obj[1].worldY = gp.tileSize * 4;

        gp.obj[2] = new OBJ_Totem(gp);
        gp.obj[2].worldX = gp.tileSize * 36;
        gp.obj[2].worldY = gp.tileSize * 95;

        gp.obj[3] = new OBJ_Wall(gp); // spawns the wall
        gp.obj[3].worldX = gp.tileSize * 17;
        gp.obj[3].worldY = gp.tileSize * 76;

        gp.obj[4] = new OBJ_Wall(gp); // spawns the wall
        gp.obj[4].worldX = gp.tileSize * 18;
        gp.obj[4].worldY = gp.tileSize * 76;

        gp.obj[5] = new OBJ_Wall(gp); // spawns the wall
        gp.obj[5].worldX = gp.tileSize * 19;
        gp.obj[5].worldY = gp.tileSize * 76;

        gp.obj[6] = new OBJ_Wall(gp); // spawns the wall
        gp.obj[6].worldX = gp.tileSize * 20;
        gp.obj[6].worldY = gp.tileSize * 76;

        gp.obj[7] = new OBJ_Wall(gp); // spawns the wall
        gp.obj[7].worldX = gp.tileSize * 18;
        gp.obj[7].worldY = gp.tileSize * 77;

        gp.obj[8] = new OBJ_Wall(gp); // spawns the wall
        gp.obj[8].worldX = gp.tileSize * 19;
        gp.obj[8].worldY = gp.tileSize * 77;

        gp.obj[9] = new OBJ_Totem(gp);
        gp.obj[9].worldX = gp.tileSize * 76;
        gp.obj[9].worldY = gp.tileSize * 33;
    }

    public void setNPC() {
        Random random = new Random();
        // create 1 npc
        for (int i = 0; i < gp.npc.length; i++) {
            gp.npc[i] = new NPC(gp);
            gp.npc[i].worldX = gp.tileSize * random.nextInt(99);
            gp.npc[i].worldY  = gp.tileSize * random.nextInt(99);
        }

    }

    public void setMonster() {
        // create the monsters
        gp.monster[0] = new MON_Miasma(gp);
        gp.monster[0].worldX = gp.tileSize * 50;
        gp.monster[0].worldY = gp.tileSize * 48;

        gp.monster[1] = new MON_Micipijiu(gp);
        gp.monster[1].worldX = gp.tileSize * 48;
        gp.monster[1].worldY = gp.tileSize * 48;

        gp.monster[2] = new MON_Windigo(gp);
        gp.monster[2].worldX = gp.tileSize * 49;
        gp.monster[2].worldY = gp.tileSize * 49;
    }
}
