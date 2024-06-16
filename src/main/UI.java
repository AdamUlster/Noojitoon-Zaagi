package main;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_Totem;

import java.awt.*;
import java.awt.image.BufferedImage;

//USER INTERFACE CLASS
public class UI {
    private GamePanel gp;
    private Font arial_40; //FONT NOT INSTANTIATED IN GAME LOOP SO THAT IT DOESN'T RUN 60 TIMES PER SECOND
  
 //    CREATE EMPTY BUFFERED IMAGES FOR TOTEM IMAGE, AND HEART
    private BufferedImage totemImage, heart_full, heart_half, heart_blank;
    private boolean messageOn = false; // WHETHER THERE IS A MESSAGE DISPLAYED
    private boolean loadingMessageOn = true; // LOADING SPRITES MESSAGE
    private boolean respawningMessageOn = false; // RESPAWN MESSAGE
    private boolean collectionMessageOn = false; // TOTEM COLLECTION MESSAGE
    private boolean completionMessageOn = false;// GAME OVER MESSAGE
    private String message = "";// STRING THAT WILL BE INPUTTED INTO MESSAGE METHODS
  
//    VARIABLE TO KEEP TRACK OF THE AMOUNT OF TIME THAT HAS ELAPSED SINCE THE MESSAGE HAS APPEARED
    private int messageDisplayTime = 0; 
  
    //    DISPLAYS A COUNTDOWN UP TO 3 BEFORE RESPAWNING THE PLAYER
    private int respawningMessageDisplayTime = 0; // counts to 3 and then, the player respawns


    //    CONSTRUCTOR METHOD
    public UI(GamePanel gp) {
        this.gp = gp;// CALL ON GAME PANEL CLASS

//        SET FONT
        arial_40 = new Font("Arial", Font.PLAIN, 40);

//        CREATE TOTEM IMAGE
        OBJ_Totem totem = new OBJ_Totem(gp);
        totemImage = totem.getDown1();

//        DISPLAY HEARTS ON SCREEN
        Entity heart = new OBJ_Heart(gp); // CREATE A HEART OBJECT

        // sets the heart states to their respective images
        heart_full = heart.getImage1();
        heart_half = heart.getImage2();
        heart_blank = heart.getImage3();
    }

    //    SHOW MESSAGE WITH THE TEXT STRING AS INPUT
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }


    void showLoadingMessage(String text) {
        message = text;
        loadingMessageOn = true;
    }

    //    SHOW COLLECTION FOR COLLECTION OF TOTEM
    public void showCollectionMessage(String text) {
        message = text;
        collectionMessageOn = true;
    }

    //    SHOW RESPAWNING MESSAGE
    public void showRespawningMessage() {
        respawningMessageOn = true;
    }

    //    DRAW METHOD
    public void draw(Graphics2D g2) {
        drawSpiritsHealth(g2); // draws the spirits' health

//        CHECK IF MESSAGE BELONGS TO TARGETING PROJECTILE, MAKE THE FONT COLOUR BLACK
        if (message.equals("There are no monsters for the eagle eye to lock onto")) {
            g2.setColor(Color.black);
        } else {
            g2.setColor(Color.white);//OTHERWISE THE FONT COLOUR IS BLACK
        }
        g2.setFont(arial_40);
        g2.setColor(Color.white);

//        DRAW THE TOTEM IMAGE IN THE TOP RIGHT AND THE NUMBER OF TOTEMS
        g2.drawImage(totemImage, gp.getTileSize() / 2, gp.getTileSize() / 2, gp.getTileSize(), gp.getTileSize(), null);
        g2.drawString("x " + gp.getPlayer().getNumTotems(), 160, 110);
//        DISPLAYS CONTROLS IF C HAS BEEN TOGGLED ON
        if (gp.keyH.displayControls) {
            g2.fillRect(40, 700, 330, 280);
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
        } else {//OTHERWISE SHOWS ON THE BOTTOM LEFT THE USER PROMPT TO SHOW CONTROLS
            g2.fillRect(40, 940, 230, 40);
            g2.setColor(new Color(255, 255, 255)); // white
            g2.drawString("Open Controls Menu -> C", 40, 970);
        }


//        TELLS USER THE GOAL OF THE GAME
            g2.setFont(g2.getFont().deriveFont(20F)); // changes the font size
            g2.setColor(new Color(135, 206, 235)); // light blue
            g2.fillRect(540, 940, 850, 40);
            g2.setColor(new Color(255, 255, 255)); // white
            g2.drawString("Goal: Collect all of the totems to reunite the Indigenous spirit animals and protect the community", 540, 970);

        // MESSAGE
        if (messageOn) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // RESETS THE OPACITY
            g2.setFont(g2.getFont().deriveFont(30F));  // CHANGE FONT SIZE
            g2.drawString(message, gp.getTileSize() / 2, gp.getTileSize() * 5);

                messageDisplayTime++;//ADDS TO COUNTER FOR DISPLAY DURATION

//            MESSAGE DISAPPEARS AFTER 2 SECONDS
                if (messageDisplayTime > 120) {
                    messageDisplayTime = 0;
                    messageOn = false;
                }
            }

        // LOADING MESSAGE
        if (loadingMessageOn) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); 
            g2.setFont(g2.getFont().deriveFont(30F)); 
            g2.drawString(message, gp.getTileSize() / 2, gp.getTileSize() * 5);
        }

        // TOTEM COLLECTION MESSAGE
        if (collectionMessageOn) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2.setFont(g2.getFont().deriveFont(30F));
            g2.drawString(message, gp.getTileSize() / 2, gp.getTileSize() * 5);

                messageDisplayTime++;

