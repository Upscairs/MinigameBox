package dev.upscairs.minigameBox.arenas.creation_and_storing;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.config.MessagesConfig;
import dev.upscairs.minigameBox.games.GameTypes;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public abstract class PendingArenaCreations {

    private static HashMap<Player, ArenaCreationWizard> setups = new HashMap<>();

    //Checks if no other setup is running by player, gametype exists and the name is not used -> then crating setup
    public static void newSetup(Player player, String gameType, String arenaName) {

        if(setups.containsKey(player)) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-already-creating"));
            return;
        }

        if(!GameTypes.nameExists(gameType)) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-unknown-gametype"));
            return;
        }

        if(GameRegister.gameExists(arenaName) || arenaName.equalsIgnoreCase("#PlayerIsInQueue#")) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-duplicate-name"));
            return;
        }

        setups.put(player, new ArenaCreationWizard(player, GameTypes.getFromName(gameType), arenaName));
        player.sendMessage(MessagesConfig.get().getString("managing.success-wizard-creation"));

    }

    //Giving in location data for arena -> getting null, when incomplete, getting arena, when complete, saving it
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
