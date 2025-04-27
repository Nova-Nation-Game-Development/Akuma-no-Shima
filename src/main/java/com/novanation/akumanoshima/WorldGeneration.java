package com.novanation.akumanoshima;


import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class WorldGeneration {

    private static HashMap<Integer, Tile> tileMap = new HashMap<>();
    private static HashMap<Integer, Tile> tileDepthMap = new HashMap<>();
    private static HashMap<Integer, Chunk> chunkMap = new HashMap<>();

    private static ForestLevel forestLevel;
    private static VolcanicLevel volcanicLevel;
    private static BlizzardLevel blizzardLevel;

    private static Level currentLevel;
    
    public static void reset()
    {
        forestLevel = null;
        volcanicLevel = null;
        blizzardLevel = null;

        if (tileMap != null) tileMap.clear();
        if (tileDepthMap != null) tileDepthMap.clear();
        if (chunkMap != null) chunkMap.clear();
        
        tileMap = new HashMap<>();
        tileDepthMap = new HashMap<>();
        chunkMap = new HashMap<>();
    }

    public static void generateLevel(GamePanel panel, WorldType world)
    {
        switch (world) {
            case FOREST -> {
                forestLevel = new ForestLevel(panel);
                forestLevel.createLevel();

                tileMap = forestLevel.getTileDictionary();
                tileDepthMap = forestLevel.getTileDepthDictionary();
                chunkMap = forestLevel.getChunkDictionary();

                currentLevel = forestLevel;
            }

            case VOLCANIC -> { 
                volcanicLevel = new VolcanicLevel(panel);
                volcanicLevel.createLevel();

                tileMap = volcanicLevel.getTileDictionary();
                tileDepthMap = volcanicLevel.getTileDepthDictionary();
                chunkMap = volcanicLevel.getChunkDictionary();

                currentLevel = volcanicLevel;
            }

            case BLIZZARD -> {
                blizzardLevel = new BlizzardLevel(panel);
                blizzardLevel.createLevel();

                tileMap = blizzardLevel.getTileDictionary();
                tileDepthMap = blizzardLevel.getTileDepthDictionary();
                chunkMap = blizzardLevel.getChunkDictionary();

                currentLevel = blizzardLevel;
            }

            case END -> { 
                generateEndLevel();
            }
        }
    }

    public static int getTileLength() { return currentLevel.getTileLength(); }
    public static Chunk getChunk(int xPos) 
    {
        if (chunkMap.get(xPos) != null)
            return chunkMap.get(xPos);

        return null;
    }

    private static void generateEndLevel() {}

    public static WorldType getRandomWorld()
    {
        Random random = new Random();
        int randWorld = random.nextInt(100);

        return WorldType.VOLCANIC;
        
        // if (randWorld < 40)
        //     return WorldType.FOREST;
        // else
        //     if (randWorld < 70)
        //         return WorldType.BLIZZARD; // 30% Chance of snow
        //     else
        //         return WorldType.VOLCANIC; // 30% Chance of the hardest possible level
    }

    public static Tile getTile(int key) { return tileMap.get(key); }
    public static Collection<Tile> getAllTiles() { return tileMap.values(); }
    public static Collection<Tile> getAllDepthTiles() { return tileDepthMap.values(); }
    public static Collection<Chunk> getAllChunks() { return chunkMap.values(); }

    // This is called in InputHandler
    public static void move(int worldSpeed)
    {
        if (tileMap.values() == null) return;

        for (Tile tile : tileMap.values())
            tile.move(worldSpeed);

        if (tileDepthMap.values() == null) return;

        for (Tile tile : tileDepthMap.values())
            tile.move(worldSpeed);

        for (Chunk chunk : chunkMap.values())
            chunk.move(worldSpeed);
    }
}
