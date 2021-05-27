package com.rainchat.villages.utilities;


import com.rainchat.inventoryapi.inventory.item.ClickableItem;
import com.rainchat.villages.api.placeholder.replacer.*;
import com.rainchat.villages.data.enums.VillageGlobalPermission;
import com.rainchat.villages.data.enums.VillagePermission;
import com.rainchat.villages.data.village.*;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Item;
import com.rainchat.villages.utilities.menus.MenuConstructor;
import com.rainchat.villages.utilities.menus.PaginationItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpecialActions {

    public Village village;
    public PaginationItem paginationItem;
    public MenuConstructor menuConstructor;
    public Player player;


    public SpecialActions(PaginationItem paginationItem, Village village, MenuConstructor menuConstructor, Player player) {
        this.paginationItem = paginationItem;
        this.village = village;
        this.menuConstructor = menuConstructor;
        this.player = player;
    }


    public void action() {
        switch (paginationItem.getType()) {
            case "roles-menu":
                roleMenu();
                break;
            case "roles-permission-menu":
                rolePermissionMenu();
                break;
            case "global-permission":
                globalPermissions();
                break;
            case "members-menu":
                membersMenu();
                break;
            case "members-role-menu":
                memberRoleMenu();
                break;
            case "claims-menu":
                claimsMenu();
                break;
            case "subclaim-claims-menu":
                subRegionMenu();
                break;
            case "subclaim-menu-members":
                subRegionMembers();
                break;
            case "subclaim-global-permission":
                subRegionGlobalPermission();
                break;
            case "subclaim-role-menu":
                subClaimRoles();
                break;
            case "online-players":
                break;
            default:
                System.out.println("unknown type (" + paginationItem.getType() + ")");

        }
    }


    public void roleMenu() {
        List<ClickableItem> clickableItemList = new ArrayList<>();
        for (VillageRole villageRole : village.getRoles()) {
            List<String> listCommands = new ArrayList<>();
            for (String s : paginationItem.getCommands()) {
                s = Chat.translateRaw(s, new VillageRoleReplacements(villageRole));
                listCommands.add(s);
            }
            ClickableItem item = ClickableItem.empty(paginationItem.build(new VillageRoleReplacements(villageRole), new VillageReplacements(village)));
            item.setClick((event) ->
                    {
                        new Executor(listCommands, player, village, menuConstructor).start();
                    }
            );
            clickableItemList.add(item);
        }

        menuConstructor.pagination.setItems(clickableItemList);
    }


    public void rolePermissionMenu() {
        List<ClickableItem> clickableItemList = new ArrayList<>();

        VillageRole villageRole = village.getRole(menuConstructor.parameter);
        for (VillagePermission villagePermission : VillagePermission.values()) {
            List<String> listCommands = new ArrayList<>();
            for (String s : paginationItem.getCommands()) {
                s = Chat.translateRaw(s);
                listCommands.add(s);
            }
            ClickableItem item = ClickableItem.empty(permission(villagePermission, paginationItem, villageRole.hasPermission(villagePermission)));

            item.setClick((event) ->
                    {
                        if (villageRole.hasPermission(villagePermission)) {
                            villageRole.remove(villagePermission);
                        } else {
                            villageRole.add(villagePermission);
                        }
                        event.getInventory().setItem(event.getSlot(), permission(villagePermission, paginationItem, villageRole.hasPermission(villagePermission)));
                        item.setItem(permission(villagePermission, paginationItem, villageRole.hasPermission(villagePermission)));
                        new Executor(listCommands, player, village, menuConstructor).start();
                    }
            );
            clickableItemList.add(item);
        }

        menuConstructor.pagination.setItems(clickableItemList);
    }

    public void globalPermissions() {
        List<ClickableItem> clickableItemList = new ArrayList<>();
        for (VillageGlobalPermission villageGlobalPermission : VillageGlobalPermission.values()) {
            List<String> listCommands = new ArrayList<>(paginationItem.getCommands());

            ClickableItem item = ClickableItem.empty(permissionGlobal(villageGlobalPermission, paginationItem, village.hasPermission(villageGlobalPermission)));
            item.setClick((event) ->
                    {
                        if (village.hasPermission(villageGlobalPermission)) {
                            village.remove(villageGlobalPermission);
                        } else {
                            village.add(villageGlobalPermission);
                        }
                        event.getInventory().setItem(event.getSlot(), permissionGlobal(villageGlobalPermission, paginationItem, village.hasPermission(villageGlobalPermission)));
                        item.setItem(permissionGlobal(villageGlobalPermission, paginationItem, village.hasPermission(villageGlobalPermission)));
                        new Executor(listCommands, player, village, menuConstructor).start();
                    }
            );
            clickableItemList.add(item);
        }

        menuConstructor.pagination.setItems(clickableItemList);
    }

    public void membersMenu() {
        List<ClickableItem> clickableItemList = new ArrayList<>();

        for (VillageMember villageMember : village.getVillageMembers()) {
            List<String> listCommands = new ArrayList<>();
            for (String s : paginationItem.getCommands()) {
                s = Chat.translateRaw(s, new VillageMemberReplacements(villageMember));
                listCommands.add(s);
            }

            ClickableItem item = ClickableItem.empty(paginationItem.build(new VillageMemberReplacements(villageMember), new VillageReplacements(village)));
            item.setClick((event) ->
                    {
                        new Executor(listCommands, player, village, menuConstructor).start();
                    }
            );
            clickableItemList.add(item);
        }

        menuConstructor.pagination.setItems(clickableItemList);
    }

    public void memberRoleMenu() {
        List<ClickableItem> clickableItemList = new ArrayList<>();
        VillageMember villageMember = village.getMember(UUID.fromString(menuConstructor.parameter));
        for (VillageRole villageRole : village.getRoles()) {
            List<String> listCommands = new ArrayList<>();
            for (String s : paginationItem.getCommands()) {
                s = Chat.translateRaw(s, new VillageRoleReplacements(villageRole));
                listCommands.add(s);
            }

            ClickableItem item = ClickableItem.empty(paginationItem.build(new VillageRoleReplacements(villageRole), new VillageMemberReplacements(villageMember), new VillageReplacements(village)));
            item.setClick((event) ->
                    {
                        event.getWhoClicked().closeInventory();
                        villageMember.setRole(villageRole.getName());
                        new Executor(listCommands, player, village, menuConstructor).start();
                    }
            );
            clickableItemList.add(item);
        }

        menuConstructor.pagination.setItems(clickableItemList);
    }


    public void claimsMenu() {
        List<ClickableItem> clickableItemList = new ArrayList<>();
        for (VillageClaim villageClaim : village.getVillageClaims()) {
            List<String> listCommands = new ArrayList<>();
            for (String s : paginationItem.getCommands()) {
                s = Chat.translateRaw(s, new VillageClaimReplacements(villageClaim));
                listCommands.add(s);
            }

            ClickableItem item = ClickableItem.empty(paginationItem.build(new VillageClaimReplacements(villageClaim), new VillageReplacements(village)));
            item.setClick((event) ->
                    {
                        new Executor(listCommands, player, village, menuConstructor).start();
                    }
            );
            clickableItemList.add(item);
        }

        menuConstructor.pagination.setItems(clickableItemList);
    }

    public void subRegionMenu() {
        List<ClickableItem> clickableItemList = new ArrayList<>();
        for (VillageSubClaim villageSubClaim : village.getVillageSubRegions()) {
            List<String> listCommands = new ArrayList<>();
            for (String s : paginationItem.getCommands()) {
                s = Chat.translateRaw(s, new VillageSubClaimReplacements(villageSubClaim));
                listCommands.add(s);
            }

            ClickableItem item = ClickableItem.empty(paginationItem.build(new VillageSubClaimReplacements(villageSubClaim), new VillageReplacements(village)));
            item.setClick((event) ->
                    {
                        new Executor(listCommands, player, village, menuConstructor).start();
                    }
            );
            clickableItemList.add(item);
        }

        menuConstructor.pagination.setItems(clickableItemList);
    }

    public void subRegionGlobalPermission() {
        List<ClickableItem> clickableItemList = new ArrayList<>();
        VillageSubClaim villageSubClaim = village.getSubClaim(menuConstructor.parameter);
        for (VillageGlobalPermission villageGlobalPermission : VillageGlobalPermission.values()) {
            List<String> listCommands = new ArrayList<>(paginationItem.getCommands());

            ClickableItem item = ClickableItem.empty(permissionGlobal(villageGlobalPermission, paginationItem, villageSubClaim.hasPermission(villageGlobalPermission)));
            item.setClick((event) ->
                    {
                        if (villageSubClaim.hasPermission(villageGlobalPermission)) {
                            villageSubClaim.remove(villageGlobalPermission);
                        } else {
                            villageSubClaim.add(villageGlobalPermission);
                        }
                        event.getInventory().setItem(event.getSlot(), permissionGlobal(villageGlobalPermission, paginationItem, villageSubClaim.hasPermission(villageGlobalPermission)));
                        item.setItem(permissionGlobal(villageGlobalPermission, paginationItem, villageSubClaim.hasPermission(villageGlobalPermission)));

                        new Executor(listCommands, player, village, menuConstructor).start();
                    }
            );
            clickableItemList.add(item);
        }

        menuConstructor.pagination.setItems(clickableItemList);
    }

    public void subRegionMembers() {
        List<ClickableItem> clickableItemList = new ArrayList<>();
        VillageSubClaim villageSubClaim = village.getSubClaim(menuConstructor.parameter);

        for (UUID uuid : villageSubClaim.getMembers()) {

            List<String> listCommands = new ArrayList<>(paginationItem.getCommands());
            for (String s : paginationItem.getCommands()) {
                s = Chat.translateRaw(s, new PlayerReplacements(uuid));
                listCommands.add(s);
            }

            ClickableItem item = ClickableItem.empty(paginationItem.build(new VillageReplacements(village), new PlayerReplacements(Bukkit.getOfflinePlayer(uuid))));
            item.setClick((event) ->
                    {
                        new Executor(listCommands, player, village, menuConstructor).start();
                    }
            );
            clickableItemList.add(item);
        }

        menuConstructor.pagination.setItems(clickableItemList);
    }

    public void subClaimRoles() {
        List<ClickableItem> clickableItemList = new ArrayList<>();
        VillageSubClaim villageSubClaim = village.getSubClaim(menuConstructor.parameter);
        for (VillageRole villageRole : village.getRoles()) {
            List<String> listCommands = new ArrayList<>();
            for (String s : paginationItem.getCommands()) {
                s = Chat.translateRaw(s, new VillageRoleReplacements(villageRole));
                listCommands.add(s);
            }

            ClickableItem item = ClickableItem.empty(paginationItem.build(new VillageRoleReplacements(villageRole), new VillageSubClaimReplacements(villageSubClaim), new VillageReplacements(village)));
            item.setClick((event) ->
                    {
                        event.getWhoClicked().closeInventory();
                        villageSubClaim.setRole(villageRole.getName());
                        new Executor(listCommands, player, village, menuConstructor).start();
                    }
            );
            clickableItemList.add(item);
        }

        menuConstructor.pagination.setItems(clickableItemList);
    }


    private ItemStack permission(VillagePermission villagePermission, PaginationItem paginationItem, boolean enabled) {
        Item item = new Item();
        if (enabled) {
            item.material(Material.LIME_DYE);
            item.name(paginationItem.getName().replace("%Permission%", villagePermission.name()));
            item.lore(paginationItem.getLore());
        } else {
            item.material(Material.GRAY_DYE);
            item.name(paginationItem.getName().replace("%Permission%", villagePermission.name()));
            item.lore(paginationItem.getLore());
        }
        return item.build();
    }

    private ItemStack permissionGlobal(VillageGlobalPermission villageGlobalPermission, PaginationItem paginationItem, boolean enabled) {
        Item item = new Item();
        if (enabled) {
            item.material(Material.LIME_DYE);
            item.name(paginationItem.getName().replace("%Permission%", villageGlobalPermission.name()));
            item.lore(paginationItem.getLore());
        } else {
            item.material(Material.GRAY_DYE);
            item.name(paginationItem.getName().replace("%Permission%", villageGlobalPermission.name()));
            item.lore(paginationItem.getLore());
        }
        return item.build();
    }
}
