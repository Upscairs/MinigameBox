package dev.upscairs.minigameBox.events.handling;

import dev.upscairs.minigameBox.GameRegister;
import dev.upscairs.minigameBox.events.custom.PlayerJoinQueueEvent;
import dev.upscairs.minigameBox.games.MiniGame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinQueueHandler implements Listener {

    @EventHandler
    public void onPlayerJoinQueue(PlayerJoinQueueEvent event) {

        Player p = event.getPlayer();
        String arenaName = event.getArenaName();

        if(GameRegister.gameExists(arenaName)) {
            MiniGame game = GameRegister.getGame(arenaName);
            if(game.playerJoinQueue(p)) {
                //Success message
            }
            else {
                //Error message
            }
        }


    }



}
