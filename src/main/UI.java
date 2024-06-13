package main;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_Totem;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gp;
    Font arial_40; // not instantiated in game loop so that it doesn't run 60 times per second
    BufferedImage totemImage, heart_full, heart_half, heart_blank;
    public boolean messageOn = false; // whether there is a message displayed
    public boolean loadingMessageOn = true;
    public String message = "";
    int messageDisplayTime = 0; // keeps track of the amount of time that has elapsed since the message has appeared

    public UI (GamePanel gp) { // constructor
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        OBJ_Totem totem = new OBJ_Totem(gp);
        totemImage = totem.down1;

        // Display hearts on screen
        Entity heart = new OBJ_Heart(gp); // creates a heart object

        // sets the heart states to their respective images
        heart_full = heart.image1;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void showLoadingMessage (String text) {
        message = text;
        loadingMessageOn = true;
    }

    public void draw(Graphics2D g2) {
        drawSpiritsHealth(g2); // draws the spirits' health

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawImage(totemImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.numTotems, 160, 110);

        g2.setFont(g2.getFont().deriveFont(20F)); // changes the font size
        g2.setColor(new Color(135, 206, 235)); // light blue
        if (gp.keyH.displayControls) { // displays the controls if they should be displayed
            g2.fillRect(40, 730, 330, 250);
            g2.setColor(new Color(255, 255, 255)); // white
            g2.drawString("Controls:", 40, 760);
            g2.drawString("Up, Left, Down, Right -> W, A, S, D", 40, 790);
            g2.drawString("Primary Attack -> Left Click", 40, 820);
            g2.drawString("Special Attack -> Right Click", 40, 850);
            g2.drawString("Open/Close Map -> M", 40, 880);
            g2.drawString("Open/Close Mini Map -> Q", 40, 910);
            g2.drawString("Show Draw Time -> T", 40, 940);
            g2.drawString("Close Menu -> C", 40, 970);
        }
        else {
            g2.fillRect(40, 940, 230, 40);
            g2.setColor(new Color(255, 255, 255)); // white
            g2.drawString("Open Controls Menu -> C", 40, 970);
        }

        // message
        if (messageOn) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // resets the opacity
            g2.setFont(g2.getFont().deriveFont(30F)); // changes the font size
            g2.drawString(message, gp.tileSize / 2, gp.tileSize * 5);

            messageDisplayTime ++;

            if (messageDisplayTime > 120) { // makes the message disappear after 2 seconds
                messageDisplayTime = 0;
                messageOn = false;
            }
        }

        // loading message
        if (loadingMessageOn) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // resets the opacity
            g2.setFont(g2.getFont().deriveFont(30F)); // changes the font size
            g2.drawString(message, gp.tileSize / 2, gp.tileSize * 5);
        }
    }

    public void drawSpiritsHealth(Graphics2D g2) {

        // sets the coordinates of the spirit name in terms of the tile size
        int x;
        int y = gp.tileSize * 2;
        float fontSize; // font size of the name of the spirits
        float scale; // a scale to scale the images when drawing them

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // resets the opacity

        for (int i = 0; i < gp.player.spirits.length; i++) {
            if (gp.player.spirits[i] == gp.player.getCurrentSpirit()) {
                fontSize = 30F;
            }
            else {
                fontSize = 20F;
            }
            g2.setFont(g2.getFont().deriveFont(fontSize)); // changes the font size

            x = gp.tileSize;
            g2.drawString(gp.player.spirits[i].name, x, y);
            x += 150;
            y -= 60;

            // Adjusts the drawing of the spirits if the spirit is a turtle to account for the differences in its drawig
            if (gp.player.spirits[i].name.equals("Turtle")) {
                x -= 25;
                y -= 50;
            }

            // Get the original width and height of the image
            int originalWidth = gp.player.spirits[i].right1.getWidth();
            int originalHeight = gp.player.spirits[i].right1.getHeight();

            // Calculate scaled size
            if (gp.player.spirits[i] == gp.player.getCurrentSpirit()) {
                scale = 1F;
            }
            else {
                scale = 0.6F;
            }
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);

            // Draws the image at the scaled size
            g2.drawImage(gp.player.spirits[i].right1, x, y, scaledWidth, scaledHeight, null);
            y += 60;

            // Resets the coordinates after drawing the turtle
            if (gp.player.spirits[i].name.equals("Turtle")) {
                x += 25;
                y += 50;
            }

            x += 110;
            y -= 80;

            // Draws the hearts at the scaled size
            originalWidth = heart_blank.getWidth();
            originalHeight = heart_blank.getHeight();
            scaledWidth = (int) (originalWidth * scale);
            scaledHeight = (int) (originalHeight * scale);

            for (int j = 0; j < gp.player.spirits[i].getMaxHealth() / 2; j++) { // since 2 lives means 1 heart
                g2.drawImage(heart_blank, x, y, scaledWidth, scaledHeight, null);
                x += gp.tileSize * 0.9; // moves over to the right to draw the next heart
            }

            // resets the coordinates
            x = gp.tileSize + 260;

            for (int j = 0; j < gp.player.spirits[i].getHealth(); j++) {
                g2.drawImage(heart_half, x, y, scaledWidth, scaledHeight, null); // draws a half heart
                j++;
                if (j < gp.player.spirits[i].getHealth()) { // if the player's health still is not full, make the heart full
                    g2.drawImage(heart_full, x, y, scaledWidth, scaledHeight, null); // draws the full heart
                }
                x += gp.tileSize * 0.9; // moves over to the right to draw the next heart
            }
            if (gp.player.spirits[i] == gp.player.getCurrentSpirit()) {
                y += 170;
            }
            else {
                y += 140;
            }
        }
    }
}