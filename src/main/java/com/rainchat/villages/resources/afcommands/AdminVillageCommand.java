package com.rainchat.villages.resources.afcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.rainchat.villages.Villages;
import com.rainchat.villages.api.placeholder.replacer.ArgsReplacements;
import com.rainchat.villages.api.placeholder.replacer.EconomyReplacements;
import com.rainchat.villages.api.placeholder.replacer.PlayerReplacements;
import com.rainchat.villages.data.config.ConfigRole;
import com.rainchat.villages.data.config.ConfigVillage;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.FileManager;
import com.rainchat.villages.managers.MenuManager;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

@CommandAlias("va|villageadmin|villagesadmin")
public class AdminVillageCommand extends BaseCommand {

    private final VillageManager villageManager;
    private final MenuManager menuManager;

    public AdminVillageCommand(VillageManager villageManager, MenuManager menuManager) {
        this.villageManager = villageManager;
        this.menuManager = menuManager;
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
            villageManager.remove(village);
        } else {
            Chat.sendTranslation(sender, true, Message.VILLAGE_NO_EXISTS.toString(), new ArgsReplacements(args));
        }
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


    @Subcommand("reload")
    @CommandCompletion("@nothing")
    @CommandPermission("villages.commands.admin.reload")
    public void onReload(CommandSender sender) {
        FileManager.getInstance().reloadAllFiles();
        ConfigVillage.setup();
        ConfigRole.setup();
        FileManager.getInstance().setup(Villages.getInstance());
        menuManager.setupMenus();
        Villages.getInstance().registerMessages();
        Chat.sendTranslation(sender, true, Message.RELOAD.toString());

    }


}

