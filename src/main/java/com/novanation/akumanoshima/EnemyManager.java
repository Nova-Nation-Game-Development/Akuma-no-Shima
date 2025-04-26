package com.novanation.akumanoshima;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EnemyManager {
    
    private static HashMap<String, Entity> enemies = new HashMap<>();
    private static int worldWidth;

    private static Player player;

    // Difficulty Presets

    // Enemy Count
    public static final int LOWER_EASY = 5;
    public static final int UPPER_EASY = 15;

    public static final int LOWER_NORMAL = 20;
    public static final int UPPER_NORMAL = 30;

    public static final int LOWER_HARD = 40;
    public static final int UPPER_HARD = 70;

    // Enemy Health
    public static final int HEALTH_EASY = 2;
    public static final int HEALTH_NORMAL = 3;
    public static final int HEALTH_HARD = 5;

    public static void generateEnemies(Difficulty difficulty)
    {
        Random random = new Random();

        int enemyCount = 0;
        int hellHoundCount = 0;

        switch (difficulty)
        {
            case EASY -> {
                enemyCount = random.nextInt(LOWER_EASY, UPPER_EASY) + 1;
                hellHoundCount = random.nextInt(2); // [0..1]
            }
            case NORMAL -> {
                enemyCount = random.nextInt(LOWER_NORMAL, UPPER_NORMAL) + 1;
                hellHoundCount = random.nextInt(2, 6); // [2..5]
            }
            case HARD -> {
                enemyCount = random.nextInt(LOWER_HARD, UPPER_HARD) + 1;
                hellHoundCount = random.nextInt(8, 16); // [8..15]
            }
        }

        List<String> enemyTypes = new ArrayList<>();

        // Fill the list with the correct number of each type
        for (int i = 0; i < hellHoundCount; i++) {
            enemyTypes.add("HELLHOUND");
        }
        for (int i = 0; i < enemyCount - hellHoundCount; i++) {
            enemyTypes.add("ONI");
        }

        // Shuffle the spawn order
        Collections.shuffle(enemyTypes);

        int segmentWidth = worldWidth / enemyCount;
        Set<Integer> usedXPositions = new HashSet<>();

        for (int i = 0; i < enemyCount; i++)
        {
            String type = enemyTypes.get(i);

            int enemyWidth = type.equals("HELLHOUND") ? 128 : 64;
            int enemyHeight = type.equals("HELLHOUND") ? 64 : 128;

            int segmentStart = i * segmentWidth;
            int segmentEnd = segmentStart + segmentWidth;

            segmentStart = Math.max(segmentStart, 1000); // To prevent the enemies from spawning in front the player
            segmentEnd = Math.min(segmentEnd, worldWidth);

            int x; // TODO: Presently, multiple enemies can spawn on top of each other
            int attempts = 0;
            do
            {
                x = (int) (Math.random() * (segmentEnd - segmentStart)) + segmentStart;
                x = Math.max(1000, Math.min(worldWidth, x));

                if (++attempts > UPPER_HARD) break; // Prevent looping infinitely
            } while (usedXPositions.contains(x));

            usedXPositions.add(x);
            int y = getYPosition();

            if (type.equals("HELLHOUND")) {
                EnemyHellhound hellHound = new EnemyHellhound(enemyWidth, enemyHeight, x, y, "Enemy " + i);
                enemies.put("Enemy " + i, hellHound);
            } else {
                EnemyOni oni = new EnemyOni(enemyWidth, enemyHeight, x, y, "Enemy " + i, player);
                enemies.put("Enemy " + i, oni);
            }
        }
    }

    public static void destroyEntity(Entity enemy)
    {
        // TODO: Update the Entity Manager to remove it from the list
        if (enemy instanceof EnemyHellhound enemyHellhound)
        {
            enemies.remove(enemyHellhound.getEnemyID());
        }

        if (enemy instanceof EnemyOni enemyOni)
        {
            enemies.remove(enemyOni.getEnemyID());
        }
    }

    private static int getYPosition()
    {
        // TODO: Get height using their x location with the tile and getting the y pos of the tile
        return 50;
    }

    public static void setWorldWidth(int worldLimit) { worldWidth = worldLimit; }

    public static int getRemainingEnemies() { return enemies.size(); }

    // TODO: Move based on world speed
    public static void move(int direction)
    {
        for (Entity enemy : enemies.values())
            enemy.move(direction);
    }

    public static void draw(Graphics2D g2)
    {
        if (enemies == null) return;

        for (Entity enemy : enemies.values())
            enemy.draw(g2);
    }

    public static Collection<Entity> getAllEnemies() {
    return enemies.values();
}

    public static void setPlayer(Player player) {
        EnemyManager.player = player;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void clearEnemies() {
        enemies.clear();
    }

    
}
