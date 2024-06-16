package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//HANDLE KEYBOARD INPUTS
public class KeyHandler implements KeyListener, MouseListener {

    private GamePanel gp;
    private boolean upPressed, downPressed, leftPressed, rightPressed, shotKeyPressed;//boolean values that determine which keys are
    // pressed and which are not
    private boolean onePressed, twoPressed, threePressed;//boolean values that determine which number keys have been
    private boolean primaryPressed, secondaryPressed;
    // pressed for sprite switching
    //DEBUG STUFF
    private boolean checkDrawTime = false;
    private boolean displayControls = false;
    private boolean displayMap = false;

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
        if (!gp.getPlayer().isDying()) { // runs if the player is not dying
            if (code == KeyEvent.VK_1 && !gp.getPlayer().getSpirits()[0].isDead()) {//if 1 key has been pressed and bear is not dead
                onePressed = true;
            } else if (code == KeyEvent.VK_1 && gp.getPlayer().getSpirits()[0].isDead() && gp.getPlayer().isDisplayDeathMessage()) { // if the spirit is dead and the death message should be displayed
                gp.getUi().showMessage("You cannot switch to the bear since the bear is dead");
            }
            if (code == KeyEvent.VK_2 && !gp.getPlayer().getSpirits()[1].isDead()) {//if key 2 has been pressed and eagle is not dead
                twoPressed = true;
            } else if (code == KeyEvent.VK_2 && gp.getPlayer().getSpirits()[1].isDead() && gp.getPlayer().isDisplayDeathMessage()) { // if the spirit is dead and the death message should be displayed
                gp.getUi().showMessage("You cannot switch to the eagle since the eagle is dead");
            }
            if (code == KeyEvent.VK_3 && !gp.getPlayer().getSpirits()[2].isDead()) {//if key 3 has been pressed and turtle is not dead
                threePressed = true;
            } else if (code == KeyEvent.VK_3 && gp.getPlayer().getSpirits()[2].isDead() && gp.getPlayer().isDisplayDeathMessage()) { // if the spirit is dead and the death message should be displayed
                gp.getUi().showMessage("You cannot switch to the turtle since the turtle is dead");
            }
            if (code == KeyEvent.VK_K) {//K key has been pressed
                primaryPressed = true;
            }
            if (code == KeyEvent.VK_L) {//L key has been pressed
                secondaryPressed = true;
            }
            //continue this chain for when more moves are added
        }
        if (code == KeyEvent.VK_F && gp.getPlayer().getCurrentSpirit().getName().equals("Turtle")) { // only allow the projectile to be shot if the "F" key is pressed and the current spirit is a turtle
            shotKeyPressed = true;
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
            gp.getMap().setMiniMapOn(!gp.getMap().isMiniMapOn()); // displays the mini map if it wasn't already displayed
        }
        if (code == KeyEvent.VK_H) {
            gp.getPlayer().setOnPath(!gp.getPlayer().isOnPath());
            gp.getTileM().setDrawPath(!gp.getTileM().isDrawPath()); // draws the path to the maze if it wasn't already shown
        }
    }

    @Override
    public void keyReleased (KeyEvent e){

//        RETURNS THE KEYCODE OF A KEY THAT HAS BEN RELEASED
        int code = e.getKeyCode();

//        RESETS THE KEYPRESSED BOOLEANS WHEN THE KEYS ARE RELEASED
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
//        GETS THE KEYCODE OF THE MOUSE BUTTON THAT WAS RELEASED
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
    public void mouseExited(MouseEvent e) {

    }

    // Get and set methods
    public boolean isUpPressed() {
        return upPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public boolean isShotKeyPressed() {
        return shotKeyPressed;
    }

    public void setShotKeyPressed(boolean shotKeyPressed) {
        this.shotKeyPressed = shotKeyPressed;
    }

    public boolean isOnePressed() {
        return onePressed;
    }

    public void setOnePressed(boolean onePressed) {
        this.onePressed = onePressed;
    }

    public boolean isTwoPressed() {
        return twoPressed;
    }

    public void setTwoPressed(boolean twoPressed) {
        this.twoPressed = twoPressed;
    }

    public boolean isThreePressed() {
        return threePressed;
    }

    public void setThreePressed(boolean threePressed) {
        this.threePressed = threePressed;
    }

    public boolean isPrimaryPressed() {
        return primaryPressed;
    }

    public void setPrimaryPressed(boolean primaryPressed) {
        this.primaryPressed = primaryPressed;
    }

    public boolean isSecondaryPressed() {
        return secondaryPressed;
    }

    public void setSecondaryPressed(boolean secondaryPressed) {
        this.secondaryPressed = secondaryPressed;
    }

    public boolean isCheckDrawTime() {
        return checkDrawTime;
    }

    public void setCheckDrawTime(boolean checkDrawTime) {
        this.checkDrawTime = checkDrawTime;
    }

    public boolean isDisplayControls() {
        return displayControls;
    }

    public void setDisplayControls(boolean displayControls) {
        this.displayControls = displayControls;
    }

    public boolean isDisplayMap() {
        return displayMap;
    }

    public void setDisplayMap(boolean displayMap) {
        this.displayMap = displayMap;
    }
}