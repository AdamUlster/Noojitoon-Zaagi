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
            gp.npc[i].worldY = gp.tileSize * random.nextInt(99);
        }

    }

    public void setMonster() {

        int[][] miasmaCoords = {{58, 44}, {61, 45}, {61, 52}, {61, 55}, {67, 57}, {62, 59}, {64, 61}, {67, 62}, {69, 57},
                {74, 50}, {73, 50}, {72, 49}, {71, 49}, {70, 46}, {68, 43}, {64, 43}, {64, 42}, {64, 42}, {70, 40},
                {58, 72}, {54, 72}, {51, 76}, {60, 76}, {60, 77}, {60, 78}, {50, 78}, {47, 82}, {50, 85}, {54, 87},
                {60, 85}, {63, 85}, {54, 91}, {60, 93}, {57, 94}, {68, 81}, {72, 81}, {72, 83}, {75, 90}, {82, 87},
                {77, 92}, {79, 94}, {85, 97}, {90, 97}, {92, 93}, {88, 89}, {81, 79}, {78, 77}, {71, 70}, {73, 69},
                {78, 67}, {82, 68}, {86, 79}, {90, 74}, {91, 84}, {95, 84}, {91, 69}, {91, 67}, {95, 54}, {97, 52},
                {95, 51}, {82, 53}, {81, 51}, {85, 51}, {87, 44}, {87, 42}, {81, 43}, {93, 38}, {96, 38}, {88, 30},
                {86, 29}, {82, 28}, {80, 39}, {79, 35}, {79, 30}, {76, 31}, {92, 33}, {96, 33}, {91, 29}, {95, 29},
                {96, 24}, {94, 23}, {96, 15}, {95, 13}, {96, 6}, {93, 6}, {92, 12}, {91, 14}, {88, 10}, {86, 10},
                {85, 16}, {83, 15}, {94, 89}, {96, 89}};
        // create the monsters
        for (int i = 0; i < gp.monster.length; i++) {
            gp.monster[i] = new MON_Miasma(gp);
            gp.monster[i].worldX = gp.tileSize * miasmaCoords[i][0];
            gp.monster[i].worldY = gp.tileSize * miasmaCoords[i][1];
        }


        gp.monster[1] = new MON_Micipijiu(gp);
        gp.monster[1].worldX = gp.tileSize * 48;
        gp.monster[1].worldY = gp.tileSize * 48;

        gp.monster[2] = new MON_Windigo(gp);
        gp.monster[2].worldX = gp.tileSize * 49;
        gp.monster[2].worldY = gp.tileSize * 49;
    }
}
