package dev.upscairs.minigameBox.games;

import dev.upscairs.minigameBox.MinigameBox;
import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.SpleefArena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class SpleefGame extends MiniGame {

    private SpleefArena arena;
    private boolean gameRunning;

    public SpleefGame(MinigameBox plugin, MinigameArena arena) {
        super(plugin, arena);

        gameRunning = false;
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
        gameRunning = true;
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                startGameFinal();
            }
        }, arena.getFillupWaitingTimeSec()*20L);

    }

    private void startGameFinal() {
        gameRunning = true;
        arena.movePlayersIn();
        //Arena needs default protection
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                for(Player p : arena.getIngamePlayers()) {
                    p.setMetadata("GameName", new FixedMetadataValue(getPlugin(), arena.getName()));
                }
            }
        }, arena.getSetupTimeSec()*20L);

    }

    @Override
    public boolean playerJoinQueue(Player player) {
        if(arena.isQueueOpen()) {
            arena.addPlayerToQueue(player);
            player.setMetadata("GameName", new FixedMetadataValue(getPlugin(), "#PlayerIsInQueue#"));
            startGameAttempt();
        }
        return false;
    }













}
