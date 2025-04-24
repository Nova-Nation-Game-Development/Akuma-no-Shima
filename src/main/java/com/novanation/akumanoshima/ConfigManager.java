package com.novanation.akumanoshima;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConfigManager {

    private static final Path CONFIG_PATH = Paths.get(System.getProperty("user.dir"), "user-config", "user-settings.config");

    public static void saveConfig(Config config)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                gson.toJson(config, writer);
            }
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    public static Config loadConfig()
    {
        Gson gson = new Gson();

        if (Files.notExists(CONFIG_PATH))
        {
            // Create and save the default config if no file was found
            System.out.println("Config file not found. Creating default config...");
            
            Config defaultConfig = new Config();
            saveConfig(defaultConfig);
            return defaultConfig;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            return gson.fromJson(reader, Config.class);
        } catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
            return new Config();
        }
    }
}
