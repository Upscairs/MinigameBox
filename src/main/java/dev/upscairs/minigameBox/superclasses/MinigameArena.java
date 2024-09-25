package dev.upscairs.minigameBox.superclasses;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
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

    private boolean setupMode;

    public MinigameArena(String name, Location location1, Location location2, Location outsideLocation, String[] args) {

        this.name = name;
        this.location1 = location1;
        this.location2 = location2;
        this.outsideLocation = outsideLocation;

        setRawArgs(args);
        reloadSettings();

        generateMaxMinLocs();

        setupMode = false;
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


    /*
        Game functionalities
     */

    public boolean enoughPlayersToStart() {
        return queuedPlayers.size() >= minPlayers;
    }

    public void moveToOutsideBlock(Player player) {
        player.teleport(outsideLocation);
    }

    public void regenerateArena() {
        //For override
    }

    //Physical move
    public void movePlayersIn() {
        //For override
    }

    //Moves front queued players into "game" status, "Status move"
    public void setQueuedPlayersIngame() {
        /*
        int playersIngame = ingamePlayers.size();
        int spaceLeft = playersIngame - maxPlayers;
        for (int i = 0; i < spaceLeft; i++) {
            Player player = queuedPlayers.getFirst();
            System.out.println(player.getName());
            ingamePlayers.add(queuedPlayers.poll());
        }*/

        System.out.println(queuedPlayers.size());

        while (ingamePlayers.size() < maxPlayers && !queuedPlayers.isEmpty()) {
            Player player = queuedPlayers.poll();
            System.out.println(player.getName());
            ingamePlayers.add(player);
        }

    }

    //Checks if game needs to be terminated
    public boolean gameEndingState() {
        if(ingamePlayers.size() <= 1) {
            return true;
        }
        return false;
    }


    /*
        Player Queue and Ingame System
     */

    public boolean isPlayerIngame(Player player) {
        return ingamePlayers.contains(player);
    }

    public ArrayList<Player> getIngamePlayers() {
        return ingamePlayers;
    }

    //Removes from player list and tps out
    public void removePlayerFromGame(Player player) {
        ingamePlayers.remove(player);
        moveToOutsideBlock(player);
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

    public int getQueueLength() {
        return queuedPlayers.size();
    }


    /*
        Properties Getter/setter
     */

    public void setInSetupMode(boolean inSetupMode) {
        this.setupMode = inSetupMode;
    }

    public boolean isInSetupMode() {
        return setupMode;
    }


    /*
        Settings Getter
     */

    public String[] getRawArgs() {
        return rawArgs;
    }


    public String getName() {
        return name;
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

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getFillupWaitingTimeSec() {
        return fillupWaitingTimeSec;
    }

    public int getSetupTimeSec() {
        return setupTimeSec;
    }

    public boolean isContinuous() {
        return continuous;
    }

    public boolean isQueueOpen() {
        return queueOpen;
    }

    public boolean isVisible() {
        return visible;
    }

    public Material getRepresentingItem() {
        return representingItem;
    }

    public String getDescription() {
        return description;
    }


    /*
        Settings Setter
     */

    public void setRawArgs(String[] rawArgs) {
        this.rawArgs = rawArgs;
    }

    public void editArgs(int index, String newString) throws IllegalArgumentException {

        String[] oldSettings = getRawArgs();
        String[] settings = getRawArgs();
        settings[index] = newString;
        setRawArgs(settings);

        try {
            reloadSettings();
        } catch (NumberFormatException e) {
            setRawArgs(oldSettings);
            throw new IllegalArgumentException("Not requested type.");
        } catch (IllegalArgumentException e) {
            setRawArgs(oldSettings);
            throw new IllegalArgumentException(e.getMessage());
        }

        GameRegister.saveArenaSettings(this);

    }

    public void reloadSettings() throws NumberFormatException, IllegalArgumentException {

        int number = Integer.parseInt(rawArgs[0]);
        if(number < 1) {
            throw new IllegalArgumentException("Number must be greater than 0");
        }
        this.minPlayers = number;


        number = Integer.parseInt(rawArgs[1]);
        if(number < minPlayers) {
            throw new IllegalArgumentException("Number must be greater than min Players");
        }
        this.maxPlayers = number;

        number = Integer.parseInt(rawArgs[2]);
        if(number < 0) {
            throw new IllegalArgumentException("Number must be at least 0");
        }
        this.fillupWaitingTimeSec = number;

        number = Integer.parseInt(rawArgs[3]);
        if(number < 0) {
            throw new IllegalArgumentException("Number must be at least 0");
        }
        this.setupTimeSec = number;

        this.continuous = Boolean.parseBoolean(rawArgs[4]);
        this.queueOpen = Boolean.parseBoolean(rawArgs[5]);
        this.visible = Boolean.parseBoolean(rawArgs[6]);

        Material material = Material.valueOf(rawArgs[7]);
        if(material == null || material.isAir() || material.isLegacy()) {
            throw new IllegalArgumentException("Not a valid material");
        }
        this.representingItem = material;

        this.description = rawArgs[8];
    }



}
