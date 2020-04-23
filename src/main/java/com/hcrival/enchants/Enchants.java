package com.hcrival.enchants;

import com.hcrival.enchants.commands.GKitCommand;
import com.hcrival.enchants.customenchant.CustomEnchantHandler;
import com.hcrival.enchants.event.ArmorListener;
import com.hcrival.enchants.util.MongoDatabase;
import lombok.Getter;
import net.frozenorb.qlib.command.FrozenCommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
public final class Enchants extends JavaPlugin {

    @Getter private static Enchants instance;

    private MongoDatabase mongoDatabase;

    private CustomEnchantHandler customEnchantHandler;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        mongoDatabase = new MongoDatabase(getConfig().getString("database"));

        customEnchantHandler = new CustomEnchantHandler();

        FrozenCommandHandler.registerClass(GKitCommand.class);

        Arrays.asList(
                new ArmorListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }
}
