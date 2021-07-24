package com.rainchat.villages.managers;

import com.rainchat.villages.data.config.ConfigFlags;
import com.rainchat.villages.data.flags.FlagVillage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FlagManager {

    private final List<FlagVillage> flagVillages;

    public FlagManager() {
        this.flagVillages = new ArrayList<>();

        flagVillages.add(new FlagVillage("PVP",new ItemStack(Material.LIME_DYE)));
        flagVillages.add(new FlagVillage("EXPLOSIONS",new ItemStack(Material.LIME_DYE)));
        flagVillages.add(new FlagVillage("DAMAGE_ANIMALS",new ItemStack(Material.LIME_DYE)));
        flagVillages.add(new FlagVillage("FIRE_SPREAD",new ItemStack(Material.LIME_DYE)));

    }

    public List<String> getStringFlags() {
        List<String> list = new ArrayList<>();
        for (FlagVillage flagVillage: flagVillages) {
            list.add(flagVillage.getName());
        }
        return list;
    }

    public FlagVillage getFlag(String flag) {
        for (FlagVillage flagVillage: flagVillages) {
            if (flagVillage.getName().equalsIgnoreCase(flag)) {
                return flagVillage;
            }
        }
        return null;
    }

    public List<FlagVillage> getFlags() {
        return flagVillages;
    }

    public void addFlag(FlagVillage flag) {
        this.flagVillages.add(flag);
    }

    public void removeFlag(String flag){
        flagVillages.removeIf(flagV -> flagV.getName().equalsIgnoreCase(flag));
    }

    public boolean contain(String flag){
        for (FlagVillage flagVillage: flagVillages) {
            if (flagVillage.getName().equalsIgnoreCase(flag)) {
                return true;
            }
        }
        return false;
    }

    public void disableFlags() {
        for (String flag: ConfigFlags.DISABLED_FLAGS) {
            removeFlag(flag);
        }
    }
}
