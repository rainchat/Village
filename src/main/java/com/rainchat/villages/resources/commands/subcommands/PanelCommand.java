package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.Villages;
import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.menus.PanelMenu;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.entity.Player;

import java.util.List;

public class PanelCommand extends Command {

    private final Villages villages;
    private final VillageManager villageManager;

    public PanelCommand(Villages villages) {
        super("panel", "panel");
        this.villages = villages;
        this.villageManager = villages.getVillageManager();
    }

    @Override
    public boolean run(Player player, String[] args) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageManager.checkPermission(VillagePermission.PANEL, village, villageMember.getUniqueId())) {
                new PanelMenu(villages, villageManager, village).build().open(player);
            } else {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.PANEL.name())));
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
