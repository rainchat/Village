package com.rainchat.villages.resources.listeners;

import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.VillageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;

public class BurnEvent implements Listener {

    private final VillageManager villageManager;

    public BurnEvent(VillageManager villageManager) {
        this.villageManager = villageManager;
    }

    @EventHandler
    public void onBlockIgnite(BlockSpreadEvent event) {
        if (event.getSource().getType() != Material.FIRE) return;

        Village entityVillage = villageManager.getVillage(event.getBlock().getChunk());
        if (entityVillage == null) {
            return;
        }

        if (entityVillage.hasPermission("FIRE_SPREAD", event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        Village entityVillage = villageManager.getVillage(event.getBlock().getChunk());
        if (entityVillage == null) {
            return;
        }

        if (entityVillage.hasPermission("FIRE_SPREAD", event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }


}
