package dev.upscairs.minigameBox;

import dev.upscairs.minigameBox.base_functionality.coms_and_guis.MinigameCommand;
import dev.upscairs.minigameBox.base_functionality.coms_and_guis.MinigameTabCompleter;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import dev.upscairs.minigameBox.base_functionality.managing.config.ArenaRegisterFile;
import dev.upscairs.minigameBox.base_functionality.managing.config.MessagesConfig;
import dev.upscairs.minigameBox.base_functionality.managing.config.SettingsFile;
import dev.upscairs.minigameBox.base_functionality.event_handlers.GuiInteractionHandler;
import dev.upscairs.minigameBox.base_functionality.event_handlers.PlayerDisconnectHandler;
import dev.upscairs.minigameBox.minigames.spleef.SpleefMonitoring;
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
        getServer().getPluginManager().registerEvents(new SpleefMonitoring(), this);
        getServer().getPluginManager().registerEvents(new PlayerDisconnectHandler(), this);
        getServer().getPluginManager().registerEvents(new GuiInteractionHandler(), this);
    }

}


/*TODO
   - customizable reward
   - change messages that extra data can be included %p ~ player etc
   - economy
   - add reload file command
   - Arena info gui, view queue
 */

/*

Errors detected:
- Arena doesnt start by command or automatically
- Leaving / rejoining queue ends you in same position


 */

/*

+++Creating new arena types+++
1. Create Classes: Arena, Game, EditGui (arg[0] is usually players uuid)
2. Make them extend their superclasses
3. New entry in Game types Enum: Define with classes and default args
4. Implement fuctionality in classes: +Settings items in GuiClickHandler, ...
5. Implement Gameplay, use EventListeners for Controlling

 */