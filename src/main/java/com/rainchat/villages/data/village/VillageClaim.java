package com.rainchat.villages.data.village;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

public class VillageClaim {

    private final String world;
    private final int x, z;

    public VillageClaim(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public Chunk getChunk() {
        return new Location(Bukkit.getWorld(world), x, 0, z).getChunk();
    }

    public boolean contains(Chunk chunk) {
        return world.equals(chunk.getWorld().getName()) && x == chunk.getX() && z == chunk.getZ();
    }

    @Override
    public String toString() {
        return world + ", " + x + ", " + z;
    }
}