package com.rainchat.villages.api.placeholder.replacer;

import com.rainchat.rainlib.placeholder.BaseReplacements;
import com.rainchat.villages.Villages;
import com.rainchat.villages.data.village.VillageMember;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VillageMemberReplacements extends BaseReplacements<Player> {

    private final Villages plugin;
    private final VillageMember villageMember;


    public VillageMemberReplacements(VillageMember villageMember) {
        super("member_");

        this.villageMember = villageMember;
        this.plugin = Villages.getInstance();
    }


    @Override
    public Class<Player> forClass() {
        return Player.class;
    }

    @Override
    public String getReplacement(String base, String fullKey) {

        if ("role".equals(base)) {
            return villageMember.getRole();
        }
        if ("uuid".equals(base)) {
            return villageMember.getUniqueId().toString();
        }
        if ("name".equals(base)) {
            return Bukkit.getOfflinePlayer(villageMember.getUniqueId()).getName();
        }

        return "";
    }

}