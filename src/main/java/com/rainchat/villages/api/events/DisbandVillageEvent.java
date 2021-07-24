package com.rainchat.villages.api.events;

import com.rainchat.villages.data.village.Village;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DisbandVillageEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Village village;
    private final Player player;
    private boolean cancelled;

    public DisbandVillageEvent(Village village, Player player) {
        this.village = village;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Village getVillage() {
        return village;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

}