package com.hcrival.enchants.util;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PotionUtil {

    public static boolean doesHavePotionEffect(Player player, PotionEffect potionEffect) {
        if (player.hasPotionEffect(potionEffect.getType())) {
            return getByPotionType(player, potionEffect) != null;
        } else {
            return false;
        }
    }

    public static PotionEffect getByPotionType(Player player, PotionEffect potionEffect) {
        return player.getActivePotionEffects().stream()
                .filter(potionEffect1 -> potionEffect1.getType() == potionEffect.getType())
                .filter(potionEffect1 -> potionEffect1.getAmplifier() == potionEffect.getAmplifier())
                .findFirst().orElse(null);
    }

}
