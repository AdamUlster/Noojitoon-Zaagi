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
    GamePanel gp;

    public AssetSetter(GamePanel gp) { // constructor
        this.gp = gp;
    }

//    OBJECT CREATION METHOD
    public void setObject() {
        gp.obj[0] = new OBJ_Totem(gp);
        gp.obj[0].worldX = gp.tileSize * 36;
        gp.obj[0].worldY = gp.tileSize * 95;

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

        gp.obj[2] = new OBJ_Totem(gp); // spawns a totem
        gp.obj[2].worldX = gp.tileSize * 2;
        gp.obj[2].worldY = gp.tileSize * 55;

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

//    NPC CREATIONG METHOD
    public void setNPC() {
        //CREATES THIRTY BUTTERFLY SPRITES IN RANDOM LOCATIONS IN THE MAP, THAT FLY AROUND. FOR VISUAL PURPOSES CUZ
        // THEY LOOK REALLY COOL
        Random random = new Random();
        // CREATE HOWEVER MANY NPCs ARE CREATED INSIDE GAME PANEL CLASS
        for (int i = 0; i < gp.npc.length; i++) {
            gp.npc[i] = new NPC(gp);
            gp.npc[i].worldX = gp.tileSize * random.nextInt(99);
            gp.npc[i].worldY = gp.tileSize * random.nextInt(99);
        }

    }

    public void setMonster() {

//        2D COORDINATE ARRAYS FOR MIASMA, WINDIGO, AND MICIPJIU MONSTER SPRITES
//        MIASMA
        int[][] miasmaCoords = {{54,56},{57,56},{62,59},{66,62},{76,52},{73,50},{73,42},{65,43},{67,38},{61,80},{48,
                82},{51,88},{61,87},{56,93},{61,93},{72,71},{74,69},{79,68},{79,79},{81,88},{86,97},{88,91},{90,94},
                {95,89},{93,85},{96,82},{91,75},{93,67},{93,59},{96,52},{82,54},{81,50},{81,42},{87,29},{85,33},{81,
                27},{80,27},{82,32},{77,38},{78,29},{92,41},{95,38},{90,29},{97,29},{96,23},{95,17},{96,6},{94,6},{92
                ,6},{91,18},{88,18},{88,9},{86,9},{84,15},{22,85},{30,78},{32,74},{37,98},{3,77},{5,92},{20,96},{16,
                98}};
//        WINDIGO
        int[][] windigoCoords ={{79,6},{77,4},{76,7},{75,17},{74,21},{70,17},{65,3},{60,3},{51,3},{52,7},{48,5},{70,
                24},{66,26},{65,22},{49,18},{48,23},{45,15},{40,26},{35,28},{31,26},{35,16},{37,13},{27,4},{24,9},{23,4
        },{20,9},{19,3},{16,7},{36,79},{38,89},{28,94},{21,81},{16,81},{10,85},{3,91},{16,92},{18,96},{53,40},{50,40}
                ,{54,37},{54,33},{43,34},{40,36},{43,40}};
//        MICIPIJIU
        int[][] micipijiuCoords ={{48,46},{45,45},{33,43},{33,48},{39,50},{36,56},{35,62},{42,5},{44,64},{46,66},{46,60
        },{55,62},{60,64},{7,1},{4,2},{7,4},{1,9},{3,11},{3,18},{7,18},{9,22},{3,27},{7,27},{4,33},{1,37},{5,39},{2,
                41},{17,29},{17,31},{28,35},{26,37},{12,40},{17,40},{15,43},{28,43},{19,47},{24,50},{22,54},{26,53},
                {5,44},{2,46},{2,54},{4,56},{14,54},{2,62},{5,64},{2,70},{6,70},{10,70},{12,60},{14,62},{23,61},{22,
                65},{31,64},{33,67},{16,69},{20,71},{24,69},{15,77},{2,80},{41,76},{40,83},{23,92},{18,98}};

        System.out.println(miasmaCoords.length + windigoCoords.length + micipijiuCoords.length);
        //ITERATE THROUGH MONSTER COORDINATE ARRAYS AND CREATE THEM
        for (int i = 0; i < miasmaCoords.length; i++) {
            gp.monster[i] = new MON_Miasma(gp);
            gp.monster[i].worldX = gp.tileSize * miasmaCoords[i][0];
            gp.monster[i].worldY = gp.tileSize * miasmaCoords[i][1];
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