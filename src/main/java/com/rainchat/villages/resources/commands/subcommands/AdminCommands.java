package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.api.placeholder.replacer.ArgsReplacements;
import com.rainchat.villages.api.placeholder.replacer.PlayerReplacements;
import com.rainchat.villages.api.placeholder.replacer.VillageReplacements;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminCommands extends Command {

    public VillageManager villageManager;

    public AdminCommands(VillageManager villageManager) {
        super("admin", "admin [command]");
        this.villageManager = villageManager;
    }

    @Override
    public boolean run(Player player, String[] args) {
        if (args.length == 2){
            if (args[1].equals("mode")) {
                if (villageManager.hasAdminMode(player.getUniqueId())){
                    villageManager.removeAdmin(player);
                    Chat.sendTranslation(player, true, Message.VILLAGE_ADMIN_DISABLED.toString(), new PlayerReplacements(player));
                } else {
                    villageManager.addAdmin(player);
                    Chat.sendTranslation(player, true, Message.VILLAGE_ADMIN_ENABLED.toString(), new PlayerReplacements(player));
                }
            }
        } else if (args.length == 3){
            if (args[1].equals("disband")){
                Village village = villageManager.getVillage(args[2]);
                if (village != null){
                    Bukkit.broadcastMessage(Chat.format(Message.DISBAND.toString().replace("{0}", village.getName())));
                    villageManager.remove(village);
                } else {
                    Chat.sendTranslation(player, true, Message.VILLAGE_NO_EXISTS.toString(), new ArgsReplacements(args));
                }
            }
        }
        return true;
    }

    @Override
    public List<String> tabRun(Player player, String[] args) {
        List<String> stringList = new ArrayList<>();
        if (args.length == 2) {
            stringList.add("mode");
            stringList.add("disband");
            return stringList;
        }
        return null;
    }
}
