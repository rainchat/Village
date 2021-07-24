package com.rainchat.villages;

import co.aikar.commands.CommandManager;
import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import com.google.gson.reflect.TypeToken;
import com.rainchat.villages.api.VillagesApi;
import com.rainchat.villages.data.config.ConfigFlags;
import com.rainchat.villages.data.config.ConfigRole;
import com.rainchat.villages.data.config.ConfigVillage;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.*;
import com.rainchat.villages.resources.afcommands.*;
import com.rainchat.villages.utilities.general.Message;
import com.rainchat.villages.utilities.general.ServerLog;
import org.bukkit.ChatColor;

import java.util.Set;
import java.util.logging.Level;

public final class VillageImpl implements VillagesApi {

    private final VillageManager villageManager;
    private final FileManager fileManager = FileManager.getInstance();
    private final MenuManager menuManager = MenuManager.getInstance();
    private final FlagManager flagManager;
    private final PaperCommandManager manager;

    private final Villages plugin;


    public VillageImpl(Villages plugin) {
        this.plugin = plugin;

        //==================== File manager ====================
        String languages = "/language";
        String menu = "/menus";
        fileManager.logInfo(false)
                .registerCustomFilesFolder("/date")
                .registerCustomFilesFolder("/date/players")
                .registerCustomFilesFolder("/language")
                .registerCustomFilesFolder("/menus")
                .registerCustomFilesFolder("/extension")
                .registerDefaultGenerateFiles("ClaimMenu.yml", "/menus", menu)
                .registerDefaultGenerateFiles("GlobalPermission.yml", "/menus", menu)
                .registerDefaultGenerateFiles("main.yml", "/menus", menu)
                .registerDefaultGenerateFiles("MemberRoles.yml", "/menus", menu)
                .registerDefaultGenerateFiles("MembersMenu.yml", "/menus", menu)
                .registerDefaultGenerateFiles("RolePermission.yml", "/menus", menu)
                .registerDefaultGenerateFiles("RolesMenu.yml", "/menus", menu)
                .registerDefaultGenerateFiles("SubClaimMembers.yml", "/menus", menu)
                .registerDefaultGenerateFiles("SubClaimMenuMembers.yml", "/menus", menu)
                .registerDefaultGenerateFiles("SubClaimMenuPermission.yml", "/menus", menu)
                .registerDefaultGenerateFiles("SubClaimMenuRole.yml", "/menus", menu)
                .registerDefaultGenerateFiles("SubClaimPermissions.yml", "/menus", menu)
                .registerDefaultGenerateFiles("SubClaimRole.yml", "/menus", menu)
                .registerDefaultGenerateFiles("en_EN.yml", "/language", languages)
                .registerDefaultGenerateFiles("ru_RU.yml", "/language", languages)
                .registerDefaultGenerateFiles("it_IT.yml", "/language", languages)
                .registerDefaultGenerateFiles("fr_FR.yml", "/language", languages)
                .setup(plugin);
        ConfigRole.setup();
        ConfigVillage.setup();
        ConfigFlags.setup();

        Message.setConfiguration(fileManager.getFile(ConfigVillage.LANGUAGE));
        ServerLog.log(Level.INFO, "Registered " + Message.addMissingMessages() + " message(s).");


        //==================== village manager ====================
        ServerLog.log(Level.INFO, "Loading Village Data.");
        villageManager = new VillageManager(plugin);
        villageManager.load(new TypeToken<Set<Village>>() {
        }.getType());



        //==================== flag manager ====================
        flagManager = new FlagManager();

        //==================== save manager ====================

        ServerLog.log(Level.INFO, "Loading Save manager.");
        SaveManager saveManager = new SaveManager(villageManager);
        saveManager.start();

        //==================== Command manager ====================
        manager = new PaperCommandManager(plugin);
        manager.getCommandCompletions().registerCompletion("villages", c -> {
            return villageManager.getVillages();
        });
        manager.getCommandCompletions().registerCompletion("menus", c -> {
            return menuManager.getAllMenu();
        });
        manager.getCommandReplacements().addReplacement("avillage", "va|villageadmin|villagesadmin|adminvillage");
        manager.getCommandReplacements().addReplacement("village", "v|village|villages");

        manager.setFormat(MessageType.SYNTAX, ChatColor.GRAY, ChatColor.AQUA, ChatColor.DARK_AQUA);


        manager.registerCommand(new AdminVillageCommand(villageManager, menuManager));
        manager.registerCommand(new ClaimVillageCommand(villageManager, menuManager));
        manager.registerCommand(new ListCommand(villageManager));
        manager.registerCommand(new OwnerVillageCommand(villageManager, menuManager));
        manager.registerCommand(new PlayerVillageCommand(villageManager, menuManager));
        manager.registerCommand(new SubClaimVillageCommand(villageManager, menuManager));

        //=====================================================================


        menuManager.setupMenus(villageManager);

    }


    @Override
    public VillageManager getVillageManage() {
        return villageManager;
    }

    @Override
    public FileManager getFileManager() {
        return fileManager;
    }

    @Override
    public PaperCommandManager getCommandManager() {
        return manager;
    }

    @Override
    public MenuManager getMenuManager() {
        return menuManager;
    }

    @Override
    public FlagManager getFlagManager() {
        return flagManager;
    }

    @Override
    public Villages getPlugin() {
        return plugin;
    }

    @Override
    public void onDiscard() {
    }

    @Override
    public void onSave() {
    }

    @Override
    public void onReload() {
    }
}
