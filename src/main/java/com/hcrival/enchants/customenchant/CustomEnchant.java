package com.hcrival.enchants.customenchant;

import com.hcrival.enchants.Enchants;
import com.hcrival.enchants.event.ArmorType;
import com.hcrival.enchants.util.PotionUtil;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomEnchant {

    private String name;
    private String displayName;

    private ArmorType armorType;

    private List<PotionEffect> potionEffects;

    private List<String> effects;

    public CustomEnchant(String name, String displayName, ArmorType armorType) {
        this.name = name;
        this.displayName = displayName;
        this.armorType = armorType;

        this.potionEffects = new ArrayList<>();

        this.effects = new ArrayList<>();
    }

    public void effectPlayer(Player player) {
        if (!potionEffects.isEmpty()) {
            for (PotionEffect potionEffect : potionEffects) {
                if (player.hasPotionEffect(potionEffect.getType())) {
                    Bukkit.getServer().getScheduler().runTask(Enchants.getInstance(), () -> player.removePotionEffect(potionEffect.getType()));
                }

                Bukkit.getServer().getScheduler().runTask(Enchants.getInstance(), () -> player.addPotionEffect(potionEffect));
            }
        }

        if (!effects.isEmpty()) {
            for (String effect : effects) {
                if (effect.equalsIgnoreCase("NOHUNGER")) {
                    if (player.hasMetadata("nohunger")) {
                        continue;
                    }

                    player.setMetadata("nohunger", new FixedMetadataValue(Enchants.getInstance(), true));
                }

                if (effect.equalsIgnoreCase("NODURABILITY")) {
                    if (player.hasMetadata("nodurability")) {
                        continue;
                    }

                    player.setMetadata("nodurability", new FixedMetadataValue(Enchants.getInstance(), true));
                }
            }
        }
    }

    public void uneffectPlayer(Player player) {
        if (player.hasMetadata("nohunger")) {
            player.removeMetadata("nohunger", Enchants.getInstance());
        }

        if (player.hasMetadata("nodurability")) {
            player.removeMetadata("nodurability", Enchants.getInstance());
        }

        for (PotionEffect potionEffect : getPotionEffects()) {
            if (PotionUtil.doesHavePotionEffect(player, potionEffect)) {
                Bukkit.getServer().getScheduler().runTask(Enchants.getInstance(), () -> player.removePotionEffect(potionEffect.getType()));
            }
        }
    }

}
