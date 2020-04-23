package com.hcrival.enchants.commands;

import com.hcrival.enchants.Enchants;
import com.hcrival.enchants.gkit.GKitGUI;
import com.hcrival.enchants.gkit.GKitInventoryBuilder;
import com.hcrival.enchants.gkit.GKitPlayer;
import com.hcrival.enchants.util.ItemUtil;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.minecraft.server.v1_7_R4.ItemArmor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GKitCommand {

    @Command(names = { "kit give", "gkit give", "gkits give", "kits give" }, permission = "qenchants.kit.give")
    public static void kitgive(CommandSender sender, @Param(name = "player")Player player, @Param(name = "kit")String kit) {
        if (Enchants.getInstance().getConfig().getConfigurationSection("GKitz").getConfigurationSection(kit) == null) {
            sender.sendMessage(ChatColor.RED + "Invalid kit.");
            return;
        }

        Inventory inventory = new GKitInventoryBuilder(kit, Enchants.getInstance().getConfig().getStringList("GKitz." + kit + ".Items")).buildInventory();
        int armor = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;
            if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemArmor) {
                if (ItemUtil.autoEquip(player, item)) {
                    armor++;
                    continue;
                }
            }

            if (ItemUtil.getRealInventoryCount(player.getInventory()) >= player.getInventory().getSize()) {
                player.getWorld().dropItem(player.getLocation(), item);
                continue;
            }

            player.getInventory().addItem(item);
        }

        if (armor > 0) {
            for (int i = 0; i < armor; i++) {
                if (ItemUtil.getRealInventoryCount(player.getInventory()) >= player.getInventory().getSize()) {
                    player.getWorld().dropItem(player.getLocation(), inventory.getContents()[ItemUtil.getRealInventoryCount(inventory) - 1]);
                } else {
                    player.getInventory().addItem(inventory.getContents()[ItemUtil.getRealInventoryCount(inventory) - 1]);
                }
            }
        }
    }

    @Command(names = { "kit resetcooldown" }, permission = "qenchants.resetcooldown", async = true)
    public static void kitresetcooldown(Player sender, @Param(name = "player")Player player, @Param(name = "kit")String kit) {
        GKitPlayer gKitPlayer = GKitPlayer.getPlayer(player);

        if (gKitPlayer.getKitCooldowns().containsKey(kit)) {
            gKitPlayer.getKitCooldowns().remove(kit);
            gKitPlayer.save();

            sender.sendMessage(ChatColor.GREEN + "Successfully reset the cooldown for " + player.getName() + ".");
        }
    }

    @Command(names = { "gkit", "kit", "gkits", "kits" }, permission = "", async = true)
    public static void gkit(Player sender) {
        new GKitGUI().openMenu(sender);
    }
}
