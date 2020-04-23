package com.hcrival.enchants.gkit;

import com.hcrival.enchants.Enchants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.frozenorb.qlib.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@AllArgsConstructor
public class GKitInventoryBuilder {

    @Getter private static Map<String, Inventory> gkitItems = new HashMap<>();

    private String name;

    private List<String> items;

    public Inventory buildInventory() {
        if (!gkitItems.containsKey(name)) {
            Inventory inventory = Bukkit.createInventory(null, 36, "Inventory for kit");
            for (String item : items) {
                Map<String, String> keyValue = new HashMap<>();

                for (String option : item.split(Pattern.quote(", "))) {
                    String[] kV = option.split(Pattern.quote(":"));
                    keyValue.put(kV[0], kV[1]);
                }

                String[] split = keyValue.get("Item").split(Pattern.quote(";"));

                ItemBuilder itemBuilder = ItemBuilder.of(Material.getMaterial(Integer.parseInt(split[0])));
                int amount = Integer.parseInt(keyValue.get("Amount"));
                if (itemBuilder.build().getType() == Material.ENDER_PEARL && amount < 16) {
                    amount = 16;
                }
                itemBuilder.amount(amount);

                itemBuilder.name(keyValue.get("Name"));

                if (split.length >= 2) {
                    itemBuilder.data(Short.parseShort(split[1]));
                }

                if (keyValue.get("Enchantments") != null) {
                    String[] enchantments = (keyValue.get("Enchantments")).split(Pattern.quote(","));

                    for (String enchantment : enchantments) {
                        String[] idkwhattocallthisimonthevergeofkillingmyself = enchantment.split(Pattern.quote(";"));
                        if (idkwhattocallthisimonthevergeofkillingmyself.length == 2) {
                            itemBuilder.enchant(parseEnchantment(idkwhattocallthisimonthevergeofkillingmyself[0]), Integer.parseInt(idkwhattocallthisimonthevergeofkillingmyself[1]));
                        }
                    }
                }

                if (keyValue.get("CustomEnchantments") != null) {
                    String[] customEnchantments = (keyValue.get("CustomEnchantments")).split(Pattern.quote(","));

                    for (String enchantment : customEnchantments) {
                        itemBuilder.addToLore(Enchants.getInstance().getCustomEnchantHandler().getCustomEnchant(enchantment).getDisplayName());
                    }
                }

                inventory.addItem(itemBuilder.build());

            }
            gkitItems.put(name, inventory);
        }

        return gkitItems.get(name);
    }

    public Enchantment parseEnchantment(String enchantment) {
        switch (enchantment) {
            case "Sharpness":
                return Enchantment.DAMAGE_ALL;
            case "Unbreaking":
                return Enchantment.DURABILITY;
            case "Protection":
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            case "Feather_Falling":
                return Enchantment.PROTECTION_FALL;
            case "Fire_Aspect":
                return Enchantment.FIRE_ASPECT;
            case "Efficiency":
                return Enchantment.DIG_SPEED;
            case "Looting":
                return Enchantment.LOOT_BONUS_MOBS;
            case "Smite":
                return Enchantment.DAMAGE_UNDEAD;
            case "Knockback":
                return Enchantment.KNOCKBACK;
            case "Fortune":
                return Enchantment.LOOT_BONUS_BLOCKS;
            case "Power":
                return Enchantment.ARROW_DAMAGE;
            case "Infinity":
                return Enchantment.ARROW_INFINITE;
            case "Flame":
                return Enchantment.ARROW_FIRE;
            case "ArrowKnockback":
                return Enchantment.ARROW_KNOCKBACK;
            default:
                throw new IllegalArgumentException("Invalid enchant - " + enchantment);
        }
    }

}
