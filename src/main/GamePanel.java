package main;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import tile.Map;
import tile.TileManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    public final int originalTileSize = 32;
//    32 X 32 MEANS THE GAME OPERATES WITH OBJECTS OF 32 PIXEL SIZED TILES, OBJECTS, SPRITES, ETC.
    public final int scale = 3;//SCALING FACTOR, NOW TILES ARE TECHNICALLY 96 X 96 PIXELS BIG
//    ALL ENTITIES CREATED WILL AS IF THEY ARE 32X32 BUT WILL BE SCALED UP THREEFOLD

    public final int tileSize = originalTileSize * scale;//96X96 IS THE ACTUAL SIZE OF TILES IN PIXELS

    //SCREEN SIZE

    final int maxScreenCol = 16;//16 TILES CROSS
    final int maxScreenRow = 12;//12 TILES VERTICALLY

//    HOW BIG IN PIXELS WILL THE SCREEN BE AS REPRESENTED BY THE SIZE OF THE TILE AND THE NUMBER OF TILES
    public final int screenWidth = tileSize * maxScreenCol;//768 PIXELS HORIZONTALLY
    public final int screenHeight = tileSize * maxScreenRow;//576 PIXELS VERTICALLY

    //WORLD MAP SETTINGS
//    SETS THE BORDERS OF THE WORLD IN TERMS OF TILES
//    CREATES A MAP OF 100X100 TILES
    public final int maxWorldCol = 100;//sets the borders of the world in terms of tiles
    public final int maxWorldRow = 100;


    //FPS
    int FPS = 60;//GAME RUNS AT 60 FRAMES PER SECOND, OTHERWISE, THE GAME WOULD RUN TOO FAST

    // SYSTEM
    public TileManager tileM = new TileManager(this); // CREATE TILE MANGEGER CLASS
    KeyHandler keyH = new KeyHandler(this);//CALL KEY HANDLER CLASS TO CREATE KEY AND MOUSE LISTENER
    public CollisionChecker cChecker = new CollisionChecker(this);//CREATE COLLISION CHECKER CLASS
    public AssetSetter aSetter = new AssetSetter(this); // CREATE ASSET SETTER CLASS
    public UI ui = new UI(this);//CREATE UI CLASS

//    CREATE PATHFINDER
    public PathFinder pFinderToTotem = new PathFinder(this);
    public PathFinder pFinderToPlayer = new PathFinder(this);

    public Map map = new Map(this); // CREATES THE MAP
    Thread gameThread;//CALL ON THREAD CLASS, THAT ALLOWS FOR GAME LOGIC TO BE RUN AGAIN AND AGAIN

    // ENTITIES AND OBJECTS
    public Player player = new Player(this, keyH);//CREATE PLPAYER
    public Entity[] obj = new Entity[10]; // CREATE 10 OBJECTS
    public Entity[] npc = new Entity[50];// CREATE 50 NPCs
    public Entity[] monster = new Entity[170];//CREATE 170
    public ArrayList<Entity> projectileList = new ArrayList<>(); // CREATE ARRAY LIST TO STORE ALL THE PROJECTILES
    public ArrayList<Entity> targetProjectileList = new ArrayList<>(); // CREATE LIST TO STORE TARGET PROJECTILES
    public ArrayList<Entity> entityList = new ArrayList<>(); // CREATE ARRAY LIST TO STORE ALL ENTITIES

//    SET DEFAULT VALUES FOR GAME PANEL
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));// SET SCREEN DIMENSIONS
        this.setBackground(Color.black);//BACKGROUND COLOURS SET TO BLACK

//        ALL GRAPHICS ARE NOT BUFFERED, MEANING ON EACH FRAME THE SCREEN GETS RENDERED BEFORE IT GETS DISPLAYED ALL
//        AT ONCE TO IMPROVE RENDERING PERFORMANCE
        this.setDoubleBuffered(true);

//        ADD KEY LISTENER, MOUSE LISTENER, AND FOCUSES GAMEPANEL TO KEY INPUTS
        this.addKeyListener(keyH);
        this.addMouseListener(keyH);
        this.setFocusable(true);
    }

//    SET UP GAME BEFORE PLAYER CAN MOVE
    public void setupGame() {
        ui.showLoadingMessage("Loading... Please Wait");// DISPLAYS LOADING MESSAGE EVERYTHING IS DONE LOADING
        aSetter.setObject();//PLACE OBJECTS ONTO MAP
        aSetter.setNPC();//PLACE NPCs ONTO MAP
        aSetter.setMonster();//PLACE MONSTERS ONTO MAP
        ui.loadingMessageOn = false; // MAKE LOADING MESSAGE DISAPPEAR
    }

//    START CORE LOGIC WHEN THE PROGRAM STARTS
    public void startGameThread() {//starts core logic when the program starts
        gameThread = new Thread(this);//CREATE THE GAME THREAD THAT WILL RUN FOREVER
        gameThread.start();//START EHT GAME LOGIC
    }

    @Override
