package dev.upscairs.minigameBox.arenas.creation_and_storing;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PendingArenaCreations {

    private static HashMap<Player, ArenaCreationWizard> setups = new HashMap<>();

    public static void newSetup(Player player, String gameType, String arenaName) {

        if(setups.containsKey(player)) {
            //Error Message
            return;
        }

        if(!ArenaCreationWizard.getGamemodes().contains(gameType)) {
            //Error Message
            return;
        }

        setups.put(player, new ArenaCreationWizard(gameType, arenaName, player));
        //Success Message

    }

    public static MinigameArena giveNextVar(Player player, Location location) {

        if(!setups.containsKey(player)) {
            //Error Message
            return null;
        }

        ArenaCreationWizard wizard = setups.get(player);
        MinigameArena arena = wizard.nextInput(location);

        if(arena != null) {
            closeSetup(player);
        }

        return arena;

    }

    public static void closeSetup(Player player) {
        setups.remove(player);
    }

}
