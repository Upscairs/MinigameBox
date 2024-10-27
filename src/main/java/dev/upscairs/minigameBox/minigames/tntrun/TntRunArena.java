package dev.upscairs.minigameBox.minigames.tntrun;

import dev.upscairs.minigameBox.MinigameBox;
import dev.upscairs.minigameBox.superclasses.MinigameArena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;

public class TntRunArena extends MinigameArena {

    private int layerCount;
    private ArrayList<Integer> layersY;
    private int layerDistance;
    private Material layerMaterial;
    private int breakDelayTicks;

    public TntRunArena(String name, Location location1, Location location2, Location outsideLocation, Map<String, String> args) {

        super(name, location1, location2, outsideLocation, args);

        int heightDifference = (int) (location1.getY() - location2.getY());

        //Checking if enough space for layers (4x layers < space)
        if(heightDifference/4 < layerCount) {
            throw new IllegalArgumentException("Arena is too small");
        }

        //Calculating y pos for layers with equal distance

        layersY = new ArrayList<>();

        layerDistance = (int) Math.floor((double) heightDifference /layerCount);

        for(int i = 0; i < layerCount; i++) {
            layersY.add((layerDistance*i)+(int)location2.getY()+1);
        }

    }

    @Override
    public void regenerateArena() {

        Location location1 = getLocation1();
        Location location2 = getLocation2();

        //Placing spleef block in layers
        for(double x = location2.getX(); x < location1.getX(); x++) {
             for(int y = (int) location2.getY(); y < location1.getY(); y++) {
                 for (double z = location2.getZ(); z < location1.getZ(); z++) {
                     Location loc = new Location(location1.getWorld(), x, y, z);
                     if(layersY.contains(y)) {
                         loc.getBlock().setType(layerMaterial);
                     }
                     else {
                         loc.getBlock().setType(Material.AIR);
                     }

                 }
             }
        }

    }

    @Override
    public void deleteArena() {

        MinigameBox plugin = (MinigameBox) Bukkit.getPluginManager().getPlugin("MinigameBox");

        for(int y : layersY) {
            for(double x = getLocation2().getX(); x <= getLocation1().getX(); x++) {
                for(double z = getLocation2().getZ(); z <= getLocation1().getZ(); z++) {
                    Location loc = new Location(getLocation1().getWorld(), x, y, z);
                    loc.getBlock().setType(Material.AIR);
                }
            }
        }

    }

    public Material getLayerMaterial() {
        return layerMaterial;
    }

    //Tping players to top of area in the center
    @Override
    public void movePlayersIn() {
        Location dropLocation = new Location(getLocation1().getWorld(), (getLocation1().getX()+getLocation2().getX())/2, getLocation1().getY(), (getLocation1().getZ()+getLocation2().getZ())/2);
        for(Player p : getIngamePlayers()) {
            p.teleport(dropLocation);
        }
    }

    public int getLayerCount() {
        return layerCount;
    }

    public int getBreakDelayTicks() {
        return breakDelayTicks;
    }

    @Override
    public void reloadSettings() throws NumberFormatException, IllegalArgumentException {
        int number = Integer.parseInt(getRawArgs().get("layers"));
        if(number < 1) {
            throw new IllegalArgumentException("There must be at least one layer.");
        }
        int heightDifference = (int) (getLocation1().getY() - getLocation2().getY());
        if(heightDifference/4 < number) {
            throw new IllegalArgumentException("Arena is too small");
        }
        this.layerCount = number;

        Material material = Material.getMaterial(getRawArgs().get("layer_material").toUpperCase());
        if(material == null || material == Material.AIR || material.isLegacy()) {
            throw new IllegalArgumentException("Not a valid material.");
        }
        this.layerMaterial = material;

        number = Integer.parseInt(getRawArgs().get("breakdelay_ticks"));
        if(number < 0) {
            throw new IllegalArgumentException("Break delay must be a positive time.");
        }
        this.breakDelayTicks = number;

        super.reloadSettings();
    }
}
