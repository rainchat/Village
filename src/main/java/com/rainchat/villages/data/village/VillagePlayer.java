package com.rainchat.villages.data.village;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class VillagePlayer {
    private final Player player;
    private Village village;
    private Location pos1;
    private Location pos2;

    public VillagePlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Village getCurrentVillage() {
        return village;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public boolean isCuboid() {
        return pos1 != null && pos2 != null;
    }

    public void setCurrentLand(Village village) {
        this.village = village;
    }
}