package src;

import java.util.HashMap;
import java.util.Random;

public class ForestLevel implements Level {

    private static final HashMap<Integer, Tile> tileMap = new HashMap<>();
    private static final HashMap<Integer, Tile> tileDepthMap = new HashMap<>();
    private static final HashMap<Integer, Chunk> chunkMap = new HashMap<>();

    // Level presets
    private static final int TILE_LENGTH = 64;                  // Length of a tile in pixels
    private static final int WORLD_LENGTH = 30;                 // Total amount of tiles that can be made // 64 x 30 = 1920 pixels * 2
    private static final int SPAWN_LENGTH = 3;                  // Total amount of tiles dedicated to generating spawn
    private static final int BASE_HEIGHT = 64;                  // Starting height for world generation
    private static final int ELEVATION_LENGTH = 3;              // Total guaranteed tiles placed on elevation
    private static final int ELEVATION_PERCENT = 60;

    private static final int MAIN_TILE_PERCENT = 40;            // Represents grass, lava stone, snowy grass
    private static final int SECONDARY_TILE_PERCENT = 50;       // Represents dirt, stone, snow pile
    private static final int TERTIARY_TILE_PERCENT = 75;        // Represents water, lava, ice
    private static final int VOID_PERCENT = 100;                // Represents air gaps that can cause an individual to fall

    private static final int FIRST_HEIGHT_PRESET = 20;
    private static final int SECOND_HEIGHT_PRESET = 40;
    private static final int THRID_HEIGHT_PRESET = 60;
    private static final int FOURTH_HEIGHT_PRESET = 100;

    // Temporary level presets
    private int newMainPercent = MAIN_TILE_PERCENT;
    private int newSecondaryPercent = SECONDARY_TILE_PERCENT;
    private int newTertiaryPercent = TERTIARY_TILE_PERCENT;
    private int newVoidPercent = VOID_PERCENT;

    private int yPos = BASE_HEIGHT;                     // Current height at which the random terrain is generated
    private int tempTileCount = 0;                      // Keep track of any extra tiles added
    private int elevationDistance = 0;                  // How many consecutive tiles is a single elevation
    private int previousDistance = 0;
    private int previousHeight = 0;
    private int randChance = ELEVATION_PERCENT;         // Chance of continuing the generation at the current elevation
    private int tileDepth = 0;
    private boolean isFirstElevation = true;

    // Generation variables
    private static final int SCALE = 2;

    private int currentTile = 0;
    private int successiveTiles = 0;
    
    private TileType previousTile;

    // Game Panel
    private final GamePanel panel;
    private int panelHeight;

    public ForestLevel(GamePanel panel) { this.panel = panel; }

    public int getTileLength() { return TILE_LENGTH; }

    @Override
    public HashMap<Integer, Tile> getTileDictionary() { return tileMap; }
    @Override
    public HashMap<Integer, Tile> getTileDepthDictionary() { return tileDepthMap; }
    @Override
    public HashMap<Integer, Chunk> getChunkDictionary() { return chunkMap; }

    private void createChunk(int elevation, int tileCount)
    {
        int newTileHeight = TILE_LENGTH * elevation;

        if (previousHeight != TILE_LENGTH)
        {
            if (!isFirstElevation)
            {
                Chunk newChunk = new Chunk((tileCount) * TILE_LENGTH, yPos, (previousDistance + 1), elevation, TILE_LENGTH);
                chunkMap.put((tileCount) * TILE_LENGTH, newChunk);

                System.out.println(newChunk);
            }
            
            previousHeight = newTileHeight;
        }
        else
        {
            if (!isFirstElevation)
            {
                Chunk newChunk = new Chunk((tileCount) * TILE_LENGTH, yPos, (previousDistance + 1), elevation, TILE_LENGTH);
                chunkMap.put((tileCount) * TILE_LENGTH, newChunk);

                System.out.println(newChunk);
            }
        }
    }
    
