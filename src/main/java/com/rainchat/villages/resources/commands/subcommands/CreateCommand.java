package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.Villages;
import com.rainchat.villages.data.enums.ParticleTip;
import com.rainchat.villages.data.enums.VillageGlobalPermission;
import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageClaim;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.data.village.VillageRole;
import com.rainchat.villages.hooks.WorldGuardHook;
import com.rainchat.villages.managers.FileManager;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.*;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

public class CreateCommand extends Command {

    private final Villages villages;
    private final VillageManager villageManager;

    public CreateCommand(Villages villages) {
        super("create", "create [name]");
        this.villages = villages;
        this.villageManager = villages.getVillageManager();
    }

    @Override
    public boolean run(Player player, String[] args) {
        if (args.length == 2) {
            Village village = villageManager.getVillage(player);
            if (village == null) {

                if (args[1].length() > 32) {
                    player.sendMessage(Chat.format(Message.VILLAGE_CREATE_LIMIT.toString()));
                    return true;
                }
                village = villageManager.getVillage(args[1]);
                if (village != null) {
                    player.sendMessage(Chat.format(Message.VILLAGE_EXISTS.toString().replace("{0}", village.getName())));
                } else {
                    Chunk chunk = player.getLocation().getChunk();
                    Village tempVillage = villageManager.getVillage(chunk);
                    if (tempVillage != null) {
                        player.sendMessage(Chat.format(Message.VILLAGE_CREATE_OTHER.toString().replace("{0}", tempVillage.getName())));
                    } else {
                        if (villages.isWorldGuard()) {
                            if (new WorldGuardHook().isRegion(player)) {
                                player.sendMessage(Chat.format(Message.WORLDGUARD_CREATE.toString()));
                                return true;
                            }
                        }

                        village = new Village(args[1], "A peaceful settlement.", player.getUniqueId());

                        // villages roles add
                        FileConfiguration rolesFile = FileManager.Files.ROLES.getFile();
                        for (String path : rolesFile.getConfigurationSection("VillageRoles").getKeys(false)) {
                            VillageRole role = new VillageRole(path);
                            for (String permission : rolesFile.getStringList("VillageRoles." + path + ".permissions")) {
                                try {
                                    role.add(VillagePermission.valueOf(permission.toUpperCase()));
                                } catch (Exception ex) {
                                    ServerLog.log(Level.WARNING, "Exception Thrown " + ex);
                                }
                            }
                            village.addRole(role);
                        }
                        // villages add global flags

                            for (String permission : rolesFile.getStringList("VillageGlobalFlags")) {
                                try {
                                    village.add(VillageGlobalPermission.valueOf(permission));
                                } catch (Exception ex) {
                                    ServerLog.log(Level.WARNING, "Exception Thrown " + ex);
                                }
                            }



                        // villages clim add

                        village.add(new VillageClaim(chunk.getWorld().getName(), chunk.getX(), chunk.getZ()));
                        VillageMember villageMember = new VillageMember(player.getUniqueId());
                        villageMember.setRole(rolesFile.getString("Settings.ownerRole", "null"));
                        village.add(villageMember);

                        // set home villages
                        village.setLocation(player.getLocation());

                        villageManager.add(village);
                        player.sendMessage(Chat.format(Message.VILLAGE_CREATE.toString().replace("{0}", village.getName())));

                        ParticleSpawn.particleTusc(player, player.getChunk(), ParticleTip.CLAIM);
                    }
                }
            } else {
                player.sendMessage(Chat.format(Message.VILLAGE_NOT_NULL.toString()));
            }
        } else {
            player.sendMessage(Chat.format(Message.USAGE.toString().replace("{0}", "/village " + getUsage())));
        }
        return false;
    }

    @Override
    public List<String> tabRun(Player player, String[] args) {
        return null;
    }
}
