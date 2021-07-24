package com.rainchat.villages.resources.afcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.data.village.VillageRequest;
import com.rainchat.villages.managers.MenuManager;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;


@CommandAlias("%village")
public class OwnerVillageCommand extends BaseCommand {

    private final VillageManager villageManager;
    private final MenuManager menuManager;

    public OwnerVillageCommand(VillageManager villageManager, MenuManager menuManager) {
        this.villageManager = villageManager;
        this.menuManager = menuManager;
    }

    @Subcommand("invite")
    @CommandCompletion("@players")
    @CommandPermission("village.commands.invite")
    public void onInvite(Player player, String args) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageManager.hasPermission(VillagePermission.INVITE_MEMBER, village, villageMember.getUniqueId())) {
                VillageRequest villageRequest = villageManager.getRequest(player);
                if (villageRequest == null) {
                    Player target = Bukkit.getPlayer(args);
                    if (target != player) {
                        if (target != null) {
                            Village targetVillage = villageManager.getVillage(target);
                            if (targetVillage == null) {
                                villageRequest = new VillageRequest(village, player.getUniqueId(), target.getUniqueId(), VillageRequest.VillageRequestAction.INVITE);
                                villageRequest.send();
                                villageManager.addPlayer(villageRequest, target);
                            } else {
                                player.sendMessage(Chat.format(Message.REQUEST_INVITE_TARGET_NOT_NULL.toString().replace("{0}", target.getDisplayName())));
                            }
                        } else {
                            player.sendMessage(Chat.format(Message.PLAYER_OFFLINE.toString().replace("{0}", args)));
                        }
                    } else {
                        player.sendMessage(Chat.format(Message.REQUEST_INVITE_SELF.toString()));
                    }
                } else {
                    player.sendMessage(Chat.format(Message.REQUEST_PENDING.toString()));
                }
            } else {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.INVITE_MEMBER.name())));
            }
        } else {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
        }
    }

    @Subcommand("kick")
    @CommandCompletion("@players")
    @CommandPermission("village.commands.kick")
    public void onKick(Player player, String args) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageManager.hasPermission(VillagePermission.KICK_MEMBER, village, villageMember.getUniqueId())) {
                OfflinePlayer offlinePlayer = villageManager.offlinePlayer(village, args);
                if (offlinePlayer != null) {
                    if (offlinePlayer.getUniqueId() != player.getUniqueId()) {
                        VillageRequest villageRequest = villageManager.getRequest(player);
                        if (villageRequest == null) {
                            villageRequest = new VillageRequest(village, player.getUniqueId(), offlinePlayer.getUniqueId(), VillageRequest.VillageRequestAction.KICK);
                            villageRequest.send();
                            villageManager.addPlayer(villageRequest, player);
                        } else {
                            player.sendMessage(Chat.format(Message.REQUEST_PENDING.toString()));
                        }
                    } else {
                        player.sendMessage(Chat.format(Message.REQUEST_KICK_SELF.toString()));
                    }
                } else {
                    player.sendMessage(Chat.format(Message.VILLAGE_MEMBER_NULL.toString().replace("{0}", args)));
                }
            } else {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.KICK_MEMBER.name())));
            }
        } else {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
        }
    }

    @Subcommand("rename")
    @CommandCompletion("@players")
    @CommandPermission("village.commands.rename")
    public void onRename(Player player, String args) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageManager.hasPermission(VillagePermission.RENAME_VILLAGE, village, villageMember.getUniqueId())) {
                if (args.length() > 32) {
                    player.sendMessage(Chat.format(Message.VILLAGE_CREATE_LIMIT.toString()));
                    return;
                }
                Village other = villageManager.getVillage(args);
                if (other == null) {
                    player.sendMessage(Chat.format(Message.VILLAGE_RENAME.toString().replace("{0}", args)));
                    village.setName(args);
                } else {
                    player.sendMessage(Chat.format(Message.VILLAGE_EXISTS.toString().replace("{0}", village.getName())));
                }
            } else {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.RENAME_VILLAGE.name())));
            }
        } else {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
        }
    }

    @Subcommand("setowner")
    @CommandCompletion("@players")
    @CommandPermission("village.commands.setowner")
    public void onSetOwner(Player player, String args) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (village.getOwner().equals(villageMember.getUniqueId())) {
                OfflinePlayer offlinePlayer = villageManager.offlinePlayer(village, args);
                if (offlinePlayer != null) {
                    if (!village.getOwner().equals(offlinePlayer.getUniqueId())) {
                        village.setOwner(offlinePlayer.getUniqueId());
                        player.sendMessage(Chat.format(Message.VILLAGE_SET_OWNER.toString().replace("{0}", Objects.requireNonNull(offlinePlayer.getName()))));
                    } else {
                        player.sendMessage(Chat.format(Message.VILLAGE_ALREADY_OWNER.toString()));
                    }
                } else {
                    player.sendMessage(Chat.format(Message.VILLAGE_MEMBER_NULL.toString().replace("{0}", args)));
                }
            } else {
                player.sendMessage(Chat.format(Message.VILLAGE_OWNER.toString()));
            }
        } else {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
        }
    }

    @Subcommand("setdescription")
    @CommandCompletion("description")
    @CommandPermission("village.commands.setdescription")
    public void onSetDescription(Player player, String... args) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageManager.hasPermission(VillagePermission.SET_DESCRIPTION, village, villageMember.getUniqueId())) {
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
    }

}