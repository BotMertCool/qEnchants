package com.hcrival.enchants.util;

import com.hcrival.enchants.event.ArmorEquipEvent;
import com.hcrival.enchants.event.ArmorType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {

    public static boolean hasLore(ItemStack itemStack) {
        return itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getLore() != null && !itemStack.getItemMeta().getLore().isEmpty();
    }

    public static boolean autoEquip(Player player, ItemStack armor) {
        if (armor.getType() == Material.LEATHER_HELMET || armor.getType() == Material.IRON_HELMET || armor.getType() == Material.GOLD_HELMET
            || armor.getType() == Material.CHAINMAIL_HELMET || armor.getType() == Material.DIAMOND_HELMET) {
            if (player.getInventory().getArmorContents()[3] == null || player.getInventory().getArmorContents()[3].getType() == Material.AIR) {
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DRAG, ArmorType.HELMET, player.getInventory().getArmorContents()[3], armor));
                setArmor(player, armor, player.getInventory().getArmorContents()[2], player.getInventory().getArmorContents()[1], player.getInventory().getArmorContents()[0]);
                return true;
            }
        }

        if (armor.getType() == Material.LEATHER_CHESTPLATE || armor.getType() == Material.IRON_CHESTPLATE || armor.getType() == Material.GOLD_CHESTPLATE
                || armor.getType() == Material.CHAINMAIL_CHESTPLATE || armor.getType() == Material.DIAMOND_CHESTPLATE) {
            if (player.getInventory().getArmorContents()[2] == null || player.getInventory().getArmorContents()[2].getType() == Material.AIR) {
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DRAG, ArmorType.CHESTPLATE, player.getInventory().getArmorContents()[2], armor));
                setArmor(player, player.getInventory().getArmorContents()[3], armor, player.getInventory().getArmorContents()[1], player.getInventory().getArmorContents()[0]);
                return true;
            }
        }

        if (armor.getType() == Material.LEATHER_LEGGINGS || armor.getType() == Material.IRON_LEGGINGS || armor.getType() == Material.GOLD_LEGGINGS
                || armor.getType() == Material.CHAINMAIL_LEGGINGS || armor.getType() == Material.DIAMOND_LEGGINGS) {
            if (player.getInventory().getArmorContents()[1] == null || player.getInventory().getArmorContents()[1].getType() == Material.AIR) {
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DRAG, ArmorType.LEGGINGS, player.getInventory().getArmorContents()[1], armor));
                setArmor(player, player.getInventory().getArmorContents()[3], player.getInventory().getArmorContents()[2], armor, player.getInventory().getArmorContents()[0]);
                return true;
            }
        }

        if (armor.getType() == Material.LEATHER_BOOTS || armor.getType() == Material.IRON_BOOTS || armor.getType() == Material.GOLD_BOOTS
                || armor.getType() == Material.CHAINMAIL_BOOTS || armor.getType() == Material.DIAMOND_BOOTS) {
            if (player.getInventory().getArmorContents()[0] == null || player.getInventory().getArmorContents()[0].getType() == Material.AIR) {
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DRAG, ArmorType.BOOTS, player.getInventory().getArmorContents()[0], armor));
                setArmor(player, player.getInventory().getArmorContents()[3], player.getInventory().getArmorContents()[2], player.getInventory().getArmorContents()[1], armor);
                return true;
            }
        }
        return false;
    }

    public static int getRealInventoryCount(Inventory inventory) {
        int count = 0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                count++;
            }
        }
        return count;
    }

    public static void setArmor(Player player, ItemStack helmet, ItemStack chest, ItemStack leg, ItemStack boot) {
        ItemStack[] is = new ItemStack[4];
        is[0] = boot;
        is[1] = leg;
        is[2] = chest;
        is[3] = helmet;
        player.getInventory().setArmorContents(is);
    }
}
