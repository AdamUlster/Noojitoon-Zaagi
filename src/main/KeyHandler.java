package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//HANDLE KEYBOARD INPUTS
public class KeyHandler implements KeyListener, MouseListener {

    public GamePanel gp;//IMPORT GAME PANEL

//    BOOLEAN VALUES THAT DETERMINE WHICH KEYS ARE PRESSED AND WHICH ARE NOT
    public boolean upPressed, downPressed, leftPressed, rightPressed, shotKeyPressed;

//    BOOLEAN VALUES THAT DETERMINE WHICH NUMBER KEYS HAVE BEEN PRESSED, USED FOR SPRITE SWITCHING
    public boolean onePressed, twoPressed, threePressed;//boolean values that determine which number keys have been

//    BOOLEAN VALUES THAT DETERMINE LEFT CLICK OR RIGHT CLICK
    public boolean primaryPressed, secondaryPressed;

    //DEBUG STUFF
    public boolean checkDrawTime = false;
    public boolean displayControls = false;
    public boolean displayMap = false;

    public KeyHandler(GamePanel gp) { // constructor
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
//        RETURNS THE KEYCODE OF THE KEY THAT HAS BEEN PRESSED
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {//W KEY HAS BEEN PRESSED
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {// S KEY HAS BEEN PRESSED
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {// A KEY HAS BEEN PRESSED
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {//D KEY HAD BEEN PRESSED
            rightPressed = true;
        }
//        RUNS IF THE PLAYER IS NOT DYING
        if (!gp.player.isDying) {
            if (code == KeyEvent.VK_1 && !gp.player.spirits[0].dead) {// IF 1 KEY HAS BEEN PRESSED AND BEAR IS NOT DEAD
                onePressed = true;
//                IF SPIRIT IS DEAD DISPLAY DEATH MESSAGE AND PREVENT PLAYER FROM SWITCHING BACK
            } else if (code == KeyEvent.VK_1 && gp.player.spirits[0].dead && gp.player.displayDeathMessage) {
                gp.ui.showMessage("You cannot switch to the bear since the bear is dead");
            }

//            SWITCH TO EAGLE
            if (code == KeyEvent.VK_2 && !gp.player.spirits[1].dead) {
                twoPressed = true;
            } else if (code == KeyEvent.VK_2 && gp.player.spirits[1].dead && gp.player.displayDeathMessage) {
                gp.ui.showMessage("You cannot switch to the eagle since the eagle is dead");
            }

//            SWITCH TO TURTLE
            if (code == KeyEvent.VK_3 && !gp.player.spirits[2].dead) {
                threePressed = true;
            } else if (code == KeyEvent.VK_3 && gp.player.spirits[2].dead && gp.player.displayDeathMessage) {
                gp.ui.showMessage("You cannot switch to the turtle since the turtle is dead");
            }
        }

        //DEBUGGING STUFF
//        PRINTS DRAW TIME IF T KEY HAS BEEN PRESSED
        if (code == KeyEvent.VK_T) {
            checkDrawTime = !checkDrawTime;
        }
//        DISPLAY CONTROLS IF THEY WERE NOT DISPLAYED PREVIOUSLY
//       DISPLAYS THE CONTROLS IF THEY WERE NOT DISPLAYED PREVIOUSLY
        if (code == KeyEvent.VK_C) {
//            DISPLAY CONTROLS ARE NOW TOGGLEABLE
            displayControls = !displayControls;
        }
        if (code == KeyEvent.VK_M) {
//            DISPLAYS THE MAP IF IT WASN'T ALREADY, TOGGLEABLE
            displayMap = !displayMap;
        }
        if (code == KeyEvent.VK_Q) {
//            TOGGLEABLE DISPLAY MINI MAP
            gp.map.miniMapOn = !gp.map.miniMapOn;
        }
        if (code == KeyEvent.VK_H) {
            //TOGGLEABLE DRAW PATH TO THE MAZE IF IT WASN'T ALREADY SHOWN
            gp.player.onPath = !gp.player.onPath;
            gp.tileM.drawPath = !gp.tileM.drawPath;
        }
    }

    @Override
    public void keyReleased (KeyEvent e){

//        RETURNS THE KEYCODE OF A KEY THAT HAS BEN RELEASED
        int code = e.getKeyCode();

//        RESETS THE KEYKEYPRESSED BOOLEANS WHEN THE KEYS ARE RELEASED
        if (code == KeyEvent.VK_W) {// W KEY
            upPressed = false;
        }if (code == KeyEvent.VK_S) {// S KEY
            downPressed = false;
        }if (code == KeyEvent.VK_A) {// A KEY
            leftPressed = false;
        }if (code == KeyEvent.VK_D) {//D KEY
            rightPressed = false;
        }if (code == KeyEvent.VK_1) {// 1 KEY
            onePressed = false;
        }if (code == KeyEvent.VK_2) {// 2 KEY
            twoPressed = false;
        }if (code == KeyEvent.VK_3) {// 3 KET
            threePressed = false;
        }
    }

//    NECESSARY CLASS INSIDE MOUSE LISTENER IMPORT
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();//GET KEYCODE OF THE WHICH MOUSE BUTTON HAS BEEN PRESSED
        if (button == MouseEvent.BUTTON1) {// LEFT CLICK
            primaryPressed = true;
            secondaryPressed = false; // ENSURES ONLY ONE ACTION AT A TIME
        } else if (button == MouseEvent.BUTTON3) {// RIGHT CLICK
            secondaryPressed = true;
            primaryPressed = false; // ENSURES ONLY ONE ACTION AT A TIME
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        GETS THGE KEYCODE OF THE MOUSE BUTTON THAT WAS RELEASED
        int button = e.getButton();
        if (button == MouseEvent.BUTTON1) { // LEFT CLICK
            primaryPressed = false;
        }
        else if (button == MouseEvent.BUTTON3) { // RIGHT CLICK
            secondaryPressed = false;
        }
    }

//    NECESSARY CLASSES THAT ARE PART OF MOUSE LISTENER CLASS
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}