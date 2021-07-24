package com.rainchat.villages.data.config;

import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.flags.FlagItem;
import com.rainchat.villages.data.village.VillageRole;
import com.rainchat.villages.managers.FileManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConfigFlags {


    public static Set<String> DISABLED_FLAGS;
    public static HashMap<String,FlagItem> FLAG_ITEM;


    public static void setup() {
        DISABLED_FLAGS = new HashSet<>();
        FLAG_ITEM = new HashMap<>();

        FileConfiguration roleConfig = FileManager.Files.FLAGS.getFile();


        //VILLAGES_DEFAULT_ROLES
        DISABLED_FLAGS.addAll(roleConfig.getStringList("disable-flags"));


        ConfigurationSection section = roleConfig.getConfigurationSection("flag-items");

        if (section == null) {
            return;
        }

        for (String path: section.getKeys(false)) {
            FlagItem flagItem = new FlagItem();
            if (section.contains(path + ".name")) {
                flagItem.setName(section.getString(path + ".name"));
            } else {
                continue;
            }
            if (section.contains(path + ".material")) {
                Material material = Material.PAPER;
                try {
                    material = Material.valueOf(section.getString(path + ".material").toUpperCase());
                } catch (IllegalArgumentException ignore) {

                }

                flagItem.setMaterial(material);
            }
            FLAG_ITEM.put(path,flagItem);
        }

    }

}
