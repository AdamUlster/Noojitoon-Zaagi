package main;

import entity.NPC;
import monster.MON_Miasma;
import monster.MON_Micipijiu;
import monster.MON_Windigo;
import object.OBJ_Totem;
import object.OBJ_Wall;

import java.util.Random;

//SET UP ALL ENTITIES, NPCs, MONSTERS, PLAYERS, OBJECTS, ETC.
public class AssetSetter {
    private GamePanel gp;

    public AssetSetter(GamePanel gp) { // constructor
        this.gp = gp;
    }
  
//    OBJECT CREATION METHOD
    void setObject() {
        gp.getObj()[0] = new OBJ_Totem(gp);
        gp.getObj()[0].setWorldX(gp.getTileSize() * 36);
        gp.getObj()[0].setWorldY(gp.getTileSize() * 95);

        gp.getObj()[1] = new OBJ_Totem(gp);
        gp.getObj()[1].setWorldX(gp.getTileSize() * 14);
        gp.getObj()[1].setWorldY(gp.getTileSize() * 4);

//        2D ARRAYS FOR COORDINATES OF TOTEMS AND WALLS
        int[][] totemCoords = {{2,55}, {14,4}, {36,95}, {76,33}};
        int[][] wallCoords = { {17,76}, {18,76}, {19,76}, {20,76}, {18,77}, {19,77}};

        // LOOP THROUGH EVERY COORDINATE IN BOTH ARRAYS, ADD TO OBJECT LIST IN GAME PANEL
        //TOTEMS
        for (int i = 0; i < totemCoords.length; i++) {
            gp.obj[i] = new OBJ_Totem(gp); // spawns a totem
            gp.obj[i].worldX = gp.tileSize * totemCoords[i][0];
            gp.obj[i].worldY = gp.tileSize * totemCoords[i][1];
        }
        // WALLS
        for (int i = totemCoords.length; i < (totemCoords.length + wallCoords.length); i ++) {
            gp.obj[i] = new OBJ_Wall(gp); // spawns the wall
            gp.obj[i].worldX = gp.tileSize * wallCoords[i - totemCoords.length][0];
            gp.obj[i].worldY = gp.tileSize * wallCoords[i - totemCoords.length][1];
        }
    }

//    NPC CREATIONG METHOD
    public void setNPC() {
        //CREATES THIRTY BUTTERFLY SPRITES IN RANDOM LOCATIONS IN THE MAP, THAT FLY AROUND. FOR VISUAL PURPOSES CUZ
        // THEY LOOK REALLY COOL
        Random random = new Random();
        // CREATE HOWEVER MANY NPCs ARE CREATED INSIDE GAME PANEL CLASS
        for (int i = 0; i < gp.getNpc().length; i++) {
            gp.getNpc()[i] = new NPC(gp);
            gp.getNpc()[i].setWorldX(gp.getTileSize() * random.nextInt(99));
            gp.getNpc()[i].setWorldY(gp.getTileSize() * random.nextInt(99));
        }
    }

    public void setMonster() {

//        2D COORDINATE ARRAYS FOR MIASMA, WINDIGO, AND MICIPJIU MONSTER SPRITES
//        MIASMA
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
//        WINDIGO
        int[][] windigoCoords = {{74, 12}, {69, 9}, {54, 7}, {65, 24}, {50, 20}, {32, 25}, {40, 9}, {20, 8}, {18, 18},
                {43, 34}, {57, 31}, {39, 26}, {48, 20}, {43, 12}, {79, 14}, {63, 3}, {55, 1}, {56, 55}, {65, 57},
                {72, 51}, {58, 36}, {59, 53}, {50, 66}, {96, 8}, {94, 30}, {36, 23}};
//        MICIPIJIU
        int[][] micipijiuCoords = {{48,48}};

        //ITERATE THROUGH MONSTER COORDINATE ARRAYS AND CREATE THEM
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

        for (int i = miasmaCoords.length; i < (miasmaCoords.length + windigoCoords.length); i++) {
            gp.monster[i] = new MON_Windigo(gp);
            gp.monster[i].worldX = gp.tileSize * windigoCoords[i - miasmaCoords.length][0];
            gp.monster[i].worldY = gp.tileSize * windigoCoords[i - miasmaCoords.length][1];
        }
        for (int i =(miasmaCoords.length + windigoCoords.length); i < (miasmaCoords.length + windigoCoords.length + micipijiuCoords.length); i++) {
            gp.monster[i] = new MON_Micipijiu(gp);
            gp.monster[i].worldX = gp.tileSize * micipijiuCoords[i - miasmaCoords.length - windigoCoords.length][0];
            gp.monster[i].worldY = gp.tileSize * micipijiuCoords[i - miasmaCoords.length - windigoCoords.length][1];
        }
    }
}