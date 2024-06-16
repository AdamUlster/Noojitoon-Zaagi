package entity;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_EagleShot;
import object.OBJ_Water_Jet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Player extends Entity {
    private KeyHandler keyH;//call on keyhandler class

    private Spirit[] spirits = new Spirit[3]; // CREATE THREE SPIRITS
    private int currentSpiritIndex = 0; // KEEP TRACK ON CURRENT SPIRIT

  //POSITION RELATIVE TO PANEL
    private final int screenX;
    private final int screenY;

    private int numTotems = 0; // KEEP TRACK OF NUMBER OF TOTEMS PLAYER HAS COLLECTED

    //    SCALING FACTORS FOR PLAYER HIT BOXES
    private double bearHitboxScale = 0.5;//bear hit box scale
    private double eagleHitboxScale = 0.5;//eagle hit box scale
    private double turtleHitboxScale = 0.5;//turtle hit box scale
  
  //    ATTACKING ZONE, A HITBOX TO DETERMINE WHETHER AN ATTACK REGISTERS ON A MONSTER
    private double bearAttackBoxScaleSize = 1.25;
    private double eagleAttackBoxScaleSize = 1.25;
    private double turtleAttackBoxScaleSize = 1;

    //INDICES
    private int monsterIndex;

    //CHECKERS
    private boolean bearSpecialUnlocked = false;
    private boolean eagleSpecialUnlocked = false;
    private boolean turtleSpecialUnlocked = false;

//    SPIRIT STATES
    private boolean berserkerMode = false;// BEAR SPIRIT IN BERSERK MODE

    //COUNTERS
    private int invincibilityCounter = 0;
    private int primaryICD = 0;//internal cooldown for attacks
    private int secondaryICD = 0;//internal cooldown for special/secondary moves
    private int berserkerCounter = 0;//bear spirit berserker mode duration

    public Player(GamePanel gp, KeyHandler keyH) { //CREATE DEFAULT ATTRIBUTES (CONSTRUCTOR)

        super(gp); // CALL ENTITY CLASS
        this.keyH = keyH;// CALL ON KEY HANDLER CLASS

        //PUTS PLAYER INTO THE MIDDLE OF THE SCREEN
        screenX = gp.getScreenWidth() / 2 - (gp.getTileSize() / 2);
        screenY = gp.getScreenHeight() / 2 - (gp.getTileSize() / 2);

        setDefaultValues();// CALL ON DEFAULT CONSTRUCTOR METHOD

//        RETRIEVE ANIMATION SPRITES FOR MOVING, PRIMARY ATTACK, AND SPECIAL ATTACK
        getPlayerImage();
        getPlayerAttackImage();
        getPlayerSpecialAttackImage();
    }

    public Spirit getCurrentSpirit() { // RETURN CURRENT SPIRIT
        return spirits[currentSpiritIndex];
    }

    private void setDefaultValues() {{// CREATE DEFAULT VALUES TO SPAWN THE PLAYER

//        SPAWN PLAYER AT COORDINATES 53, 50, WHICH IS IN THE CENTER AREA
        setWorldX(gp.getTileSize() * 53); // sets the default position x-coordinate
        setWorldY(gp.getTileSize() * 50); //sets the default position y-coordinate
  
        setSpeed(6);//PLAYER MOVES AT 6 PIXELS A FRAME, OR APPROX. 2 TILES PER SECOND
        setDirection("right");// PLAYER FACES RIGHT BY DEFAULT
  
  //        CREATE TURTLE AND EAGLE PROJECTILES
        setProjectile(new OBJ_Water_Jet(gp));
        setTargetProjectile(new OBJ_EagleShot(gp));

//        INITIALIZE INDIVUDAL SPIRIT SPRITES AND THEIR HEALTH
        spirits[0] = new Spirit(gp, "Bear", 9, 9,
                (int) (gp.getTileSize() * (1.0 - bearHitboxScale)) / 2,
                (int) (gp.getTileSize() * (1.0 - bearHitboxScale)) / 2,
                (int) (gp.getTileSize() * bearHitboxScale),
                (int) (gp.getTileSize() * bearHitboxScale),
                (int) (gp.getTileSize() * bearAttackBoxScaleSize),
                (int) (gp.getTileSize() * bearAttackBoxScaleSize),
                1, 4);
        spirits[1] = new Spirit(gp, "Eagle", 5, 5,
                (int) (gp.getTileSize() * eagleHitboxScale) / 2,
                (int) (gp.getTileSize() * eagleHitboxScale) / 2,
                (int) (gp.getTileSize() * eagleHitboxScale),
                (int) (gp.getTileSize() * eagleHitboxScale),
                (int) (gp.getTileSize() * eagleAttackBoxScaleSize),
                (int) (gp.getTileSize() * eagleAttackBoxScaleSize),
                1, 4);
        spirits[2] = new Spirit(gp, "Turtle", 8, 8,
                (int) (gp.getTileSize() * (1.0 - turtleHitboxScale)) / 2,
                (int) (gp.getTileSize() * (1.0 - turtleHitboxScale)) / 2,
                (int) (gp.getTileSize() * turtleHitboxScale),
                (int) (gp.getTileSize() * turtleHitboxScale),
                (int) (gp.getTileSize() * turtleAttackBoxScaleSize),
                (int) (gp.getTileSize() * turtleAttackBoxScaleSize),
                1, 4);
        switchSpirit(0); // STARS THE GAME AS THE BEAR SPIRIT
    }

//    RESETS GAME IF ALL SPIRITS HAVE DIED                                     
    private void restoreSettings() {
        Arrays.fill(gp.getNpc(), null);
        Arrays.fill(gp.getMonster(), null);
      
      //CLEAR THE ENTIRE GAME
        gp.getProjectileList().clear();
        gp.getTargetProjectileList().clear();
        gp.getEntityList().clear();

        for (int i = 0; i < gp.getPlayer().spirits.length; i++) { // makes every spirit alive
            gp.getPlayer().spirits[i].setDead(false);
        }
        gp.getPlayer().setDying(false); // MAKES IT SO PLAYER IS NO LONGER DYING
      
      //        RESET NPC'S AND MONSTERS
        gp.getASetter().setNPC();
        gp.getASetter().setMonster();

        // RESETS THINGS DISPLAYED ON SCREEN
        keyH.setCheckDrawTime(false);
        keyH.setDisplayControls(false);
        keyH.setDisplayMap(false);
        gp.getMap().setMiniMapOn(false);
        gp.getPlayer().setOnPath(false);
        gp.getTileM().setDrawPath(false);

        setDefaultValues(); //        RESET GAME BACK TO START
    }

//    RETRIEVE PLAYER MOVEMENT IMAGES                                  
    private void getPlayerImage() { 
        Spirit currentSpirit = getCurrentSpirit(); // GET CURRENT SPIRIT

        //        SETS PLAYER'S IMAGES TO THE CURRENT SPIRIT'S IMAGES
        setUp1(currentSpirit.getUp1());
        setUp2(currentSpirit.getUp2());
        setDown1(currentSpirit.getDown1());
        setDown2(currentSpirit.getDown2());
        setLeft1(currentSpirit.getLeft1());
        setLeft2(currentSpirit.getLeft2());
        setRight1(currentSpirit.getRight1());
        setRight2(currentSpirit.getRight2());

//        USE SETUP METHOD FROM ENTITY CLASS TO RETRIEVE WALKING FILES DEPENDING ON THE CURRENT SPIRIT      
        if (currentSpirit.getName().equals("Bear")) { //BEAR IMAGES
            // call on setup method to find image files
            setUp1(setup("bear/bear_up", 1, 1));
            setUp2(setup("bear/bear_up_2", 1, 1));
            setDown1(setup("bear/bear_down", 1, 1));
            setDown2(setup("bear/bear_down_2", 1, 1));
            setLeft1(setup("bear/bear_left", 1, 1));
            setLeft2(setup("bear/bear_left_2", 1, 1));
            setRight1(setup("bear/bear_right", 1, 1));
            setRight2(setup("bear/bear_right_2", 1, 1));

        } else if (currentSpirit.getName().equals("Eagle")) {//EAGLE IMAGES
            setUp1(setup("eagle/eagle_up", 1, 1));
            setUp2(setup("eagle/eagle_up_2", 1, 1));
            setDown1(setup("eagle/eagle_down", 1, 1));
            setDown2(setup("eagle/eagle_down_2", 1, 1));
            setLeft1(setup("eagle/eagle_left", 1, 1));
            setLeft2(setup("eagle/eagle_left_2", 1, 1));
            setRight1(setup("eagle/eagle_right", 1, 1));
            setRight2(setup("eagle/eagle_right_2", 1, 1));
        } else if (currentSpirit.getName().equals("Turtle")) {//TURTLE IMAGES
            setUp1(setup("turtle/turtle_up", 1.25, 1.25));
            setUp2(setup("turtle/turtle_up_2", 1.25, 1.25));
            setDown1(setup("turtle/turtle_down", 1.25, 1.25));
            setDown2(setup("turtle/turtle_down_2", 1.25, 1.25));
            setLeft1(setup("turtle/turtle_left", 1.25, 1.25));
            setLeft2(setup("turtle/turtle_left_2", 1.25, 1.25));
            setRight1(setup("turtle/turtle_right", 1.25, 1.25));
            setRight2(setup("turtle/turtle_right_2", 1.25, 1.25));
        }
    }

//    USE SETUP METHOD FROM ENTITY CLASS TO RETRIEVE PRIMARY ATTACK FILES DEPENDING ON THE CURRENT SPIRIT                                     
    private void getPlayerAttackImage() {
        if (getCurrentSpirit().getName().equals("Bear")) {//BEAR IMAGES
            setAttackUp1(setup("bear/bear_up_attack_1", 1.25, 1.25));
            setAttackUp2(setup("bear/bear_up_attack_2", 1.25, 1.25));
            setAttackUp3(setup("bear/bear_up_attack_3", 1.25, 1.25));
            setAttackDown1(setup("bear/bear_down_attack_1", 1.25, 1.25));
            setAttackDown2(setup("bear/bear_down_attack_2", 1.25, 1.25));
            setAttackDown3(setup("bear/bear_down_attack_3", 1.25, 1.25));
            setAttackLeft1(setup("bear/bear_left_attack_1", 1.25, 1.25));
            setAttackLeft2(setup("bear/bear_left_attack_2", 1.25, 1.25));
            setAttackLeft3(setup("bear/bear_left_attack_3", 1.25, 1.25));
            setAttackRight1(setup("bear/bear_right_attack_1", 1.25, 1.25));
            setAttackRight2(setup("bear/bear_right_attack_2", 1.25, 1.25));
            setAttackRight3(setup("bear/bear_right_attack_3", 1.25, 1.25));
        }
        if (getCurrentSpirit().getName().equals("Eagle")) {//EAGLE IMAGES
            setAttackUp1(setup("eagle/eagle_up_attack_1", 1.25, 1.25));
            setAttackUp2(setup("eagle/eagle_up_attack_2", 1.25, 1.25));
            setAttackUp3(setup("eagle/eagle_up_attack_3", 1.25, 1.25));
            setAttackDown1(setup("eagle/eagle_down_attack_1", 1.25, 1.25));
            setAttackDown2(setup("eagle/eagle_down_attack_2", 1.25, 1.25));
            setAttackDown3(setup("eagle/eagle_down_attack_3", 1.25, 1.25));
            setAttackLeft1(setup("eagle/eagle_left_attack_1", 1.25, 1.25));
            setAttackLeft2(setup("eagle/eagle_left_attack_2", 1.25, 1.25));
            setAttackLeft3(setup("eagle/eagle_left_attack_3", 1.25, 1.25));
            setAttackRight1(setup("eagle/eagle_right_attack_1", 1.25, 1.25));
            setAttackRight2(setup("eagle/eagle_right_attack_2", 1.25, 1.25));
            setAttackRight3(setup("eagle/eagle_right_attack_3", 1.25, 1.25));
        }
        if (getCurrentSpirit().getName().equals("Turtle")) {//TURTLE IMAGES
            setAttackUp1(setup("turtle/turtle_up_attack_1", 1.7, 1.7));
            setAttackUp2(setup("turtle/turtle_up_attack_2", 1.7, 1.7));
            setAttackUp3(setup("turtle/turtle_up_attack_3", 1.7, 1.7));
            setAttackDown1(setup("turtle/turtle_down_attack_1", 1.7, 1.7));
            setAttackDown2(setup("turtle/turtle_down_attack_2", 1.7, 1.7));
            setAttackDown3(setup("turtle/turtle_down_attack_3", 1.7, 1.7));
            setAttackLeft1(setup("turtle/turtle_left_attack_1", 1.7, 1.7));
            setAttackLeft2(setup("turtle/turtle_left_attack_2", 1.7, 1.7));
            setAttackLeft3(setup("turtle/turtle_left_attack_3", 1.7, 1.7));
            setAttackRight1(setup("turtle/turtle_right_attack_1", 1.7, 1.7));
            setAttackRight2(setup("turtle/turtle_right_attack_2", 1.7, 1.7));
            setAttackRight3(setup("turtle/turtle_right_attack_3", 1.7, 1.7));
        }
    }

    private void getPlayerSpecialAttackImage() {//get sprites for secondary attack
        if (getCurrentSpirit().getName().equals("Bear")) {
            //up specials
            setSpecialUp1(setup("bear/bear_up_special_1", 1, 1));
            setSpecialUp2(setup("bear/bear_up_special_2", 1, 1));
            setSpecialUp3(setup("bear/bear_up_special_3", 1, 1));
            setSpecialUp4(setup("bear/bear_up_special_4", 1, 1));
            setSpecialUp5(setup("bear/bear_up_special_5", 1, 1));
            setSpecialUp6(setup("bear/bear_up_special_6", 1, 1));
            setSpecialUp7(setup("bear/bear_up_special_6", 1, 1));

            //down specials
            setSpecialDown1(setup("bear/bear_down_special_1", 1, 1));
            setSpecialDown2(setup("bear/bear_down_special_2", 1, 1));
            setSpecialDown3(setup("bear/bear_down_special_3", 1, 1));
            setSpecialDown4(setup("bear/bear_down_special_4", 1, 1));
            setSpecialDown5(setup("bear/bear_down_special_5", 1, 1));
            setSpecialDown6(setup("bear/bear_down_special_6", 1, 1));
            setSpecialDown7(setup("bear/bear_down_special_6", 1, 1));

            //left specials
            setSpecialLeft1(setup("bear/bear_left_special_1", 1, 1));
            setSpecialLeft2(setup("bear/bear_left_special_2", 1, 1));
            setSpecialLeft3(setup("bear/bear_left_special_3", 1, 1));
            setSpecialLeft4(setup("bear/bear_left_special_4", 1, 1));
            setSpecialLeft5(setup("bear/bear_left_special_5", 1, 1));
            setSpecialLeft6(setup("bear/bear_left_special_6", 1, 1));
            setSpecialLeft7(setup("bear/bear_left_special_6", 1, 1));

            //right specials
            setSpecialRight1(setup("bear/bear_right_special_1", 1, 1));
            setSpecialRight2(setup("bear/bear_right_special_2", 1, 1));
            setSpecialRight3(setup("bear/bear_right_special_3", 1, 1));
            setSpecialRight4(setup("bear/bear_right_special_4", 1, 1));
            setSpecialRight5(setup("bear/bear_right_special_5", 1, 1));
            setSpecialRight6(setup("bear/bear_right_special_6", 1, 1));
            setSpecialRight7(setup("bear/bear_right_special_6", 1, 1));
        }
        if (getCurrentSpirit().getName().equals("Eagle")) {
            //up specials
            setSpecialUp1(setup("eagle/eagle_up_special_1", 1.25, 1.25));
            setSpecialUp2(setup("eagle/eagle_up_special_2", 1.25, 1.25));
            setSpecialUp3(setup("eagle/eagle_up_special_3", 1.25, 1.25));
            setSpecialUp4(setup("eagle/eagle_up_special_4", 1.25, 1.25));
            setSpecialUp5(setup("eagle/eagle_up_special_5", 1.25, 1.25));
            setSpecialUp6(setup("eagle/eagle_up_special_6", 1.25, 1.25));
            setSpecialUp7(setup("eagle/eagle_up_special_6", 1.25, 1.25));

            //down specials
            setSpecialDown1(setup("eagle/eagle_down_special_1", 1.25, 1.25));
            setSpecialDown2(setup("eagle/eagle_down_special_2", 1.25, 1.25));
            setSpecialDown3(setup("eagle/eagle_down_special_3", 1.25, 1.25));
            setSpecialDown4(setup("eagle/eagle_down_special_4", 1.25, 1.25));
            setSpecialDown5(setup("eagle/eagle_down_special_5", 1.25, 1.25));
            setSpecialDown6(setup("eagle/eagle_down_special_6", 1.25, 1.25));
            setSpecialDown7(setup("eagle/eagle_down_special_6", 1.25, 1.25));

            //left specials
            setSpecialLeft1(setup("eagle/eagle_left_special_1", 1.25, 1.25));
            setSpecialLeft2(setup("eagle/eagle_left_special_2", 1.25, 1.25));
            setSpecialLeft3(setup("eagle/eagle_left_special_3", 1.25, 1.25));
            setSpecialLeft4(setup("eagle/eagle_left_special_4", 1.25, 1.25));
            setSpecialLeft5(setup("eagle/eagle_left_special_5", 1.25, 1.25));
            setSpecialLeft6(setup("eagle/eagle_left_special_6", 1.25, 1.25));
            setSpecialLeft7(setup("eagle/eagle_left_special_6", 1.25, 1.25));

            //right specials
            setSpecialRight1(setup("eagle/eagle_right_special_1", 1.25, 1.25));
            setSpecialRight2(setup("eagle/eagle_right_special_2", 1.25, 1.25));
            setSpecialRight3(setup("eagle/eagle_right_special_3", 1.25, 1.25));
            setSpecialRight4(setup("eagle/eagle_right_special_4", 1.25, 1.25));
            setSpecialRight5(setup("eagle/eagle_right_special_5", 1.25, 1.25));
            setSpecialRight6(setup("eagle/eagle_right_special_6", 1.25, 1.25));
            setSpecialRight7(setup("eagle/eagle_right_special_6", 1.25, 1.25));
        }
        if (getCurrentSpirit().getName().equals("Turtle")) {
            //up specials
            setSpecialUp1(setup("turtle/turtle_up_special_1", 1.7, 1.7));
            setSpecialUp2(setup("turtle/turtle_up_special_2", 1.7, 1.7));
            setSpecialUp3(setup("turtle/turtle_up_special_3", 1.7, 1.7));
            setSpecialUp4(setup("turtle/turtle_up_special_4", 1.7, 1.7));
            setSpecialUp5(setup("turtle/turtle_up_special_5", 1.7, 1.7));
            setSpecialUp6(setup("turtle/turtle_up_special_6", 1.7, 1.7));
            setSpecialUp7(setup("turtle/turtle_up_special_7", 1.7, 1.7));
            //down specials
            setSpecialDown1(setup("turtle/turtle_down_special_1", 1.7, 1.7));
            setSpecialDown2(setup("turtle/turtle_down_special_2", 1.7, 1.7));
            setSpecialDown3(setup("turtle/turtle_down_special_3", 1.7, 1.7));
            setSpecialDown4(setup("turtle/turtle_down_special_4", 1.7, 1.7));
            setSpecialDown5(setup("turtle/turtle_down_special_5", 1.7, 1.7));
            setSpecialDown6(setup("turtle/turtle_down_special_6", 1.7, 1.7));
            setSpecialDown7(setup("turtle/turtle_down_special_7", 1.7, 1.7));

            //left specials
            setSpecialLeft1(setup("turtle/turtle_left_special_1", 1.7, 1.7));
            setSpecialLeft2(setup("turtle/turtle_left_special_2", 1.7, 1.7));
            setSpecialLeft3(setup("turtle/turtle_left_special_3", 1.7, 1.7));
            setSpecialLeft4(setup("turtle/turtle_left_special_4", 1.7, 1.7));
            setSpecialLeft5(setup("turtle/turtle_left_special_5", 1.7, 1.7));
            setSpecialLeft6(setup("turtle/turtle_left_special_6", 1.7, 1.7));
            setSpecialLeft7(setup("turtle/turtle_left_special_7", 1.7, 1.7));

            //right specials
            setSpecialRight1(setup("turtle/turtle_right_special_1", 1.7, 1.7));
            setSpecialRight2(setup("turtle/turtle_right_special_2", 1.7, 1.7));
            setSpecialRight3(setup("turtle/turtle_right_special_3", 1.7, 1.7));
            setSpecialRight4(setup("turtle/turtle_right_special_4", 1.7, 1.7));
            setSpecialRight5(setup("turtle/turtle_right_special_5", 1.7, 1.7));
            setSpecialRight6(setup("turtle/turtle_right_special_6", 1.7, 1.7));
            setSpecialRight7(setup("turtle/turtle_right_special_7", 1.7, 1.7));
        }
    }

    //    UPDATE METHOD THAT GETS CALLED ON EACH FRAME
    public void update() {
        if (isOnPath()) {

            // SET DESTINATION TILE TO THE START OF THE MAZE
            int goalCol = 0;
            int goalRow = 0;

            // sets the destination tile to the next totem the player needs to collect
            for (int i = 0; i < gp.getObj().length; i++) {
                if (gp.getObj()[i] == null) {
                    if (i >= 3 && i <= 8) { // if the user just destroyed the brick, find the end of the maze
                        goalCol = 76;
                        goalRow = 33;
                        break;
                    }
                } else if (gp.getObj()[i] != null) {
                    if (numTotems < 3) { // find the next totem
                        goalCol = gp.getObj()[i].getWorldX() / gp.getTileSize();
                        goalRow = gp.getObj()[i].getWorldY() / gp.getTileSize();
                    }
                    else {

                        // find the bricks to unlock the maze
                        goalCol = 18;
                        goalRow = 75;
                    }
                    break;
                }
            }
            searchPathToTotem(goalCol, goalRow);
        }

//        INCREASE INTERNAL COOLDOWNS FOR PRIMARY AND SPECIAL ATTACKS EVERY FRAME
        secondaryICD++;
        primaryICD++;

//        INCREASE COUNTER FOR BERSERKER MODE
        if (berserkerMode) {
            berserkerCounter++;
        }

//        HEAL BEAR HALF A HEART FOR 4 SECONDS
        if (berserkerMode && berserkerCounter%30 == 0) {//perform every half a second
            if (spirits[0].health < spirits[0].maxHealth) {
                spirits[0].health++;//heal for one heart
            }
        }

//        TURN OFF BERSERKER MODE AFTER 5 SECONDS
        if (berserkerCounter > 300) {
            spirits[0].attack = 1;//RESET ATTACK VALUE
            if (spirits[0].health > 18) {//RESET HEALTH IF OVER MAX HEALTH
                spirits[0].health  = 18;
            }
            spirits[0].maxHealth = 18;//RESET MAX HEALTH
            berserkerMode = false;//TURN OFF BERSERKER MODE
        }
              
        //ANIMATION FOR IF PLAYER IS PRIMARY ATTACKING AND NOT SPECIAL ATTACKING
        if (attacking && !specialAttacking) {
            attacking();
        }

//        ANIMATION FOR IF PLAYER IS SPECIAL ATTACKING AND NOT PRIMARY ATTACKING
        if (specialAttacking && !attacking) {
            specialAttacking();
        }

//        CALLS ON KEY HANDLER CLASS TO GET USER INPUT FOR IF PRIMARY OR SPECIAL ATTACKING
        if (keyH.primaryPressed || keyH.secondaryPressed) {
//            SIMULATES AN ATTACK SO LONG AS LEFT CLICK IS THE ONLY THING HAPPENING AND AN ATTACK IS NOT ALREADY
//            HAPPENING AND IF ICD HAS REFRESHED
            if (keyH.primaryPressed && !keyH.secondaryPressed && !specialAttacking && primaryICD > 60) {//ATTACK ONCE
                // EVERY SECOND
                spriteCounter = 0;
                primaryICD = 0;
                setAttacking(true);
            }

            if (keyH.isSecondaryPressed() && !keyH.isPrimaryPressed() && !isAttacking() && secondaryICD > 100) {//if right click
                // has been
                // pressed, do a special attack once
                // every 400 frames ie 13 seconds
//                getPlayerSpecialAttackImage();
                setSpriteCounter(0);

//            SIMULATES AN ATTACK SO LONG AS RIGHT CLICK IS THE ONLY THING BEING PRESSED AND AN ATTACK IS NOT ALREADY
//            HAPPENING AND IF ICD HAS REFRESHED
            if (keyH.secondaryPressed && !keyH.primaryPressed && !attacking && secondaryICD > 360) {//ALLOW A SPECIAL
                // ATTACK EVERY6 SECONDS
                spriteCounter = 0;
                secondaryICD = 0;
                setSpecialAttacking(true);
            }
        }

        if ((keyH.isUpPressed() || keyH.isDownPressed() ||
                keyH.isLeftPressed() || keyH.isRightPressed()) && !isAttacking() && !isSpecialAttacking()) {//direction changes only
            // occur
            // if the key is
            // being pressed and player is not attacking
            if (keyH.isUpPressed()) {//move up
                setDirection("up");
            } else if (keyH.isDownPressed()) { // move down
                setDirection("down");
            } else if (keyH.isLeftPressed()) { // move left
                //remove the else portion to make x and y movements independent
                setDirection("left");
            } else if (keyH.isRightPressed()) { // move right
                setDirection("right");
            }

            // check tile collision
            setCollisionOn(false);
            gp.getCChecker().checkTile(this);

            // check object collision
            int objIndex = gp.getCChecker().checkObject(this, true);
            pickUpObject(objIndex);

            // check npc collision
            int npcIndex = gp.getCChecker().checkEntity(this, gp.getNpc());
            interactNPC(npcIndex);

            // check monster collision
            int monsterIndex = gp.getCChecker().checkEntity(this, gp.getMonster());
            contactMonster(monsterIndex); // runs when the user makes contact with the monster

            // player can only move if collision is false & if not attacking
            if (!isCollisionOn() && !isAttacking() && !isSpecialAttacking()) {
                switch (getDirection()) {

//        CALLS ON KEY HANDLER CLASS TO GET USER INPUT FOR MOVEMENT
        if ((keyH.upPressed || keyH.downPressed ||
                keyH.leftPressed || keyH.rightPressed) && !attacking && !specialAttacking) {
//            CHANGES DIRECTION ONLY IF ONE KEY IS PRESSED AND PLAYER IS NOT ATTACKING
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            // CHECK COLLISION ON TILES, OBJECTS, NPCs, AND MONSTERS
            collisionOn = false;
            gp.cChecker.checkTile(this);

            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

//            PLAYER CAN ONLY MOVE IF COLLISION IS FALSE AND IF NOT ATTACKING, MOVES PLAYER BY CHANGING WORLD
//            COORDINATES BY SPEED VALUE
            if (!collisionOn && !attacking && !specialAttacking) {
                switch (direction) {
                    case "up":
                        setWorldY(getWorldY() - getSpeed());
                        break;
                    case "down":
                        setWorldY(getWorldY() + getSpeed());
                        break;
                    case "left":
                        setWorldX(getWorldX() - getSpeed());
                        break;
                    case "right":
                        setWorldX(getWorldX() + getSpeed());
                        break;
                }
            }

            if (!isAttacking() && !isSpecialAttacking()) {
                setSpriteCounter(getSpriteCounter() + 1);
                if (getSpriteCounter() > 12) {//player image changes once every 12 frames, can adjust by increasing or decreasing
                    if (getSpriteNum() == 1) {//changes the player to first walking sprite to second sprite
                        setSpriteNum(2);
                    } else if (getSpriteNum() == 2) {//changes the player sprite from second to first
                        setSpriteNum(1);
                    }
                    setSpriteCounter(0);//resets the sprite counter
                }
            }
        }
        if (isInvincible()) { // if the player is invisible
//            WALKING ANIMATION ONLY OCCURS WHEN PLAYER IS NOT ATTACKING
            if (!attacking && !specialAttacking) {
                spriteCounter++;
                if (spriteCounter > 12) {//PLAYER IMAGE CHANGES ONCE EVERY 12 FRAMES
                    if (spriteNum == 1) {//CHANGES PLAYER WALKING SPRITE FROM FIRST TO SECOND
                        spriteNum = 2;
                    } else if (spriteNum == 2) {//CHANGES PLAYER WALKING SPRITE FROM SECOND TO FIRST
                        spriteNum = 1;
                    }
                    spriteCounter = 0;//RESET SPRITE COUNTER
                }
            }
        }
//        RUN INVINCIBILITY FOR 40 FRAMES AFTER BEING HIT BY MONSTER, APPROX. 0.67 SECONDS
        if (invincible) {
            invincibilityCounter++;
            if (invincibilityCounter > 40) {
                setInvincible(false);
                invincibilityCounter = 0;
            }
        }

        if (keyH.isOnePressed()) {
            switchSpirit(0); // switches to the bear
        } else if (keyH.isTwoPressed()) {
            switchSpirit(1); // switches to the eagle

        } else if (keyH.isThreePressed()) {
            switchSpirit(2);
        }

        if (gp.getPlayer().getCurrentSpirit().getHealth() <= 0) {
            setDying(true);
            setDisplayDeathMessage(false);
            gp.getPlayer().getCurrentSpirit().setDead(true);
            int spiritIndex = nextAliveSpirit();
            if (spiritIndex == -1) {
                if (!gp.getUi().isRespawningMessageOn()) { // display the respawning message if it has not been already displayed
                    gp.getUi().showRespawningMessage();
                }

                gp.getUi().setRespawningMessageDisplayTime(gp.getUi().getRespawningMessageDisplayTime() + 1);
                if (gp.getUi().getRespawningMessageDisplayTime() >= 240) { // makes the message disappear after 4 seconds
                    gp.getUi().setRespawningMessageDisplayTime(0);
                    gp.getUi().setRespawningMessageOn(false);
                    gp.getPlayer().restoreSettings(); // restores the world as if it is a new game

//        SWITCH SPIRITS DEPENDING ON NUMBER KEYS PRESSED
        if (keyH.onePressed) {
            switchSpirit(0); // SWITCH TO BEAR
        } else if (keyH.twoPressed) {
            switchSpirit(1); // SWITCH TO EAGLE

        } else if (keyH.threePressed) { // SWITCH TO TURTLE
            switchSpirit(2);
        }

//        SPIRIT DEATH
        if (gp.player.getCurrentSpirit().getHealth() <= 0) {
            isDying = true;
            displayDeathMessage = false;
            gp.player.getCurrentSpirit().dead = true;
            int spiritIndex = nextAliveSpirit();
            if (spiritIndex == -1) {
                if (!gp.ui.respawningMessageOn) { // DISPLAY RESPAWNING MESSAGE IF IT HASN'T ALREADY BEEN DISPLAYED
                    gp.ui.showRespawningMessage();
                }

                gp.ui.respawningMessageDisplayTime++;
                if (gp.ui.respawningMessageDisplayTime >= 240) { // MAKES THE MESSAGE DISAPPEAR AFTER 4 SECONDS
                    gp.ui.respawningMessageDisplayTime = 0;
                    gp.ui.respawningMessageOn = false;
                    gp.player.restoreSettings(); // RESTORES THE WORLD AS IF IT WERE A NEW GAME
                }
            }
//            FLICKERS THE SPIRIT FOR 4 SECONDS IF IT IS DEAD
            else {
                setDeadCounter(getDeadCounter() + 1);
                if (getDeadCounter() <= 240) {
                    if (getDeadCounter() % 30 == 0) {
                        setDeadFlicker(!isDeadFlicker());
                    }
                } else {
                    setDying(false);
                    setDisplayDeathMessage(true); // display the death message
                    setDeadFlicker(false);
                    setDeadCounter(0);
                    switchSpirit(nextAliveSpirit());
                }
            }
        }
    }

    private void switchSpirit(int spiritIndex) {
        currentSpiritIndex = spiritIndex; // sets the current spirit index to the spirit index
        getPlayerImage(); // reset the image pulls via getPlayerImage method
        getPlayerSpecialAttackImage();
        getPlayerAttackImage();

        // sets the player's hit box to the current spirit's hit box
        this.getSolidArea().x = getCurrentSpirit().getSolidArea().x;
        this.getSolidArea().y = getCurrentSpirit().getSolidArea().y;
        this.getSolidArea().width = getCurrentSpirit().getSolidArea().width;
        this.getSolidArea().height = getCurrentSpirit().getSolidArea().height;
        this.setSolidAreaDefaultX(getCurrentSpirit().getX());
        this.setSolidAreaDefaultY(getCurrentSpirit().getY());

        // sets the player's attack area to the current spirit's attack area
        this.getAttackArea().width = getCurrentSpirit().getAttackArea().width;
        this.getAttackArea().height = getCurrentSpirit().getAttackArea().height;

        // sets the player's attack and defense to the current spirit's attack and defense
        this.setAttack(getCurrentSpirit().getAttack());
        this.setDefense(getCurrentSpirit().getDefense());
    }

    private int nextAliveSpirit() {
        for (int i = currentSpiritIndex + 1; i < currentSpiritIndex + gp.getPlayer().spirits.length; i++) {
            int loopIndex = i % gp.getPlayer().spirits.length; // Calculates the loop index
            if (!gp.getPlayer().spirits[loopIndex].isDead()) {
                return loopIndex;
                    isDying = false;
                    displayDeathMessage = true; // DISPLAYS DEATH MESSAGE
                    deadFlicker = false;
                    deadCounter = 0;
                    switchSpirit(nextAliveSpirit());//SWITCH SPIRIT TO THE NEXT AVAILABLE ALIVE SPIRIT
                }
            }
        }
    }

    //    SWITCHES SPIRIT DEPENDING ON WHICH SPIRIT INDEX IS INPUTTED
    public void switchSpirit(int spiritIndex) {
        currentSpiritIndex = spiritIndex; //SET CURRENT SPIRIT INDEX TO SPIRIT INDEX

//        RESET IMAGE PULLS VIA IMAGE PULLING METHOD
        getPlayerImage();
        getPlayerSpecialAttackImage();
        getPlayerAttackImage();

        // SETS PLAYERS HITBOX TO THE CURRENT SPIRIT HITBOX
        this.solidArea.x = getCurrentSpirit().solidArea.x;
        this.solidArea.y = getCurrentSpirit().solidArea.y;
        this.solidArea.width = getCurrentSpirit().solidArea.width;
        this.solidArea.height = getCurrentSpirit().solidArea.height;
        this.solidAreaDefaultX = getCurrentSpirit().x;
        this.solidAreaDefaultY = getCurrentSpirit().y;

//        SETS THE PLAYER'S ATTACKING AREA TO CURRENT SPIRIT'S ATTACK AREA
        this.attackArea.width = getCurrentSpirit().attackArea.width;
        this.attackArea.height = getCurrentSpirit().attackArea.height;

//        SETS THE PLAYER'S ATTACK AND DEFENSE TO CURRENT SPIRIT'S ATTACK AND DEFENSE
        this.attack = getCurrentSpirit().attack;
        this.defense = getCurrentSpirit().defense;
    }

    //    FINDS AND RETURNS THE NEXT SPIRIT THAT IS ALLIVE
    public int nextAliveSpirit() {
        for (int i = currentSpiritIndex + 1; i < currentSpiritIndex + gp.player.spirits.length; i++) {
            int loopIndex = i % gp.player.spirits.length; // CALCULATES THE LOOP INDEX
            if (!gp.player.spirits[loopIndex].dead) {
                return loopIndex;//RETURNS INDEX OF THE NEXT ALIVE SPIRIT
            }
        }
        return -1; // RETURNS -1 IF EVERY SPIRIT IS DEAD AND THEREFORE TRIGGERING RESPAWN
    }

    //    ATTACK METHOD, PLAYERS ATTACKING ANIMATION AND DAMAGES MONSTERS IN ATTACK BOX
    public void attacking() {
        setSpriteCounter(getSpriteCounter() + 1);//UPDATE ANIMATION COUNTER
        if (getSpriteCounter() <= 10) {//SHOW FIRST ANIMATION FRAME FOR THE FIRST 10 GAME TICKS
            setSpriteNum(1);
        }
        if (getSpriteCounter() > 10 && getSpriteCounter() <= 15) {//SHOW SECOND ANIMATION FRAME FOR THE SECOND 5 GAME TICKS
            setSpriteNum(2);

//            SAVE THE CURRENT WORLD COORDINATES AND THE SOLID AREAS TO BE ABLE TO RESET TO THEM LATER AFTER
//            ANIMATION IS DONE
            int currentWorldX = getWorldX();
            int currentWorldY = getWorldY();
            int solidAreaWidth = getSolidArea().width;
            int solidAreaHeight = getSolidArea().height;

//      CREATES THE SPIRIT'S ATTACKING HIT BOX DEPENDING ON WHICH SPIRIT IS ACTIVE AND WHICH DIRECTION IT IS FACING
//            MANUAL ADJUSTMENTS HAVE BEEN MADE SO THE ANIMATIONS ARE FLUSH WITH EACH OTHER AND THE HIT ATTACK BOX
            switch (direction) {
                case "up":
                    switch(getCurrentSpirit().getName()) {
                        case "Bear", "Eagle":
                            setWorldX(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 1.6)));
                            setWorldY(getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 2.6)));
                            break;
                        case "Turtle":
                            setWorldX(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 1.2)));
                            setWorldY(getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 2.2)));
                            break;
                    }
                    break;
                case "down":
                    switch(getCurrentSpirit().getName()) {
                        case "Bear":
                            setWorldX(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 1.6)));
                            setWorldY(getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 0.7)));
                            break;
                        case "Eagle":
                            setWorldX(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 1.6)));
                            setWorldY(getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 0.8)));
                            break;
                        case "Turtle":
                            setWorldX(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 1.2)));
                            setWorldY(getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 0.5)));
                            break;
                    }
                    break;
                case "left":
                    switch(getCurrentSpirit().getName()) {
                        case "Bear":
                            setWorldX(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 2.6)));
                            setWorldY(getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 1.6)));
                            break;
                        case "Eagle":
                            setWorldX(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 2.5)));
                            setWorldY(getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 1.6)));
                            break;
                        case "Turtle":
                            setWorldX(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 2.1)));
                            setWorldY(getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 1.4)));
                            break;
                    }
                    break;
                case "right":
                    switch(getCurrentSpirit().getName()) {
                        case "Bear", "Eagle":
                            setWorldX(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 0.6)));
                            setWorldY(getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 1.7)));
                            break;
                        case "Turtle":
                            setWorldX(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 0.4)));
                            setWorldY(getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 1.3)));
                            break;
                    }
                    break;
            }

            // attack area becomes solid area
            getSolidArea().width = getAttackArea().width;
            getSolidArea().height = getAttackArea().height;

            monsterIndex = gp.getCChecker().checkEntity(this, gp.getMonster()); // gets the monster that the user is making contact with

            // Reset the world coordinates and solid area to the previous coordinates
            setWorldX(currentWorldX);
            setWorldY(currentWorldY);
            getSolidArea().width = solidAreaWidth;
            getSolidArea().height = solidAreaHeight;
        }
        if (getSpriteCounter() > 15 && getSpriteCounter() <= 25) {
            setSpriteNum(3);
            if (getCurrentSpirit().getName().equals("Turtle") && !getProjectile().isAlive() && getShotAvailableCounter() == 30) { // the player can only shoot one projectile at a time (and no quicker than half a second apart)

                // sets default coordinates for the projectile
                switch (getDirection()) {

//            ATTACKING AREA IS TURNED INTO A SOLID AREA
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

//            SEARCHES FOR THE MONSTER INDEX THE ATTACK AREA IS TOUCHING, IF ONE IS TOUCHING
            monsterIndex = gp.cChecker.checkEntity(this, gp.monster);

//            RESET THE WORLD COORDINATES AND THE SOLID AREA TO THE PREVIOUS COORDINATES
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if (spriteCounter > 15 && spriteCounter <= 25) {// THIRD ANIMATION FRAME
            spriteNum = 3;
//            SHOOTS A PROJECTILE IF TURTLE SPIRIT IS CURRENTLY ACTIVE, AND IF PROJECTILE SHOT IS OFF COOLDOWN
            if (getCurrentSpirit().name.equals("Turtle") && !projectile.alive && shotAvailableCounter == 25) {

//                SETS DEFAULT COORDINATES FOR THE PROJECTILE, ADJUSTMENTS HAVE BEEN MADE SO PROJECTILE SPAWNS AT THE
//                TURTLE ATTACK BOX
                switch (direction) {
                    case"up":
                        getProjectile().set(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 1.2)),
                                getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 2.2)), getDirection(), true, this);
                        break;
                    case "down":
                        getProjectile().set(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 1.2)),
                                getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 0.5)), getDirection(), true, this);
                        break;
                    case "left":
                        getProjectile().set(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 2.1)),
                                getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 1.4)), getDirection(), true, this);
                        break;
                    case "right":
                        getProjectile().set(getWorldX() + (int) (getAttackArea().width - (gp.getTileSize() * 0.4)),
                                getWorldY() + (int) (getAttackArea().height - (gp.getTileSize() * 1.3)), getDirection(), true, this);
                        break;
                }

                // add the projectile to the list of projectiles
                gp.getProjectileList().add(getProjectile());

                setShotAvailableCounter(0); // resets the counter
            }
        }
        if (getSpriteCounter() > 25) {
//             getPlayerImage();
            if (monsterIndex == 999 || gp.getMonster()[monsterIndex] != null) { // if the player is not touching a monster or the monster has not just been killed
                damageMonster(monsterIndex, getAttack());
            }

            setSpriteNum(1);
            setSpriteCounter(0);
            setAttacking(false);
        }

        if (getShotAvailableCounter() < 30) { // after half a second
            setShotAvailableCounter(getShotAvailableCounter() + 1);
        }
    }

    private void specialAttacking() {
        setSpriteCounter(getSpriteCounter() + 1);

        if (getSpriteCounter() <= 10) {
            setSpriteNum(1);
        }
        if (getSpriteCounter() > 10 && getSpriteCounter() <= 15) {
            monsterIndex = gp.getCChecker().checkEntity(this, gp.getMonster()); // gets the monster that the user is making contact with
            setSpriteNum(2);
        }
        if (getSpriteCounter() > 15 && getSpriteCounter() <= 20) {
            setSpriteNum(3);
        }
        if (getSpriteCounter() > 20 && getSpriteCounter() <= 25) {
            setSpriteNum(4);
        }
        if (getSpriteCounter() > 25 && getSpriteCounter() <= 30) {
            setSpriteNum(5);
        }
        if (getSpriteCounter() > 30 && getSpriteCounter() <= 35) {
            setSpriteNum(6);
        }
        if (getSpriteCounter() > 35 && getSpriteCounter() <= 40) {
            setSpriteNum(7);
        }
        if (getSpriteCounter() > 40) {
//            getPlayerImage();
            if (monsterIndex == 999 || gp.getMonster()[monsterIndex] != null) { // if the player is not touching a monster or the monster has not just been killed
                damageMonster(monsterIndex, getAttack());
            }

            if (getCurrentSpirit().getName().equals("Bear")) {//berserker mode for bear
                if (bearSpecialUnlocked) { // if the bear special ability is unlocked
                    System.out.println(spirits[0].getHealth());
                    if (spirits[0].getHealth() < 8) {
                        spirits[0].setHealth(spirits[0].getHealth() + 3);

//                ADD PROJECTILE TO THE LIST OF PROJECTILES
                gp.projectileList.add(projectile);

                shotAvailableCounter = 0; // RESETS SHOT COUNTER
            }
        }
        if (spriteCounter > 25) {//RESETS EVERYTHING AFTER 25 FRAMES

//            DEAL DAMAGE TO MONSTER IF THE PLAYER IS TOUCHING A MONSTER OR THE MONSTER EXISTS AND IS ALIVE
            if (monsterIndex == 999 || gp.monster[monsterIndex] != null) {
                damageMonster(monsterIndex, attack);
            }

//            RESET SPRITES BACK TO NORMAL WALKING SPRITES, TURN OFF ATTACKING
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }

//        INCREASE TURTLE PROJECTILE COOLDOWN COUNTER
        if (shotAvailableCounter < 25) {
            shotAvailableCounter++;
        }
    }

    //    SPECIAL ATTACKING METHOD
    public void specialAttacking() {
        spriteCounter++;

        if (spriteCounter <= 10) {//FIRST ANIMATION FRAME
            spriteNum = 1;
        }
        if (spriteCounter > 10 && spriteCounter <= 15) {//SECOND ANIMATION FRAME
//            IF A MONSTER IS TOUCHING THE PLAYER'S ATTACK HIT BOX, FIND THE INDEX OF THE MONSTER
            monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            spriteNum = 2;
        }
        if (spriteCounter > 15 && spriteCounter <= 20) {// THIRD ANIMATION FRAME
            spriteNum = 3;
        }
        if (spriteCounter > 20 && spriteCounter <= 25) {// FOURTH ANIMATION FRAME
            spriteNum = 4;
        }
        if (spriteCounter > 25 && spriteCounter <= 30) {// FIFTH ANIMATION FRAME
            spriteNum = 5;
        }
        if (spriteCounter > 30 && spriteCounter <= 35) {// SIXTH ANIMATION FRAME
            spriteNum = 6;
        }
        if (spriteCounter > 35 && spriteCounter <= 40) {// SEVENTH ANIMATION FRAME
            spriteNum = 7;
        }
//        DEAL DAMAGE, RESET FRAMES
        if (spriteCounter > 40) {
//            DEAL DAMAGE TO MONSTER IF THE MONSTER IS TOUCHING THE PLAYER ATTACK BOX, OR IT HAS NOT JUST BEEN KILLED
            if (monsterIndex == 999 || gp.monster[monsterIndex] != null) {
                damageMonster(monsterIndex, attack);
            }

//            BERSERKER MODE FOR BEAR
            if (getCurrentSpirit().name.equals("Bear")) {
//                CHECK IF THE BEAR TOTEM HAS BEEN UNLOCKED IN ORDER TO DEAL DAMAGE, OTHERWISE JUST PLAYS THE ANIMATION
                if (bearSpecialUnlocked) {

//                    ACTIVATE BERSERKER MODE
                    berserkerMode = true;
                      
//                    GRANT BONUS HEALTH
                    spirits[0].maxHealth = 26;
                      
//                    INSTANTLY HEAL SOME DAMAGE IF BELOW THREE HEARTS
                    if (spirits[0].getHealth < 6 ) {
                        spirits[0].setHealth(spirits[0].getHealth + 10);
                    }
                    berserkerCounter = 0;//RESET COUNTER
                    spirits[0].setAttack(10);//any attack done by bear should now be one shot
                }
            }

            if (getCurrentSpirit().getName().equals("Turtle")) { // if the turtle special ability is unlocked
                if (turtleSpecialUnlocked) {
                    for (int i = 0; i < spirits.length; i++) { // sets every spirit's health to the maximum
                        if (spirits[i].getHealth() < spirits[i].getMaxHealth()) {
                            spirits[i].setHealth(spirits[i].getMaxHealth());
                            spirits[i].setDead(false);

//            TURTLE HEALING WAVE
            if (getCurrentSpirit().name.equals("Turtle")) {
//                CHECK IF THE TURTLE TOTEM HAS BEEN UNLOCKED IN ORDER TO HEAL, OTHERWISE JUST PLAYS THE ANIMATION
                if (turtleSpecialUnlocked) {
//                    ITERATE THROUGH EACH SPIRIT, HEAL THEM TO MAX HEALTH
                    for (Spirit spirit : spirits) {
                        if (spirit.getHealth() < spirit.getMaxHealth()) {
                            spirit.setHealth(spirit.getMaxHealth());
                            spirit.dead = false;
                        }
                    }
                }
            }

            if (getCurrentSpirit().getName().equals("Eagle")) {
                if (eagleSpecialUnlocked) { // if the eagle special ability is unlocked
                    int targetSmallestDistance = 999;
                    int targetIndex = -1;
                    for (int i = 0; i < gp.getMonster().length ; i++) {
                        if (gp.getMonster()[i] != null) { // if the monster exists
                            if (getDistance(i) < targetSmallestDistance) { // checks if the smaller distance is smaller than the last smallest

//            EAGLE EYE SHOT
            if (getCurrentSpirit().name.equals("Eagle")) {
//                CHECK IF THE EAGLE TOTEM HAS BEEN UNLOCKED IN ORDER TO HEAL, OTHERWISE JUST PLAYS THE ANIMATION
                if (eagleSpecialUnlocked) {

//                    FIND INDEX OF THE CLOSEST MONSTER TO EAGLE
                    int targetSmallestDistance = 999;
                    int targetIndex = -1;
                    for (int i = 0; i < gp.monster.length; i++) {
                        if (gp.monster[i] != null) { // MAKE SURE MONSTER EXISTS
                            if (getDistance(i) < targetSmallestDistance) { // CHECKS IF THE DISTANCE IS SMALLET THAN
                                // THE SMALLEST DISTANCE
                                targetIndex = i;
                                targetSmallestDistance = getDistance(i);
                            }
                        }
                    }

                    if (targetIndex == -1) { // if no monsters remain
                        gp.getUi().showMessage("There are no monsters for the eagle eye to lock onto");
                    }
                    else {
                        switch (getDirection()) {//spawn projectile based on what direction eagle is facing

                    //IF THERE ARE NO MONSTERS NEARBY
                    if (targetIndex == -1) {
                        gp.ui.showMessage("There are no monsters nearby for the eagle eye to lock onto");
                    } else {
//                        SPAWN EAGLE EYE PROJECTILES BASED ON WHAT DIRECTION EAGLE IS FACING
                        switch (direction) {
                            case "up":
                                getTargetProjectile().set((int) (getWorldX() + getAttackArea().width - (gp.getTileSize() * 1.35)),
                                        (int) (getWorldY() - getAttackArea().height + (gp.getTileSize() * 0.3)), true, targetIndex);
                                break;
                            case "down":
                                getTargetProjectile().set((int) (getWorldX() + getAttackArea().width - (gp.getTileSize() * 1.35)), (int)
                                        (getWorldY() + getAttackArea().height + (gp.getTileSize() * -0.5)), true, targetIndex);
                                break;
                            case "left":
                                getTargetProjectile().set((int) (getWorldX() - getAttackArea().width + (gp.getTileSize() * 0.2)), (int)
                                        (getWorldY() + getAttackArea().height - (gp.getTileSize() * 1.2)), true, targetIndex);
                                break;
                            case "right":
                                getTargetProjectile().set((int) (getWorldX() + getAttackArea().width - (gp.getTileSize() * 0.4)), (int)
                                        (getWorldY() - getAttackArea().height + (gp.getTileSize() * 1.3)), true, targetIndex);
                                break;
                        }

                        // add the projectile to the list of projectiles
                        gp.getTargetProjectileList().add(getTargetProjectile());
                        setShotAvailableCounter(0);//resets the shot counter
                    }
                }
            }
            setSpriteNum(1);
            setSpriteCounter(0);
            setSpecialAttacking(false);
        }

        int currentWorldX = getWorldX();
        int currentWorldY = getWorldY();
        int solidAreaWidth = getSolidArea().width;
        int solidAreaHeight = getSolidArea().height;

        // attack area becomes solid area
        getSolidArea().width = getAttackArea().width;
        getSolidArea().height = getAttackArea().height;

        // Reset the world coordinates and solid area to the previous coordinates
        setWorldX(currentWorldX);
        setWorldY(currentWorldY);
        getSolidArea().width = solidAreaWidth;
        getSolidArea().height = solidAreaHeight;
    }
    private int getDistance (int i) {//gets current distance from player to a monster
        int currentDistance = (int) Math.sqrt(Math.pow(getWorldX() - gp.getMonster()[i].getWorldX(), 2) + Math.pow(getWorldY() - gp.getMonster()[i].getWorldY(), 2)); // calculates the distance between the player and the monster
        return currentDistance;
    }

    private void pickUpObject(int index) {
        if (index != 999) { // if index is 999, no object was touched
            String objectName = gp.getObj()[index].getName();
//                        ADD PROJECTILE TO THE LIST OF PROJECTILES
                        gp.targetProjectileList.add(targetProjectile);
                        shotAvailableCounter = 0;//RESET SHOT COUNTER
                    }
                }
            }
            //RESET ANIMATION BACK TO WALKING SPRITES, TURN OFF SPECIAL ATTACKING
            spriteNum = 1;
            spriteCounter = 0;
            specialAttacking = false;
        }

//        FIND THE CURRENT WORLD X AND GET THE SPIRITS ATTACK AREA WIDTH
        int currentWorldX = worldX;
        int currentWorldY = worldY;
        int solidAreaWidth = solidArea.width;
        int solidAreaHeight = solidArea.height;


//        RESET THE WORLD COORDINATES AND THE SOLID AREA TO THE PREVIOUS COORDINATES
        worldX = currentWorldX;
        worldY = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;
    }

    //    METHOD TO CALCULATE THE DISTANCE BETWEEN THE PLAYER AND A MONSTER
    public int getDistance(int i) {
//        USES PYTHAGOREAN THEOREM TO RETURN THE DISTANCE BETWEEN THE PLAYER COORDS AND THE MONSTER COORDS
        return (int) Math.sqrt(Math.pow(worldX - gp.monster[i].worldX, 2) + Math.pow(worldY - gp.monster[i].worldY, 2));

    }

    //    METHOD TO PICK UP TOTEM
    public void pickUpObject(int index) {
//        CHECK IF AN OBJECT IS CURRENTLY BEING TOUCHED BY THE PLAYER, IF INDEX IS 999, NO OBJECT WAS TOUCHED
        if (index != 999) {
            String objectName = gp.obj[index].name;

//            FIND OUT WHICH OBJECT THE PLAYER IS TOUCHING, EITHER A TOTEM OR A WALL
            switch (objectName) {
//                    DISPLAY MESSAGE SAYING THE PLAYER HAS PICKED UP A TOTEM
                case "Totem":// PLAYER IS TOUCHING A TOTEM OBJECT
                    numTotems++; // INCREASES THE NUMBER OF TOTEMS THE PLAYER HAS COLLECTED
                    gp.obj[index] = null; // REMOVES THE TOTEM
                    if (index == 0) { // TURTLE TOTEM COLLECTED
                        turtleSpecialUnlocked = true;
                    }
                    else if (index == 1) { // EAGLE TOTEM COLLECTED
                        eagleSpecialUnlocked = true;
                    }
                    else { // BEAR TOTEM COLLECTED
                        bearSpecialUnlocked = true;
                    }

//                    TOTEM COLLECTION MESSAGES
                    if (numTotems == 3) {// MESSAGE FOR WHEN THE PLAYER HAS COLLECTED THE THREE TOTEMS NEEDED TO
                        // UNLOCK THE MAZE
                        gp.ui.showCollectionMessage("Congratulations, all three totems have been collected. I think I hear a door opening somewhere");
                    }
//                    DISPLAY COMPLETION MESSAGE IF THE FOURTH TOTEM IN THE MAZE HAS BEEN COLLECTED
                    else if (numTotems == 4) {
                        gp.ui.completionMessageOn = true;
                    }
//                    DISPLAY MESSAGE SAYING THE PLAYER HAS PICKED UP A TOTEM
                case "Totem":
                    numTotems++; // increases the number of totems the user has collected
                    gp.getObj()[index] = null; // removes the object
                    if (numTotems < 3) {
                        if (index == 0) { // TURTLE TOTEM COLLECTED
                            turtleSpecialUnlocked = true;
                            gp.getUi().showMessage("Turtle Special Unlocked");
                        } else if (index == 1) { // eagle totem collected
                            eagleSpecialUnlocked = true;
                            gp.getUi().showMessage("Eagle Special Unlocked");
                        } else { // bear totem collected
                            bearSpecialUnlocked = true;
                            gp.getUi().showMessage("Bear Special Unlocked");
                        }
                    }
                    else if (numTotems == 3) {//PLAY MESSAGE FOR WHEN PLAYER HAS COLLECTED THE THREE TOTEMS NEEDED TO
                        // UNLOCK THE MAZE
                        gp.getUi().showCollectionMessage("Congratulations, all three totems have been collected. I think I hear a door opening somewhere");
                    } else {//DISPLAY COMPLETION MESSAGE IF THE FOURTH TOTEM IN THE MAZE HAS BEEN COLLECTED
                        gp.getUi().setCompletionMessageOn(true);
                    }
                    break;

                case "Wall":
                    if (numTotems == 3) { // if the user has 3 totems
                        gp.getObj()[index] = null; // destroys the wall

                case "Wall":// PLAYER IS TOUCHING A WALL OBJECT AT THE MAZE
//                    CHECK IF PLAYER HAS THREE TOTEMS
                    if (numTotems == 3) {
                        gp.obj[index] = null; // DESTROYES THE WALL
                    }
//                    DISPLAY MESSAGE SAYING PLAYER NEEDS TO CONTINUE COLLECTING TOTEMS IN ORDER TO UNLOCK THE MAZE
                    else {
                        gp.getUi().showMessage("You need to collect " + (3 - numTotems) + " more totems to get past the wall");
                    }
                    break;
            }
        }
    }

//    MONSTER CONTACT METHOD

//    MODIFIES THE PLAYER'S INVINCIBILITY IF THEY MAKE CONTACT WITH A MONSTER
    public void contactMonster(int index) {
        Spirit currentSpirit = gp.player.getCurrentSpirit(); // GET CURRENT SPIRIT

//        CHECK IF A MONSTER IS TOUCHING A PLAYER, IF INDEX IS 999 NO MONSTER WAS TOUCHED
        if (index != 999) {
//            DEAL DAMAGE TO PLAYER IF PLAYER IS NOT INVINCIBLE
            if (!invincible) {
                int damage = gp.monster[index].attack - defense;
                if (damage < 0) { // so damage is not negative
                    damage = 0;
                }
                currentSpirit.setHealth(currentSpirit.getHealth() - damage);
                setInvincible(true);
            }
        }
    }

    void damageMonster(int index, int attack) { // deals damage to the monster

        if (index != 999) { // if index is 999, no monster was touched
            int damage = attack - gp.getMonster()[index].getDefense();
            if (damage < 0) { // so damage is not negative
                damage = 0;
            }
            gp.getMonster()[index].setHealth(gp.getMonster()[index].getHealth() - damage);
            gp.getMonster()[index].damageReaction();
            System.out.println("Hit"); // for debugging

            if (gp.getMonster()[index].getHealth() <= 0) { // if the monster dies, replace that slot in the array with a null value
                gp.getMonster()[index] = null;
                invincible = true;//TURN ON INVINCIBILITY NOW THAT PLAYER HAS BEEN DAMAGED
            }
        }
    }

    //    PLAYER DAMAGE MONSTER METHOD
    public void damageMonster(int index, int attack) { // ACCEPTS THE MONSTER BEING ATTACKED AND THE SPIRIT'S ATTACK
        // VALUE

//        CHECK IF THE MONSTER IS BEING ATTACKED, IF INDEX RETURNS 999, NO MONSTER WAS TOUCHED
        if (index != 999) {
            int damage = attack - gp.monster[index].defense;
//            ENSURE DAMAGE IS NOT NEGATIVE IN CASE DEFENSE IS REALLY HIGH, OTHERWISE IT WOULD HEAL THE MONSTER
            if (damage < 0) {
                damage = 0;
            }
            gp.monster[index].health -= damage;
            gp.monster[index].damageReaction();

//            DEBUGGING
            System.out.println("Hit"); // PRINT ON CONSOLE THAT HIT WAS REGISTERED

//            IF MONSTER IS DEAD, REPLACE ITS INDEX WITHIN THE MONSTER ARRAY WITH A NULL VALUE
            if (gp.monster[index].health <= 0) {
                gp.monster[index] = null;
            }
        }
    }

    //DRAWING METHOD
    public void draw(Graphics2D g2) {
        BufferedImage image = null;//CALL ON BUFFERED IMAGE CLASS

//        CREATE TEMPORARY SCREEN VARIABLES TO ACCOUNT FOR CHANGE IN SPIRIT POSITION WHEN ATTACKING
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        if (isAttacking() && !isSpecialAttacking()) {
            switch (getDirection()) {//check the direction, based on the direction it picks a different image
                case "up":
                    // compensate for  the sprite moving when doing the attacking animation
                    if (getCurrentSpirit().getName().equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.33);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.45);
                    }
                    else if (getCurrentSpirit().getName().equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.33);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.38);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.58);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.68);
                    }
                    if (getSpriteNum() == 1) {image = getAttackUp1();}
                    if (getSpriteNum() == 2) {image = getAttackUp2();}
                    if (getSpriteNum() == 3) {image = getAttackUp3();}
                    break;
                case "down":
                    // compensate the sprite moving  when doing the attacking animation
                    if (getCurrentSpirit().getName().equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.4);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.25);
                    }
                    else if (getCurrentSpirit().getName().equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.33);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.47);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.58);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.62);
                    }
                    if (getSpriteNum() == 1) {image = getAttackDown1();}
                    if (getSpriteNum() == 2) {image = getAttackDown2();}
                    if (getSpriteNum() == 3) {image = getAttackDown3();}
                    break;
                case "left":
                    // compensate the sprite moving when doing the attacking animation
                    if (getCurrentSpirit().getName().equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.38);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.38);
                    }
                    else if (getCurrentSpirit().getName().equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.38);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.525);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.58);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.68);
                    }
                    if (getSpriteNum() == 1) {image = getAttackLeft1();}
                    if (getSpriteNum() == 2) {image = getAttackLeft2();}
                    if (getSpriteNum() == 3) {image = getAttackLeft3();}
                    break;
                case "right":
                    // compensate the sprite moving when doing the attacking animation
                    if (getCurrentSpirit().getName().equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.33);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.31);
                    }
                    else if (getCurrentSpirit().getName().equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.28);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.475);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.57);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.68);
                    }
                    if (getSpriteNum() == 1) {image = getAttackRight1();}
                    if (getSpriteNum() == 2) {image = getAttackRight2();}
                    if (getSpriteNum() == 3) {image = getAttackRight3();}
                    break;
            }
        }
        if (isSpecialAttacking() && !isAttacking()) {
            switch (getDirection()) {//check the direction, based on the direction it picks a different image
                case "up":
                    // Moves the sprite when doing the special attacking animation
                    if (getCurrentSpirit().getName().equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.2);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.2);
                    }
                    else if (getCurrentSpirit().getName().equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.323);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.38);
                    }
                    else {//turtle
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.567);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.677);
                    }
                    if (getSpriteNum() == 1) {image = getSpecialUp1();}
                    if (getSpriteNum() == 2) {image = getSpecialUp2();}
                    if (getSpriteNum() == 3) {image = getSpecialUp3();}
                    if (getSpriteNum() == 4) {image = getSpecialUp4();}
                    if (getSpriteNum() == 5) {image = getSpecialUp5();}
                    if (getSpriteNum() == 6) {image = getSpecialUp6();}
                    if (getSpriteNum() == 7) {image = getSpecialUp7();}
                    break;
                case "down":
                    // Moves the sprite when doing the special attacking animation
                    if (getCurrentSpirit().getName().equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.25);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.18);
                    }
                    else if (getCurrentSpirit().getName().equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.325);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.469);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.58);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.62);
                    }
                    if (getSpriteNum() == 1) {image = getSpecialDown1();}
                    if (getSpriteNum() == 2) {image = getSpecialDown2();}
                    if (getSpriteNum() == 3) {image = getSpecialDown3();}
                    if (getSpriteNum() == 4) {image = getSpecialDown4();}
                    if (getSpriteNum() == 5) {image = getSpecialDown5();}
                    if (getSpriteNum() == 6) {image = getSpecialDown6();}
                    if (getSpriteNum() == 7) {image = getSpecialDown7();}
                    break;
                case "left":
                    // Moves the sprite when doing the special attacking animation
                    if (getCurrentSpirit().getName().equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.259);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.252);
                    }
                    else if (getCurrentSpirit().getName().equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.378);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.53);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.6);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.57);
                    }
                    if (getSpriteNum() == 1) {image = getSpecialLeft1();}
                    if (getSpriteNum() == 2) {image = getSpecialLeft2();}
                    if (getSpriteNum() == 3) {image = getSpecialLeft3();}
                    if (getSpriteNum() == 4) {image = getSpecialLeft4();}
                    if (getSpriteNum() == 5) {image = getSpecialLeft5();}
                    if (getSpriteNum() == 6) {image = getSpecialLeft6();}
                    if (getSpriteNum() == 7) {image = getSpecialLeft7();}
                    break;
                case "right":
                    // Moves the sprite when doing the special attacking animation
                    if (getCurrentSpirit().getName().equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.205);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.25);
                    }
                    else if (getCurrentSpirit().getName().equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.28);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.47);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.55);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.58);
                    }
                    if (getSpriteNum() == 1) {image = getSpecialRight1();}
                    if (getSpriteNum() == 2) {image = getSpecialRight2();}
                    if (getSpriteNum() == 3) {image = getSpecialRight3();}
                    if (getSpriteNum() == 4) {image = getSpecialRight4();}
                    if (getSpriteNum() == 5) {image = getSpecialRight5();}
                    if (getSpriteNum() == 6) {image = getSpecialRight6();}
                    if (getSpriteNum() == 7) {image = getSpecialRight7();}
                    break;
            }
        }
        if (!isSpecialAttacking() && !isAttacking()) {//drawing function for basic movement
            switch (getDirection()) {//check the direction, based on the direction it picks a different image
                case "up":
                    if (getCurrentSpirit().getName().equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.2);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.2);
                    }
                    else if (getCurrentSpirit().getName().equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.2);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.25);
                    }
                    else {//turtle
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.35);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.45);
                    }
                    if (getSpriteNum() == 1) {image = getUp1();}
                    if (getSpriteNum() == 2) {image = getUp2();}
                    break;
                case "down":
                    if (getCurrentSpirit().getName().equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.25);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.15);
                    }
                    else if (getCurrentSpirit().getName().equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.2);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.35);
                    }
                    else {//turtle
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.35);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.4);
                    }
                    if (getSpriteNum() == 1) {image = getDown1();}
                    if (getSpriteNum() == 2) {image = getDown2();}
                    break;
                case "left":
                    if (getCurrentSpirit().getName().equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.25);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.25);
                    }
                    else if (getCurrentSpirit().getName().equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.25);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.4);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.35);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.45);
                    }
                    if (getSpriteNum() == 1) {image = getLeft1();}
                    if (getSpriteNum() == 2) {image = getLeft2();}
                    break;
                case "right":
                    if (getCurrentSpirit().getName().equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.2);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.25);
                    }
                    else if (getCurrentSpirit().getName().equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.15);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.35);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.getTileSize() * 0.35);
                        tempScreenY = screenY - (int) (gp.getTileSize() * 0.45);
                    }
                    if (getSpriteNum() == 1) {image = getRight1();}
                    if (getSpriteNum() == 2) {image = getRight2();}
                    break;
            }
        }
        if ((isInvincible() && !gp.getPlayer().getCurrentSpirit().isDead()) || isDeadFlicker()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f)); // reduces the opacity to 70% to show when the player is invincible

