package com.rainchat.villages.utilities.general;

import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class MathUtil {

    public static Set<Chunk> getChunksBetween(Location location1, Location location2) {
        Set<Chunk> chunks = new HashSet<>();
        int minX = Math.min(location1.getBlockX(), location2.getBlockX());
        int maxX = Math.max(location1.getBlockX(), location2.getBlockX());
        int minZ = Math.min(location1.getBlockZ(), location2.getBlockZ());
        int maxZ = Math.max(location1.getBlockZ(), location2.getBlockZ());
        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                Chunk chunk = new Location(location1.getWorld(), x, 0, z).getChunk();
                chunks.add(chunk);
            }
        }
        return chunks;
    }

    //Checks if a string is a int
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    //Checks if a stirng is a double
    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    //Just checks if a string is a number (int or a double)
    public static boolean isNumeric(String s) {
        return isInt(s) || isDouble(s);
    }

    /*This is pretty useful for me when ever I need
    //to store a location in a config file instead of
    //having it all on multiple lines this makes it nice
    //and neat all on one line
    //		   x  y  z  world-name
    //Format - 11:64:320:world   */
    public static String locToString(Location loc) {
        return loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ() + ":" + loc.getWorld().getName();
    }
}
