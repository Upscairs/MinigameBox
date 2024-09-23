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

        get().addDefault("managing.success-wizard-creation", "The arena setup has been started. Please select a Postion 1 for the arena bounds. Use /minigame setpos");
        get().addDefault("managing.success-pos1-placed", "Position 1 is set. Next, please select a postion 2...");
        get().addDefault("managing.success-pos2-placed", "Position 2 is set. Next, please select a throwback block outside of the arena...");
        get().addDefault("managing.success-outpos-placed-arena-created", "Outside Block set. Creating arena...");
        get().addDefault("managing.success-setup-canceled", "Canceled creation.");
        get().addDefault("managing.success-editing-started", "You are now editing the arena. Input your new value with /minigame edit-input <value>");
        get().addDefault("managing.success-edited-value", "Value got updated. You might need to restart/refresh the arena for changes to be applied.");
        get().addDefault("managing.success-editing-ended", "You left the arena editing mode.");
        get().addDefault("managing.error-outpos-in-bounds", "Error. Please place the outside block outside of the arena.");
        get().addDefault("managing.error-already-creating", "You cant create a new arena, because the previous setup is still pending. Finish the setup or use /minigame setup-cancel");
        get().addDefault("managing.error-unknown-gametype", "The gamemode you specified is unknown.");
        get().addDefault("managing.error-duplicate-name", "An arena with that name already exists. Please choose another name.");
        get().addDefault("managing.error-no-wizard-running", "You need to start a setup with /minigame create <Gamemode> <Name> first");
        get().addDefault("managing.error-no-permission", "You dont have permission to do that.");
        get().addDefault("managing.error-game-not-found", "Theres no game with that name.");
        get().addDefault("managing.error-create-wrong-syntax", "Insufficient parameters. Use /minigame create <Gamemode> <Name>");
        get().addDefault("managing.error-edit-wrong-syntax", "Insufficient parameters. Use /minigame edit <Name>");
        get().addDefault("managing.error-game-running", "You cant do this while the game is running");
        get().addDefault("managing.error-already-editing", "You are already editing an arena.");
        get().addDefault("managing.error-not-editing", "You arent editing anything at the moment.");
        get().addDefault("managing.error-edit-illegal-value-input", "Error while parsing input: ");

        get().addDefault("game.success-queue-joined", "You are queued. Your position is: ");
        get().addDefault("game.success-queue-left", "You left the queue.");
        get().addDefault("game.error-already-in-game", "You are already in a game.");
        get().addDefault("game.error-queue-closed", "You cant join this game. The queue is closed.");
        get().addDefault("game.error-not-in-game", "Youre not in a game.");
        get().addDefault("game.info-out-of-game", "Youre out of the game.");

        get().addDefault("general.error-send-from-console", "Use this command as a player.");
        get().addDefault("general.error-subcommand-not-found", "Subcommand not found.");

        get().addDefault("broadcast.info-start-game-countdown", "Enough players for next round. Game starts in ");
        get().addDefault("broadcast.info-start-game-final", "Good luck. Setup time ends in ");
        get().addDefault("broadcast.info-setup-time-over", "Setup time is over.");
        get().addDefault("broadcast.info-game-end-winner-announce", "The game is over. Congratulations to ");
        get().addDefault("broadcast.issue-start-aborted-playercount", "The game start got aborted due to players leaving the queue.");
        get().addDefault("broadcast.issue-game-end-force", "The game was canceled by a Game Manager.");

        get().options().copyDefaults(true);
        save();
    }
}
