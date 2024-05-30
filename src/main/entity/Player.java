package main.entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gp;//call on the gamepanel class
    KeyHandler keyH;//call on keyhandler class

    public Player(GamePanel gp, KeyHandler keyH) {//creat default class
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();//sets default values for the player
        getPlayerImage();
    }

    public void setDefaultValues() {//create default values to spawn the player
        x = 100;//sets default x coordinate to 100
        y = 100;//sets defualt y coordinate to 100
        speed = 4;//sets speed to 4
        direction = "down";//can input any direction
    }

    public void getPlayerImage() {
        System.out.println("image loading started");
        try {
            //read through the res file and find the images
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/bear_up.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/bear_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/bear_down.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/bear_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/bear_left.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/bear_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/bear_right.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/bear_right_2.png"));

//            up1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_up.png"));
//            up2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_up_2.png"));
//            down1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_down.png"));
//            down2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_down_2.png"));
//            left1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_left.png"));
//            left2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_left_2.png"));
//            right1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other things\\my2dGame\\res\\player\\bear_right.png"));
//            right2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("G:\\My Drive\\ICS and other " +
//                                                                                         "things\\my2dGame\\res\\player\\bear_right_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Image loading complete");
    }

    public void update() {
        if (keyH.upPressed == true) {//move up
            direction = "up";
            y -= speed;
        } else if (keyH.downPressed == true) {//move down
            direction = "down";
            y += speed;
        } else if (keyH.leftPressed == true) {//move left
            //remove the else portion to make x and y movements independant
            direction = "left";
            x -= speed;
        } else if (keyH.rightPressed == true) {//move right
            direction = "right";
            x += speed;
        }
    }

    public void draw(Graphics2D g2) {
        //g2.setColor(Color.white);//set colour of the painter to white
        //
        //g2.fillRect(x, y, gp.tileSize, gp.tileSize);//create and fill something

        BufferedImage image = null;
        switch(direction) {//check the direction, based on the direction it picks a different image
            case "up":
                image = up1;
                break;
            case "down":
                image = down1;
                break;
            case "left":
                image = left1;
                break;
            case "right":
                image = right1;
                break;
        }
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize,null);//draws the image, null means we cannot type


    }
}
