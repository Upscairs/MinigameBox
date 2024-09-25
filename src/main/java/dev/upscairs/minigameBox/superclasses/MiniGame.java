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
import java.util.ArrayList;
import java.util.List;

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
            player.sendMessage(MessagesConfig.get().getString("game.error-not-in-game"));
        }
        GameRegister.removePlayerFromGame(player);

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

    //Starting game in x seconds, but waiting for other players to join, can get called by game master
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

    //Starting game, players get placed in arena and tagged
    public void startGameFinal(boolean force) {

        //Aborting if players left
        if(!arena.enoughPlayersToStart() && !force) {
            startGameAttempt();
            GameUtils.broadcastMessage(arena.getOutsideLocation(), MessagesConfig.get().getString("broadcast.issue-start-aborted-playercount"));
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

        ArrayList<Player> winners = arena.getIngamePlayers();
        ArrayList<Player> losers = droppedOutPlayers;

        for(Player player : arena.getIngamePlayers()) {
            GameRegister.removePlayerFromGame(player);
            arena.removePlayerFromGame(player);
        }

        droppedOutPlayers.clear();
        
        gameRunning = false;

        if(force) {
            arena.editArgs(4, "false");
            GameUtils.broadcastMessage(arena.getOutsideLocation(), MessagesConfig.get().getString("broadcast.issue-game-end-force"));
            return;
        }

        StringBuilder winnersString = new StringBuilder();
        for(Player winner : winners) {
            winnersString.append(winner.getName() + ", ");
        }
        if(winnersString.length() > 2) {
            winnersString.delete(winnersString.length() - 2, winnersString.length() - 1);
        }

        GameUtils.broadcastMessage(arena.getOutsideLocation(), MessagesConfig.get().getString("broadcast.info-game-end-winner-announce") + winnersString.toString());

        grantWinnersReward(winners, losers);

        startGameAttempt();

    }

    public void grantWinnersReward(List<Player> winners, List<Player> losers) {

        //TODO customizable content

        StringBuilder losersString = new StringBuilder();

        for(Player loser : losers) {
            losersString.append(loser.getName() + ", ");
        }
        if(losersString.length() > 2) {
            losersString.delete(losersString.length() - 2, losersString.length() - 1);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedTime = LocalDateTime.now().format(formatter);

        for(Player winner : winners) {
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookMeta = (BookMeta) book.getItemMeta();

            bookMeta.setTitle("Winner of " + GameTypes.getFromGameClass(this.getClass()).getName());
            bookMeta.setAuthor(arena.getName());
            bookMeta.addPage("Congratulations!\n\nYou, " + winner.getName() + ", won a game of "
                    + GameTypes.getFromGameClass(this.getClass()).getName() + " against " + losersString.toString() + ".\n\n" +
                    "Time: " + formattedTime);
            book.setItemMeta(bookMeta);
            winner.getInventory().addItem(book);
        }

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


}
