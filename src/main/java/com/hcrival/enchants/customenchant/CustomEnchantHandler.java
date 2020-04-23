package com.hcrival.enchants.customenchant;

import com.hcrival.enchants.Enchants;
import com.hcrival.enchants.event.ArmorType;
import com.hcrival.enchants.util.ColorUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class CustomEnchantHandler {

    private Enchants instance = Enchants.getInstance();

    private Set<CustomEnchant> customEnchants = new HashSet<>();

    private ExecutorService executorService = Executors.newFixedThreadPool(15);

    public CustomEnchantHandler() {
        //TODO: REWRITE
        for (String key : instance.getConfig().getConfigurationSection("enchants").getKeys(false)) {
            String path = "enchants." + key + ".";

            List<PotionEffect> potions = new ArrayList<>();
            List<String> effects = new ArrayList<>();

            if (instance.getConfig().getConfigurationSection("enchants").getConfigurationSection(key).getConfigurationSection("potions") != null) {
                for (String key2 : instance.getConfig().getConfigurationSection("enchants").getConfigurationSection(key).getConfigurationSection("potions").getKeys(false)) {
                    if (PotionEffectType.getByName(key2) == null) System.out.println(key2);
                    potions.add(new PotionEffect(PotionEffectType.getByName(key2), Integer.MAX_VALUE, instance.getConfig().getInt(path + "potions." + key2)-1));
                }
            }

            if (instance.getConfig().getStringList(path + "effects") != null) {
                effects.addAll(instance.getConfig().getStringList(path + "effects"));
            }

            CustomEnchant customEnchant = new CustomEnchant
                    (
                            instance.getConfig().getString(path + "name"),
                            instance.getConfig().getString(path + "display_name"),
                            ArmorType.valueOf(instance.getConfig().getString(path + "type"))
                    );

            if (!potions.isEmpty()) {
                customEnchant.setPotionEffects(potions);
            }

            if (!effects.isEmpty()) {
                customEnchant.setEffects(effects);
            }

            customEnchants.add(customEnchant);
        }

        Bukkit.getServer().getPluginManager().registerEvents(new CustomEnchantListener(), Enchants.getInstance());
    }

    public CustomEnchant getCustomEnchantDisplay(String display) {
        return customEnchants.stream()
                .filter(customEnchant -> ColorUtil.format(customEnchant.getDisplayName()).equalsIgnoreCase(display))
                .findFirst().orElse(null);
    }


    public CustomEnchant getCustomEnchant(ArmorType armorType, String enchantment) {
        return customEnchants.stream()
                .filter(customEnchant -> customEnchant.getArmorType() == armorType || customEnchant.getArmorType() == ArmorType.ALL)
                .filter(customEnchant -> ColorUtil.format(customEnchant.getDisplayName()).equalsIgnoreCase(enchantment))
                .findFirst().orElse(null);
    }

    public CustomEnchant getCustomEnchant(String name) {
        return customEnchants.stream()
                .filter(customEnchant -> customEnchant.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

}
