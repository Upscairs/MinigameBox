package dev.upscairs.minigameBox.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveQueueEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    private final Player player;

    public PlayerLeaveQueueEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
