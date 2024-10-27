package dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing;

import dev.upscairs.minigameBox.superclasses.MinigameArena;
import dev.upscairs.minigameBox.base_functionality.managing.config.ArenaRegisterFile;
import dev.upscairs.minigameBox.superclasses.MiniGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class GameRegister {

    //Maps arena Name to Instance
    private static HashMap<String, MiniGame> games = new HashMap<>();
    private static HashMap<Player, MiniGame> playersQueuedAndIngame = new HashMap<>();

    //Reload a single game from the register file
    public static void reloadGame(String gameName, GameTypes gameType) {
        ArenaRegisterFile.reload();
        FileConfiguration config = ArenaRegisterFile.get();

        String path = gameType.getName() + "." + gameName;

        Location location1;
        Location location2;
        Location outsideLocation;
        Map<String, String> args;

        //Ignoring arena if values are unparsable
        try {
            location1 = config.getLocation(path + ".location1");
            location2 = config.getLocation(path + ".location2");
            outsideLocation = config.getLocation(path + ".outside-location");
            args = (Map<String, String>) config.get(path + ".args");
        } catch (Exception e) {
            games.remove(gameName);
            return;
        }

        MiniGame oldGame = games.get(gameName);

        //Writing games map entry
        try {
            MinigameArena arena = gameType.getArenaClass().getDeclaredConstructor(String.class, Location.class, Location.class, Location.class, Map.class).newInstance(gameName, location1, location2, outsideLocation, args);
            MiniGame game = gameType.getGameClass().getDeclaredConstructor(MinigameArena.class).newInstance(arena);
            games.put(gameName, game);

            //Remapping queued players of old game, to the new queue
            if(oldGame != null) {
                for(Player p : oldGame.getArena().getQueuedPlayers()) {
                    playersQueuedAndIngame.remove(p);
                    game.playerJoinQueue(p);
                }
            }

        } catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Bukkit.getLogger().severe("Error reloading game " + gameName);
            e.printStackTrace();
        }

    }

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

                Object args = arenas.get(arenaName + ".args");

                //Conversion from String-Array save-standard (remove later)
                if(args instanceof List) {

                    Bukkit.getLogger().info("The arena \"" + arenaName + "\" has a legacy format standard. Converting...");

                    Map<String, String> convertedArgs = new HashMap<>();
                    List<String> argsList = (List<String>) args;

                    for(int i = 0; i < argsList.size(); i++) {
                        switch(i) {
                            case 0: convertedArgs.put("min_players", argsList.get(i)); break;
                            case 1: convertedArgs.put("max_players", argsList.get(i)); break;
                            case 2: convertedArgs.put("fillup_time", argsList.get(i)); break;
                            case 3: convertedArgs.put("setup_time", argsList.get(i)); break;
                            case 4: convertedArgs.put("continuous", argsList.get(i)); break;
                            case 5: convertedArgs.put("queue_open", argsList.get(i)); break;
                            case 6: convertedArgs.put("list_visible", argsList.get(i)); break;
                            case 7: convertedArgs.put("list_item", argsList.get(i)); break;
                            case 8: convertedArgs.put("description", argsList.get(i)); break;
                            case 9: convertedArgs.put("layers", argsList.get(i)); break;
                            case 10: convertedArgs.put("layer_material", argsList.get(i)); break;
                            case 11: convertedArgs.put("breakdelay_ticks", argsList.get(i)); break;
                            default: break;
                        }
                    }

                    try {

                        MinigameArena arena = GameTypes.getFromName(gameType).getArenaClass().getDeclaredConstructor(String.class, Location.class, Location.class, Location.class, Map.class).newInstance(arenaName, location1, location2, outsideLocation, convertedArgs);
                        MiniGame game = GameTypes.getFromName(gameType).getGameClass().getDeclaredConstructor(MinigameArena.class).newInstance(arena);
                        games.put(arenaName, game);

                        //Writing back in correct format
                        saveArenaSettings(arena);

                    } catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        //Bukkit.getLogger().warning("Conversion failed. You might need to check the integrity of your arena.");
                        e.printStackTrace();
                    }

                }
                else if(args instanceof Map) {

                    try {

                        MinigameArena arena = GameTypes.getFromName(gameType).getArenaClass().getDeclaredConstructor(String.class, Location.class, Location.class, Location.class, Map.class).newInstance(arenaName, location1, location2, outsideLocation, args);
                        MiniGame game = GameTypes.getFromName(gameType).getGameClass().getDeclaredConstructor(MinigameArena.class).newInstance(arena);
                        games.put(arenaName, game);

                    } catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        Bukkit.getLogger().warning("Loading of arena \"" + arenaName + "\" failed. Did you edit the arena register file?");
                    }

                }

            });

        }

    }

    //Saving given arena to file, calls map reload
    public static void saveArenaSettings(MinigameArena arena) {

        FileConfiguration config = ArenaRegisterFile.get();

        String pref = GameTypes.getFromArenaClass(arena.getClass()).getName() + "." + arena.getName() + ".";
        config.set(pref + ".location1", arena.getLocation1());
        config.set(pref + ".location2", arena.getLocation2());
        config.set(pref + ".outside-location", arena.getOutsideLocation());
        config.set(pref + ".args", arena.getRawArgs());

        ArenaRegisterFile.setConfig(config);
        ArenaRegisterFile.save();
    }

    public static void removeEntriesForName(String name) {
        games.remove(name);
        playersQueuedAndIngame.entrySet().removeIf(entry -> entry.getValue().getArena().getName().equals(name));
    }

    public static boolean gameExists(String name) {
        return games.containsKey(name);
    }

    public static MiniGame getGame(String name) {
        return games.get(name);
    }

    public static void shutdownGames() {
        for(MiniGame game : games.values()) {
            saveArenaSettings(game.getArena());
        }
    }

    public static HashMap<String, MiniGame> getGames() {
        return games;
    }

    public static void putPlayerInGame(Player player, MiniGame game) {
        playersQueuedAndIngame.put(player, game);
    }

    public static boolean isPlayerInGame(Player player) {
        return playersQueuedAndIngame.containsKey(player);
    }

    public static MiniGame getPlayersGame(Player player) {
        return playersQueuedAndIngame.get(player);
    }

    public static void removePlayerFromGame(Player player) {
        playersQueuedAndIngame.remove(player);
    }

    public static Set<Player> getIngamePlayers() {
        return playersQueuedAndIngame.keySet();
    }

}
