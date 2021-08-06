package com.rainchat.villages.resources.afcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.rainchat.villages.Villages;
import com.rainchat.villages.api.events.ClaimVillageEvent;
import com.rainchat.villages.api.events.CreateVillageEvent;
import com.rainchat.villages.api.events.UnClaimVillageEvent;
import com.rainchat.villages.api.placeholder.replacer.EconomyReplacements;
import com.rainchat.villages.data.config.ConfigRole;
import com.rainchat.villages.data.config.ConfigVillage;
import com.rainchat.villages.data.enums.ParticleTip;
import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.*;
import com.rainchat.villages.hooks.EconomyBridge;
import com.rainchat.villages.hooks.WorldGuardHook;
import com.rainchat.villages.managers.MenuManager;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Message;
import com.rainchat.villages.utilities.general.ParticleSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

@CommandAlias("%village")
public class ClaimVillageCommand extends BaseCommand {

    private final VillageManager villageManager;
    private final MenuManager menuManager;

    public ClaimVillageCommand(VillageManager villageManager, MenuManager menuManager) {
        this.villageManager = villageManager;
        this.menuManager = menuManager;
    }

    @Subcommand("unclaim")
    @CommandCompletion("@nothing")
    @CommandPermission("village.commands.unclaim")
    public void onUnClaim(Player player) {
        Village village = villageManager.getVillage(player);
        if (village != null) {
            VillageMember villageMember = village.getMember(player.getUniqueId());
            if (villageManager.hasPermission(VillagePermission.UNCLAIM_LAND, village, villageMember.getUniqueId())) {
                Village tempVillage = villageManager.getVillage(player.getLocation().getChunk());
                if (tempVillage == village) {
                    if (village.getVillageClaims().size() > 1) {

                        UnClaimVillageEvent event = new UnClaimVillageEvent(village, player.getLocation().getChunk(), player);
                        Bukkit.getServer().getPluginManager().callEvent(event);

                        if (event.isCancelled()) return;


                        VillageClaim villageClaim = villageManager.getClaim(village, player.getLocation().getChunk());
                        village.remove(villageClaim);
                        ParticleSpawn.particleTusc(player, player.getLocation().getChunk(), ParticleTip.UN_CLAIM);
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
    }

    @Subcommand("unclaim")
    @CommandCompletion("@nothing")
    @CommandPermission("village.commands.unclaim")
    public void onUnClaimArg(Player player, String world, int x, int y) {
        Village village = villageManager.getVillage(player);
        if (village == null) {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
        }
        VillageMember villageMember = village.getMember(player.getUniqueId());
        if (!villageManager.hasPermission(VillagePermission.UNCLAIM_LAND, village, villageMember.getUniqueId())) {
            return;
        }
        Village tempVillage = villageManager.getVillage(Objects.requireNonNull(Bukkit.getWorld(world)).getChunkAt(x, y));
        if (tempVillage != village) {
            player.sendMessage(Chat.format(Message.VILLAGE_UNCLAIM_OTHER.toString()));
            return;
        }
        if (village.getVillageClaims().size() < 1) {
            player.sendMessage(Chat.format(Message.VILLAGE_UNCLAIM_ONE.toString()));
        }

        UnClaimVillageEvent event = new UnClaimVillageEvent(village, new Location(Bukkit.getWorld(world),x*16,0,y*16).getChunk(), player);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        VillageClaim villageClaim = villageManager.getClaim(village, player.getLocation().getChunk());
        village.remove(villageClaim);
        ParticleSpawn.particleTusc(player, player.getLocation().getChunk(), ParticleTip.UN_CLAIM);
        player.sendMessage(Chat.format(Message.VILLAGE_UNCLAIM.toString()));

        VillagePlayer landPlayer = villageManager.getVillagePlayer(player);
        landPlayer.setCurrentLand(null);

    }

    @Subcommand("claim")
    @CommandCompletion("@nothing")
    @CommandPermission("village.commands.claim")
    public void onClaim(Player player) {
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
        Chunk chunk = player.getLocation().getChunk();
        Village tempVillage = villageManager.getVillage(chunk);
        if (tempVillage == village) {
            player.sendMessage(Chat.format(Message.VILLAGE_CLAIM_OWNED.toString()));
            return;
        }
        if (tempVillage != null) {
            player.sendMessage(Chat.format(Message.VILLAGE_CLAIM_OTHER.toString()));
            return;
        }
        if (!villageManager.canClaim(village)) {
            player.sendMessage(Chat.format(Message.VILLAGE_MAX_CLAIMS.toString().replace("{0}", String.valueOf(villageManager.checkMaxClaims(village)))));
            return;
        }

        if (Villages.WORLD_GUARD) {
            if (WorldGuardHook.isRegion(player.getLocation().getChunk())) {
                player.sendMessage(Chat.format(Message.WORLDGUARD_CREATE.toString()));
                return;
            }
        }

        if (ConfigVillage.NEARBY_CHUNKS) {
            boolean x = false;
            if (village == villageManager.getVillage(player.getLocation().add(16, 0, 0).getChunk())) x = true;
            if (village == villageManager.getVillage(player.getLocation().add(-16, 0, 0).getChunk())) x = true;
            if (village == villageManager.getVillage(player.getLocation().add(0, 0, 16).getChunk())) x = true;
            if (village == villageManager.getVillage(player.getLocation().add(0, 0, -16).getChunk())) x = true;

            if (!x) {
                player.sendMessage(Chat.format(Message.VILLAGE_NEARBY_CHUNKS.toString()));
                return;
            }
        }


        if (ConfigVillage.ECONOMY_ENABLE && EconomyBridge.hasValidEconomy()) {
            if (EconomyBridge.hasMoney(player, ConfigVillage.CLAIM_MONEY_TAKE)) {
                EconomyBridge.takeMoney(player, ConfigVillage.CLAIM_MONEY_TAKE);
            } else {
                Chat.sendTranslation(player, true, Message.ECONOMY_CLAIM_VILLAGE.toString(), new EconomyReplacements(ConfigVillage.CLAIM_MONEY_TAKE));
                return;
            }
        }


        ClaimVillageEvent chunkEvent = new ClaimVillageEvent(village, chunk, player);
        Bukkit.getServer().getPluginManager().callEvent(chunkEvent);

        if (chunkEvent.isCancelled()) return;

        VillageClaim villageClaim = new VillageClaim(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
        village.add(villageClaim);
        ParticleSpawn.particleTusc(player, player.getLocation().getChunk(), ParticleTip.CLAIM);
        player.sendMessage(Chat.format(Message.VILLAGE_CLAIM.toString().replace("{0}", villageClaim.toString())));

        VillagePlayer landPlayer = villageManager.getVillagePlayer(player);
        landPlayer.setCurrentLand(village);

    }

    @Subcommand("create")
    @CommandCompletion("@players")
    @Syntax("<village name>")
    @CommandPermission("village.commands.create")
    public void onCreate(Player player, String args) {
        Village village = villageManager.getVillage(player);

        if (village != null) {
            player.sendMessage(Chat.format(Message.VILLAGE_NOT_NULL.toString()));
            return;
        }
        if (args.length() > 32) {
            player.sendMessage(Chat.format(Message.VILLAGE_CREATE_LIMIT.toString()));
            return;
        }
        village = villageManager.getVillage(args);
        if (village != null) {
            player.sendMessage(Chat.format(Message.VILLAGE_EXISTS.toString().replace("{0}", village.getName())));
            return;
        }
        Chunk chunk = player.getLocation().getChunk();
        Village tempVillage = villageManager.getVillage(chunk);
        if (tempVillage != null) {
            player.sendMessage(Chat.format(Message.VILLAGE_CREATE_OTHER.toString().replace("{0}", tempVillage.getName())));
            return;
        }

        if (Villages.WORLD_GUARD) {
            if (WorldGuardHook.isRegion(player.getLocation().getChunk())) {
                player.sendMessage(Chat.format(Message.WORLDGUARD_CREATE.toString()));
                return;
            }
        }

        if (ConfigVillage.ECONOMY_ENABLE && EconomyBridge.hasValidEconomy()) {
            if (EconomyBridge.hasMoney(player, ConfigVillage.CREATE_MONEY_TAKE)) {
                EconomyBridge.takeMoney(player, ConfigVillage.CREATE_MONEY_TAKE);
            } else {
                Chat.sendTranslation(player, true, Message.ECONOMY_CREATE_VILLAGE.toString(), new EconomyReplacements(ConfigVillage.CREATE_MONEY_TAKE));
                return;
            }
        }


        // village create
        village = new Village(args, ConfigVillage.DEFAULT_TITLE, player.getUniqueId(), villageManager.generateUUID());
        // village roles add
        village.setRoles(ConfigRole.VILLAGES_DEFAULT_ROLES);
        // village add global flags
        village.add(ConfigRole.VILLAGES_GLOBAL_PERMISSIONS);
        // village claim add
        village.add(new VillageClaim(chunk.getWorld().getName(), chunk.getX(), chunk.getZ()));
        // village add member
        VillageMember villageMember = new VillageMember(player.getUniqueId());
        villageMember.setRole(ConfigRole.OWNER_ROLE);
        village.add(villageMember);
        // set home village
        village.setLocation(player.getLocation());
        // register village

        CreateVillageEvent chunkEvent = new CreateVillageEvent(village,chunk,player);
        Bukkit.getServer().getPluginManager().callEvent(chunkEvent);

        if(chunkEvent.isCancelled()) return;

        villageManager.add(village);
        player.sendMessage(Chat.format(Message.VILLAGE_CREATE.toString().replace("{0}", village.getName())));
        ParticleSpawn.particleTusc(player, player.getLocation().getChunk(), ParticleTip.CLAIM);

        VillagePlayer landPlayer = villageManager.getVillagePlayer(player);
        landPlayer.setCurrentLand(village);
    }

    @Subcommand("disband")
    @CommandCompletion("@nothing")
    @CommandPermission("village.commands.disband")
    public void onDisband(Player player) {
        Village village = villageManager.getVillage(player);
        if (village == null) {
            player.sendMessage(Chat.format(Message.VILLAGE_NULL.toString()));
            return;
        }
        VillageMember villageMember = village.getMember(player.getUniqueId());
        if (!villageManager.hasPermission(VillagePermission.DISBAND, village, villageMember.getUniqueId())) {
            player.sendMessage(Chat.format(Message.NO_PERMISSION.toString().replace("{0}", VillagePermission.DISBAND.name())));
            return;
        }
        VillageRequest villageRequest = villageManager.getRequest(player);
        if (villageRequest != null) {
            player.sendMessage(Chat.format(Message.REQUEST_PENDING.toString()));
            return;
        }

        villageRequest = new VillageRequest(village, player.getUniqueId(), null, VillageRequest.VillageRequestAction.DISBAND);
        villageRequest.send();
        villageManager.addPlayer(villageRequest, player);
    }

}