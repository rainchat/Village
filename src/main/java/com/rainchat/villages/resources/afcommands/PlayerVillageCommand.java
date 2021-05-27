package com.rainchat.villages.resources.afcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.rainchat.villages.Villages;
import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.data.village.VillageRequest;
import com.rainchat.villages.managers.MenuManager;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.entity.Player;


@CommandAlias("v|village|villages")
public class PlayerVillageCommand extends BaseCommand {

    private final VillageManager villageManager;
    private final MenuManager menuManager;

    public PlayerVillageCommand(VillageManager villageManager, MenuManager menuManager) {
        this.villageManager = villageManager;
        this.menuManager = menuManager;
    }

    @Subcommand("accept")
    @CommandCompletion("@nothing")
    @CommandPermission("village.commands.accept")
    public void onAccept(Player player) {
        VillageRequest villageRequest = villageManager.getRequest(player);
        if (villageRequest == null) {
            player.sendMessage(Chat.format(Message.REQUEST_NULL.toString()));
        } else {
            villageRequest.complete(villageManager);
            villageManager.removePlayer(player);
        }
    }

    @Subcommand("deny")
    @CommandCompletion("@nothing")
    @CommandPermission("village.commands.deny")
    public void onDeny(Player player) {
        VillageRequest villageRequest = villageManager.getRequest(player);
        if (villageRequest == null) {
            player.sendMessage(Chat.format(Message.REQUEST_NULL.toString()));
            return;
        }
        player.sendMessage(Chat.format(Message.REQUEST_DENIED.toString()));
        villageManager.removePlayer(player);
    }

    @Subcommand("leave")
    @CommandCompletion("@nothing")
    @CommandPermission("village.commands.leave")
    public void onLeave(Player player) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            if (!village.getOwner().equals(player.getUniqueId())) {
                player.sendMessage(Chat.format(Message.VILLAGE_LEAVE.toString().replace("{0}", village.getName())));
                VillageMember villageMember = village.getMember(player.getUniqueId());
                village.remove(villageMember);
            } else {
                player.sendMessage(Chat.format(Message.VILLAGE_LEAVE_OWNER.toString()));
            }
        } else {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
        }
    }

    @Subcommand("home")
    @CommandCompletion("@nothing")
    @CommandPermission("village.commands.home")
    public void onHome(Player player) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageManager.hasPermission(VillagePermission.HOME, village, villageMember.getUniqueId())) {
                if (villageMember.hasCooldown()) {
                    player.sendMessage(Chat.format(Message.VILLAGE_COOLDOWN.toString().replace("{0}", String.valueOf((villageMember.getCooldown() / 1000) - (System.currentTimeMillis() / 1000)))));
                } else {
                    player.teleport(village.getVillageLocation().toLocation(Villages.getInstance()));
                    player.sendMessage(Chat.format(Message.VILLAGE_HOME.toString()));
                    villageMember.setCooldown(30);
                }
            } else {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.HOME.name())));
            }
        } else {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
        }
    }

    @Subcommand("sethome")
    @CommandCompletion("@nothing")
    @CommandPermission("village.commands.sethome")
    public void onSetHome(Player player) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageManager.hasPermission(VillagePermission.SET_HOME, village, villageMember.getUniqueId())) {
                village.setLocation(player.getLocation());
                player.sendMessage(Chat.format(Message.VILLAGE_SET_HOME.toString()));
            } else {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.SET_HOME.name())));
            }
        } else {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
        }
    }

    @Subcommand("panel")
    @CommandCompletion("@nothing")
    @CommandPermission("village.commands.panel")
    public void onPanel(Player player) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageManager.hasPermission(VillagePermission.PANEL, village, villageMember.getUniqueId())) {
                MenuManager.getInstance().openMenu(player, "main");
            } else {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.PANEL.name())));
            }
        } else {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
        }
    }

}
