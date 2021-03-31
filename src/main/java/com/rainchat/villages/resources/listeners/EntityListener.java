package com.rainchat.villages.resources.listeners;

import com.rainchat.villages.Villages;
import com.rainchat.villages.data.enums.VillageGlobalPermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.VillageManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityListener implements Listener {

    private final VillageManager villageManager;

    public EntityListener(Villages villages) {
        this.villageManager = villages.getVillageManager();
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {

        for (Block block : event.blockList()) {
            Village village = villageManager.getVillage(block.getChunk());
            if (village == null) {
                continue;
            }
            if (village.hasPermission(VillageGlobalPermission.EXPLOSIONS)) {
                event.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player1 = (Player) event.getEntity();
            Village entityVillage = villageManager.getVillage(player1.getChunk());

            Player player2 = (Player) event.getDamager();
            Village otherVillage = villageManager.getVillage(player2.getChunk());

            if (villageManager.hasAdminMode(player2.getUniqueId())) return;

            if (entityVillage != null) {
                if (entityVillage.hasPermission(VillageGlobalPermission.PVP)) {
                    event.setCancelled(true);
                }
            }
            if (otherVillage != null) {
                if (otherVillage.hasPermission(VillageGlobalPermission.PVP)) {
                    event.setCancelled(true);
                }
            }


        }

    }

}
