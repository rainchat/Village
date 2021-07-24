package com.rainchat.villages.resources.afcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.rainchat.villages.api.placeholder.replacer.PlayerReplacements;
import com.rainchat.villages.api.placeholder.replacer.VillageReplacements;
import com.rainchat.villages.api.placeholder.replacer.VillageSubClaimReplacements;
import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.data.village.VillagePlayer;
import com.rainchat.villages.data.village.VillageSubClaim;
import com.rainchat.villages.managers.MenuManager;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.MathUtil;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Set;

@CommandAlias("%village")
public class SubClaimVillageCommand extends BaseCommand {

    private final VillageManager villageManager;
    private final MenuManager menuManager;

    public SubClaimVillageCommand(VillageManager villageManager, MenuManager menuManager) {
        this.villageManager = villageManager;
        this.menuManager = menuManager;
    }

    @Subcommand("subclaim")
    public class SubClaim extends BaseCommand {

        @Subcommand("create")
        @CommandCompletion("@players")
        @CommandPermission("village.commands.subclaim.creaate")
        public void onCreate(Player player, String args) {
            Village village = villageManager.getVillage(player);
            if (village == null) {
                player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
                return;
            }
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (!villageManager.hasPermission(VillagePermission.CLAIM_LAND, village, villageMember.getUniqueId())) {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.CLAIM_LAND.name())));
                return;
            }

            VillagePlayer villagePlayer = villageManager.getVillagePlayer(player);
            if (!villagePlayer.isCuboid()) {
                Chat.sendTranslation(player, true, Message.VILLAGE_SUB_CLAIM_UNSELECTED.toString());
                return;
            }
            Set<Chunk> chunks = MathUtil.getChunksBetween(villagePlayer.getPos1(), villagePlayer.getPos2());

            for (Chunk chunk : chunks) {
                if (!village.containsChunk(chunk)) {
                    Chat.sendTranslation(player, true, Message.VILLAGE_SUB_CLAIM_NULL.toString());
                    return;
                }
            }

            VillageSubClaim cuboid = new VillageSubClaim(args, villagePlayer.getPos1(), villagePlayer.getPos2());
            if (village.containsSubCuboid((cuboid.getName()))) {
                Chat.sendTranslation(player, true, Message.VILLAGE_SUB_CLAIM_NAME.toString());
                return;
            }

            if (village.containsSubCuboid(cuboid)) {
                Chat.sendTranslation(player, true, Message.VILLAGE_SUB_CLAIM_CUT.toString());
                return;
            }

            village.add(cuboid);
            Chat.sendTranslation(player, true, Message.VILLAGE_SUB_CLAIM.toString(), new VillageReplacements(village), new VillageSubClaimReplacements(cuboid));
        }

        @Subcommand("remove")
        @CommandCompletion("@nothing")
        @CommandPermission("village.commands.subclaim.remove")
        public void onRemove(Player player, String args) {
            Village village = villageManager.getVillage(player);
            if (village == null) {
                player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
                return;
            }
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (!villageManager.hasPermission(VillagePermission.CLAIM_LAND, village, villageMember.getUniqueId())) {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.UNCLAIM_LAND.name())));
                return;
            }

            if (!village.containsSubCuboid(args)) {
                Chat.sendTranslation(player, true, Message.VILLAGE_SUB_CLAIM_NAME_NULL.toString(), new VillageReplacements(village));
                return;
            }

            village.removeClaim(args);
            Chat.sendTranslation(player, true, Message.VILLAGE_SUB_CLAIM_REMOVE.toString(), new VillageSubClaimReplacements(village.getSubClaim(args)));
        }

        @Subcommand("addmember")
        @CommandCompletion("@players @player")
        @Syntax("<subclaim> <player>")
        @CommandPermission("village.commands.subclaim.addmember")
        public void onAddMember(Player player, String[] args) {
            if (args.length != 2) {
                return;
            }

            Village village = villageManager.getVillage(player);
            if (village == null) {
                player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
                return;
            }
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (!villageManager.hasPermission(VillagePermission.INVITE_MEMBER, village, villageMember.getUniqueId())) {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.UNCLAIM_LAND.name())));
                return;
            }
            if (!village.containsSubCuboid(args[0])) {
                Chat.sendTranslation(player, true, Message.VILLAGE_SUB_CLAIM_NAME_NULL.toString());
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                Chat.sendTranslation(player, true, Message.PLAYER_OFFLINE.toString());
                return;
            }
            if (village.getMember(player.getUniqueId()) == null) {
                player.sendMessage(Chat.format(Message.VILLAGE_MEMBER_NULL.toString().replace("{0}", args[1])));
                return;
            }

            village.getSubClaim(args[0]).add(player);
            Chat.sendTranslation(player, true, Message.VILLAGE_SUB_CLAIM_ADD_MEMBER.toString(), new PlayerReplacements(target));

        }
    }
}