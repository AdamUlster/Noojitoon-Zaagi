package main;

import entity.Entity;

public class CollisionChecker {
    private GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.getWorldX() + entity.getSolidArea().x;
        int entityRightWorldX = entity.getWorldX() + entity.getSolidArea().x + entity.getSolidArea().width;
        int entityTopWorldY = entity.getWorldY();
        int entityBottomWorldY = entity.getWorldY() + entity.getSolidArea().y + entity.getSolidArea().height;

        // Makes sure the butterflies do not go out of bounds
        int entityLeftCol = entityLeftWorldX / gp.getTileSize();
        if (entityLeftCol < 0) { // prevents a butterfly from going off the screen
            entityLeftCol = 0;
        }
        else if (entityLeftCol > gp.getMaxWorldCol() - 1) {
            entityLeftCol = gp.getMaxWorldCol() - 1;
        }
        int entityRightCol = entityRightWorldX / gp.getTileSize();
        if (entityRightCol < 0) { // prevents a butterfly from going off the screen
            entityRightCol = 0;
        }
        else if (entityRightCol > gp.getMaxWorldCol() - 1) {
            entityRightCol = gp.getMaxWorldCol() - 1;
        }
        int entityTopRow = entityTopWorldY / gp.getTileSize();
        if (entityTopRow < 0) { // prevents a butterfly from going off the screen
            entityTopRow = 0;
        }
        else if (entityTopRow > gp.getMaxWorldRow() - 1) {
            entityTopRow = gp.getMaxWorldRow() - 1;
        }
        int entityBottomRow = entityBottomWorldY / gp.getTileSize();
        if (entityBottomRow < 0) { // prevents a butterfly from going off the screen
            entityBottomRow = 0;
        }
        else if (entityBottomRow > gp.getMaxWorldRow() - 1) {
            entityBottomRow = gp.getMaxWorldRow() - 1;
        }

        int tileNum1, tileNum2;

