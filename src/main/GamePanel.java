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

    //screen settings
    private final int originalTileSize = 32;
    //32 x 32 means the game operates with objects of 32 pixel tiles, so objects, sprites etc.
    private final int scale = 3;//DO NOT CHANGE, WILL SCREW EVERYTHING UP
    //all the characters we create will be as if they are 32x32 but will be scaled up threefold

    private final int tileSize = originalTileSize * scale;//48x48 actual tile size that will be displayed

    //determine the size of the screen in terms of ties
    //changeable values
    private final int maxScreenCol = 16;//16 tiles across
    private final int maxScreenRow = 12;//12 tiles vertically

    //how big in pixels will the screen be as represented by the size of the tile and the number of tiles
    private final int screenWidth = tileSize * maxScreenCol;//768 pixels horizontally
    private final int screenHeight = tileSize * maxScreenRow;//576 pixels vertically

    //world map settings
    //change these values to change the map size
    private final int maxWorldCol = 100;//sets the borders of the world in terms of tiles
    private final int maxWorldRow = 100;//sets the border of the world in terms of tiles

    //FPS
    private int FPS = 60;

    // System
    private TileManager tileM = new TileManager(this); // passes the game panel
    private KeyHandler keyH = new KeyHandler(this);//call on the keyhandle class to create the keylistener
    private CollisionChecker cChecker = new CollisionChecker(this);
    private AssetSetter aSetter = new AssetSetter(this); // passes the game panel as a parameter
    private UI ui = new UI(this);
    private PathFinder pFinderToTotem = new PathFinder(this);
    private PathFinder pFinderToPlayer = new PathFinder(this);

    private Map map = new Map(this); // instantiates the map
    private Thread gameThread;//repeats a process again and again

    // Entities and objects
    private Player player = new Player(this, keyH);
    private Entity[] obj = new Entity[10]; // to display up to 10 objects at the same time
    private Entity[] npc = new Entity[50];//create 50 npcs
    private Entity[] monster = new Entity[200];//create 200 monsters
    private ArrayList<Entity> projectileList = new ArrayList<>(); // holds the projectiles
    private ArrayList<Entity> targetProjectileList = new ArrayList<>(); // holds the target projectiles
    private ArrayList<Entity> entityList = new ArrayList<>(); // creates an array list to store all the entities

    public GamePanel() {//set default values for the gamepanel
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));//set screen dimensions
        this.setBackground(Color.black);//changeable colour via rgb values
        this.setDoubleBuffered(true);//all graphics are now done buffered, ie the screen gets rendered before being displayed
        //improves rendering performance
        this.addKeyListener(keyH); // adds the key listener to the gamepanel
        this.addMouseListener(keyH); // adds the mouse listener to the gamepanel
        this.setFocusable(true);//changes the focus of the gamepanel to the key inputs
    }

    void setupGame() {
        ui.showLoadingMessage("Loading... Please Wait");
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        ui.setLoadingMessageOn(false); // makes the loading message disappear after the monsters load
    }

    void startGameThread() {//starts core logic when the program starts
        gameThread = new Thread(this);//create the thread that will run
        gameThread.start();
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
                if (projectileList.get(i).isAlive()) {
                    projectileList.get(i).update();
                }
                if (projectileList.get(i).isAlive() == false) {
                    projectileList.remove(i); // removes the projectile if it is no longer alive
                }
            }
        }

//        UPDATE TARGET PROJECTILES
        for (int i = 0; i < targetProjectileList.size(); i++) {
            if (targetProjectileList.get(i) != null) { // if the projectile exists
                if (targetProjectileList.get(i).isAlive()) {
                    System.out.println(targetProjectileList.get(i).toString() + " " + i);
                    targetProjectileList.get(i).update();
                }
                if (targetProjectileList.get(i).isAlive() == false) {
                    targetProjectileList.remove(i); // removes the projectile if it is no longer alive
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
        if (keyH.isCheckDrawTime() == true) {
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
                int result = Integer.compare(e1.getWorldY(), e2.getWorldY()); // compares the two entity's world y values
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

        if (keyH.isDisplayMap()) {
            map.drawFullMapScreen(g2); // draws the map screen
        }

        //DEBUG STUFF
        if (keyH.isCheckDrawTime()) {
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

    // Get and set methods
    public int getOriginalTileSize() {
        return originalTileSize;
    }

    public int getScale() {
        return scale;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMaxScreenCol() {
        return maxScreenCol;
    }

    public int getMaxScreenRow() {
        return maxScreenRow;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getMaxWorldCol() {
        return maxWorldCol;
    }

    public int getMaxWorldRow() {
        return maxWorldRow;
    }

    public int getFPS() {
        return FPS;
    }

    public void setFPS(int FPS) {
        this.FPS = FPS;
    }

    public TileManager getTileM() {
        return tileM;
    }

    public KeyHandler getKeyH() {
        return keyH;
    }

    public CollisionChecker getCChecker() {
        return cChecker;
    }

    public AssetSetter getASetter() {
        return aSetter;
    }

    public UI getUi() {
        return ui;
    }

    public PathFinder getPFinderToTotem() {
        return pFinderToTotem;
    }

    public PathFinder getPFinderToPlayer() {
        return pFinderToPlayer;
    }

    public Map getMap() {
        return map;
    }

    public Thread getGameThread() {
        return gameThread;
    }

    public void setGameThread(Thread gameThread) {
        this.gameThread = gameThread;
    }

    public Player getPlayer() {
        return player;
    }

    public Entity[] getObj() {
        return obj;
    }

    public Entity[] getNpc() {
        return npc;
    }

    public Entity[] getMonster() {
        return monster;
    }

    public ArrayList<Entity> getProjectileList() {
        return projectileList;
    }

    public ArrayList<Entity> getTargetProjectileList() {
        return targetProjectileList;
    }

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }
}