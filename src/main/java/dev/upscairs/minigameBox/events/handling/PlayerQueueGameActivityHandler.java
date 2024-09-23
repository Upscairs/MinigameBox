package dev.upscairs.minigameBox.events.handling;

import dev.upscairs.minigameBox.MinigameBox;
import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import dev.upscairs.minigameBox.config.MessagesConfig;
import dev.upscairs.minigameBox.events.custom.PlayerLeaveMinigameEvent;
import dev.upscairs.minigameBox.games.MiniGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQueueGameActivityHandler implements Listener {

    /*
    @EventHandler
    public void onPlayerJoinQueue(PlayerJoinQueueEvent event) {

        Player p = event.getPlayer();
        String arenaName = event.getArenaName();

        if(GameRegister.gameExists(arenaName)) {
            MiniGame game = GameRegister.getGame(arenaName);


        }


    }

    @EventHandler
    public void onPlayerLeaveQueueEvent(PlayerLeaveQueueEvent event) {

        Player player = event.getPlayer();

        if(GameRegister.dequeuePlayer(player)) {
            player.sendMessage(MessagesConfig.get().getString("game.success-queue-left"));
        }
        else {

        }

        MinigameBox plugin = (MinigameBox) Bukkit.getPluginManager().getPlugin("MinigameBox");
        player.removeMetadata("GameName", plugin);
    }

    @EventHandler
    public void onPlayerLeaveMinigame(PlayerLeaveMinigameEvent event) {

        Player p = event.getPlayer();

        if(!p.hasMetadata("GameName")) {
            return;
        }
        String gameName = p.getMetadata("GameName").get(0).asString();

        GameRegister.getGame(gameName).playerOut(event.getPlayer());

    }*/

}
