package com.rainchat.villages.api.placeholder.replacer;

import com.rainchat.rainlib.placeholder.BaseReplacements;
import com.rainchat.villages.Villages;
import com.rainchat.villages.data.village.VillageSubClaim;
import org.bukkit.entity.Player;

public class VillageSubClaimReplacements extends BaseReplacements<Player> {

    private final Villages plugin;
    private final VillageSubClaim villageSubClaim;


    public VillageSubClaimReplacements(VillageSubClaim villageSubClaim) {
        super("subclaim_");

        this.villageSubClaim = villageSubClaim;
        this.plugin = Villages.getInstance();
    }


    @Override
    public Class<Player> forClass() {
        return Player.class;
    }

    @Override
    public String getReplacement(String base, String fullKey) {

        if ("role".equals(base)) {
            return villageSubClaim.getRole();
        }

        if ("name".equals(base)) {
            return villageSubClaim.getName();
        }

        return "";
    }

}