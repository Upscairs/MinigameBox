package dev.upscairs.minigameBox.arenas;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SpleefArena extends MinigameArena {

    private int layerCount;
    private ArrayList<Integer> layersY;
    private int layerDistance;
    private Material spleefMaterial;

    public SpleefArena(Location location1, Location location2, Location outsideLocation, int minPlayers, int maxPlayers, int layerCount, Material spleefMaterial) {

        super(location1, location2, outsideLocation, minPlayers, maxPlayers);

        this.spleefMaterial = spleefMaterial;
        this.layerCount = layerCount;

        int heightDifference = (int) (location1.getY() - location2.getY());

        if(heightDifference/4 < layerCount) {
            throw new IllegalArgumentException("Spleef arena is too small");
        }

        layersY = new ArrayList<>();

        layerDistance = (int) Math.floor(heightDifference/layerCount);

        for(int i = 1; i < layerCount; i++) {
            if(i % layerDistance == 0) {
                layersY.add(i);
            }
        }

    }

    public void regenerateArena() {

        Location location1 = getLocation1();
        Location location2 = getLocation2();

        for(int y : layersY) {
            for(int x = (int) location2.getX(); x < location1.getX(); x++) {
                for(int z = (int) location2.getZ(); z < location1.getZ(); z++) {
                    Location loc = new Location(location1.getWorld(), x, y, z);
                    loc.getBlock().setType(spleefMaterial);
                }
            }

        }

    }

    public Material getSpleefMaterial() {
        return spleefMaterial;
    }







}
