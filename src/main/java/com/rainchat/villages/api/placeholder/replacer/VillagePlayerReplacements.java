package com.rainchat.villages.api.placeholder.replacer;


import com.rainchat.rainlib.placeholder.BaseReplacements;
import com.rainchat.villages.Villages;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.VillageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class VillagePlayerReplacements extends BaseReplacements<Player> {
    private final Player player;
    private final Villages plugin;
    private final VillageManager islandManager;


    public VillagePlayerReplacements(Player player) {
        super("village_");

        this.player = player;
        this.plugin = Villages.getInstance();
        this.islandManager = Villages.getAPI().getVillageManage();
    }


    @Override
    public Class<Player> forClass() {
        return Player.class;
    }

    @Override
    public String getReplacement(String base, String fullKey) {

        Village village = islandManager.getVillage(player);

        if (village != null) {
            switch (base) {
                case "name":
                    return Objects.requireNonNull(Bukkit.getPlayer(village.getOwner())).getName();
                case "owner":
                    return plugin.getServer().getOfflinePlayer(village.getOwner()).getName();
                case "members":
                    return String.valueOf(village.getVillageMembers().size());
                case "claims":
                    return String.valueOf(village.getVillageClaims().size());
            }
        }

        village = islandManager.getVillage(player.getLocation().getChunk());

        if (village != null) {
            switch (base) {
                case "target_name":
                    return Objects.requireNonNull(Bukkit.getPlayer(village.getOwner())).getName();
                case "target_owner":
                    return plugin.getServer().getOfflinePlayer(village.getOwner()).getName();
                case "target_members":
                    return String.valueOf(village.getVillageMembers().size());
                case "target_claims":
                    return String.valueOf(village.getVillageClaims().size());
            }
        }

        return "";
    }
}
