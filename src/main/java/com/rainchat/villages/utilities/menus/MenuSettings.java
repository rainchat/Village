package com.rainchat.villages.utilities.menus;

import com.rainchat.villages.utilities.general.MathUtil;
import com.rainchat.villages.utilities.general.ServerLog;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.logging.Level;

public class MenuSettings {

    // ################# Settings #################
    public static final String
            SLOT = "slot",
            MATERIAL = "material",
            SKULL_TEXTURE = "skull-texture",
            NAME = "name",
            LORE = "lore",
            MODEL_DATE = "madel-date",
            ACTIONS = "actions",
            TYPE = "type";
    private static final String MENU_TITLE = "Menu-settings.title";
    private static final String MENU_SIZE = "Menu-settings.size";
    private static final String PAGINATION_SLOTS = "Menu-settings.pagination-slots";
    public String title;
    public int size = 3;
    public List<Integer> slots;
    public Set<ClickItem> itemDataList = new HashSet<>();
    public PaginationItem paginationItem;
    public ClickItem feelItem;

    //#############################################


    public MenuSettings(FileConfiguration file) {

        title = file.getString(MENU_TITLE);
        size = file.getInt(MENU_SIZE);


        loadItemDatas(file);
    }


    private void loadItemDatas(FileConfiguration file) {
        LinkedList<ClickItem> itemdatas = new LinkedList<>();

        for (String subSectionName : file.getConfigurationSection("Menu-items").getKeys(false)) {

            ConfigurationSection s = file.getConfigurationSection("Menu-items." + subSectionName);
            ClickItem itemdata = new ClickItem();
            itemdata.setSlot(s.getInt(SLOT));
            if (s.isString(MATERIAL)) {
                itemdata
                        .name(s.getString(NAME))
                        .material(Material.valueOf(s.getString(MATERIAL).toUpperCase()));
                if (s.isInt(MODEL_DATE)) {
                    itemdata.setCustomModelDate(s.getInt(MODEL_DATE));
                }
            } else {
                //THROW ERROR
                ServerLog.log(Level.WARNING, "You need at least ID or SKULL TEXTURE to create any item");
            }

            List<String> stringList = s.getStringList(ACTIONS);
            itemdata.setCommands(stringList);
            itemdata.lore(s.getStringList(LORE));
            itemdatas.add(itemdata);
            itemDataList.add(itemdata);
        }

        feelItem = new ClickItem();

        if (file.isString("Menu-settings.fill-items." + MATERIAL)) {
            feelItem
                    .name(file.getString("Menu-settings.fill-items." + NAME))
                    .material(Material.valueOf(file.getString("Menu-settings.fill-items." + MATERIAL).toUpperCase()));
            if (file.isInt("Menu-settings.fill-items." + MODEL_DATE)) {
                feelItem.setCustomModelDate(file.getInt("Menu-settings.fill-items." + MODEL_DATE));
            }
            if (file.isList("Menu-settings.fill-items." + LORE)) {
                feelItem.lore(file.getStringList("Menu-settings.fill-items." + LORE));
            }
        } else {
            feelItem = null;
        }

        if (file.isString(PAGINATION_SLOTS)) {
            String[] strings = file.getString(PAGINATION_SLOTS).split(",");
            slots = new ArrayList<>();
            for (String s : strings) {
                if (MathUtil.isInt(s)) {
                    slots.add(Integer.parseInt(s));
                }
            }
        }

        paginationItem = new PaginationItem();

        if (file.isString("Pagination-items." + NAME)) {
            paginationItem.name(file.getString("Pagination-items." + NAME));
            if (file.isString("Pagination-items." + MATERIAL)) {
                paginationItem.material(Material.valueOf(file.getString("Pagination-items." + MATERIAL).toUpperCase()));
            }
            if (file.isInt("Pagination-items." + MODEL_DATE)) {
                paginationItem.setCustomModelDate(file.getInt("Pagination-items." + MODEL_DATE));
            }
            if (file.isList("Pagination-items." + LORE)) {
                paginationItem.lore(file.getStringList("Pagination-items." + LORE));
            }
            if (file.isList("Pagination-items." + ACTIONS)) {
                List<String> stringList = file.getStringList("Pagination-items." + ACTIONS);
                paginationItem.setCommands(stringList);
            }
            if (file.isString("Pagination-items." + TYPE)) {
                paginationItem.setType(file.getString("Pagination-items." + TYPE));
            } else {
                paginationItem = null;
            }
        } else {
            paginationItem = null;
        }

    }


}
