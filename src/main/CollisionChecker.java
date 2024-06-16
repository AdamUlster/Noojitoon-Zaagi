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
                if (gp.getTileM().getTile()[tileNum1].collision || gp.getTileM().getTile()[tileNum2].collision) {
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
                if (gp.getTileM().getTile()[tileNum1].collision || gp.getTileM().getTile()[tileNum2].collision) {
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
                if (gp.getTileM().getTile()[tileNum1].collision || gp.getTileM().getTile()[tileNum2].collision) {
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
                if (gp.getTileM().getTile()[tileNum1].collision || gp.getTileM().getTile()[tileNum2].collision) {
                    entity.setCollisionOn(true);
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) { // if a player hits an object, return the index of the object that a player is hitting
        int index = 999;

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {

                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the object's solid area position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed; // predicts the movement of the entity
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }
                if (entity.solidArea.intersects(gp.obj[i].solidArea)) { // automatically checks if the two rectangles are colliding
                    if (gp.obj[i].collision) { // if the object is solid
                        entity.collisionOn = true;
                    }
                    if (player) { // if the entity is a player
                        index = i;
                    }
                }

                // resets the entity and the object's collision box
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
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
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the object's solid area position
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        if (entity.type == 3 && entity.name.equals("Eagle Shot")) { // detects if the eagle shot is hitting the target
                            entity.speed = entity.yMove; // predicts the projectile's next movement
                        }
                        entity.solidArea.y -= entity.speed; // predicts the movement of the entity
                        break;
                    case "down":
                        if (entity.type == 3 && entity.name.equals("Eagle Shot")) { // detects if the eagle shot is hitting the target
                            entity.speed = entity.yMove; // predicts the projectile's next movement
                        }
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        if (entity.type == 3 && entity.name.equals("Eagle Shot")) { // detects if the eagle shot is hitting the target
                            entity.speed = entity.xMove; // predicts the projectile's next movement
                        }
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        if (entity.type == 3 && entity.name.equals("Eagle Shot")) { // detects if the eagle shot is hitting the target
                            entity.speed = entity.xMove; // predicts the projectile's next movement
                        }
                        entity.solidArea.x += entity.speed;
                        break;
                }
                if (entity.solidArea.intersects(target[i].solidArea)) { // automatically checks if the two rectangles are colliding
                    if (target[i] != entity) { // makes sure the entity does not collide with itself
                        entity.collisionOn = true;
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