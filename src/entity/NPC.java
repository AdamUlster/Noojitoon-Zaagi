package entity;

import main.GamePanel;

import java.util.Random;

public class NPC extends Entity {
    private double hitboxScale = 0.65;
    public NPC(GamePanel gp) {
        super(gp);

        setType(1); // sets this entity's type to an NPC
        setDirection("down");
        setSpeed(2);

        getSolidArea().width = 1;
        getSolidArea().height = 1;
//        solidArea.width = (int)(gp.tileSize * hitboxScale);
//        solidArea.height = (int)(gp.tileSize * hitboxScale);
//        solidArea.x = (gp.tileSize - solidArea.width) / 2;
//        solidArea.y = (gp.tileSize - solidArea.height) / 2;
//
//        solidAreaDefaultX = solidArea.x;
//        solidAreaDefaultY = solidArea.y;

    public double hitboxScale = 0.65;// HITBOX SCALING FACTOR

    public NPC(GamePanel gp) {//DEFAULT NPC CONSTRUCTOR
        super(gp);//CALL ON GAMEPANEL CLASS

        type = 1; // SET ENTITY TYPE TO NPC
        direction = "down";// SET DEFAULT DIRECTION TO DOWN
        speed = 2;//SET SPEED TO TWO PIXELS PER FRAME

//        KEEP SOLID AREA VERY SMALL N CASE WE DECIDE TO TURN COLLISIONS ON FOR THE NPC BUTTERFLIES
        solidArea.width = 1;
        solidArea.height = 1;

        getImage();// CALL ON GET IMAGE CLASS TO READ AND RETRIEVE THE SPRITE IMAGES
    }

    private void getImage() {//read and retrieve butterfly sprite images
        setUp1(setup("npc/butterfly_1", 0.75, 0.75));
        setUp2(setup("npc/butterfly_2", 0.75, 0.75));
        setDown1(setup("npc/butterfly_1", 0.75, 0.75));
        setDown2(setup("npc/butterfly_2", 0.75, 0.75));
        setLeft1(setup("npc/butterfly_1", 0.75, 0.75));
        setLeft2(setup("npc/butterfly_2", 0.75, 0.75));
        setRight1(setup("npc/butterfly_1", 0.75, 0.75));
        setRight2(setup("npc/butterfly_2", 0.75, 0.75));

//    MOVEMENT
    public void setAction() {
        Random random = new Random();// CREATE RANDOMIZER
        setActionLockCounter(getActionLockCounter() + 1);//INCREASE COUNTER WITH EACH FRAME
        if (getActionLockCounter() == 30) {// NPC WILL MOVE CONTINUOUSLY IN ONE DIRECTION FOR 30 FRAMES, THEN RANDOMLY PICK
            // ANOTHER DIRECTION TO TRAVEL IN

            int i = random.nextInt(100) + 1;//PICKS A RANDOM NUMBER BETWEEN 1 AND 100

            if (i <= 25) { //TRAVEL UP 1/4 OF THE TIME
                setDirection("up");
            }
            if (i > 25 && i <= 50) { //TRAVEL DOWN 1/4 OF THE TIME
                setDirection("down");
            }
            if (i > 50 && i <= 75) { //TRAVEL LEFT 1/4 OF THE TIME
                setDirection("left");
            }
            if (i > 75 && i < 100) { //TRAVEL RIGHT 1/4 OF THE IME
                setDirection("right");
            }
            setActionLockCounter(0); //RESET COUNTER
        }
    }
}
