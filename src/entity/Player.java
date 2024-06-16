package entity;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_EagleShot;
import object.OBJ_Water_Jet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Player extends Entity {
    KeyHandler keyH;//CALL ON KEYHANDLER CLASS

    public Spirit[] spirits = new Spirit[3];// CREATE THREE SPIRITS
    public int currentSpiritIndex = 0; // KEEP TRACK ON CURRENT SPIRIT

    //POSITION RELATIVE TO PANEL
    public final int screenX;
    public final int screenY;

    public int numTotems = 0; // KEEP TRACK OF NUMBER OF TOTEMS PLAYER HAS COLLECTED

//    SCALING FACTORS FOR PLAYER HIT BOXES
    public double bearHitboxScale = 0.5;
    public double eagleHitboxScale = 0.5;
    public double turtleHitboxScale = 0.5;

//    ATTACKING ZONE, A HITBOX TO DETERMINE WHETHER AN ATTACK REGISTERS ON A MONSTER
    public double bearAttackBoxScaleSize = 1.25; // BEAR SPIRIT ATTACKING HIT BOX SCALE
    public double eagleAttackBoxScaleSize = 1.25;// EAGLE SPIRIT ATTACKING HIT BOX SCALE
    public double turtleAttackBoxScaleSize = 1;// TURTLE SPIRIT ATTACKING HIT BOX SCALE

    //INDICES
    int monsterIndex;

    //CHECKERS
    public boolean bearSpecialUnlocked = false;
    public boolean eagleSpecialUnlocked = false;
    public boolean turtleSpecialUnlocked = false;

    //COUNTERS
    public int invincibilityCounter = 0;
    public int primaryICD = 0;//internal cooldown for attacks
    public int secondaryICD = 0;//internal cooldown for special/secondary moves

    public Player(GamePanel gp, KeyHandler keyH) { //CREATE DEFAULT ATTRIBUTES (CONSTRUCTOR)

        super(gp); // CALL ENTITY CLASS
        this.keyH = keyH;// CALL ON KEY HANDLER CLASS

        //PUTS PLAYER INTO THE MIDDLE OF THE SCREEN
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        setDefaultValues();// CALL ON DEFAULT CONSTRUCTOR METHOD

//        RETRIEVE ANIMATION SPRITES FOR MOVING, PRIMARY ATTACK, AND SPECIAL ATTACK
        getPlayerImage();
        getPlayerAttackImage();
        getPlayerSpecialAttackImage();
    }

    public Spirit getCurrentSpirit() { // RETURN CURRENT SPIRIT
        return spirits[currentSpiritIndex];
    }

    public void setDefaultValues() {// CREATE DEFAULT VALUES TO SPAWN THE PLAYER

//        SPAWN PLAYER AT COORDINATES 53, 50, WHICH IS IN THE CENTER AREA
        worldX = gp.tileSize * 53;
        worldY = gp.tileSize * 50;

        speed = 6;//PLAYER MOVES AT 6 PIXELS A FRAME, OR APPROX. 2 TILES PER SECOND
        direction = "right";// PLAYER FACES RIGHT BY DEFAULT

//        CREATE TURTLE AND EAGLE PROJECTILES
        projectile = new OBJ_Water_Jet(gp);
        targetProjectile = new OBJ_EagleShot(gp);

//        INITIALIZE INDIVUDAL SPIRIT SPRITES AND THEIR HEALTH
        spirits[0] = new Spirit(gp, "Bear", 9, 9,
                (int) (gp.tileSize * (1.0 - bearHitboxScale)) / 2,
                (int) (gp.tileSize * (1.0 - bearHitboxScale)) / 2,
                (int) (gp.tileSize * bearHitboxScale),
                (int) (gp.tileSize * bearHitboxScale),
                (int) (gp.tileSize * bearAttackBoxScaleSize),
                (int) (gp.tileSize * bearAttackBoxScaleSize),
                1, 4);
        spirits[1] = new Spirit(gp, "Eagle", 5, 5,
                (int) (gp.tileSize * eagleHitboxScale) / 2,
                (int) (gp.tileSize * eagleHitboxScale) / 2,
                (int) (gp.tileSize * eagleHitboxScale),
                (int) (gp.tileSize * eagleHitboxScale),
                (int) (gp.tileSize * eagleAttackBoxScaleSize),
                (int) (gp.tileSize * eagleAttackBoxScaleSize),
                1, 4);
        spirits[2] = new Spirit(gp, "Turtle", 8, 8,
                (int) (gp.tileSize * (1.0 - turtleHitboxScale)) / 2,
                (int) (gp.tileSize * (1.0 - turtleHitboxScale)) / 2,
                (int) (gp.tileSize * turtleHitboxScale),
                (int) (gp.tileSize * turtleHitboxScale),
                (int) (gp.tileSize * turtleAttackBoxScaleSize),
                (int) (gp.tileSize * turtleAttackBoxScaleSize),
                1, 4);
        switchSpirit(0); // STARS THE GAME AS THE BEAR SPIRIT
    }

//    RESETS GAME IF ALL SPIRITS HAVE DIED
    public void restoreSettings() {
        Arrays.fill(gp.npc, null);
        Arrays.fill(gp.monster, null);
        //CLEAR THE ENTIRE GAME
        gp.projectileList.clear();
        gp.targetProjectileList.clear();
        gp.entityList.clear();

//        RESET GAME BACK TO START
        setDefaultValues();
        for (int i = 0; i < gp.player.spirits.length; i++) { // MAKES EVERY SPIRIT ALIVE
            gp.player.spirits[i].dead = false;
        }
        gp.player.isDying = false; // MAKES IT SO PLAYER IS NO LONGER DYING
//        RESET NPC'S AND MONSTERS
        gp.aSetter.setNPC();
        gp.aSetter.setMonster();

        // RESETS THINGS DISPLAYED ON SCREEN
        keyH.checkDrawTime = false;
        keyH.displayControls = false;
        keyH.displayMap = false;
        gp.map.miniMapOn = false;
        gp.player.onPath = false;
        gp.tileM.drawPath = false;

        setDefaultValues(); // sets the default player values
    }

//    RETRIEVE PLAYER MOVEMENT IMAGES
    public void getPlayerImage() {
        Spirit currentSpirit = getCurrentSpirit(); // GET CURRENT SPIRIT

//        SETS PLAYER'S IMAGES TO THE CURRENT SPIRIT'S IMAGES
        up1 = currentSpirit.up1;
        up2 = currentSpirit.up2;
        down1 = currentSpirit.down1;
        down2 = currentSpirit.down2;
        left1 = currentSpirit.left1;
        left2 = currentSpirit.left2;
        right1 = currentSpirit.right1;
        right2 = currentSpirit.right2;

//        USE SETUP METHOD FROM ENTITY CLASS TO RETRIEVE WALKING FILES DEPENDING ON THE CURRENT SPIRIT
        if (currentSpirit.name.equals("Bear")) { //BEAR IMAGES
            up1 = setup("bear/bear_up", 1, 1);
            up2 = setup("bear/bear_up_2", 1, 1);
            down1 = setup("bear/bear_down", 1, 1);
            down2 = setup("bear/bear_down_2", 1, 1);
            left1 = setup("bear/bear_left", 1, 1);
            left2 = setup("bear/bear_left_2", 1, 1);
            right1 = setup("bear/bear_right", 1, 1);
            right2 = setup("bear/bear_right_2", 1, 1);

        } else if (currentSpirit.name.equals("Eagle")) { //EAGLE IMAGES
            up1 = setup("eagle/eagle_up", 1, 1);
            up2 = setup("eagle/eagle_up_2", 1, 1);
            down1 = setup("eagle/eagle_down", 1, 1);
            down2 = setup("eagle/eagle_down_2", 1, 1);
            left1 = setup("eagle/eagle_left", 1, 1);
            left2 = setup("eagle/eagle_left_2", 1, 1);
            right1 = setup("eagle/eagle_right", 1, 1);
            right2 = setup("eagle/eagle_right_2", 1, 1);

        } else if (currentSpirit.name.equals("Turtle")) {// TURTLE IMAGES
            up1 = setup("turtle/turtle_up", 1.25, 1.25);
            up2 = setup("turtle/turtle_up_2", 1.25, 1.25);
            down1 = setup("turtle/turtle_down", 1.25, 1.25);
            down2 = setup("turtle/turtle_down_2", 1.25, 1.25);
            left1 = setup("turtle/turtle_left", 1.25, 1.25);
            left2 = setup("turtle/turtle_left_2", 1.25, 1.25);
            right1 = setup("turtle/turtle_right", 1.25, 1.25);
            right2 = setup("turtle/turtle_right_2", 1.25, 1.25);
        }
    }

//    USE SETUP METHOD FROM ENTITY CLASS TO RETRIEVE PRIMARY ATTACK FILES DEPENDING ON THE CURRENT SPIRIT
    public void getPlayerAttackImage() {
        if (getCurrentSpirit().name.equals("Bear")) {// BEAR IMAGES
            attackUp1 = setup("bear/bear_up_attack_1", 1.25, 1.25);
            attackUp2 = setup("bear/bear_up_attack_2", 1.25, 1.25);
            attackUp3 = setup("bear/bear_up_attack_3", 1.25, 1.25);
            attackDown1 = setup("bear/bear_down_attack_1", 1.25, 1.25);
            attackDown2 = setup("bear/bear_down_attack_2", 1.25, 1.25);
            attackDown3 = setup("bear/bear_down_attack_3", 1.25, 1.25);
            attackLeft1 = setup("bear/bear_left_attack_1", 1.25, 1.25);
            attackLeft2 = setup("bear/bear_left_attack_2", 1.25, 1.25);
            attackLeft3 = setup("bear/bear_left_attack_3", 1.25, 1.25);
            attackRight1 = setup("bear/bear_right_attack_1", 1.25, 1.25);
            attackRight2 = setup("bear/bear_right_attack_2", 1.25, 1.25);
            attackRight3 = setup("bear/bear_right_attack_3", 1.25, 1.25);
        }
        if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE IMAGES
            attackUp1 = setup("eagle/eagle_up_attack_1", 1.25, 1.25);
            attackUp2 = setup("eagle/eagle_up_attack_2", 1.25, 1.25);
            attackUp3 = setup("eagle/eagle_up_attack_3", 1.25, 1.25);
            attackDown1 = setup("eagle/eagle_down_attack_1", 1.25, 1.25);
            attackDown2 = setup("eagle/eagle_down_attack_2", 1.25, 1.25);
            attackDown3 = setup("eagle/eagle_down_attack_3", 1.25, 1.25);
            attackLeft1 = setup("eagle/eagle_left_attack_1", 1.25, 1.25);
            attackLeft2 = setup("eagle/eagle_left_attack_2", 1.25, 1.25);
            attackLeft3 = setup("eagle/eagle_left_attack_3", 1.25, 1.25);
            attackRight1 = setup("eagle/eagle_right_attack_1", 1.25, 1.25);
            attackRight2 = setup("eagle/eagle_right_attack_2", 1.25, 1.25);
            attackRight3 = setup("eagle/eagle_right_attack_3", 1.25, 1.25);
        }
        if (getCurrentSpirit().name.equals("Turtle")) {// TURTLE IMAGES
            attackUp1 = setup("turtle/turtle_up_attack_1", 1.7, 1.7);
            attackUp2 = setup("turtle/turtle_up_attack_2", 1.7, 1.7);
            attackUp3 = setup("turtle/turtle_up_attack_3", 1.7, 1.7);
            attackDown1 = setup("turtle/turtle_down_attack_1", 1.7, 1.7);
            attackDown2 = setup("turtle/turtle_down_attack_2", 1.7, 1.7);
            attackDown3 = setup("turtle/turtle_down_attack_3", 1.7, 1.7);
            attackLeft1 = setup("turtle/turtle_left_attack_1", 1.7, 1.7);
            attackLeft2 = setup("turtle/turtle_left_attack_2", 1.7, 1.7);
            attackLeft3 = setup("turtle/turtle_left_attack_3", 1.7, 1.7);
            attackRight1 = setup("turtle/turtle_right_attack_1", 1.7, 1.7);
            attackRight2 = setup("turtle/turtle_right_attack_2", 1.7, 1.7);
            attackRight3 = setup("turtle/turtle_right_attack_3", 1.7, 1.7);
        }
    }

