package dev.upscairs.minigameBox.arenas.creation_and_storing;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.SpleefArena;
import dev.upscairs.minigameBox.config.MessagesConfig;
import dev.upscairs.minigameBox.games.GameTypes;
import dev.upscairs.utils.Tuple;
import org.bukkit.entity.Player;

import java.util.HashMap;

public abstract class PendingArenaEdits {

    private static final HashMap<Player, Tuple<MinigameArena, Integer>> pendingEdits = new HashMap<>();


    public static void newEditInstance(Player player, MinigameArena arena, int argIndex) {

        if (pendingEdits.containsKey(player)) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-already-editing"));
            return;
        }

        pendingEdits.put(player, new Tuple<>(arena, argIndex));
        player.sendMessage(MessagesConfig.get().getString("managing.success-editing-started"));

    }

    public static void giveArg(Player player, String newSetting) {

        MinigameArena arena = pendingEdits.get(player).left;

        if(!pendingEdits.containsKey(player)) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-not-editing"));
            return;
        }

        try {
            arena.editArgs(pendingEdits.get(player).right, newSetting);
        } catch (IllegalArgumentException e) {
            player.sendMessage(MessagesConfig.get().getString("managing.error-edit-illegal-value-input") + e.getMessage());
            return;
        }


        GameTypes gameType = GameTypes.getFromArenaClass(arena.getClass());

        if(!GameRegister.getGame(arena.getName()).isGameRunning()) {
            GameRegister.reloadGame(arena.getName(), gameType);
            GameRegister.getGame(arena.getName()).getArena().regenerateArena();
        }

        player.sendMessage(MessagesConfig.get().getString("managing.success-edited-value"));
        removePlayer(player);

    }

    public static void removePlayer(Player player) {
        pendingEdits.remove(player);
        player.sendMessage(MessagesConfig.get().getString("managing.success-editing-ended"));
    }

}
