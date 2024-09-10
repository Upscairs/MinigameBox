package dev.upscairs.minigameBox;

import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import dev.upscairs.minigameBox.config.ArenaRegister;
import dev.upscairs.minigameBox.events.handling.PlayerJoinQueueHandler;
import dev.upscairs.minigameBox.events.handling.PlayerLeaveMinigameHandler;
import dev.upscairs.minigameBox.events.handling.SpleefMonitoring;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinigameBox extends JavaPlugin {

    @Override
    public void onEnable() {

        setupFiles();

        registerCommands();
        registerEvents();

        GameRegister.setPlugin(this);
        GameRegister.loadGames();

    }

    @Override
    public void onDisable() {
        GameRegister.shutdownGames();
    }

    public void setupFiles() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        ArenaRegister.setup();
        ArenaRegister.defaults();
    }

    public void registerCommands() {
        getCommand("minigame").setExecutor(new MinigameCommand());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoinQueueHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveMinigameHandler(), this);
        getServer().getPluginManager().registerEvents(new SpleefMonitoring(), this);
    }

}
