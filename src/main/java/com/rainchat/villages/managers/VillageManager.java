package com.rainchat.villages.managers;

import com.rainchat.villages.data.village.*;
import com.rainchat.villages.utilities.general.Manager;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class VillageManager extends Manager<Village> {

    private final Plugin plugin;
    private final HashMap<UUID, VillageRequest> villageRequestHashMap = new HashMap<>();

    public VillageManager(Plugin plugin) {
        super("villages", plugin);
        this.plugin = plugin;
    }


    public void addP(VillageRequest villageRequest, Player player) {
        villageRequestHashMap.put(player.getUniqueId(), villageRequest);
    }

    public void removeP(Player player) {
        villageRequestHashMap.remove(player.getUniqueId());
    }

    public Village getVillage(String string) {
        for (Village village : toSet()) {
            if (village.getName().equalsIgnoreCase(string)) {
                return village;
            }
        }
        return null;
    }

    public Village getVillage(Chunk chunk) {
        for (Village village : toSet()) {
            for (VillageClaim villageClaim : village.getVillageClaims()) {
                if (chunkMatchesClaim(chunk, villageClaim)) return village;
            }
        }
        return null;
    }

    public Village getVillage(Player player) {
        for (Village village : toSet()) {
            for (VillageMember villageMember : village.getVillageMembers()) {
                if (villageMember.getUniqueId().equals(player.getUniqueId())) return village;
            }
        }
        return null;
    }

    public VillageRequest getRequest(Player player) {
        if (villageRequestHashMap.containsKey(player.getUniqueId())) {
            return villageRequestHashMap.get(player.getUniqueId());
        }
        /*for (VillageRequest villageRequest : villageRequests) {
            if (villageRequest.getTarget() != null && villageRequest.getUniqueId() != null) {
                if (villageRequest.getTarget().equals(player.getUniqueId())) {
                    return villageRequest;
                }
                if (villageRequest.getUniqueId().equals(player.getUniqueId())) {
                    return villageRequest;
                }
            }
        }*/
        return null;
    }

    public VillageClaim getClaim(Village village, Chunk chunk) {
        for (VillageClaim villageClaim : village.getVillageClaims()) {
            if (chunkMatchesClaim(chunk, villageClaim)) {
                return villageClaim;
            }
        }
        return null;
    }

    public OfflinePlayer offlinePlayer(Village village, String name) {
        for (VillageMember villageMember : village.getVillageMembers()) {
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(villageMember.getUniqueId());
            if (Objects.requireNonNull(offlinePlayer.getName()).equalsIgnoreCase(name)) {
                return offlinePlayer;
            }
        }
        return null;
    }

    public int getMax(Player player) {
        final AtomicInteger max = new AtomicInteger();

        player.getEffectivePermissions().stream().map(PermissionAttachmentInfo::getPermission).map(String::toLowerCase).filter(value ->
                value.startsWith("village.claims.")).map(value ->
                value.replace("village.claims.", "")).forEach(value -> {
            max.set(-1);
            try {
                if (Integer.parseInt(value) > max.get()) max.set(Integer.parseInt(value));
            } catch (NumberFormatException ignored) {
                max.set(0);
            }
        });

        return max.get();
    }

    public boolean checkPermission(VillagePermission villagePermission, Village village, Player player) {
        if (village.getOwner().equals(player.getUniqueId())) {
            return false;
        } else if (village.getMember(player.getUniqueId()).hasPermission(villagePermission)) {
            return false;
        }
        return (!village.hasPermission(villagePermission));
    }

    private boolean chunkMatchesClaim(Chunk chunk, VillageClaim villageClaim) {
        return villageClaim.getX() == chunk.getX() && villageClaim.getZ() == chunk.getZ() && villageClaim.getWorld().equals(chunk.getWorld().getName());
    }

    public void updateLastActive(Village village, Long time) {
        village.add(time);
    }

    public Long getActive(Village village) {
        return village.getLastActive();
    }


    public void deleteNonActive() {
        int i = 0;
        long time_expiration = ConfigSettings.CLAIM_EXPIRATION_CLAIM_TIME.getLong() * 1000 * 60 * 60 * 24;
        for (Village village : toSet()) {
            if (System.currentTimeMillis() - village.getLastActive() > time_expiration) {
                i++;
                remove(village);
            }
        }
        plugin.getLogger().info("Removed " + i + " inactive villages!");
    }

    /*public Set<VillageRequest> getRequests() {
        return Collections.unmodifiableSet(villageRequests);
    }*/

}
