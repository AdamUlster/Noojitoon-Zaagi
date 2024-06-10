package entity;

import main.GamePanel;

import java.awt.*;

public class Spirit extends Entity {
    public Spirit(GamePanel gp, String name, int maxHealth, int health, int x, int y, int width, int height) {
        super(gp);
        this.name = name; // sets the spirit's name to the value passed to it
        this.maxHealth = maxHealth;
        this.health = health;

        // gives each spirit its own image
        if (name.equals("Bear")) {
            up1 = setup("bear/bear_up", 1);
            up2 = setup("bear/bear_up_2", 1);
            down1 = setup("bear/bear_down", 1);
            down2 = setup("bear/bear_down_2", 1);
            left1 = setup("bear/bear_left", 1);
            left2 = setup("bear/bear_left_2", 1);
            right1 = setup("bear/bear_right", 1);
            right2 = setup("bear/bear_right_2", 1);
        }
        else if (name.equals("Eagle")) {
            up1 = setup("eagle/eagle_up", 1);
            up2 = setup("eagle/eagle_up_2", 1);
            down1 = setup("eagle/eagle_down", 1);
            down2 = setup("eagle/eagle_down_2", 1);
            left1 = setup("eagle/eagle_left", 1);
            left2 = setup("eagle/eagle_left_2", 1);
            right1 = setup("eagle/eagle_right", 1);
            right2 = setup("eagle/eagle_right_2", 1);
        }
        else {
            up1 = setup("turtle/turtle_up", 1.8);
            up2 = setup("turtle/turtle_up_2", 1.8);
            down1 = setup("turtle/turtle_down", 1.8);
            down2 = setup("turtle/turtle_down_2", 1.8);
            left1 = setup("turtle/turtle_left", 1.8);
            left2 = setup("turtle/turtle_left_2", 1.8);
            right1 = setup("turtle/turtle_right", 1.8);
            right2 = setup("turtle/turtle_right_2", 1.8);
        }

        // Collision box
        this.solidArea = new Rectangle(x, y, width, height);
    }

    public int getMaxHealth() { // gets the spirit's max health
        return maxHealth;
    }

    public int getHealth() { // gets the spirit's current health
        return health;
    }

    public void setHealth(int health) { // sets the spirit's current health
        this.health = health;
    }

    public void setHitBox (int width, int height) {
        solidArea = new Rectangle(8, 16, width, height);

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        System.out.println("hitbox established");
    }

}
