package dev.upscairs.minigameBox.minigames.tntrun;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.TNTPrimeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TntRunMonitoring implements Listener {

    //Prevent fall damage
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player p) {

            TntRunGame game = getPlayedTntRunGame(p);
            if(game == null) return;

            if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                event.setCancelled(true);
            }
        }
    }

    //Prevent block breaking
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();

        TntRunGame game = getPlayedTntRunGame(p);
        if(game == null) return;

        event.setCancelled(true);

    }

    //Supressing block placement
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(getPlayedTntRunGame(event.getPlayer()) != null) {
            event.setCancelled(true);
        }
    }

    //Supressing block interaction
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        TntRunGame game = getPlayedTntRunGame(p);
        if(game == null) return;

        event.setCancelled(true);
    }

    public static void boundsCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    for(Player p : GameRegister.getIngamePlayers()) {

                        TntRunGame game = getPlayedTntRunGame(p);
                        if(game == null) return;

                        Location loc = p.getLocation();

                        Location arenaLoc1 = game.getArena().getLocation1();
                        Location arenaLoc2 = game.getArena().getLocation2();

                        //Players out, which are on the lowest layer
                        if(loc.getY() < arenaLoc2.getY()+0.5) {
                            game.playerRemove(p);
                        }

                        //Out of bounds
                        if(loc.getX() > arenaLoc1.getX()+1 || loc.getX() < arenaLoc2.getX()-1) {
                            game.playerRemove(p);
                        }
                        if(loc.getY() > arenaLoc1.getY()+1 || loc.getY() < arenaLoc2.getY()-1) {
                            game.playerRemove(p);
                        }
                        if(loc.getZ() > arenaLoc1.getZ()+1 || loc.getZ() < arenaLoc2.getZ()-1) {
                            game.playerRemove(p);
                        }

                    }
                } catch (Exception ignored) {

                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("MinigameBox"), 0L, 1L);
    }

    //Tnt Run mechanic - block breaks behind player
    public static void blockBreakTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    for(Player p : GameRegister.getIngamePlayers()) {
                        TntRunGame game = getPlayedTntRunGame(p);
                        if(game == null) return;

                        if(game.getArena().isInSetupMode()) {
                            return;
                        }

                        Location currentPlayerUnderground = p.getLocation().subtract(0, 1, 0);

                        Vector[] undergroundSearchVectors = {new Vector(0,0,0), new Vector(0,-1,0), new Vector(0,-2,0),
                                new Vector(0.3,-1,0), new Vector(-0.3,-1,0), new Vector(0,-1,0.3), new Vector(0,-1,-0.3),
                                new Vector(0.3,-1,0.3), new Vector(0.3,-1,-0.3), new Vector(-0.3,-1,0.3), new Vector(-0.3,-1,-0.3),
                                new Vector(0.3,0,0), new Vector(-0.3,0,0), new Vector(0,0,0.3), new Vector(0,0,-0.3)};

                        for(Vector vector : undergroundSearchVectors) {
                            if(currentPlayerUnderground.clone().add(vector).getBlock().getType() == ((TntRunArena) game.getArena()).getLayerMaterial()) {
                                currentPlayerUnderground = currentPlayerUnderground.add(vector);
                                break;
                            }
                        }

                        Location removeBlock = currentPlayerUnderground;

                        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("MinigameBox"), () -> {
                            removeBlock.getBlock().setType(Material.AIR);
                        }, ((TntRunArena) game.getArena()).getBreakDelayTicks());

                    }
                } catch (Exception ignored) {

                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("MinigameBox"), 0L, 1L);
    }

    //Returns SpleefGame, if player is in one and is activly playing
    public static TntRunGame getPlayedTntRunGame(Player player) {

        if(GameRegister.isPlayerInGame(player)) {
            if(GameRegister.getPlayersGame(player) instanceof TntRunGame g) {
                if(g.getArena().isPlayerIngame(player)) {
                    return g;
                }
            }
        }

        return null;

    }

}
