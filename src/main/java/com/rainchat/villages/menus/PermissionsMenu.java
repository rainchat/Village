package com.rainchat.villages.menus;

import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageRole;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Item;
import com.rainchat.villages.utilities.general.Message;
import com.rainchat.villages.utilities.menus.Menu;
import com.rainchat.villages.utilities.menus.MenuItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.atomic.AtomicInteger;

public class PermissionsMenu extends Menu {

    private final Village village;
    private final VillageManager villageManager;
    private final VillageRole villageRole;

    PermissionsMenu(Plugin plugin, Village village, VillageManager villageManager, VillageRole villageRole) {
        super(plugin, villageRole.getName() + "'s Permissions", 36);
        this.village = village;
        this.villageManager = villageManager;
        this.villageRole = villageRole;
    }

    @Override
    public Menu build() {
        AtomicInteger atomicInteger = new AtomicInteger(-1);

        for (VillagePermission villagePermission : VillagePermission.values()) {
            addItems(new MenuItem(atomicInteger.addAndGet(1),
                    permission(villagePermission, villageRole.hasPermission(villagePermission)),
                    event -> {
                        if (villageRole.hasPermission(villagePermission)) {
                            villageRole.remove(villagePermission);
                        } else {
                            villageRole.add(villagePermission);
                        }
                    }));
        }

        addItems(new MenuItem(31, back(), inventoryClickEvent ->
        {
            inventoryClickEvent.getWhoClicked().closeInventory();
            new RoleMenu(getPlugin(), village, villageManager).build().open((Player) inventoryClickEvent.getWhoClicked());
        }));
        return this;
    }

    private ItemStack permission(VillagePermission villagePermission, boolean enabled) {
        Item item = new Item();
        if (enabled) {
            item.material(Material.LIME_DYE);
            item.name(Message.MENU_ENABLED_TITLE.toString().replace("{0}", villagePermission.name()));
            item.lore(Message.MENU_ENABLED_LORE.toList());
        } else {
            item.material(Material.GRAY_DYE);
            item.name(Message.MENU_DISABLED_TITLE.toString().replace("{0}", villagePermission.name()));
            item.lore(Message.MENU_DISABLED_LORE.toList());
        }
        return item.build();
    }

    private ItemStack back() {
        return new Item()
                .material(Material.PISTON)
                .name(Message.MENU_BACK_TITLE.toString())
                .lore(Message.MENU_BACK_LORE.toList())
                .build();
    }
}
