package com.rainchat.villages.managers;

import com.rainchat.rainlib.utils.Manager;
import com.rainchat.villages.data.config.ConfigVillage;
import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class VillageManagerOld extends Manager<VillageOld> {


    public VillageManagerOld(Plugin plugin) {
        super("villages", plugin);
    }

    public Set<VillageOld> getVillages() {
        return toSet();
    }

    public List<VillageOld> getArray() {
        return new ArrayList<>(toSet());
    }

}
