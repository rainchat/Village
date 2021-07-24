package com.rainchat.villages.resources.listeners;

import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.VillageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

    private final VillageManager villageManager;

    public PlayerListener(VillageManager villageManager) {
        this.villageManager = villageManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.isOp()) return;
        if (villageManager.hasAdminMode(player.getUniqueId())) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        Village currentVillage = villageManager.getVillage(block.getChunk());
        if (currentVillage == null) return;

        Village playerVillage = villageManager.getVillage(player);

        Material material = block.getType();
        if (material == Material.FURNACE) {
            if (playerVillage == currentVillage) {
                if (villageManager.hasPermission(VillagePermission.FURNACE_ACCESS, playerVillage, player, block.getLocation()))
                    event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }

        if (material == Material.BARREL) {
            if (playerVillage == currentVillage) {
                if (villageManager.hasPermission(VillagePermission.BARREL_ACCESS, playerVillage, player, block.getLocation()))
                    event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }

        if (block.getState() instanceof ShulkerBox) {
            if (playerVillage == currentVillage) {
                if (villageManager.hasPermission(VillagePermission.SHULKER_ACCESS, playerVillage, player, block.getLocation()))
                    event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }

        if (material == Material.CHEST || material == Material.TRAPPED_CHEST) {
            if (playerVillage == currentVillage) {
                if (villageManager.hasPermission(VillagePermission.CHEST_ACCESS, playerVillage, player, block.getLocation()))
                    event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }

        if (material == Material.HOPPER || material == Material.DISPENSER || material == Material.DROPPER) {
            if (playerVillage == currentVillage) {
                if (villageManager.hasPermission(VillagePermission.RESTONE_INVENTORY_ACCESS, playerVillage, player, block.getLocation()))
                    event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }

        if (material == Material.BREWING_STAND) {
            if (playerVillage == currentVillage) {
                if (villageManager.hasPermission(VillagePermission.BREWING_ACCESS, playerVillage, player, block.getLocation()))
                    event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }

        if (material == Material.ANVIL || material == Material.CHIPPED_ANVIL || material == Material.DAMAGED_ANVIL) {
            if (playerVillage == currentVillage) {
                if (villageManager.hasPermission(VillagePermission.ANVIL_ACCESS, playerVillage, player, block.getLocation()))
                    event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }

        if (material == Material.DRAGON_EGG) {
            if (playerVillage == currentVillage) {
                if (villageManager.hasPermission(VillagePermission.DRAGON_EGG_TOUCH, playerVillage, player, block.getLocation()))
                    event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();

        Block block = event.getBlockClicked();

        Village currentVillage = villageManager.getVillage(block.getChunk());
        if (currentVillage == null) return;

        Village playerVillage = villageManager.getVillage(player);

        Material material = event.getBucket();
        if (material == Material.WATER_BUCKET || material == Material.PUFFERFISH_BUCKET || material == Material.TROPICAL_FISH_BUCKET || material == Material.SALMON_BUCKET) {
            if (playerVillage == currentVillage) {
                if (villageManager.hasPermission(VillagePermission.WATER_PLACEMENT, playerVillage, player, block.getLocation()))
                    event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }

        if (material == Material.LAVA_BUCKET) {
            if (playerVillage == currentVillage) {
                if (villageManager.hasPermission(VillagePermission.LAVA_PLACEMENT, playerVillage, player, block.getLocation()))
                    event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onArmorStandInteract(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();

        ArmorStand armorStand = event.getRightClicked();
        Village currentVillage = villageManager.getVillage(armorStand.getLocation().getChunk());
        if (currentVillage == null) return;

        Village playerVillage = villageManager.getVillage(player);

        if (playerVillage == currentVillage) {
            if (villageManager.hasPermission(VillagePermission.ARMOR_STAND_ACCESS, playerVillage, player, armorStand.getLocation()))
                event.setCancelled(true);
        } else {
            event.setCancelled(true);
        }
    }


}
