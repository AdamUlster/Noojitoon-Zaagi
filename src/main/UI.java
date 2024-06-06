package main;

import object.OBJ_Heart;
import object.OBJ_Totem;
import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gp;
    Font arial_40; // not instantiated in game loop so that it doesn't run 60 times per second
    BufferedImage totemImage, heart_full, heart_half, heart_blank;
    public boolean messageOn = false; // whether there is a message displayed
    public String message = "";
    int messageDisplayTime = 0; // keeps track of the amount of time that has elapsed since the message has appeared

    public UI (GamePanel gp) { // constructor
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        OBJ_Totem totem = new OBJ_Totem();
        totemImage = totem.image1;

        // Display hearts on screen
        SuperObject heart = new OBJ_Heart(); // creates a heart object

        // sets the heart states to their respective images
        heart_full = heart.image1;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics g2) {
        drawPlayerHealth(g2); // draws the player health

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawImage(totemImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.numTotems, 160, 110);

        // message
        if (messageOn == true) {
            g2.setFont(g2.getFont().deriveFont(30F)); // changes the font size
            g2.drawString(message, gp.tileSize / 2, gp.tileSize * 5);

            messageDisplayTime ++;

            if (messageDisplayTime > 120) { // makes the message disappear after 2 seconds
                messageDisplayTime = 0;
                messageOn = false;
            }
        }
    }

    public void drawPlayerHealth(Graphics g2) {
        gp.player.health = 5;

        // sets the coordinates of first heart in terms of the tile size
        int x = gp.tileSize + 50;
        int y = gp.tileSize;

        // draws the blank hearts
        for (int i = 0; i < gp.player.maxHealth / 2; i++) { // since 2 lives means 1 heart
            g2.drawImage(heart_blank, x, y, null);
            x += gp.tileSize; // moves over to the right to draw the next heart
        }

        // resets the coordinates
        x = gp.tileSize + 50;
        y = gp.tileSize;
        for (int i = 0; i < gp.player.health; i++) {
            g2.drawImage(heart_half, x, y, null); // draws a half heart
            i++;
            if (i < gp.player.health) { // if the player's health still is not full, make the heart full
                g2.drawImage(heart_full, x, y, null); // draws the full heart
            }
            x += gp.tileSize; // moves over to the right to draw the next heart
        }
    }
}
