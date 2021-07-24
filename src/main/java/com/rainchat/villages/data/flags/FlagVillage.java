package com.rainchat.villages.data.flags;

import org.bukkit.inventory.ItemStack;

public class FlagVillage {

    private String name;
    private String displayName;
    private ItemStack item;

    public FlagVillage(String name, String displayName, ItemStack item) {
        this.name = name;
        this.displayName = displayName;
        this.item = item;
    }

    public FlagVillage(String name, ItemStack item) {
        this.name = name;
        this.displayName = name;
        this.item = item;
    }


    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
