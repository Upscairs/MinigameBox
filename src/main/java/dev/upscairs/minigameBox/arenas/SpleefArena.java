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

    public SpleefArena(String name, Location location1, Location location2, Location outsideLocation, int minPlayers, int maxPlayers, int fillupWaitingTimeSec, int setupTimeSec, boolean continuous, boolean queueOpen, int layerCount, Material spleefMaterial) {

        super(name, location1, location2, outsideLocation, minPlayers, maxPlayers, fillupWaitingTimeSec, setupTimeSec, continuous, queueOpen);

        this.spleefMaterial = spleefMaterial;
        this.layerCount = layerCount;

        int heightDifference = (int) (location1.getY() - location2.getY());

        if(heightDifference/4 < layerCount) {
            throw new IllegalArgumentException("Spleef arena is too small");
        }

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

    public void movePlayersIn() {
        Location dropLocation = new Location(getLocation1().getWorld(), (getLocation1().getX()+getLocation2().getX())/2, getLocation1().getY(), (getLocation1().getZ()+getLocation2().getZ())/2);
        for(Player p : getIngamePlayers()) {
            p.teleport(dropLocation);
        }
    }

    public int getLayerCount() {
        return layerCount;
    }
}
