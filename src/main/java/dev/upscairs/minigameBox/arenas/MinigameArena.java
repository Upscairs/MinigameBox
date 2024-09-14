package dev.upscairs.minigameBox.arenas;

import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class MinigameArena {

    private String name;

    private Location location1;
    private Location location2;
    private Location outsideLocation;
    private String[] rawArgs;

    private int minPlayers;
    private int maxPlayers;
    private int fillupWaitingTimeSec;
    private int setupTimeSec;
    private boolean continuous;
    private boolean queueOpen;
    private boolean visible;
    private Material representingItem;
    private String description;

    private ArrayList<Player> ingamePlayers = new ArrayList<>();
    private ArrayDeque<Player> queuedPlayers = new ArrayDeque<>();

    public MinigameArena(String name, Location location1, Location location2, Location outsideLocation, String[] args) {

        this.name = name;
        this.location1 = location1;
        this.location2 = location2;
        this.outsideLocation = outsideLocation;

        setRawArgs(args);
        reloadSettings();

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

    //Moves front queued players into "game" status
    public void setQueuedPlayersIngame() {
        int playersIngame = ingamePlayers.size();
        int spaceLeft = playersIngame - maxPlayers;
        for (int i = 0; i < spaceLeft; i++) {
            ingamePlayers.add(queuedPlayers.poll());
        }
    }

    //Returns if game can start by itself
    public boolean isAutoStartable() {
        if(ingamePlayers.size() < minPlayers) {
            return false;
        }
        if(ingamePlayers.size() > maxPlayers) {
            return false;
        }
        if(!continuous) {
            return false;
        }
        return true;
    }

    //Removes from player lsit and tps out
    public void removePlayerFromGame(Player player) {
        ingamePlayers.remove(player);
        moveToOutsideBlock(player);
    }

    //Checks if game needs to be terminated
    public boolean gameEndingState() {
        if(ingamePlayers.size() <= 1) {
            return true;
        }
        return false;
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

    public boolean isQueueOpen() {
        return queueOpen;
    }

    public void addPlayerToQueue(Player player) {
        queuedPlayers.add(player);
    }

    public boolean isPlayerInQueue(Player player) {
        return queuedPlayers.contains(player);
    }

    public void removePlayerFromQueue(Player player) {
        queuedPlayers.remove(player);
    }

    public int getFillupWaitingTimeSec() {
        return fillupWaitingTimeSec;
    }

    public int getSetupTimeSec() {
        return setupTimeSec;
    }

    public ArrayList<Player> getIngamePlayers() {
        return ingamePlayers;
    }

    public String getName() {
        return name;
    }

    public void moveToOutsideBlock(Player player) {
        player.teleport(outsideLocation);
    }

    /*
    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
        reloadRawArgs();
    }*/

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isContinuous() {
        return continuous;
    }

    public void regenerateArena() {
        return;
    }

    public String[] getRawArgs() {
        return rawArgs;
    }

    public void setRawArgs(String[] rawArgs) {
        this.rawArgs = rawArgs;
    }

    public boolean enoughPlayersToStart() {
        return ingamePlayers.size() >= minPlayers;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getDescription() {
        return description;
    }

    public Material getRepresentingItem() {
        return representingItem;
    }

    /*
    public void setQueueOpen(boolean open) {
        this.queueOpen = open;
        reloadRawArgs();
    }*/


    public void editArgs(int index, String newString) {
        String[] settings = getRawArgs();
        settings[index] = newString;
        setRawArgs(settings);
        reloadSettings();
        GameRegister.saveArenaSettings(this);
    }

    public void reloadSettings() {
        this.minPlayers = Integer.parseInt(rawArgs[0]);
        this.maxPlayers = Integer.parseInt(rawArgs[1]);
        this.fillupWaitingTimeSec = Integer.parseInt(rawArgs[2]);
        this.setupTimeSec = Integer.parseInt(rawArgs[3]);
        this.continuous = Boolean.parseBoolean(rawArgs[4]);
        this.queueOpen = Boolean.parseBoolean(rawArgs[5]);
        this.visible = Boolean.parseBoolean(rawArgs[6]);
        this.representingItem = Material.getMaterial(rawArgs[7]);
        this.description = rawArgs[8];
    }

}
