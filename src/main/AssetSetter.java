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
//        2D ARRAYS FOR COORDINATES OF TOTEMS AND WALLS
        int[][] totemCoords = {{76,33}, {14,4}, {2,55}, {36,95}};
        int[][] wallCoords = { {17,76}, {18,76}, {19,76}, {20,76}, {18,77}, {19,77}};

        // LOOP THROUGH EVERY COORDINATE IN BOTH ARRAYS, ADD TO OBJECT LIST IN GAME PANEL
        //TOTEMS
        for (int i = 0; i < totemCoords.length; i++) {
            gp.getObj()[i] = new OBJ_Totem(gp); // spawns a totem
            gp.getObj()[i].setWorldX(gp.getTileSize() * totemCoords[i][0]);
            gp.getObj()[i].setWorldY(gp.getTileSize() * totemCoords[i][1]);
        }
        // WALLS
        for (int i = totemCoords.length; i < (totemCoords.length + wallCoords.length); i ++) {
            gp.getObj()[i] = new OBJ_Wall(gp); // spawns the wall
            gp.getObj()[i].setWorldX(gp.getTileSize() * wallCoords[i - totemCoords.length][0]);
            gp.getObj()[i].setWorldY(gp.getTileSize() * wallCoords[i - totemCoords.length][1]);
        }
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

        //        2D COORDINATE ARRAYS FOR MIASMA, WINDIGO, AND MICIPJIU MONSTER SPRITES
//        MIASMA
        /*
        int[][] miasmaCoords = {{54,56},{57,56},{62,59},{66,62},{76,52},{73,50},{73,42},{65,43},{67,38},{61,80},{48,
                82},{51,88},{61,87},{56,93},{61,93},{72,71},{74,69},{79,68},{79,79},{81,88},{86,97},{88,91},{90,94},
                {95,89},{93,85},{96,82},{91,75},{93,67},{93,59},{96,52},{82,54},{81,50},{81,42},{87,29},{85,33},{81,
                27},{80,27},{82,32},{77,38},{78,29},{92,41},{95,38},{90,29},{97,29},{96,23},{95,17},{96,6},{94,6},{92
                ,6},{91,18},{88,18},{88,9},{86,9},{84,15},{22,85},{30,78},{32,74},{37,98},{3,77},{5,92},{20,96},{16,
                98}};
        */
//        for playtesting
        int[][]  miasmaCoords = {{54,56},{66,62},{65,43},{48,82},{61,93},{81,88},{96,82},{96,52},{81,27},{78,29},{96,
                23},{94,6},{30,78},{37,98}};
//        WINDIGO
        /*
        int[][] windigoCoords ={{79,6},{77,4},{76,7},{75,17},{74,21},{70,17},{65,3},{60,3},{51,3},{52,7},{48,5},{70,
                24},{66,26},{65,22},{49,18},{48,23},{45,15},{40,26},{35,28},{31,26},{35,16},{37,13},{27,4},{24,9},{23,4
        },{20,9},{19,3},{16,7},{36,79},{38,89},{28,94},{21,81},{16,81},{10,85},{3,91},{16,92},{18,96},{53,40},{50,40}
                ,{54,37},{54,33},{43,34},{40,36},{43,40}};

         */

        //playtesting monsters
        int[][] windigoCoords = {{75,17},{70,17},{52,7},{65,22},{40,26},{23,4},{36,79},{18,96},{40,36}};
//        MICIPIJIU

        /*
        int[][] micipijiuCoords ={{48,46},{45,45},{33,43},{33,48},{39,50},{36,56},{35,62},{42,5},{44,64},{46,66},{46,60
        },{55,62},{60,64},{7,1},{4,2},{7,4},{1,9},{3,11},{3,18},{7,18},{9,22},{3,27},{7,27},{4,33},{1,37},{5,39},{2,
                41},{17,29},{17,31},{28,35},{26,37},{12,40},{17,40},{15,43},{28,43},{19,47},{24,50},{22,54},{26,53},
                {5,44},{2,46},{2,54},{4,56},{14,54},{2,62},{5,64},{2,70},{6,70},{10,70},{12,60},{14,62},{23,61},{22,
                65},{31,64},{33,67},{16,69},{20,71},{24,69},{15,77},{2,80},{41,76},{40,83},{23,92},{18,98}};
        */
        //playtesting monsters
        int[][] micipijiuCoords = {{36,56},{46,60},{1,9},{1,37},{5,39},{17,31},{24,50},{26,53},{2,46},{2,70},{23,61}};

        //ITERATE THROUGH MONSTER COORDINATE ARRAYS AND CREATE THEM
        for (int i = 0; i < miasmaCoords.length; i++) {
            gp.getMonster()[i] = new MON_Miasma(gp);
            gp.getMonster()[i].setWorldX(gp.getTileSize() * miasmaCoords[i][0]);
            gp.getMonster()[i].setWorldY(gp.getTileSize() * miasmaCoords[i][1]);
        }
        for (int i = miasmaCoords.length; i < (miasmaCoords.length + windigoCoords.length); i++) {
            gp.getMonster()[i] = new MON_Windigo(gp);
            gp.getMonster()[i].setWorldX(gp.getTileSize() * windigoCoords[i - miasmaCoords.length][0]);
            gp.getMonster()[i].setWorldY(gp.getTileSize() * windigoCoords[i - miasmaCoords.length][1]);
        }
        for (int i =(miasmaCoords.length + windigoCoords.length); i < (miasmaCoords.length + windigoCoords.length + micipijiuCoords.length); i++) {
            gp.getMonster()[i] = new MON_Micipijiu(gp);
            gp.getMonster()[i].setWorldX(gp.getTileSize() * micipijiuCoords[i - miasmaCoords.length - windigoCoords.length][0]);
            gp.getMonster()[i].setWorldY(gp.getTileSize() * micipijiuCoords[i - miasmaCoords.length - windigoCoords.length][1]);
        }
    }
}