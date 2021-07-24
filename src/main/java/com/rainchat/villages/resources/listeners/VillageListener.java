package com.rainchat.villages.resources.listeners;

import com.rainchat.villages.api.events.ClaimVillageEvent;
import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillagePlayer;
import com.rainchat.villages.managers.VillageManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class VillageListener implements Listener {

    private final VillageManager villageManager;

    public VillageListener(VillageManager villageManager) {
        this.villageManager = villageManager;
    }


    @EventHandler
    public void Teleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        VillagePlayer landPlayer = villageManager.getVillagePlayer(player);

        if (event.getFrom().getChunk() != player.getLocation().getChunk()) {
            Village village = villageManager.getVillage(player.getLocation().getChunk());
            if (landPlayer.getCurrentVillage() != village) {
                landPlayer.setCurrentLand(village);
            }
        }
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
            if (villageManager.hasPermission(VillagePermission.BLOCK_BREAK, playerVillage, player, block.getLocation()))
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
            if (villageManager.hasPermission(VillagePermission.BLOCK_PLACE, playerVillage, player, block.getLocation()))
                event.setCancelled(true);
        } else {
            event.setCancelled(true);
        }
    }

}
