package dev.upscairs.minigameBox.base_functionality.managing.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SettingsFile {

    private static File file;
    private static FileConfiguration customFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("MinigameBox").getDataFolder(), "settings.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        customFile = YamlConfiguration.loadConfiguration(file);

    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().warning("Could not save file " + file.getAbsolutePath());
        }
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void setConfig(FileConfiguration config) {
        customFile = config;
    }

    public static void defaults() {
        get().addDefault("listClickAction", "tp");
        get().addDefault("arena-broadcast", "true");
        get().addDefault("broadcast-range", 75);

        get().options().copyDefaults(true);
        save();
    }


}
