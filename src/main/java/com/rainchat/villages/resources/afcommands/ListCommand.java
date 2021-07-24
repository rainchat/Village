package com.rainchat.villages.resources.afcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Message;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;


@CommandAlias("%village")
public class ListCommand extends BaseCommand {

    public VillageManager villageManager;

    public ListCommand(VillageManager villageManager) {
        this.villageManager = villageManager;
    }

    @Subcommand("list")
    @CommandCompletion("@range:5")
    @CommandPermission("village.commands.list")
    public void runCommand(CommandSender sender, String... args) {
        List<Village> mainList = villageManager.getArray();
        if (mainList.size() == 0) {
            sender.sendMessage(Message.CLAIM_LIST_EMPTY.toString());
            return;
        }
        if (args.length == 0) {
            pageConstructor(sender, 0, 10);
        } else if (args.length == 1) {
            int page = Integer.parseInt(args[0]);
            if (getPages(mainList.size(), 10) <= (page)) {
                page = getPages(mainList.size(), 10) - 1;
            } else if (0 > page) {
                page = 0;
            }
            pageConstructor(sender, page, 10);
        }
    }

    public void pageConstructor(CommandSender sender, int page, int count) {
        List<Village> mainList = villageManager.getArray();
        sender.sendMessage(Message.CLAIM_PAGE.toString()
                .replace("{0}", String.valueOf(page + 1))
                .replace("{1}", String.valueOf(getPages(mainList.size(), count)))
        );
        int i = 0;
        for (String s : getPage(mainList, page, count)) {
            i++;
            sender.sendMessage(Chat.color((page * count + i) + ". &7" + s));
        }
        TextComponent accept = new TextComponent(Chat.color("&a<<" + " "));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Chat.color(Message.TOOLTIP.toString())).create()));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/village list " + (page - 1)));


        TextComponent deny = new TextComponent(Chat.color("&a>>"));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Chat.color(Message.TOOLTIP.toString())).create()));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/village list " + (page + 1)));

        sender.spigot().sendMessage(
                new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7==============")),
                accept,
                deny,
                new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7==============")));


    }

    public List<String> getPage(List<Village> mainList, int page, int count) {
        int size = mainList.size();

        List<String> list = new ArrayList<>();
        int start = count * page;
        int end = count * (page + 1);

        for (int i = start; i < end; i++) {
            if (i < size) {
                list.add(mainList.get(i).getName());
            } else {
                break;
            }
        }
        return list;
    }

    public int getPages(int size, int count) {
        if (size % count != 0) {
            return size / count + 1;
        }
        return size / count;
    }

}
