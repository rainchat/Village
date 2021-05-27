package com.rainchat.villages.api.placeholder.replacer;

import com.rainchat.rainlib.placeholder.BaseReplacements;
import com.rainchat.villages.Villages;
import com.rainchat.villages.data.village.VillageClaim;
import org.bukkit.entity.Player;

public class VillageClaimReplacements extends BaseReplacements<Player> {

    private final Villages plugin;
    private final VillageClaim villageClaim;


    public VillageClaimReplacements(VillageClaim villageClaim) {
        super("claim_");

        this.villageClaim = villageClaim;
        this.plugin = Villages.getInstance();
    }


    @Override
    public Class<Player> forClass() {
        return Player.class;
    }

    @Override
    public String getReplacement(String base, String fullKey) {

        if ("id".equals(base)) {
            return villageClaim.toString();
        }
        if ("world".equals(base)) {
            return villageClaim.getWorld();
        }

        if ("chunkx".equals(base)) {
            return String.valueOf(villageClaim.getX());
        }

        if ("chunky".equals(base)) {
            return String.valueOf(villageClaim.getZ());
        }


        return "";
    }

}