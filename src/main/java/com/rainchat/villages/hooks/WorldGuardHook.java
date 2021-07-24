package com.rainchat.villages.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class WorldGuardHook {

    private static StateFlag FLAG_CHUNK_CLAIM;
    private static boolean isLoad = false;


    public static void loadWorldGuard(boolean loadPlugin) {
        isLoad = loadPlugin;
    }


    public static boolean hasValidPlugin() {
        return isLoad;
    }

    public static void registerFlags() {
        // ... do your own plugin things, etc

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StateFlag flag = new StateFlag("village-claim-flag", false);
            if (registry.get("village-claim-flag") != null) {
                FLAG_CHUNK_CLAIM = flag;
                return;
            }
            registry.register(flag);
            FLAG_CHUNK_CLAIM = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("village-claim-flag");
            if (existing instanceof StateFlag) {
                FLAG_CHUNK_CLAIM = (StateFlag) existing;
            }
        }
    }


    public static boolean isRegion(Player player) {
        Location location = BukkitAdapter.adapt(player).getLocation();
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet applicableRegionSet = query.getApplicableRegions(location);
        return !applicableRegionSet.getRegions().isEmpty();
    }


    public static boolean isRegion(Chunk chunk) {

        try {
            // Generate a region in the given chunk to get all intersecting regions
            int bx = chunk.getX() << 4;
            int bz = chunk.getZ() << 4;
            BlockVector3 pt1 = BlockVector3.at(bx, 0, bz);
            BlockVector3 pt2 = BlockVector3.at(bx + 15, 256, bz + 15);
            ProtectedCuboidRegion region = new ProtectedCuboidRegion("_", pt1, pt2);
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(chunk.getWorld()));

            // No regions in this world, claiming should be determined by the config
            if (regionManager == null) {
                return false;
            }

            // If any regions in the given chunk deny chunk claiming, false is returned
            for (ProtectedRegion regionIn : regionManager.getApplicableRegions(region)) {
                StateFlag.State flag = regionIn.getFlag(FLAG_CHUNK_CLAIM);
                if (flag == StateFlag.State.DENY || flag == null) return true;
            }
            // No objections
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // An error occurred, better to be on the safe side so false is returned
        return false;
    }
}
