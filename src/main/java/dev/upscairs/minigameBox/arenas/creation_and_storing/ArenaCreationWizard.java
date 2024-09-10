package dev.upscairs.minigameBox.arenas.creation_and_storing;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.SpleefArena;
import dev.upscairs.minigameBox.config.MessagesConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ArenaCreationWizard {

    private final String gameType;
    private final String arenaName;
    private final Player creator;
    private Location location1 = null;
    private Location location2 = null;
    private Location outsideLocation = null;

    public ArenaCreationWizard(String gameType, String arenaName, Player creator) {
        this.gameType = gameType;
        this.arenaName = arenaName;
        this.creator = creator;
    }

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

        if(gameType.equalsIgnoreCase("Spleef")) {
            arena = new SpleefArena(arenaName, location1, location2, outsideLocation, 2, 2, 20, 10, true, true, 1, Material.WHITE_WOOL);
        }

        return arena;

    }

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

    public static ArrayList<String> getGamemodes() {
        ArrayList<String> gamemodes = new ArrayList<>();
        gamemodes.add("Spleef");
        //Here more
        return gamemodes;
    }


}
