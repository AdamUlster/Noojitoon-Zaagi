package main;

import object.OBJ_Totem;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gp;
    Font arial_40; // not instantiated in game loop so that it doesn't run 60 times per second
    BufferedImage totemImage; // not inside the draw method for the same reason
    public boolean messageOn = false; // whether there is a message displayed
    public String message = "";
    int messageDisplayTime = 0; // keeps track of the amount of time that has elapsed since the message has appeared

    public UI (GamePanel gp) { // constructor
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        OBJ_Totem totem = new OBJ_Totem();
        totemImage = totem.image;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics g2) {
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
}
