package com.rainchat.villages;

import com.rainchat.inventoryapi.InventoryAPI;
import com.rainchat.villages.data.VillageExtension;
import com.rainchat.villages.data.config.ConfigVillage;
import com.rainchat.villages.hooks.EconomyBridge;
import com.rainchat.villages.hooks.PlaceholderAPIBridge;
import com.rainchat.villages.hooks.PlaceholderAPIHook;
import com.rainchat.villages.hooks.WorldGuardHook;
import com.rainchat.villages.managers.ExtensionLoader;
import com.rainchat.villages.resources.listeners.*;
import com.rainchat.villages.utilities.general.ResourceLoader;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Villages extends JavaPlugin {

    public static boolean WORLD_GUARD = false;
    private static Villages instance;
    private ResourceLoader resources;
    private VillageImpl villageApi;
    private static ExtensionLoader extLoader;

    public static Villages getInstance() {
        return instance;
    }

    public static void setInstance(Villages instance) {
        Villages.instance = instance;
    }

    public static VillageImpl getAPI() {
        return getInstance().villageApi;
    }

    public static ExtensionLoader getExpansions() {
        return extLoader;
    }

    public void onEnable() {
        instance = this;
        villageApi = new VillageImpl(this);
        resources = new ResourceLoader(this);

        InventoryAPI.setup(this);

        // register listeners and hooks
        registerProtectMode(ConfigVillage.CLAIM_GLOBAL_PROTECT_MODE);
        getLogger().info("Registered " + registerHooks() + " hook(s).");

        // register expansions
        extLoader = new ExtensionLoader(resources.getClassLoader(), new File(getDataFolder() + File.separator + "extension"));
        for (VillageExtension villageExtension : extLoader.loadLocal()) {
            villageExtension.init(this);
        }

        villageApi.getFlagManager().disableFlags();
    }

    private void registerProtectMode(String claim_mode) {
        if (claim_mode.equalsIgnoreCase("PROTECT")) {
            getLogger().info("Registered " + registerListeners(
                    new EntityListener(villageApi.getVillageManage()),
                    new PlayerListener(villageApi.getVillageManage()),
                    new BurnEvent(villageApi.getVillageManage()),
                    new VillageListener(villageApi.getVillageManage()),
                    new WorldListener(villageApi.getVillageManage()),
                    new MoveEvent(villageApi.getVillageManage()),
                    new CuboidEvent(villageApi.getVillageManage()),
                    new ConnectListener(villageApi.getVillageManage())
            ) + " listener(s).");
        } else if (claim_mode.equalsIgnoreCase("ROLEPLAY")) {
            getLogger().info("Registered " + registerListeners(
                    new MoveEvent(villageApi.getVillageManage()),
                    new WorldListener(villageApi.getVillageManage()),
                    new ConnectListener(villageApi.getVillageManage())
            ) + " listener(s).");
        } else {
            getLogger().info("Registered " + registerListeners(
                    new EntityListener(villageApi.getVillageManage()),
                    new PlayerListener(villageApi.getVillageManage()),
                    new VillageListener(villageApi.getVillageManage()),
                    new WorldListener(villageApi.getVillageManage()),
                    new CuboidEvent(villageApi.getVillageManage()),
                    new MoveEvent(villageApi.getVillageManage())
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

    public int registerHooks() {
        int index = 0;
        PlaceholderAPIBridge placeholderAPIBridge = new PlaceholderAPIBridge();
        placeholderAPIBridge.setupPlugin();
        if (PlaceholderAPIBridge.hasValidPlugin()) {
            getLogger().info("Successfully hooked into PlaceholderAPI.");
            new PlaceholderAPIHook(this, villageApi.getVillageManage()).register();
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
        villageApi.getVillageManage().unload();
    }

}
