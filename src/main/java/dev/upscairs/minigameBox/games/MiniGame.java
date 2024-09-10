package dev.upscairs.minigameBox.games;

import dev.upscairs.minigameBox.MinigameBox;
import dev.upscairs.minigameBox.arenas.MinigameArena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class MiniGame {

    private MinigameBox plugin;
    private MinigameArena arena;
    private boolean gameRunning;
    private ArrayList<Player> droppedOutPlayers = new ArrayList<>();

    public MiniGame(MinigameBox plugin, MinigameArena arena) {
        this.plugin = plugin;
        this.arena = arena;
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

    public void startGameCountdown() {
        gameRunning = true;
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                startGameFinal();
            }
        }, arena.getFillupWaitingTimeSec()*20L);

    }

    public void startGameFinal() {
        gameRunning = true;
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

    public boolean playerJoinQueue(Player player) {
        if(arena.isQueueOpen()) {
            arena.addPlayerToQueue(player);
            player.setMetadata("GameName", new FixedMetadataValue(getPlugin(), "#PlayerIsInQueue#"));
            startGameAttempt();
        }
        return false;
    }

    public MinigameArena getArena() {
        return arena;
    }

    public MinigameBox getPlugin() {
        return plugin;
    }

    public void playerOut(Player player) {
        droppedOutPlayers.add(player);
        arena.removePlayerFromGame(player);
        player.removeMetadata("GameName", plugin);
        if(arena.gameEndingState()) {
            endGame(false);
        }
    }
    

    public void endGame(boolean force) {

        for(Player player : arena.getIngamePlayers()) {
            player.removeMetadata("GameName", plugin);
            arena.removePlayerFromGame(player);
        }
        
        gameRunning = false;

        if(force) {
            arena.setContinuous(false);
            return;
        }

        //TODO reward
        startGameAttempt();

    }
    
    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

}
