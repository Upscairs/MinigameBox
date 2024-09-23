package dev.upscairs.minigameBox.events.handling;

import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import dev.upscairs.minigameBox.arenas.SpleefArena;
import dev.upscairs.minigameBox.games.SpleefGame;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpleefMonitoring implements Listener {

    //Players out, which are on the lowest layer
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player p = event.getPlayer();

        SpleefGame game = getSpleefGame(p);
        if(game == null) return;

        Location loc = p.getLocation();

        if(loc.getY() < game.getArena().getLocation2().getY()+0.5) {
            game.playerRemove(p);
        }

    }

    //Only spleef block breakable, but insta breaks
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if(event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        SpleefGame game = getSpleefGame(p);
        if(game == null) return;

        event.setCancelled(true);

        Block clickedBlock = event.getClickedBlock();

        if(((SpleefArena) game.getArena()).getSpleefMaterial() == clickedBlock.getType()) {

            if(!game.getArena().isInSetupMode()) {
                clickedBlock.setType(Material.AIR);
            }
        }
    }

    //Supressing block placement
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(getSpleefGame(event.getPlayer()) != null) {
            event.setCancelled(true);
        }
    }

    public SpleefGame getSpleefGame(Player player) {
        if(GameRegister.isPlayerInGame(player) && GameRegister.getPlayersGame(player) instanceof SpleefGame g) {
            return g;
        }
        else {
            return null;
        }
    }

}
