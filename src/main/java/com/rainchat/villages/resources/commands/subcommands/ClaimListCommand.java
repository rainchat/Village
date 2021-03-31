package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClaimListCommand extends Command {

    private final VillageManager villageManager;

    public ClaimListCommand(VillageManager villageManager) {
        super("claimlist", "claimlist [page]");
        this.villageManager = villageManager;
    }

    @Override
    public boolean run(Player player, String[] args) {
        if (args.length == 1) {
            pageConstructor(player, 0, 5);
        } else if (args.length == 2) {
            List<Village> mainList = villageManager.getArray();
            int page = Integer.parseInt(args[1]);
            if (getPages(mainList.size(), 5) <= (page)) {
                page = getPages(mainList.size(), 5) - 1;
            } else if (0 > page) {
                page = 0;
            }
            pageConstructor(player, page, 5);
        }
        return false;
    }

    public void pageConstructor(Player player, int page, int count) {
        List<Village> mainList = villageManager.getArray();
        player.sendMessage(Message.CLAIM_PAGE.toString()
                .replace("{0}", String.valueOf(page + 1))
                .replace("{1}", String.valueOf(getPages(mainList.size(), count)))
        );
        int i = 0;
        for (String s : getPage(mainList, page, count)) {
            i++;
            player.sendMessage(Chat.color((page * count + i) + ". &7" + s));
        }
        TextComponent accept = new TextComponent(Chat.color("&a<<" + " "));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Chat.color(Message.TOOLTIP.toString())).create()));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/village claimlist " + (page - 1)));


        TextComponent deny = new TextComponent(Chat.color("&a>>"));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Chat.color(Message.TOOLTIP.toString())).create()));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/village claimlist " + (page + 1)));

        player.spigot().sendMessage(
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


    @Override
    public List<String> tabRun(Player player, String[] args) {
        return null;
    }
}
