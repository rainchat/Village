package com.rainchat.villages.api.placeholder.replacer;

import com.rainchat.rainlib.placeholder.BaseReplacements;
import com.rainchat.villages.Villages;
import org.bukkit.entity.Player;

public class EconomyReplacements extends BaseReplacements<Player> {

    private final Villages plugin;
    private final double money_Need;


    public EconomyReplacements(int money_Need) {
        super("econ_");

        this.money_Need = money_Need;
        this.plugin = Villages.getInstance();
    }


    @Override
    public Class<Player> forClass() {
        return Player.class;
    }

    @Override
    public String getReplacement(String base, String fullKey) {

        if ("need".equals(base)) {
            return String.valueOf(money_Need);
        }

        return "";
    }

}