//        DRAW SPRITES FOR ATTACKING, MAKE SURE SPECIAL ATTACKING ISN'T HAPPENING AT THE SAME TIME
        if (attacking && !specialAttacking) {
//            CHECK DIRECTION, AND PICK A DIFFERENT SET OF IMAGES DEPENDING ON DIRECTION PLAYER IS FACING
//            ALL COORDINATES HAVE BEEN PAINSTAKINGLY COMPENSATED TO BE CENTERED WITH THE COLLISION BOX
            switch (direction) {
                case "up":
                    if (getCurrentSpirit().name.equals("Bear")) {// BEAR SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.33);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.45);
                    } else if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.33);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.38);
                    } else {// TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.58);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.68);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {
                        image = attackUp1;
                    }
                    if (spriteNum == 2) {
                        image = attackUp2;
                    }
                    if (spriteNum == 3) {
                        image = attackUp3;
                    }
                    break;
                case "down":
                    if (getCurrentSpirit().name.equals("Bear")) {// BEAR SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.4);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.25);
                    } else if (getCurrentSpirit().name.equals("Eagle")) {//EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.33);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.47);
                    } else {// TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.58);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.62);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {
                        image = attackDown1;
                    }
                    if (spriteNum == 2) {
                        image = attackDown2;
                    }
                    if (spriteNum == 3) {
                        image = attackDown3;
                    }
                    break;
                case "left":
                    if (getCurrentSpirit().name.equals("Bear")) {// BEAR SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.38);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.38);
                    } else if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.38);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.525);
                    } else {// TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.58);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.68);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {
                        image = attackLeft1;
                    }
                    if (spriteNum == 2) {
                        image = attackLeft2;
                    }
                    if (spriteNum == 3) {
                        image = attackLeft3;
                    }
                    break;
                case "right":
                    if (getCurrentSpirit().name.equals("Bear")) {//TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.33);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.31);
                    } else if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.28);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.475);
                    } else {// TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.57);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.68);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {
                        image = attackRight1;
                    }
                    if (spriteNum == 2) {
                        image = attackRight2;
                    }
                    if (spriteNum == 3) {
                        image = attackRight3;
                    }
                    break;
            }
        }

