package dev.upscairs.minigameBox.arenas.creation_and_storing;

import dev.upscairs.minigameBox.MinigameBox;
import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.config.MessagesConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PendingArenaCreations {

    private static HashMap<Player, ArenaCreationWizard> setups = new HashMap<>();

    public static void newSetup(Player player, String gameType, String arenaName) {

        if(setups.containsKey(player)) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-already-creating"));
            return;
        }

        if(!ArenaCreationWizard.getGamemodes().contains(gameType)) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-unknown-gametype"));
            return;
        }

        if(GameRegister.gameExists(arenaName) || arenaName.equalsIgnoreCase("#PlayerIsInQueue#")) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-duplicate-name"));
            return;
        }

        setups.put(player, new ArenaCreationWizard(gameType, arenaName, player));
        player.sendMessage(MessagesConfig.get().getString("managing.success-wizard-creation"));

    }

    public static MinigameArena giveNextVar(Player player, Location location) {

        if(!setups.containsKey(player)) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-no-wizard-running"));
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
