package entity;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_EagleShot;
import object.OBJ_Water_Jet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    KeyHandler keyH;//call on keyhandler class

    public Spirit[] spirits = new Spirit[3];//create three spirites
    public int currentSpiritIndex = 0; // keeps track of the current spirit

//    POSITION RELATIVE TO THE SCREEN
    public final int screenX;
    public final int screenY;

    public int numTotems = 0; // keeps track of the number of totems the player has collected

//    SCALING FACTORS FOR HIT BOXES AND ATTACK AREAS
    public double bearHitboxScale = 0.5;//bear hit box scale
    public double eagleHitboxScale = 0.5;//eagle hit box scale
    public double turtleHitboxScale = 0.5;//turtle hit box scale
    public double bearAttackBoxScaleSize = 1.25;//bear attack hit box scale
    public double eagleAttackBoxScaleSize = 1.25;//eagle attack hit box scale
    public double turtleAttackBoxScaleSize = 1;//turtle attack scale different b/c png is 40x40 instead of 32x32 pixels

    //INDICES
    int monsterIndex;

    //COUNTERS
    public int invincibilityCounter = 0;//counts how long player is invisible for
    public int primaryICD = 0;//internal cooldown for attacks
    public int secondaryICD = 0;//internal cooldown for special/secondary moves

    public Player(GamePanel gp, KeyHandler keyH) { //create default attributes (constructor)

        super(gp); // call on Entity class
        this.keyH = keyH;//call on keyhandler class

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);//ScreenX is in the center of the window
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);//ScreenX is in the center of the window

        setDefaultValues();//sets default values for the player
        getPlayerImage();//call on player image depending on the spirit selected
        getPlayerAttackImage();//call on player attack image depending on the spirit selected
        getPlayerSpecialAttackImage();//call on player special attack image depending on the spirit selected
    }

//    returns the  current spirit
    public Spirit getCurrentSpirit() { // gets the current spirit
        return spirits[currentSpiritIndex];
    }

    public void setDefaultValues() {//create default values to spawn the player (default constructor)

        //53 50
        worldX = gp.tileSize * 53; // sets the default position x-coordinate
        worldY = gp.tileSize * 50; //sets the default position y-coordinate
        speed = 6;//player moves at 6 pixels per frame, almost four tiles per second
        direction = "right";//sets starting direction to right, but can be any direction
        projectile = new OBJ_Water_Jet(gp);//create water projectile for turtle normal attack
        targetProjectile = new OBJ_EagleShot(gp);//create targeting projectile for eagle special attack

//        INITIALIZES THE SPIRITS AND THEIR HEALTH VALUES

        //bear spirit
        spirits[0] = new Spirit(gp, "Bear", 10, 9,
                (int) (gp.tileSize * (1.0 - bearHitboxScale)) / 2,
                (int) (gp.tileSize * (1.0 - bearHitboxScale)) / 2,
                (int) (gp.tileSize * bearHitboxScale),
                (int) (gp.tileSize * bearHitboxScale),
                (int) (gp.tileSize * bearAttackBoxScaleSize),
                (int) (gp.tileSize * bearAttackBoxScaleSize),
                1, 4);

        //eagle spirit
        spirits[1] = new Spirit(gp, "Eagle", 6, 5,
                (int) (gp.tileSize * eagleHitboxScale) / 2,
                (int) (gp.tileSize * eagleHitboxScale) / 2,
                (int) (gp.tileSize * eagleHitboxScale),
                (int) (gp.tileSize * eagleHitboxScale),
                (int) (gp.tileSize * eagleAttackBoxScaleSize),
                (int) (gp.tileSize * eagleAttackBoxScaleSize),
                1, 4);

        //turtle spirit
        spirits[2] = new Spirit(gp, "Turtle", 8, 8,
                (int) (gp.tileSize * (1.0 - turtleHitboxScale)) / 2,
                (int) (gp.tileSize * (1.0 - turtleHitboxScale)) / 2,
                (int) (gp.tileSize * turtleHitboxScale),
                (int) (gp.tileSize * turtleHitboxScale),
                (int) (gp.tileSize * turtleAttackBoxScaleSize),
                (int) (gp.tileSize * turtleAttackBoxScaleSize),
                1, 4);
        switchSpirit(0); // player starts as the bear spirit
    }