//        DRAW SPRITES FOR SPECIAL ATTACKING, MAKE SURE NOT ALSO ATTACKING AT THE SAME TIME
        if (specialAttacking && !attacking) {
            switch (direction) {
                case "up":
                    if (getCurrentSpirit().name.equals("Bear")) {//BEAR SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.2);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.2);
                    } else if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.323);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.38);
                    } else {// TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.567);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.677);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {
                        image = specialUp1;
                    }
                    if (spriteNum == 2) {
                        image = specialUp2;
                    }
                    if (spriteNum == 3) {
                        image = specialUp3;
                    }
                    if (spriteNum == 4) {
                        image = specialUp4;
                    }
                    if (spriteNum == 5) {
                        image = specialUp5;
                    }
                    if (spriteNum == 6) {
                        image = specialUp6;
                    }
                    if (spriteNum == 7) {
                        image = specialUp7;
                    }
                    break;
                case "down":
                    if (getCurrentSpirit().name.equals("Bear")) {//BEAR SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.25);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.18);
                    } else if (getCurrentSpirit().name.equals("Eagle")) {//EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.325);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.469);
                    } else {// TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.58);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.62);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {
                        image = specialDown1;
                    }
                    if (spriteNum == 2) {
                        image = specialDown2;
                    }
                    if (spriteNum == 3) {
                        image = specialDown3;
                    }
                    if (spriteNum == 4) {
                        image = specialDown4;
                    }
                    if (spriteNum == 5) {
                        image = specialDown5;
                    }
                    if (spriteNum == 6) {
                        image = specialDown6;
                    }
                    if (spriteNum == 7) {
                        image = specialDown7;
                    }
                    break;
                case "left":
                    if (getCurrentSpirit().name.equals("Bear")) {// BEAR SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.259);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.252);
                    } else if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.378);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.53);
                    } else {// TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.6);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.57);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {
                        image = specialLeft1;
                    }
                    if (spriteNum == 2) {
                        image = specialLeft2;
                    }
                    if (spriteNum == 3) {
                        image = specialLeft3;
                    }
                    if (spriteNum == 4) {
                        image = specialLeft4;
                    }
                    if (spriteNum == 5) {
                        image = specialLeft5;
                    }
                    if (spriteNum == 6) {
                        image = specialLeft6;
                    }
                    if (spriteNum == 7) {
                        image = specialLeft7;
                    }
                    break;
                case "right":
                    if (getCurrentSpirit().name.equals("Bear")) {// BEAR SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.205);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.25);
                    } else if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.28);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.47);
                    } else {//TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.55);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.58);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {
                        image = specialRight1;
                    }
                    if (spriteNum == 2) {
                        image = specialRight2;
                    }
                    if (spriteNum == 3) {
                        image = specialRight3;
                    }
                    if (spriteNum == 4) {
                        image = specialRight4;
                    }
                    if (spriteNum == 5) {
                        image = specialRight5;
                    }
                    if (spriteNum == 6) {
                        image = specialRight6;
                    }
                    if (spriteNum == 7) {
                        image = specialRight7;
                    }
                    break;
            }
        }

