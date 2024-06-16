package entity;

import main.GamePanel;

import java.awt.*;

//SPIRIT SUBCLASS WITHIN ENTITY
public class Spirit extends Entity {

//    DEFAULT VALUES (CONSTRUCTOR)
    public Spirit(GamePanel gp, String name, int maxHealth, int health, int x, int y, int width, int height, int width2, int height2, int attack, int defense) {
        super(gp);
        this.setName(name); // sets the spirit's name to the value passed to it
        this.setMaxHealth(maxHealth);
        this.setHealth(health);
        this.setAttack(attack);
        this.setDefense(defense);

        // GIVES EACH SPRITE ITS OWN IMAGE
        if (name.equals("Bear")) {
            setUp1(setup("bear/bear_up", 1, 1));
            setUp2(setup("bear/bear_up_2", 1, 1));
            setDown1(setup("bear/bear_down", 1, 1));
            setDown2(setup("bear/bear_down_2", 1, 1));
            setLeft1(setup("bear/bear_left", 1, 1));
            setLeft2(setup("bear/bear_left_2", 1, 1));
            setRight1(setup("bear/bear_right", 1, 1));
            setRight2(setup("bear/bear_right_2", 1, 1));
        }
        else if (name.equals("Eagle")) {
            setUp1(setup("eagle/eagle_up", 1, 1));
            setUp2(setup("eagle/eagle_up_2", 1, 1));
            setDown1(setup("eagle/eagle_down", 1, 1));
            setDown2(setup("eagle/eagle_down_2", 1, 1));
            setLeft1(setup("eagle/eagle_left", 1, 1));
            setLeft2(setup("eagle/eagle_left_2", 1, 1));
            setRight1(setup("eagle/eagle_right", 1, 1));
            setRight2(setup("eagle/eagle_right_2", 1, 1));
        }
        else {
            setUp1(setup("turtle/turtle_up", 1.8, 1.8));
            setUp2(setup("turtle/turtle_up_2", 1.8, 1.8));
            setDown1(setup("turtle/turtle_down", 1.8, 1.8));
            setDown2(setup("turtle/turtle_down_2", 1.8, 1.8));
            setLeft1(setup("turtle/turtle_left", 1.8, 1.8));
            setLeft2(setup("turtle/turtle_left_2", 1.8, 1.8));
            setRight1(setup("turtle/turtle_right", 1.8, 1.8));
            setRight2(setup("turtle/turtle_right_2", 1.8, 1.8));
        }

        //COLLISION BOX
        this.solidArea = new Rectangle(x, y, width, height);

        // ATTACK AREA DEFAULTS
        this.attackArea = new Rectangle(x, y, width2, height2);
    }

    public void setHitBox (int width, int height) {
        setSolidArea(new Rectangle(8, 16, width, height));

        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);
        System.out.println("hitbox established");
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