    @Override
    public void setElevation(int randHeight)
    {
        // Set the spawning to chance after ELEVATION_LENGTH blocks
        if (elevationDistance > ELEVATION_LENGTH)
        {
            Random random = new Random();
            int rand = random.nextInt(100);

            if (rand < randChance)
                randChance -= 10;
            else
            {
                randChance = ELEVATION_PERCENT;
                previousDistance = elevationDistance;
                elevationDistance = 0;
                return;
            }
        }

        if (elevationDistance == 0)
        {
            int tileCount = currentTile - previousDistance - 1;
            if (!isFirstElevation)
                System.out.println("Tile: " + tileCount); // This should only be called once per chunk call

            if (randHeight < FIRST_HEIGHT_PRESET)
            {
                yPos = panelHeight;
                createChunk(1, tileCount);
            }
    
            if (randHeight >= FIRST_HEIGHT_PRESET && randHeight < SECOND_HEIGHT_PRESET)
            {
                yPos = panelHeight - (TILE_LENGTH);
                createChunk(2, tileCount);
            }
    
            if (randHeight >= SECOND_HEIGHT_PRESET && randHeight < THRID_HEIGHT_PRESET)
            {
                yPos = panelHeight - (TILE_LENGTH * 2);
                createChunk(3, tileCount);
            }
    
            if (randHeight >= THRID_HEIGHT_PRESET && randHeight < FOURTH_HEIGHT_PRESET)
            {
                yPos = panelHeight - (TILE_LENGTH * 3);
                createChunk(4, tileCount);
            }

            elevationDistance = 0;
        }

        if (isFirstElevation)
            isFirstElevation = false;

        elevationDistance++;
    }

    @Override
    public void fillAirGaps(TileType tileType)
    {
        if (yPos == panelHeight) return; // At base level

        int levelCount = (panelHeight - yPos) / TILE_LENGTH;
        int tileOffset = currentTile - 1;

        // Separated for future manipulation
        switch (tileType)
        {
            case TileType.PRIMARY -> {
                for (int i = 1; i <= levelCount; i++)
                    iterateGaps(tileOffset, i, TileType.SECONDARY);
            }

            case TileType.SECONDARY -> {
                for (int i = 1; i <= levelCount; i++)
                    iterateGaps(tileOffset, i, TileType.SECONDARY);
            }

            case TileType.TERTIARY -> {
                for (int i = 1; i <= levelCount; i++)
                    iterateGaps(tileOffset, i, TileType.SECONDARY);
            }

            case TileType.VOID -> { }
        }
    }

    private void iterateGaps(int tileOffset, int i, TileType tileType)
    {
        Tile newTile = new Tile(panel, (tileOffset * TILE_LENGTH), yPos + (i * TILE_LENGTH), TILE_LENGTH, tileType, WorldType.FOREST);
        tileDepthMap.put(tileDepth, newTile);
        tileDepth++;
    }

    // TODO: Finish platform stuff
    public void generateFloatingPlatform()
    {
        
    }

