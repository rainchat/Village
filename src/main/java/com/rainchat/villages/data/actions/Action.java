package com.rainchat.villages.data.actions;

import org.bukkit.entity.Player;

public interface Action {

    void execute(Player player, String action, String... options);

}