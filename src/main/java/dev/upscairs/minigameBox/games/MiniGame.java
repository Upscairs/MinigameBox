package dev.upscairs.minigameBox.games;

import dev.upscairs.minigameBox.MinigameBox;
import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import dev.upscairs.minigameBox.config.MessagesConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                startGameFinal();
            }
        }, arena.getFillupWaitingTimeSec()*20L);

    }

    //Starting game, players get placed in arena and tagged
    public void startGameFinal() {

        //Aborting if players left
        if(!arena.enoughPlayersToStart()) {
            startGameAttempt();
            return;
        }

        gameRunning = true;
        arena.setQueuedPlayersIngame();

        MiniGame gameInstance = this;
        arena.setInSetupMode(true);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            //Setup time in arena
            @Override
            public void run() {
                arena.setInSetupMode(false);
            }
        }, arena.getSetupTimeSec()*20L);

    }

    //Checking if player can join queue, placing him, tagging him, attempting gamestart
    public boolean playerJoinQueue(Player player) {

        if(GameRegister.isPlayerInGame(player)) {
            player.sendMessage(MessagesConfig.get().getString("game.error-already-in-game"));
            return false;
        }

        if(arena.isQueueOpen()) {
            arena.addPlayerToQueue(player);
            GameRegister.putPlayerInGame(player, this);

            startGameAttempt();

            player.sendMessage(MessagesConfig.get().getString("game.success-queue-joined"));
            return true;
        }
        else {
            player.sendMessage(MessagesConfig.get().getString("game.error-queue-closed"));
        }

        return false;
    }

    //Removes player from queue or game, if success -> return true
    public void playerRemove(Player player) {
        if(arena.isPlayerInQueue(player)) {
            arena.removePlayerFromQueue(player);
            player.sendMessage(MessagesConfig.get().getString("game.success-queue-left"));

        }
        else if(arena.isPlayerIngame(player)) {
            droppedOutPlayers.add(player);
            arena.removePlayerFromGame(player);
            if(arena.gameEndingState()) {
                endGame(false);
            }
            player.sendMessage(MessagesConfig.get().getString("game.info-out-of-game"));
        }
        else {
            player.sendMessage(MessagesConfig.get().getString("game.error-not-in-queue"));
        }
        GameRegister.removePlayerFromGame(player);

    }

    //Ending the game, moving players out, removing tags, giving reward
    //If force, stopping queue, no reward
    public void endGame(boolean force) {

        for(Player player : arena.getIngamePlayers()) {
            GameRegister.removePlayerFromGame(player);
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
        //To Override
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
