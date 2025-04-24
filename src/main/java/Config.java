

public class Config {

    // Graphics settings
    private boolean fullscreenMode;
    private int resolutionWidth;
    private int resolutionHeight;

    // Game state
    private int level;
    private WorldType worldType;
    private String currentWeapon;
    private int currentAmmo;

    // Audio settings
    private float sfxVolume;
    private float menuVolume;
    private float musicVolume;
    private float masterVolume;

    // Default constructor
    public Config() {
        // Set default values
        this.fullscreenMode = false;
        this.resolutionWidth = 1366;
        this.resolutionHeight = 768;
        this.level = 1;
        this.worldType = WorldType.FOREST;
        this.currentWeapon = "Assault";
        this.currentAmmo = 30;
        this.sfxVolume = 1.0f;
        this.menuVolume = 1.0f;
        this.musicVolume = 1.0f;
        this.masterVolume = 1.0f;
    }

    // Accessors and mutators

    // Graphics
    public boolean isFullscreenMode() { return fullscreenMode; }
    public void setFullscreenMode(boolean fullscreenMode) { this.fullscreenMode = fullscreenMode; }

    public int getResolutionWidth() { return resolutionWidth; }
    public void setResolutionWidth(int resolutionWidth) { this.resolutionWidth = resolutionWidth; }

    public int getResolutionHeight() { return resolutionHeight; }
    public void setResolutionHeight(int resolutionHeight) { this.resolutionHeight = resolutionHeight; }

    // Game state
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public WorldType getWorldType() { return worldType; }
    public void setWorldType(WorldType worldType) { this.worldType = worldType; }

    public String getCurrentWeapon() { return currentWeapon; }
    public void setCurrentWeapon(String currentWeapon) { this.currentWeapon = currentWeapon; }

    public int getCurrentAmmo() { return currentAmmo; }
    public void setCurrentAmmo(int currentAmmo) { this.currentAmmo = currentAmmo; }

    // Audio
    public float getSfxVolume() { return sfxVolume; }
    public void setSfxVolume(float sfxVolume) { this.sfxVolume = sfxVolume; }

    public float getMenuVolume() { return menuVolume; }
    public void setMenuVolume(float menuVolume) { this.menuVolume = menuVolume; }

    public float getMusicVolume() { return musicVolume; }
    public void setMusicVolume(float musicVolume) { this.musicVolume = musicVolume; }

    public float getMasterVolume() { return masterVolume; }
    public void setMasterVolume(float masterVolume) { this.masterVolume = masterVolume; }
}
