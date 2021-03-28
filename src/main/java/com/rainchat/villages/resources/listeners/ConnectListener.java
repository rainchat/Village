package com.rainchat.villages.resources.listeners;

import com.rainchat.villages.Villages;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.VillageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectListener implements Listener {


    private final VillageManager villageManager;

    public ConnectListener(Villages villages) {
        this.villageManager = villages.getVillageManager();
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Village village = villageManager.getVillage(event.getPlayer());
        if (village != null) {
            villageManager.updateLastActive(village, System.currentTimeMillis());
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Village village = villageManager.getVillage(event.getPlayer());
        if (village != null) {
            villageManager.updateLastActive(village, System.currentTimeMillis());
        }
    }


}