//    USE SETUP METHOD FROM ENTITY CLASS TO RETRIEVE SPECIAL ATTACK FILES DEPENDING ON CURRENT SPIRIT
    public void getPlayerSpecialAttackImage() {
        if (getCurrentSpirit().name.equals("Bear")) {// BEAR IMAGES
            //UP SPECIALS
            specialUp1 = setup("bear/bear_up_special_1", 1, 1);
            specialUp2 = setup("bear/bear_up_special_2", 1, 1);
            specialUp3 = setup("bear/bear_up_special_3", 1, 1);
            specialUp4 = setup("bear/bear_up_special_4", 1, 1);
            specialUp5 = setup("bear/bear_up_special_5", 1, 1);
            specialUp6 = setup("bear/bear_up_special_6", 1, 1);
            specialUp7 = setup("bear/bear_up_special_6", 1, 1);

            //DOWN SPECIALS
            specialDown1 = setup("bear/bear_down_special_1", 1, 1);
            specialDown2 = setup("bear/bear_down_special_2", 1, 1);
            specialDown3 = setup("bear/bear_down_special_3", 1, 1);
            specialDown4 = setup("bear/bear_down_special_4", 1, 1);
            specialDown5 = setup("bear/bear_down_special_5", 1, 1);
            specialDown6 = setup("bear/bear_down_special_6", 1, 1);
            specialDown7 = setup("bear/bear_down_special_6", 1, 1);

            //LEFT SPECIALS
            specialLeft1 = setup("bear/bear_left_special_1", 1, 1);
            specialLeft2 = setup("bear/bear_left_special_2", 1, 1);
            specialLeft3 = setup("bear/bear_left_special_3", 1, 1);
            specialLeft4 = setup("bear/bear_left_special_4", 1, 1);
            specialLeft5 = setup("bear/bear_left_special_5", 1, 1);
            specialLeft6 = setup("bear/bear_left_special_6", 1, 1);
            specialLeft7 = setup("bear/bear_left_special_6", 1, 1);

            //RIGHT SPECIALS
            specialRight1 = setup("bear/bear_right_special_1", 1, 1);
            specialRight2 = setup("bear/bear_right_special_2", 1, 1);
            specialRight3 = setup("bear/bear_right_special_3", 1, 1);
            specialRight4 = setup("bear/bear_right_special_4", 1, 1);
            specialRight5 = setup("bear/bear_right_special_5", 1, 1);
            specialRight6 = setup("bear/bear_right_special_6", 1, 1);
            specialRight7 = setup("bear/bear_right_special_6", 1, 1);
        }
        if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE IMAGES
            //UP SPECIALS
            specialUp1 = setup("eagle/eagle_up_special_1", 1.25, 1.25);
            specialUp2 = setup("eagle/eagle_up_special_2", 1.25, 1.25);
            specialUp3 = setup("eagle/eagle_up_special_3", 1.25, 1.25);
            specialUp4 = setup("eagle/eagle_up_special_4", 1.25, 1.25);
            specialUp5 = setup("eagle/eagle_up_special_5", 1.25, 1.25);
            specialUp6 = setup("eagle/eagle_up_special_6", 1.25, 1.25);
            specialUp7 = setup("eagle/eagle_up_special_6", 1.25, 1.25);

            //DOWN SPECIALS
            specialDown1 = setup("eagle/eagle_down_special_1", 1.25, 1.25);
            specialDown2 = setup("eagle/eagle_down_special_2", 1.25, 1.25);
            specialDown3 = setup("eagle/eagle_down_special_3", 1.25, 1.25);
            specialDown4 = setup("eagle/eagle_down_special_4", 1.25, 1.25);
            specialDown5 = setup("eagle/eagle_down_special_5", 1.25, 1.25);
            specialDown6 = setup("eagle/eagle_down_special_6", 1.25, 1.25);
            specialDown7 = setup("eagle/eagle_down_special_6", 1.25, 1.25);

            //LEFT SPECIALS
            specialLeft1 = setup("eagle/eagle_left_special_1", 1.25, 1.25);
            specialLeft2 = setup("eagle/eagle_left_special_2", 1.25, 1.25);
            specialLeft3 = setup("eagle/eagle_left_special_3", 1.25, 1.25);
            specialLeft4 = setup("eagle/eagle_left_special_4", 1.25, 1.25);
            specialLeft5 = setup("eagle/eagle_left_special_5", 1.25, 1.25);
            specialLeft6 = setup("eagle/eagle_left_special_6", 1.25, 1.25);
            specialLeft7 = setup("eagle/eagle_left_special_6", 1.25, 1.25);

            //RIGHT SPECIALS
            specialRight1 = setup("eagle/eagle_right_special_1", 1.25, 1.25);
            specialRight2 = setup("eagle/eagle_right_special_2", 1.25, 1.25);
            specialRight3 = setup("eagle/eagle_right_special_3", 1.25, 1.25);
            specialRight4 = setup("eagle/eagle_right_special_4", 1.25, 1.25);
            specialRight5 = setup("eagle/eagle_right_special_5", 1.25, 1.25);
            specialRight6 = setup("eagle/eagle_right_special_6", 1.25, 1.25);
            specialRight7 = setup("eagle/eagle_right_special_6", 1.25, 1.25);
        }
        if (getCurrentSpirit().name.equals("Turtle")) {// TURTLE IMAGES
            //UP SPECIALS
            specialUp1 = setup("turtle/turtle_up_special_1", 1.7, 1.7);
            specialUp2 = setup("turtle/turtle_up_special_2", 1.7, 1.7);
            specialUp3 = setup("turtle/turtle_up_special_3", 1.7, 1.7);
            specialUp4 = setup("turtle/turtle_up_special_4", 1.7, 1.7);
            specialUp5 = setup("turtle/turtle_up_special_5", 1.7, 1.7);
            specialUp6 = setup("turtle/turtle_up_special_6", 1.7, 1.7);
            specialUp7 = setup("turtle/turtle_up_special_7", 1.7, 1.7);

            //DOWN SPECIALS
            specialDown1 = setup("turtle/turtle_down_special_1", 1.7, 1.7);
            specialDown2 = setup("turtle/turtle_down_special_2", 1.7, 1.7);
            specialDown3 = setup("turtle/turtle_down_special_3", 1.7, 1.7);
            specialDown4 = setup("turtle/turtle_down_special_4", 1.7, 1.7);
            specialDown5 = setup("turtle/turtle_down_special_5", 1.7, 1.7);
            specialDown6 = setup("turtle/turtle_down_special_6", 1.7, 1.7);
            specialDown7 = setup("turtle/turtle_down_special_7", 1.7, 1.7);

            //LEFT SPECIALS
            specialLeft1 = setup("turtle/turtle_left_special_1", 1.7, 1.7);
            specialLeft2 = setup("turtle/turtle_left_special_2", 1.7, 1.7);
            specialLeft3 = setup("turtle/turtle_left_special_3", 1.7, 1.7);
            specialLeft4 = setup("turtle/turtle_left_special_4", 1.7, 1.7);
            specialLeft5 = setup("turtle/turtle_left_special_5", 1.7, 1.7);
            specialLeft6 = setup("turtle/turtle_left_special_6", 1.7, 1.7);
            specialLeft7 = setup("turtle/turtle_left_special_7", 1.7, 1.7);

            //RIGHT SPECIALS
            specialRight1 = setup("turtle/turtle_right_special_1", 1.7, 1.7);
            specialRight2 = setup("turtle/turtle_right_special_2", 1.7, 1.7);
            specialRight3 = setup("turtle/turtle_right_special_3", 1.7, 1.7);
            specialRight4 = setup("turtle/turtle_right_special_4", 1.7, 1.7);
            specialRight5 = setup("turtle/turtle_right_special_5", 1.7, 1.7);
            specialRight6 = setup("turtle/turtle_right_special_6", 1.7, 1.7);
            specialRight7 = setup("turtle/turtle_right_special_7", 1.7, 1.7);
        }
    }

