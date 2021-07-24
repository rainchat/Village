package com.rainchat.villages.resources.afcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.gson.reflect.TypeToken;
import com.rainchat.rainlib.utils.Manager;
import com.rainchat.villages.Villages;
import com.rainchat.villages.api.events.DisbandVillageEvent;
import com.rainchat.villages.api.placeholder.replacer.ArgsReplacements;
import com.rainchat.villages.api.placeholder.replacer.PlayerReplacements;
import com.rainchat.villages.data.VillageExtension;
import com.rainchat.villages.data.config.ConfigRole;
import com.rainchat.villages.data.config.ConfigVillage;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageOld;
import com.rainchat.villages.managers.FileManager;
import com.rainchat.villages.managers.MenuManager;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.managers.VillageManagerOld;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CommandAlias("%avillage")
public class AdminVillageCommand extends BaseCommand {

    private final VillageManager villageManager;
    private final MenuManager menuManager;

    public AdminVillageCommand(VillageManager villageManager, MenuManager menuManager) {
        this.villageManager = villageManager;
        this.menuManager = menuManager;
    }

    @Subcommand("addclaims")
    @Syntax("<village_name> <claims>")
    @CommandCompletion("@villages")
    @CommandPermission("village.commands.admin.addclaims")
    public void onAddClaims(Player player, String village, int claim) {
        if (villageManager.getVillage(village) != null) {
            Chat.sendTranslation(player, true, Message.VILLAGE_ADMIN_ADD_CLAIMS.toString(), new PlayerReplacements(player));
            villageManager.getVillage(village).addClaims(claim);
        }
    }


    @Subcommand("mode")
    @CommandCompletion("@nothing")
    @CommandAlias("amode|adminmode")
    @CommandPermission("village.commands.admin.mode")
    public void onMode(Player player) {
        if (villageManager.hasAdminMode(player.getUniqueId())) {
            villageManager.removeAdmin(player);
            Chat.sendTranslation(player, true, Message.VILLAGE_ADMIN_DISABLED.toString(), new PlayerReplacements(player));
        } else {
            villageManager.addAdmin(player);
            Chat.sendTranslation(player, true, Message.VILLAGE_ADMIN_ENABLED.toString(), new PlayerReplacements(player));
        }
    }

    @Subcommand("disband")
    @Syntax("<village_name>")
    @CommandCompletion("@villages")
    @CommandPermission("village.commands.admin.disband")
    public void onDisband(CommandSender sender, String args) {
        Village village = villageManager.getVillage(args);
        if (village != null) {
            Bukkit.broadcastMessage(Chat.format(Message.DISBAND.toString().replace("{0}", village.getName())));

            DisbandVillageEvent chunkEvent = new DisbandVillageEvent(village, null);
            Bukkit.getServer().getPluginManager().callEvent(chunkEvent);

            if (chunkEvent.isCancelled()) return;

            villageManager.remove(village);
        } else {
            Chat.sendTranslation(sender, true, Message.VILLAGE_NO_EXISTS.toString(), new ArgsReplacements(args));
        }
    }

    @Subcommand("convert")
    @Syntax("<village_name>")
    @CommandCompletion("@nothing")
    @CommandPermission("village.commands.admin.convert")
    public void onConvert(CommandSender sender) {
        VillageManagerOld villageOldManager = new VillageManagerOld(Villages.getInstance());
        villageOldManager.load(new TypeToken<Set<VillageOld>>() {
        }.getType());

        for (VillageOld villageOld: villageOldManager.getVillages()) {
            Village village = new Village(villageOld,villageManager.generateUUID());
            villageManager.add(village);
        }
        sender.sendMessage(ChatColor.GREEN + "data converted successfully ");
    }

    @Subcommand("villages")
    @CommandCompletion("@nothing")
    @CommandPermission("village.commands.admin.villages")
    public void onVillages(CommandSender sender) {

    }

    @Subcommand("open")
    @Syntax("[menu_name]")
    @CommandPermission("villages.commands.admin.open")
    @CommandCompletion("@menus")
    public void onList(Player player, String args) {
        menuManager.openMenu(player, args);
    }


    @Subcommand("version")
    @CommandPermission("villages.commands.admin.version")
    @CommandCompletion("@nothing")
    public void onVersion(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + Villages.getInstance().getDescription().getVersion());
    }


    @Subcommand("addons")
    @CommandPermission("villages.commands.admin.addon")
    @CommandCompletion("@nothing")
    public void onExpansion(CommandSender sender) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<ClassLoader, List<VillageExtension>> map: Villages.getExpansions().getClassLoaders().entrySet()) {
            for (VillageExtension villageExtension: map.getValue()){
                list.add(villageExtension.getName());
            }
        }
        sender.sendMessage(ChatColor.GRAY + "addons: " + ChatColor.GREEN + list);
    }


    @Subcommand("reload")
    @CommandCompletion("@nothing")
    @CommandPermission("villages.commands.admin.reload")
    public void onReload(CommandSender sender) {
        FileManager.getInstance().reloadAllFiles();
        ConfigVillage.setup();
        ConfigRole.setup();
        FileManager.getInstance().setup(Villages.getInstance());
        menuManager.setupMenus();
        Chat.sendTranslation(sender, true, Message.RELOAD.toString());

    }


}

