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
        if (code == KeyEvent.VK_C) {
            displayControls = !displayControls; // displays the controls if they were not displayed previously
        }

        if (code == KeyEvent.VK_M) {
            displayMap = !displayMap; // displays the map if it wasn't already
        }

        if (code == KeyEvent.VK_Q) {
            gp.map.miniMapOn = !gp.map.miniMapOn; // displays the mini map if it wasn't already displayed
        }

        if (code == KeyEvent.VK_H) {
            gp.player.onPath = !gp.player.onPath;
            gp.tileM.drawPath = !gp.tileM.drawPath; // draws the path to the maze if it wasn't already shown
        }
    }

    @Override
    public void keyReleased (KeyEvent e){

        int code = e.getKeyCode();//returns the keycode of a key that has been released

        //now resets the keypressed booleans when the key is released
        if (code == KeyEvent.VK_W) {//if w key has been released
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {//if s key has been released
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {//if A key has been released
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {//if D key has been released
            rightPressed = false;
        }
        if (code == KeyEvent.VK_1) {//if key 1 has been released
            onePressed = false;
        }
        if (code == KeyEvent.VK_2) {
            twoPressed = false;
        }
        if (code == KeyEvent.VK_3) {
            threePressed = false;
        }
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = false;
        }
    } // implements a class that listens to keyboard inputs

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        if (button == MouseEvent.BUTTON1) {
            primaryPressed = true;
            secondaryPressed = false; // ensures only one action at a time
        } else if (button == MouseEvent.BUTTON3) {
            secondaryPressed = true;
            primaryPressed = false; // ensures only one action at a time
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int button = e.getButton(); // gets the mouse button that was clicked
        if (button == MouseEvent.BUTTON1) { // left click
            primaryPressed = false;
        }
        else if (button == MouseEvent.BUTTON3) { // right click
            secondaryPressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}