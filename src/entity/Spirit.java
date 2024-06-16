package entity;

import main.GamePanel;

import java.awt.*;

//SPIRIT SUBCLASS WITHIN ENTITY
public class Spirit extends Entity {

//    DEFAULT VALUES (CONSTRUCTOR)
    public Spirit(GamePanel gp, String name, int maxHealth, int health, int x, int y, int width, int height, int width2, int height2, int attack, int defense) {
        super(gp);//CALL ON GAME PANEL
        this.name = name; // SPIRIT NAME
        this.maxHealth = maxHealth;//SPIRIT MAX HEALTH
        this.health = health;//SPIRIT CURRENT HEALTH
        this.attack = attack;//SPIRIT ATTACK STRENGTH
        this.defense = defense;//SPIRIT DEFENSE STRENGTH

        // GIVES EACH SPRITE ITS OWN IMAGE
        if (name.equals("Bear")) {
            up1 = setup("bear/bear_up", 1, 1);
            up2 = setup("bear/bear_up_2", 1, 1);
            down1 = setup("bear/bear_down", 1, 1);
            down2 = setup("bear/bear_down_2", 1, 1);
            left1 = setup("bear/bear_left", 1, 1);
            left2 = setup("bear/bear_left_2", 1, 1);
            right1 = setup("bear/bear_right", 1, 1);
            right2 = setup("bear/bear_right_2", 1, 1);
        }
        else if (name.equals("Eagle")) {
            up1 = setup("eagle/eagle_up", 1, 1);
            up2 = setup("eagle/eagle_up_2", 1, 1);
            down1 = setup("eagle/eagle_down", 1, 1);
            down2 = setup("eagle/eagle_down_2", 1, 1);
            left1 = setup("eagle/eagle_left", 1, 1);
            left2 = setup("eagle/eagle_left_2", 1, 1);
            right1 = setup("eagle/eagle_right", 1, 1);
            right2 = setup("eagle/eagle_right_2", 1, 1);
        }
        else {
            up1 = setup("turtle/turtle_up", 1.8, 1.8);
            up2 = setup("turtle/turtle_up_2", 1.8, 1.8);
            down1 = setup("turtle/turtle_down", 1.8, 1.8);
            down2 = setup("turtle/turtle_down_2", 1.8, 1.8);
            left1 = setup("turtle/turtle_left", 1.8, 1.8);
            left2 = setup("turtle/turtle_left_2", 1.8, 1.8);
            right1 = setup("turtle/turtle_right", 1.8, 1.8);
            right2 = setup("turtle/turtle_right_2", 1.8, 1.8);
        }

        //COLLISION BOX
        this.solidArea = new Rectangle(x, y, width, height);

        // ATTACK AREA DEFAULTS
        this.attackArea = new Rectangle(x, y, width2, height2);
    }

//    RETURNS MAX HEALTH OF THE SPIRIT
    public int getMaxHealth() { // gets the spirit's max health
        return maxHealth;
    }

//    RETURNS CURRENT HEALTH
    public int getHealth() { // gets the spirit's current health
        return health;
    }

//    SETS HEALTH TO WHATEVER VALUE HAS BEEN INPUTTED
    public void setHealth(int health) { // sets the spirit's current health
        this.health = health;
    }

}
