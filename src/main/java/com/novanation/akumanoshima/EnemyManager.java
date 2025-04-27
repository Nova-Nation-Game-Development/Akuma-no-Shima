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
    private static HashMap<String, Entity> enemiesAlive = new HashMap<>();
    private static int worldWidth;

    private static Player player;

    // Difficulty Presets

    // Enemy Count
    private static final int LOWER_EASY = 5;
    private static final int UPPER_EASY = 15;

    private static final int LOWER_NORMAL = 20;
    private static final int UPPER_NORMAL = 30;

    private static final int LOWER_HARD = 40;
    private static final int UPPER_HARD = 70;

    // Enemy Health
    private static final int HEALTH_EASY = 2;
    private static final int HEALTH_NORMAL = 3;
    private static final int HEALTH_HARD = 5;

    // Boss Fight
    private static boolean isFinalLevel = false;

    public static boolean isFinal() { return isFinalLevel; }

    public static void generateEnemies(Difficulty difficulty, boolean isFinal, GamePanel panel)
    {
        isFinalLevel = isFinal;
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

        if (isFinal)
        {
            double widthScale = (double) panel.getGameWindow().getConfig().getResolutionWidth() / panel.getGameWindow().getWidth();
            double heightScale = (double) panel.getGameWindow().getConfig().getResolutionHeight() / panel.getGameWindow().getHeight();

            int bossHeight = (int) (170 * heightScale);
            int bossWidth = (int) (120 * widthScale);

            // TODO: Update Boss y level
            EnemyMaou demonLord = new EnemyMaou(bossWidth, bossHeight, panel.getWidth() - bossWidth - 30, 50, "The Demon Lord", panel);
            enemies.put(demonLord.getEnemyID(), demonLord);
            enemiesAlive.put(demonLord.getEnemyID(), demonLord);
            return; // Only the demon lord will be initially created
        }

        LevelManager.setTotalEnemies(enemyCount);
        LevelManager.update();

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

            int enemyWidth = type.equals("HELLHOUND") ? panel.getScaledHeight() : panel.getScaledWidth();
            int enemyHeight = type.equals("HELLHOUND") ? panel.getScaledWidth() : panel.getScaledHeight();

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
                EnemyHellhound hellHound = new EnemyHellhound(enemyWidth, enemyHeight, x, y, "Enemy " + i, panel);
                enemies.put("Enemy " + i, hellHound);
            } else {
                EnemyOni oni = new EnemyOni(enemyWidth, enemyHeight, x, y, "Enemy " + i, panel);
                enemies.put("Enemy " + i, oni);
            }
        }
    }

    public static HashMap<String, Entity> getEnemies() { return enemies; }

    public static void killAllEntities()
    {
        for (Entity enemy : enemies.values())
        {
            if (enemy instanceof EnemyHellhound enemyHellhound)
                enemyHellhound.setEnemyID("DESTROYED");

            if (enemy instanceof EnemyOni enemyOni)
                enemyOni.setEnemyID("DESTROYED");

            if (enemy instanceof EnemyMaou enemyMaou)
                enemyMaou.setEnemyID("DESTROYED");
        }

        LevelManager.update();
    }

    public static void destroyEntity(Entity enemy)
    {
        if (enemy instanceof EnemyHellhound enemyHellhound)
            enemyHellhound.setEnemyID("DESTROYED");

        if (enemy instanceof EnemyOni enemyOni)
            enemyOni.setEnemyID("DESTROYED");

        if (enemy instanceof EnemyMaou enemyMaou)
            enemyMaou.setEnemyID("DESTROYED");

        LevelManager.update();
    }

    private static int getYPosition()
    {
        // TODO: Get height using their x location with the tile and getting the y pos of the tile
        return 50;
    }

    public static void setWorldWidth(int worldLimit) { worldWidth = worldLimit; }

    public static HashMap<String, Entity> getAliveList()
    {
        for (Entity entity : enemies.values())
        {
            if (entity instanceof EnemyHellhound enemyHellhound)
                if ("DESTROYED".equals(enemyHellhound.getEnemyID()))
                    enemiesAlive.put(enemyHellhound.getEnemyID(), enemyHellhound);
                
            if (entity instanceof EnemyOni enemyOni)
                if ("DESTROYED".equals(enemyOni.getEnemyID()))
                    enemiesAlive.put(enemyOni.getEnemyID(), enemyOni);

            if (entity instanceof EnemyMaou enemyMaou)
                if ("DESTROYED".equals(enemyMaou.getEnemyID()))
                    enemiesAlive.put(enemyMaou.getEnemyID(), enemyMaou);
        }

        return enemiesAlive;
    }

    public static int getRemainingEnemies()
    {
        int remainingEnemies = 0;
        if (enemies.values() == null) return 0;

        for (Entity entity : enemies.values())
        {
            if (entity instanceof EnemyHellhound enemyHellhound)
                if (!"DESTROYED".equals(enemyHellhound.getEnemyID()))
                    remainingEnemies++;
                
            if (entity instanceof EnemyOni enemyOni)
                if (!"DESTROYED".equals(enemyOni.getEnemyID()))
                    remainingEnemies++;

            if (entity instanceof EnemyMaou enemyMaou)
                if (!"DESTROYED".equals(enemyMaou.getEnemyID()))
                    remainingEnemies++;
        }

        return remainingEnemies;
    }

    // TODO: Move based on world speed
    public static void move(int direction)
    {
        for (Entity enemy : enemies.values())
        {
            enemy.move(direction);
            // enemy.setWorldPos((int)(direction * 0.1));
            enemy.setWorldPos(direction);
        }
    }

    public static void draw(Graphics2D g2)
    {
        if (enemies == null) return;

        for (Entity enemy : enemies.values())
        {
            if (enemy instanceof EnemyHellhound enemyHellhound)
                if (!"DESTROYED".equals(enemyHellhound.getEnemyID()))
                    enemy.draw(g2);

            if (enemy instanceof EnemyOni enemyOni)
                if (!"DESTROYED".equals(enemyOni.getEnemyID()))
                    enemy.draw(g2);

            if (enemy instanceof EnemyMaou enemyMaou)
                if (!"DESTROYED".equals(enemyMaou.getEnemyID()))
                    enemy.draw(g2);

            // TODO: Testing purposes
            // g2.setColor(Color.BLUE);
            // if (enemy.getCurrentChunk() != null)
            //     g2.fill(enemy.getCurrentChunk().getChunkBounds());
        }
    }

    public static Collection<Entity> getAllEnemies() {
    return enemies.values();
}

    public static Player getPlayer() {
        return player;
    }

    public static void clearEnemies() {
        enemies.clear();
    }

    
}
