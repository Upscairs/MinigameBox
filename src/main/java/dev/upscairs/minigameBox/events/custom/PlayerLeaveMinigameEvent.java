package dev.upscairs.minigameBox.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveMinigameEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    public PlayerLeaveMinigameEvent(Player player) {
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
