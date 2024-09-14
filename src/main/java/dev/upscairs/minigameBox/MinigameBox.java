package dev.upscairs.minigameBox;

import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import dev.upscairs.minigameBox.config.ArenaRegisterFile;
import dev.upscairs.minigameBox.config.MessagesConfig;
import dev.upscairs.minigameBox.events.handling.GuiInteractionHandler;
import dev.upscairs.minigameBox.events.handling.PlayerDisconnectHandler;
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
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("empty", "empty");
        saveDefaultConfig();

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
        getServer().getPluginManager().registerEvents(new PlayerDisconnectHandler(), this);
        getServer().getPluginManager().registerEvents(new GuiInteractionHandler(), this);
    }

}


/*TODO
   (1) Info to run /minigame refresh
   (2) protection for edit while game running
   (3) add reload file command
   (4) tab completor
   (5) list of arenas + tp, join etc



 */