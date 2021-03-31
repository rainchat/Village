package com.rainchat.villages.menus;

import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillageMember;
import com.rainchat.villages.data.village.VillageRole;
import com.rainchat.villages.managers.FileManager;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Item;
import com.rainchat.villages.utilities.general.Message;
import com.rainchat.villages.utilities.general.ServerLog;
import com.rainchat.villages.utilities.menus.Menu;
import com.rainchat.villages.utilities.menus.MenuItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class MemberRoleMenu extends Menu {


    private final Village village;
    private final VillageManager villageManager;
    private final VillageMember villageMember;


    MemberRoleMenu(Plugin plugin, Village village, VillageManager villageManager, VillageMember villageMember) {
        super(plugin, "RoleMenu", 36);
        this.village = village;
        this.villageManager = villageManager;
        this.villageMember = villageMember;
    }

    @Override
    public Menu build() {
        AtomicInteger atomicInteger = new AtomicInteger(-1);

        FileConfiguration rolesFile = FileManager.Files.ROLES.getFile();
        for (String path : rolesFile.getConfigurationSection("VillageRoles").getKeys(false)) {
            if (!village.hasRole(path)) {
                VillageRole role = new VillageRole(path);
                for (String permission : rolesFile.getStringList("VillageRoles." + path + ".permissions")) {
                    try {
                        role.add(VillagePermission.valueOf(permission.toUpperCase()));
                    } catch (Exception ex) {
                        ServerLog.log(Level.WARNING, "Exception Thrown " + ex);
                    }
                }
                village.addRole(role);

            }
            VillageRole villageRole = village.getRole(path);
            addItems(new MenuItem(atomicInteger.addAndGet(1),
                    role(villageRole),
                    event -> {
                        event.getWhoClicked().closeInventory();
                        villageMember.setRole(villageRole.getName());
                        new MembersMenu(getPlugin(), villageManager, village, 1).build().open((Player) event.getWhoClicked());
                    }));
        }


        addItems(new MenuItem(31, back(), inventoryClickEvent ->
        {
            inventoryClickEvent.getWhoClicked().closeInventory();
            new MembersMenu(getPlugin(), villageManager, village, 1).build().open((Player) inventoryClickEvent.getWhoClicked());
        }));
        return this;
    }


    private ItemStack role(VillageRole villageRole) {
        Item item = new Item();
        List<String> lore = Message.MENU_MEMBER_ROLE_LORE.toList();
        List<String> updated = new ArrayList<>();
        for (String string : lore) {
            string = string.replace("{role}", villageRole.getName());
            updated.add(string);
        }
        item.material(Material.BOOK);
        item.name(Message.MENU_MEMBER_ROLE_TITLE.toString().replace("{role}", villageRole.getName()));
        item.lore(updated);

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
