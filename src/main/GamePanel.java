package main;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import java.awt.*;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    //screen settings
    final int originalTileSize = 32;
    //32 x 32 means the game operates with objects of 32 pixel tiles, so objects, sprites etc.
    final int scale = 3;//changeable number
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
    KeyHandler keyH = new KeyHandler();//call on the keyhandle class to create the keylistener
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this); // passes the game panel as a parameter
    public UI ui = new UI(this);

    Thread gameThread;//repeats a process again and again

    // Entities and objects
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10]; // to display up to 10 objects at the same time
    public Entity npc[] = new Entity[10];

    public GamePanel() {//set default values for the gamepanel
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));//set screen dimensions
        this.setBackground(Color.black);//changeable colour via rgb values
        this.setDoubleBuffered(true);//all graphics are now done buffered, ie the screen gets rendered before being displayed
        //improves rendering performance
        this.addKeyListener(keyH);//adds the key listener to the gamepanel
        this.setFocusable(true);//changes the focus of the gamepanel to the key inputs
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
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

    //update information
    public void update() {
        //UPDATE PLAYER
        player.update();

        //UPDATE NPC
        for (int i = 0; i < npc.length; i++) {
            if (npc[i] != null) {
                npc[i].update();
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

        tileM.draw(g2);//tiles are drawn before the player so to prevent layering issues

        // Draws the object
        for (int i = 0; i < obj.length; i++) { // draws every object
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        for (int i = 0; i < npc.length;i++) {
            if(npc[i] != null) {
                npc[i].draw(g2);
            }
        }

        // Draws the player
        player.draw(g2);

        // Draws the UI
        ui.draw(g2);

        //DEBUG STUFF
        if (keyH.checkDrawTime == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw TIme: " + passed);
        }

        g2.dispose();//saves processing power
    }
}