//    UPDATE METHOD THAT GETS CALLED ON EACH FRAME
    public void update() {
        if (onPath) {
            // SET DESTINATION TILE TO THE START OF THE MAZE
            int goalCol = 0;
            int goalRow = 0;

            // sets the destination tile to the next totem the player needs to collect
            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] == null) {
                    if (i >= 3 && i <= 8) { // if the user just destroyed the brick, find the end of the maze
                        goalCol = 76;
                        goalRow = 33;
                        break;
                    }
                } else if (gp.obj[i] != null) {
                    if (numTotems < 3) { // find the next totem
                        goalCol = gp.obj[i].worldX / gp.tileSize;
                        goalRow = gp.obj[i].worldY / gp.tileSize;
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

        //ANIMTATION FOR IF PLAYER IS PRIMARY ATTACKING AND NOT SPECIAL ATTACKING
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
            if (keyH.primaryPressed && !keyH.secondaryPressed &&  !specialAttacking && primaryICD > 60) {
                spriteCounter = 0;
                primaryICD = 0;
                attacking = true;
            }

//            SIMULATES AN ATTACK SO LONG AS RIGHT CLICK IS THE ONLY THING BEING PRESSED AND AN ATTACK IS NOT ALREADY
//            HAPPENING AND IF ICD HAS REFRESHED
            if (keyH.secondaryPressed && !keyH.primaryPressed && !attacking && secondaryICD > 100) {
                spriteCounter = 0;
                secondaryICD = 0;
                specialAttacking = true;
            }
        }

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

            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

//            PLAYER CAN ONLY MOVE IF COLLISION IS FALSE AND IF NOT ATTACKING, MOVES PLAYER BY CHANGING WORLD
//            COORDINATES BY SPEED VALUE
            if (!collisionOn && !attacking && !specialAttacking) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

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
                invincible = false;
                invincibilityCounter = 0;
            }
        }

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
                deadCounter++;
                if (deadCounter <= 240) {
                    if (deadCounter % 30 == 0) {
                        deadFlicker = !deadFlicker;
                    }
                } else {
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
        spriteCounter++;//UPDATE ANIMATION COUNTER
        if (spriteCounter <= 10) {//SHOW FIRST ANIMATION FRAME FOR THE FIRST 10 GAME TICKS
            spriteNum = 1;
        }
        if (spriteCounter > 10 && spriteCounter <= 15) {//SHOW SECOND ANIMATION FRAME FOR THE SECOND 5 GAME TICKS
            spriteNum = 2;

//            SAVE THE CURRENT WORLD COORDINATES AND THE SOLID AREAS TO BE ABLE TO RESET TO THEM LATER AFTER
//            ANIMATION IS DONE
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

//      CREATES THE SPIRIT'S ATTACKING HIT BOX DEPENDING ON WHICH SPIRIT IS ACTIVE AND WHICH DIRECTION IT IS FACING
//            MANUAL ADJUSTMENTS HAVE BEEN MADE SO THE ANIMATIONS ARE FLUSH WITH EACH OTHER AND THE HIT ATTACK BOX
            switch (direction) {
                case "up":
                    switch(getCurrentSpirit().name) {
                        case "Bear":
                            worldX += (int) (attackArea.width - (gp.tileSize * 1.6));
                            worldY += (int) (attackArea.height - (gp.tileSize * 2.6));
                            break;
                        case "Eagle":
                            worldX += (int) (attackArea.width - (gp.tileSize * 1.6));
                            worldY += (int) (attackArea.height - (gp.tileSize * 2.6));
                            break;
                        case "Turtle":
                            worldX += (int) (attackArea.width - (gp.tileSize * 1.2));
                            worldY += (int) (attackArea.height - (gp.tileSize * 2.2));
                            break;
                    }
                    break;
                case "down":
                    switch(getCurrentSpirit().name) {
                        case "Bear":
                            worldX += (int) (attackArea.width - (gp.tileSize * 1.6));
                            worldY += (int) (attackArea.height - (gp.tileSize * 0.7));
                            break;
                        case "Eagle":
                            worldX += (int) (attackArea.width - (gp.tileSize * 1.6));
                            worldY += (int) (attackArea.height - (gp.tileSize * 0.8));
                            break;
                        case "Turtle":
                            worldX += (int) (attackArea.width - (gp.tileSize * 1.2));
                            worldY += (int) (attackArea.height - (gp.tileSize * 0.5));
                            break;
                    }
                    break;
                case "left":
                    switch(getCurrentSpirit().name) {
                        case "Bear":
                            worldX += (int) (attackArea.width - (gp.tileSize * 2.6));
                            worldY += (int) (attackArea.height - (gp.tileSize * 1.6));
                            break;
                        case "Eagle":
                            worldX += (int) (attackArea.width - (gp.tileSize * 2.5));
                            worldY += (int) (attackArea.height - (gp.tileSize * 1.6));
                            break;
                        case "Turtle":
                            worldX += (int) (attackArea.width - (gp.tileSize * 2.1));
                            worldY += (int) (attackArea.height - (gp.tileSize * 1.4));
                            break;
                    }
                    break;
                case "right":
                    switch(getCurrentSpirit().name) {
                        case "Bear":
                            worldX += (int) (attackArea.width - (gp.tileSize * 0.6));
                            worldY += (int) (attackArea.height - (gp.tileSize * 1.7));
                            break;
                        case "Eagle":
                            worldX += (int) (attackArea.width - (gp.tileSize * 0.6));
                            worldY += (int) (attackArea.height - (gp.tileSize * 1.7));
                            break;
                        case "Turtle":
                            worldX += (int) (attackArea.width - (gp.tileSize * 0.4));
                            worldY += (int) (attackArea.height - (gp.tileSize * 1.3));
                            break;
                    }
                    break;
            }

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
                        projectile.set(worldX + (int) (attackArea.width - (gp.tileSize * 1.2)),
                                worldY + (int) (attackArea.height - (gp.tileSize * 2.2)), direction, true, this);
                        break;
                    case "down":
                        projectile.set(worldX + (int) (attackArea.width - (gp.tileSize * 1.2)),
                                worldY + (int) (attackArea.height - (gp.tileSize * 0.5)), direction, true, this);
                        break;
                    case "left":
                        projectile.set(worldX + (int) (attackArea.width - (gp.tileSize * 2.1)),
                                worldY + (int) (attackArea.height - (gp.tileSize * 1.4)), direction, true, this);
                        break;
                    case "right":
                        projectile.set(worldX + (int) (attackArea.width - (gp.tileSize * 0.4)),
                                worldY + (int) (attackArea.height - (gp.tileSize * 1.3)), direction, true, this);
                        break;
                }

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
            shotAvailableCounter ++;
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
                    System.out.println(spirits[0].health);
//                    HEAL BEAR SPIRIT
                    if (spirits[0].health < 8) {
                        spirits[0].health += 3;
                    } else {
                        spirits[0].health = spirits[0].maxHealth;
                    }
                }
                //TODO
                // find a way to increase attack for 10 seconds or smth idk
            }
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
                //TODO
                // once sprite health has been decided, we can hard code some healing numbers instead of restoring all health
            }
