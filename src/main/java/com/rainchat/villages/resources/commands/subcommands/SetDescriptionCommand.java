package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.entity.Player;

import java.util.List;

public class SetDescriptionCommand extends Command {

    private final VillageManager villageManager;

    public SetDescriptionCommand(VillageManager villageManager) {
        super("setdescription", "setdescription [description]");
        this.villageManager = villageManager;
    }

    @Override
    public boolean run(Player player, String[] args) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageManager.checkPermission(VillagePermission.SET_DESCRIPTION, village, villageMember.getUniqueId())) {
                StringBuilder description = new StringBuilder();
                for (String string : args) {
                    if (!string.equalsIgnoreCase(toString())) description.append(string).append(" ");
                }
                if (description.toString().length() <= 32) {
                    village.setDescription(description.toString());
                    player.sendMessage(Chat.format(Message.VILLAGE_SET_DESCRIPTION.toString().replace("{0}", description.toString())));
                } else {
                    player.sendMessage(Chat.format(Message.VILLAGE_DESCRIPTION_LIMIT.toString()));
                }
            } else {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.SET_DESCRIPTION.name())));
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