    @Override
    public void createLevel()
    {
        panelHeight = panel.getHeight() - BASE_HEIGHT - (TILE_LENGTH / 2);

        // For percentage resets; this means other tiles can have a higher percentage with a weighting > 100
        // The world moves around the player so it must be larger than the visible area
        while (currentTile < (WORLD_LENGTH * 2))
        {
            if (currentTile < SPAWN_LENGTH) // Setup Spawn Area
            {
                Tile newTile = new Tile(panel, (currentTile * TILE_LENGTH), panelHeight, TILE_LENGTH, TileType.PRIMARY, WorldType.FOREST);
                tileMap.put(newTile.getX(), newTile);

                previousTile = TileType.PRIMARY;

                successiveTiles++;
                currentTile++;

                if (currentTile + 1 > SPAWN_LENGTH)
                {
                    Chunk newChunk = new Chunk(0, panelHeight, 3, 1, TILE_LENGTH);
                    chunkMap.put(0, newChunk);
                    
                    System.out.println("\n\n" + newChunk); // Spawn chunk
                }
            }
            else
            {
                Random random = new Random();

                int randTile = random.nextInt(100);
                int randHeight = random.nextInt(100);

                // Generate a random elevation
                setElevation(randHeight);

                // Main Tiles
                if (randTile < newMainPercent)
                {
                    setMainTile();
                    fillAirGaps(TileType.PRIMARY);
                    continue;
                }

                // Secondary Tiles
                if (randTile >= newMainPercent && randTile < newSecondaryPercent)
                {
                    setSecondaryTile();
                    fillAirGaps(TileType.SECONDARY);
                    continue;
                }

                // Tertiary Tiles
                if (randTile >= newSecondaryPercent && randTile < newTertiaryPercent)
                {
                    setTertiaryTile();
                    fillAirGaps(TileType.TERTIARY);
                    continue;
                }

                // Air tiles
                if (randTile >= newTertiaryPercent && randTile < newVoidPercent)
                    setVoidTile();
            }
        }

        // In the very rare case where the whole world is flat
        if (chunkMap == null)
        {
            // Create a chunk of the entire world
            Chunk newChunk = new Chunk(0, yPos, currentTile, 1, TILE_LENGTH);
            chunkMap.put((currentTile + 1) * TILE_LENGTH, newChunk);
        }
    }

    @Override
    public void setMainTile()
    {
        // Compare previous tile
        if (previousTile == TileType.PRIMARY)
        {
            Tile newTile = new Tile(panel, (currentTile * 64), yPos, TILE_LENGTH, TileType.PRIMARY, WorldType.FOREST);
            tileMap.put(newTile.getX(), newTile);

            // Increase weighting for main percent by an exponential value
            if (successiveTiles <= 3)
            {
                // Reset percentages for ONLY connected tiles
                if (successiveTiles <= 1)
                {
                    newMainPercent = MAIN_TILE_PERCENT;
                    newSecondaryPercent = SECONDARY_TILE_PERCENT;
                    newTertiaryPercent = TERTIARY_TILE_PERCENT;
                }
                
                newMainPercent += (successiveTiles * SCALE);
            }   
            else
            {
                // Reset successive percentages
                newMainPercent = MAIN_TILE_PERCENT;
                newSecondaryPercent = SECONDARY_TILE_PERCENT;

                // Raise the other percentages
                newMainPercent -= 20;
                newSecondaryPercent += 10;
                newTertiaryPercent += 10;
            }

            successiveTiles++;
        }
        else
        {
            // New set of main tiles (Not just a single patch)
            newMainPercent += 20;
            newSecondaryPercent -= 20;

            Tile newTile = new Tile(panel, (currentTile * 64), yPos, TILE_LENGTH, TileType.PRIMARY, WorldType.FOREST);
            tileMap.put(newTile.getX(), newTile);

            previousTile = TileType.PRIMARY;
            successiveTiles = 0;
        }

        currentTile += 1 + tempTileCount;
        tempTileCount = 0;
    }

    @Override
    public void setSecondaryTile()
    {
        // Compare previous tile
        if (previousTile == TileType.SECONDARY)
        {
            Tile newTile = new Tile(panel, (currentTile * 64), yPos, TILE_LENGTH, TileType.SECONDARY, WorldType.FOREST);
            tileMap.put(newTile.getX(), newTile);

            // Increase weighting for main percent by an exponential value
            if (successiveTiles <= 3)
            {
                // Reset percentages for ONLY connected tiles
                if (successiveTiles <= 1)
                {
                    newMainPercent = MAIN_TILE_PERCENT;
                    newSecondaryPercent = SECONDARY_TILE_PERCENT;
                    newTertiaryPercent = TERTIARY_TILE_PERCENT;
                }
                
                newSecondaryPercent += (successiveTiles * SCALE);
            }   
            else
            {
                // Reset successive percentages
                newMainPercent = MAIN_TILE_PERCENT;
                newSecondaryPercent = SECONDARY_TILE_PERCENT;

                // Raise the other percentages
                newSecondaryPercent -= 20;
                newTertiaryPercent += 10;
                newVoidPercent += 10;
            }

            successiveTiles++;
        }
        else
        {
            // New set of main tiles (Not just a single patch)
            newSecondaryPercent += 20;
            newMainPercent -= 20;

            Tile newTile = new Tile(panel, (currentTile * 64), yPos, TILE_LENGTH, TileType.SECONDARY, WorldType.FOREST);
            tileMap.put(newTile.getX(), newTile);

            previousTile = TileType.SECONDARY;
            successiveTiles = 0;
        }

        currentTile += 1 + tempTileCount;
        tempTileCount = 0;
    }

