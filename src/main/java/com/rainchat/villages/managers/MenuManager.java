package com.rainchat.villages.managers;

import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.utilities.menus.MenuConstructor;
import com.rainchat.villages.utilities.menus.MenuSettings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuManager {

    private static final MenuManager instance = new MenuManager();
    private final FileManager fileManager = FileManager.getInstance();
    public VillageManager villageManager;
    public HashMap<String, MenuSettings> menus;


    public static MenuManager getInstance() {
        return instance;
    }


    public void setupMenus(VillageManager villageManager) {
        this.villageManager = villageManager;
        setupMenus();
    }

    public void setupMenus() {
        this.menus = new HashMap<>();
        for (String menuName : fileManager.getAllCategory("menus")) {
            FileConfiguration file = fileManager.getFile(menuName).getFile();
            menus.put(menuName, new MenuSettings(file));
        }
    }

    public void openMenu(Player player, String name) {
        MenuConstructor menuConstructor = new MenuConstructor(player, menus.get(name), villageManager.getVillage(player));
        menuConstructor.openMenu();
    }

    public void openMenu(Player player, String name, String param) {
        MenuConstructor menuConstructor = new MenuConstructor(player, menus.get(name), villageManager.getVillage(player), param);
        menuConstructor.openMenu();
    }

    public void openMenu(Player player, String name, Village village) {
        MenuConstructor menuConstructor = new MenuConstructor(player, menus.get(name), village);
        menuConstructor.openMenu();
    }

    public List<String> getAllMenu() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, MenuSettings> menu : menus.entrySet()) {
            list.add(menu.getKey());
        }
        return list;
    }
}
