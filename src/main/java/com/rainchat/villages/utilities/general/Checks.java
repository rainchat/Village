package com.rainchat.villages.utilities.general;

import com.rainchat.villages.Villages;
import org.bukkit.World;

public class Checks {

    public static boolean checkWorld(String name) {
        for (World world : Villages.getInstance().getServer().getWorlds()) {
            if (world.getName().equals(name)) return true;
        }
        return false;
    }

}
