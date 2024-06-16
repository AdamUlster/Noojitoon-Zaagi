package main;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_Totem;

import java.awt.*;
import java.awt.image.BufferedImage;

//USER INTERFACE CLASS
public class UI {
    GamePanel gp;
//    FONT NOT INSTANTIATED IN GAME LOOP SO THAT IT DOESN'T RUN 60 TIMES PER SECOND
    Font arial_40;
//    CREATE EMPTY BUFFERED IMAGES FOR TOTEM IMAGE, AND HEART
    BufferedImage totemImage, heart_full, heart_half, heart_blank;
    public boolean messageOn = false; // WHETHER THERE IS A MESSAGE DISPLAYED
    public boolean loadingMessageOn = true; // LOADING SPRITES MESSAGE
    public boolean respawningMessageOn = false; // RESPAWN MESSAGE
    public boolean collectionMessageOn = false; // TOTEM COLLECTION MESSAGE
    public boolean completionMessageOn = false; // GAME OVER MESSAGE
    public String message = "";// STRING THAT WILL BE INPUTTED INTO MESSAGE METHODS

//    VARIABLE TO KEEP TRACK OF THE AMOUNT OF TIME THAT HAS ELAPSED SINCE THE MESSAGE HAS APPEARED
    int messageDisplayTime = 0;

//    DISPLAYS A COUNTDOWN UP TO 3 BEFORE RESPAWNING THE PKAYER
    public int respawningMessageDisplayTime = 0; // counts to 3 and then, the player respawns

//    CONSTRUCTOR METHOD
    public UI (GamePanel gp) {
        this.gp = gp;// CALL ON GAME PANEL CLASS

//        SET FONT
        arial_40 = new Font("Arial", Font.PLAIN, 40);

//        CREATE TOTEM IMAGE
        OBJ_Totem totem = new OBJ_Totem(gp);
        totemImage = totem.down1;// SET IMAGE DIRECTION THAT WILL BE DISPLAYED TO 1

//        DISPLAY HEARTS ON SCREEN
        Entity heart = new OBJ_Heart(gp); // CREATE A HEART OBJECT

//        SETS HEART STATES TO THEIR RESPECTIVE IMAGES
        heart_full = heart.image1;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

//    SHOW MESSAGE WITH THE TEXT STRING AS INPUT
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

//    SHOW LOADING MESSAGE
    public void showLoadingMessage(String text) {
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
        }
        else {
            g2.setColor(Color.white);//OTHERWISE THE FONT COLOUR IS BLACK
        }
        g2.setFont(arial_40);
        g2.setColor(Color.white);

//        DRAW THE TOTEM IMAGE IN THE TOP RIGHT AND THE NUMBER OF TOTEMS
         g2.drawImage(totemImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.numTotems, 160, 110);

        g2.setFont(g2.getFont().deriveFont(20F)); //CHANGE FONT SIZE AND CHANGE TO LIGHT BLUE
        g2.setColor(new Color(135, 206, 235));

//        DISPLAYS CONTROLS IF C HAS BEEN TOGGLED ON
        if (gp.keyH.displayControls) {
            g2.fillRect(40, 700, 330, 280);
            g2.setColor(new Color(255, 255, 255)); // white
            g2.drawString("Controls:", 40, 730);
            g2.drawString("Up, Left, Down, Right -> W, A, S, D", 40, 760);
            g2.drawString("Primary Attack (Once Unlocked) -> Left Click", 40, 790);
            g2.drawString("Special Attack -> Right Click", 40, 820);
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
            g2.setFont(g2.getFont().deriveFont(30F)); // CHANGE FOTN SIZE
            g2.drawString(message, gp.tileSize / 2, gp.tileSize * 5);

            messageDisplayTime ++;//ADDS TO COUNTER FOR DISPLAY DURATION

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
            g2.drawString(message, gp.tileSize / 2, gp.tileSize * 5);
        }

        // TOTEM COLLECTION MESSAGE
        if (collectionMessageOn) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2.setFont(g2.getFont().deriveFont(30F));
            g2.drawString(message, gp.tileSize / 2, gp.tileSize * 5);

            messageDisplayTime ++;

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
            g2.drawString("Respawning in", gp.screenWidth / 2 - 115, gp.screenHeight / 2 - 150);
            if (countdown <= 3) {
                g2.drawString(message, gp.screenWidth / 2, gp.screenHeight / 2 - 65);
            }
        }

//        IF USER BEATS THE GAME
        if (completionMessageOn) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2.setFont(g2.getFont().deriveFont(30F));
            g2.setColor(Color.MAGENTA); // TEXT BOX COLOUR
            g2.fillRect(gp.tileSize / 2, (int) (gp.tileSize * 4.5), 950, 350);
            g2.setColor(Color.black); // TEXT COLOUR
            g2.drawString("Congratulations!", gp.tileSize / 2, gp.tileSize * 5);
            g2.drawString("By collecting all four totems, you have just reunited the spirit animals!", gp.tileSize / 2, gp.tileSize * 6);
            g2.drawString("The community is much safer with you in it! Good work!", gp.tileSize / 2, gp.tileSize * 7);
            g2.drawString("Thank you for playing!", gp.tileSize / 2, gp.tileSize * 8);
            System.exit(0); // EXITS THE PROGRAM
        }
    }

