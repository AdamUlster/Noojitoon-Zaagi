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

        getImage();
    }

    private void getImage() {
        setUp1(setup("npc/butterfly_1", 0.75, 0.75));
        setUp2(setup("npc/butterfly_2", 0.75, 0.75));
        setDown1(setup("npc/butterfly_1", 0.75, 0.75));
        setDown2(setup("npc/butterfly_2", 0.75, 0.75));
        setLeft1(setup("npc/butterfly_1", 0.75, 0.75));
        setLeft2(setup("npc/butterfly_2", 0.75, 0.75));
        setRight1(setup("npc/butterfly_1", 0.75, 0.75));
        setRight2(setup("npc/butterfly_2", 0.75, 0.75));
    }

    public void setAction() {
        Random random = new Random();
        setActionLockCounter(getActionLockCounter() + 1);
        if (getActionLockCounter() == 30) {

            int i = random.nextInt(100) + 1;//pick a random number from 1 to 100

            if (i <= 25) {
                setDirection("up");
            }
            if (i > 25 && i <= 50) {
                setDirection("down");
            }
            if (i > 50 && i <= 75) {
                setDirection("left");
            }
            if (i > 75 && i < 100) {
                setDirection("right");
            }
            setActionLockCounter(0);
        }

    }

}
