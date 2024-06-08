package entity;

import main.GamePanel;

public class Spirit extends Entity {
    public Spirit(GamePanel gp, String name) {
        super(gp);
        this.name = name; // sets the spirit's name to the value passed to it
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

}
