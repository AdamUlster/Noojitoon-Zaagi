package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//handle keyboard inputs
public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;//boolean values that determine which keys are
    // pressed and which are not

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
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();//returns the keycode of a key that has beeen released

        //now resets the keypressed booleans when the key is released
        if (code == KeyEvent.VK_W) {//if w key has been pressed
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {//if s key has been pressed
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {//if A key has been pressed
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {//if D key has been pressed
            rightPressed = false;
        }
    }//implements a class that listens to keyboadd inputs
}
