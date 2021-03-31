package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.data.village.VillageRequest;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class InviteCommand extends Command {

    private final VillageManager villageManager;

    public InviteCommand(VillageManager villageManager) {
        super("invite", "invite [player]");
        this.villageManager = villageManager;
    }

    @Override
    public boolean run(Player player, String[] args) {
        if (args.length == 2) {
            Village village = villageManager.getVillage(player);
            if (village != null) {
                VillageMember villageMember = village.getMember(player.getUniqueId());
                if (villageManager.checkPermission(VillagePermission.INVITE_MEMBER, village, villageMember.getUniqueId())) {
                    VillageRequest villageRequest = villageManager.getRequest(player);
                    if (villageRequest == null) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != player) {
                            if (target != null) {
                                Village targetVillage = villageManager.getVillage(target);
                                if (targetVillage == null) {
                                    villageRequest = new VillageRequest(village, player.getUniqueId(), target.getUniqueId(), VillageRequest.VillageRequestAction.INVITE);
                                    villageRequest.send();
                                    villageManager.addPlayer(villageRequest, target);
                                } else {
                                    player.sendMessage(Chat.format(Message.REQUEST_INVITE_TARGET_NOT_NULL.toString().replace("{0}", target.getDisplayName())));
                                }
                            } else {
                                player.sendMessage(Chat.format(Message.PLAYER_OFFLINE.toString().replace("{0}", args[1])));
                            }
                        } else {
                            player.sendMessage(Chat.format(Message.REQUEST_INVITE_SELF.toString()));
                        }
                    } else {
                        player.sendMessage(Chat.format(Message.REQUEST_PENDING.toString()));
                    }
                } else {
                    player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.INVITE_MEMBER.name())));
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