//            MESSAGE DISAPPEARS AFTER 2 SECONDS
                if (messageDisplayTime > 240) {
                    collectionMessageOn = false;
                }
            }

            // RESPAWNING MESSAGE
            if (respawningMessageOn) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                g2.setFont(g2.getFont().deriveFont(50F));

//            CALCULATE THE COUNTDOWN NUMBER BASED ON THE TIME
                int countdown = 4 - respawningMessageDisplayTime / 60; // COUNTDOWN FROM 3 TO 0


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

//        IF USER BEATS THE GAME
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

//        SETS THE COORDINATES OF THE SPIRIT NAME IN TERMS OF TILE SIZE
        int x;
        int y = gp.getTileSize() * 2;
        float fontSize; // font size of the name of the spirits
        float scale; // a scale to scale the images when drawing them

            g2.setFont(arial_40);
            g2.setColor(Color.white);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        for (int i = 0; i < gp.getPlayer().getSpirits().length; i++) {
            if (gp.getPlayer().getSpirits()[i] == gp.getPlayer().getCurrentSpirit()) {
                fontSize = 30F;
            }
            else {
                fontSize = 20F;
            }
            g2.setFont(g2.getFont().deriveFont(fontSize));

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

//            DRAWS THE HEARTS AT THE SCALED SIZE
            originalWidth = heart_blank.getWidth();
            originalHeight = heart_blank.getHeight();
            scaledWidth = (int) (originalWidth * scale);
            scaledHeight = (int) (originalHeight * scale);

            for (int j = 0; j < gp.getPlayer().getSpirits()[i].getMaxHealth() / 2; j++) { // since 2 lives means 1 heart
                g2.drawImage(heart_blank, x, y, scaledWidth, scaledHeight, null);
                x += gp.getTileSize() * 0.9; // moves over to the right to draw the next heart
            }

            // resets the coordinates
            x = gp.getTileSize() + 260;

            for (int j = 0; j < gp.getPlayer().getSpirits()[i].getHealth(); j++) {
                g2.drawImage(heart_half, x, y, scaledWidth, scaledHeight, null); // draws a half heart
                j++;
                if (j < gp.getPlayer().getSpirits()[i].getHealth()) { // if the player's health still is not full, make the heart full
                    g2.drawImage(heart_full, x, y, scaledWidth, scaledHeight, null); // draws the full heart
                }
                x += gp.getTileSize() * 0.9; // moves over to the right to draw the next heart
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