//    DRAWS A SPIRIT IMAGE, AND THEN THE SPIRITS HEALTH
    public void drawSpiritsHealth(Graphics2D g2) {

//        SETS THE COORDINATES OF THE SPIRIT NAME IN TERMS OF TILE SIZE
        int x;
        int y = gp.tileSize * 2;
        float fontSize;
        float scale; // SCALES THE SPIRIT IMAGES

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

//        ITERATES THROUGH EACH SPIRIT, AND DRAWS THEIR INDICATING IMAGE AND CURRENT HEALTH USING HEART IMAGES
        for (int i = 0; i < gp.player.spirits.length; i++) {
            if (gp.player.spirits[i] == gp.player.getCurrentSpirit()) {
                fontSize = 30F;
            }
            else {
                fontSize = 20F;
            }
            g2.setFont(g2.getFont().deriveFont(fontSize));

//            CALCULATE COORDINATES TO PUT THE SPIRIT HEALTH
            x = gp.tileSize;
            g2.drawString(gp.player.spirits[i].name, x, y);
            x += 150;
            y -= 60;

//            ADJUST THE DRAWING OF THE TURTLE SPIRIT TO ACCOUNT FOR THE DIFFERENCE IN ITS IMAGE PNG SIZE
            if (gp.player.spirits[i].name.equals("Turtle")) {
                x -= 25;
                y -= 50;
            }

//            GET THE ORIGINAL WIDTH AND HEIGHT OF THE IMAGE
            int originalWidth = gp.player.spirits[i].right1.getWidth();
            int originalHeight = gp.player.spirits[i].right1.getHeight();

//            CALCULATE SCALED SIZE
            if (gp.player.spirits[i] == gp.player.getCurrentSpirit()) {
                scale = 1F;
            }
            else {
                scale = 0.6F;
            }
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);

//            DRAWS THE IMAGE AT THE SCALED SIZE
            g2.drawImage(gp.player.spirits[i].right1, x, y, scaledWidth, scaledHeight, null);
            y += 60;

//            RESETS THE COORDINATES AFTER DRAWING THE TURTLE
            if (gp.player.spirits[i].name.equals("Turtle")) {
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

            for (int j = 0; j < gp.player.spirits[i].getMaxHealth() / 2; j++) { // SINCE TWO HP MEANS ONE HEART
                g2.drawImage(heart_blank, x, y, scaledWidth, scaledHeight, null);
                x += gp.tileSize * 0.9; // MOVES OVER TO THE RIGHT TO DRAW THE NEXT HEART
            }

//            RESET COORDINATES
            x = gp.tileSize + 260;

            for (int j = 0; j < gp.player.spirits[i].getHealth(); j++) {
                g2.drawImage(heart_half, x, y, scaledWidth, scaledHeight, null); // DRAWS A HALF HEART
                j++;
//                IF PLAYER'S HEALTH STILL IS NOT FULL, MAKE THE HEART FULL
                if (j < gp.player.spirits[i].getHealth()) {
                    g2.drawImage(heart_full, x, y, scaledWidth, scaledHeight, null); // DRAWS THE FULL HEART
                }
//                MOVES OVER TO THE RIGHT TO DRAW THE NEXT HEART
                x += gp.tileSize * 0.9; //
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