//    RETURNS MOVEMENT IMAGE FOR EVERY SPIRIT
    public void getPlayerImage() {
        Spirit currentSpirit = getCurrentSpirit(); // gets the current spirit

        // Sets the player's images to the current spirit's images
        up1 = currentSpirit.up1;
        up2 = currentSpirit.up2;
        down1 = currentSpirit.down1;
        down2 = currentSpirit.down2;
        left1 = currentSpirit.left1;
        left2 = currentSpirit.left2;
        right1 = currentSpirit.right1;
        right2 = currentSpirit.right2;


        if (currentSpirit.name.equals("Bear")) { // set up walking images if current spirit is bear spirit
            // use setup method from entity class to find image files and scale them properly
            up1 = setup("bear/bear_up", 1, 1);
            up2 = setup("bear/bear_up_2", 1, 1);
            down1 = setup("bear/bear_down", 1, 1);
            down2 = setup("bear/bear_down_2", 1, 1);
            left1 = setup("bear/bear_left", 1, 1);
            left2 = setup("bear/bear_left_2", 1, 1);
            right1 = setup("bear/bear_right", 1, 1);
            right2 = setup("bear/bear_right_2", 1, 1);

        } else if (currentSpirit.name.equals("Eagle")) { // set up walking images if current spirit is eagle spirit
//            use setup method from entity class to find image files and scale them properly
            up1 = setup("eagle/eagle_up", 1, 1);
            up2 = setup("eagle/eagle_up_2", 1, 1);
            down1 = setup("eagle/eagle_down", 1, 1);
            down2 = setup("eagle/eagle_down_2", 1, 1);
            left1 = setup("eagle/eagle_left", 1, 1);
            left2 = setup("eagle/eagle_left_2", 1, 1);
            right1 = setup("eagle/eagle_right", 1, 1);
            right2 = setup("eagle/eagle_right_2", 1, 1);

        } else if (currentSpirit.name.equals("Turtle")) {// set up walking images if current spirit is turtle spirit
//            use setup method from entity class to find image files and scale them properly
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

//    SET UP IMAGES FOR PLAYER NORMAL ATTACK
    public void getPlayerAttackImage() {

        if (getCurrentSpirit().name.equals("Bear")) {//runs if current spirit is the bear spirit
            //use set up method from entity class, scale it properly
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
        if (getCurrentSpirit().name.equals("Eagle")) {//runs if the current spirit is the eagle spirit
            //use set up method from entity class, scale it properly
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
        if (getCurrentSpirit().name.equals("Turtle")) {// runs if the current spirit is the turtle spirit
            //use set up method from entity class, scale it properly
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

//    GET IMAGES FOR SPECIAL ATTACKS
    public void getPlayerSpecialAttackImage() {
        if (getCurrentSpirit().name.equals("Bear")) {
            //up specials
            specialUp1 = setup("bear/bear_up_special_1", 1, 1);
            specialUp2 = setup("bear/bear_up_special_2", 1, 1);
            specialUp3 = setup("bear/bear_up_special_3", 1, 1);
            specialUp4 = setup("bear/bear_up_special_4", 1, 1);
            specialUp5 = setup("bear/bear_up_special_5", 1, 1);
            specialUp6 = setup("bear/bear_up_special_6", 1, 1);
            specialUp7 = setup("bear/bear_up_special_6", 1, 1);

            //down specials
            specialDown1 = setup("bear/bear_down_special_1", 1, 1);
            specialDown2 = setup("bear/bear_down_special_2", 1, 1);
            specialDown3 = setup("bear/bear_down_special_3", 1, 1);
            specialDown4 = setup("bear/bear_down_special_4", 1, 1);
            specialDown5 = setup("bear/bear_down_special_5", 1, 1);
            specialDown6 = setup("bear/bear_down_special_6", 1, 1);
            specialDown7 = setup("bear/bear_down_special_6", 1, 1);

            //left specials
            specialLeft1 = setup("bear/bear_left_special_1", 1, 1);
            specialLeft2 = setup("bear/bear_left_special_2", 1, 1);
            specialLeft3 = setup("bear/bear_left_special_3", 1, 1);
            specialLeft4 = setup("bear/bear_left_special_4", 1, 1);
            specialLeft5 = setup("bear/bear_left_special_5", 1, 1);
            specialLeft6 = setup("bear/bear_left_special_6", 1, 1);
            specialLeft7 = setup("bear/bear_left_special_6", 1, 1);

            //right specials
            specialRight1 = setup("bear/bear_right_special_1", 1, 1);
            specialRight2 = setup("bear/bear_right_special_2", 1, 1);
            specialRight3 = setup("bear/bear_right_special_3", 1, 1);
            specialRight4 = setup("bear/bear_right_special_4", 1, 1);
            specialRight5 = setup("bear/bear_right_special_5", 1, 1);
            specialRight6 = setup("bear/bear_right_special_6", 1, 1);
            specialRight7 = setup("bear/bear_right_special_6", 1, 1);
        }
        if (getCurrentSpirit().name.equals("Eagle")) {//get special attack sprites for the eagle spirit
            //up specials
            specialUp1 = setup("eagle/eagle_up_special_1", 1.25, 1.25);
            specialUp2 = setup("eagle/eagle_up_special_2", 1.25, 1.25);
            specialUp3 = setup("eagle/eagle_up_special_3", 1.25, 1.25);
            specialUp4 = setup("eagle/eagle_up_special_4", 1.25, 1.25);
            specialUp5 = setup("eagle/eagle_up_special_5", 1.25, 1.25);
            specialUp6 = setup("eagle/eagle_up_special_6", 1.25, 1.25);
            specialUp7 = setup("eagle/eagle_up_special_6", 1.25, 1.25);

            //down specials
            specialDown1 = setup("eagle/eagle_down_special_1", 1.25, 1.25);
            specialDown2 = setup("eagle/eagle_down_special_2", 1.25, 1.25);
            specialDown3 = setup("eagle/eagle_down_special_3", 1.25, 1.25);
            specialDown4 = setup("eagle/eagle_down_special_4", 1.25, 1.25);
            specialDown5 = setup("eagle/eagle_down_special_5", 1.25, 1.25);
            specialDown6 = setup("eagle/eagle_down_special_6", 1.25, 1.25);
            specialDown7 = setup("eagle/eagle_down_special_6", 1.25, 1.25);

            //left specials
            specialLeft1 = setup("eagle/eagle_left_special_1", 1.25, 1.25);
            specialLeft2 = setup("eagle/eagle_left_special_2", 1.25, 1.25);
            specialLeft3 = setup("eagle/eagle_left_special_3", 1.25, 1.25);
            specialLeft4 = setup("eagle/eagle_left_special_4", 1.25, 1.25);
            specialLeft5 = setup("eagle/eagle_left_special_5", 1.25, 1.25);
            specialLeft6 = setup("eagle/eagle_left_special_6", 1.25, 1.25);
            specialLeft7 = setup("eagle/eagle_left_special_6", 1.25, 1.25);

            //right specials
            specialRight1 = setup("eagle/eagle_right_special_1", 1.25, 1.25);
            specialRight2 = setup("eagle/eagle_right_special_2", 1.25, 1.25);
            specialRight3 = setup("eagle/eagle_right_special_3", 1.25, 1.25);
            specialRight4 = setup("eagle/eagle_right_special_4", 1.25, 1.25);
            specialRight5 = setup("eagle/eagle_right_special_5", 1.25, 1.25);
            specialRight6 = setup("eagle/eagle_right_special_6", 1.25, 1.25);
            specialRight7 = setup("eagle/eagle_right_special_6", 1.25, 1.25);
        }
        if (getCurrentSpirit().name.equals("Turtle")) {//get special attack sprites for the turtle
            //up specials and scale them properly
            specialUp1 = setup("turtle/turtle_up_special_1", 1.7, 1.7);
            specialUp2 = setup("turtle/turtle_up_special_2", 1.7, 1.7);
            specialUp3 = setup("turtle/turtle_up_special_3", 1.7, 1.7);
            specialUp4 = setup("turtle/turtle_up_special_4", 1.7, 1.7);
            specialUp5 = setup("turtle/turtle_up_special_5", 1.7, 1.7);
            specialUp6 = setup("turtle/turtle_up_special_6", 1.7, 1.7);
            specialUp7 = setup("turtle/turtle_up_special_7", 1.7, 1.7);
            //down specials
            specialDown1 = setup("turtle/turtle_down_special_1", 1.7, 1.7);
            specialDown2 = setup("turtle/turtle_down_special_2", 1.7, 1.7);
            specialDown3 = setup("turtle/turtle_down_special_3", 1.7, 1.7);
            specialDown4 = setup("turtle/turtle_down_special_4", 1.7, 1.7);
            specialDown5 = setup("turtle/turtle_down_special_5", 1.7, 1.7);
            specialDown6 = setup("turtle/turtle_down_special_6", 1.7, 1.7);
            specialDown7 = setup("turtle/turtle_down_special_7", 1.7, 1.7);

            //left specials
            specialLeft1 = setup("turtle/turtle_left_special_1", 1.7, 1.7);
            specialLeft2 = setup("turtle/turtle_left_special_2", 1.7, 1.7);
            specialLeft3 = setup("turtle/turtle_left_special_3", 1.7, 1.7);
            specialLeft4 = setup("turtle/turtle_left_special_4", 1.7, 1.7);
            specialLeft5 = setup("turtle/turtle_left_special_5", 1.7, 1.7);
            specialLeft6 = setup("turtle/turtle_left_special_6", 1.7, 1.7);
            specialLeft7 = setup("turtle/turtle_left_special_7", 1.7, 1.7);

            //right specials
            specialRight1 = setup("turtle/turtle_right_special_1", 1.7, 1.7);
            specialRight2 = setup("turtle/turtle_right_special_2", 1.7, 1.7);
            specialRight3 = setup("turtle/turtle_right_special_3", 1.7, 1.7);
            specialRight4 = setup("turtle/turtle_right_special_4", 1.7, 1.7);
            specialRight5 = setup("turtle/turtle_right_special_5", 1.7, 1.7);
            specialRight6 = setup("turtle/turtle_right_special_6", 1.7, 1.7);
            specialRight7 = setup("turtle/turtle_right_special_7", 1.7, 1.7);
        }
    }

//UPDATE METHOD THAT WILL BE CALLED WHILE THE GAME IS RUNNING
    public void update() {
        secondaryICD++;//increase secondary internal cooldown
        primaryICD++;//increase primary internal cooldown for every frame

        if (attacking && !specialAttacking) {//make sure the player is attacking and not special attacking at the
            // same time before running the attacking method
            attacking();
        }
        if (specialAttacking && !attacking) {//make sure the player is special attacking and not attacking at the
            // same time before running the special attacking method
            specialAttacking();
        }

//
        if (keyH.primaryPressed || keyH.secondaryPressed) {
            if (keyH.primaryPressed && !keyH.secondaryPressed &&  !specialAttacking && primaryICD > 60) {//if left
                // click, simulate an attack,
                // attack once
                // every 60 frames ie 2 seconds
//                getPlayerAttackImage();
                spriteCounter = 0;
                primaryICD = 0;
                attacking = true;
            }
            if (keyH.secondaryPressed && !keyH.primaryPressed && !attacking && secondaryICD > 100) {//if right click
                // has been
                // pressed, do a special attack once
                // every 400 frames ie 13 seconds
//                getPlayerSpecialAttackImage();
                spriteCounter = 0;
                secondaryICD = 0;
                specialAttacking = true;
            }
        }

        if ((keyH.upPressed || keyH.downPressed ||
                keyH.leftPressed || keyH.rightPressed) && !attacking && !specialAttacking) {//direction changes only
            // occur
            // if the key is
            // being pressed and player is not attacking
            if (keyH.upPressed) {//move up
                direction = "up";
            } else if (keyH.downPressed) { // move down
                direction = "down";
            } else if (keyH.leftPressed) { // move left
                //remove the else portion to make x and y movements independent
                direction = "left";
            } else if (keyH.rightPressed) { // move right
                direction = "right";
            }

            // check tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // check object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // check npc collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // check monster collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex); // runs when the user makes contact with the monster

            // player can only move if collision is false & if not attacking
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

            if (!attacking && !specialAttacking) {
                spriteCounter++;
                if (spriteCounter > 12) {//player image changes once every 12 frames, can adjust by increasing or decreasing
                    if (spriteNum == 1) {//changes the player to first walking sprite to second sprite
                        spriteNum = 2;
                    } else if (spriteNum == 2) {//changes the player sprite from second to first
                        spriteNum = 1;
                    }
                    spriteCounter = 0;//resets the sprite counter
                }
            }
        }
        if (invincible) { // if the player is invisible
            invincibilityCounter++;
            if (invincibilityCounter > 40) {
                invincible = false;
                invincibilityCounter = 0;
            }
        }

        if (keyH.onePressed) {
            switchSpirit(0); // switches to the bear
        } else if (keyH.twoPressed) {
            switchSpirit(1); // switches to the eagle

        } else if (keyH.threePressed) {
            switchSpirit(2);
        }

        if (gp.player.getCurrentSpirit().health <= 0) {
            isDying = true;
            displayDeathMessage = false;
            gp.player.getCurrentSpirit().dead = true;
            deadCounter++;
            if (deadCounter <= 240) {
                if (deadCounter % 30 == 0) {
                    if (deadFlicker) {
                        deadFlicker = false;
                    } else {
                        deadFlicker = true;
                    }
                }
            } else {
                isDying = false;
                displayDeathMessage = true; // display the death message
                deadFlicker = false;
                deadCounter = 0;
                int spiritIndex = nextAliveSpirit(); // gets the next alive spirit, returns -1 otherwise
                switchSpirit(spiritIndex);
            }
        }
    }

    public void switchSpirit(int spiritIndex) {
        currentSpiritIndex = spiritIndex; // sets the current spirit index to the spirit index
        getPlayerImage(); // reset the image pulls via getPlayerImage method
        getPlayerSpecialAttackImage();
        getPlayerAttackImage();

        // sets the player's hit box to the current spirit's hit box
        this.solidArea.x = getCurrentSpirit().solidArea.x;
        this.solidArea.y = getCurrentSpirit().solidArea.y;
        this.solidArea.width = getCurrentSpirit().solidArea.width;
        this.solidArea.height = getCurrentSpirit().solidArea.height;
        this.solidAreaDefaultX = getCurrentSpirit().x;
        this.solidAreaDefaultY = getCurrentSpirit().y;

        // sets the player's attack area to the current spirit's attack area
        this.attackArea.width = getCurrentSpirit().attackArea.width;
        this.attackArea.height = getCurrentSpirit().attackArea.height;

        // sets the player's attack and defense to the current spirit's attack and defense
        this.attack = getCurrentSpirit().attack;
        this.defense = getCurrentSpirit().defense;
    }

    public int nextAliveSpirit() {
        for (int i = currentSpiritIndex + 1; i < currentSpiritIndex + gp.player.spirits.length; i++) {
            int loopIndex = i % gp.player.spirits.length; // Calculates the loop index
            if (!gp.player.spirits[loopIndex].dead) {
                return loopIndex;
            }
        }
        return -1; // returns -1 if every spirit is dead
    }

    public void attacking() {
        spriteCounter++;
        if (spriteCounter <= 10) {
            spriteNum = 1;
        }
        if (spriteCounter > 10 && spriteCounter <= 15) {
            spriteNum = 2;

            // Save the current world coordinates and the solid areas to be able to reset to them later
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // gets the area the user can hit

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

            // attack area becomes solid area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            monsterIndex = gp.cChecker.checkEntity(this, gp.monster); // gets the monster that the user is making contact with

            // Reset the world coordinates and solid area to the previous coordinates
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if (spriteCounter > 15 && spriteCounter <= 25) {
            spriteNum = 3;
            if (getCurrentSpirit().name.equals("Turtle") && !projectile.projectileAlive && shotAvailableCounter == 30) { // the player can only shoot one projectile at a time (and no quicker than half a second apart)

                // sets default coordinates for the projectile
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

                // add the projectile to the list of projectiles
                gp.projectileList.add(projectile);

                shotAvailableCounter = 0; // resets the counter
            }
        }
        if (spriteCounter > 25) {
//             getPlayerImage();
            if (monsterIndex == 999 || gp.monster[monsterIndex] != null) { // if the player is not touching a monster or the monster has not just been killed
                damageMonster(monsterIndex, attack);
            }

            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }

        if (shotAvailableCounter < 30) { // after half a second
            shotAvailableCounter ++;
        }
    }

    public void specialAttacking() {
        spriteCounter++;

        if (spriteCounter <= 10) {
            spriteNum = 1;
        }
        if (spriteCounter > 10 && spriteCounter <= 15) {
            monsterIndex = gp.cChecker.checkEntity(this, gp.monster); // gets the monster that the user is making contact with
            spriteNum = 2;
        }
        if (spriteCounter > 15 && spriteCounter <= 20) {
            spriteNum = 3;
        }
        if (spriteCounter > 20 && spriteCounter <= 25) {
            spriteNum = 4;
        }
        if (spriteCounter > 25 && spriteCounter <= 30) {
            spriteNum = 5;
        }
        if (spriteCounter > 30 && spriteCounter <= 35) {
            spriteNum = 6;
        }
        if (spriteCounter > 35 && spriteCounter <= 40) {
            spriteNum = 7;
        }
        if (spriteCounter > 40) {
//            getPlayerImage();
            if (monsterIndex == 999 || gp.monster[monsterIndex] != null) { // if the player is not touching a monster or the monster has not just been killed
                damageMonster(monsterIndex, attack);
            }

            if (getCurrentSpirit().name.equals("Bear")) {//berserker mode for bear
                System.out.println(spirits[0].health);
                if (spirits[0].health < 8) {
                    spirits[0].health += 3;
                } else {
                    spirits[0].health = spirits[0].maxHealth;
                }
                //TODO
                // find a way to increase attack for 10 seconds or smth idk
            }
            if (getCurrentSpirit().name.equals("Turtle")) {
                if (spirits[0].health < spirits[0].maxHealth) {
                    spirits[0].health = spirits[0].maxHealth;
                }
                if (spirits[1].health < spirits[0].maxHealth) {
                    spirits[1].health = spirits[1].maxHealth;
                }
                if (spirits[2].health < spirits[0].maxHealth) {
                    spirits[2].health = spirits[2].maxHealth;
                }
                //TODO
                // once sprite health has been decided, we can hard code some healing numbers instead of restoring all health
            }
            if (getCurrentSpirit().name.equals("Eagle")) {
                int targetSmallestDistance = 999;
                int targetIndex = -1;
                for (int i = 0; i < gp.monster.length ; i++) {
                    if (gp.monster[i] != null) { // if the monster exists
                        if (getDistance(i) < targetSmallestDistance) { // checks if the smaller distance is smaller than the last smallest
                            targetIndex = i;
                            targetSmallestDistance = getDistance(i);
                        }
                    }
                }
                if (targetIndex == -1) { // if no monsters remain
                    gp.ui.showMessage("There are no monsters for the eagle eye to lock onto");
                }
                else {
                    switch (direction) {//spawn projectile based on what direction eagle is facing
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
                    // add the projectile to the list of projectiles
                    gp.targetProjectileList.add(targetProjectile);
                    shotAvailableCounter = 0;//resets the shot counter
                }
            }
            spriteNum = 1;
            spriteCounter = 0;
            specialAttacking = false;
        }

        int currentWorldX = worldX;
        int currentWorldY = worldY;
        int solidAreaWidth = solidArea.width;
        int solidAreaHeight = solidArea.height;

        // attack area becomes solid area
        solidArea.width = attackArea.width;
        solidArea.height = attackArea.height;

        // Reset the world coordinates and solid area to the previous coordinates
        worldX = currentWorldX;
        worldY = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;
    }
    public int getDistance (int i) {//gets current distance from player to a monster
        int currentDistance = (int) Math.sqrt(Math.pow(worldX - gp.monster[i].worldX, 2) + Math.pow(worldY - gp.monster[i].worldY, 2)); // calculates the distance between the player and the monster
        return currentDistance;
    }

    public void pickUpObject(int index) {
        if (index != 999) { // if index is 999, no object was touched
            String objectName = gp.obj[index].name;

            switch (objectName) {
                case "Totem":
                    numTotems++; // increases the number of totems the user has collected
                    gp.obj[index] = null; // removes the object
                    gp.ui.showMessage("You picked up a totem!");
                    break;
                case "Wall":
                    if (numTotems == 3) { // if the user has 3 totems
                        gp.obj[index] = null; // destroys the wall
                    }
                    else {
                        gp.ui.showMessage("You need to collect " + (3 - numTotems) + " more totems to get past the wall");
                    }
                    break;
            }
        }
    }

    public void interactNPC(int i) {
        if (i != 999) {
            System.out.println("you are hitting an npc");
        }
    }

    public void contactMonster(int index) { // modifies the player's invincibility if they make contact with a monster
        Spirit currentSpirit = gp.player.getCurrentSpirit(); // gets the current spirit

        if (index != 999) { // if index is 999, no monster was touched
            if (!invincible) {
                int damage = gp.monster[index].attack - defense;
                if (damage < 0) { // so damage is not negative
                    damage = 0;
                }
                currentSpirit.setHealth(currentSpirit.getHealth() - damage);
                invincible = true;
            }
        }
    }

    public void damageMonster(int index, int attack) { // deals damage to the monster

        if (index != 999) { // if index is 999, no monster was touched
            int damage = attack - gp.monster[index].defense;
            if (damage < 0) { // so damage is not negative
                damage = 0;
            }
            gp.monster[index].health -= damage;
            gp.monster[index].damageReaction();
            System.out.println("Hit"); // for debugging

            if (gp.monster[index].health <= 0) { // if the monster dies, replace that slot in the array with a null value
                gp.monster[index] = null;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        // sets temporary screen variables to account for change in spirit position when attacking
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        if (attacking && !specialAttacking) {
            switch (direction) {//check the direction, based on the direction it picks a different image
                case "up":
                    // compensate for  the sprite moving when doing the attacking animation
                    if (getCurrentSpirit().name.equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.33);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.45);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.33);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.38);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.58);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.68);
                    }
                    if (spriteNum == 1) {image = attackUp1;}
                    if (spriteNum == 2) {image = attackUp2;}
                    if (spriteNum == 3) {image = attackUp3;}
                    break;
                case "down":
                    // compensate the sprite moving  when doing the attacking animation
                    if (getCurrentSpirit().name.equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.4);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.25);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.33);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.47);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.58);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.62);
                    }
                    if (spriteNum == 1) {image = attackDown1;}
                    if (spriteNum == 2) {image = attackDown2;}
                    if (spriteNum == 3) {image = attackDown3;}
                    break;
                case "left":
                    // compensate the sprite moving when doing the attacking animation
                    if (getCurrentSpirit().name.equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.38);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.38);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.38);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.525);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.58);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.68);
                    }
                    if (spriteNum == 1) {image = attackLeft1;}
                    if (spriteNum == 2) {image = attackLeft2;}
                    if (spriteNum == 3) {image = attackLeft3;}
                    break;
                case "right":
                    // compensate the sprite moving when doing the attacking animation
                    if (getCurrentSpirit().name.equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.33);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.31);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.28);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.475);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.57);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.68);
                    }
                    if (spriteNum == 1) {image = attackRight1;}
                    if (spriteNum == 2) {image = attackRight2;}
                    if (spriteNum == 3) {image = attackRight3;}
                    break;
            }
        }
        if (specialAttacking && !attacking) {
            switch (direction) {//check the direction, based on the direction it picks a different image
                case "up":
                    // Moves the sprite when doing the special attacking animation
                    if (getCurrentSpirit().name.equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.2);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.2);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.323);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.38);
                    }
                    else {//turtle
                        tempScreenX = screenX - (int) (gp.tileSize * 0.567);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.677);
                    }
                    if (spriteNum == 1) {image = specialUp1;}
                    if (spriteNum == 2) {image = specialUp2;}
                    if (spriteNum == 3) {image = specialUp3;}
                    if (spriteNum == 4) {image = specialUp4;}
                    if (spriteNum == 5) {image = specialUp5;}
                    if (spriteNum == 6) {image = specialUp6;}
                    if (spriteNum == 7) {image = specialUp7;}
                    break;
                case "down":
                    // Moves the sprite when doing the special attacking animation
                    if (getCurrentSpirit().name.equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.25);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.18);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.325);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.469);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.58);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.62);
                    }
                    if (spriteNum == 1) {image = specialDown1;}
                    if (spriteNum == 2) {image = specialDown2;}
                    if (spriteNum == 3) {image = specialDown3;}
                    if (spriteNum == 4) {image = specialDown4;}
                    if (spriteNum == 5) {image = specialDown5;}
                    if (spriteNum == 6) {image = specialDown6;}
                    if (spriteNum == 7) {image = specialDown7;}
                    break;
                case "left":
                    // Moves the sprite when doing the special attacking animation
                    if (getCurrentSpirit().name.equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.259);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.252);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.378);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.53);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.6);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.57);
                    }
                    if (spriteNum == 1) {image = specialLeft1;}
                    if (spriteNum == 2) {image = specialLeft2;}
                    if (spriteNum == 3) {image = specialLeft3;}
                    if (spriteNum == 4) {image = specialLeft4;}
                    if (spriteNum == 5) {image = specialLeft5;}
                    if (spriteNum == 6) {image = specialLeft6;}
                    if (spriteNum == 7) {image = specialLeft7;}
                    break;
                case "right":
                    // Moves the sprite when doing the special attacking animation
                    if (getCurrentSpirit().name.equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.205);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.25);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.28);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.47);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.55);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.58);
                    }
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
        if (!specialAttacking && !attacking) {//drawing function for basic movement
            switch (direction) {//check the direction, based on the direction it picks a different image
                case "up":
                    if (getCurrentSpirit().name.equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.2);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.2);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.2);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.25);
                    }
                    else {//turtle
                        tempScreenX = screenX - (int) (gp.tileSize * 0.35);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.45);
                    }
                    if (spriteNum == 1) {image = up1;}
                    if (spriteNum == 2) {image = up2;}
                    break;
                case "down":
                    if (getCurrentSpirit().name.equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.25);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.15);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.2);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.35);
                    }
                    else {//turtle
                        tempScreenX = screenX - (int) (gp.tileSize * 0.35);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.4);
                    }
                    if (spriteNum == 1) {image = down1;}
                    if (spriteNum == 2) {image = down2;}
                    break;
                case "left":
                    if (getCurrentSpirit().name.equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.25);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.25);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.25);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.4);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.35);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.45);
                    }
                    if (spriteNum == 1) {image = left1;}
                    if (spriteNum == 2) {image = left2;}
                    break;
                case "right":
                    if (getCurrentSpirit().name.equals("Bear")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.2);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.25);
                    }
                    else if (getCurrentSpirit().name.equals("Eagle")) {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.15);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.35);
                    }
                    else {
                        tempScreenX = screenX - (int) (gp.tileSize * 0.35);
                        tempScreenY = screenY - (int) (gp.tileSize * 0.45);
                    }
                    if (spriteNum == 1) {image = right1;}
                    if (spriteNum == 2) {image = right2;}
                    break;
            }
        }
        if ((invincible && !gp.player.getCurrentSpirit().dead) || deadFlicker) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f)); // reduces the opacity to 70% to show when the player is invincible
        } else {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);//draws the image, null means we cannot type



        // Debugging
        // Draws the attack area of the player
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

        // For debugging
        g2.setColor(new Color(255, 0, 0));
        g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // resets the opacity for future images
    }
}