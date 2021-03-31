package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.Villages;
import com.rainchat.villages.data.enums.ParticleTip;
import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageClaim;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.hooks.WorldGuardHook;
import com.rainchat.villages.managers.ConfigSettings;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import com.rainchat.villages.utilities.general.ParticleSpawn;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.List;

public class ClaimCommand extends Command {

    private final Villages villages;
    private final VillageManager villageManager;

    public ClaimCommand(Villages villages) {
        super("claim", "claim");
        this.villages = villages;
        this.villageManager = villages.getVillageManager();
    }

    @Override
    public boolean run(Player player, String[] args) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageManager.checkPermission(VillagePermission.CLAIM_LAND, village, villageMember.getUniqueId())) {
                Chunk chunk = player.getLocation().getChunk();
                Village tempVillage = villageManager.getVillage(chunk);
                if (tempVillage == null) {
                    int claimLimit = villageManager.getMax(player);
                    int defaultClaimLimit = ConfigSettings.CLAIM_DEFAULT_CLAIM_LIMIT.getInt();
                    if (village.getVillageClaims().size() < claimLimit || village.getVillageClaims().size() < claimLimit + defaultClaimLimit) {
                        if (villages.isWorldGuard()) {
                            if (new WorldGuardHook().isRegion(player)) {
                                player.sendMessage(Chat.format(Message.WORLDGUARD_CLAIM.toString()));
                                return true;
                            }
                        }
                        VillageClaim villageClaim = new VillageClaim(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
                        village.add(villageClaim);
                        ParticleSpawn.particleTusc(player, player.getChunk(), ParticleTip.CLAIM);
                        player.sendMessage(Chat.format(Message.VILLAGE_CLAIM.toString().replace("{0}", villageClaim.toString())));
                    } else {
                        player.sendMessage(Chat.format(Message.VILLAGE_MAX_CLAIMS.toString().replace("{0}", String.valueOf(claimLimit + defaultClaimLimit))));
                    }
                } else if (tempVillage == village) {
                    player.sendMessage(Chat.format(Message.VILLAGE_CLAIM_OWNED.toString()));
                } else {
                    player.sendMessage(Chat.format(Message.VILLAGE_CLAIM_OTHER.toString()));
                }
            } else {
                player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.CLAIM_LAND.name())));
            }
        } else {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
        }
        return true;
    }

    @Override
    public List<String> tabRun(Player player, String[] args) {
        return null;
    }


}
