package com.rainchat.villages;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import com.google.gson.reflect.TypeToken;
import com.rainchat.inventoryapi.InventoryAPI;
import com.rainchat.villages.data.config.ConfigRole;
import com.rainchat.villages.data.config.ConfigVillage;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.hooks.*;
import com.rainchat.villages.managers.FileManager;
import com.rainchat.villages.managers.MenuManager;
import com.rainchat.villages.managers.SaveManager;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.resources.afcommands.*;
import com.rainchat.villages.resources.listeners.*;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Villages extends JavaPlugin {

    private static Villages instance;

    private final FileManager fileManager = FileManager.getInstance();
    private final MenuManager menuManager = MenuManager.getInstance();
    private VillageManager villageManager;
    public static boolean WORLD_GUARD = false;


    public static Villages getInstance() {
        return instance;
    }

    public static void setInstance(Villages instance) {
        Villages.instance = instance;
    }

    public void onEnable() {
        instance = this;

        getLogger().info("Loading Village Data.");
        villageManager = new VillageManager(this);
        villageManager.load(new TypeToken<Set<Village>>() {
        }.getType());

        getLogger().info("Loading Save manager.");
        SaveManager saveManager = new SaveManager(this);
        saveManager.start();


        // File manager
        String languages = "/language";
        String menu = "/menus";
        fileManager.logInfo(false)
                .registerCustomFilesFolder("/language")
                .registerCustomFilesFolder("/menus")
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
                .setup(this);
        ConfigRole.setup();
        ConfigVillage.setup();
        registerMessages();

        menuManager.setupMenus(villageManager);


        // Command manager
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.getCommandCompletions().registerCompletion("villages", c -> {
            return villageManager.getVillages();
        });
        manager.getCommandCompletions().registerCompletion("menus", c -> {
            return menuManager.getAllMenu();
        });
        manager.setFormat(MessageType.SYNTAX, ChatColor.GRAY, ChatColor.AQUA, ChatColor.DARK_AQUA);


        manager.registerCommand(new AdminVillageCommand(villageManager, menuManager));
        manager.registerCommand(new ClaimVillageCommand(villageManager, menuManager));
        manager.registerCommand(new ListCommand(villageManager));
        manager.registerCommand(new OwnerVillageCommand(villageManager, menuManager));
        manager.registerCommand(new PlayerVillageCommand(villageManager, menuManager));
        manager.registerCommand(new SubClaimVillageCommand(villageManager, menuManager));

        // register listeners and hooks
        registerProtectMode(ConfigVillage.CLAIM_GLOBAL_PROTECT_MODE);

        getLogger().info("Registered " + registerHooks() + " hook(s).");

        InventoryAPI.setup(this);
    }

    private void registerProtectMode(String claim_mode) {
        if (claim_mode.equals("PROTECT")) {
            getLogger().info("Registered " + registerListeners(
                    new EntityListener(this),
                    new PlayerListener(this),
                    new VillageListener(this),
                    new WorldListener(villageManager),
                    new MoveEvent(this),
                    new ConnectListener(this)
            ) + " listener(s).");
        } else if (claim_mode.equals("ROLEPLAY")) {
            getLogger().info("Registered " + registerListeners(
                    new MoveEvent(this),
                    new WorldListener(villageManager),
                    new ConnectListener(this)
            ) + " listener(s).");
        } else {
            getLogger().info("Registered " + registerListeners(
                    new EntityListener(this),
                    new PlayerListener(this),
                    new VillageListener(this),
                    new WorldListener(villageManager),
                    new CuboidEvent(villageManager),
                    new MoveEvent(this)
            ) + " listener(s).");
        }
    }

    public int registerListeners(Listener... listeners) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Arrays.asList(listeners).forEach(listener -> {
            atomicInteger.getAndAdd(1);
            getServer().getPluginManager().registerEvents(listener, this);
        });
        return atomicInteger.get();
    }

    public void registerMessages() {
        Message.setConfiguration(fileManager.getFile(ConfigVillage.LANGUAGE));
        getLogger().info("Registered " + Message.addMissingMessages() + " message(s).");
    }

    public int registerHooks() {
        int index = 0;
        PlaceholderAPIBridge placeholderAPIBridge = new PlaceholderAPIBridge();
        placeholderAPIBridge.setupPlugin();
        if (PlaceholderAPIBridge.hasValidPlugin()) {
            getLogger().info("Successfully hooked into PlaceholderAPI.");
            new PlaceholderAPIHook(this).register();
            index += 1;
        }
        if (EconomyBridge.setupEconomy() && ConfigVillage.ECONOMY_ENABLE) {
            getLogger().info("Successfully hooked into Economy vault.");
            index += 1;
        } else {
            getLogger().warning("Vault with a compatible economy plugin was not found! Icons with a PRICE or commands that give money will not work.");

        }

        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            getLogger().info("Successfully hooked into WorldGuard.");
            index += 1;
        }
        MetricsBridge();
        return index;
    }

    public void MetricsBridge() {
        // All you have to do is adding the following two lines in your onEnable method.
        // You can find the plugin ids of your plugins on the page https://bstats.org/what-is-my-plugin-id
        int pluginId = 11468; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

    }


    @Override
    public void onLoad() {
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            WorldGuardHook.loadWorldGuard(true);
            WORLD_GUARD = true;
            WorldGuardHook.registerFlags();
        } else {
            WORLD_GUARD = false;
        }
    }

    public void onDisable() {
        villageManager.unload();
    }

    public VillageManager getVillageManager() {
        return villageManager;
    }

}
