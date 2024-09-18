package dev.upscairs.minigameBox.games;

import dev.upscairs.minigameBox.MinigameBox;
import dev.upscairs.minigameBox.arenas.MinigameArena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class MiniGame {

    private final MinigameBox plugin;
    private MinigameArena arena;
    private boolean gameRunning;
    private ArrayList<Player> droppedOutPlayers = new ArrayList<>();

    public MiniGame(MinigameArena arena) {
        this.plugin = (MinigameBox) Bukkit.getPluginManager().getPlugin("MinigameBox");
        this.arena = arena;
        gameRunning = false;
    }

    //Attempting to start game, fails if game running, not enough players in, or arena can't start itself
    public boolean startGameAttempt() {
        if(gameRunning) {
            return false;
        }

        if(!arena.enoughPlayersToStart()) {
            return false;
        }

        if(arena.isAutoStartable()) {
            startGameCountdown();
            return true;
        }
        return false;
    }

    //Starting game in x seconds, but waiting for other players to join, can get called by game master
    public void startGameCountdown() {
        gameRunning = true;
        arena.regenerateArena();
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                startGameFinal();
            }
        }, arena.getFillupWaitingTimeSec()*20L);

    }

    //Starting game, players get placed in arena and tagged
    public void startGameFinal() {
        gameRunning = true;
        arena.setQueuedPlayersIngame();
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                for(Player p : arena.getIngamePlayers()) {
                    p.setMetadata("GameName", new FixedMetadataValue(getPlugin(), arena.getName()));
                }
            }
        }, arena.getSetupTimeSec()*20L);

    }

    //Checking if player can join queue, placing him, tagging him, attempting gamestart
    public boolean playerJoinQueue(Player player) {
        if(arena.isQueueOpen()) {
            arena.addPlayerToQueue(player);
            player.setMetadata("GameName", new FixedMetadataValue(getPlugin(), "#PlayerIsInQueue#"));
            startGameAttempt();
        }
        return false;
    }

    //Removing player from queues
    public boolean playerLeaveQueue(Player player) {
        if(arena.isPlayerInQueue(player)) {
            arena.removePlayerFromQueue(player);
            player.removeMetadata("GameName", getPlugin());
            return true;
        }
        return false;
    }

    //Player out, removing from arena, removing tag
    public void playerOut(Player player) {
        droppedOutPlayers.add(player);
        arena.removePlayerFromGame(player);
        player.removeMetadata("GameName", plugin);
        if(arena.gameEndingState()) {
            endGame(false);
        }
    }

    //Ending the game, moving players out, removing tags, giving reward
    //If force, stopping queue, no reward
    public void endGame(boolean force) {

        for(Player player : arena.getIngamePlayers()) {
            player.removeMetadata("GameName", plugin);
            arena.removePlayerFromGame(player);
        }
        
        gameRunning = false;

        if(force) {
            arena.editArgs(4, "false");
            return;
        }

        //TODO reward
        droppedOutPlayers.clear();
        startGameAttempt();

    }

    public void setArena(MinigameArena arena) {
        this.arena = arena;
    }

    public MinigameArena getArena() {
        return arena;
    }

    public void movePlayersIn() {
    }

    public MinigameBox getPlugin() {
        return plugin;
    }
    
    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

}
