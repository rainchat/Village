package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.entity.Player;

import java.util.List;

public class LeaveCommand extends Command {

    private final VillageManager villageManager;

    public LeaveCommand(VillageManager villageManager) {
        super("leave", "leave");
        this.villageManager = villageManager;
    }

    @Override
    public boolean run(Player player, String[] args) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            if (!village.getOwner().equals(player.getUniqueId())) {
                player.sendMessage(Chat.format(Message.VILLAGE_LEAVE.toString().replace("{0}", village.getName())));
                VillageMember villageMember = village.getMember(player.getUniqueId());
                village.remove(villageMember);
            } else {
                player.sendMessage(Chat.format(Message.VILLAGE_LEAVE_OWNER.toString()));
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
