package dev.upscairs.minigameBox.events.handling;

import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnectHandler implements Listener {

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if(player.hasMetadata("GameName") && GameRegister.gameExists("GameName")) {
            GameRegister.getGames().get("GameName").playerRemove(player);
        }

    }
}
