package com.rainchat.villages.resources.commands.subcommands;


import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.data.village.VillageRequest;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.entity.Player;

import java.util.List;

public class DisbandCommand extends Command {

    private final VillageManager villageManager;

    public DisbandCommand(VillageManager villageManager) {
        super("disband", "disband");
        this.villageManager = villageManager;
    }

    @Override
    public boolean run(Player player, String[] args) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageManager.checkPermission(VillagePermission.DISBAND, village, villageMember.getUniqueId())) {
                VillageRequest villageRequest = villageManager.getRequest(player);
                if (villageRequest == null) {
                    villageRequest = new VillageRequest(village, player.getUniqueId(), null, VillageRequest.VillageRequestAction.DISBAND);
                    villageRequest.send();
                    villageManager.addPlayer(villageRequest, player);
                } else {
                    player.sendMessage(Chat.format(Message.REQUEST_PENDING.toString()));
                }
            } else {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.DISBAND.name())));
            }
        } else {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
        }
        return false;
    }

    @Override
    public List<String> tabRun(Player player, String[] args) {
        return null;
    }
}
