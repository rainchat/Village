package com.rainchat.villages.utilities.menus;

import com.rainchat.inventoryapi.InventoryAPI;
import com.rainchat.inventoryapi.inventory.invs.HInventory;
import com.rainchat.inventoryapi.inventory.invs.Pagination;
import com.rainchat.inventoryapi.inventory.item.ClickableItem;
import com.rainchat.villages.api.placeholder.replacer.VillageReplacements;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.utilities.Executor;
import com.rainchat.villages.utilities.SpecialActions;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuConstructor {

    public HInventory hInventory;

    public Pagination pagination;

    public String parameter;

    public PaginationItem paginationItem;

    public Village village;

    public Player player;


    public MenuConstructor(Player player, MenuSettings menuSettings, Village village, String parameter) {
        hInventory = InventoryAPI.getInventoryManager()
                .setTitle(menuSettings.title).setCloseable(true)
                .setSize(menuSettings.size).setId("b")
                .create();

        this.parameter = parameter;
        this.player = player;
        this.pagination = hInventory.getPagination();
        this.village = village;

        if (menuSettings.slots != null) {
            this.pagination.setItemSlots(menuSettings.slots);
        } else {
            this.pagination.setItemSlots(Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35));
        }


        if (menuSettings.feelItem != null) {
            hInventory.guiFill(menuSettings.feelItem.build(new VillageReplacements(village)));
        }
        setupItems(menuSettings);
        paginationItem = menuSettings.paginationItem;

        if (paginationItem != null) {
            new SpecialActions(paginationItem, village, this, player).action();
        }
    }

    public MenuConstructor(Player player, MenuSettings menuSettings, Village village) {
        hInventory = InventoryAPI.getInventoryManager()
                .setTitle(menuSettings.title).setCloseable(true)
                .setSize(menuSettings.size).setId("b")
                .create();

        this.player = player;
        this.pagination = hInventory.getPagination();
        this.village = village;

        if (menuSettings.slots != null) {
            this.pagination.setItemSlots(menuSettings.slots);
        } else {
            this.pagination.setItemSlots(Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35));
        }


        if (menuSettings.feelItem != null) {
            hInventory.guiFill(menuSettings.feelItem.build(new VillageReplacements(village)));
        }
        setupItems(menuSettings);
        paginationItem = menuSettings.paginationItem;

        if (paginationItem != null) {
            new SpecialActions(paginationItem, village, this, player).action();
        }
    }


    public void setupItems(MenuSettings menuSettings) {
        List<ClickableItem> clickableItemList = new ArrayList<>();
        for (ClickItem clickItem : menuSettings.itemDataList) {

            ClickableItem item = ClickableItem.empty(clickItem.build(new VillageReplacements(village)));
            item.setClick((event) ->
                    {
                        new Executor(clickItem.getCommands(), player, village, this).start();
                    }
            );
            hInventory.setItem(clickItem.getSlot(), item);
        }
    }


    public void openMenu() {
        hInventory.open(player);
    }
}
