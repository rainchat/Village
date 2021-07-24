package com.rainchat.villages.api.placeholder.replacer;

import com.rainchat.rainlib.placeholder.BaseReplacements;
import com.rainchat.villages.Villages;
import com.rainchat.villages.data.config.ConfigVillage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CustomPlaceholderReplacements extends BaseReplacements<Player> {


    public CustomPlaceholderReplacements() {
        super("customp_");
    }


    @Override
    public Class<Player> forClass() {
        return Player.class;
    }

    @Override
    public String getReplacement(String base, String fullKey) {

        for (Map.Entry<String,String> placeholder: ConfigVillage.PLACEHOLDERS.entrySet()) {
            if (placeholder.getKey().equalsIgnoreCase(base)) {
                return placeholder.getValue();
            }
        }

        return "";
    }

}