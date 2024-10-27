package dev.upscairs.minigameBox.base_functionality.managing.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessagesConfig {

    private static File file;
    private static FileConfiguration customFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("MinigameBox").getDataFolder(), "messages-config.yml");

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

        get().addDefault("managing.success-wizard-creation", "§l§aM§bB§r§7 The arena setup has been started. Please select a Postion 1 for the arena bounds. Use /minigame setpos");
        get().addDefault("managing.success-pos1-placed", "§l§aM§bB§r§7 Position 1 is set. Next, please select a postion 2...");
        get().addDefault("managing.success-pos2-placed", "§l§aM§bB§r§7 Position 2 is set. Next, please select a throwback block outside of the arena...");
        get().addDefault("managing.success-outpos-placed-arena-created", "§l§aM§bB§r§7 Outside Block set. Creating arena...");
        get().addDefault("managing.success-setup-canceled", "§l§aM§bB§r§7 Canceled creation.");
        get().addDefault("managing.success-editing-started", "§l§aM§bB§r§7 You are now editing the arena. Input your new value with /minigame edit-input <value>");
        get().addDefault("managing.success-edited-value", "§l§aM§bB§r§7 Value got updated. You might need to restart/refresh the arena for changes to be applied.");
        get().addDefault("managing.success-editing-ended", "§l§aM§bB§r§7 You left the arena editing mode.");
        get().addDefault("managing.success-arena-deleted", "§l§aM§bB§r§7 The arena got deleted.");
        get().addDefault("managing.success-arena-reloaded", "§l§aM§bB§r§7 Reloading arena...");
        get().addDefault("managing.success-queue-flushed", "§l§aM§bB§r§7 Queue flushed.");
        get().addDefault("managing.error-outpos-in-bounds", "§l§aM§bB§r§c Error. Please place the outside block outside of the arena.");
        get().addDefault("managing.error-already-creating", "§l§aM§bB§r§c You cant create a new arena, because the previous setup is still pending. Finish the setup or use /minigame setup-cancel");
        get().addDefault("managing.error-unknown-gametype", "§l§aM§bB§r§c The gamemode you specified is unknown.");
        get().addDefault("managing.error-duplicate-name", "§l§aM§bB§r§c An arena with that name already exists. Please choose another name.");
        get().addDefault("managing.error-no-wizard-running", "§l§aM§bB§r§c You need to start a setup with /minigame create <Gamemode> <Name> first");
        get().addDefault("managing.error-no-permission", "§l§aM§bB§r§c You dont have permission to do that.");
        get().addDefault("managing.error-game-not-found", "§l§aM§bB§r§c Theres no game with that name.");
        get().addDefault("managing.error-create-wrong-syntax", "§l§aM§bB§r§c Insufficient parameters. Use /minigame create <Gamemode> <Name>");
        get().addDefault("managing.error-edit-wrong-syntax", "§l§aM§bB§r§c Insufficient parameters. Use /minigame edit <Name>");
        get().addDefault("managing.error-game-running", "§l§aM§bB§r§c You cant do this while the game is running");
        get().addDefault("managing.error-already-editing", "§l§aM§bB§r§c You are already editing an arena.");
        get().addDefault("managing.error-not-editing", "§l§aM§bB§r§c You arent editing anything at the moment.");
        get().addDefault("managing.error-edit-illegal-value-input", "§l§aM§bB§r§c Error while parsing input: ");

        get().addDefault("game.success-queue-joined", "§l§aM§bB§r§7 You are queued. Your position is: ");
        get().addDefault("game.success-queue-left", "§l§aM§bB§r§7 You left the queue.");
        get().addDefault("game.error-already-in-game", "§l§aM§bB§r§c You are already in a game.");
        get().addDefault("game.error-queue-closed", "§l§aM§bB§r§c You cant join this game. The queue is closed.");
        get().addDefault("game.error-not-in-game", "§l§aM§bB§r§c Youre not in a game.");
        get().addDefault("game.info-out-of-game", "§l§aM§bB§r§7 Youre out of the game.");
        get().addDefault("game.info-removed-from-queue", "§l§aM§bB§r§7 You got removed from the queue.");

        get().addDefault("general.error-send-from-console", "§l§aM§bB§r§c Use this command as a player.");
        get().addDefault("general.error-subcommand-not-found", "§l§aM§bB§r§c Subcommand not found.");

        get().addDefault("broadcast.info-start-game-countdown", "§l§aM§bB§r§7 Enough players for next round. Game starts in ");
        get().addDefault("broadcast.info-start-game-final", "§l§aM§bB§r§7 Good luck. Setup time ends in ");
        get().addDefault("broadcast.info-setup-time-over", "§l§aM§bB§r§7 Setup time is over.");
        get().addDefault("broadcast.info-game-end-winner-announce", "§l§aM§bB§r§7 The game is over. Congratulations to ");
        get().addDefault("broadcast.issue-start-aborted-playercount", "§l§aM§bB§r§c The game start got aborted due to players leaving the queue.");
        get().addDefault("broadcast.issue-game-end-force", "§l§aM§bB§r§c The game was canceled by a Game Manager.");

        get().addDefault("rewards.book-title", "Winner of %m");
        get().addDefault("rewards.book-author", "%a");
        get().addDefault("rewards.book-text", "Congratulations!%n%nYou, %p, won a game of %m against %l.%n%nTime: %t");

        get().options().copyDefaults(true);
        save();
    }
}
