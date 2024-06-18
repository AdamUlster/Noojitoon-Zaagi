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

    private Spirit[] spirits = new Spirit[3];
    private int currentSpiritIndex = 0; // keeps track of the current spirit

    private final int screenX;
    private final int screenY;

    private int numTotems = 0; // keeps track of the number of totems the player has collected

    //scaling factors for hitboxes and attack areas
    private double bearHitboxScale = 0.5;//bear hit box scale
    private double eagleHitboxScale = 0.5;//eagle hit box scale
    private double turtleHitboxScale = 0.5;//turtle hit box scale
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
    private int berserkerCounter = 0;//BEAR SPIRIT BERSERKER MODE DURATION

    public Player(GamePanel gp, KeyHandler keyH) { //create default attributes (constructor)

        super(gp); // call on Entity class
        this.keyH = keyH;

        screenX = gp.getScreenWidth() / 2 - (gp.getTileSize() / 2);
        screenY = gp.getScreenHeight() / 2 - (gp.getTileSize() / 2);

        setDefaultValues();//sets default values for the player
        getPlayerImage();
        getPlayerAttackImage();
        getPlayerSpecialAttackImage();
    }

    public Spirit getCurrentSpirit() { // gets the current spirit
        return spirits[currentSpiritIndex];
    }

    private void setDefaultValues() {//create default values to spawn the player

        //53 50
        setWorldX(gp.getTileSize() * 53); // sets the default position x-coordinate
        setWorldY(gp.getTileSize() * 50); //sets the default position y-coordinate
        setSpeed(8);//sets speed to 4
        setDirection("right");//can input any direction
        setProjectile(new OBJ_Water_Jet(gp));
        setTargetProjectile(new OBJ_EagleShot(gp));

        // Initializes the spirits and their health values
        spirits[0] = new Spirit(gp, "Bear", 18, 18,
                (int) (gp.getTileSize() * (1.0 - bearHitboxScale)) / 2,
                (int) (gp.getTileSize() * (1.0 - bearHitboxScale)) / 2,
                (int) (gp.getTileSize() * bearHitboxScale),
                (int) (gp.getTileSize() * bearHitboxScale),
                (int) (gp.getTileSize() * bearAttackBoxScaleSize),
                (int) (gp.getTileSize() * bearAttackBoxScaleSize),
                1, 4);
        spirits[1] = new Spirit(gp, "Eagle", 14, 14,
                (int) (gp.getTileSize() * eagleHitboxScale) / 2,
                (int) (gp.getTileSize() * eagleHitboxScale) / 2,
                (int) (gp.getTileSize() * eagleHitboxScale),
                (int) (gp.getTileSize() * eagleHitboxScale),
                (int) (gp.getTileSize() * eagleAttackBoxScaleSize),
                (int) (gp.getTileSize() * eagleAttackBoxScaleSize),
                1, 4);
        spirits[2] = new Spirit(gp, "Turtle", 20, 20,
                (int) (gp.getTileSize() * (1.0 - turtleHitboxScale)) / 2,
                (int) (gp.getTileSize() * (1.0 - turtleHitboxScale)) / 2,
                (int) (gp.getTileSize() * turtleHitboxScale),
                (int) (gp.getTileSize() * turtleHitboxScale),
                (int) (gp.getTileSize() * turtleAttackBoxScaleSize),
                (int) (gp.getTileSize() * turtleAttackBoxScaleSize),
                1, 4);
        switchSpirit(0); // the player is the bear spirit to start
    }

//    reset the game after all spirits die
    private void restoreSettings() {
        Arrays.fill(gp.getNpc(), null);
        Arrays.fill(gp.getMonster(), null);
        gp.getProjectileList().clear();
        gp.getTargetProjectileList().clear();
        gp.getEntityList().clear();

        for (int i = 0; i < gp.getPlayer().spirits.length; i++) { // makes every spirit alive
            gp.getPlayer().spirits[i].setDead(false);
        }
        gp.getPlayer().setDying(false); // makes it so the player is no longer dying
        gp.getASetter().setNPC();
        gp.getASetter().setMonster();

        // resets the things displayed on the screen
        keyH.setCheckDrawTime(false);
        keyH.setDisplayControls(false);
        keyH.setDisplayMap(false);
        gp.getMap().setMiniMapOn(false);
        gp.getPlayer().setOnPath(false);
        gp.getTileM().setDrawPath(false);

        setDefaultValues(); // sets the default player values
    }

