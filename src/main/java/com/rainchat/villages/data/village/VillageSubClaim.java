package com.rainchat.villages.data.village;

import com.rainchat.villages.data.config.ConfigRole;
import com.rainchat.villages.data.enums.VillageGlobalPermission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VillageSubClaim {

    private final String name;
    private final String world;
    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;
    private final int minZ;
    private final int maxZ;
    private final Set<VillageGlobalPermission> villagePermissions;
    private final Set<UUID> members;
    private String role;

    public VillageSubClaim(String name, Location loc1, Location loc2) {
        this(name, loc1.getWorld(), loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
    }

    public VillageSubClaim(String name, World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.villagePermissions = new HashSet<>();
        this.members = new HashSet<>();
        this.world = world.getName();
        this.role = ConfigRole.DEFAULT_ROLE;
        this.name = name;
        minX = Math.min(x1, x2);
        minY = Math.min(y1, y2);
        minZ = Math.min(z1, z2);
        maxX = Math.max(x1, x2);
        maxY = Math.max(y1, y2);
        maxZ = Math.max(z1, z2);
    }

    public void add(VillageGlobalPermission villageGlobalPermission) {
        villagePermissions.add(villageGlobalPermission);
    }

    public void add(Set<VillageGlobalPermission> villageGlobalPermissions) {
        villagePermissions.addAll(villageGlobalPermissions);
    }

    public void add(Player player) {
        members.add(player.getUniqueId());
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public boolean containMember(Player player) {
        return members.contains(player.getUniqueId());
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean hasPermission(VillageGlobalPermission villageGlobalPermission) {
        return villagePermissions.contains(villageGlobalPermission);
    }

    public void remove(VillageGlobalPermission villageGlobalPermission) {
        villagePermissions.remove(villageGlobalPermission);
    }

    public void remove(Player player) {
        members.remove(player.getUniqueId());
    }

    public boolean contains(VillageSubClaim cuboid) {
        return cuboid.getWorld().equals(getWorld()) &&
                cuboid.getMinX() >= minX && cuboid.getMaxX() <= maxX &&
                cuboid.getMinY() >= minY && cuboid.getMaxY() <= maxY &&
                cuboid.getMinZ() >= minZ && cuboid.getMaxZ() <= maxZ;
    }

    public boolean contains(Location location) {
        return contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean contains(int x, int y, int z) {
        return x >= minX && x <= maxX &&
                y >= minY && y <= maxY &&
                z >= minZ && z <= maxZ;
    }

    public boolean overlaps(VillageSubClaim cuboid) {
        return cuboid.getWorld().equals(getWorld()) &&
                !(cuboid.getMinX() > maxX || cuboid.getMinY() > maxY || cuboid.getMinZ() > maxZ ||
                        minZ > cuboid.getMaxX() || minY > cuboid.getMaxY() || minZ > cuboid.getMaxZ());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VillageSubClaim)) {
            return false;
        }
        final VillageSubClaim other = (VillageSubClaim) obj;
        return world.equals(other.world)
                && minX == other.minX
                && minY == other.minY
                && minZ == other.minZ
                && maxX == other.maxX
                && maxY == other.maxY
                && maxZ == other.maxZ;
    }

    @Override
    public String toString() {
        return "Cuboid[world:" + world +
                ", minX:" + minX +
                ", minY:" + minY +
                ", minZ:" + minZ +
                ", maxX:" + maxX +
                ", maxY:" + maxY +
                ", maxZ:" + maxZ + "]";
    }
}
