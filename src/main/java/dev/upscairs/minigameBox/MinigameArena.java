package dev.upscairs.minigameBox;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class MinigameArena {

    private Location location1;
    private Location location2;
    private Location outsideLocation;

    private int minPlayers;
    private int maxPlayers;
    private int fillupWaitingTimeSec;
    private int setupTimeSec;
    private boolean continuous;

    private ArrayList<Player> ingamePlayers = new ArrayList<>();
    private ArrayDeque<Player> queuedPlayers = new ArrayDeque<>();

    public MinigameArena(Location location1, Location location2, Location outsideLocation, int minPlayers, int maxPlayers) {
        construct(location1, location2, outsideLocation, minPlayers, maxPlayers, 20, 10, true);
    }

    public MinigameArena(Location location1, Location location2, Location outsideLocation, int minPlayers, int maxPlayers, int fillupWaitingTimeSec, int setupTimeSec, boolean continuous) {
        construct(location1, location2, outsideLocation, minPlayers, maxPlayers, fillupWaitingTimeSec, setupTimeSec, continuous);
    }

    private void construct(Location location1, Location location2, Location outsideLocation, int minPlayers, int maxPlayers, int fillupWaitingTimeSec, int setupTimeSec, boolean continuous) {
        this.location1 = location1;
        this.location2 = location2;
        this.outsideLocation = outsideLocation;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.fillupWaitingTimeSec = fillupWaitingTimeSec;
        this.setupTimeSec = setupTimeSec;
        this.continuous = continuous;
        generateMaxMinLocs();
    }

    //Overwrites location1 and location2 so all coords of loc1 are greater than those of loc2
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

    public Location getLocation1() {
        return location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public Location getOutsideLocation() {
        return outsideLocation;
    }
}
