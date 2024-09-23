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

        SpleefGame game = getPlayedSpleefGame(p);
        if(game == null) return;

        Location loc = p.getLocation();

        Location arenaLoc1 = game.getArena().getLocation1();
        Location arenaLoc2 = game.getArena().getLocation2();

        //Lowest layer
        if(loc.getY() < arenaLoc2.getY()+0.5) {
            game.playerRemove(p);
        }

        //Out of bounds
        if(loc.getX() > arenaLoc1.getX() || loc.getX() < arenaLoc1.getX()) {
            if(loc.getY() > arenaLoc1.getY() || loc.getY() < arenaLoc1.getY()) {
                if(loc.getZ() > arenaLoc1.getZ() || loc.getZ() < arenaLoc1.getZ()) {
                    game.playerRemove(p);
                }
            }
        }

    }

    //Only spleef block breakable, but insta breaks
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if(event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        SpleefGame game = getPlayedSpleefGame(p);
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
        if(getPlayedSpleefGame(event.getPlayer()) != null) {
            event.setCancelled(true);
        }
    }

    public SpleefGame getPlayedSpleefGame(Player player) {

        if(GameRegister.isPlayerInGame(player)) {
            if(GameRegister.getPlayersGame(player) instanceof SpleefGame g) {
                if(g.getArena().isPlayerIngame(player)) {
                    return g;
                }
            }
        }

        return null;

    }

}
