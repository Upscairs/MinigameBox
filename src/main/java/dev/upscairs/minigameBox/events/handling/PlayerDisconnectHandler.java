package dev.upscairs.minigameBox.events.handling;

import dev.upscairs.minigameBox.events.custom.PlayerLeaveMinigameEvent;
import dev.upscairs.minigameBox.events.custom.PlayerLeaveQueueEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnectHandler implements Listener {

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        //Removing from queues and games
        Bukkit.getPluginManager().callEvent(new PlayerLeaveMinigameEvent(player));
        Bukkit.getPluginManager().callEvent(new PlayerLeaveQueueEvent(player));

    }
}
