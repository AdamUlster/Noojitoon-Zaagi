package main;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_Totem;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    private GamePanel gp;
    private Font arial_40; // not instantiated in game loop so that it doesn't run 60 times per second
    private BufferedImage totemImage, heart_full, heart_half, heart_blank;
    private boolean messageOn = false; // whether there is a message displayed
    private boolean loadingMessageOn = true; // loading sprites message
    private boolean respawningMessageOn = false; // respawning message
    private boolean collectionMessageOn = false; // totem collection message
    private boolean completionMessageOn = false; // game over message
    private String message = "";
    private int messageDisplayTime = 0; // keeps track of the amount of time that has elapsed since the message has appeared
    private int respawningMessageDisplayTime = 0; // counts to 3 and then, the player respawns

    public UI (GamePanel gp) { // constructor
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        OBJ_Totem totem = new OBJ_Totem(gp);
        totemImage = totem.getDown1();

        // Display hearts on screen
        Entity heart = new OBJ_Heart(gp); // creates a heart object

        // sets the heart states to their respective images
        heart_full = heart.getImage1();
        heart_half = heart.getImage2();
        heart_blank = heart.getImage3();
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    void showLoadingMessage(String text) {
        message = text;
        loadingMessageOn = true;
    }

    public void showCollectionMessage(String text) {
        message = text;
        collectionMessageOn = true;
    }

    public void showRespawningMessage() {
        respawningMessageOn = true;
    }

    public void draw(Graphics2D g2) {
        drawSpiritsHealth(g2); // draws the spirits' health

        if (message.equals("There are no monsters for the eagle eye to lock onto")) { // make this font colour black
            g2.setColor(Color.black);
        }
        else {
            g2.setColor(Color.white);
        }
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawImage(totemImage, gp.getTileSize() / 2, gp.getTileSize() / 2, gp.getTileSize(), gp.getTileSize(), null);
        g2.drawString("x " + gp.getPlayer().getNumTotems(), 160, 110);

        g2.setFont(g2.getFont().deriveFont(20F)); // changes the font size
        g2.setColor(new Color(135, 206, 235)); // light blue
        if (gp.getKeyH().isDisplayControls()) { // displays the controls if they should be displayed
            g2.fillRect(40, 700, 405, 280);
            g2.setColor(new Color(255, 255, 255)); // white
            g2.drawString("Controls:", 40, 730);
            g2.drawString("Up, Left, Down, Right -> W, A, S, D", 40, 760);
            g2.drawString("Primary Attack -> Left Click", 40, 790);
            g2.drawString("Special Attack (Once Unlocked) -> Right Click", 40, 820);
            g2.drawString("Hint -> H", 40, 850);
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

        // Tells the user the goal of the game
        g2.setFont(g2.getFont().deriveFont(20F)); // changes the font size
        g2.setColor(new Color(135, 206, 235)); // light blue
        g2.fillRect(540, 940, 850, 40);
        g2.setColor(new Color(255, 255, 255)); // white
        g2.drawString("Goal: Collect all of the totems to reunite the Indigenous spirit animals and protect the community", 540, 970);

        // message
        if (messageOn) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // resets the opacity
            g2.setFont(g2.getFont().deriveFont(30F)); // changes the font size
            g2.drawString(message, gp.getTileSize() / 2, gp.getTileSize() * 5);

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
            g2.drawString(message, gp.getTileSize() / 2, gp.getTileSize() * 5);
        }

        // totem collection message
        if (collectionMessageOn) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // resets the opacity
            g2.setFont(g2.getFont().deriveFont(30F)); // changes the font size
            g2.drawString(message, gp.getTileSize() / 2, gp.getTileSize() * 5);

            messageDisplayTime ++;

            if (messageDisplayTime > 240) { // makes the message disappear after 2 seconds
                messageDisplayTime = 0;
                collectionMessageOn = false;
            }
        }

        // respawning message
        if (respawningMessageOn) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // resets the opacity
            g2.setFont(g2.getFont().deriveFont(50F)); // changes the font size

            // Calculate the countdown number based on the time
            int countdown = 4 - respawningMessageDisplayTime / 60; // Countdown from 3 to 0

            // Display the countdown message
            String message = Integer.toString(countdown); // Convert countdown number to string
            g2.drawString("Respawning in", gp.getScreenWidth() / 2 - 115, gp.getScreenHeight() / 2 - 150);
            if (countdown <= 3) {
                g2.drawString(message, gp.getScreenWidth() / 2, gp.getScreenHeight() / 2 - 65);
            }
            if (countdown == 1) { // displays the loading message on the last part of the countdown
                g2.setFont(g2.getFont().deriveFont(30F)); // changes the font size
                g2.drawString("Loading... Please Wait", gp.getTileSize() / 2, gp.getTileSize() * 5);
            }
        }

        // if the user beats the game
        if (completionMessageOn) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // resets the opacity
            g2.setFont(g2.getFont().deriveFont(30F)); // changes the font size
            g2.setColor(Color.MAGENTA); // text box colour
            g2.fillRect(gp.getTileSize() / 2, (int) (gp.getTileSize() * 4.5), 950, 350);
            g2.setColor(Color.black); // text colour
            g2.drawString("Congratulations!", gp.getTileSize() / 2, gp.getTileSize() * 5);
            g2.drawString("By collecting all four totems, you have just reunited the spirit animals!", gp.getTileSize() / 2, gp.getTileSize() * 6);
            g2.drawString("The community is much safer with you in it! Good work!", gp.getTileSize() / 2, gp.getTileSize() * 7);
            g2.drawString("Thank you for playing!", gp.getTileSize() / 2, gp.getTileSize() * 8);
            System.exit(0); // Exits the program
        }
    }

    private void drawSpiritsHealth(Graphics2D g2) {

        // sets the coordinates of the spirit name in terms of the tile size
        int x;
        int y = gp.getTileSize() * 2;
        float fontSize; // font size of the name of the spirits
        float scale; // a scale to scale the images when drawing them

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // resets the opacity

        for (int i = 0; i < gp.getPlayer().getSpirits().length; i++) {
            if (gp.getPlayer().getSpirits()[i] == gp.getPlayer().getCurrentSpirit()) {
                fontSize = 30F;
            }
            else {
                fontSize = 20F;
            }
            g2.setFont(g2.getFont().deriveFont(fontSize)); // changes the font size

            x = gp.getTileSize();
            g2.drawString(gp.getPlayer().getSpirits()[i].getName(), x, y);
            x += 150;
            y -= 60;

            // Adjusts the drawing of the spirits if the spirit is a turtle to account for the differences in its drawing
            if (gp.getPlayer().getSpirits()[i].getName().equals("Turtle")) {
                x -= 25;
                y -= 50;
            }

            // Get the original width and height of the image
            int originalWidth = gp.getPlayer().getSpirits()[i].getRight1().getWidth();
            int originalHeight = gp.getPlayer().getSpirits()[i].getRight1().getHeight();

            // Calculate scaled size
            if (gp.getPlayer().getSpirits()[i] == gp.getPlayer().getCurrentSpirit()) {
                scale = 1F;
            }
            else {
                scale = 0.6F;
            }
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);

            // Draws the image at the scaled size
            g2.drawImage(gp.getPlayer().getSpirits()[i].getRight1(), x, y, scaledWidth, scaledHeight, null);
            y += 60;

            // Resets the coordinates after drawing the turtle
            if (gp.getPlayer().getSpirits()[i].getName().equals("Turtle")) {
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

            for (int j = 0; j < gp.getPlayer().getSpirits()[i].getMaxHealth() / 2; j++) { // since 2 lives means 1 heart
                g2.drawImage(heart_blank, x, y, scaledWidth, scaledHeight, null);
                x += gp.getTileSize() * 0.5; // moves over to the right to draw the next heart
            }

            // resets the coordinates
            x = gp.getTileSize() + 260;

            for (int j = 0; j < gp.getPlayer().getSpirits()[i].getHealth(); j++) {
                g2.drawImage(heart_half, x, y, scaledWidth, scaledHeight, null); // draws a half heart
                j++;
                if (j < gp.getPlayer().getSpirits()[i].getHealth()) { // if the player's health still is not full, make the heart full
                    g2.drawImage(heart_full, x, y, scaledWidth, scaledHeight, null); // draws the full heart
                }
                x += gp.getTileSize() * 0.5; // moves over to the right to draw the next heart
            }
            if (gp.getPlayer().getSpirits()[i] == gp.getPlayer().getCurrentSpirit()) {
                y += 170;
            }
            else {
                y += 140;
            }
        }
    }

    // Get and set methods
    public boolean isMessageOn() {
        return messageOn;
    }

    public void setMessageOn(boolean messageOn) {
        this.messageOn = messageOn;
    }

    public boolean isLoadingMessageOn() {
        return loadingMessageOn;
    }

    public void setLoadingMessageOn(boolean loadingMessageOn) {
        this.loadingMessageOn = loadingMessageOn;
    }

    public boolean isRespawningMessageOn() {
        return respawningMessageOn;
    }

    public void setRespawningMessageOn(boolean respawningMessageOn) {
        this.respawningMessageOn = respawningMessageOn;
    }

    public boolean isCollectionMessageOn() {
        return collectionMessageOn;
    }

    public void setCollectionMessageOn(boolean collectionMessageOn) {
        this.collectionMessageOn = collectionMessageOn;
    }

    public boolean isCompletionMessageOn() {
        return completionMessageOn;
    }

    public void setCompletionMessageOn(boolean completionMessageOn) {
        this.completionMessageOn = completionMessageOn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageDisplayTime() {
        return messageDisplayTime;
    }

    public void setMessageDisplayTime(int messageDisplayTime) {
        this.messageDisplayTime = messageDisplayTime;
    }

    public int getRespawningMessageDisplayTime() {
        return respawningMessageDisplayTime;
    }

    public void setRespawningMessageDisplayTime(int respawningMessageDisplayTime) {
        this.respawningMessageDisplayTime = respawningMessageDisplayTime;
    }
}