package dev.upscairs.minigameBox.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinQueueEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final String arenaName;

    public PlayerJoinQueueEvent(Player player, String arenaName) {
        this.player = player;
        this.arenaName = arenaName;
    }

    public Player getPlayer() {
        return player;
    }

    public String getArenaName() {
        return arenaName;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
