package dev.upscairs.minigameBox.minigames.spleef;

import dev.upscairs.minigameBox.superclasses.MinigameArena;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SpleefArena extends MinigameArena {

    private int layerCount;
    private ArrayList<Integer> layersY;
    private int layerDistance;
    private Material spleefMaterial;

    public SpleefArena(String name, Location location1, Location location2, Location outsideLocation, String[] args) {

        super(name, location1, location2, outsideLocation, args);

        int heightDifference = (int) (location1.getY() - location2.getY());

        //Checking if enough space for layers (4x layers < space)
        if(heightDifference/4 < layerCount) {
            throw new IllegalArgumentException("Spleef arena is too small");
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
        for(int x = (int) location2.getX(); x < location1.getX(); x++) {
             for(int y = (int) location2.getY(); y < location1.getY(); y++) {
                 for (int z = (int) location2.getZ(); z < location1.getZ(); z++) {
                     Location loc = new Location(location1.getWorld(), x, y, z);
                     if(layersY.contains(y)) {
                         loc.getBlock().setType(spleefMaterial);
                     }
                     else {
                         loc.getBlock().setType(Material.AIR);
                     }

                 }
             }
        }

    }

    public Material getSpleefMaterial() {
        return spleefMaterial;
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

    @Override
    public void reloadSettings() throws NumberFormatException, IllegalArgumentException {
        int number = Integer.parseInt(getRawArgs()[9]);
        if(number < 1) {
            throw new IllegalArgumentException("There must be at least one layer.");
        }
        int heightDifference = (int) (getLocation1().getY() - getLocation2().getY());
        if(heightDifference/4 < number) {
            throw new IllegalArgumentException("Spleef arena is too small");
        }
        this.layerCount = number;

        Material material = Material.getMaterial(getRawArgs()[10]);
        if(material == null || material == Material.AIR || material.isLegacy()) {
            throw new IllegalArgumentException("Not a valid material.");
        }
        this.spleefMaterial = material;
        super.reloadSettings();
    }
}