package com.rainchat.villages.data.config;

import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.VillageRole;
import com.rainchat.villages.managers.FileManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Set;

public class ConfigRole {


    public static String DEFAULT_ROLE;
    public static String OWNER_ROLE;
    public static Set<VillageRole> VILLAGES_DEFAULT_ROLES;
    public static Set<String> VILLAGES_GLOBAL_PERMISSIONS;


    public static void setup() {
        VILLAGES_DEFAULT_ROLES = new HashSet<>();
        VILLAGES_GLOBAL_PERMISSIONS = new HashSet<>();
        FileConfiguration roleConfig = FileManager.Files.ROLES.getFile();
        DEFAULT_ROLE = roleConfig.getString("Settings.defaultRole", "null");
        OWNER_ROLE = roleConfig.getString("Settings.ownerRole", "null");

        //VILLAGES_DEFAULT_ROLES
        for (String path : roleConfig.getConfigurationSection("VillageRoles").getKeys(false)) {
            VillageRole role = new VillageRole(path);
            for (String permission : roleConfig.getStringList("VillageRoles." + path + ".permissions")) {
                try {
                    role.add(VillagePermission.valueOf(permission.toUpperCase()));
                } catch (Exception ignored) {
                }
            }
            VILLAGES_DEFAULT_ROLES.add(role);
        }

        //VILLAGES_GLOBAL_PERMISSIONS
        for (String permission : roleConfig.getStringList("VillageGlobalFlags")) {
            try {
                VILLAGES_GLOBAL_PERMISSIONS.add(permission);
            } catch (Exception ignored) {
            }
        }

    }

}