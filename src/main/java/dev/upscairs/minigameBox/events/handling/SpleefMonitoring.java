package dev.upscairs.minigameBox.events.handling;

import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import dev.upscairs.minigameBox.arenas.SpleefArena;
import dev.upscairs.minigameBox.events.custom.PlayerLeaveMinigameEvent;
import dev.upscairs.minigameBox.games.SpleefGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpleefMonitoring implements Listener {

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player p = event.getPlayer();

        if(!p.hasMetadata("GameName")) {
            return;
        }

        String gameName = p.getMetadata("GameName").get(0).asString();

        if(!isPlayerInSpleef(p)) {
            return;
        }

        SpleefGame game = (SpleefGame) GameRegister.getGame(gameName);
        Location loc = p.getLocation();

        if(loc.getY() < game.getArena().getLocation2().getY()+0.5) {
            Bukkit.getPluginManager().callEvent(new PlayerLeaveMinigameEvent(p));
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if(!p.hasMetadata("GameName")) {
            return;
        }

        String gameName = p.getMetadata("GameName").get(0).asString();

        if(!isPlayerInSpleef(p)) {
            return;
        }

        if(event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        event.setCancelled(true);

        Block clickedBlock = event.getClickedBlock();

        if(((SpleefArena) GameRegister.getGame(gameName).getArena()).getSpleefMaterial() == clickedBlock.getType()) {
            clickedBlock.setType(Material.AIR);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(isPlayerInSpleef(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    public boolean isPlayerInSpleef(Player player) {

        if(!player.hasMetadata("GameName")) {
            return false;
        }

        String gameName = player.getMetadata("GameName").get(0).asString();

        if(!(GameRegister.getGame(gameName) instanceof SpleefGame)) {
            return false;
        }
        return true;
    }

}
