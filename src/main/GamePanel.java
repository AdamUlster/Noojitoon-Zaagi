package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    //screen settings
    public final int originalTileSize = 32;
    //32 x 32 means the game operates with objects of 32 pixel tiles, so objects, sprites etc.
    public final int scale = 3;//DO NOT CHANGE, WILL SCREW EVERYTHING UP
    //all the characters we create will be as if they are 32x32 but will be scaled up threefold

    public final int tileSize = originalTileSize * scale;//48x48 actual tile size that will be displayed

    //determine the size of the screen in terms of ties
    //changeable values
    final int maxScreenCol = 16;//16 tiles across
    final int maxScreenRow = 12;//12 tiles vertically

    //how big in pixels will the screen be as represented by the size of the tile and the number of tiles
    public final int screenWidth = tileSize * maxScreenCol;//768 pixels horizontally
    public final int screenHeight = tileSize * maxScreenRow;//576 pixels vertically

    //world map settings
    //change these values to change the map size
    public final int maxWorldCol = 100;//sets the borders of the world in terms of tiles
    public final int maxWorldRow = 100;//sets the border of the world in terms of tiles

    public final int worldWidth = tileSize * maxWorldCol;//sets the border of the world in pixels
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int FPS = 60;

    // System
    TileManager tileM = new TileManager(this); // passes the game panel
    KeyHandler keyH = new KeyHandler(this);//call on the keyhandle class to create the keylistener
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this); // passes the game panel as a parameter
    public UI ui = new UI(this);

    Thread gameThread;//repeats a process again and again

    // Entities and objects
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[10]; // to display up to 10 objects at the same time
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[10];
    public ArrayList<Entity> projectileList = new ArrayList<>(); // holds the projectiles
    public ArrayList<Entity> targetProjectileList = new ArrayList<>(); // holds the target projectiles
    ArrayList<Entity> entityList = new ArrayList<>(); // creates an array list to store all the entities

    public GamePanel() {//set default values for the gamepanel
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));//set screen dimensions
        this.setBackground(Color.black);//changeable colour via rgb values
        this.setDoubleBuffered(true);//all graphics are now done buffered, ie the screen gets rendered before being displayed
        //improves rendering performance
        this.addKeyListener(keyH); // adds the key listener to the gamepanel
        this.addMouseListener(keyH); // adds the mouse listener to the gamepanel
        this.setFocusable(true);//changes the focus of the gamepanel to the key inputs
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
    }

    public void startGameThread() {//starts core logic when the program starts
        gameThread = new Thread(this);//create the thread that will run
        gameThread.start();
    }

    @Override
    public void run() {//core game loop, all the actions done, better than running a forever loop for game logic

        double drawInterval = 1000000000 / FPS;//1/60th of a second, intervals are now at 60 fps
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {//will repeat the game logic forever

            //ensures game operates at 60 fps
            currentTime = System.nanoTime();//get current time in nanosecondd

            delta += (currentTime - lastTime) / drawInterval;//delta counts upwards in a linear pattern, waiting up
            // the remaining time
            lastTime = currentTime;

            if (delta >= 1) {//when delta has passed 1, repaint everything and reset delta
                update();
                repaint();
                delta--;
            }
        }
    }

    // update information
    public void update() {
        //UPDATE PLAYER
        player.update();

        // update NPC
        for (int i = 0; i < npc.length; i++) {
            if (npc[i] != null) {
                npc[i].update();
            }
        }

       // update monster
        for (int i = 0; i < monster.length; i++) {
            if (monster[i] != null) {
                monster[i].update();
            }
        }

        // update projectile
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

        // update target projectile
        for (int i = 0; i < targetProjectileList.size(); i++) {
            if (targetProjectileList.get(i) != null) { // if the projectile exists
                if (targetProjectileList.get(i).alive) {
                    System.out.println(targetProjectileList.get(i).toString() + " " + i);
                    targetProjectileList.get(i).update();
                }
                if (targetProjectileList.get(i).alive == false) {
                    targetProjectileList.remove(i); // removes the projectile if it is no longer alive
                }
            }
        }
    }

    //draw screen with updated information
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;//better graphics class that makes things easier

        //DEBUG STUFF
        long drawStart = 0;
        if (keyH.checkDrawTime == true) {
            drawStart = System.nanoTime();
        }

        tileM.draw(g2); // tiles are drawn before the player so to prevent layering issues

        entityList.add(player); // adds the player to the entity list

        for (int i = 0; i < npc.length; i++) { // adds every npc to the entity list
            if (npc[i] != null) {
                entityList.add(npc[i]);
            }
        }

        for (int i = 0; i < obj.length; i++) { // adds every object to the entity list
            if (obj[i] != null) {
                entityList.add(obj[i]);
            }
        }

        for (int i = 0; i < monster.length; i++) { // adds every monster to the entity list
            if (monster[i] != null) {
                entityList.add(monster[i]);
            }
        }

        for (int i = 0; i < projectileList.size(); i++) { // adds every projectile to the entity list
            if (projectileList.get(i) != null) {
                entityList.add(projectileList.get(i));
            }
        }

        for (int i = 0; i < targetProjectileList.size(); i++) { // adds every target projectile to the entity list
            if (targetProjectileList.get(i) != null) {
                entityList.add(targetProjectileList.get(i));
            }
        }

        // Sorts the entities by their world y values
        Collections.sort(entityList, new Comparator<Entity>() {

            @Override
            public int compare(Entity e1, Entity e2) {
                int result = Integer.compare(e1.worldY, e2.worldY); // compares the two entity's world y values
                return result;
            }
        });

        // draws the entities based on their world y values (the ones further up are drawn first to avoid clipping the other entities)
        for (int i = 0; i < entityList.size(); i++) {
            entityList.get(i).draw(g2);
        }
        entityList.clear(); // resets the entity list so that it doesn't keep adding the same entities to the list every time the paintComponent method is called

        // Draws the UI
        ui.draw(g2);

        //DEBUG STUFF
        if (keyH.checkDrawTime == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw Time: " + passed);
        }
        g2.dispose();//saves processing power
    }
}