//            EAGLE EYE SHOT
            if (getCurrentSpirit().name.equals("Eagle")) {
//                CHECK IF THE EAGLE TOTEM HAS BEEN UNLOCKED IN ORDER TO HEAL, OTHERWISE JUST PLAYS THE ANIMATION
                if (eagleSpecialUnlocked) {

//                    FIND INDEX OF THE CLOSEST MONSTER TO EAGLE
                    int targetSmallestDistance = 999;
                    int targetIndex = -1;
                    for (int i = 0; i < gp.monster.length ; i++) {
                        if (gp.monster[i] != null) { // MAKE SURE MONSTER EXISTS
                            if (getDistance(i) < targetSmallestDistance) { // CHECKS IF THE DISTANCE IS SMALLET THAN
                                // THE SMALLEST DISTANCE
                                targetIndex = i;
                                targetSmallestDistance = getDistance(i);
                            }
                        }
                    }
                    //IF THERE ARE NO MONSTERS NEARBY
                    if (targetIndex == -1) {
                        gp.ui.showMessage("There are no monsters nearby for the eagle eye to lock onto");
                    }
                    else {
//                        SPAWN EAGLE EYE PROJECTILES BASED ON WHAT DIRECTION EAGLE IS FACING
                        switch (direction) {
                            case "up":
                                targetProjectile.set((int) (worldX + attackArea.width - (gp.tileSize * 1.35)),
                                        (int) (worldY - attackArea.height + (gp.tileSize * 0.3)), true, targetIndex);
                                break;
                            case "down":
                                targetProjectile.set((int) (worldX + attackArea.width - (gp.tileSize * 1.35)), (int)
                                        (worldY + attackArea.height + (gp.tileSize * -0.5)), true, targetIndex);
                                break;
                            case "left":
                                targetProjectile.set((int) (worldX - attackArea.width + (gp.tileSize * 0.2)), (int)
                                        (worldY + attackArea.height - (gp.tileSize * 1.2)), true, targetIndex);
                                break;
                            case "right":
                                targetProjectile.set((int) (worldX + attackArea.width - (gp.tileSize * 0.4)), (int)
                                        (worldY - attackArea.height + (gp.tileSize * 1.3)), true, targetIndex);
                                break;
                        }
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
    public int getDistance (int i) {
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
                case "Totem":// PLAYER IS TOUCHING A TOTEM OBJECT
                    numTotems++; // INCREASE THE NUMBER OF TOTEMS THE PLAYER HAS COLLECTED
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
                    gp.obj[index] = null; // removes the object
                    if (numTotems < 3) {
                        if (index == 0) { // turtle totem collected
                            turtleSpecialUnlocked = true;
                            gp.ui.showMessage("Turtle Special Unlocked");
                        } else if (index == 1) { // eagle totem collected
                            eagleSpecialUnlocked = true;
                            gp.ui.showMessage("Eagle Special Unlocked");
                        } else { // bear totem collected
                            bearSpecialUnlocked = true;
                            gp.ui.showMessage("Bear Special Unlocked");
                        }
                    }
                    else if (numTotems == 3) {
                        gp.ui.showCollectionMessage("Congratulations, all three totems have been collected. I think I hear a door opening somewhere");
                    }
                    else {
                        gp.ui.completionMessageOn = true;
                    }
                    break;
                case "Wall":// PLAYER IS TOUCHING A WALL OBJECT AT THE MAZE

//                    CHECK IF PLAYER HAS THREE TOTEMS
                    if (numTotems == 3) {
                        gp.obj[index] = null; // DESTROYES THE WALL
                    }
//                    DISPLAY MESSAGE SAYING PLAYER NEEDS TO CONTINUE COLLECTING TOTEMS IN ORDER TO UNLOCK THE MAZE
                    else {
                        gp.ui.showMessage("You need to collect " + (3 - numTotems) + " more totems to get past the wall");
                    }
                    break;
            }
        }
    }

//    NPC INTERACTION METHOD
    public void interactNPC(int i) {
        if (i != 999) {
            System.out.println("you are hitting an npc");
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
                    if (spriteNum == 1) {image = attackUp1;}
                    if (spriteNum == 2) {image = attackUp2;}
                    if (spriteNum == 3) {image = attackUp3;}
                    break;
                case "down":
                    if (getCurrentSpirit().name.equals("Bear")) {// BEAR SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.4);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.25);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {//EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.33);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.47);
                    } else {// TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.58);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.62);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {image = attackDown1;}
                    if (spriteNum == 2) {image = attackDown2;}
                    if (spriteNum == 3) {image = attackDown3;}
                    break;
                case "left":
                    if (getCurrentSpirit().name.equals("Bear")) {// BEAR SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.38);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.38);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {// EAGLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.38);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.525);
                    } else {// TURTLE SPRITE
                        tempScreenX = screenX - (int) (gp.tileSize * 0.58);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.68);
                    }
//                    SET THE SPRITE IMAGE DEPENDING ON WHAT ANIMATION FRAME
                    if (spriteNum == 1) {image = attackLeft1;}
                    if (spriteNum == 2) {image = attackLeft2;}
                    if (spriteNum == 3) {image = attackLeft3;}
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
                    if (spriteNum == 1) {image = attackRight1;}
                    if (spriteNum == 2) {image = attackRight2;}
                    if (spriteNum == 3) {image = attackRight3;}
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
                    if (spriteNum == 1) {image = specialUp1;}
                    if (spriteNum == 2) {image = specialUp2;}
                    if (spriteNum == 3) {image = specialUp3;}
                    if (spriteNum == 4) {image = specialUp4;}
                    if (spriteNum == 5) {image = specialUp5;}
                    if (spriteNum == 6) {image = specialUp6;}
                    if (spriteNum == 7) {image = specialUp7;}
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
                    if (spriteNum == 1) {image = specialDown1;}
                    if (spriteNum == 2) {image = specialDown2;}
                    if (spriteNum == 3) {image = specialDown3;}
                    if (spriteNum == 4) {image = specialDown4;}
                    if (spriteNum == 5) {image = specialDown5;}
                    if (spriteNum == 6) {image = specialDown6;}
                    if (spriteNum == 7) {image = specialDown7;}
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
                    if (spriteNum == 1) {image = specialLeft1;}
                    if (spriteNum == 2) {image = specialLeft2;}
                    if (spriteNum == 3) {image = specialLeft3;}
                    if (spriteNum == 4) {image = specialLeft4;}
                    if (spriteNum == 5) {image = specialLeft5;}
                    if (spriteNum == 6) {image = specialLeft6;}
                    if (spriteNum == 7) {image = specialLeft7;}
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
                    if (spriteNum == 1) {image = specialRight1;}
                    if (spriteNum == 2) {image = specialRight2;}
                    if (spriteNum == 3) {image = specialRight3;}
                    if (spriteNum == 4) {image = specialRight4;}
                    if (spriteNum == 5) {image = specialRight5;}
                    if (spriteNum == 6) {image = specialRight6;}
                    if (spriteNum == 7) {image = specialRight7;}
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
                    if (spriteNum == 1) {image = up1;}
                    if (spriteNum == 2) {image = up2;}
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
                    if (spriteNum == 1) {image = down1;}
                    if (spriteNum == 2) {image = down2;}
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
                    if (spriteNum == 1) {image = left1;}
                    if (spriteNum == 2) {image = left2;}
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
                    if (spriteNum == 1) {image = right1;}
                    if (spriteNum == 2) {image = right2;}
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
        /*
        tempScreenX = screenX + solidArea.x;
        tempScreenY = screenY + solidArea.y;
        switch (direction) {
            case "up":
                switch(getCurrentSpirit().name) {
                    case "Bear":
                        tempScreenX = (int) (screenX + attackArea.width - (gp.tileSize * 1.6));
                        tempScreenY = (int) (screenY + attackArea.height - (gp.tileSize * 2.6));
                        break;
                    case "Eagle":
                        tempScreenX = (int) (screenX + attackArea.width - (gp.tileSize * 1.6));
                        tempScreenY = (int) (screenY + attackArea.height - (gp.tileSize * 2.6));
                        break;
                    case "Turtle":
                        tempScreenX = (int) (screenX + attackArea.width - (gp.tileSize * 1.2));
                        tempScreenY = (int) (screenY + attackArea.height - (gp.tileSize * 2.2));
                        break;
                }
                break;
            case "down":
                switch(getCurrentSpirit().name) {
                    case "Bear":
                        tempScreenX = (int) (screenX + attackArea.width - (gp.tileSize * 1.6));
                        tempScreenY = (int) (screenY + attackArea.height - (gp.tileSize * 0.7));
                        break;
                    case "Eagle":
                        tempScreenX = (int) (screenX + attackArea.width - (gp.tileSize * 1.6));
                        tempScreenY = (int) (screenY + attackArea.height - (gp.tileSize * 0.8));
                        break;
                    case "Turtle":
                        tempScreenX = (int) (screenX + attackArea.width - (gp.tileSize * 1.2));
                        tempScreenY = (int) (screenY + attackArea.height - (gp.tileSize * 0.5));
                        break;
                }
                break;
            case "left":
                switch(getCurrentSpirit().name) {
                    case "Bear":
                        tempScreenX = (int) (screenX + attackArea.width - (gp.tileSize * 2.6));
                        tempScreenY = (int) (screenY + attackArea.height - (gp.tileSize * 1.6));
                        break;
                    case "Eagle":
                        tempScreenX = (int) (screenX + attackArea.width - (gp.tileSize * 2.5));
                        tempScreenY = (int) (screenY + attackArea.height - (gp.tileSize * 1.6));
                        break;
                    case "Turtle":
                        tempScreenX = (int) (screenX + attackArea.width - (gp.tileSize * 2.1));
                        tempScreenY = (int) (screenY + attackArea.height - (gp.tileSize * 1.4));
                        break;
                }
                break;
            case "right":
                switch(getCurrentSpirit().name) {
                    case "Bear":
                        tempScreenX = (int) (screenX + attackArea.width - (gp.tileSize * 0.6));
                        tempScreenY = (int) (screenY + attackArea.height - (gp.tileSize * 1.7));
                        break;
                    case "Eagle":
                        tempScreenX = (int) (screenX + attackArea.width - (gp.tileSize * 0.6));
                        tempScreenY = (int) (screenY + attackArea.height - (gp.tileSize * 1.7));
                        break;
                    case "Turtle":
                        tempScreenX = (int) (screenX + attackArea.width - (gp.tileSize * 0.4));
                        tempScreenY = (int) (screenY + attackArea.height - (gp.tileSize * 1.3));
                        break;
                }
                break;
        }
        g2.drawRect(tempScreenX, tempScreenY, attackArea.width, attackArea.height);

         */

        // PRINTS THE COLLISION BOX OF THE PLAYER
        /*
        g2.setColor(new Color(255, 0, 0));
        g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
         */

//        RESET THE OPACITY FOR FUTURE IMAGES
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}