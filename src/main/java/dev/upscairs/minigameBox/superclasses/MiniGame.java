package dev.upscairs.minigameBox.superclasses;

import dev.upscairs.minigameBox.MinigameBox;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameTypes;
import dev.upscairs.minigameBox.base_functionality.managing.config.MessagesConfig;
import dev.upscairs.minigameBox.utils.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MiniGame {

    private final MinigameBox plugin;
    private MinigameArena arena;
    private boolean gameRunning;
    private ArrayList<Player> droppedOutPlayers = new ArrayList<>();

    private boolean reloadFlag;

    public MiniGame(MinigameArena arena) {
        this.plugin = (MinigameBox) Bukkit.getPluginManager().getPlugin("MinigameBox");
        this.arena = arena;
        gameRunning = false;
        reloadFlag = false;
    }

    //Checking if player can join queue, placing him, attempting gamestart
    public boolean playerJoinQueue(Player player) {

        if(GameRegister.isPlayerInGame(player)) {
            player.sendMessage(MessagesConfig.get().getString("game.error-already-in-game"));
            return false;
        }

        if(arena.isQueueOpen()) {
            arena.addPlayerToQueue(player);
            GameRegister.putPlayerInGame(player, this);

            startGameAttempt();

            player.sendMessage(MessagesConfig.get().getString("game.success-queue-joined") + arena.getQueueLength());
            return true;
        }
        else {
            player.sendMessage(MessagesConfig.get().getString("game.error-queue-closed"));
        }

        return false;
    }

    //Removes player from queue or game
    public void playerRemove(Player player) {
        GameRegister.removePlayerFromGame(player);
        if(arena.isPlayerInQueue(player)) {
            arena.removePlayerFromQueue(player);
            player.sendMessage(MessagesConfig.get().getString("game.success-queue-left"));

        }
        else if(arena.isPlayerIngame(player)) {
            player.sendMessage(MessagesConfig.get().getString("game.info-out-of-game"));
            droppedOutPlayers.add(player);
            arena.removePlayerFromGame(player);
            if(arena.gameEndingState()) {
                endGame(false);
            }
        }
        else {
            player.sendMessage(MessagesConfig.get().getString("game.error-not-in-game"));
        }

    }

    //Attempting to start game, fails if game running, not enough players in, or arena can't start itself
    public boolean startGameAttempt() {
        if(gameRunning) {
            return false;
        }

        if(!arena.enoughPlayersToStart()) {
            return false;
        }

        if(arena.isContinuous()) {
            startGameCountdown();
            return true;
        }
        return false;
    }

    //Starting game in x seconds, but waiting for other players to join
    public void startGameCountdown() {
        GameUtils.broadcastMessage(arena.getOutsideLocation(), MessagesConfig.get().getString("broadcast.info-start-game-countdown") + arena.getFillupWaitingTimeSec() + "s");
        gameRunning = true;
        arena.regenerateArena();
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                startGameFinal(false);
            }
        }, arena.getFillupWaitingTimeSec()*20L);

    }

    //Starting game, players get placed in arena
    public void startGameFinal(boolean force) {

        //Mainly for command use - don't start, if game running
        if(!arena.getIngamePlayers().isEmpty()) {
            return;
        }

        //Aborting if players left
        if(!arena.enoughPlayersToStart() && !force) {
            startGameAttempt();
            GameUtils.broadcastMessage(arena.getOutsideLocation(), MessagesConfig.get().getString("broadcast.issue-start-aborted-playercount"));
            gameRunning = false;
            return;
        }

        GameUtils.broadcastMessage(arena.getOutsideLocation(), MessagesConfig.get().getString("broadcast.info-start-game-final") + arena.getSetupTimeSec() + "s");

        gameRunning = true;
        arena.setQueuedPlayersIngame();

        arena.movePlayersIn();

        MiniGame gameInstance = this;
        arena.setInSetupMode(true);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            //Setup time in arena
            @Override
            public void run() {
                arena.setInSetupMode(false);
                GameUtils.broadcastMessage(arena.getOutsideLocation(), MessagesConfig.get().getString("broadcast.info-setup-time-over"));
            }
        }, arena.getSetupTimeSec()*20L);

    }

    //Ending the game, moving players out, removing tags, giving reward
    //If force, stopping queue, no reward
    public void endGame(boolean force) {

        HashSet<Player> winners = new HashSet<>();
        arena.getIngamePlayers().forEach(player -> {
            winners.add(player);
        });
        HashSet<Player> losers = new HashSet<>();
        droppedOutPlayers.forEach(player -> losers.add(player));

        arena.flushIngamePlayers();

        gameRunning = false;

        if(force) {
            arena.editArgs("continuous", "false");
            GameUtils.broadcastMessage(arena.getOutsideLocation(), MessagesConfig.get().getString("broadcast.issue-game-end-force"));
            return;
        }

        StringBuilder winnersString = new StringBuilder();
        for(Player winner : winners.stream().toList()) {
            winnersString.append(winner.getName() + ", ");
            GameRegister.removePlayerFromGame(winner);
        }
        if(winnersString.length() > 2) {
            winnersString.delete(winnersString.length() - 2, winnersString.length() - 1);
        }

        GameUtils.broadcastMessage(arena.getOutsideLocation(), MessagesConfig.get().getString("broadcast.info-game-end-winner-announce") + winnersString.toString());

        GameUtils.grantWinnersReward(winners, losers, this);

        droppedOutPlayers.clear();

        if(reloadFlag) {
            arena.reloadSettings();
        }

        startGameAttempt();

    }

    public void setArena(MinigameArena arena) {
        this.arena = arena;
    }

    public MinigameArena getArena() {
        return arena;
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

    public void movePlayersIn() {
        //To Override
    }

    public void flagForReload() {
        reloadFlag = true;
    }


}
