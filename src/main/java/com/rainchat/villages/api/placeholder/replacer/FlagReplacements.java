package com.rainchat.villages.api.placeholder.replacer;

import com.rainchat.rainlib.placeholder.BaseReplacements;
import com.rainchat.villages.Villages;
import com.rainchat.villages.data.config.ConfigFlags;
import com.rainchat.villages.data.config.ConfigVillage;
import com.rainchat.villages.data.flags.FlagItem;
import com.rainchat.villages.data.flags.FlagVillage;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FlagReplacements extends BaseReplacements<Player> {

    private final Villages plugin;
    private final FlagItem flagItem;
    private final FlagVillage flagVillage;
    private final boolean status;


    public FlagReplacements(FlagVillage flagVillage, FlagItem flagItem, boolean status) {
        super("gflag_");

        this.flagItem = flagItem;
        this.flagVillage = flagVillage;
        this.status = status;
        this.plugin = Villages.getInstance();
    }


    @Override
    public Class<Player> forClass() {
        return Player.class;
    }

    @Override
    public String getReplacement(String base, String fullKey) {

        if ("getName".equalsIgnoreCase(base)) {
            if (flagItem == null) {
                return flagVillage.getDisplayName();
            }
            return flagItem.getName();
        }
        if ("getMaterial".equalsIgnoreCase(base)) {
            if (flagItem == null) {
                return flagVillage.getItem().getType().toString();
            }
            return flagItem.getMaterial().toString();
        }
        if ("getStatus".equalsIgnoreCase(base)) {
            if (status) {
                return ConfigVillage.PLACEHOLDERS.get("enable-flag");
            }
            return ConfigVillage.PLACEHOLDERS.get("disable-flag");
        }

        return "";
    }

    public Material getMaterial() {
        if (flagItem == null) {
            return  flagVillage.getItem().getType();
        }
        return flagItem.getMaterial();
    }


}