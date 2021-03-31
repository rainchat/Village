package com.rainchat.villages.resources.listeners;

import com.rainchat.villages.Villages;
import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.VillageManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class VillageListener implements Listener {

    private final VillageManager villageManager;

    public VillageListener(Villages villages) {
        this.villageManager = villages.getVillageManager();
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (villageManager.hasAdminMode(player.getUniqueId())) return;

        Village currentVillage = villageManager.getVillage(block.getChunk());
        if (currentVillage == null) return;

        Village playerVillage = villageManager.getVillage(player);

        if (playerVillage == currentVillage) {
            if (villageManager.checkPermission(VillagePermission.BLOCK_BREAK, playerVillage, player))
                event.setCancelled(true);
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (villageManager.hasAdminMode(player.getUniqueId())) return;

        Village currentVillage = villageManager.getVillage(block.getChunk());
        if (currentVillage == null) return;

        Village playerVillage = villageManager.getVillage(player);

        if (playerVillage == currentVillage) {
            if (villageManager.checkPermission(VillagePermission.BLOCK_PLACE, playerVillage, player))
                event.setCancelled(true);
        } else {
            event.setCancelled(true);
        }
    }

}
