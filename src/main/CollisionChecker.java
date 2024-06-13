package main;

import entity.Entity;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                if (entityTopRow < 0) { // prevents a butterfly from going off the screen
                    entityTopRow = 0;
                }
                else if (entityTopRow > gp.maxWorldRow - 1) {
                    entityTopRow = gp.maxWorldRow - 1;
                }
                System.out.println("Top row " + entityTopRow);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                if (entityBottomRow < 0) { // prevents a butterfly from going off the screen
                    entityBottomRow = 0;
                }
                else if (entityBottomRow > gp.maxWorldRow - 1) {
                    entityBottomRow = gp.maxWorldRow - 1;
                }
                System.out.println("Bottom row " + entityBottomRow);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                if (entityLeftCol < 0) { // prevents a butterfly from going off the screen
                    entityLeftCol = 0;
                }
                else if (entityLeftCol > gp.maxWorldCol - 1) {
                    entityLeftCol = gp.maxWorldCol - 1;
                }
                System.out.println("Left col " + entityLeftCol);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                if (entityRightCol < 0) { // prevents a butterfly from going off the screen
                    entityRightCol = 0;
                }
                else if (entityRightCol > gp.maxWorldCol - 1) {
                    entityRightCol = gp.maxWorldCol - 1;
                }
                System.out.println("Right col " + entityRightCol);
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
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
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }

        return index;
    }

    //CHECK IF NPC OR MONSTER IS HITTING THE PLAYER
    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false; // keeps track of whether another entity is making contact with the player

        // Get entity's solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        // Get the object's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

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
        if (entity.solidArea.intersects(gp.player.solidArea)) { // automatically checks if the two rectangles are colliding
            entity.collisionOn = true;
            contactPlayer = true; // the entity has made contact with the player
        }

        // resets the entity and the object's collision box
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }
}