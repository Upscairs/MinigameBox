package dev.upscairs.minigameBox;

import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import dev.upscairs.minigameBox.config.ArenaRegisterFile;
import dev.upscairs.minigameBox.config.MessagesConfig;
import dev.upscairs.minigameBox.config.SettingsFile;
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

        SettingsFile.setup();
        SettingsFile.defaults();
    }

    public void registerCommands() {
        getCommand("minigame").setExecutor(new MinigameCommand());
        getCommand("minigame").setTabCompleter(new MinigameTabCompleter());
    }

    public void registerEvents() {                                                                  
        getServer().getPluginManager().registerEvents(new PlayerQueueGameActivityHandler(), this);
        getServer().getPluginManager().registerEvents(new SpleefMonitoring(), this);
        getServer().getPluginManager().registerEvents(new PlayerDisconnectHandler(), this);
        getServer().getPluginManager().registerEvents(new GuiInteractionHandler(), this);
    }

}


/*TODO
   - Refactor queue
   - Remove metadata
   - move players ingame at the end of startup
   - Reward
   - Remove player from spleef if out of bounds
   - braodcast messages
   - economy LATER
   - add reload file command LATER
   - Arena info gui, view queue LATER
 */

/*

+++Creating new arena types+++
1. Create Classes: Arena, Game, EditGui (arg[0] is usually players uuid)
2. Make them extend their superclasses
3. New entry in Game types Enum: Define with classes and default args
4. Implement fuctionality in classes: +Settings items in GuiClickHandler, ...
5. Implement Gameplay, use EventListeners for Controlling

 */