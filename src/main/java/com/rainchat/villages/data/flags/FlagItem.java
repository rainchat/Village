package com.rainchat.villages.data.flags;

import org.bukkit.Material;

public class FlagItem {
    private String name;
    private Material material;

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
