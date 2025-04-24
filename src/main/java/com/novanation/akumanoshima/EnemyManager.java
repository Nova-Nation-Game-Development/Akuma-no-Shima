package com.novanation.akumanoshima;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public class EnemyManager {
    
    private static ArrayList<Entity> enemies = new ArrayList<>();

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

        for (int i = 0; i < enemyCount; i++)
        {
            if (i < hellHoundCount)
            {
                // Hellhounds are quick but do only melee damage
                EnemyHellhound hellHound = new EnemyHellhound(80, 100, 100, 50);
                enemies.add((EnemyHellhound) hellHound);
            }
            else
            {
                // The Oni are slow but have range attacks to compensate
                EnemyOni oni = new EnemyOni(80, 120, 10, 50);
                enemies.add((EnemyOni) oni);
            }
        }
    }

    // TODO: Move based on world speed
    public static void move(int direction)
    {
        for (Entity enemy : enemies)
            enemy.move(direction);
    }

    public static void draw(Graphics2D g2)
    {
        if (enemies == null) return;

        for (Entity enemy : enemies)
            enemy.draw(g2);
    }
}
