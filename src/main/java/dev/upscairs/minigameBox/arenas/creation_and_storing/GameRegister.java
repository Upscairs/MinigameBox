package dev.upscairs.minigameBox.arenas.creation_and_storing;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.SpleefArena;
import dev.upscairs.minigameBox.config.ArenaRegisterFile;
import dev.upscairs.minigameBox.games.GameTypes;
import dev.upscairs.minigameBox.games.MiniGame;
import dev.upscairs.minigameBox.games.SpleefGame;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class GameRegister {

    //Maps arena Name to Instance
    private static HashMap<String, MiniGame> games = new HashMap<>();

    public static void loadGames() {

        ArenaRegisterFile.reload();
        FileConfiguration config = ArenaRegisterFile.get();

        games.clear();

        //Iterating through gameTypes (highest section)
        for(String gameType : config.getKeys(false)) {

            if(!GameTypes.nameExists(gameType)) {
                continue;
            }

            ConfigurationSection arenas = config.getConfigurationSection(gameType);

            //For each arena retrieve info and put retrieved fitting gameClass in map
            arenas.getKeys(false).forEach(arenaName -> {

                Location location1 = arenas.getLocation(arenaName + ".location1");
                Location location2 = arenas.getLocation(arenaName + ".location2");
                Location outsideLocation = arenas.getLocation(arenaName + ".outside-location");
                String[] args = arenas.getStringList(arenaName + ".args").toArray(new String[0]);

                try {

                    MinigameArena arena = GameTypes.getFromName(gameType).getArenaClass().getDeclaredConstructor(String.class, Location.class, Location.class, Location.class, String[].class).newInstance(arenaName, location1, location2, outsideLocation, args);
                    MiniGame game = GameTypes.getFromName(gameType).getGameClass().getDeclaredConstructor(MinigameArena.class).newInstance(arena);
                    games.put(arenaName, game);

                } catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }

            });


        }

    }

    public static void saveArenaSettings(MinigameArena arena) {

        FileConfiguration config = ArenaRegisterFile.get();

        String pref = GameTypes.getFromArenaClass(arena.getClass()).getName() + "." + arena.getName() + ".";
        config.set(pref + ".location1", arena.getLocation1());
        config.set(pref + ".location2", arena.getLocation2());
        config.set(pref + ".outside-location", arena.getOutsideLocation());
        config.set(pref + ".args", arena.getRawArgs());

        ArenaRegisterFile.setConfig(config);
        ArenaRegisterFile.save();

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

    public static boolean dequeuePlayer(Player player) {
        for(MiniGame game : games.values()) {
            if(game.playerLeaveQueue(player)) {
                return true;
            }
        }
        return false;
    }

}
