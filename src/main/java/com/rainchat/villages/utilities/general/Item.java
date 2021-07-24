package com.rainchat.villages.utilities.general;

import com.cryptomorin.xseries.SkullUtils;
import com.rainchat.rainlib.placeholder.PlaceholderSupply;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Item {

    private String name;
    private String skull_texture;
    private Material material;
    private short durability;
    private String[] lore;
    private int custom_model = -1;
    private Map<Enchantment, Integer> enchantments;


    public String getName() {
        return name;
    }

    public String[] getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public Item name(Message message) {
        this.name = message.toString();
        return this;
    }

    public Item name(String name) {
        this.name = name;
        return this;
    }

    public Item material(Material material) {
        this.material = material;
        return this;
    }

    public Item textureSkull(String skull_texture) {
        this.skull_texture = skull_texture;
        return this;
    }

    public Item setCustomModelDate(int value) {
        this.custom_model = value;
        return this;
    }


    public Item durability(int durability) {
        this.durability = (short) durability;
        return this;
    }

    public Item lore(Message message) {
        this.lore = message.toList().toArray(new String[0]);
        return this;
    }

    public Item lore(List<String> lore) {
        this.lore = lore.toArray(new String[0]);
        return this;
    }

    public Item lore(String[] lore) {
        this.lore = lore;
        return this;
    }


    public Item enchants(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public static ItemStack skullTextured(String base64) {
        UUID id = UUID.nameUUIDFromBytes(base64.getBytes());
        int less = (int) id.getLeastSignificantBits();
        int most = (int) id.getMostSignificantBits();
        return Bukkit.getUnsafe().modifyItemStack(
                new ItemStack(Material.PLAYER_HEAD),
                "{SkullOwner:{Id:[I;" + (less * most) + "," + (less >> 23) + "," + (most / less) + "," + (most * 8731) + "],Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
        );

    }

    public ItemStack buildPlayer(OfflinePlayer offlinePlayer) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        if (itemMeta == null) return null;
        if (offlinePlayer != null) itemMeta.setOwningPlayer(offlinePlayer);
        if (name != null) itemMeta.setDisplayName(Chat.color(name));
        if (enchantments != null) enchantments.forEach(itemStack::addEnchantment);
        if (lore != null) itemMeta.setLore(Arrays.stream(lore).map(Chat::color).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack build() {
        if (material == null) return null;
        ItemStack itemStack;
        if (skull_texture != null) {
            itemStack = skullTextured(skull_texture);
        } else {
            itemStack = new ItemStack(material);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return null;
        if (durability >= 0) itemStack.setDurability(durability);
        if (name != null) itemMeta.setDisplayName(Chat.color(name));
        if (enchantments != null) enchantments.forEach(itemStack::addEnchantment);
        if (lore != null) itemMeta.setLore(Arrays.stream(lore).map(Chat::color).collect(Collectors.toList()));
        if (custom_model != -1) itemMeta.setCustomModelData(custom_model);


        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack build(PlaceholderSupply<?>... replacementSource) {
        if (material == null) return null;
        ItemStack itemStack;
        if (skull_texture != null) {
            itemStack = skullTextured(skull_texture);
        } else {
            itemStack = new ItemStack(material);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return null;
        if (durability >= 0) itemStack.setDurability(durability);
        if (name != null) itemMeta.setDisplayName(Chat.color(Chat.translateRaw(name, replacementSource)));
        if (enchantments != null) enchantments.forEach(itemStack::addEnchantment);
        if (lore != null)
            itemMeta.setLore(Chat.translateRaw(Arrays.stream(lore).map(Chat::color).collect(Collectors.toList()), replacementSource));
        if (custom_model != -1) itemMeta.setCustomModelData(custom_model);


        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}