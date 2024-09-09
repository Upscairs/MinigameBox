package dev.upscairs.minigameBox.events.handling;

import dev.upscairs.minigameBox.GameRegister;
import dev.upscairs.minigameBox.arenas.SpleefArena;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockMineEventHandler implements Listener {

    @EventHandler
    public void onStartMining(PlayerInteractEvent event) {

        Player p = event.getPlayer();

        if(event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if(!p.hasPermission("minigamebox.ingame.spleef")) {
            event.setCancelled(true);

            Block clickedBlock = event.getClickedBlock();

            String ingameName = p.getMetadata("GameName").get(0).asString();

            if(((SpleefArena) GameRegister.getGame(ingameName).getArena()).getSpleefMaterial() == clickedBlock.getType()) {
                clickedBlock.setType(Material.AIR);
            }

        }

    }

}