//        DRAW SPRITES FOR REGULAR MOVEMENT
        if (!specialAttacking && !attacking) {
            switch (direction) {
                case "up":
                    if (getCurrentSpirit().name.equals("Bear")) {// BEAR SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.2);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.2);
                    } else if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.2);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.25);
                    } else {// TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.35);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.45);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                    break;
                case "down":
                    if (getCurrentSpirit().name.equals("Bear")) {// BEAR SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.25);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.15);
                    } else if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.2);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.35);
                    } else {//TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.35);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.4);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                    break;
                case "left":
                    if (getCurrentSpirit().name.equals("Bear")) {// BEAR SPRIETE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.25);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.25);
                    } else if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.25);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.4);
                    } else {// TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.35);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.45);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                    break;
                case "right":
                    if (getCurrentSpirit().name.equals("Bear")) {// BEAR SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.2);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.25);
                    } else if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.15);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.35);
                    } else {// TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.35);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.45);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                    break;
            }
        }
//        REDUCES OPACITY OF THE IMAGE TO SHOW WHEN THE PLAYER IS INVINCIBLE
        if ((invincible && !gp.player.getCurrentSpirit().dead) || deadFlicker) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        } else {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

//        DRAW THE IMAGE, NULL MEANS WE CANNOT TYPE ON THE IMAGE
        g2.drawImage(image, tempScreenX, tempScreenY, null);//draws the image, null means we cannot type

        // DEBUGGING

//        DRAWS THE ATTACK AREA OF THE PLAYER ONTO THE SCREEN
//        COMPENSATION VALUES ON THIS SECTION ARE THE SAME AS ABOVE
        tempScreenX = screenX + getSolidArea().x;
        tempScreenY = screenY + getSolidArea().y;
        switch (getDirection()) {
            case "up":
                switch(getCurrentSpirit().getName()) {
                    case "Bear", "Eagle":
                        tempScreenX = (int) (screenX + getAttackArea().width - (gp.getTileSize() * 1.6));
                        tempScreenY = (int) (screenY + getAttackArea().height - (gp.getTileSize() * 2.6));
                        break;
                    case "Turtle":
                        tempScreenX = (int) (screenX + getAttackArea().width - (gp.getTileSize() * 1.2));
                        tempScreenY = (int) (screenY + getAttackArea().height - (gp.getTileSize() * 2.2));
                        break;
                }
                break;
            case "down":
                switch(getCurrentSpirit().getName()) {
                    case "Bear":
                        tempScreenX = (int) (screenX + getAttackArea().width - (gp.getTileSize() * 1.6));
                        tempScreenY = (int) (screenY + getAttackArea().height - (gp.getTileSize() * 0.7));
                        break;
                    case "Eagle":
                        tempScreenX = (int) (screenX + getAttackArea().width - (gp.getTileSize() * 1.6));
                        tempScreenY = (int) (screenY + getAttackArea().height - (gp.getTileSize() * 0.8));
                        break;
                    case "Turtle":
                        tempScreenX = (int) (screenX + getAttackArea().width - (gp.getTileSize() * 1.2));
                        tempScreenY = (int) (screenY + getAttackArea().height - (gp.getTileSize() * 0.5));
                        break;
                }
                break;
            case "left":
                switch(getCurrentSpirit().getName()) {
                    case "Bear":
                        tempScreenX = (int) (screenX + getAttackArea().width - (gp.getTileSize() * 2.6));
                        tempScreenY = (int) (screenY + getAttackArea().height - (gp.getTileSize() * 1.6));
                        break;
                    case "Eagle":
                        tempScreenX = (int) (screenX + getAttackArea().width - (gp.getTileSize() * 2.5));
                        tempScreenY = (int) (screenY + getAttackArea().height - (gp.getTileSize() * 1.6));
                        break;
                    case "Turtle":
                        tempScreenX = (int) (screenX + getAttackArea().width - (gp.getTileSize() * 2.1));
                        tempScreenY = (int) (screenY + getAttackArea().height - (gp.getTileSize() * 1.4));
                        break;
                }
                break;
            case "right":
                switch(getCurrentSpirit().getName()) {
                    case "Bear", "Eagle":
                        tempScreenX = (int) (screenX + getAttackArea().width - (gp.getTileSize() * 0.6));
                        tempScreenY = (int) (screenY + getAttackArea().height - (gp.getTileSize() * 1.7));
                        break;
                    case "Turtle":
                        tempScreenX = (int) (screenX + getAttackArea().width - (gp.getTileSize() * 0.4));
                        tempScreenY = (int) (screenY + getAttackArea().height - (gp.getTileSize() * 1.3));
                        break;
                }
                break;
        }
        g2.drawRect(tempScreenX, tempScreenY, getAttackArea().width, getAttackArea().height);

         */

        // PRINTS THE COLLISION BOX OF THE PLAYER
        /*
        g2.setColor(new Color(255, 0, 0));
        g2.fillRect(screenX + getSolidArea().x, screenY + getSolidArea().y, getSolidArea().width, getSolidArea().height);
         */

//        RESET THE OPACITY FOR FUTURE IMAGES
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    // Get and set methods
    public KeyHandler getKeyH() {
        return keyH;
    }

    public void setKeyH(KeyHandler keyH) {
        this.keyH = keyH;
    }

    public Spirit[] getSpirits() {
        return spirits;
    }

    public void setSpirits(Spirit[] spirits) {
        this.spirits = spirits;
    }

    public int getCurrentSpiritIndex() {
        return currentSpiritIndex;
    }

    public void setCurrentSpiritIndex(int currentSpiritIndex) {
        this.currentSpiritIndex = currentSpiritIndex;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public int getNumTotems() {
        return numTotems;
    }

    public void setNumTotems(int numTotems) {
        this.numTotems = numTotems;
    }

    public double getBearHitboxScale() {
        return bearHitboxScale;
    }

    public void setBearHitboxScale(double bearHitboxScale) {
        this.bearHitboxScale = bearHitboxScale;
    }

    public double getEagleHitboxScale() {
        return eagleHitboxScale;
    }

    public void setEagleHitboxScale(double eagleHitboxScale) {
        this.eagleHitboxScale = eagleHitboxScale;
    }

    public double getTurtleHitboxScale() {
        return turtleHitboxScale;
    }

    public void setTurtleHitboxScale(double turtleHitboxScale) {
        this.turtleHitboxScale = turtleHitboxScale;
    }

    public double getBearAttackBoxScaleSize() {
        return bearAttackBoxScaleSize;
    }

    public void setBearAttackBoxScaleSize(double bearAttackBoxScaleSize) {
        this.bearAttackBoxScaleSize = bearAttackBoxScaleSize;
    }

    public double getEagleAttackBoxScaleSize() {
        return eagleAttackBoxScaleSize;
    }

    public void setEagleAttackBoxScaleSize(double eagleAttackBoxScaleSize) {
        this.eagleAttackBoxScaleSize = eagleAttackBoxScaleSize;
    }

    public double getTurtleAttackBoxScaleSize() {
        return turtleAttackBoxScaleSize;
    }

    public void setTurtleAttackBoxScaleSize(double turtleAttackBoxScaleSize) {
        this.turtleAttackBoxScaleSize = turtleAttackBoxScaleSize;
    }

    public int getMonsterIndex() {
        return monsterIndex;
    }

    public void setMonsterIndex(int monsterIndex) {
        this.monsterIndex = monsterIndex;
    }

    public boolean isBearSpecialUnlocked() {
        return bearSpecialUnlocked;
    }

    public void setBearSpecialUnlocked(boolean bearSpecialUnlocked) {
        this.bearSpecialUnlocked = bearSpecialUnlocked;
    }

    public boolean isEagleSpecialUnlocked() {
        return eagleSpecialUnlocked;
    }

    public void setEagleSpecialUnlocked(boolean eagleSpecialUnlocked) {
        this.eagleSpecialUnlocked = eagleSpecialUnlocked;
    }

    public boolean isTurtleSpecialUnlocked() {
        return turtleSpecialUnlocked;
    }

    public void setTurtleSpecialUnlocked(boolean turtleSpecialUnlocked) {
        this.turtleSpecialUnlocked = turtleSpecialUnlocked;
    }

    public int getInvincibilityCounter() {
        return invincibilityCounter;
    }

    public void setInvincibilityCounter(int invincibilityCounter) {
        this.invincibilityCounter = invincibilityCounter;
    }

    public int getPrimaryICD() {
        return primaryICD;
    }

    public void setPrimaryICD(int primaryICD) {
        this.primaryICD = primaryICD;
    }

    public int getSecondaryICD() {
        return secondaryICD;
    }

    public void setSecondaryICD(int secondaryICD) {
        this.secondaryICD = secondaryICD;
    }
}