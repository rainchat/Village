package com.rainchat.villages.api.events;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeChunkEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Chunk fromChunk;
    private final Chunk toChunk;
    private final Location fromLocation;
    private final Location toLocation;
    private boolean shouldCancelMove;

    public PlayerChangeChunkEvent(Player p, Chunk fc, Chunk tc, Location fl, Location tl) {
        this.player = p;
        this.fromChunk = fc;
        this.toChunk = tc;
        this.fromLocation = fl;
        this.toLocation = tl;
        this.shouldCancelMove = false;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Chunk getFromChunk() {
        return fromChunk;
    }

    public Chunk getToChunk() {
        return toChunk;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public Location getToLocation() {
        return toLocation;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return shouldCancelMove;
    }

    @Override
    public void setCancelled(boolean b) {
        this.shouldCancelMove = b;
    }
}
