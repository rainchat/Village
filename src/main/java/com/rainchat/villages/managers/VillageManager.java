package com.rainchat.villages.managers;

import com.rainchat.rainlib.utils.Manager;
import com.rainchat.villages.data.config.ConfigVillage;
import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.*;
import com.rainchat.villages.utilities.general.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class VillageManager extends Manager<Village> {

    private final Plugin plugin;
    private final HashMap<UUID, VillageRequest> villageRequestHashMap = new HashMap<>();
    private final Set<UUID> adminModePlayers = new HashSet<>();
    private final Map<UUID, VillagePlayer> villagePlayers = new HashMap<>();

    public VillageManager(Plugin plugin) {
        super("date/villages", plugin);
        this.plugin = plugin;
    }


    public void addPlayer(VillageRequest villageRequest, Player player) {
        villageRequestHashMap.put(player.getUniqueId(), villageRequest);
    }

    public void addAdmin(Player player) {
        adminModePlayers.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        villageRequestHashMap.remove(player.getUniqueId());
    }

    public void removeVillagePlayer(Player player) {
       villagePlayers.remove(player.getUniqueId());
    }

    public void removeAdmin(Player player) {
        adminModePlayers.remove(player.getUniqueId());
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

    public UUID generateUUID() {
        UUID uuid = UUID.randomUUID();
        for (Village village : toSet()) {
            if (village.containsID(uuid)) {
                return generateUUID();
            }
        }
        return uuid;
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

    public VillagePlayer getVillagePlayer(Player player) {
        return villagePlayers.computeIfAbsent(player.getUniqueId(), k -> new VillagePlayer(player));
    }

    public int getMax(Player player) {
        int SellLimit = 0;
        for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
            String perm = permission.getPermission();
            if (perm.startsWith("village.claims.")) {
                perm = perm.replace("village.claims.", "");
                if (MathUtil.isInt(perm)) {
                    if (Integer.parseInt(perm) > SellLimit) {
                        SellLimit = Integer.parseInt(perm);
                    }
                }
            }
        }

        return SellLimit;
    }


    public VillageSubClaim getCuboid(Village village, Location location) {
        for (VillageSubClaim villageSubClaim : village.getVillageSubRegions()) {
            if (villageSubClaim.contains(location)) {
                return villageSubClaim;
            }
        }
        return null;
    }

    public int getMax(Village village) {
        final AtomicInteger max = new AtomicInteger();
        Player player = Bukkit.getPlayer(village.getOwner());
        if (player != null) {
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
            Bukkit.broadcastMessage(max.get() + "");
            return max.get();
        }
        return 0;
    }


    public int checkMaxClaims(Village village) {
        int claims = 0;
        int perMemberLimit = 0;


        int defaultClaimLimit = ConfigVillage.CLAIM_DEFAULT_CLAIM_LIMIT;
        if (village.getVillageMembers().size() > 1) {
            perMemberLimit = (ConfigVillage.CLAIM_ADD_PER_MEMBER) * (village.getVillageMembers().size() - 1);
        }
        claims += perMemberLimit;
        claims += defaultClaimLimit;

        for (VillageMember villageMember : village.getVillageMembers()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(villageMember.getUniqueId());
            Player player1 = player.getPlayer();
            if (player1 != null) {
                claims += getMax(player1);
            }
        }
        claims += village.getExtraClaims();
        return claims;
    }


    public boolean canClaim(Village village) {
        return checkMaxClaims(village) > village.getVillageClaims().size();
    }


    public boolean hasPermission(VillagePermission islandPermission, Village village, Player player, Location location) {
        if (village.getOwner().equals(player.getUniqueId()) || hasAdminMode(player.getUniqueId())) return false;

        if (village.containsSubCuboid(location)) {
            return !village.hasPermission(getCuboid(village, location).getRole(), islandPermission);
        } else return !village.hasPermission(islandPermission, player.getUniqueId());
    }

    public boolean hasPermission(VillagePermission islandPermission, Village village, UUID uuid) {
        if (village.getOwner().equals(uuid)) {
            return true;
        } else return village.hasPermission(islandPermission, uuid);
    }

    public boolean hasAdminMode(UUID uuid) {
        return adminModePlayers.contains(uuid);
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
        long time_expiration = ConfigVillage.CLAIM_EXPIRATION_CLAIM_TIME * 1000 * 60 * 60 * 24;
        for (Village village : toSet()) {
            if (System.currentTimeMillis() - village.getLastActive() > time_expiration) {
                i++;
                remove(village);
            }
        }

        plugin.getLogger().info("Removed " + i + " inactive villages!");
    }


    public Set<String> getVillages() {
        Set<String> strings = new HashSet<>();
        for (Village village : toSet()) {
            strings.add(village.getName());
        }
        return strings;
    }

    public List<Village> getArray() {
        return new ArrayList<>(toSet());
    }

}
