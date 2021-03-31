package com.rainchat.villages.utilities.general;


import com.rainchat.villages.Villages;

import java.util.logging.Level;

public class ServerLog {


    public static void log(Level level, String message) {
        Villages.getInstance().getServer().getLogger().log(level,
                "[" + Villages.getInstance().getDescription().getName() + "] " + message);
    }

}
