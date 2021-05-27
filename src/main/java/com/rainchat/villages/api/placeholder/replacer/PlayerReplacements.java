package com.rainchat.villages.api.placeholder.replacer;


import com.rainchat.rainlib.placeholder.BaseReplacements;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerReplacements extends BaseReplacements<Player> {
    private final OfflinePlayer player;


    public PlayerReplacements(OfflinePlayer player) {
        super("player_");

        this.player = player;
    }

    public PlayerReplacements(UUID playerUUID) {
        super("player_");

        this.player = Bukkit.getOfflinePlayer(playerUUID);
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
