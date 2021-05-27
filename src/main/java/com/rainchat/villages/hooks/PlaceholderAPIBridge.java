package com.rainchat.villages.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderAPIBridge {

    private static boolean placeholderAPI = false;


    public PlaceholderAPIBridge() {

    }

    public static boolean hasValidPlugin() {
        return placeholderAPI;
    }

    public static boolean hasPlaceholders(String message) {
        if (!hasValidPlugin()) {
            throw new IllegalStateException("PlaceholderAPI plugin was not found!");
        }

        return PlaceholderAPI.containsPlaceholders(message);
    }

    public static String setPlaceholders(String message, Player executor) {
        if (!hasValidPlugin()) {
            return message;
        }

        return PlaceholderAPI.setPlaceholders(executor, message);
    }

    public boolean setupPlugin() {
        Plugin placeholderPlugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderPlugin == null) {
            placeholderAPI = false;
            return false;
        }
        placeholderAPI = true;
        return true;
    }

}
