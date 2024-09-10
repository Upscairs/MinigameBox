package dev.upscairs.minigameBox;

import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import dev.upscairs.minigameBox.config.ArenaRegisterFile;
import dev.upscairs.minigameBox.config.MessagesConfig;
import dev.upscairs.minigameBox.events.handling.PlayerQueueGameActivityHandler;
import dev.upscairs.minigameBox.events.handling.SpleefMonitoring;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinigameBox extends JavaPlugin {

    public static MinigameBox instance;

    @Override
    public void onEnable() {

        instance = this;

        setupFiles();

        registerCommands();
        registerEvents();

        GameRegister.loadGames();

    }

    @Override
    public void onDisable() {
        GameRegister.shutdownGames();
    }

    public void setupFiles() {
        //getConfig().options().copyDefaults(true);
        //saveDefaultConfig();

        ArenaRegisterFile.setup();
        ArenaRegisterFile.defaults();

        MessagesConfig.setup();
        MessagesConfig.defaults();
    }

    public void registerCommands() {
        getCommand("minigame").setExecutor(new MinigameCommand());
    }

    public void registerEvents() {                                                                  
        getServer().getPluginManager().registerEvents(new PlayerQueueGameActivityHandler(), this);
        getServer().getPluginManager().registerEvents(new SpleefMonitoring(), this);
    }

}