    @Override
    public void setTertiaryTile()
    {
        // Must not place water after air
        if (previousTile == TileType.VOID)
        {
            Random random = new Random();
            int randomInt = random.nextInt(2);

            if (randomInt == 0) setMainTile(); else setSecondaryTile();
            return;
        }

        // Compare previous tile
        if (previousTile == TileType.TERTIARY)
        {
            Tile newTile = new Tile(panel, (currentTile * 64), yPos, TILE_LENGTH, TileType.TERTIARY, WorldType.FOREST);
            tileMap.put(newTile.getX(), newTile);

            // Increase weighting for main percent by an exponential value
            if (successiveTiles <= 8)
            {
                // Reset percentages for ONLY connected tiles
                if (successiveTiles <= 1)
                {
                    newTertiaryPercent = TERTIARY_TILE_PERCENT;
                    newVoidPercent = VOID_PERCENT;
                }
                
                newTertiaryPercent += (successiveTiles * SCALE * 10);
            }   
            else
            {
                // Reset successive percentages
                newTertiaryPercent = TERTIARY_TILE_PERCENT;
                newVoidPercent = VOID_PERCENT;

                // Raise the other percentages
                newTertiaryPercent -= 10;
                newVoidPercent += 10;
            }

            successiveTiles++;
        }
        else
        {
            // New set of main tiles (Not just a single patch)
            newTertiaryPercent += 20;
            newVoidPercent -= 20;

            Tile newTile = new Tile(panel, (currentTile * 64), yPos, TILE_LENGTH, TileType.TERTIARY, WorldType.FOREST);
            tileMap.put(newTile.getX(), newTile);

            previousTile = TileType.TERTIARY;
            successiveTiles = 0;
        }

        currentTile += 1 + tempTileCount;
        tempTileCount = 0;
    }

    @Override
    public void setVoidTile()
    {
        // Must not place air after water
        if (previousTile == TileType.TERTIARY)
        {
            Random random = new Random();
            int randomInt = random.nextInt(2);

            if (randomInt == 0) setMainTile(); else setSecondaryTile();
            return;
        }

        // Compare previous tile
        if (previousTile == TileType.VOID)
        {
            // Increase weighting for main percent by an exponential value
            if (successiveTiles <= 3)
            {
                // Reset percentages for ONLY connected tiles
                if (successiveTiles <= 1)
                {
                    newMainPercent += MAIN_TILE_PERCENT;
                    newVoidPercent = VOID_PERCENT;
                }
                
                // Significantly increase the chances of spawning a consecutive air gap
                newVoidPercent = 90;
                newMainPercent = 95;
                newSecondaryPercent = 98;
                newTertiaryPercent = 100;
            }   
            else
            {
                // Reset successive percentages
                newVoidPercent = VOID_PERCENT;

                // Raise the other percentages
                newMainPercent = MAIN_TILE_PERCENT + 10; // 40 -> 50
                newSecondaryPercent = SECONDARY_TILE_PERCENT + 10; // 50 -> 60
                newTertiaryPercent = TERTIARY_TILE_PERCENT + 15; // 75 -> 90
                newVoidPercent = VOID_PERCENT; // 100
            }

            successiveTiles++;
        }
        else
        {
            newVoidPercent += 20;

            previousTile = TileType.VOID;
            successiveTiles = 0;
        }

        currentTile += 1 + tempTileCount;
        tempTileCount = 0;
    }
}
