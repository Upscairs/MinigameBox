package dev.upscairs.minigameBox.games;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.SpleefArena;
import org.bukkit.entity.Player;

public class SpleefGame extends MiniGame {

    private SpleefArena arena;
    private boolean gameRunning;

    public SpleefGame(MinigameArena arena) {
        super(arena);

        gameRunning = false;
    }

    public void nextGame() {

    }

    public boolean startGameAttempt() {
        if(gameRunning) {
            return false;
        }
        arena.setQueuedPlayersIngame();
        if(arena.isAutoStartable()) {
            startGameCountdown();
            return true;
        }
        return false;
    }

    private void startGameCountdown() {
        //Waiting period
        //Set running
        //Set status
        //Move ingame
        //etc
    }

    @Override
    public boolean playerJoinQueue(Player player) {
        if(arena.isQueueOpen()) {
            arena.addPlayerToQueue(player);
        }
        return false;
    }

    public void playerOut() {

    }

    public void endGame(boolean force) {

    }











}
