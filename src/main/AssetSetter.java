package main;

import entity.NPC;
import monster.MON_Miasma;
import monster.MON_Micipijiu;
import monster.MON_Windigo;
import object.OBJ_Totem;
import object.OBJ_Wall;

import java.util.Random;

public class AssetSetter {
    private GamePanel gp;

    public AssetSetter(GamePanel gp) { // constructor
        this.gp = gp;
    }

    void setObject() {
        gp.getObj()[0] = new OBJ_Totem(gp);
        gp.getObj()[0].setWorldX(gp.getTileSize() * 36);
        gp.getObj()[0].setWorldY(gp.getTileSize() * 95);

        gp.getObj()[1] = new OBJ_Totem(gp);
        gp.getObj()[1].setWorldX(gp.getTileSize() * 14);
        gp.getObj()[1].setWorldY(gp.getTileSize() * 4);

        gp.getObj()[2] = new OBJ_Totem(gp); // spawns a totem
        gp.getObj()[2].setWorldX(gp.getTileSize() * 2);
        gp.getObj()[2].setWorldY(gp.getTileSize() * 55);

        gp.getObj()[3] = new OBJ_Wall(gp); // spawns the wall
        gp.getObj()[3].setWorldX(gp.getTileSize() * 17);
        gp.getObj()[3].setWorldY(gp.getTileSize() * 76);

        gp.getObj()[4] = new OBJ_Wall(gp); // spawns the wall
        gp.getObj()[4].setWorldX(gp.getTileSize() * 18);
        gp.getObj()[4].setWorldY(gp.getTileSize() * 76);

        gp.getObj()[5] = new OBJ_Wall(gp); // spawns the wall
        gp.getObj()[5].setWorldX(gp.getTileSize() * 19);
        gp.getObj()[5].setWorldY(gp.getTileSize() * 76);

        gp.getObj()[6] = new OBJ_Wall(gp); // spawns the wall
        gp.getObj()[6].setWorldX(gp.getTileSize() * 20);
        gp.getObj()[6].setWorldY(gp.getTileSize() * 76);

        gp.getObj()[7] = new OBJ_Wall(gp); // spawns the wall
        gp.getObj()[7].setWorldX(gp.getTileSize() * 18);
        gp.getObj()[7].setWorldY(gp.getTileSize() * 77);

        gp.getObj()[8] = new OBJ_Wall(gp); // spawns the wall
        gp.getObj()[8].setWorldX(gp.getTileSize() * 19);
        gp.getObj()[8].setWorldY(gp.getTileSize() * 77);

        gp.getObj()[9] = new OBJ_Totem(gp);
        gp.getObj()[9].setWorldX(gp.getTileSize() * 76);
        gp.getObj()[9].setWorldY(gp.getTileSize() * 33);
    }

    public void setNPC() {
        Random random = new Random();
        // create 1 npc
        for (int i = 0; i < gp.getNpc().length; i++) {
            gp.getNpc()[i] = new NPC(gp);
            gp.getNpc()[i].setWorldX(gp.getTileSize() * random.nextInt(99));
            gp.getNpc()[i].setWorldY(gp.getTileSize() * random.nextInt(99));
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
        int[][] windigoCoords = {{74, 12}, {69, 9}, {54, 7}, {65, 24}, {50, 20}, {32, 25}, {40, 9}, {20, 8}, {18, 18},
                {43, 34}, {57, 31}, {39, 26}, {48, 20}, {43, 12}, {79, 14}, {63, 3}, {55, 1}, {56, 55}, {65, 57},
                {72, 51}, {58, 36}, {59, 53}, {50, 66}, {96, 8}, {94, 30}, {36, 23}};

        // Spawns the monsters
        for (int i = 0; i < miasmaCoords.length; i++) {
            gp.getMonster()[i] = new MON_Miasma(gp);
            gp.getMonster()[i].setWorldX(gp.getTileSize() * miasmaCoords[i][0]);
            gp.getMonster()[i].setWorldY(gp.getTileSize() * miasmaCoords[i][1]);
        }
        for (int i = 0; i < windigoCoords.length; i++) {
            gp.getMonster()[i] = new MON_Windigo(gp);
            gp.getMonster()[i].setWorldX(gp.getTileSize() * windigoCoords[i][0]);
            gp.getMonster()[i].setWorldY(gp.getTileSize() * windigoCoords[i][1]);
        }


        gp.getMonster()[1] = new MON_Micipijiu(gp);
        gp.getMonster()[1].setWorldX(gp.getTileSize() * 48);
        gp.getMonster()[1].setWorldY(gp.getTileSize() * 48);

        gp.getMonster()[2] = new MON_Windigo(gp);
        gp.getMonster()[2].setWorldX(gp.getTileSize() * 49);
        gp.getMonster()[2].setWorldY(gp.getTileSize() * 49);
    }
}