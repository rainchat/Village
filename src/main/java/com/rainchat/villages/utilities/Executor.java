package com.rainchat.villages.utilities;


import com.rainchat.inventoryapi.InventoryAPI;
import com.rainchat.rainlib.utils.Color;
import com.rainchat.villages.Villages;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.hooks.PlaceholderAPIBridge;
import com.rainchat.villages.managers.MenuManager;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Message;
import com.rainchat.villages.utilities.menus.MenuConstructor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Executor {
    private static final Pattern ACTION_PATTERN = Pattern.compile("(?iu)\\[([\\w\\._-]+)\\](.*)");
    private final List<String> cmd;
    private final Player player;
    private final Village village;
    private final VillageManager villageManager = Villages.getInstance().getVillageManager();
    private final MenuManager menuManager = MenuManager.getInstance();
    private final MenuConstructor menuConstructor;

    public Executor(List<String> command, Player player, Village village, MenuConstructor menuConstructor) {
        this.cmd = command;
        this.player = player;
        this.village = village;
        this.menuConstructor = menuConstructor;
    }

    public void start() {


        for (String s : cmd) {
            s = Color.parseHexString(s);
            s = PlaceholderAPIBridge.setPlaceholders(s, player);
            Matcher result = ACTION_PATTERN.matcher(s);
            if (result.find()) {
                action(result.group(1), result.group(2));
            }
        }
    }

    public void action(String s, String action) {
        switch (s.toLowerCase()) {
            case "tell":
                player.sendMessage(action);
                break;
            case "title":
                player.sendTitle(action, action, 10, 70, 20);
                break;
            case "console":
                Villages.getInstance().getServer().dispatchCommand(Villages.getInstance().getServer().getConsoleSender(), action);
                break;
            case "op":
                if (player.isOp())
                    player.performCommand(action);
                else {
                    player.setOp(true);
                    player.performCommand(action);
                    player.setOp(false);
                }
                break;
            case "player":
                player.performCommand(action);
                break;
            case "close":
                InventoryAPI.getInventory(player).close(player);
                break;
            case "next-page":
                menuConstructor.pagination.nextPage();
                break;
            case "previous-page":
                menuConstructor.pagination.previousPage();
                break;
            case "remove-claim":
                if (village.getVillageClaims().size() > 1) {
                    village.removeClaim(action);
                    player.sendMessage(Chat.format(Message.VILLAGE_UNCLAIM.toString()));
                } else {
                    player.sendMessage(Chat.format(Message.VILLAGE_UNCLAIM_ONE.toString()));
                }
                break;
            case "menu-open":
                String[] words = action.split(",");
                String menu = words[0];
                if (words.length > 1) {
                    String param = words[1];
                    menuManager.openMenu(player, menu, param);
                    break;
                }
                menuManager.openMenu(player, menu);
                break;
            default:
                System.out.println("such ACTION does not exist (" + s + ")");

        }
    }
}
