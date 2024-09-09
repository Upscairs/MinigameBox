package dev.upscairs.minigameBox.games;

import dev.upscairs.minigameBox.MinigameBox;
import dev.upscairs.minigameBox.arenas.MinigameArena;
import org.bukkit.entity.Player;

public class MiniGame {

    private MinigameBox plugin;
    private MinigameArena arena;

    public MiniGame(MinigameBox plugin, MinigameArena arena) {
        this.plugin = plugin;
        this.arena = arena;
    }

    public boolean playerJoinQueue(Player player) {
        return false;
    }

    public MinigameArena getArena() {
        return arena;
    }

    public MinigameBox getPlugin() {
        return plugin;
    }

    public void playerOut(Player player) {

    }

    public void endGame(boolean force) {

    }

}
