package dev.upscairs.minigameBox;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.SpleefArena;
import dev.upscairs.minigameBox.config.ArenaRegister;
import dev.upscairs.minigameBox.games.MiniGame;
import dev.upscairs.minigameBox.games.SpleefGame;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public abstract class GameRegister {

    private static MinigameBox plugin;

    //Maps arena Name to Instance
    private static HashMap<String, MiniGame> games = new HashMap<>();

    public static void loadGames() {

        if(plugin == null) {
            throw new IllegalStateException("Register has no plugin given yet. Please set one");
        }

        games.clear();

        FileConfiguration config = ArenaRegister.get();
        ConfigurationSection spleefArenas = config.getConfigurationSection("SPLEEF");

        if(spleefArenas != null) {
            spleefArenas.getKeys(false).forEach(arenaName -> {

                Location location1 = spleefArenas.getLocation(arenaName + ".location1");
                Location location2 = spleefArenas.getLocation(arenaName + ".location2");
                Location outsideLocation = spleefArenas.getLocation(arenaName + ".outside-location");
                int minPlayers = spleefArenas.getInt(arenaName + ".min-players");
                int maxPlayers = spleefArenas.getInt(arenaName + ".max-players");
                int fillupWaitingTimeSec = spleefArenas.getInt(arenaName + ".fillup-waiting-time-sec");
                int setupTimeSec = spleefArenas.getInt(arenaName + ".setup-time-sec");
                boolean continuous = spleefArenas.getBoolean(arenaName + ".continuous");
                boolean queueOpen = spleefArenas.getBoolean(arenaName + ".queue-open");
                int layerCount = spleefArenas.getInt(arenaName + ".layer-count");
                Material spleefMaterial = Material.getMaterial(spleefArenas.getString(arenaName + ".spleef-material"));

                MinigameArena arena = new SpleefArena(arenaName, location1, location2, outsideLocation, minPlayers, maxPlayers, fillupWaitingTimeSec, setupTimeSec, continuous, queueOpen, layerCount, spleefMaterial);

                MiniGame game = new SpleefGame(plugin, arena);

                games.put(arenaName, game);
            });
        }

    }

    public static void saveArenaSettings(MinigameArena arena) {

        FileConfiguration config = ArenaRegister.get();

        if(arena instanceof SpleefArena a) {

            String pref = "SPLEEF" + a.getName();

            config.set(pref + ".location1", a.getLocation1());
            config.set(pref + ".location2", a.getLocation2());
            config.set(pref + ".outside-location", a.getOutsideLocation());
            config.set(pref + ".min-players", a.getMinPlayers());
            config.set(pref + ".max-players", a.getMaxPlayers());
            config.set(pref + ".fillup-waiting-time-sec", a.getFillupWaitingTimeSec());
            config.set(pref + ".setup-time-sec", a.getSetupTimeSec());
            config.set(pref + ".continuous", a.isContinuous());
            config.set(pref + ".queue-open", a.isQueueOpen());
            config.set(pref + "layer-count", a.getLayerCount());
            config.set(pref + ".spleef-material", a.getSpleefMaterial().name());

        }

        ArenaRegister.setConfig(config);
        ArenaRegister.save();

        loadGames();
    }

    public static boolean gameExists(String name) {
        return games.containsKey(name);
    }

    public static MiniGame getGame(String name) {
        return games.get(name);
    }

    public static void shutdownGames() {
        for(MiniGame game : games.values()) {
            game.endGame(false);
            saveArenaSettings(game.getArena());
        }
    }

    public static void setPlugin(MinigameBox plugin) {
        GameRegister.plugin = plugin;
    }

}
