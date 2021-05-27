package com.rainchat.villages.api.events;

import com.rainchat.villages.data.village.Village;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeLandEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Village fromLand;
    private final Village toLand;
    private boolean cancelled;

    public PlayerChangeLandEvent(Player p, Village fl, Village tl) {
        this.player = p;
        this.fromLand = fl;
        this.toLand = tl;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Village getFromVillage() {
        return fromLand;
    }

    public Village getToVillage() {
        return toLand;
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