//    retrieve walking images
    private void getPlayerImage() {
        Spirit currentSpirit = getCurrentSpirit(); // gets the current spirit

        // Sets the player's images to the current spirit's images
        setUp1(currentSpirit.getUp1());
        setUp2(currentSpirit.getUp2());
        setDown1(currentSpirit.getDown1());
        setDown2(currentSpirit.getDown2());
        setLeft1(currentSpirit.getLeft1());
        setLeft2(currentSpirit.getLeft2());
        setRight1(currentSpirit.getRight1());
        setRight2(currentSpirit.getRight2());


        if (currentSpirit.getName().equals("Bear")) { // walking animation for only the bear pngs
            // call on setup method to find image files
            setUp1(setup("bear/bear_up", 1, 1));
            setUp2(setup("bear/bear_up_2", 1, 1));
            setDown1(setup("bear/bear_down", 1, 1));
            setDown2(setup("bear/bear_down_2", 1, 1));
            setLeft1(setup("bear/bear_left", 1, 1));
            setLeft2(setup("bear/bear_left_2", 1, 1));
            setRight1(setup("bear/bear_right", 1, 1));
            setRight2(setup("bear/bear_right_2", 1, 1));

        } else if (currentSpirit.getName().equals("Eagle")) { // walking animation for only the eagle pngs
            setUp1(setup("eagle/eagle_up", 1, 1));
            setUp2(setup("eagle/eagle_up_2", 1, 1));
            setDown1(setup("eagle/eagle_down", 1, 1));
            setDown2(setup("eagle/eagle_down_2", 1, 1));
            setLeft1(setup("eagle/eagle_left", 1, 1));
            setLeft2(setup("eagle/eagle_left_2", 1, 1));
            setRight1(setup("eagle/eagle_right", 1, 1));
            setRight2(setup("eagle/eagle_right_2", 1, 1));
        } else if (currentSpirit.getName().equals("Turtle")) {
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

    private void getPlayerAttackImage() {//get primary attack images
        if (getCurrentSpirit().getName().equals("Bear")) {
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
        if (getCurrentSpirit().getName().equals("Eagle")) {
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
        if (getCurrentSpirit().getName().equals("Turtle")) {
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

//    retrieve special attacking images
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

// run game logic for player every frame
    public void update() {
        if (isOnPath()) {
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

        secondaryICD++;//increase secondary internal cooldown for every frame, after 30 frames, will be able to attack
        // again
        primaryICD++;//increase primary internal cooldown for every frame, after 30 frames, will be able to attack again

        // Update the berserker timer and disable berserker mode after 5 seconds (300 ticks)
        if (berserkerMode) {
            berserkerCounter++;
            if (berserkerCounter % 30 == 0) {
                if (spirits[0].getHealth() != spirits[0].getMaxHealth()) {
                    spirits[0].setHealth(spirits[0].getHealth() + 1);
                }
            }
            if (berserkerCounter > 300) {
                berserkerMode = false;
                spirits[0].setAttack(1); // Reset to the bear's original attack value
                gp.getPlayer().setAttack(spirits[0].getAttack());
            }
        }
        if (isAttacking() && !isSpecialAttacking()) {//check if the player is attacking
            attacking();
        }
        if (isSpecialAttacking() && !isAttacking()) {
            specialAttacking();
        }

        if (keyH.isPrimaryPressed() || keyH.isSecondaryPressed()) {
            if (keyH.isPrimaryPressed() && !keyH.isSecondaryPressed() &&  !isSpecialAttacking() && primaryICD > 60) {//if left
                // click, simulate an attack,
                // attack once
                // every 60 frames ie 2 seconds
//                getPlayerAttackImage();
                setSpriteCounter(0);
                primaryICD = 0;
                setAttacking(true);
            }
            if (keyH.isSecondaryPressed() && !keyH.isPrimaryPressed() && !isAttacking() && secondaryICD > 100) {//if right click
                // has been
                // pressed, do a special attack once
                // every 400 frames ie 13 seconds
//                getPlayerSpecialAttackImage();
                setSpriteCounter(0);
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
                }
            }
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

//    switch spirit depending on which button is pressed
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

//    retrieve the next alive spirit
    private int nextAliveSpirit() {
        for (int i = currentSpiritIndex + 1; i < currentSpiritIndex + gp.getPlayer().spirits.length; i++) {
            int loopIndex = i % gp.getPlayer().spirits.length; // Calculates the loop index
            if (!gp.getPlayer().spirits[loopIndex].isDead()) {
                return loopIndex;
            }
        }
        return -1; // returns -1 if every spirit is dead
    }

//    attacking logic
    private void attacking() {
        setSpriteCounter(getSpriteCounter() + 1);
        if (getSpriteCounter() <= 10) {
            setSpriteNum(1);
        }
        if (getSpriteCounter() > 10 && getSpriteCounter() <= 15) {
            setSpriteNum(2);

            // Save the current world coordinates and the solid areas to be able to reset to them later
            int currentWorldX = getWorldX();
            int currentWorldY = getWorldY();
            int solidAreaWidth = getSolidArea().width;
            int solidAreaHeight = getSolidArea().height;

            // gets the area the user can hit

            switch (getDirection()) {
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

//    special attacking logic
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

            //            BERSERKER MODE FOR BEAR
            // BERSERKER MODE FOR BEAR
            if (getCurrentSpirit().getName().equals("Bear")) {
                // CHECK IF THE BEAR TOTEM HAS BEEN UNLOCKED IN ORDER TO DEAL DAMAGE, OTHERWISE JUST PLAYS THE ANIMATION
                if (bearSpecialUnlocked) {
                    // ACTIVATE BERSERKER MODE
                    berserkerMode = true;
                    berserkerCounter = 0; // Reset the timer when activating berserker mode

                    // GRANT BONUS HEALTH
                    spirits[0].setMaxHealth(26);

                    // INSTANTLY HEAL SOME DAMAGE IF BELOW THREE HEARTS
                    if (spirits[0].getHealth() < 6) {
                        spirits[0].setHealth(spirits[0].getHealth() + 10);
                    }

                    berserkerCounter = 0; // RESET COUNTER
                    spirits[0].setAttack(10); // any attack done by bear should now be one shot
                    gp.getPlayer().setAttack(spirits[0].getAttack());
                }
            }
//            TURTLE HEALING WAVE
            if (getCurrentSpirit().getName().equals("Turtle")) {
//                CHECK IF THE TURTLE TOTEM HAS BEEN UNLOCKED IN ORDER TO HEAL, OTHERWISE JUST PLAYS THE ANIMATION
                if (turtleSpecialUnlocked) {
//                    ITERATE THROUGH EACH SPIRIT, HEAL THEM TO MAX HEALTH
                    for (Spirit spirit : spirits) {
                        if (spirit.getHealth() < spirit.getMaxHealth()) {
                            spirit.setHealth(spirit.getMaxHealth());
                            spirit.setDead(false);
                        }
                    }
                }
            }
//            EAGLE EYE SHOT
            if (getCurrentSpirit().getName().equals("Eagle")) {
//                CHECK IF THE EAGLE TOTEM HAS BEEN UNLOCKED IN ORDER TO HEAL, OTHERWISE JUST PLAYS THE ANIMATION
                if (eagleSpecialUnlocked) {

//                    FIND INDEX OF THE CLOSEST MONSTER TO EAGLE
                    int targetSmallestDistance = 999;
                    int targetIndex = -1;
                    for (int i = 0; i < gp.getMonster().length; i++) {
                        if (gp.getMonster()[i] != null) { // MAKE SURE MONSTER EXISTS
                            if (getDistance(i) < targetSmallestDistance) { // CHECKS IF THE DISTANCE IS SMALLET THAN
                                // THE SMALLEST DISTANCE
                                targetIndex = i;
                                targetSmallestDistance = getDistance(i);
                            }
                        }
                    }
                    //IF THERE ARE NO MONSTERS NEARBY
                    if (targetIndex == -1) {
                        gp.getUi().showMessage("There are no monsters nearby for the eagle eye to lock onto");
                    } else {
//                        SPAWN EAGLE EYE PROJECTILES BASED ON WHAT DIRECTION EAGLE IS FACING
                        switch (getDirection()) {
                            case "up":
                                getTargetProjectile().set((int) (getWorldX() + getAttackArea().width - (gp.getTileSize() * 1.6)),
                                        (int) (getWorldY() + getAttackArea().height - (gp.getTileSize() * 2.6)), true, targetIndex);
                                break;
                            case "down":
                                getTargetProjectile().set((int) (getWorldX() + getAttackArea().width - (gp.getTileSize() * 1.6)), (int)
                                        (getWorldY() + getAttackArea().height - (gp.getTileSize() * 0.8)), true, targetIndex);
                                break;
                            case "left":
                                getTargetProjectile().set((int) (getWorldX() + getAttackArea().width - (gp.getTileSize() * 2.5)), (int)
                                        (getWorldY() + getAttackArea().height - (gp.getTileSize() * 1.6)), true, targetIndex);
                                break;
                            case "right":
                                getTargetProjectile().set((int) (getWorldX() + getAttackArea().width - (gp.getTileSize() * 0.6)), (int)
                                        (getWorldY() + getAttackArea().height - (gp.getTileSize() * 1.7)), true, targetIndex);
                                break;
                        }
//                        ADD PROJECTILE TO THE LIST OF PROJECTILES
                        gp.getTargetProjectileList().add(getTargetProjectile());
                        setShotAvailableCounter(0);//RESET SHOT COUNTER
                    }
                }
            }
            //RESET ANIMATION BACK TO WALKING SPRITES, TURN OFF SPECIAL ATTACKING
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

//    pick up object method
    private void pickUpObject(int index) {
        if (index != 999) { // if index is 999, no object was touched
            String objectName = gp.getObj()[index].getName();

            switch (objectName) {
                case "Totem":
                    numTotems++; // increases the number of totems the user has collected
                    gp.getObj()[index] = null; // removes the object
                    if (numTotems < 3) {
                        if (index == 0) { // turtle totem collected
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
                    else if (numTotems == 3) {
                        gp.getUi().showCollectionMessage("Congratulations, all three totems have been collected. I think I hear a door opening somewhere");
                    }
                    else {
                        gp.getUi().setCompletionMessageOn(true);
                    }
                    break;
                case "Wall":
                    if (numTotems == 3) { // if the user has 3 totems
                        gp.getObj()[index] = null; // destroys the wall
                    }
                    else {
                        gp.getUi().showMessage("You need to collect " + (3 - numTotems) + " more totems to get past the wall");
                    }
                    break;
            }
        }
    }

//    interact with npc
    private void interactNPC(int i) {
        if (i != 999) {
            System.out.println("you are hitting an npc");
        }
    }

//    damage plaer if in ontact with monster
    private void contactMonster(int index) { // modifies the player's invincibility if they make contact with a monster
        Spirit currentSpirit = gp.getPlayer().getCurrentSpirit(); // gets the current spirit

        if (index != 999) { // if index is 999, no monster was touched
            if (!isInvincible()) {
                int damage = gp.getMonster()[index].getAttack() - getDefense();
                if (damage < 0) { // so damage is not negative
                    damage = 0;
                }
                currentSpirit.setHealth(currentSpirit.getHealth() - damage);
                setInvincible(true);
            }
        }
    }

//    damage player if attacking
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
            }
        }
    }

//    draw player animations
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        // sets temporary screen variables to account for change in spirit position when attacking
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
        } else {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);//draws the image, null means we cannot type



        // Debugging
        // Draws the attack area of the player
        /*
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

         */
//        g2.drawRect(tempScreenX, tempScreenY, getAttackArea().width, getAttackArea().height);

        // For debugging
//        g2.setColor(new Color(255, 0, 0));
//        g2.fillRect(screenX + getSolidArea().x, screenY + getSolidArea().y, getSolidArea().width, getSolidArea().height);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // resets the opacity for future images
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

    public boolean isBerserkerMode() {
        return berserkerMode;
    }

    public void setBerserkerMode(boolean berserkerMode) {
        this.berserkerMode = berserkerMode;
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

    public int getBerserkerCounter() {
        return berserkerCounter;
    }

    public void setBerserkerCounter(int berserkerCounter) {
        this.berserkerCounter = berserkerCounter;
    }
}