package dev.upscairs.minigameBox.minigames.spleef;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SpleefMonitoring implements Listener {

    //Prevent fall damage
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player p) {

            SpleefGame game = getPlayedSpleefGame(p);
            if(game == null) return;

            if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                event.setCancelled(true);
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

    public static void boundsCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    for(Player p : GameRegister.getIngamePlayers()) {

                        SpleefGame game = getPlayedSpleefGame(p);
                        if(game == null) return;

                        Location loc = p.getLocation();

                        Location arenaLoc1 = game.getArena().getLocation1();
                        Location arenaLoc2 = game.getArena().getLocation2();

                        //Players out, which are on the lowest layer
                        if(loc.getY() < arenaLoc2.getY()+0.5) {
                            game.playerRemove(p);
                        }

                        //Out of bounds
                        if(loc.getX() > arenaLoc1.getX() || loc.getX() < arenaLoc2.getX()) {
                            if(loc.getY() > arenaLoc1.getY() || loc.getY() < arenaLoc2.getY()) {
                                if(loc.getZ() > arenaLoc1.getZ() || loc.getZ() < arenaLoc2.getZ()) {
                                    game.playerRemove(p);
                                }
                            }
                        }
                    }
                } catch (Exception ignored) {

                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("MinigameBox"), 0L, 1L);
    }

    //Returns SpleefGame, if player is in one and is activly playing
    public static SpleefGame getPlayedSpleefGame(Player player) {

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
