package com.rainchat.villages.api.placeholder.replacer;

import com.rainchat.rainlib.placeholder.BaseReplacements;
import com.rainchat.villages.Villages;
import com.rainchat.villages.data.village.Village;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class VillageReplacements extends BaseReplacements<Player> {

    private final Villages plugin;
    private final Village village;


    public VillageReplacements(Village village) {
        super("village_");

        this.village = village;
        this.plugin = Villages.getInstance();
    }


    @Override
    public Class<Player> forClass() {
        return Player.class;
    }

    @Override
    public String getReplacement(String base, String fullKey) {
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
                case "max_claims":
                    return String.valueOf(Villages.getAPI().getVillageManage().checkMaxClaims(village));
            }

        }
        return "";
    }

}
