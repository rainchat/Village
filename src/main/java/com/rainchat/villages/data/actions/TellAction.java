package com.rainchat.villages.data.actions;

import org.bukkit.entity.Player;

public class TellAction implements Action{

    @Override
    public void execute(Player player, String action, String... options) {
        player.sendMessage(action);
    }
}
