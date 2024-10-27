package dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.changing;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameTypes;
import dev.upscairs.minigameBox.superclasses.MinigameArena;
import dev.upscairs.minigameBox.base_functionality.managing.config.MessagesConfig;
import dev.upscairs.utils.Tuple;
import org.bukkit.entity.Player;

import java.util.HashMap;

public abstract class PendingArenaEdits {

    //Maps player to arena they are editing and the index of the arg that is going to be changed
    private static final HashMap<Player, Tuple<MinigameArena, String>> pendingEdits = new HashMap<>();


    public static void newEditInstance(Player player, MinigameArena arena, String argKey) {

        //Check if player is already editing something
        if (pendingEdits.containsKey(player)) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-already-editing"));
            return;
        }

        pendingEdits.put(player, new Tuple<>(arena, argKey));
        player.sendMessage(MessagesConfig.get().getString("managing.success-editing-started"));

    }

    //Handles the input by a player for editing an arena
    public static void giveArg(Player player, String newSetting) {

        //Check if player is editing something
        if(!pendingEdits.containsKey(player)) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-not-editing"));
            return;
        }

        MinigameArena arena = pendingEdits.get(player).left;

        //Trying to change args to given input, gets returned error if input unparsable
        try {
            arena.editArgs(pendingEdits.get(player).right, newSetting);
        } catch (IllegalArgumentException e) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-edit-illegal-value-input") + e.getMessage());
            return;
        }

        GameTypes gameType = GameTypes.getFromArenaClass(arena.getClass());

        //Regenerate instantly if game not running, if game is running arena gets regenerated on next startup.
        //It's not necessary, but a visual convenience :)
        if(!GameRegister.getGame(arena.getName()).isGameRunning()) {
            GameRegister.getGame(arena.getName()).getArena().regenerateArena();
        }

        player.sendMessage(MessagesConfig.get().getString("managing.success-edited-value"));
        removePlayer(player);

    }

    //No longer mark player as "editing arena"
    public static void removePlayer(Player player) {

        if(pendingEdits.containsKey(player)) {
            pendingEdits.remove(player);
            player.sendMessage(MessagesConfig.get().getString("managing.success-editing-ended"));
        }
        else {
            player.sendMessage(MessagesConfig.get().getString("managing.error-not-editing"));
        }

    }

}
