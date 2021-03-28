package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.data.village.VillageRequest;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.entity.Player;

import java.util.List;

public class DenyCommand extends Command {

    private final VillageManager villageManager;

    public DenyCommand(VillageManager villageManager) {
        super("deny", "deny");
        this.villageManager = villageManager;
    }

    @Override
    public boolean run(Player player, String[] args) {
        VillageRequest villageRequest = villageManager.getRequest(player);
        if (villageRequest == null) {
            player.sendMessage(Chat.format(Message.REQUEST_NULL.toString()));
        } else {
            player.sendMessage(Chat.format(Message.REQUEST_DENIED.toString()));
            villageManager.removeP(player);
        }
        return false;
    }

    @Override
    public List<String> tabRun(Player player, String[] args) {
        return null;
    }
}
