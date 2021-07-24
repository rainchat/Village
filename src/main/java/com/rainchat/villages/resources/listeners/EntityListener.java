package com.rainchat.villages.resources.listeners;

import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.FlagManager;
import com.rainchat.villages.managers.VillageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EntityListener implements Listener {

    private final VillageManager villageManager;

    public EntityListener(VillageManager villageManager) {
        this.villageManager = villageManager;
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {

        for (Block block : event.blockList()) {
            Village village = villageManager.getVillage(block.getChunk());
            if (village == null) {
                continue;
            }
            if (village.hasPermission("EXPLOSIONS", block.getLocation())) {
                event.blockList().remove(block);
                event.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler
    public void onDamageAnimal(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Animals && event.getDamager() instanceof Player) {
            Entity entity = event.getEntity();
            Village entityVillage = villageManager.getVillage(entity.getLocation().getChunk());

            Player player2 = (Player) event.getDamager();
            Village otherVillage = villageManager.getVillage(player2);

            if (villageManager.hasAdminMode(player2.getUniqueId())) return;

            if (entityVillage != null) {
                if (otherVillage == entityVillage) {
                    return;
                }
                if (entityVillage.hasPermission("DAMAGE_ANIMALS", entity.getLocation())) {
                    event.setCancelled(true);
                }
            }

        }

    }


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player1 = (Player) event.getEntity();
            Village entityVillage = villageManager.getVillage(player1.getLocation().getChunk());

            Player player2 = (Player) event.getDamager();
            Village otherVillage = villageManager.getVillage(player2.getLocation().getChunk());

            if (villageManager.hasAdminMode(player2.getUniqueId())) return;

            if (entityVillage != null) {
                if (entityVillage.hasPermission("PVP", player1.getLocation())) {
                    event.setCancelled(true);
                }
            }
            if (otherVillage != null) {
                if (otherVillage.hasPermission("PVP", player2.getLocation())) {
                    event.setCancelled(true);
                }
            }


        }

    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {

        for (Block block : event.blockList()) {
            Village village = villageManager.getVillage(block.getChunk());
            if (village == null) {
                continue;
            }
            if (village.hasPermission("EXPLOSIONS", block.getLocation())) {
                event.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler
    public void itemFrameBreak(HangingBreakByEntityEvent event) {
        event.setCancelled(true);

    }

    @EventHandler
    public void itemFramePlace(HangingPlaceEvent event) {
        Village village = villageManager.getVillage(event.getEntity().getLocation().getChunk());
        if (village == null) {
            return;
        }
        Player player = event.getPlayer();
        if (!village.hasMember(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void armourStandEvents(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();
        Village village = villageManager.getVillage(player.getLocation().getChunk());
        if (village == null) {
            return;
        }
        if (!village.hasMember(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getItem() == null) {
            return;
        }
        if (!(event.getItem().getType().equals(Material.ARMOR_STAND))) {
            return;
        }

        Player player = event.getPlayer();
        Village village = villageManager.getVillage(event.getClickedBlock().getChunk());
        if (village == null) {
            return;
        }
        if (!village.hasMember(player)) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame)) {
            return;
        }

        Player player = event.getPlayer();
        Village village = villageManager.getVillage(event.getRightClicked().getLocation().getChunk());
        if (village == null) {
            return;
        }
        if (!village.hasMember(player)) {
            event.setCancelled(true);
        }

    }


    @EventHandler
    public void ArmorStandDestroy(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof ItemFrame) && !(event.getEntity() instanceof ArmorStand)) {
            return;
        }
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        Village village = villageManager.getVillage(event.getEntity().getLocation().getChunk());

        if (village == null) {
            return;
        }
        if (village.hasMember(player)) {
            return;
        }

        event.setCancelled(true);
    }


}
