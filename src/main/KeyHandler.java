package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.Key;

//handle keyboard inputs
public class KeyHandler implements KeyListener, MouseListener {

    public GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, shotKeyPressed;//boolean values that determine which keys are
    // pressed and which are not
    public boolean onePressed, twoPressed, threePressed;//boolean values that determine which number keys have been
    public boolean primaryPressed, secondaryPressed;
    // pressed for sprite switching
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
        int code = e.getKeyCode();//returns the keycode of a key that has been pressed

        if (code == KeyEvent.VK_W) {//if w key has been pressed
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {//if s key has been pressed
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {//if A key has been pressed
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {//if D key has been pressed
            rightPressed = true;
        }
        if (!gp.player.isDying) { // runs if the player is not dying
            if (code == KeyEvent.VK_1 && !gp.player.spirits[0].dead) {//if 1 key has been pressed and bear is not dead
                onePressed = true;
            } else if (code == KeyEvent.VK_1 && gp.player.spirits[0].dead && gp.player.displayDeathMessage) { // if the spirit is dead and the death message should be displayed
                gp.ui.showMessage("You cannot switch to the bear since the bear is dead");
            }
            if (code == KeyEvent.VK_2 && !gp.player.spirits[1].dead) {//if key 2 has been pressed and eagle is not dead
                twoPressed = true;
            } else if (code == KeyEvent.VK_2 && gp.player.spirits[1].dead && gp.player.displayDeathMessage) { // if the spirit is dead and the death message should be displayed
                gp.ui.showMessage("You cannot switch to the eagle since the eagle is dead");
            }
            if (code == KeyEvent.VK_3 && !gp.player.spirits[2].dead) {//if key 3 has been pressed and turtle is not dead
                threePressed = true;
            } else if (code == KeyEvent.VK_3 && gp.player.spirits[2].dead && gp.player.displayDeathMessage) { // if the spirit is dead and the death message should be displayed
                gp.ui.showMessage("You cannot switch to the turtle since the turtle is dead");
            }
            if (code == KeyEvent.VK_K) {//K key has been pressed
                primaryPressed = true;
            }
            if (code == KeyEvent.VK_L) {//L key has been pressed
                secondaryPressed = true;
            }
            //continue this chain for when more moves are added
        }
        if (code == KeyEvent.VK_F && gp.player.getCurrentSpirit().name.equals("Turtle")) { // only allow the projectile to be shot if the "F" key is pressed and the current spirit is a turtle
            shotKeyPressed = true;
        }

        //DEBUG STUFF
        if (code == KeyEvent.VK_T) {
            checkDrawTime = !checkDrawTime; // sets checkDrawTime to its other state
        }

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