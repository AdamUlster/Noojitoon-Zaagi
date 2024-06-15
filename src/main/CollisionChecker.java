package main;

import entity.Entity;

public class CollisionChecker {
    GamePanel gp;// CALL ON GAMEPANEL CLASS

    //    CONSTRUCTOR CLASS
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
//        CALCULATE BOUNDARIES OF ENTITY
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

//        MAKE SURE NPC BUTTERFLIES DO NOT GO OUT OF BOUNDS
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        if (entityLeftCol < 0) { // prevents a butterfly from going off the screen
            entityLeftCol = 0;
        } else if (entityLeftCol > gp.maxWorldCol - 1) {
            entityLeftCol = gp.maxWorldCol - 1;
        }
        int entityRightCol = entityRightWorldX / gp.tileSize;

//        PREVENTS NPC BUTTERFLY FROM GOING OFF THE SCREEN
        if (entityRightCol < 0) {
            entityRightCol = 0;
        } else if (entityRightCol > gp.maxWorldCol - 1) {
            entityRightCol = gp.maxWorldCol - 1;
        }
        int entityTopRow = entityTopWorldY / gp.tileSize;
        if (entityTopRow < 0) {
            entityTopRow = 0;
        } else if (entityTopRow > gp.maxWorldRow - 1) {
            entityTopRow = gp.maxWorldRow - 1;
        }
        int entityBottomRow = entityBottomWorldY / gp.tileSize;
        if (entityBottomRow < 0) {
            entityBottomRow = 0;
        } else if (entityBottomRow > gp.maxWorldRow - 1) {
            entityBottomRow = gp.maxWorldRow - 1;
        }

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
//                PREVENTS A BUTTERFLY NPC FROM GOING OFF THE SCREEN
                if (entityTopRow < 0) { // prevents a butterfly from going off the screen
                    entityTopRow = 0;
                } else if (entityTopRow > gp.maxWorldRow - 1) {
                    entityTopRow = gp.maxWorldRow - 1;
                }
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
                } else if (entityBottomRow > gp.maxWorldRow - 1) {
                    entityBottomRow = gp.maxWorldRow - 1;
                }
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
                } else if (entityLeftCol > gp.maxWorldCol - 1) {
                    entityLeftCol = gp.maxWorldCol - 1;
                }
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
                } else if (entityRightCol > gp.maxWorldCol - 1) {
                    entityRightCol = gp.maxWorldCol - 1;
                }
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    //    IF A PLAYER HITS AN OBJECT, RETURN THE INDEX OF THE  OBJECT THE PLAYER IS HITTING
    public int checkObject(Entity entity, boolean player) {
        int index = 999;

//        ITERATE THROUGH ALL OBJECTS
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {

                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the object's solid area position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

//                PREDICTS THE MOVEMENT OF THE ENTITY
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
//                AUTOMATICALLY CHECKS IF THE TWO RECTANGLES ARE COLLISING
                if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
//                    CHECK IF THE OBJECT IS SOLID
                    if (gp.obj[i].collision) {
                        entity.collisionOn = true;
                    }
//                    IF THE ENTITY IS A PLAYER
                    if (player) {
                        index = i;
                    }
                }

//                RESETS THE ENTITY AND THE OBJECT'S COLLISION BOX
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

//                GET ENTITY'S SOLID AREA POSITION
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

//                GET THE OBJECT'S SOLID AREA POSITION
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                switch (entity.direction) {
                    case "up":
//                        DETECTS IF THE EAGLE SHOT IS HITTING THE TARGET
                        if (entity.type == 3 && entity.name.equals("Eagle Shot")) {
//                            PREDICTS THE PROJECTILE'S NEXT MOVEMENT
                            entity.speed = entity.yMove;
                        }
//                        PREDICTS THE MOVEMENT OF THE ENTITY
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
//                        DETECTS IF THE EAGLE SHOT IS HITTING THE TARGET
                        if (entity.type == 3 && entity.name.equals("Eagle Shot")) {
//                            PREDICTS THE PROJECTILE'S NEXT MOVEMENT
                            entity.speed = entity.yMove;
                        }
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
//                    DETECTS IF THE EAGLE SHOT IS HITTING THE TARGET
                        if (entity.type == 3 && entity.name.equals("Eagle Shot")) { // detects if the eagle shot is hitting the target
                            entity.speed = entity.xMove;
                        }
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
//                        DETECTS IF THE EAGLE SHOT IS HITTING THE TARGET
                        if (entity.type == 3 && entity.name.equals("Eagle Shot")) {
                            entity.speed = entity.xMove;
                        }
                        entity.solidArea.x += entity.speed;
                        break;
                }
//                AUTOMATICALLY CHECKS IF THE TWO RECTANGLES ARE COLLIDING
                if (entity.solidArea.intersects(target[i].solidArea)) {
//                    MAKES SURE THE ENTITY DOES NOT COLLIDE WITH ITSELF
                    if (target[i] != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }

//                RESETS ENTITY AND THE OBJECT'S COLLISION BOX
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        return index;//RETURN THE INDEX OF THE ENTITY BEING TOUCHED
    }

    //CHECK IF NPC OR MONSTER IS HITTING THE PLAYER
    public boolean checkPlayer(Entity entity) {

//        BOOLEAN TO KEEP TRACK OF WHETHTER ANOTHER ENTITY IS MAKING CONTACT WITH THE PLAYER
        boolean contactPlayer = false;// SET TO FALSE BY DEFAULT MEANING ENTITY IS NOT CONTACTING PLAYER

//        GET ENTITY'S SOLID AREA POSITION
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

//        GET OBJECT'S SOLID AREA POSITION
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

//        PREDICT MOVEMENT OF THE ENTITY
        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
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
//        AUTOMATICALLY CHECKS IF THE TWO COLLISION RECTANGLES ARE COLLIDING
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true; // THE ENTITY HAS MADE CONTACT WITH THEY PLAYER HIT BOX
        }

//        RESET THE ENTITY AND THE OBJECT'S COLLISION BOX
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;//RETURN TRUE IF THE ENTITY IS IN CONTACT WITH THE PLAYER, RETURNS FALSE OTHERWISE
    }
}