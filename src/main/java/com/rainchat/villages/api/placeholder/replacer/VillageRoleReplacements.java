package com.rainchat.villages.api.placeholder.replacer;

import com.rainchat.rainlib.placeholder.BaseReplacements;
import com.rainchat.villages.Villages;
import com.rainchat.villages.data.village.VillageRole;
import org.bukkit.entity.Player;

public class VillageRoleReplacements extends BaseReplacements<Player> {

    private final Villages plugin;
    private final VillageRole villageRole;


    public VillageRoleReplacements(VillageRole villageRole) {
        super("role_");

        this.villageRole = villageRole;
        this.plugin = Villages.getInstance();
    }


    @Override
    public Class<Player> forClass() {
        return Player.class;
    }

    @Override
    public String getReplacement(String base, String fullKey) {

        if ("name".equals(base)) {
            return villageRole.getName();
        }

        return "";
    }

}