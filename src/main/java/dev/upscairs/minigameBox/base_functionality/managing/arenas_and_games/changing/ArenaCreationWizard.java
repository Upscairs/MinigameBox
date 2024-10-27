package dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.changing;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameTypes;
import dev.upscairs.minigameBox.superclasses.MinigameArena;
import dev.upscairs.minigameBox.base_functionality.managing.config.MessagesConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ArenaCreationWizard {

    private final GameTypes gameType;
    private final String arenaName;
    private final Player creator;
    private Location location1 = null;
    private Location location2 = null;
    private Location outsideLocation = null;

    public ArenaCreationWizard(Player creator, GameTypes gameType, String arenaName) {
        this.gameType = gameType;
        this.arenaName = arenaName;
        this.creator = creator;
    }

    //Filling required data, if full, creating arena
    public MinigameArena nextInput(Location location) {

        if(location1 == null) {
            location1 = location;
            creator.sendMessage(MessagesConfig.get().getString("managing.success-pos1-placed"));
            return null;
        }
        if(location2 == null) {
            location2 = location;
            creator.sendMessage(MessagesConfig.get().getString("managing.success-pos2-placed"));
            return null;
        }

        generateMaxMinLocs();

        outsideLocation = location;

        //Check if outside location is outside of arena
        if(outsideLocation.getX() > location2.getX() && outsideLocation.getX() < location1.getX()) {
            if(outsideLocation.getY() > location2.getY() && outsideLocation.getY() < location1.getY()) {
                if(outsideLocation.getZ() > location2.getZ() && outsideLocation.getZ() < location1.getZ()) {
                    creator.sendMessage(MessagesConfig.get().getString("managing.error-outpos-in-bounds"));
                    outsideLocation = null;
                }
            }
        }

        if(outsideLocation == null) {
            return null;
        }

        creator.sendMessage(MessagesConfig.get().getString("managing.success-outpos-placed-arena-created"));

        MinigameArena arena = null;

        //Fetching arena class from gametype enum and parsing variables
        try {
            arena = gameType.getArenaClass().getDeclaredConstructor(String.class, Location.class, Location.class, Location.class, Map.class).newInstance(arenaName, location1, location2, outsideLocation, gameType.getDefaultArgs());
        } catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return arena;

    }

    //Rotating both locations so Location1 is greater than Location2 on all axis
    private void generateMaxMinLocs() {
        double maxX = Math.max(location1.getX(), location2.getX());
        double maxY = Math.max(location1.getY(), location2.getY());
        double maxZ = Math.max(location1.getZ(), location2.getZ());
        double minX = Math.min(location1.getX(), location2.getX());
        double minY = Math.min(location1.getY(), location2.getY());
        double minZ = Math.min(location1.getZ(), location2.getZ());

        location1 = new Location(location1.getWorld(), maxX, maxY, maxZ);
        location2 = new Location(location2.getWorld(), minX, minY, minZ);

    }

}