        switch (entity.getDirection()) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.getSpeed()) / gp.getTileSize();
                if (entityTopRow < 0) { // prevents a butterfly from going off the screen
                    entityTopRow = 0;
                }
                else if (entityTopRow > gp.getMaxWorldRow() - 1) {
                    entityTopRow = gp.getMaxWorldRow() - 1;
                }
                tileNum1 = gp.getTileM().getMapTileNum()[entityLeftCol][entityTopRow];
                tileNum2 = gp.getTileM().getMapTileNum()[entityRightCol][entityTopRow];
                if (gp.getTileM().getTile()[tileNum1].isCollision() || gp.getTileM().getTile()[tileNum2].isCollision()) {
                    entity.setCollisionOn(true);
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / gp.getTileSize();
                if (entityBottomRow < 0) { // prevents a butterfly from going off the screen
                    entityBottomRow = 0;
                }
                else if (entityBottomRow > gp.getMaxWorldRow() - 1) {
                    entityBottomRow = gp.getMaxWorldRow() - 1;
                }
                tileNum1 = gp.getTileM().getMapTileNum()[entityLeftCol][entityBottomRow];
                tileNum2 = gp.getTileM().getMapTileNum()[entityRightCol][entityBottomRow];
                if (gp.getTileM().getTile()[tileNum1].isCollision() || gp.getTileM().getTile()[tileNum2].isCollision()) {
                    entity.setCollisionOn(true);
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / gp.getTileSize();
                if (entityLeftCol < 0) { // prevents a butterfly from going off the screen
                    entityLeftCol = 0;
                }
                else if (entityLeftCol > gp.getMaxWorldCol() - 1) {
                    entityLeftCol = gp.getMaxWorldCol() - 1;
                }
                tileNum1 = gp.getTileM().getMapTileNum()[entityLeftCol][entityTopRow];
                tileNum2 = gp.getTileM().getMapTileNum()[entityLeftCol][entityBottomRow];
                if (gp.getTileM().getTile()[tileNum1].isCollision() || gp.getTileM().getTile()[tileNum2].isCollision()) {
                    entity.setCollisionOn(true);
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.getSpeed()) / gp.getTileSize();
                if (entityRightCol < 0) { // prevents a butterfly from going off the screen
                    entityRightCol = 0;
                }
                else if (entityRightCol > gp.getMaxWorldCol() - 1) {
                    entityRightCol = gp.getMaxWorldCol() - 1;
                }
                tileNum1 = gp.getTileM().getMapTileNum()[entityRightCol][entityTopRow];
                tileNum2 = gp.getTileM().getMapTileNum()[entityRightCol][entityBottomRow];
                if (gp.getTileM().getTile()[tileNum1].isCollision() || gp.getTileM().getTile()[tileNum2].isCollision()) {
                    entity.setCollisionOn(true);
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) { // if a player hits an object, return the index of the object that a player is hitting
        int index = 999;

        for (int i = 0; i < gp.getObj().length; i++) {
            if (gp.getObj()[i] != null) {

                // Get entity's solid area position
                entity.getSolidArea().x = entity.getWorldX() + entity.getSolidArea().x;
                entity.getSolidArea().y = entity.getWorldY() + entity.getSolidArea().y;

                // Get the object's solid area position
                gp.getObj()[i].getSolidArea().x = gp.getObj()[i].getWorldX() + gp.getObj()[i].getSolidArea().x;
                gp.getObj()[i].getSolidArea().y = gp.getObj()[i].getWorldY() + gp.getObj()[i].getSolidArea().y;

                switch (entity.getDirection()) {
                    case "up":
                        entity.getSolidArea().y -= entity.getSpeed(); // predicts the movement of the entity
                        break;
                    case "down":
                        entity.getSolidArea().y += entity.getSpeed();
                        break;
                    case "left":
                        entity.getSolidArea().x -= entity.getSpeed();
                        break;
                    case "right":
                        entity.getSolidArea().x += entity.getSpeed();
                        break;
                }
                if (entity.getSolidArea().intersects(gp.getObj()[i].getSolidArea())) { // automatically checks if the two rectangles are colliding
                    if (gp.getObj()[i].isCollision()) { // if the object is solid
                        entity.setCollisionOn(true);
                    }
                    if (player) { // if the entity is a player
                        index = i;
                    }
                }

                // resets the entity and the object's collision box
                entity.getSolidArea().x = entity.getSolidAreaDefaultX();
                entity.getSolidArea().y = entity.getSolidAreaDefaultY();
                gp.getObj()[i].getSolidArea().x = gp.getObj()[i].getSolidAreaDefaultX();
                gp.getObj()[i].getSolidArea().y = gp.getObj()[i].getSolidAreaDefaultY();
            }
        }

        return index;

    }

    //NPCS OR MONSTER COLLISION
    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;

        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {

                // Get entity's solid area position
                entity.getSolidArea().x = entity.getWorldX() + entity.getSolidArea().x;
                entity.getSolidArea().y = entity.getWorldY() + entity.getSolidArea().y;

                // Get the object's solid area position
                target[i].getSolidArea().x = target[i].getWorldX() + target[i].getSolidArea().x;
                target[i].getSolidArea().y = target[i].getWorldY() + target[i].getSolidArea().y;

                switch (entity.getDirection()) {
                    case "up":
                        if (entity.getType() == 3 && entity.getName().equals("Eagle Shot")) { // detects if the eagle shot is hitting the target
                            entity.setSpeed(entity.getyMove()); // predicts the projectile's next movement
                        }
                        entity.getSolidArea().y -= entity.getSpeed(); // predicts the movement of the entity
                        break;
                    case "down":
                        if (entity.getType() == 3 && entity.getName().equals("Eagle Shot")) { // detects if the eagle shot is hitting the target
                            entity.setSpeed(entity.getyMove()); // predicts the projectile's next movement
                        }
                        entity.getSolidArea().y += entity.getSpeed();
                        break;
                    case "left":
                        if (entity.getType() == 3 && entity.getName().equals("Eagle Shot")) { // detects if the eagle shot is hitting the target
                            entity.setSpeed(entity.getxMove()); // predicts the projectile's next movement
                        }
                        entity.getSolidArea().x -= entity.getSpeed();
                        break;
                    case "right":
                        if (entity.getType() == 3 && entity.getName().equals("Eagle Shot")) { // detects if the eagle shot is hitting the target
                            entity.setSpeed(entity.getxMove()); // predicts the projectile's next movement
                        }
                        entity.getSolidArea().x += entity.getSpeed();
                        break;
                }
                if (entity.getSolidArea().intersects(target[i].getSolidArea())) { // automatically checks if the two rectangles are colliding
                    if (target[i] != entity) { // makes sure the entity does not collide with itself
                        entity.setCollisionOn(true);
                        index = i;
                    }
                }

                // resets the entity and the object's collision box
                entity.getSolidArea().x = entity.getSolidAreaDefaultX();
                entity.getSolidArea().y = entity.getSolidAreaDefaultY();
                target[i].getSolidArea().x = target[i].getSolidAreaDefaultX();
                target[i].getSolidArea().y = target[i].getSolidAreaDefaultY();
            }
        }

        return index;
    }

    //CHECK IF NPC OR MONSTER IS HITTING THE PLAYER
    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false; // keeps track of whether another entity is making contact with the player

        // Get entity's solid area position
        entity.getSolidArea().x = entity.getWorldX() + entity.getSolidArea().x;
        entity.getSolidArea().y = entity.getWorldY() + entity.getSolidArea().y;

        // Get the object's solid area position
        gp.getPlayer().getSolidArea().x = gp.getPlayer().getWorldX() + gp.getPlayer().getSolidArea().x;
        gp.getPlayer().getSolidArea().y = gp.getPlayer().getWorldY() + gp.getPlayer().getSolidArea().y;

        switch (entity.getDirection()) {
            case "up":
                entity.getSolidArea().y -= entity.getSpeed(); // predicts the movement of the entity
                break;
            case "down":
                entity.getSolidArea().y += entity.getSpeed();
                break;
            case "left":
                entity.getSolidArea().x -= entity.getSpeed();
                break;
            case "right":
                entity.getSolidArea().x += entity.getSpeed();
                break;
        }
        if (entity.getSolidArea().intersects(gp.getPlayer().getSolidArea())) { // automatically checks if the two rectangles are colliding
            entity.setCollisionOn(true);
            contactPlayer = true; // the entity has made contact with the player
        }

        // resets the entity and the object's collision box
        entity.getSolidArea().x = entity.getSolidAreaDefaultX();
        entity.getSolidArea().y = entity.getSolidAreaDefaultY();
        gp.getPlayer().getSolidArea().x = gp.getPlayer().getSolidAreaDefaultX();
        gp.getPlayer().getSolidArea().y = gp.getPlayer().getSolidAreaDefaultY();

        return contactPlayer;
    }
}