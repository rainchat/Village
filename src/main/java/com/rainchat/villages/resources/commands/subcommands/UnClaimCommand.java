package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.data.enums.ParticleTip;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageClaim;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.data.village.VillagePermission;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import com.rainchat.villages.utilities.general.ParticleSpawn;
import org.bukkit.entity.Player;

import java.util.List;

public class UnClaimCommand extends Command {

    private final VillageManager villageManager;

    public UnClaimCommand(VillageManager villageManager) {
        super("unclaim", "unclaim");
        this.villageManager = villageManager;
    }

    @Override
    public boolean run(Player player, String[] args) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageMember.hasPermission(VillagePermission.UNCLAIM_LAND) || village.getOwner().equals(player.getUniqueId()) || village.hasPermission(VillagePermission.UNCLAIM_LAND)) {
                Village tempVillage = villageManager.getVillage(player.getLocation().getChunk());
                if (tempVillage == village) {
                    if (village.getVillageClaims().size() > 1) {
                        VillageClaim villageClaim = villageManager.getClaim(village, player.getLocation().getChunk());
                        village.remove(villageClaim);
                        ParticleSpawn.particleTusc(player, player.getChunk(), ParticleTip.UN_CLAIM);
                        player.sendMessage(Chat.format(Message.VILLAGE_UNCLAIM.toString()));
                    } else {
                        player.sendMessage(Chat.format(Message.VILLAGE_UNCLAIM_ONE.toString()));
                    }
                } else {
                    player.sendMessage(Chat.format(Message.VILLAGE_UNCLAIM_OTHER.toString()));
                }
            }
        } else {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
        }
        return false;
    }

    @Override
    public List<String> tabRun(Player player, String[] args) {
        return null;
    }
}
