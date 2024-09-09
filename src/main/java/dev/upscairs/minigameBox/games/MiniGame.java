package dev.upscairs.minigameBox.games;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import org.bukkit.entity.Player;

public class MiniGame {

    private MinigameArena arena;

    public MiniGame(MinigameArena arena) {
        this.arena = arena;
    }

    public boolean playerJoinQueue(Player player) {
        return false;
    }

    public MinigameArena getArena() {
        return arena;
    }

}