//    CORE GAME LOOP, ALL THE ACTIONS DONE IN THE GAME ARE RUN HERE, IT IS MORE EFFICIENT THAN RUNNING A FOREVER LOOP
    public void run() {

        double drawInterval = 1000000000 / FPS;// 1/60TH OF A SECOND, GAME NOW RUNS AT 60 FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {//WILL REPEAT AS LONG AS GAME THREAD IS ACTIVE

//            ENSURES GAME OPERATES AT 60 FPS
            currentTime = System.nanoTime();//GET CURRENT TIME IN NANOSECONDS

//            DELTA COUNTS UPWARDS IN A LINEAR PATTERN WAITING UP THE REMAINING TIME
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

//            WHEN DELTA HAS PASSED 1, REPAINT EVERYTHING AND RESET DELTA
            if (delta >= 1) {
                update();//UPDATE ALL INFORMATION TO BE PRINTED
                repaint();//PRINT GAME PANEL AGAIN
                delta--;//RESET DELTA
            }
        }
    }

//    UPDATE INFORMATION
    public void update() {
        //UPDATE PLAYER
        player.update();

        // UPDATE ALL NPCs
        for (int i = 0; i < npc.length; i++) {
            if (npc[i] != null) {
                npc[i].update();
            }
        }

//        UPDATE MONSTERS
        for (int i = 0; i < monster.length; i++) {
            if (monster[i] != null) {
                monster[i].update();
            }
        }

//        UPDATE PROJECTILES
        for (int i = 0; i < projectileList.size(); i++) {
            if (projectileList.get(i) != null) { // if the projectile exists
                if (projectileList.get(i).alive) {
                    projectileList.get(i).update();
                }
                if (projectileList.get(i).alive == false) {
                    projectileList.remove(i); // removes the projectile if it is no longer alive
                }
            }
        }

//        UPDATE TARGET PROJECTILES
        for (int i = 0; i < targetProjectileList.size(); i++) {
//            CHECK IF PROJECTILE EXISTS
            if (targetProjectileList.get(i) != null) {
                if (targetProjectileList.get(i).alive) {
                    System.out.println(targetProjectileList.get(i).toString() + " " + i);
                    targetProjectileList.get(i).update();
                }
                if (targetProjectileList.get(i).alive == false) {
                    targetProjectileList.remove(i); // REMOVES PROJECTILE IF IT IS NO LONGER ALIVE
                }
            }
        }
    }

//    DRAW SCREEN WITH UPDATED INFORMATION
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

//        USE 2D GRAPHICS CLASS TO MAKE THINGS EASIER
        Graphics2D g2 = (Graphics2D) g;

        //DEBUGGING STUFF
//        PART OF DISPLAYING TIME TAKEN TO DRAW ALL COMPONENTS
        long drawStart = 0;
        if (keyH.checkDrawTime == true) {
            drawStart = System.nanoTime();
        }

//        DRAWING ORDER, THE ORDER IN WHICH TO DRAW THINGS HAS BEEN PREDETERMINED IN ORDER TO PREVENT OVERLAPPING

//        DRAW TILES FIRST TO PREVENT OVERLAPPING
        tileM.draw(g2);

//        ADD PLAYER TO ENTITY LIST
        entityList.add(player);

//        ADD EVERY NPC TO ENTITY LIST
        for (int i = 0; i < npc.length; i++) {
            if (npc[i] != null) {
                entityList.add(npc[i]);
            }
        }

//        ADD EVERY OBJECT TO ENTITY LIST
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                entityList.add(obj[i]);
            }
        }

//        ADD EVERY MONSTER TO ENTITY LIST
        for (int i = 0; i < monster.length; i++) {
            if (monster[i] != null) {
                entityList.add(monster[i]);
            }
        }

//        ADD EVERY PROJECTILE TO THE ENTITY LIST
        for (int i = 0; i < projectileList.size(); i++) {
            if (projectileList.get(i) != null) {
                entityList.add(projectileList.get(i));
            }
        }

//        ADD EVERY TARGET PROJECTILE TO THE ENTITY LIST
        for (int i = 0; i < targetProjectileList.size(); i++) { // adds every target projectile to the entity list
            if (targetProjectileList.get(i) != null) {
                entityList.add(targetProjectileList.get(i));
            }
        }

//        SORT ENTITIES BY THEIR WORLD Y VALUES
        Collections.sort(entityList, new Comparator<Entity>() {

            @Override
            public int compare(Entity e1, Entity e2) {
//                COMPARES THE TWO ENTITY'S WORLD Y VALUES
                int result = Integer.compare(e1.worldY, e2.worldY);
                return result;
            }
        });

//        DRAW ENTITIES BASED ON WORLD Y VALUES (ONES HIGHER UP ON THE SCREEN ARE DRAWN FIRST TO AVOID CLIPPING THE
//        OTHER ENTITIES
        for (int i = 0; i < entityList.size(); i++) {
            entityList.get(i).draw(g2);
        }
//        RESETS THE ENTITY LIST SO THAT IT DOESN'T KEEP ADDING THE SAME ENTITIES TO THE LIST EVERY TIME THE PAINT
//        COMPONENT METHOD IS CALLED
        entityList.clear();

//        DRAWS MINI MAP BEFORE UI SO UI TEXT IS NOT HIDDEN
        map.drawMiniMap(g2);

        // DRAWS UI
        ui.draw(g2);

//        DRAW MAP SCREEN IF M KEY HAS BEEN PRESSED
        if (keyH.displayMap) {
            map.drawFullMapScreen(g2);
        }

        //DEBUGGING STUFF
//        DISPLAYS TIME TAKEN TO DRAW EVERYTHING BY PRESSING T ON KEYBOARD
        if (keyH.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(30F));
            g2.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw Time: " + passed);
        }

//        DISPOSES OF G2 TO SAVE PROCESSING POWER
        g2.dispose();
    }
}