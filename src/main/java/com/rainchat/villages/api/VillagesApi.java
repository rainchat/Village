package com.rainchat.villages.api;

import co.aikar.commands.CommandManager;
import co.aikar.commands.PaperCommandManager;
import com.rainchat.villages.api.inter.Reloadable;
import com.rainchat.villages.managers.FileManager;
import com.rainchat.villages.managers.FlagManager;
import com.rainchat.villages.managers.MenuManager;
import com.rainchat.villages.managers.VillageManager;
import org.bukkit.plugin.Plugin;


public interface VillagesApi extends Reloadable {


    VillageManager getVillageManage();

    FileManager getFileManager();

    PaperCommandManager getCommandManager();

    MenuManager getMenuManager();

    FlagManager getFlagManager();

    Plugin getPlugin();


}
