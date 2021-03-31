package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.data.village.VillageRequest;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class KickCommand extends Command {

    private final VillageManager villageManager;

    public KickCommand(VillageManager villageManager) {
        super("kick", "kick [player]");
        this.villageManager = villageManager;
    }

    @Override
    public boolean run(Player player, String[] args) {
        if (args.length == 2) {
            Village village = villageManager.getVillage(player);
            if (village != null) {
                VillageMember villageMember = village.getMember(player.getUniqueId());
                if (villageManager.checkPermission(VillagePermission.KICK_MEMBER, village, villageMember.getUniqueId())) {
                    OfflinePlayer offlinePlayer = villageManager.offlinePlayer(village, args[1]);
                    if (offlinePlayer != null) {
                        if (offlinePlayer.getUniqueId() != player.getUniqueId()) {
                            VillageRequest villageRequest = villageManager.getRequest(player);
                            if (villageRequest == null) {
                                villageRequest = new VillageRequest(village, player.getUniqueId(), offlinePlayer.getUniqueId(), VillageRequest.VillageRequestAction.KICK);
                                villageRequest.send();
                                villageManager.addPlayer(villageRequest, player);
                            } else {
                                player.sendMessage(Chat.format(Message.REQUEST_PENDING.toString()));
                            }
                        } else {
                            player.sendMessage(Chat.format(Message.REQUEST_KICK_SELF.toString()));
                        }
                    } else {
                        player.sendMessage(Chat.format(Message.VILLAGE_MEMBER_NULL.toString().replace("{0}", args[1])));
                    }
                } else {
                    player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.KICK_MEMBER.name())));
                }
            } else {
                player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
            }
        } else {
            player.sendMessage(Chat.format(Message.USAGE.toString().replace("{0}", "/village " + getUsage())));
        }
        return true;
    }

    @Override
    public List<String> tabRun(Player player, String[] args) {
        return null;
    }
}
