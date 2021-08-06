package com.rainchat.villages.data.config;

import com.rainchat.villages.Villages;
import com.rainchat.villages.data.flags.FlagItem;
import com.rainchat.villages.managers.FileManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ConfigVillage {

    public static String LANGUAGE;
    public static boolean ECONOMY_ENABLE;
    public static List<String> REGIONS_WORLD_GUARD;

    public static String DEFAULT_TITLE;
    public static long CLAIM_EXPIRATION_CLAIM_TIME;
    public static String CLAIM_GLOBAL_PROTECT_MODE;
    public static int CLAIM_DEFAULT_CLAIM_LIMIT;
    public static List<String> CLAIM_ENABLED_WORLDS_LIST;
    public static int CLAIM_ADD_PER_MEMBER;
    public static boolean NEARBY_CHUNKS;

    public static int SUB_DEFAULT_CLAIM_LIMIT;
    public static int SUB_CLAIM_ADD_PER_MEMBER;

    public static HashMap<String, String> PLACEHOLDERS;


    public static int CREATE_MONEY_TAKE;
    public static int CLAIM_MONEY_TAKE;

    public static String TAG = "tag";
    public static String TIME = "char";

    public static void setup() {
        PLACEHOLDERS = new HashMap<>();
        FileConfiguration config = FileManager.Files.CONFIG.getFile();
        LANGUAGE = config.getString("Settings-Global.default-language", "en_En");
        createLanguage();
        REGIONS_WORLD_GUARD = config.getStringList("WorldGuard.ignores-regions");

        CLAIM_ENABLED_WORLDS_LIST = config.getStringList("Settings-Claim.enabled-worlds");
        DEFAULT_TITLE = config.getString("Settings-Claim.default-title-description", "A peaceful settlement.");
        CLAIM_EXPIRATION_CLAIM_TIME = config.getLong("Settings-Claim.expiration.time", 5);
        CLAIM_GLOBAL_PROTECT_MODE = config.getString("Settings-Claim.protect-mode", "protect");
        CLAIM_DEFAULT_CLAIM_LIMIT = config.getInt("Settings-Claim.default-claim-limit", 6);
        CLAIM_ADD_PER_MEMBER = config.getInt("Settings-Claim.default-claim-per-member", 2);
        NEARBY_CHUNKS = config.getBoolean("Settings-Claim.nearby-chunks", false);

        SUB_DEFAULT_CLAIM_LIMIT = config.getInt("Settings-Sub-Claim.default-claim-limit", 3);
        SUB_CLAIM_ADD_PER_MEMBER = config.getInt("Settings-Sub-Claim.default-claim-per-member", 1);

        ECONOMY_ENABLE = config.getBoolean("Settings-Global.economy-enable", false);
        CLAIM_MONEY_TAKE = config.getInt("Economy.claim-money-take", 800);
        CREATE_MONEY_TAKE = config.getInt("Economy.create-money-take", 5000);

        ConfigurationSection section = config.getConfigurationSection("Placeholders");

        if (section == null) {
            return;
        }

        for (String path: section.getKeys(false)) {
            if (section.contains(path)) {
                PLACEHOLDERS.put(path,section.getString(path));
            }
        }
    }


    private static void createLanguage() {
        File file = new File(Villages.getInstance().getDataFolder(), "language" + File.separator + LANGUAGE + ".yml");
        try {
            if (file.createNewFile()) {
                System.out.println(LANGUAGE + ".yml" + " was successfully created!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
