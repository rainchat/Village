package com.rainchat.villages.resources.listeners;

import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillagePlayer;
import com.rainchat.villages.managers.VillageManager;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectListener implements Listener {


    private final VillageManager villageManager;

    public ConnectListener(VillageManager villageManager) {
        this.villageManager = villageManager;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Village village = villageManager.getVillage(event.getPlayer());



        if (village != null) {
            villageManager.updateLastActive(village, System.currentTimeMillis());
        }

        VillagePlayer landPlayer = villageManager.getVillagePlayer(event.getPlayer());
        Village fromLand = landPlayer.getCurrentVillage();

        Chunk toChunk = event.getPlayer().getLocation().getChunk();
        Village toLand = villageManager.getVillage(toChunk);

        if (village != null) {
            landPlayer.setCurrentLand(village);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Village village = villageManager.getVillage(event.getPlayer());
        if (village != null) {
            villageManager.updateLastActive(village, System.currentTimeMillis());
        }

        villageManager.removeVillagePlayer(event.getPlayer());
    }


}
