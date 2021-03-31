package com.rainchat.villages.api.placeholder.replacer;


import com.rainchat.villages.api.placeholder.BaseReplacements;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerReplacements extends BaseReplacements<Player> {
    private final OfflinePlayer player;


    public PlayerReplacements(OfflinePlayer player) {
        super("player_");

        this.player = player;
    }


    @Override
    public Class<Player> forClass() {
        return Player.class;
    }

    @Override
    public String getReplacement(String base, String fullKey) {


        switch (base) {
            case "name":
                return player.getName();

            case "world":
                if (player instanceof Player) {
                    return ((Player) player).getWorld().getName();
                }

                return "";

            case "uuid":
                return player.getUniqueId().toString();

        }

        return "";
    }

}
