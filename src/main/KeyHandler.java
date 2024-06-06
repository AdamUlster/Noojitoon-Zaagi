package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//handle keyboard inputs
public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;//boolean values that determine which keys are
    // pressed and which are not
    public boolean onePressed, twoPressed;//boolean values that determine which number keys have been pressed for sprite switching
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
        if (code == KeyEvent.VK_1) {//if 1 key has been pressed
            onePressed = true;
        }
        if (code == KeyEvent.VK_2) {//if key 2 has been pressed
            twoPressed = true;
        }
        //continue this chain for when more moves are added
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();//returns the keycode of a key that has beeen released

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
    }//implements a class that listens to keyboard inputs
}