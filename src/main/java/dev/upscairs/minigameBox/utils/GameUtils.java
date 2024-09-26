package dev.upscairs.minigameBox.utils;

import dev.upscairs.minigameBox.base_functionality.managing.config.SettingsFile;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class GameUtils {

    public static void broadcastMessage(Location center, String message) {

        if(!SettingsFile.get().getBoolean("arena-broadcast")) {
            return;
        }

        double range = SettingsFile.get().getDouble("broadcast-range");

        for(Player player : center.getWorld().getPlayers()) {
            if(player.getLocation().distance(center) <= range) {
                player.sendMessage(message);
            }
        }


    }
}
