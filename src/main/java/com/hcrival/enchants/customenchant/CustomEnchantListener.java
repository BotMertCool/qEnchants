package com.hcrival.enchants.customenchant;

import com.hcrival.enchants.Enchants;
import com.hcrival.enchants.event.ArmorEquipEvent;
import com.hcrival.enchants.util.ItemUtil;;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class CustomEnchantListener implements Listener {

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        Player player = event.getPlayer();

        Enchants.getInstance().getCustomEnchantHandler().getExecutorService().execute(() -> {
            // Detect/Remove old enchants
            ItemStack old = event.getOldArmorPiece();

            if (ItemUtil.hasLore(old)) {
                for (String potentialEnchantment : old.getItemMeta().getLore()) {
                    CustomEnchant customEnchant = Enchants.getInstance().getCustomEnchantHandler().getCustomEnchant(event.getType(), potentialEnchantment);
                    if (customEnchant != null) {
                        customEnchant.uneffectPlayer(player);
                    }
                }
            }

            // Detect/Add new enchants
            ItemStack newIS = event.getNewArmorPiece();

            if (ItemUtil.hasLore(newIS)) {
                for (String potentialEnchantment : newIS.getItemMeta().getLore()) {
                    CustomEnchant customEnchant = Enchants.getInstance().getCustomEnchantHandler().getCustomEnchant(event.getType(), potentialEnchantment);
                    if (customEnchant != null) {
                        customEnchant.effectPlayer(player);
                    }
                }
            }
        });
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity().hasMetadata("nohunger")) {
            event.setCancelled(true);
            event.setFoodLevel(20);
            ((Player) event.getEntity()).setFoodLevel(20);
        }
    }

    @EventHandler
    public void onItemDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (player.hasMetadata("nodurability")) {
                int i = 4;
                for (ItemStack itemStack : player.getInventory().getArmorContents()) {
                    i--;
                    if (ItemUtil.hasLore(itemStack)) {
                        for (String potential : itemStack.getItemMeta().getLore()) {
                            CustomEnchant enchant = Enchants.getInstance().getCustomEnchantHandler().getCustomEnchantDisplay(potential);
                            if (enchant != null && enchant.getEffects().contains("NODURABILITY") && player.getInventory().getArmorContents()[i].getDurability() != 0) {
                                player.getInventory().getArmorContents()[i].setDurability((short)0);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().hasMetadata("nodurability")) {
            event.getEntity().removeMetadata("nodurability", Enchants.getInstance());
        }

        if (event.getEntity().hasMetadata("nohunger")) {
            event.getEntity().removeMetadata("nohunger", Enchants.getInstance());
        }
    }
}
