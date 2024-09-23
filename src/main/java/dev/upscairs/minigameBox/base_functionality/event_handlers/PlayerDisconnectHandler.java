package dev.upscairs.minigameBox.base_functionality.event_handlers;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnectHandler implements Listener {

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if(GameRegister.isPlayerInGame(player)) {
            GameRegister.getPlayersGame(player).playerRemove(player);
        }

    }
}
