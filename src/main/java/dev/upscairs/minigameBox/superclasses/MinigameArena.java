package dev.upscairs.minigameBox.superclasses;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class MinigameArena {

    private String name;

    private Location location1;
    private Location location2;
    private Location outsideLocation;
    private Map<String, String> args;

    private int minPlayers;
    private int maxPlayers;
    private int fillupWaitingTimeSec;
    private int setupTimeSec;
    private boolean continuous;
    private boolean queueOpen;
    private boolean visible;
    private Material representingItem;
    private String description;

    private HashSet<Player> ingamePlayers = new HashSet<>();
    private ArrayDeque<Player> queuedPlayers = new ArrayDeque<>();

    private boolean setupMode;

    public MinigameArena(String name, Location location1, Location location2, Location outsideLocation, Map<String, String> args) {

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

    public void deleteArena() {
        //Override
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

        while (ingamePlayers.size() < maxPlayers && !queuedPlayers.isEmpty()) {
            Player player = queuedPlayers.pollFirst();
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

    public HashSet<Player> getIngamePlayers() {
        return ingamePlayers;
    }

    public void flushIngamePlayers() {
        Iterator<Player> iterator = ingamePlayers.iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            moveToOutsideBlock(player);
            iterator.remove();
        }
    }

    //Removes from player list and tps out, DO NOT CALL FROM ITERATON
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

    public ArrayDeque<Player> getQueuedPlayers() {
        return queuedPlayers;
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

    public Map<String, String> getRawArgs() {
        return args;
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

    public void setRawArgs(Map<String, String> args) {
        this.args = args;
    }

    public void editArgs(String key, String value) throws IllegalArgumentException {

        Map<String, String> oldSettings = getRawArgs();
        Map<String, String> settings = getRawArgs();
        settings.put(key, value);
        setRawArgs(settings);

        //Testing settings, if faulty -> rollback
        try {
            testSettings();
        } catch (NumberFormatException e) {
            setRawArgs(oldSettings);
            throw new IllegalArgumentException("Not requested type.");
        } catch (IllegalArgumentException e) {
            setRawArgs(oldSettings);
            throw new IllegalArgumentException(e.getMessage());
        }

        MiniGame game = GameRegister.getGame(name);

        //Reload if no game running, if yes, flag for doing that later
        if(game.isGameRunning()) {
            game.flagForReload();
        }
        else {
            reloadSettings();
        }

        GameRegister.saveArenaSettings(this);

    }

    public void reloadSettings() throws NumberFormatException, IllegalArgumentException {

        testSettings();

        int number = Integer.parseInt(args.get("min_players"));
        if(number < 1) {
            throw new IllegalArgumentException("Number must be greater than 0");
        }
        this.minPlayers = number;


        number = Integer.parseInt(args.get("max_players"));
        this.maxPlayers = number;

        number = Integer.parseInt(args.get("fillup_time"));
        this.fillupWaitingTimeSec = number;

        number = Integer.parseInt(args.get("setup_time"));
        this.setupTimeSec = number;

        this.continuous = Boolean.parseBoolean(args.get("continuous"));
        this.queueOpen = Boolean.parseBoolean(args.get("queue_open"));
        this.visible = Boolean.parseBoolean(args.get("list_visible"));

        Material material = Material.valueOf(args.get("list_item").toUpperCase());
        this.representingItem = material;

        this.description = args.get("description");
    }

    public void testSettings() throws NumberFormatException, IllegalArgumentException {

        if(Integer.parseInt(args.get("min_players")) < 1) {
            throw new IllegalArgumentException("Number must be greater than 0");
        }

        if(Integer.parseInt(args.get("max_players")) < minPlayers) {
            throw new IllegalArgumentException("Number must be greater than min Players");
        }

        if(Integer.parseInt(args.get("fillup_time")) < 0) {
            throw new IllegalArgumentException("Number must be at least 0");
        }

        if(Integer.parseInt(args.get("setup_time")) < 0) {
            throw new IllegalArgumentException("Number must be at least 0");
        }

        Material material = Material.valueOf(args.get("list_item").toUpperCase());
        if(material == null || material.isAir() || material.isLegacy()) {
            throw new IllegalArgumentException("Not a valid material");
        }

    }



}
