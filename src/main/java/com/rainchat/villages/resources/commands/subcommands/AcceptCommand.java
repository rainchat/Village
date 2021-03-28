package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.data.village.VillageRequest;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.entity.Player;

import java.util.List;

public class AcceptCommand extends Command {

    private final VillageManager villageManager;

    public AcceptCommand(VillageManager villageManager) {
        super("accept", "accept");
        this.villageManager = villageManager;
    }

    @Override
    public boolean run(Player player, String[] args) {
        VillageRequest villageRequest = villageManager.getRequest(player);
        if (villageRequest == null) {
            player.sendMessage(Chat.format(Message.REQUEST_NULL.toString()));
        } else {
            villageRequest.complete(villageManager);
            villageManager.removeP(player);
        }
        return false;
    }

    @Override
    public List<String> tabRun(Player player, String[] args) {
        return null;
    }
}
