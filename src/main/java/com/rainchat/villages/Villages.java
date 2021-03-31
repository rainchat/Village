package com.rainchat.villages;

import com.google.gson.reflect.TypeToken;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.hooks.PlaceholderAPIHook;
import com.rainchat.villages.managers.ConfigSettings;
import com.rainchat.villages.managers.FileManager;
import com.rainchat.villages.managers.SaveManager;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.resources.commands.VillageCommand;
import com.rainchat.villages.resources.commands.subcommands.*;
import com.rainchat.villages.resources.listeners.*;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Villages extends JavaPlugin {

    private static Villages instance;
    private FileManager fileManager = FileManager.getInstance();
    private VillageManager villageManager;
    private SaveManager saveManager;

    private boolean worldGuard = false;

    public void onEnable() {
        instance = this;

        getLogger().info("Loading Village Data.");
        villageManager = new VillageManager(this);
        villageManager.load(new TypeToken<Set<Village>>() {
        }.getType());

        getLogger().info("Loading Save manager.");
        saveManager = new SaveManager(this);
        saveManager.start();


        String languages = "/messages";
        fileManager.logInfo(true)
                .registerCustomFilesFolder("/messages")
                .registerDefaultGenerateFiles("ru_RU.yml", "/messages", languages)
                .registerDefaultGenerateFiles("en_EN.yml", "/messages", languages)
                .setup(getInstance());


        registerMessages(ConfigSettings.LANGUAGE.getString());

        VillageCommand villageCommand = new VillageCommand(this);
        villageCommand.initialise(
                new AcceptCommand(villageManager),
                new AdminCommands(villageManager),
                new ClaimCommand(this),
                new CheckCommand(this),
                new CreateCommand(this),
                new DenyCommand(villageManager),
                new DisbandCommand(villageManager),
                new HelpCommand(villageCommand),
                new HomeCommand(this),
                new InviteCommand(villageManager),
                new ClaimListCommand(villageManager),
                new KickCommand(villageManager),
                new LeaveCommand(villageManager),
                new PanelCommand(this),
                new RenameCommand(villageManager),
                new SetDescriptionCommand(villageManager),
                new SetHomeCommand(villageManager),
                new SetOwnerCommand(villageManager),
                new UnClaimCommand(villageManager)
        );

        getLogger().info("Registered " + villageCommand.getCommands().size() + " sub-command(s).");

        Objects.requireNonNull(getCommand(villageCommand.toString())).setExecutor(villageCommand);


        registerProtectMode(ConfigSettings.CLAIM_GLOBAL_PROTECT_MODE.getString().toUpperCase());


        getLogger().info("Registered " + registerHooks() + " hook(s).");
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
                    new MoveEvent(this)
            ) + " listener(s).");
        }
    }

    private int registerListeners(Listener... listeners) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Arrays.asList(listeners).forEach(listener -> {
            atomicInteger.getAndAdd(1);
            getServer().getPluginManager().registerEvents(listener, this);
        });
        return atomicInteger.get();
    }

    private void registerMessages(String name) {
        FileConfiguration file = fileManager.getFile(name).getFile();
        Message.setConfiguration(file);

        int index = 0;


        for (Message message : Message.values()) {
            if (message.getList() != null) {
                file.set(message.getPath(), message.getList());
            } else {
                index += 1;
                file.set(message.getPath(), message.getDef());
            }
        }
        FileManager.getInstance().saveFile(name);
        getLogger().info("Registered " + index + " message(s).");
    }


    private int registerHooks() {
        int index = 0;
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().info("Successfully hooked into PlaceholderAPI.");
            new PlaceholderAPIHook(this).register();
            index += 1;
        }

        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            getLogger().info("Successfully hooked into WorldGuard.");
            worldGuard = true;
            index += 1;
        }
        return index;
    }

    public void onDisable() {
        villageManager.unload();
    }

    public VillageManager getVillageManager() {
        return villageManager;
    }

    public static void setInstance(Villages instance) {
        Villages.instance = instance;
    }

    public static Villages getInstance() {
        return instance;
    }

    public boolean isWorldGuard() {
        return worldGuard;
    }

}
