package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.data.village.VillagePermission;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHomeCommand extends Command {

    private final VillageManager villageManager;

    public SetHomeCommand(VillageManager villageManager) {
        super("sethome", "sethome");
        this.villageManager = villageManager;
    }

    @Override
    public boolean run(Player player, String[] args) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageMember.hasPermission(VillagePermission.SET_HOME) || village.getOwner().equals(player.getUniqueId()) || village.hasPermission(VillagePermission.SET_HOME)) {
                village.setLocation(player.getLocation());
                player.sendMessage(Chat.format(Message.VILLAGE_SET_HOME.toString()));
            } else {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.SET_HOME.name())));
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
