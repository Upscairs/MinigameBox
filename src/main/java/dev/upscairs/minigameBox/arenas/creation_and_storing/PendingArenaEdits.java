package dev.upscairs.minigameBox.arenas.creation_and_storing;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.utils.Tuple;
import org.bukkit.entity.Player;

import java.util.HashMap;

public abstract class PendingArenaEdits {

    private static final HashMap<Player, Tuple<MinigameArena, Integer>> pendingEdits = new HashMap<>();


    public static void newEditInstance(Player player, MinigameArena arena, int argIndex) {

        if (pendingEdits.containsKey(player)) {
            player.sendMessage("Already Editing");
            return;
        }

        pendingEdits.put(player, new Tuple<>(arena, argIndex));
        player.sendMessage("Editing mode..");


    }

    public static void giveArg(Player player, String newSetting) {

        if(!pendingEdits.containsKey(player)) {
            player.sendMessage("No Editing");
            return;
        }

        try {
            pendingEdits.get(player).left.editArgs(pendingEdits.get(player).right, newSetting);
        } catch (IllegalArgumentException e) {
            player.sendMessage(e.getMessage());
            return;
        }

        removePlayer(player);

    }

    public static void removePlayer(Player player) {
        pendingEdits.remove(player);
        player.sendMessage("Left editing mode");
    }

}
