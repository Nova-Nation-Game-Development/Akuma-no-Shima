package src;

import java.util.HashMap;
import java.util.Random;

public class WorldGeneration {

    private static Tile[] tiles;
    private static final HashMap<Integer, Tile> tileMap = new HashMap<>();
    
    public static void generateLevel(int worldLength, WorldType world)
    {
        

        // Once all the tiles have been created
        generateHashMap();
    }

    public static WorldType getRandomWorld()
    {
        Random random = new Random();
        int randWorld = random.nextInt(100);

        if (randWorld < 50)
            return WorldType.FOREST;
        else
            if (randWorld < 80)
                return WorldType.BLIZZARD; // 30% Chance of snow
            else
                return WorldType.VOLCANIC; // 20% Chance of the hardest possible level
    }

    private static void generateHashMap()
    {
        if (tiles == null) return;

        for (Tile tile: tiles)
            tileMap.put(tile.getX(), tile);
    } 

    public static Tile getTile(int key) { return tileMap.get(key); }

    public static void move(int worldSpeed)
    {
        if (tiles == null) return;

        for (Tile tile: tiles)
            tile.move(worldSpeed);
    }
}
