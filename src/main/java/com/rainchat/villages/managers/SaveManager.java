package com.rainchat.villages.managers;

import com.google.gson.reflect.TypeToken;
import com.rainchat.villages.Villages;
import com.rainchat.villages.data.village.Village;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;

public class SaveManager {
    static private int tusk = 0;


    private final VillageManager villageManager;

    public SaveManager(VillageManager villages) {
        villageManager = villages;
    }


    public void start() {
        if (tusk != -1) {
            Bukkit.getScheduler().cancelTask(tusk);
        }


        tusk = Bukkit.getScheduler().scheduleSyncRepeatingTask(Villages.getInstance(), () -> {
            System.out.println("==========================================================");
            Villages.getInstance().getLogger().info("Start saving village data's");
            int i = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                Village village = villageManager.getVillage(player);
                if (village != null) {
                    i++;
                    villageManager.updateLastActive(village, System.currentTimeMillis());
                }
            }
            Villages.getInstance().getLogger().info("Updated village activity for online players - " + i);

            villageManager.deleteNonActive();

            villageManager.unload();
            villageManager.load(new TypeToken<Set<Village>>() {
            }.getType());
            Villages.getInstance().getLogger().info("Update all villages");
            System.out.println("==========================================================");
        }, 20, 20 * 60 * 60 * 6);
    }


    public void close() {
        Bukkit.getScheduler().cancelTask(tusk);
    }
}
