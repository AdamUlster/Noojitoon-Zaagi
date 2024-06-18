package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    protected GamePanel gp;
    private BufferedImage image1, image2, image3;
    private BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    private BufferedImage attackUp1, attackUp2, attackUp3, attackDown1, attackDown2,
            attackDown3, attackLeft1, attackLeft2, attackLeft3, attackRight1,
            attackRight2, attackRight3;//primary attack booleans
    private BufferedImage specialUp1, specialUp2, specialUp3, specialUp4, specialUp5, specialUp6,
            specialUp7;//special moves for up direction
    private BufferedImage specialDown1, specialDown2, specialDown3, specialDown4, specialDown5, specialDown6,
            specialDown7;//special moves for down direction
    private BufferedImage specialLeft1, specialLeft2, specialLeft3, specialLeft4, specialLeft5, specialLeft6,
            specialLeft7;//special moves for left direction
    private BufferedImage specialRight1, specialRight2, specialRight3, specialRight4, specialRight5, specialRight6,
            specialRight7;//special moves for right direction
    private int x = 0;
    private int y = 0;
    private int width = 48;
    private int height = 48;
    private int xMove = 0; // keeps track of the x movement for projectiles
    private int yMove = 0; // keeps track of the y movement for projectiles
    private Rectangle solidArea = new Rectangle(x, y, width, height); // the collision box of the characterdd
    private Rectangle attackArea = new Rectangle(0, 0, 0, 0); // the part of the spirit that is attacking the monster

    //COUNTERS
    private int deadCounter = 0; // keeps track of how long the entity is dead for
    private int actionLockCounter = 0; // sets a pause for random movements in the npcs and other things
    private int spriteCounter = 0;
    private int shotAvailableCounter = 0; // sets a counter that tracks whether the user can shoot a projectile
    private int healthBarCounter = 0;

    //STATE
    private int worldX, worldY;
    private int solidAreaDefaultX, solidAreaDefaultY;
    private String direction = "down";
    private int spriteNum = 1;
    private boolean invincible = false; // sets whether the entity is immune to damage
    private boolean collisionOn = false;
    private boolean attacking = false;
    private boolean specialAttacking = false;
    private boolean displayDeathMessage = false; // whether the death message should be displayed
    private boolean alive = true; // keeps track of whether the projectile is alive
    private boolean dead = false; // sets whether the entity is dead
    private boolean isDying = false; // sets whether the entity is dying
    private boolean deadFlicker = false; // sets whether the entity should be flickering dead
    private boolean healthBarOn = false; // whether the monster has a health bar above them
    private boolean onPath = false; // keeps track of whether an entity should be following the best path to get to a certain location

    //CHARACTER ATTRIBUTES
    private int maxHealth; // maximum number of lives the entity has
    private int health; // current number of lives the entity has
    private String name;
    private boolean collision = false;
    private int type; // 0 = player, 1 = NPC, 2 = monster, 3 = projectile
    private int speed;
    private int attack;
    private int defense;
    private int useCost;
    private Projectile projectile;
    private TargetingProjectile targetProjectile;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {
    }

    public void damageReaction() {} // controls the monster's reaction to taking damage. This method is modified in the individual monster classes

    private void checkCollision() { // checks the entity collision with other tiles and other entities
        Spirit currentSpirit = gp.getPlayer().getCurrentSpirit(); // gets the current spirit

        collisionOn = false;
        gp.getCChecker().checkTile(this);
        gp.getCChecker().checkObject(this, false);
        gp.getCChecker().checkEntity(this, gp.getNpc());
        gp.getCChecker().checkEntity(this, gp.getMonster());
        boolean contactPlayer = gp.getCChecker().checkPlayer(this);

        if (this.type == 2 && contactPlayer) { // if this class is a monster and the monster has made contact with the player
            if (!gp.getPlayer().isInvincible()) {
                int damage = attack - gp.getPlayer().getDefense();
                currentSpirit.setHealth(currentSpirit.getHealth() - damage);
                gp.getPlayer().setInvincible(true); // the player is now invincible for a given period of time
            }
        }
    }

    public void update() {
        setAction();
        checkCollision();

        // entity can only move if collision is false
        if (!collisionOn) {
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

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
        int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();

        if (!attacking && !specialAttacking) {
            if (worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() && // only draws an object if it is in the user's field of view
                    worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
                    worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
                    worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) {
                switch (direction) {//check the direction, based on the direction it picks a different image
                    case "up":
                        if (spriteNum == 1) {image = up1;}
                        if (spriteNum == 2) {image = up2;}
                        break;
                    case "down":
                        if (spriteNum == 1) {image = down1;}
                        if (spriteNum == 2) {image = down2;}
                        break;
                    case "left":
                        if (spriteNum == 1) {image = left1;}
                        if (spriteNum == 2) {image = left2;}
                        break;
                    case "right":
                        if (spriteNum == 1) {image = right1;}
                        if (spriteNum == 2) {image = right2;}
                        break;
                }

                // Monster health bar
                if (type == 2) { // if the entity is a monster and the health bar should be displayed
                    double oneHealthLength = gp.getTileSize() / maxHealth; // size of one of a monster's lives
                    double healthBarValue = oneHealthLength * health;

                    g2.setColor(new Color(35, 35, 35));
                    g2.fillRect(screenX - 1, screenY - 16, gp.getTileSize() + 2, 12); // displays the outline above the health bar

                    g2.setColor(new Color(255, 0, 0));
                    g2.fillRect(screenX, screenY - 15, (int) healthBarValue, 10); // subtracts from screenY to display above monster and draws the rectangle the same size as the monster's health

                    healthBarCounter ++;

                    if (healthBarCounter > 600) { // after 10 seconds after its appearance, the monster's health bar disappears
                        healthBarCounter = 0;
                        healthBarOn = false;
                    }
                }
                g2.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null); // draws the attacking animation
            }

            // For debugging
            //g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

//    setup images and scale them
    protected BufferedImage setup(String imagePath, double widthScale, double heightScale) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/" + imagePath + ".png"));
            image = uTool.scaleImage(image, (int) (gp.getTileSize() * widthScale), (int) (gp.getTileSize() * heightScale));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

//    use pathfinding algorithm to get a path for the player to the totem
    void searchPathToTotem(int goalCol, int goalRow) {

        // gets the entity's current tile
        int startCol = (worldX + solidArea.x) / gp.getTileSize();
        int startRow = (worldY + solidArea.y) / gp.getTileSize();

        gp.getPFinderToTotem().setNodes(startCol, startRow, goalCol, goalRow);

        if (gp.getPFinderToTotem().search()) { // a path has been found

            int nextCol = gp.getPFinderToTotem().getPathList().get(0).getCol();
            int nexRow = gp.getPFinderToTotem().getPathList().get(0).getRow();
            if (nextCol == goalCol && nexRow == goalRow) {
                onPath = false; // stops the entity when it reaches its destination
            }
        }
    }

//    use pathfinding algorithm to get a path to the player
    protected void searchPathToPlayer(int goalCol, int goalRow) {
        // gets the entity's current tile
        int startCol = (worldX + solidArea.x) / gp.getTileSize();
        int startRow = (worldY + solidArea.y) / gp.getTileSize();

        gp.getPFinderToPlayer().setNodes(startCol, startRow, goalCol, goalRow);

        if (gp.getPFinderToPlayer().search()) { // a path has been found

            // Get the entity's next coordinates using the path to follow
            int nextX = gp.getPFinderToPlayer().getPathList().get(0).getCol() * gp.getTileSize();
            int nextY = gp.getPFinderToPlayer().getPathList().get(0).getRow() * gp.getTileSize();

            // Get the entity's solid area
            int entLeftX = worldX + solidArea.x;
            int entRightX = worldX + solidArea.x + solidArea.width;
            int entTopY = worldY + solidArea.y;
            int entBottomY = worldY + solidArea.y + solidArea.height;

            if (entTopY > nextY && entLeftX >= nextX && entRightX < nextX + gp.getTileSize()) {
                direction = "up";
            }
            else if (entTopY < nextY && entLeftX >= nextX && entRightX < nextX + gp.getTileSize()) {
                direction = "down";
            }
            else if (entTopY >= nextY && entBottomY < nextY + gp.getTileSize()) { // the entity can go either left or right
                if (entLeftX > nextX) {
                    direction = "left";
                }
                if (entLeftX < nextX) {
                    direction = "right";
                }
            }
            else if (entTopY > nextY && entLeftX > nextX) { // the entity can go either up or left
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            }
            else if (entTopY > nextY && entLeftX < nextX) { // the entity can go either up or right
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }
            else if (entTopY < nextY && entLeftX > nextX) { // the entity can go either down or left
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            }
            else if (entTopY < nextY && entLeftX < nextX) { // the entity can go either down or right
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }

            int nextCol = gp.getPFinderToPlayer().getPathList().get(0).getCol();
            int nexRow = gp.getPFinderToPlayer().getPathList().get(0).getRow();
            if (nextCol == goalCol && nexRow == goalRow) {
                onPath = false; // stops the entity when it reaches its destination
            }
        }
    }

    // Get and set methods
    public BufferedImage getImage1() {
        return image1;
    }

    public void setImage1(BufferedImage image1) {
        this.image1 = image1;
    }

    public BufferedImage getImage2() {
        return image2;
    }

    public void setImage2(BufferedImage image2) {
        this.image2 = image2;
    }

    public BufferedImage getImage3() {
        return image3;
    }

    public void setImage3(BufferedImage image3) {
        this.image3 = image3;
    }

    public BufferedImage getUp1() {
        return up1;
    }

    public BufferedImage getUp2() {
        return up2;
    }

    public BufferedImage getDown1() {
        return down1;
    }

    public BufferedImage getDown2() {
        return down2;
    }

    public BufferedImage getLeft1() {
        return left1;
    }

    public BufferedImage getLeft2() {
        return left2;
    }

    public BufferedImage getRight1() {
        return right1;
    }

    public BufferedImage getRight2() {
        return right2;
    }

    public BufferedImage getAttackUp1() {
        return attackUp1;
    }

    public BufferedImage getAttackUp2() {
        return attackUp2;
    }

    public BufferedImage getAttackUp3() {
        return attackUp3;
    }

    public BufferedImage getAttackDown1() {
        return attackDown1;
    }

    public BufferedImage getAttackDown2() {
        return attackDown2;
    }

    public BufferedImage getAttackDown3() {
        return attackDown3;
    }

    public BufferedImage getAttackLeft1() {
        return attackLeft1;
    }

    public BufferedImage getAttackLeft2() {
        return attackLeft2;
    }

    public BufferedImage getAttackLeft3() {
        return attackLeft3;
    }

    public BufferedImage getAttackRight1() {
        return attackRight1;
    }

    public BufferedImage getAttackRight2() {
        return attackRight2;
    }

    public BufferedImage getAttackRight3() {
        return attackRight3;
    }

    public BufferedImage getSpecialUp1() {
        return specialUp1;
    }

    public BufferedImage getSpecialUp2() {
        return specialUp2;
    }

    public BufferedImage getSpecialUp3() {
        return specialUp3;
    }

    public BufferedImage getSpecialUp4() {
        return specialUp4;
    }

    public BufferedImage getSpecialUp5() {
        return specialUp5;
    }

    public BufferedImage getSpecialUp6() {
        return specialUp6;
    }

    public BufferedImage getSpecialUp7() {
        return specialUp7;
    }

    public BufferedImage getSpecialDown1() {
        return specialDown1;
    }

    public BufferedImage getSpecialDown2() {
        return specialDown2;
    }

    public BufferedImage getSpecialDown3() {
        return specialDown3;
    }

    public BufferedImage getSpecialDown4() {
        return specialDown4;
    }

    public BufferedImage getSpecialDown5() {
        return specialDown5;
    }

    public BufferedImage getSpecialDown6() {
        return specialDown6;
    }

    public BufferedImage getSpecialDown7() {
        return specialDown7;
    }

    public BufferedImage getSpecialLeft1() {
        return specialLeft1;
    }

    public BufferedImage getSpecialLeft2() {
        return specialLeft2;
    }

    public BufferedImage getSpecialLeft3() {
        return specialLeft3;
    }

    public BufferedImage getSpecialLeft4() {
        return specialLeft4;
    }

    public BufferedImage getSpecialLeft5() {
        return specialLeft5;
    }

    public BufferedImage getSpecialLeft6() {
        return specialLeft6;
    }

    public BufferedImage getSpecialLeft7() {
        return specialLeft7;
    }

    public BufferedImage getSpecialRight1() {
        return specialRight1;
    }

    public BufferedImage getSpecialRight2() {
        return specialRight2;
    }

    public BufferedImage getSpecialRight3() {
        return specialRight3;
    }

    public BufferedImage getSpecialRight4() {
        return specialRight4;
    }

    public BufferedImage getSpecialRight5() {
        return specialRight5;
    }

    public BufferedImage getSpecialRight6() {
        return specialRight6;
    }

    public BufferedImage getSpecialRight7() {
        return specialRight7;
    }

    // Setters
    public void setUp1(BufferedImage up1) {
        this.up1 = up1;
    }

    public void setUp2(BufferedImage up2) {
        this.up2 = up2;
    }

    public void setDown1(BufferedImage down1) {
        this.down1 = down1;
    }

    public void setDown2(BufferedImage down2) {
        this.down2 = down2;
    }

    public void setLeft1(BufferedImage left1) {
        this.left1 = left1;
    }

    public void setLeft2(BufferedImage left2) {
        this.left2 = left2;
    }

    public void setRight1(BufferedImage right1) {
        this.right1 = right1;
    }

    public void setRight2(BufferedImage right2) {
        this.right2 = right2;
    }

    public void setAttackUp1(BufferedImage attackUp1) {
        this.attackUp1 = attackUp1;
    }

    public void setAttackUp2(BufferedImage attackUp2) {
        this.attackUp2 = attackUp2;
    }

    public void setAttackUp3(BufferedImage attackUp3) {
        this.attackUp3 = attackUp3;
    }

    public void setAttackDown1(BufferedImage attackDown1) {
        this.attackDown1 = attackDown1;
    }

    public void setAttackDown2(BufferedImage attackDown2) {
        this.attackDown2 = attackDown2;
    }

    public void setAttackDown3(BufferedImage attackDown3) {
        this.attackDown3 = attackDown3;
    }

    public void setAttackLeft1(BufferedImage attackLeft1) {
        this.attackLeft1 = attackLeft1;
    }

    public void setAttackLeft2(BufferedImage attackLeft2) {
        this.attackLeft2 = attackLeft2;
    }

    public void setAttackLeft3(BufferedImage attackLeft3) {
        this.attackLeft3 = attackLeft3;
    }

    public void setAttackRight1(BufferedImage attackRight1) {
        this.attackRight1 = attackRight1;
    }

    public void setAttackRight2(BufferedImage attackRight2) {
        this.attackRight2 = attackRight2;
    }

    public void setAttackRight3(BufferedImage attackRight3) {
        this.attackRight3 = attackRight3;
    }

    public void setSpecialUp1(BufferedImage specialUp1) {
        this.specialUp1 = specialUp1;
    }

    public void setSpecialUp2(BufferedImage specialUp2) {
        this.specialUp2 = specialUp2;
    }

    public void setSpecialUp3(BufferedImage specialUp3) {
        this.specialUp3 = specialUp3;
    }

    public void setSpecialUp4(BufferedImage specialUp4) {
        this.specialUp4 = specialUp4;
    }

    public void setSpecialUp5(BufferedImage specialUp5) {
        this.specialUp5 = specialUp5;
    }

    public void setSpecialUp6(BufferedImage specialUp6) {
        this.specialUp6 = specialUp6;
    }

    public void setSpecialUp7(BufferedImage specialUp7) {
        this.specialUp7 = specialUp7;
    }

    public void setSpecialDown1(BufferedImage specialDown1) {
        this.specialDown1 = specialDown1;
    }

    public void setSpecialDown2(BufferedImage specialDown2) {
        this.specialDown2 = specialDown2;
    }

    public void setSpecialDown3(BufferedImage specialDown3) {
        this.specialDown3 = specialDown3;
    }

    public void setSpecialDown4(BufferedImage specialDown4) {
        this.specialDown4 = specialDown4;
    }

    public void setSpecialDown5(BufferedImage specialDown5) {
        this.specialDown5 = specialDown5;
    }

    public void setSpecialDown6(BufferedImage specialDown6) {
        this.specialDown6 = specialDown6;
    }

    public void setSpecialDown7(BufferedImage specialDown7) {
        this.specialDown7 = specialDown7;
    }

    public void setSpecialLeft1(BufferedImage specialLeft1) {
        this.specialLeft1 = specialLeft1;
    }

    public void setSpecialLeft2(BufferedImage specialLeft2) {
        this.specialLeft2 = specialLeft2;
    }

    public void setSpecialLeft3(BufferedImage specialLeft3) {
        this.specialLeft3 = specialLeft3;
    }

    public void setSpecialLeft4(BufferedImage specialLeft4) {
        this.specialLeft4 = specialLeft4;
    }

    public void setSpecialLeft5(BufferedImage specialLeft5) {
        this.specialLeft5 = specialLeft5;
    }

    public void setSpecialLeft6(BufferedImage specialLeft6) {
        this.specialLeft6 = specialLeft6;
    }

    public void setSpecialLeft7(BufferedImage specialLeft7) {
        this.specialLeft7 = specialLeft7;
    }

    public void setSpecialRight1(BufferedImage specialRight1) {
        this.specialRight1 = specialRight1;
    }

    public void setSpecialRight2(BufferedImage specialRight2) {
        this.specialRight2 = specialRight2;
    }

    public void setSpecialRight3(BufferedImage specialRight3) {
        this.specialRight3 = specialRight3;
    }

    public void setSpecialRight4(BufferedImage specialRight4) {
        this.specialRight4 = specialRight4;
    }

    public void setSpecialRight5(BufferedImage specialRight5) {
        this.specialRight5 = specialRight5;
    }

    public void setSpecialRight6(BufferedImage specialRight6) {
        this.specialRight6 = specialRight6;
    }

    public void setSpecialRight7(BufferedImage specialRight7) {
        this.specialRight7 = specialRight7;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getxMove() {
        return xMove;
    }

    public void setxMove(int xMove) {
        this.xMove = xMove;
    }

    public int getyMove() {
        return yMove;
    }

    public void setyMove(int yMove) {
        this.yMove = yMove;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public void setSolidArea(Rectangle solidArea) {
        this.solidArea = solidArea;
    }

    public Rectangle getAttackArea() {
        return attackArea;
    }

    public void setAttackArea(Rectangle attackArea) {
        this.attackArea = attackArea;
    }

    public int getDeadCounter() {
        return deadCounter;
    }

    public void setDeadCounter(int deadCounter) {
        this.deadCounter = deadCounter;
    }

    public int getActionLockCounter() {
        return actionLockCounter;
    }

    public void setActionLockCounter(int actionLockCounter) {
        this.actionLockCounter = actionLockCounter;
    }

    public int getSpriteCounter() {
        return spriteCounter;
    }

    public void setSpriteCounter(int spriteCounter) {
        this.spriteCounter = spriteCounter;
    }

    public int getShotAvailableCounter() {
        return shotAvailableCounter;
    }

    public void setShotAvailableCounter(int shotAvailableCounter) {
        this.shotAvailableCounter = shotAvailableCounter;
    }

    public int getHealthBarCounter() {
        return healthBarCounter;
    }

    public void setHealthBarCounter(int healthBarCounter) {
        this.healthBarCounter = healthBarCounter;
    }

    public int getWorldX() {
        return worldX;
    }

    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }

    public int getSolidAreaDefaultX() {
        return solidAreaDefaultX;
    }

    public void setSolidAreaDefaultX(int solidAreaDefaultX) {
        this.solidAreaDefaultX = solidAreaDefaultX;
    }

    public int getSolidAreaDefaultY() {
        return solidAreaDefaultY;
    }

    public void setSolidAreaDefaultY(int solidAreaDefaultY) {
        this.solidAreaDefaultY = solidAreaDefaultY;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getSpriteNum() {
        return spriteNum;
    }

    public void setSpriteNum(int spriteNum) {
        this.spriteNum = spriteNum;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isCollisionOn() {
        return collisionOn;
    }

    public void setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isSpecialAttacking() {
        return specialAttacking;
    }

    public void setSpecialAttacking(boolean specialAttacking) {
        this.specialAttacking = specialAttacking;
    }

    public boolean isDisplayDeathMessage() {
        return displayDeathMessage;
    }

    public void setDisplayDeathMessage(boolean displayDeathMessage) {
        this.displayDeathMessage = displayDeathMessage;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isDying() {
        return isDying;
    }

    public void setDying(boolean dying) {
        isDying = dying;
    }

    public boolean isDeadFlicker() {
        return deadFlicker;
    }

    public void setDeadFlicker(boolean deadFlicker) {
        this.deadFlicker = deadFlicker;
    }

    public boolean isHealthBarOn() {
        return healthBarOn;
    }

    public void setHealthBarOn(boolean healthBarOn) {
        this.healthBarOn = healthBarOn;
    }

    public boolean isOnPath() {
        return onPath;
    }

    public void setOnPath(boolean onPath) {
        this.onPath = onPath;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getUseCost() {
        return useCost;
    }

    public void setUseCost(int useCost) {
        this.useCost = useCost;
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    public TargetingProjectile getTargetProjectile() {
        return targetProjectile;
    }

    public void setTargetProjectile(TargetingProjectile targetProjectile) {
        this.targetProjectile = targetProjectile;
    }
}