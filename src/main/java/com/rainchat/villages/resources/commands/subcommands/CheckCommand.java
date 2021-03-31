package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.Villages;
import com.rainchat.villages.api.placeholder.replacer.VillageReplacements;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.entity.Player;

import java.util.List;

public class CheckCommand extends Command {

    private final Villages villages;
    private final VillageManager villageManager;

    public CheckCommand(Villages villages) {
        super("check", "check");
        this.villages = villages;
        this.villageManager = villages.getVillageManager();
    }

    @Override
    public boolean run(Player player, String[] args) {
        Village village = villageManager.getVillage(player.getChunk());
        if (village != null) {
            Chat.sendTranslation(player, true, Message.VILLAGE_INFO.toString(), new VillageReplacements(player));
        } else {
            Chat.sendTranslation(player, true, Message.VILLAGE_NULL_CHECK.toString(), new VillageReplacements(player));
        }
        return true;
    }

    @Override
    public List<String> tabRun(Player player, String[] args) {
        return null;
    }
}
