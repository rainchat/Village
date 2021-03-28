package com.rainchat.villages.hooks;

import com.rainchat.villages.Villages;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.ConfigSettings;
import com.rainchat.villages.managers.VillageManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final Villages villages;
    private final VillageManager villageManager;

    public PlaceholderAPIHook(Villages villages) {
        this.villages = villages;
        this.villageManager = villages.getVillageManager();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "villages";
    }

    @Override
    public String getAuthor() {
        return villages.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return villages.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        Village village = villageManager.getVillage(player);


        int defaultClaimLimit = ConfigSettings.CLAIM_DEFAULT_CLAIM_LIMIT.getInt();
        if (identifier.equalsIgnoreCase("max_claims")) {

            return String.valueOf(villageManager.getMax(player) + defaultClaimLimit);
        }

        if (village != null) {
            if (identifier.equalsIgnoreCase("name")) {
                return village.getName();
            }

            if (identifier.equalsIgnoreCase("owner")) {
                return villages.getServer().getOfflinePlayer(village.getOwner()).getName();
            }
            if (identifier.equalsIgnoreCase("members")) {
                return String.valueOf(village.getVillageMembers().size());
            }

            if (identifier.equalsIgnoreCase("claims")) {
                return String.valueOf(village.getVillageClaims().size());
            }
        }

        village = villageManager.getVillage(player.getLocation().getChunk());

        if (village != null) {
            if (identifier.equalsIgnoreCase("target_name")) {
                return village.getName();
            }
            if (identifier.equalsIgnoreCase("target_owner")) {
                return villages.getServer().getOfflinePlayer(village.getOwner()).getName();
            }

            if (identifier.equalsIgnoreCase("target_members")) {
                return String.valueOf(village.getVillageMembers().size());
            }

            if (identifier.equalsIgnoreCase("target_claims")) {
                return String.valueOf(village.getVillageClaims().size());
            }

        }

        if (village != null && identifier.equalsIgnoreCase("can_claim")) {
            return String.valueOf(true);
        }

        if (village == null && identifier.equalsIgnoreCase("can_claim")) {
            return String.valueOf(false);
        }

        return "";
    }
}
