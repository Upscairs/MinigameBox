package dev.upscairs.minigameBox.events.handling;

import dev.upscairs.minigameBox.GameRegister;
import dev.upscairs.minigameBox.events.custom.PlayerLeaveMinigameEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.desktop.QuitEvent;

public class PlayerLeaveMinigameHandler implements Listener {

    @EventHandler
    public void onPlayerLeaveMinigame(PlayerLeaveMinigameEvent event) {

        Player p = event.getPlayer();

        String gameName = p.getMetadata("GameName").get(0).asString();

        GameRegister.getGame(gameName).playerOut(event.getPlayer());

    }


}
