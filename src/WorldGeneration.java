package src;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class WorldGeneration {

    private static HashMap<Integer, Tile> tileMap = new HashMap<>();
    private static HashMap<Integer, Tile> tileDepthMap = new HashMap<>();
    private static HashMap<Integer, Chunk> chunkMap = new HashMap<>();

    private static ForestLevel forestLevel;
    
    public static void generateLevel(GamePanel panel, WorldType world)
    {
        switch (world) {
            case WorldType.FOREST -> {
                forestLevel = new ForestLevel(panel);
                forestLevel.createLevel();

                tileMap = forestLevel.getTileDictionary();
                tileDepthMap = forestLevel.getTileDepthDictionary();
                chunkMap = forestLevel.getChunkDictionary();
            }

            case WorldType.VOLCANIC -> {
            }

            case WorldType.BLIZZARD -> {
            }

            case WorldType.END -> {
            }
        }
    }

    public static int getTileLength() { return forestLevel.getTileLength(); }
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

        return WorldType.FOREST;

        // This will remain commented until I draw the remaining tiles

        // if (randWorld < 50)
        //     return WorldType.FOREST;
        // else
        //     if (randWorld < 80)
        //         return WorldType.BLIZZARD; // 30% Chance of snow
        //     else
        //         return WorldType.VOLCANIC; // 20% Chance of the hardest possible level
    }

    public static Tile getTile(int key) { return tileMap.get(key); }
    public static Collection<Tile> getAllTiles() { return tileMap.values(); }
    public static Collection<Tile> getAllDepthTiles() { return tileDepthMap.values(); }

    // This is called in InputHandler
    public static void move(int worldSpeed)
    {
        if (tileMap.values() == null) return;

        for (Tile tile : tileMap.values())
            tile.move(worldSpeed);

        if (tileDepthMap.values() == null) return;

        for (Tile tile : tileDepthMap.values())
            tile.move(worldSpeed);

        // for (Chunk chunk : chunkMap.values())
        //     chunk.move(worldSpeed);
    }
}
